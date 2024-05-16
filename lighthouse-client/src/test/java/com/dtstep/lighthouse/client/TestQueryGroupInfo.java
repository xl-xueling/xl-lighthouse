package com.dtstep.lighthouse.client;

import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.serializer.KryoSerializer;
import com.dtstep.lighthouse.common.serializer.Serializer;
import com.dtstep.lighthouse.common.util.JsonUtil;
import org.junit.Test;

public class TestQueryGroupInfo extends ClientBaseTest {

    @Test
    public void testQueryGroupInfo() throws Exception {
        String token = "test_scene_behavior_stat";
        GroupVerifyEntity groupVerifyEntity = LightHouse.queryGroupInfo(token);
        Serializer serializer = new KryoSerializer();
        byte[] bytes = serializer.serialize(groupVerifyEntity);
        GroupVerifyEntity result = serializer.deserialize(bytes,GroupVerifyEntity.class);
        System.out.println("result:" + JsonUtil.toJSONString(result));
    }
}
