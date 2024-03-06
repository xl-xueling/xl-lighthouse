package com.dtstep.lighthouse.core.test.engine;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.storage.*;
import com.dtstep.lighthouse.core.storage.engine.StorageEngineProxy;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HBaseStorageEngineTest {

    static {
        try{
            LDPConfig.initWithHomePath("/Users/xueling/lighthouse");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Test
    public void testCreateNamespace() throws Exception {
        StorageEngineProxy.getInstance().createNamespace("ssvs");
    }

    @Test
    public void testCreateTable() throws Exception {
        String tableName = "ssvs:table_abc";
        StorageEngineProxy.getInstance().createTable(tableName);
    }

    @Test
    public void testDeleteTable() throws Exception {
        String tableName = "ssvs:table_abc";
        StorageEngineProxy.getInstance().dropTable(tableName);
    }

    @Test
    public void testIncrement() throws Exception {
        String tableName = "ssvs:table_abc";
        for(int i=0;i<100;i++){
            LdpIncrement ldpIncrement = new LdpIncrement();
            ldpIncrement.setColumn("v");
            ldpIncrement.setStep(101);
            ldpIncrement.setKey("abc");
            ldpIncrement.setTtl(TimeUnit.DAYS.toMillis(10));
            StorageEngineProxy.getInstance().increment(tableName,ldpIncrement);
        }
        for(int i=0;i<100;i++){
            LdpIncrement ldpIncrement = new LdpIncrement();
            ldpIncrement.setColumn("v");
            ldpIncrement.setStep(201);
            ldpIncrement.setKey("def");
            ldpIncrement.setTtl(TimeUnit.DAYS.toMillis(10));
            StorageEngineProxy.getInstance().increment(tableName,ldpIncrement);
        }
    }

    @Test
    public void testGet() throws Exception {
        String tableName = "ssvs:table_abc";
        String key = "abc";
        String column = "v";
        LdpGet ldpGet = new LdpGet();
        ldpGet.setColumn(column);
        ldpGet.setKey(key);
        LdpResult<Long> result = StorageEngineProxy.getInstance().get(tableName,ldpGet,Long.class);
        System.out.println("result:" + JsonUtil.toJSONString(result));
    }

    @Test
    public void testGets() throws Exception{
        String tableName = "ssvs:table_abc";
        LdpGet get1 = new LdpGet();
        get1.setKey("abc");
        get1.setColumn("v");
        LdpGet get2 = new LdpGet();
        get2.setKey("def");
        get2.setColumn("v");
        List<LdpGet> getList = new ArrayList<>();
        getList.add(get1);
        getList.add(get2);
        List<LdpResult<Long>> results = StorageEngineProxy.getInstance().gets(tableName,getList,Long.class);
        System.out.println("results is:" + JsonUtil.toJSONString(results));
    }

    @Test
    public void testDelete() throws Exception {
        String tableName = "ssvs:table_abc";
        String key = "abc";
        StorageEngineProxy.getInstance().delete(tableName,key);
    }

    @Test
    public void testPut() throws Exception {
        String tableName = "ssvs:table_abc";
        String key = "001";
        LdpPut ldpPut = new LdpPut();
        ldpPut.setKey(key);
        ldpPut.setColumn("v");
        ldpPut.setKey(key);
        ldpPut.setData("aaaaa");
        StorageEngineProxy.getInstance().put(tableName,ldpPut);
        LdpGet ldpGet = new LdpGet();
        ldpGet.setKey(key);
        ldpGet.setColumn("v");
        LdpResult<String> ldpResult = StorageEngineProxy.getInstance().get(tableName,ldpGet,String.class);
        System.out.println("ldpResult is:" + JsonUtil.toJSONString(ldpResult));
    }

    @Test
    public void testPuts() throws Exception {
        String tableName = "ssvs:table_abc";
        List<LdpPut> putList = new ArrayList<>();
        for(int i=0;i<10;i++){
            LdpPut ldpPut = new LdpPut();
            ldpPut.setKey("00"+i);
            ldpPut.setColumn("v");
            ldpPut.setData("aaaaa_"+i);
            ldpPut.setTtl(TimeUnit.DAYS.toMillis(10));
            putList.add(ldpPut);
        }
        StorageEngineProxy.getInstance().puts(tableName,putList);
    }

    @Test
    public void testScan() throws Exception {
        String tableName = "ssvs:table_abc";
        String startRow = " 00.";
        String endRow = "00|";
        List<LdpResult<String>> results = StorageEngineProxy.getInstance().scan(tableName,startRow,endRow,10,String.class);
        for(int i=0;i<results.size();i++){
            LdpResult<String> result = results.get(i);
            System.out.println("result is:" + JsonUtil.toJSONString(result));
        }
    }

    @Test
    public void testPutInteger() throws Exception {
        String tableName = "ssvs:table_abc";
        LdpPut ldpPut = new LdpPut();
        ldpPut.setColumn("v");
        ldpPut.setData(1L);
        ldpPut.setKey("103");
        ldpPut.setTtl(TimeUnit.HOURS.toMillis(111));
        StorageEngineProxy.getInstance().put(tableName,ldpPut);
        LdpGet get = new LdpGet();
        get.setKey("103");
        get.setColumn("v");
        LdpResult<Long> result = StorageEngineProxy.getInstance().get(tableName,get,Long.class);
        System.out.println("result is:" + JsonUtil.toJSONString(result));
    }


    @Test
    public void testMaxPut() throws Exception {
        String tableName = "ssvs:table_abc";
        List<LdpPut> ldpPuts = new ArrayList<>();
        LdpPut put1 = new LdpPut();
        put1.setTtl(TimeUnit.DAYS.toMillis(10));
        put1.setColumn("v");
        put1.setData(3L);
        put1.setKey("101");
        ldpPuts.add(put1);

        LdpPut put2 = new LdpPut();
        put2.setTtl(TimeUnit.DAYS.toMillis(10));
        put2.setColumn("v");
        put2.setData(5L);
        put2.setKey("102");
        ldpPuts.add(put2);
        StorageEngineProxy.getInstance().putsWithCompare(tableName, CompareOperator.GREATER,ldpPuts);
    }
}
