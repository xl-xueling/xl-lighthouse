package com.dtstep.lighthouse.client.standalone;

import com.dtstep.lighthouse.client.AuxHandler;
import com.dtstep.lighthouse.client.LightHouse;
import com.dtstep.lighthouse.client.rpc.RPCClientProxy;
import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.util.JsonUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class TestQueryGroupInfo extends StandaloneBaseTest {

    @Test
    public void testQueryGroupInfo() throws Exception {
        String token = "test_scene_behavior_stat";
        for(int i=0;i<1000;i++){
            try{
                GroupVerifyEntity groupVerifyEntity = RPCClientProxy.instance().queryGroupInfo(token);
                System.out.println("groupVerifyInfo:" + JsonUtil.toJSONString(groupVerifyEntity));
            }catch (Exception ex){
                ex.printStackTrace();
            }
            Thread.sleep(10000);
        }
        Thread.sleep(TimeUnit.MINUTES.toMillis(50));
    }

    @Test
    public void testStat() throws Exception {
        String token = "HVP:test_standalone";
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("province", ThreadLocalRandom.current().nextInt(10));
        paramMap.put("city",ThreadLocalRandom.current().nextInt(100));
        paramMap.put("score",ThreadLocalRandom.current().nextDouble(1000));
        LightHouse.stat(token,"73V8IcFZu9kckcpc8xOrN2aZLcUrFlu2E67uRcgM",paramMap,System.currentTimeMillis());
        Thread.sleep(5000);
    }
}
