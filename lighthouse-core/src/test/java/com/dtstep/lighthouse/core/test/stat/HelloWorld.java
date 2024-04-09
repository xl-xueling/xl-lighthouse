package com.dtstep.lighthouse.core.test.stat;

import com.dtstep.lighthouse.client.LightHouse;
import com.dtstep.lighthouse.common.random.RandomID;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * 为了测试方便，您可以直接下载工程代码，在Web服务中依次创建(统计工程 -> 统计组 -> 统计项)后，然后使用com.dtstep.lighthouse.core.test.stat.HelloWorld中的单元测试方法，记得修改RPC服务IP地址和统计组的Token及秘钥，运行即可！
 *
 * 使用过程中如有问题，及时反馈，本人一定第一时间响应~
 *
 */
public class HelloWorld {

    static {
        try{
            //修改rpc服务地址,一主一从，默认为部署集群的前两个节点
            LightHouse.init("10.206.6.39:4061,10.206.6.21:4061");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Test
    public void helloWorld() throws Exception {
        long t = System.currentTimeMillis();
        for(int i = 0;i<31991;i++){
            //修改统计组参数值、Token和秘钥
            Map<String,Object> map = new HashMap<>();
            map.put("province", RandomID.id(10));
            map.put("score",ThreadLocalRandom.current().nextDouble(1000));
            map.put("uuid","test_"+ThreadLocalRandom.current().nextInt(300));
            LightHouse.stat("hvg:test_stat","zQsn6b6jpkqwGw7R2ZoMBbW8RJsh2eIgyX8ZVsyf",map,t);
        }
        System.out.println("send ok.");
        Thread.sleep(100000);
    }
}
