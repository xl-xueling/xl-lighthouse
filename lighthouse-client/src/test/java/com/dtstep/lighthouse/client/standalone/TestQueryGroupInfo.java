package com.dtstep.lighthouse.client.standalone;

import com.dtstep.lighthouse.client.LightHouse;
import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.util.JsonUtil;
import org.junit.Test;

public class TestQueryGroupInfo extends StandaloneBaseTest {

    @Test
    public void testQueryGroupInfo() throws Exception {
        String token = "test_scene_behavior_stat";
        GroupVerifyEntity groupVerifyEntity = LightHouse.queryGroupInfo(token);
        System.out.println("groupVerifyInfo:" + JsonUtil.toJSONString(groupVerifyEntity));
    }
}
