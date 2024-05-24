package com.dtstep.lighthouse.core.test.engine.warehouse;

import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.core.storage.common.CompareOperator;
import com.dtstep.lighthouse.core.storage.common.LdpIncrement;
import com.dtstep.lighthouse.core.storage.common.LdpPut;
import com.dtstep.lighthouse.core.storage.warehouse.mysql.MySQLWarehouseStorageEngine;
import com.dtstep.lighthouse.core.test.CoreBaseTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MySQLWarehouseEngineTest extends CoreBaseTest {

    private static final MySQLWarehouseStorageEngine mySQLWarehouseStorageEngine = new MySQLWarehouseStorageEngine();

    @Test
    public void testCreateTable() throws Exception{
        String table1 = StatConst.SYSTEM_STAT_RESULT_TABLE;
        mySQLWarehouseStorageEngine.createResultTable(table1);
        String table2 = "ldp_stat_1716037618313";
        mySQLWarehouseStorageEngine.createResultTable(table2);
        String table3 = StatConst.DIMENS_STORAGE_TABLE;
        mySQLWarehouseStorageEngine.createDimensTable(table3);
    }

    @Test
    public void testPut() throws Exception {
        String table = "ldp_stat_aaaaaaaaaa";
        LdpPut ldpPut = new LdpPut();
        ldpPut.setKey(UUID.randomUUID().toString());
        ldpPut.setData(1L);
        ldpPut.setTtl(10000);
        mySQLWarehouseStorageEngine.put(table,ldpPut);
    }

    @Test
    public void testPuts() throws Exception {
        String table = "ldp_stat_aaaaaaaaaa";
        List<LdpPut> putList = new ArrayList<>();
        for(int i=0;i<100;i++){
            LdpPut ldpPut = new LdpPut();
            ldpPut.setKey(UUID.randomUUID().toString());
            ldpPut.setData(1L);
            ldpPut.setTtl(10000);
            putList.add(ldpPut);
        }
        mySQLWarehouseStorageEngine.puts(table,putList);
    }

    @Test
    public void testIncrement() throws Exception {
        String table = "ldp_system_result";
        String key = "abc";
        LdpIncrement ldpIncrement = new LdpIncrement();
        ldpIncrement.setKey(key);
        ldpIncrement.setColumn(null);
        ldpIncrement.setStep(10);
        mySQLWarehouseStorageEngine.increment(table,ldpIncrement);
    }

    @Test
    public void testIncrements() throws Exception {
        String table = "ldp_system_result";
        String key1 = "a1";
        LdpIncrement ldpIncrement1 = new LdpIncrement();
        ldpIncrement1.setKey(key1);
        ldpIncrement1.setColumn(null);
        ldpIncrement1.setStep(10);
        String key2 = "a2";
        LdpIncrement ldpIncrement2 = new LdpIncrement();
        ldpIncrement2.setKey(key2);
        ldpIncrement2.setColumn(null);
        ldpIncrement2.setTtl(100000000);
        ldpIncrement2.setStep(10);
        List<LdpIncrement> ldpIncrements = new ArrayList<>();
        ldpIncrements.add(ldpIncrement1);
        ldpIncrements.add(ldpIncrement2);
        ldpIncrements.add(ldpIncrement1);
        ldpIncrements.add(ldpIncrement2);
        ldpIncrements.add(ldpIncrement1);
        ldpIncrements.add(ldpIncrement2);
        ldpIncrements.add(ldpIncrement1);
        ldpIncrements.add(ldpIncrement2);
        ldpIncrements.add(ldpIncrement1);
        ldpIncrements.add(ldpIncrement2);
        ldpIncrements.add(ldpIncrement1);
        ldpIncrements.add(ldpIncrement2);
        ldpIncrements.add(ldpIncrement1);
        ldpIncrements.add(ldpIncrement2);
        mySQLWarehouseStorageEngine.increments(table,ldpIncrements);
    }

    @Test
    public void testPutWithCompare() throws Exception {
        String table = "ldp_stat_1716037618313";
        String key1 = "a5";
        LdpPut ldpPut = new LdpPut();
        ldpPut.setKey(key1);
        ldpPut.setColumn(null);
        ldpPut.setData(13L);
        ldpPut.setTtl(5000);
        mySQLWarehouseStorageEngine.putsWithCompare(table, CompareOperator.GREATER,List.of(ldpPut));
    }


}
