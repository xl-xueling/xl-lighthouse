package com.dtstep.lighthouse.core.test.wrapper;

import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.wrapper.GroupDBWrapper;
import com.dtstep.lighthouse.core.wrapper.StatDBWrapper;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

public class StatDBWrapperTest {

    static {
        try{
            LDPConfig.initWithHomePath("/Users/xueling/lighthouse");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    @Test
    public void testStatCache() throws Exception {
        int id = 1100527;
        for(int i=0;i<100000;i++){
            StatExtEntity statExtEntity = StatDBWrapper.queryById(id);
            System.out.println("statExtEntityA:" + statExtEntity + ",refreshTime:" + statExtEntity.getRefreshTime());
            List<StatExtEntity> statExtEntityList = StatDBWrapper.queryRunningListByGroupId(statExtEntity.getGroupId());
            StatExtEntity statExtEntity2 = statExtEntityList.stream().filter(x -> x.getId() == statExtEntity.getId().intValue()).collect(Collectors.toList()).get(0);
            System.out.println("statExtEntityB:" + statExtEntity2 + ",refreshTime:" + statExtEntity2.getRefreshTime());
            Thread.sleep(5000);
        }
        Thread.sleep(100000000);
    }
}
