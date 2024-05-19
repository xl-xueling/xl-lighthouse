package com.dtstep.lighthouse.core.test.hbase;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.storage.engine.hbase.HBaseWarehouseStorageEngine;
import com.dtstep.lighthouse.core.test.CoreBaseTest;
import org.apache.hadoop.hbase.client.RegionInfo;
import org.junit.Test;

import java.util.List;

public class TestHbaseAdmin extends CoreBaseTest {

    @Test
    public void testGetRegionInfo() throws Exception {
        String tableName = "cluster_3e710a62_ldp_hbasedb:ldp_stat_1716037618313";
        HBaseWarehouseStorageEngine storageEngine = new HBaseWarehouseStorageEngine();
        List<RegionInfo> list =  storageEngine.getRegionInfo(tableName);
        System.out.println("list size:" + list.size());
        for(int i=0;i<list.size();i++){
            RegionInfo regionInfo = list.get(i);
            System.out.println("regionInfo:" + JsonUtil.toJSONString(regionInfo));
        }
    }

    @Test
    public void testMerge() throws Exception {
        String tableName = "ldp_stat_1715827280737";
        HBaseWarehouseStorageEngine storageEngine = new HBaseWarehouseStorageEngine();
        storageEngine.merge(tableName,5);
    }
}
