package com.dtstep.lighthouse.core.test.stat;

import com.dtstep.lighthouse.client.LightHouse;
import com.dtstep.lighthouse.common.enums.RunningMode;
import org.junit.Test;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class TestClient {

    static {
        try{
            //修改rpc服务注册中心地址,集群模式为一主一从，默认为部署集群的前两个节点IP,使用逗号分割，单机模式为当前节点IP
            //LightHouse.init("10.206.6.11:4061,10.206.6.12:4061");//集群模式初始化
            LightHouse.init("10.206.6.31:4061");//单机模式初始化
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Test
    public void testStandalone() throws Exception{
        System.out.println("init standalone.");
        String token = "3qa:test_stat";
        System.out.println("ok.");
        for(int i = 0;i<1;i++){
            HashMap<String,Object> paramMap = new HashMap<>();
            paramMap.put("province","p1");
            paramMap.put("city","c1");
            LightHouse.stat(token,"RXaVeenRemZRdWxqAICNkoTnynpUWCix6Lzn6e8Z",paramMap,System.currentTimeMillis());
        }
        Thread.sleep(500000);
    }

    @Test
    public void testCaller() throws Exception {
        String token = "_builtin_caller_stat";
        for(int i=0;i<1000;i++){
            HashMap<String,Object> paramMap = new HashMap<>();
            paramMap.put("callerName","11034");
            paramMap.put("function","dataQuery");
            paramMap.put("from", ThreadLocalRandom.current().nextInt(2));
            paramMap.put("status", ThreadLocalRandom.current().nextInt(3));
            paramMap.put("in_bytes",ThreadLocalRandom.current().nextInt(300));
            paramMap.put("out_bytes",ThreadLocalRandom.current().nextInt(30000));
            LightHouse.stat(token,"2l2ipBHOssTzsHsdErKDcarxjU6rKZwo",paramMap,System.currentTimeMillis());
        }
        System.out.println("send ok!");
        Thread.sleep(500000);
    }
}
