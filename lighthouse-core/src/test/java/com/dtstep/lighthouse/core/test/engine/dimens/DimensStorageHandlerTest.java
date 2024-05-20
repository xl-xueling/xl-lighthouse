package com.dtstep.lighthouse.core.test.engine.dimens;

import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.event.DimensBucket;
import com.dtstep.lighthouse.common.modal.Group;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.storage.dimens.impl.DefaultDimensStorageHandler;
import com.dtstep.lighthouse.core.storage.warehouse.WarehouseStorageEngineProxy;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DimensStorageHandlerTest {

    static {
        try{
            LDPConfig.initWithHomePath("/Users/xueling/lighthouse");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Test
    public void testCreateDimensTable() throws Exception {
        WarehouseStorageEngineProxy.getInstance().dropTable(StatConst.DIMENS_STORAGE_TABLE);
        WarehouseStorageEngineProxy.getInstance().createResultTable(StatConst.DIMENS_STORAGE_TABLE);
    }

    @Test
    public void testPut() throws Exception {
        System.out.println("test");
        Group group = new Group();
        String randomId = "test";
        group.setRandomId(randomId);
        List<DimensBucket> bucketList = new ArrayList<>();
        for(int i=0;i<10;i++){
            DimensBucket dimensBucket = new DimensBucket();
            dimensBucket.setDimens("province");
            dimensBucket.setDimensValue("p_" + i);
            dimensBucket.setGroup(group);
            dimensBucket.setTtl(TimeUnit.HOURS.toMillis(3000));
            bucketList.add(dimensBucket);
        }
        new DefaultDimensStorageHandler().put(bucketList);
    }

    @Test
    public void test1() throws Exception{
        String a = "abc1";
        String b = "abc2";
        System.out.println(b.compareTo(a));
    }

    @Test
    public void testQuery() throws Exception {
        Group group = new Group();
        String randomId = "test";
        group.setRandomId(randomId);
        List<String> dimensList = new DefaultDimensStorageHandler().query(group,"province",null,10);
        for(int i=0;i<dimensList.size();i++){
            System.out.println("dimens:" + dimensList.get(i));
        }
    }
}
