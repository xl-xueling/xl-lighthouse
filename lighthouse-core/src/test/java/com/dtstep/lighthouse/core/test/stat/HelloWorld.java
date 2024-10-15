package com.dtstep.lighthouse.core.test.stat;

import com.dtstep.lighthouse.client.LightHouse;
import com.dtstep.lighthouse.common.random.RandomID;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * 为了测试方便，在Web服务中依次创建(统计工程 -> 统计组 -> 统计项)后，您可以直接下载工程代码，然后使用com.dtstep.lighthouse.core.test.stat.HelloWorld中的单元测试方法，记得修改RPC服务IP地址和统计组的Token及秘钥，运行即可！
 *
 * Web端页面更新约有1分钟左右延时，请耐心等待！
 *
 * 使用过程中如有问题，及时反馈，本人一定第一时间响应~
 *
 */
public class HelloWorld {

    static {
        try{
            //修改rpc服务注册中心地址,集群模式为一主一从，默认为部署集群的前两个节点IP,使用逗号分割，单机模式为当前节点IP
            //LightHouse.init("10.206.6.11:4061,10.206.6.12:4061");//集群模式初始化
            LightHouse.init("10.206.6.12:4061");//单机模式初始化
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Test
    public void helloWorld() throws Exception {
        long t = System.currentTimeMillis();
        for(int i = 0;i<100000;i++){
            //修改统计组参数值、Token和秘钥
            Map<String,Object> map = new HashMap<>();
            map.put("uid",RandomID.id(6));
            map.put("province",ThreadLocalRandom.current().nextInt(10));
            Double d = ThreadLocalRandom.current().nextDouble(1000);
            map.put("stay_time",String.format("%.3f", d));//防止上面随机数出现科学计数法
            LightHouse.stat("peP:test_stat","iYmC1iUFPCCuMjqbAykSdiIYvEdsJIiUF5ByFlPJ",map,t);
        }
        System.out.println("send ok.");
        Thread.sleep(30000);//client为异步发送，防止进程结束时内存中部分消息没有发送出去
    }
}
