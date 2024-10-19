package com.dtstep.lighthouse.core.test.api;

import com.dtstep.lighthouse.client.LightHouse;
import com.dtstep.lighthouse.common.random.RandomID;
import org.junit.Test;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class RpcAPITest {

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
    public void orderStat() throws Exception {
        long t = System.currentTimeMillis();
        for(int i=0;i<81657;i++){
            HashMap<String,Object> paramMap = new HashMap<>();
            paramMap.put("order_id", RandomID.id(6));
            paramMap.put("biz", RandomID.id(2));
            paramMap.put("user_id", RandomID.id(6));
            Double d = ThreadLocalRandom.current().nextDouble(1000);
            paramMap.put("amount",String.format("%.3f", d));//防止上面随机数出现科学计数法
            LightHouse.stat("N4C:test_order","mfbWuKc17e8hGNwGRlR2JGSfS2GgvqD0wIxjMuqm",paramMap,t);
        }
        System.out.println("send ok!");
        Thread.sleep(30000);//client为异步发送，防止进程结束时内存中部分消息没有发送出去
    }
}
