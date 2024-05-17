package com.dtstep.lighthouse.client.standalone;

import com.dtstep.lighthouse.client.LightHouse;
import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.util.JsonUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class TestQueryGroupInfo extends StandaloneBaseTest {

    @Test
    public void testQueryGroupInfo() throws Exception {
        String token = "test_scene_behavior_stat";
        GroupVerifyEntity groupVerifyEntity = LightHouse.queryGroupInfo(token);
        System.out.println("groupVerifyInfo:" + JsonUtil.toJSONString(groupVerifyEntity));
    }

    @Test
    public void testStat() throws Exception {
        String token = "j3X:test_standalone";
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("province", ThreadLocalRandom.current().nextInt(10));
        paramMap.put("city",ThreadLocalRandom.current().nextInt(100));
        paramMap.put("score",ThreadLocalRandom.current().nextDouble(1000));
        LightHouse.stat(token,"IRkSd4rEDL75fXRkpUynvt2L5q8Gdwembam3OyCL",paramMap,System.currentTimeMillis());
        Thread.sleep(5000);
    }
}
