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
        int groupId = 100286;
        GroupExtEntity groupExtEntity = GroupDBWrapper.queryById(groupId);
        System.out.println("groupExtEntity:" + JsonUtil.toJSONString(groupExtEntity));
    }

    @Test
    public void testChangeGroup() throws Exception {
        int groupId = 100286;
        int result = GroupDBWrapper.changeState(groupId, GroupStateEnum.LIMITING, LocalDateTime.now());
        System.out.println("result is:" + result);
    }
}
