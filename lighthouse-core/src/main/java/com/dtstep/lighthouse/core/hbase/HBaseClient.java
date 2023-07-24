package com.dtstep.lighthouse.core.hbase;
/*
 * Copyright (C) 2022-2023 XueLing.雪灵
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.view.StateValue;
import com.dtstep.lighthouse.common.hash.HashUtil;
import com.dtstep.lighthouse.core.lock.RedLock;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;


public final class HBaseClient {

    private static final Logger logger = LoggerFactory.getLogger(HBaseClient.class);

    private static volatile Connection connection = null;

    private static final int BATCH_GET_SIZE = 200;

    public static Connection getConnection() throws Exception{
        if(connection == null || connection.isClosed()){
            synchronized (HBaseClient.class){
                if(connection == null || connection.isClosed()){
                    StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    String zooQuorum = LDPConfig.getVal(LDPConfig.KEY_ZOOKEEPER_QUORUM);
                    String port = LDPConfig.getVal(LDPConfig.KEY_ZOOKEEPER_QUORUM_PORT);
                    Configuration hBaseConfiguration = HBaseConfiguration.create();
                    hBaseConfiguration.set("hbase.zookeeper.quorum",zooQuorum);
                    hBaseConfiguration.set("hbase.zookeeper.property.clientPort",port);
                    hBaseConfiguration.setInt("hbase.client.ipc.pool.size",10);
                    hBaseConfiguration.setInt("hbase.rpc.timeout",180000);
                    hBaseConfiguration.setInt("hbase.client.operation.timeout",240000);
                    hBaseConfiguration.setInt("hbase.client.scanner.timeout.period",180000);
                    connection = ConnectionFactory.createConnection(hBaseConfiguration);
                    logger.info("create hbase connection,thread:{},cost:{}",Thread.currentThread().getName(),stopWatch.getTime());
                }
            }
        }
        return connection;
    }

    private static final Cache<String, Boolean> COLUMN_EXIST_CACHE = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .softValues()
            .maximumSize(50000)
            .build();

    public static void batchIncrement(String metaName, List<Quartet<String,String,Long,Long>> list) throws Exception{
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        try (Table table = getConnection().getTable(getHBaseTableName(metaName))) {
            List<Row> rows = Lists.newArrayListWithCapacity(list.size());
            for (Quartet<String, String, Long, Long> quartet : list) {
                String rowKey = quartet.getValue0();
                long step = quartet.getValue2();
                Increment increment = new Increment(Bytes.toBytes(rowKey));
                increment.addColumn(Bytes.toBytes("f"), Bytes.toBytes(quartet.getValue1()), step);
                increment.setTTL(quartet.getValue3());
                increment.setReturnResults(false);
                increment.setDurability(Durability.SYNC_WAL);
                rows.add(increment);
            }
            table.batch(rows,null);
        } catch (Exception ex) {
            logger.error("batch increment error,metaName:{}!",metaName,ex);
            throw ex;
        }
    }

    @Deprecated
    public static void batchIncrement_2(String metaName, List<Quartet<String,String,Long,Long>> list) throws Exception{
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        BufferedMutatorParams params = new BufferedMutatorParams(getHBaseTableName(metaName)).writeBufferSize(1024 * 1024 * 5);
        params.setWriteBufferPeriodicFlushTimeoutMs(TimeUnit.SECONDS.toMillis(2));
        params.setWriteBufferPeriodicFlushTimerTickMs(100);
        BufferedMutator mutator = connection.getBufferedMutator(params);
        List<Increment> increments = Lists.newArrayListWithCapacity(list.size());
        for (Quartet<String, String, Long, Long> triple : list) {
            String rowKey = triple.getValue0();
            long step = triple.getValue2();
            Increment increment = new Increment(Bytes.toBytes(rowKey));
            increment.addColumn(Bytes.toBytes("f"), Bytes.toBytes(triple.getValue1()), step);
            increment.setTTL(triple.getValue3());
            increment.setReturnResults(false);
            increment.setDurability(Durability.SYNC_WAL);
            increments.add(increment);
        }
        mutator.mutate(increments);
        mutator.close();
    }

    public static boolean existColumn(String metaName,String rowKey,String column) throws Exception{
        String cacheKey = String.format("%s_%s_%s",metaName,rowKey,column);
        Boolean isExist = COLUMN_EXIST_CACHE.getIfPresent(cacheKey);
        if(isExist == null){
            try (Table table = getConnection().getTable(getHBaseTableName(metaName))) {
                Get get = new Get(Bytes.toBytes(rowKey));
                Result result = table.get(get);
                isExist = (result != null && result.containsColumn(Bytes.toBytes("f"), Bytes.toBytes(column)));
                if (isExist) {
                    COLUMN_EXIST_CACHE.put(cacheKey, true);
                }
            } catch (Exception ex) {
                logger.error("exist column error,metaName:{}",metaName,ex);
                throw ex;
            }
        }
        return isExist;
    }

    private static final int batchSalt = 4;

    private static final String LOCK_PREFIX = "PUT_LOCK";

    public static void batchPut(String metaName, CompareOperator operator, List<Quartet<String,String,Long,Long>> list) throws Exception{
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        Map<Long,List<Quartet<String,String,Long,Long>>> map = list.stream().collect(Collectors.groupingBy(x -> HashUtil.BKDRHash(x.getValue0() + "_" + x.getValue1()) % batchSalt));
        for(Long object : map.keySet()){
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            String lockKey = LOCK_PREFIX + "_" + operator.name() + "_" + object;
            boolean isLock = RedLock.tryLock(lockKey,8,3, TimeUnit.MINUTES);
            if(isLock){
                try{
                    List<Quartet<String,String,Long,Long>> subList = map.get(object);
                    List<String> aggregateKeyList = subList.stream().map(x -> x.getValue0() + ";" + x.getValue1()).collect(Collectors.toList());
                    Map<String,StateValue> dbValueMap = multiGetByAggregateKey(metaName,aggregateKeyList);
                    List<Put> puts = Lists.newArrayList();
                    for(Quartet<String,String,Long,Long> quartet : subList){
                        String aggregate = quartet.getValue0() + ";" + quartet.getValue1();
                        if(operator == CompareOperator.GREATER){
                            if(MapUtils.isEmpty(dbValueMap) || !dbValueMap.containsKey(aggregate) || quartet.getValue2() > Long.parseLong(String.valueOf(dbValueMap.get(aggregate).getValue()))){
                                Put put = new Put(Bytes.toBytes(quartet.getValue0()));
                                put.addColumn(Bytes.toBytes("f"), Bytes.toBytes(quartet.getValue1()), Bytes.toBytes(quartet.getValue2()));
                                put.setTTL(quartet.getValue3());
                                put.setDurability(Durability.SYNC_WAL);
                                puts.add(put);
                            }
                        }else if(operator == CompareOperator.LESS){
                            if(MapUtils.isEmpty(dbValueMap) || !dbValueMap.containsKey(aggregate) || quartet.getValue2() < Long.parseLong(String.valueOf(dbValueMap.get(aggregate).getValue()))){
                                Put put = new Put(Bytes.toBytes(quartet.getValue0()));
                                put.addColumn(Bytes.toBytes("f"), Bytes.toBytes(quartet.getValue1()), Bytes.toBytes(quartet.getValue2()));
                                put.setTTL(quartet.getValue3());
                                put.setDurability(Durability.SYNC_WAL);
                                puts.add(put);
                            }
                        }
                    }
                    try (Table table = getConnection().getTable(getHBaseTableName(metaName))) {
                        table.put(puts);
                    }catch (Exception ex) {
                        logger.error("execute batch put error,metaName:{}!",metaName,ex);
                        throw ex;
                    }
                }catch (Exception ex){
                    logger.error("batch put error!",ex);
                }finally {
                    RedLock.unLock(lockKey);
                }
            }else{
                logger.error("try lock failed,thread unable to acquire lock,this batch data may be lost,cost:{}ms!",stopWatch.getTime());
            }
        }
    }


    @Deprecated
    public static void batchPutWithCompare(String metaName, CompareOperator operator,List<Quartet<String,String,Long,Long>> list) throws Exception{
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        try (Table table = getConnection().getTable(getHBaseTableName(metaName))) {
            for (Quartet<String, String, Long, Long> quartet : list) {
                String rowKey = quartet.getValue0();
                String column = quartet.getValue1();
                Object value = quartet.getValue2();
                Put put = new Put(Bytes.toBytes(rowKey));
                put.addColumn(Bytes.toBytes("f"), Bytes.toBytes(column), Bytes.toBytes((long) value));
                put.setTTL(quartet.getValue3());
                put.setDurability(Durability.SYNC_WAL);
                if (existColumn(metaName, rowKey, column)) {
                    table.checkAndPut(Bytes.toBytes(rowKey), Bytes.toBytes("f"), Bytes.toBytes(column), operator, Bytes.toBytes(Long.parseLong(value.toString())), put);
                } else {
                    table.put(put);
                }
            }
        } catch (Exception ex) {
            logger.error("hbase batch put error,metaName:{}!",metaName,ex);
            throw ex;
        }
    }


    public static void batchPut(String metaName, List<Quartet<String,String,Object,Long>> list) throws Exception{
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        try (Table table = getConnection().getTable(getHBaseTableName(metaName))) {
            List<Put> puts = Lists.newArrayListWithCapacity(list.size());
            for (Quartet<String, String, Object, Long> pair : list) {
                String rowKey = pair.getValue0();
                String column = pair.getValue1();
                Object value = pair.getValue2();
                Put put = new Put(Bytes.toBytes(rowKey));
                put.setDurability(Durability.SYNC_WAL);
                if (value.getClass() == String.class) {
                    put.addColumn(Bytes.toBytes("f"), Bytes.toBytes(column), Bytes.toBytes(value.toString()));
                } else if (value.getClass() == Long.class) {
                    put.addColumn(Bytes.toBytes("f"), Bytes.toBytes(column), Bytes.toBytes(Long.parseLong(value.toString())));
                } else if (value.getClass() == Integer.class) {
                    put.addColumn(Bytes.toBytes("f"), Bytes.toBytes(column), Bytes.toBytes(Integer.parseInt(value.toString())));
                }
                put.setTTL(pair.getValue3());
                puts.add(put);
            }
            table.put(puts);
        } catch (Exception ex) {
            logger.error("hbase batch put error,metaName:{}!",metaName,ex);
            throw ex;
        }
    }

    public static void put(String metaName,String rowKey,String column,Object value) throws Exception{
        try (Table table = getConnection().getTable(getHBaseTableName(metaName))) {
            Put put = new Put(Bytes.toBytes(rowKey));
            if (value.getClass() == String.class) {
                put.addColumn(Bytes.toBytes("f"), Bytes.toBytes(column), Bytes.toBytes(value.toString()));
            } else if (value.getClass() == Long.class) {
                put.addColumn(Bytes.toBytes("f"), Bytes.toBytes(column), Bytes.toBytes((long) value));
            } else if (value.getClass() == Integer.class) {
                put.addColumn(Bytes.toBytes("f"), Bytes.toBytes(column), Bytes.toBytes(Long.parseLong(value.toString())));
            }
            put.setDurability(Durability.SYNC_WAL);
            table.put(put);
        }catch (Exception ex){
            logger.error("hbase put error,metaName:{}!",metaName,ex);
            throw ex;
        }
    }

    public static <T> T get(String metaName,String rowKey,String column,Class<T> clazz) throws Exception{
        byte[] b;
        try (Table table = getConnection().getTable(getHBaseTableName(metaName))) {
            Get get = new Get(Bytes.toBytes(rowKey));
            Result result = table.get(get);
            if (result == null) {
                return null;
            }
            if (result.containsColumn(Bytes.toBytes("f"), Bytes.toBytes(column + StatConst.DB_RESULT_STORAGE_EXTEND_COLUMN))) {
                b = result.getValue(Bytes.toBytes("f"), Bytes.toBytes(column + StatConst.DB_RESULT_STORAGE_EXTEND_COLUMN));
            } else {
                b = result.getValue(Bytes.toBytes("f"), Bytes.toBytes(column));
            }
            if (b == null) {
                return null;
            }
        } catch (Exception ex) {
            logger.error("hbase get error!",ex);
            throw ex;
        }
        if(clazz == Long.class || clazz == long.class){
            return clazz.cast(Bytes.toLong(b));
        }else if(clazz == String.class){
            return clazz.cast(Bytes.toString(b));
        }else if(clazz == Integer.class || clazz == int.class){
            return clazz.cast(Bytes.toInt(b));
        }else if(clazz == Double.class || clazz == double.class){
            return clazz.cast(Bytes.toDouble(b));
        }else if(clazz == Float.class || clazz == float.class){
            return clazz.cast(Bytes.toFloat(b));
        }else if(clazz == Boolean.class || clazz == boolean.class){
            return clazz.cast(Bytes.toBoolean(b));
        }
        return null;
    }

    public static HashMap<String,StateValue> multiGetByAggregateKey(String metaName, List<String> aggregateKeyList) {
        return multiGetByAggregateKey(metaName,aggregateKeyList,false);
    }

    public static HashMap<String,StateValue> multiGetByAggregateKey(String metaName, List<String> aggregateKeyList,boolean isSequence) {
        if(CollectionUtils.isEmpty(aggregateKeyList)){
            return null;
        }
        HashMap<String,StateValue> result = null;
        try{
            result = doMultiGetNormalStatics(metaName, aggregateKeyList);
        }catch (Exception ex){
            logger.error("hbase multiGetByAggregateKey error!",ex);
        }
        return result;
    }

    public static HashMap<String, StateValue> doMultiGetSequenceStatics(String metaName, List<String> aggregateKeyList) throws Exception {
        List<Get> getList = Lists.newArrayList();
        for(String rowKey : aggregateKeyList){
            Get get = new Get(Bytes.toBytes(rowKey));
            get.addColumn(Bytes.toBytes("f"), Bytes.toBytes("v"));
            getList.add(get);
        }
        HashMap<String,StateValue> resultHashMap = Maps.newHashMap();
        List<Result> results = getData(metaName,getList);
        HashMap<String,Result> kvMap = new HashMap<>();
        results.forEach(z -> {
            String rowKey = Bytes.toString(z.getRow());
            kvMap.put(rowKey,z);
        });
        for(String aggregateKey : aggregateKeyList){
            Result result = kvMap.get(aggregateKey);
            if(result != null){
                Cell cell = result.getColumnLatestCell(Bytes.toBytes("f"),Bytes.toBytes("v"));
                if(cell != null){
                    byte[] bs = CellUtil.cloneValue(cell);
                    StateValue stateValue = new StateValue();
                    stateValue.setAggregateKey(aggregateKey);
                    stateValue.setLastUpdateTime(cell.getTimestamp());
                    stateValue.setValue(Bytes.toLong(bs));
                    resultHashMap.put(aggregateKey,stateValue);
                }
            }
        }
        return resultHashMap;
    }

    public static HashMap<String, StateValue> doMultiGetNormalStatics(String tableName, List<String> aggregateKeyList) throws Exception {
        List<Get> getList = Lists.newArrayList();
        Map<String,List<Pair<String,String>>> rowKeyMap = aggregateKeyList.stream().map(x -> {
            int tempIndex = x.indexOf(";");
            String rowKey = x.substring(0,tempIndex);
            String delta = x.substring(tempIndex + 1);
            return Pair.with(rowKey,delta);
        }).collect(Collectors.groupingBy(Pair::getValue0));
        for(String rowKey : rowKeyMap.keySet()){
            List<Pair<String,String>> deltaList = rowKeyMap.get(rowKey);
            Get get = new Get(Bytes.toBytes(rowKey));
            for (Pair<String, String> objects : deltaList) {
                String delta = objects.getValue1();
                get.addColumn(Bytes.toBytes("f"), Bytes.toBytes(delta));
            }
            getList.add(get);
        }
        HashMap<String,StateValue> resultHashMap = Maps.newHashMap();
        List<Result> results = getData(tableName,getList);
        HashMap<String,Result> kvMap = new HashMap<>();
        results.forEach(z -> {
            String rowKey = Bytes.toString(z.getRow());
            kvMap.put(rowKey,z);
        });
        for(String aggregateKey : aggregateKeyList){
            int tempIndex = aggregateKey.indexOf(";");
            String rowKey = aggregateKey.substring(0,tempIndex);
            String delta = aggregateKey.substring(tempIndex + 1);
            Result result = kvMap.get(rowKey);
            if(result != null){
                Cell cell = result.getColumnLatestCell(Bytes.toBytes("f"),Bytes.toBytes(delta));
                if(cell != null){
                    byte[] bs = CellUtil.cloneValue(cell);
                    StateValue stateValue = new StateValue();
                    stateValue.setAggregateKey(aggregateKey);
                    stateValue.setLastUpdateTime(cell.getTimestamp());
                    stateValue.setValue(Bytes.toLong(bs));
                    resultHashMap.put(aggregateKey,stateValue);
                }
            }
        }
        return resultHashMap;
    }

    public static LinkedHashMap<String,Result> scanWithMap(String metaName, String startRow, String endRow, int size) throws Exception {
        LinkedHashMap<String,Result> resultMap = new LinkedHashMap<>();
        try(Table table = getConnection().getTable(getHBaseTableName(metaName))){
            Scan scan = new Scan();
            scan.setStartRow(Bytes.toBytes(startRow + "."));
            scan.setStopRow(Bytes.toBytes(endRow));
            scan.setMaxResultSize(size);
            scan.setCaching(20);
            scan.setBatch(100);
            try (ResultScanner scanner = table.getScanner(scan)) {
                int count = 0;
                for (Result result = scanner.next(); result != null; result = scanner.next()) {
                    String rowKey = Bytes.toString(result.getRow());
                    resultMap.put(rowKey, result);
                    count++;
                    if (size != -1 && count >= size) {
                        break;
                    }
                }
            } catch (Exception ex) {
                logger.error("hbase scan error!",ex);
            }
        }catch (Exception ex){
            logger.error("hbase scan error!",ex);
            throw ex;
        }
        return resultMap;
    }

    public static Result[] scan(String metaName, String startRow, String endRow, int size) throws Exception {
        Scan scan = new Scan();
        scan.setStartRow(Bytes.toBytes(startRow));
        scan.setStopRow(Bytes.toBytes(endRow));
        scan.setCaching(50);
        Result[] results = new Result[size];
        try(Table table = getConnection().getTable(getHBaseTableName(metaName))){
            try (ResultScanner scanner = table.getScanner(scan)) {
                Iterator<Result> it = scanner.iterator();
                int count = 0;
                while (it.hasNext()) {
                    Result tempResult = it.next();
                    results[count] = tempResult;
                    count++;
                    if (count >= size) {
                        break;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }catch (Exception ex){
            logger.error("hbase scan error!",ex);
            throw ex;
        }
        return results;
    }

    public static void delete(String metaName,String rowKey) throws Exception{
        try(Table table = getConnection().getTable(getHBaseTableName(metaName))){
            Delete delete = new Delete(Bytes.toBytes(rowKey));
            table.delete(delete);
        }catch (Exception ex){
            logger.error("hbase delete error!",ex);
            throw ex;
        }
    }


    public static void delete(String metaName,List<String> keyList) throws Exception{
        try(Table table = getConnection().getTable(getHBaseTableName(metaName))){
            List<Delete> deleteList = new ArrayList<>();
            for(String key : keyList){
                Delete delete = new Delete(Bytes.toBytes(key));
                deleteList.add(delete);
            }
            table.delete(deleteList);
        }catch (Exception ex){
            logger.error("delete table {} data error!",metaName,ex);
            throw ex;
        }
    }

    private static TableName getHBaseTableName(String metaName) throws Exception {
        String namespace = HBaseTableOperator.getDefaultNamespace();
        return TableName.valueOf(namespace + ":" + metaName);
    }

    private  static final ExecutorService pool = Executors.newFixedThreadPool(5);

    private static List<Result> getData(String metaName, List<Get> keysList) throws Exception{
        int totalSize = keysList.size();
        int loopSize = totalSize % BATCH_GET_SIZE == 0 ? totalSize / BATCH_GET_SIZE : totalSize / BATCH_GET_SIZE + 1;
        ArrayList<Future<Result[]>> results = new ArrayList<>();
        for (int loop = 0; loop < loopSize; loop++)
        {
            int end = Math.min((loop + 1) * BATCH_GET_SIZE, totalSize);
            List<Get> partRowKeys = keysList.subList(loop * BATCH_GET_SIZE, end);
            HBaseGetterThread hBaseGetterThread = new HBaseGetterThread(metaName,partRowKeys);
            synchronized (pool)
            {
                Future<Result[]> result = pool.submit(hBaseGetterThread);
                results.add(result);
            }
        }
        List<Result> totalResult = new ArrayList<>();
        for (Future<Result[]> result : results){
            Result[] temp = result.get();
            if(temp != null){
                Collections.addAll(totalResult, temp);
            }
        }
        return totalResult;
    }


    private static class HBaseGetterThread implements Callable<Result[]> {

        private final String metaName;

        private final List<Get> getList;

        public HBaseGetterThread(String metaName,List<Get> getList){
            this.getList = getList;
            this.metaName = metaName;
        }

        @Override
        public Result[] call() throws Exception {
            try(Table table = getConnection().getTable(getHBaseTableName(metaName))){
                return table.get(getList);
            }catch (Exception ex){
                logger.error("get data from hbase error!",ex);
                throw ex;
            }
        }
    }
}
