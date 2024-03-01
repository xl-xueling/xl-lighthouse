package com.dtstep.lighthouse.core.storage.engine.hbase;

import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.hbase.HBaseClient;
import com.dtstep.lighthouse.core.hbase.HBaseTableOperator;
import com.dtstep.lighthouse.core.storage.Get;
import com.dtstep.lighthouse.core.storage.Put;
import com.dtstep.lighthouse.core.storage.Result;
import com.dtstep.lighthouse.core.storage.engine.StorageEngine;
import org.apache.hadoop.hbase.MemoryCompactionPolicy;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptor;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.io.encoding.DataBlockEncoding;
import org.apache.hadoop.hbase.regionserver.BloomType;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

public class HBaseStorageEngine implements StorageEngine {

    private static final Logger logger = LoggerFactory.getLogger(HBaseStorageEngine.class);

    private static Admin hBaseAdmin = null;

    private static Compression.Algorithm algorithm = null;

    static {
        try{
            hBaseAdmin = HBaseClient.getConnection().getAdmin();
        }catch (Exception ex){
            logger.error("hbase get admin error!",ex);
        }
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
                case "lzma":
                    algorithm = Compression.Algorithm.LZMA;
                    break;
                default:
                    algorithm = Compression.Algorithm.NONE;
                    break;
            }
        }
    }

    public boolean isNameSpaceExist(String namespace) throws Exception {
        String [] namespaceArr = hBaseAdmin.listNamespaces();
        for(String dbNamespace : namespaceArr){
            if(dbNamespace.equals(namespace)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int createNamespace(String namespace) throws Exception {
        if(isNameSpaceExist(namespace)){
            return 0;
        }
        NamespaceDescriptor namespaceDescriptor = NamespaceDescriptor.create(namespace).build();
        hBaseAdmin.createNamespace(namespaceDescriptor);
        logger.info("create namespace {} success!",namespace);
        return 1;
    }

    @Override
    public int createTable(String tableName) throws Exception {
        int prePartitionsSize = SysConst._DBKeyPrefixArray.length;
        String [] keys = SysConst._DBKeyPrefixArray;
        byte[][] splitKeys = new byte[prePartitionsSize][];
        TreeSet<byte[]> rows = new TreeSet<>(Bytes.BYTES_COMPARATOR);
        for (int i = 0; i < prePartitionsSize; i++) {
            rows.add(Bytes.toBytes(keys[i]));
        }
        Iterator<byte[]> rowKeyIterator = rows.iterator();
        int i=0;
        while (rowKeyIterator.hasNext()) {
            byte[] tempRow = rowKeyIterator.next();
            rowKeyIterator.remove();
            splitKeys[i] = tempRow;
            i++;
        }
        TableDescriptorBuilder tableDescriptor = TableDescriptorBuilder.newBuilder(TableName.valueOf(tableName));
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
        try{
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
        return 0;
    }


    public boolean isTableExist(String tableName) throws Exception {
        TableName tableNameObj = TableName.valueOf(tableName);
        return hBaseAdmin.tableExists(tableNameObj);
    }

    @Override
    public int dropTable(String tableName) throws Exception {
        if(!isTableExist(tableName)){
            return 0;
        }
        TableName table = TableName.valueOf(tableName);
        hBaseAdmin.disableTable(table);
        hBaseAdmin.deleteTable(table);
        return 1;
    }

    @Override
    public int put(String tableName, Put put) throws Exception {
        return 0;
    }

    @Override
    public int puts(String tableName, List<Put> putList) throws Exception {
        return 0;
    }

    @Override
    public Result get(String tableName, Get get) throws Exception {
        return null;
    }

    @Override
    public List<Result> gets(String tableName, List<Get> gets) throws Exception {
        return null;
    }

    @Override
    public List<Result> scan(String tableName, String startRow, String endRow, int limit) throws Exception {
        return null;
    }

    @Override
    public int delete(String tableName, String key) throws Exception {
        return 0;
    }
}
