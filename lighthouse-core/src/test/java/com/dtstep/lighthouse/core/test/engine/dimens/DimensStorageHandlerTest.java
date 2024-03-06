package com.dtstep.lighthouse.core.test.engine.dimens;

import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.entity.event.DimensBucket;
import com.dtstep.lighthouse.common.modal.Group;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.storage.LdpPut;
import com.dtstep.lighthouse.core.storage.dimens.DefaultDimensStorageHandler;
import com.dtstep.lighthouse.core.storage.engine.StorageEngineProxy;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
        StorageEngineProxy.getInstance().dropTable(StatConst.DIMENS_STORAGE_TABLE);
        StorageEngineProxy.getInstance().createTable(StatConst.DIMENS_STORAGE_TABLE);
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
    public void testQuery() throws Exception {
        Group group = new Group();
        String randomId = "test";
        group.setRandomId(randomId);
        List<String> dimensList = new DefaultDimensStorageHandler().query(group,"province","p_9",10);
        for(int i=0;i<dimensList.size();i++){
            System.out.println("dimens:" + dimensList.get(i));
        }
    }
}
