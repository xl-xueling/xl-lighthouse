package com.dtstep.lighthouse.core.test.wrapper;

import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.enums.GroupStateEnum;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.wrapper.GroupDBWrapper;
import org.junit.Test;

import java.time.LocalDateTime;

public class GroupDBWrapperTest {

    static {
        try{
            LDPConfig.initWithHomePath("/Users/xueling/lighthouse");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    @Test
    public void testGroupCache() throws Exception {
        int groupId = 100288;
        String token = "test_scene_behavior_stat";
        for(int i=0;i<100000;i++){
            GroupExtEntity groupExtEntity1 = GroupDBWrapper.queryById(groupId);
            System.out.println("groupExtEntity1:" + groupExtEntity1 + ",refreshTime:" + groupExtEntity1.getRefreshTime());
            Thread.sleep(5000);
        }
        Thread.sleep(100000000);
    }


}
