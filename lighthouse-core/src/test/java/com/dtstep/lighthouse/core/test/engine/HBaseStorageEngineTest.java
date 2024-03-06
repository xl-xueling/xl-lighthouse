package com.dtstep.lighthouse.core.test.engine;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.storage.LdpGet;
import com.dtstep.lighthouse.core.storage.LdpIncrement;
import com.dtstep.lighthouse.core.storage.LdpResult;
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

}
