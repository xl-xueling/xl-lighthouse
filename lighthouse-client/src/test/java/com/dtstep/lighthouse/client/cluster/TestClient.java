package com.dtstep.lighthouse.client.cluster;

import com.dtstep.lighthouse.client.LightHouse;
import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.enums.RunningMode;
import com.dtstep.lighthouse.common.serializer.SerializerProxy;
import com.dtstep.lighthouse.common.util.DateUtil;
import org.junit.Test;

import java.util.List;

public class TestClient extends ClientBaseTest {

    @Test
    public void testStandalone() throws Exception{
        String token = "test_scene_behavior_stat2";
//        System.out.println("ok.");
        GroupVerifyEntity groupVerifyEntity = LightHouse.queryGroupInfo(token);
        System.out.println("groupVrifyEntity:" + groupVerifyEntity);
    }
}
