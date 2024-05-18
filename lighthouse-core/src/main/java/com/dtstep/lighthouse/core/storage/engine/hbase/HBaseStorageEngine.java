package com.dtstep.lighthouse.core.storage.engine.hbase;

import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.hash.HashUtil;
import com.dtstep.lighthouse.common.util.ListUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.lock.RedissonLock;
import com.dtstep.lighthouse.core.storage.*;
import com.dtstep.lighthouse.core.storage.CompareOperator;
import com.dtstep.lighthouse.core.storage.engine.StorageEngine;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.io.encoding.DataBlockEncoding;
import org.apache.hadoop.hbase.regionserver.BloomType;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class HBaseStorageEngine implements StorageEngine {

    private static final Logger logger = LoggerFactory.getLogger(HBaseStorageEngine.class);

    private static Compression.Algorithm algorithm = null;

    static {
        String compression = LDPConfig.getOrDefault(LDPConfig.KEY_DATA_COMPRESSION_TYPE,"zstd",String.class);
        if(StringUtil.isNotEmpty(compression)){
            switch (compression) {
                case "snappy":
                    algorithm = Compression.Algorithm.SNAPPY;
                    break;
                case "bzip2":
                    algorithm = Compression.Algorithm.BZIP2;
                    break;
                case "zstd":
                    algorithm = Compression.Algorithm.ZSTD;
                    break;
                case "gz":
                    algorithm = Compression.Algorithm.GZ;
                    break;
                case "lzo":
                    algorithm = Compression.Algorithm.LZO;
                    break;
                default:
                    algorithm = Compression.Algorithm.NONE;
                    break;
            }
        }
    }

    private static volatile Connection connection = null;

    public static Connection getConnection() throws Exception{
        if(connection == null || connection.isClosed() || connection.isAborted()){
            synchronized (HBaseStorageEngine.class){
                if(connection == null || connection.isClosed() || connection.isAborted()){
                    StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    String zooQuorum = LDPConfig.getVal(LDPConfig.KEY_HBASE_ZOOKEEPER_QUORUM);
                    String port = LDPConfig.getVal(LDPConfig.KEY_HBASE_ZOOKEEPER_QUORUM_PORT);
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

    private boolean isNameSpaceExist(String namespace) throws Exception {
        try(Admin hBaseAdmin = getConnection().getAdmin()){
            String [] namespaceArr = hBaseAdmin.listNamespaces();
            for(String dbNamespace : namespaceArr){
                if(dbNamespace.equals(namespace)){
                    return true;
                }
            }
            return false;
        }catch (Exception ex){
            logger.error("check namespace exist error,namespace:{}",namespace,ex);
            throw ex;
        }
    }

    @Override
    public String getDefaultNamespace() {
        String clusterName = LDPConfig.getVal(LDPConfig.KEY_CLUSTER_ID);
        Validate.notNull(clusterName);
        return String.format("cluster_%s_ldp_hbasedb",clusterName);
    }

    @Override
    public void createNamespaceIfNotExist(String namespace) throws Exception {
        try(Admin hBaseAdmin = getConnection().getAdmin()){
            if(isNameSpaceExist(namespace)){
                return;
            }
            NamespaceDescriptor namespaceDescriptor = NamespaceDescriptor.create(namespace).build();
            hBaseAdmin.createNamespace(namespaceDescriptor);
            logger.info("create namespace {} success!",namespace);
        }catch (Exception ex){
            logger.error("createNamespaceIfNotExist error,namespace:{}",namespace,ex);
            throw ex;
        }
    }

    private TableName getTableName(String tableName){
        if(tableName.contains(":")){
            return TableName.valueOf(tableName);
        }else{
            return TableName.valueOf(String.format("%s:%s",getDefaultNamespace(),tableName));
        }
    }

    private int getDefaultPrePartitionSize() throws Exception {
        try(Admin hBaseAdmin = getConnection().getAdmin()){
            Collection<ServerName> collection = hBaseAdmin.getRegionServers();
            int prePartitionSize = Math.min(collection.size() * 3, SysConst._DBKeyPrefixArray.length);
            logger.info("getDefaultPrePartitionSize,region server size:{},pre-partition size:{}!",collection.size(),prePartitionSize);
            return prePartitionSize;
        }catch (Exception ex){
            logger.error("getDefaultPrePartitionSize error!",ex);
            throw ex;
        }
    }

    @Override
    public void createTable(String tableName) throws Exception {
        int prePartitionsSize = getDefaultPrePartitionSize();
        String [] keys = SysConst._DBKeyPrefixArray;
        List<String> keysList = Arrays.asList(keys);
        List<List<String>> totalGroupKeyList = ListUtil.listPartition(keysList,prePartitionsSize);
        byte[][] splitKeys = new byte[prePartitionsSize][];
        TreeSet<byte[]> rows = new TreeSet<>(Bytes.BYTES_COMPARATOR);
        for (int i = 0; i < prePartitionsSize; i++) {
            String key = totalGroupKeyList.get(i).get(0);
            rows.add(Bytes.toBytes(key));
        }
        Iterator<byte[]> rowKeyIterator = rows.iterator();
        int i=0;
        while (rowKeyIterator.hasNext()) {
            byte[] tempRow = rowKeyIterator.next();
            rowKeyIterator.remove();
            splitKeys[i] = tempRow;
            i++;
        }
        TableDescriptorBuilder tableDescriptor = TableDescriptorBuilder.newBuilder(getTableName(tableName));
        ColumnFamilyDescriptor family = ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes("f")).setMaxVersions(1)
                .setInMemory(false)
                .setBloomFilterType(BloomType.ROW)
                .setCompressTags(true)
                .setTimeToLive((int) TimeUnit.DAYS.toSeconds(30))
                .setCompactionCompressionType(algorithm)
                .setCompressionType(algorithm)
                .setBlocksize(16384)
                .setDataBlockEncoding(DataBlockEncoding.FAST_DIFF)
                .setInMemoryCompaction(MemoryCompactionPolicy.BASIC).build();
        tableDescriptor.setColumnFamily(family);
        try(Admin hBaseAdmin = getConnection().getAdmin()){
            hBaseAdmin.createTable(tableDescriptor.build(),splitKeys);
        }catch (Exception ex){
            logger.error("create hbase table,system error.metaName:{}",tableName,ex);
            throw ex;
        }
        boolean isResult;
        try{
            isResult = isTableExist(tableName);
        }catch (Exception ex){
            logger.error(String.format("create hbase table,check status error.metaName:%s",tableName),ex);
            throw ex;
        }
        if(!isResult) {
            logger.info("create hbase table error,table not created.metaName:{}",tableName);
            throw new Exception("create hbase table error!");
        }
    }

    @Override
    public boolean isTableExist(String tableName) throws Exception {
        try(Admin hBaseAdmin = getConnection().getAdmin()){
            TableName tableNameObj = getTableName(tableName);
            return hBaseAdmin.tableExists(tableNameObj);
        }catch (Exception ex){
            logger.error("check table exist error,tableName:{}",tableName,ex);
            throw ex;
        }
    }

    @Override
    public void dropTable(String tableName) throws Exception {
        if(!isTableExist(tableName)){
            return;
        }
        try(Admin hBaseAdmin = getConnection().getAdmin()){
            TableName table = getTableName(tableName);
            hBaseAdmin.disableTable(table);
            hBaseAdmin.deleteTable(table);
        }catch (Exception ex){
            logger.error("drop table error,tableName:{}",tableName,ex);
            throw ex;
        }
    }

    public List<RegionInfo> getRegionInfo(String tableName) throws Exception {
        try(Admin hBaseAdmin = getConnection().getAdmin()){
            return hBaseAdmin.getRegions(getTableName(tableName));
        }catch (Exception ex){
            logger.error("getRegionInfo error,tableName:{}",tableName,ex);
            throw ex;
        }
    }

    public TableName[] listTables() throws Exception {
        try(Admin hBaseAdmin = getConnection().getAdmin()){
            return hBaseAdmin.listTableNames();
        }catch (Exception ex){
            logger.error("listTables error!",ex);
            throw ex;
        }
    }

    public void merge(String tableName,int targetRegions) throws Exception {
        List<RegionInfo> list = getRegionInfo(tableName);
        if(CollectionUtils.isEmpty(list) || list.size() <= targetRegions){
            logger.info("Merge table:{} regions exit,Current region size is less than target!",tableName);
            return;
        }
        logger.info("Waiting for merge table regions,table:{}",tableName);
        List<List<RegionInfo>> totalGroupLists = ListUtil.listPartition(list,targetRegions);
        try(Admin hBaseAdmin = getConnection().getAdmin()){
            for (int i = 0 ;i < totalGroupLists.size();i++) {
                List<RegionInfo> groupLists = totalGroupLists.get(i);
                if (CollectionUtils.isEmpty(groupLists) || groupLists.size() <= 1) {
                    continue;
                }
                byte[][] bytesArray = new byte[groupLists.size()][];
                for (int n = 0; n < groupLists.size(); n++) {
                    RegionInfo regionInfo = groupLists.get(n);
                    bytesArray[n] = Bytes.toBytes(regionInfo.getEncodedName());
                }
                logger.info("Start to merge group index:{},regions:{}",i,groupLists.stream().map(RegionInfo::getEncodedName).collect(Collectors.joining(",")));
                hBaseAdmin.mergeRegionsAsync(bytesArray, false);
                logger.info("Merge group index:{} completed!",i);
            }
            logger.info("Merge table regions[{}] completed!",tableName);
        }catch (Exception ex){
            logger.error("Merge table regions[{}] failed!",tableName,ex);
            throw ex;
        }
    }

    @Override
    public void increment(String tableName, LdpIncrement ldpIncrement) throws Exception {
        String rowKey = ldpIncrement.getKey();
        String column = ldpIncrement.getColumn();
        long step = ldpIncrement.getStep();
        try (Table table = getConnection().getTable(getTableName(tableName))) {
            Increment increment = new Increment(Bytes.toBytes(rowKey));
            increment.addColumn(Bytes.toBytes("f"), Bytes.toBytes(column), step);
            long ttl = ldpIncrement.getTtl();
            Validate.isTrue(ttl != 0);
            increment.setTTL(ttl);
            increment.setReturnResults(false);
            increment.setDurability(Durability.SYNC_WAL);
            table.increment(increment);
        }catch (Exception ex){
            logger.error("hbase put error,tableName:{}!",tableName,ex);
            throw ex;
        }
    }

    @Override
    public void increments(String tableName, List<LdpIncrement> ldpIncrements) throws Exception {
        if(CollectionUtils.isEmpty(ldpIncrements)){
            return;
        }
        try (Table table = getConnection().getTable(getTableName(tableName))) {
            List<Row> rows = Lists.newArrayListWithCapacity(ldpIncrements.size());
            for (LdpIncrement ldpIncrement : ldpIncrements) {
                String rowKey = ldpIncrement.getKey();
                String column = ldpIncrement.getColumn();
                long step = ldpIncrement.getStep();
                long ttl = ldpIncrement.getTtl();
                Validate.isTrue(ttl != 0);
                Increment increment = new Increment(Bytes.toBytes(rowKey));
                increment.addColumn(Bytes.toBytes("f"), Bytes.toBytes(column), step);
                increment.setTTL(ttl);
                increment.setReturnResults(false);
                increment.setDurability(Durability.SYNC_WAL);
                rows.add(increment);
            }
            table.batch(rows,null);
        } catch (Exception ex) {
            logger.error("batch increment error,tableName:{}!",tableName,ex);
            throw ex;
        }
    }

    @Override
    public void put(String tableName, LdpPut ldpPut) throws Exception {
        Object value = ldpPut.getData();
        String rowKey = ldpPut.getKey();
        String column = ldpPut.getColumn();
        try (Table table = getConnection().getTable(getTableName(tableName))) {
            org.apache.hadoop.hbase.client.Put dbPut = new org.apache.hadoop.hbase.client.Put(Bytes.toBytes(rowKey));
            if (value.getClass() == String.class) {
                dbPut.addColumn(Bytes.toBytes("f"), Bytes.toBytes(column), Bytes.toBytes(value.toString()));
            } else if (value.getClass() == Long.class) {
                dbPut.addColumn(Bytes.toBytes("f"), Bytes.toBytes(column), Bytes.toBytes((Long) value));
            } else {
                throw new IllegalArgumentException(String.format("Current type(%s) not supported!",value.getClass()));
            }
            long ttl = ldpPut.getTtl();
            Validate.isTrue(ttl != 0);
            dbPut.setTTL(ttl);
            dbPut.setDurability(Durability.SYNC_WAL);
            table.put(dbPut);
        }catch (Exception ex){
            logger.error("hbase put error,metaName:{}!",tableName,ex);
            throw ex;
        }
    }

    @Override
    public void puts(String tableName, List<LdpPut> ldpPuts) throws Exception {
        if(CollectionUtils.isEmpty(ldpPuts)){
            return;
        }
        try (Table table = getConnection().getTable(getTableName(tableName))) {
            List<Put> puts = Lists.newArrayListWithCapacity(ldpPuts.size());
            for (LdpPut ldpPut : ldpPuts) {
                String rowKey = ldpPut.getKey();
                String column = ldpPut.getColumn();
                Object value = ldpPut.getData();
                long ttl = ldpPut.getTtl();
                Validate.isTrue(ttl != 0);
                Put put = new Put(Bytes.toBytes(rowKey));
                put.setDurability(Durability.SYNC_WAL);
                if (value.getClass() == String.class) {
                    put.addColumn(Bytes.toBytes("f"), Bytes.toBytes(column), Bytes.toBytes(value.toString()));
                } else if (value.getClass() == Long.class) {
                    put.addColumn(Bytes.toBytes("f"), Bytes.toBytes(column), Bytes.toBytes((Long) value));
                } else {
                    throw new IllegalArgumentException(String.format("Current type(%s) not supported!",value.getClass()));
                }
                put.setTTL(ttl);
                puts.add(put);
            }
            table.put(puts);
        } catch (Exception ex) {
            logger.error("hbase batch put error,tableName:{}!",tableName,ex);
            throw ex;
        }
    }

    @Override
    public <R> LdpResult<R> get(String tableName, LdpGet ldpGet, Class<R> clazz) throws Exception {
        String rowKey = ldpGet.getKey();
        String column = ldpGet.getColumn();
        Result dbResult;
        try (Table table = getConnection().getTable(getTableName(tableName))) {
            Get get = new Get(Bytes.toBytes(rowKey));
            dbResult = table.get(get);
        } catch (Exception ex) {
            logger.error("hbase get error!",ex);
            throw ex;
        }
        Cell cell = dbResult.getColumnLatestCell(Bytes.toBytes("f"),Bytes.toBytes(column));
        LdpResult<R> ldpResult = new LdpResult<>();
        if(cell != null){
            byte[] b = CellUtil.cloneValue(cell);
            long timestamp = cell.getTimestamp();
            R data = null;
            if(clazz == Long.class || clazz == long.class){
                data = clazz.cast(Bytes.toLong(b));
            }else if(clazz == String.class){
                data = clazz.cast(Bytes.toString(b));
            }else if(clazz == Integer.class || clazz == int.class){
                data = clazz.cast(Bytes.toInt(b));
            }else if(clazz == Double.class || clazz == double.class){
                data = clazz.cast(Bytes.toDouble(b));
            }else if(clazz == Float.class || clazz == float.class){
                data = clazz.cast(Bytes.toFloat(b));
            }else if(clazz == Boolean.class || clazz == boolean.class){
                data = clazz.cast(Bytes.toBoolean(b));
            }
            ldpResult.setData(data);
            ldpResult.setTimestamp(timestamp);
        }
        ldpResult.setKey(rowKey);
        ldpResult.setColumn(column);
        return ldpResult;
    }

    @Override
    public <R> List<LdpResult<R>> gets(String tableName, List<LdpGet> ldpGets, Class<R> clazz) throws Exception {
        int totalSize = ldpGets.size();
        int loopSize = totalSize % BATCH_GET_SIZE == 0 ? totalSize / BATCH_GET_SIZE : totalSize / BATCH_GET_SIZE + 1;
        ArrayList<Future<List<LdpResult<R>>>> results = new ArrayList<>();
        for (int loop = 0; loop < loopSize; loop++)
        {
            int end = Math.min((loop + 1) * BATCH_GET_SIZE, totalSize);
            List<LdpGet> partGets = ldpGets.subList(loop * BATCH_GET_SIZE, end);
            HBaseGetterThread<R> hBaseGetterThread = new HBaseGetterThread<>(getTableName(tableName),partGets,clazz);
            synchronized (pool)
            {
                Future<List<LdpResult<R>>> result = pool.submit(hBaseGetterThread);
                results.add(result);
            }
        }
        List<LdpResult<R>> totalResult = new ArrayList<>();
        for (Future<List<LdpResult<R>>> subResults : results){
            List<LdpResult<R>> ldpResults = subResults.get();
            if(CollectionUtils.isNotEmpty(ldpResults)){
               totalResult.addAll(ldpResults);
            }
        }
        return totalResult;
    }


    @Override
    public <R> List<LdpResult<R>> scan(String tableName, String startRow, String endRow, int limit,Class<R> clazz) throws Exception {
        List<LdpResult<R>> resultList = new ArrayList<>();
        try(Table table = getConnection().getTable(getTableName(tableName))){
            Scan scan = new Scan();
            scan.setStartRow(Bytes.toBytes(startRow+"."));
            scan.setStopRow(Bytes.toBytes(endRow));
            scan.setMaxResultSize(limit);
            scan.setCaching(20);
            scan.setBatch(100);
            try (ResultScanner scanner = table.getScanner(scan)) {
                int count = 0;
                for (Result dbResult = scanner.next(); dbResult != null; dbResult = scanner.next()) {
                    String rowKey = Bytes.toString(dbResult.getRow());
                    LdpResult<R> ldpResult = new LdpResult<>();
                    Cell cell = dbResult.getColumnLatestCell(Bytes.toBytes("f"),Bytes.toBytes("v"));
                    if(cell != null){
                        byte[] b = CellUtil.cloneValue(cell);
                        long timestamp = cell.getTimestamp();
                        R data = null;
                        if(clazz == Long.class || clazz == long.class){
                            data = clazz.cast(Bytes.toLong(b));
                        }else if(clazz == String.class){
                            data = clazz.cast(Bytes.toString(b));
                        }else if(clazz == Integer.class || clazz == int.class){
                            data = clazz.cast(Bytes.toInt(b));
                        }else if(clazz == Double.class || clazz == double.class){
                            data = clazz.cast(Bytes.toDouble(b));
                        }else if(clazz == Float.class || clazz == float.class){
                            data = clazz.cast(Bytes.toFloat(b));
                        }else if(clazz == Boolean.class || clazz == boolean.class){
                            data = clazz.cast(Bytes.toBoolean(b));
                        }
                        ldpResult.setData(data);
                        ldpResult.setTimestamp(timestamp);
                    }
                    ldpResult.setKey(rowKey);
                    count++;
                    if (limit != -1 && count > limit) {
                        break;
                    }
                    resultList.add(ldpResult);
                }
            } catch (Exception ex) {
                logger.error("hbase scan error!",ex);
            }
        }catch (Exception ex){
            logger.error("hbase scan error!",ex);
            throw ex;
        }
        return resultList;
    }

    @Override
    public void delete(String tableName, String key) throws Exception {
        try(Table table = getConnection().getTable(getTableName(tableName))){
            Delete delete = new Delete(Bytes.toBytes(key));
            table.delete(delete);
        }catch (Exception ex){
            logger.error("delete table {} data error!",tableName,ex);
            throw ex;
        }
    }

    private static final int batchSalt = 4;

    private static final String COMPARE_PUT_LOCK_PREFIX = "COMPARE_PUT_LOCK";

    @Override
    public void putsWithCompare(String tableName, CompareOperator compareOperator, List<LdpPut> ldpPuts) throws Exception {
        if(CollectionUtils.isEmpty(ldpPuts)){
            return;
        }
        Map<Long,List<LdpPut>> map = ldpPuts.stream().collect(Collectors.groupingBy(x -> HashUtil.BKDRHash(x.getKey() + "_" + x.getColumn()) % batchSalt));
        for(Long object : map.keySet()){
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            String lockKey = COMPARE_PUT_LOCK_PREFIX + "_" + compareOperator + "_" + object;
            boolean isLock = RedissonLock.tryLock(lockKey,8,3, TimeUnit.MINUTES);
            if(isLock){
                try{
                    List<LdpPut> subList = map.get(object);
                    List<LdpGet> getList = new ArrayList<>();
                    for(LdpPut ldpPut:subList){
                        LdpGet ldpGet = new LdpGet();
                        ldpGet.setKey(ldpPut.getKey());
                        ldpGet.setColumn(ldpPut.getColumn());
                        getList.add(ldpGet);
                    }
                    List<LdpResult<Long>> dbResults = gets(tableName,getList,Long.class);
                    Map<String,Long> dbValueMap = null;
                    if(CollectionUtils.isNotEmpty(dbResults)){
                        dbValueMap = dbResults.stream().filter(x -> x.getData() != null).collect(Collectors.toMap(x -> x.getKey() + ";" + x.getColumn(), LdpResult::getData));
                    }
                    List<Put> puts = Lists.newArrayList();
                    for(LdpPut ldpPut : subList){
                        String rowKey = ldpPut.getKey();
                        String column = ldpPut.getColumn();
                        Object value = ldpPut.getData();
                        long ttl = ldpPut.getTtl();
                        Validate.isTrue(ttl != 0);
                        String aggregateKey = rowKey + ";" + column;
                        if(compareOperator == CompareOperator.GREATER){
                            if(MapUtils.isEmpty(dbValueMap) || !dbValueMap.containsKey(aggregateKey) || (Long)ldpPut.getData() > dbValueMap.get(aggregateKey)){
                                Put put = new Put(Bytes.toBytes(rowKey));
                                if (value.getClass() == String.class) {
                                    put.addColumn(Bytes.toBytes("f"), Bytes.toBytes(column), Bytes.toBytes(value.toString()));
                                } else if (value.getClass() == Long.class) {
                                    put.addColumn(Bytes.toBytes("f"), Bytes.toBytes(column), Bytes.toBytes((Long) value));
                                } else {
                                    throw new IllegalArgumentException(String.format("Current type(%s) not supported!",value.getClass()));
                                }
                                put.setTTL(ttl);
                                put.setDurability(Durability.SYNC_WAL);
                                puts.add(put);
                            }
                        }else{
                            if(MapUtils.isEmpty(dbValueMap) || !dbValueMap.containsKey(aggregateKey) || (Long)ldpPut.getData() < dbValueMap.get(aggregateKey)){
                                Put put = new Put(Bytes.toBytes(rowKey));
                                if (value.getClass() == String.class) {
                                    put.addColumn(Bytes.toBytes("f"), Bytes.toBytes(column), Bytes.toBytes(value.toString()));
                                } else if (value.getClass() == Long.class) {
                                    put.addColumn(Bytes.toBytes("f"), Bytes.toBytes(column), Bytes.toBytes((Long) value));
                                } else {
                                    throw new IllegalArgumentException(String.format("Current type(%s) not supported!",value.getClass()));
                                }
                                put.setTTL(ttl);
                                put.setDurability(Durability.SYNC_WAL);
                                puts.add(put);
                            }
                        }
                    }
                    try (Table table = getConnection().getTable(getTableName(tableName))) {
                        table.put(puts);
                    }catch (Exception ex) {
                        logger.error("execute batch put error,tableName:{}!",tableName,ex);
                        throw ex;
                    }
                }catch (Exception ex){
                    logger.error("batch put error!",ex);
                }finally {
                    RedissonLock.unLock(lockKey);
                }
            }else{
                logger.error("try lock failed,thread unable to acquire lock,this batch data may be lost,cost:{}ms!",stopWatch.getTime());
            }
        }
    }

    private  static final ExecutorService pool = Executors.newFixedThreadPool(5);

    private static final int BATCH_GET_SIZE = 200;

    private static <R> List<LdpResult<R>> partGets(TableName tableName, List<LdpGet> ldpGets, Class<R> clazz) throws Exception {
        List<Get> getList = new ArrayList<>();
        for(LdpGet ldpGet : ldpGets){
            String rowKey = ldpGet.getKey();
            String column = ldpGet.getColumn();
            Get get = new Get(Bytes.toBytes(rowKey));
            get.addColumn(Bytes.toBytes("f"),Bytes.toBytes(column));
            getList.add(get);
        }
        Result[] dbResults;
        try (Table table = getConnection().getTable(tableName)) {
            dbResults = table.get(getList);
        } catch (Exception ex) {
            logger.error("hbase get error!",ex);
            throw ex;
        }
        List<LdpResult<R>> resultList = new ArrayList<>();
        for(int i=0;i<dbResults.length;i++){
            String column = ldpGets.get(i).getColumn();
            String key = ldpGets.get(i).getKey();
            Result dbResult = dbResults[i];
            LdpResult<R> ldpResult;
            if(dbResult != null){
                ldpResult = new LdpResult<>();
                Cell cell = dbResult.getColumnLatestCell(Bytes.toBytes("f"),Bytes.toBytes(column));
                if(cell != null){
                    byte[] b = CellUtil.cloneValue(cell);
                    long timestamp = cell.getTimestamp();
                    R data = null;
                    if(clazz == Long.class || clazz == long.class){
                        data = clazz.cast(Bytes.toLong(b));
                    }else if(clazz == String.class){
                        data = clazz.cast(Bytes.toString(b));
                    }else if(clazz == Integer.class || clazz == int.class){
                        data = clazz.cast(Bytes.toInt(b));
                    }else if(clazz == Double.class || clazz == double.class){
                        data = clazz.cast(Bytes.toDouble(b));
                    }else if(clazz == Float.class || clazz == float.class){
                        data = clazz.cast(Bytes.toFloat(b));
                    }else if(clazz == Boolean.class || clazz == boolean.class){
                        data = clazz.cast(Bytes.toBoolean(b));
                    }
                    ldpResult.setData(data);
                    ldpResult.setTimestamp(timestamp);
                }
                ldpResult.setKey(key);
                ldpResult.setColumn(column);
                resultList.add(ldpResult);
            }
        }
        return resultList;
    }


    @Override
    public long getMaxRecordSize() {
        return Long.MAX_VALUE;
    }

    @Override
    public long getMaxContentSize() {
        return Long.MAX_VALUE;
    }

    @Override
    public long getMaxTimeInterval() {
        return TimeUnit.DAYS.toSeconds(90);
    }

    @Override
    public long getRecordSize(String tableName) {
        return 0;
    }

    @Override
    public long getContentSize(String tableName) {
        return 0;
    }

    private static class HBaseGetterThread<R> implements Callable<List<LdpResult<R>>> {

        private final TableName tableName;

        private final List<LdpGet> getList;

        private final Class<R> clazz;

        public HBaseGetterThread(TableName tableName,List<LdpGet> getList,Class<R> clazz){
            this.getList = getList;
            this.tableName = tableName;
            this.clazz = clazz;
        }

        @Override
        public List<LdpResult<R>> call() throws Exception {
            return partGets(tableName,getList,clazz);
        }
    }
}
