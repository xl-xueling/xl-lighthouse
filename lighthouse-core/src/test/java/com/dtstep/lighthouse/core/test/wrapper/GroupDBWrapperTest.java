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
    public void testQueryGroup() throws Exception {
        int groupId = 100288;
        GroupExtEntity groupExtEntity = GroupDBWrapper.queryById(groupId);
        String token = "test_scene_behavior_stat";
        GroupExtEntity groupExtEntity3 = GroupDBWrapper.queryByToken(token);
        System.out.println("groupExtEntity:" + JsonUtil.toJSONString(groupExtEntity));
        Thread.sleep(100000000);
    }


}
