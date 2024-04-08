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

public class HelloWorld {

    static {
        try{
            //修改rpc服务地址,一主一从，默认为部署集群的前两个节点
            LightHouse.init("10.206.6.21:4061,10.206.6.5:4061");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Test
    public void helloWorld() throws Exception {
        long t = System.currentTimeMillis();
        for(int i = 0;i<31001;i++){
            Map<String,Object> map = new HashMap<>();
            map.put("province", RandomID.id(10));
            map.put("score",ThreadLocalRandom.current().nextDouble(1000));
            LightHouse.stat("BHC:test_stat","pAniRUTDgX5fkvGil2Z8ogQSyVPE93uxqMjGP4he",map,t);
        }
        System.out.println("send ok.");
        Thread.sleep(100000);
    }
}
