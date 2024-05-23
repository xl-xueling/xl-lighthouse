package com.dtstep.lighthouse.client.cluster;

import com.dtstep.lighthouse.client.LightHouse;
import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.enums.RunningMode;
import com.dtstep.lighthouse.common.random.RandomID;
import com.dtstep.lighthouse.common.serializer.SerializerProxy;
import com.dtstep.lighthouse.common.util.DateUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class TestClient extends ClientBaseTest {

    @Test
    public void testStandalone() throws Exception{
        String token = "test_scene_behavior_stat";
        long t = System.currentTimeMillis();
        for(int i = 0;i<100;i++){
            //修改统计组参数值、Token和秘钥
            Map<String,Object> map = new HashMap<>();
            map.put("uid", RandomID.id(6));
            map.put("province", ThreadLocalRandom.current().nextInt(10));
            Double d = ThreadLocalRandom.current().nextDouble(1000);
            map.put("stay_time",String.format("%.3f", d));//防止上面随机数出现科学计数法
            LightHouse.stat(token,"7G13E6ssmGSGO7aFQgbE0VhqD2bizmkv09fIFxlj",map,t);
        }
        System.out.println("send ok.");
        Thread.sleep(3000000);
    }
}
