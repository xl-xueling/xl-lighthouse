package com.dtstep.lighthouse.core.test;

import com.apifan.common.random.source.AreaSource;
import com.dtstep.lighthouse.client.LightHouse;
import com.dtstep.lighthouse.common.key.RandomID;
import com.dtstep.lighthouse.common.util.JsonUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class LDPTest {

    @Test
    public void iconClickTest() throws Exception {
        //连接RPC模块注册中心，默认为RPC服务部署的前两个节点（一主一从）
        LightHouse.init("10.206.7.15:4061,10.206.7.5:4061");
        for(int i=0;i<1000;i++){
            HashMap<String,Object> paramMap = new HashMap<>();
            paramMap.put("user_id","user-" + ThreadLocalRandom.current().nextInt(500));
            paramMap.put("tab_id","tab_" + ThreadLocalRandom.current().nextInt(3));
            paramMap.put("icon_id","icon_" + ThreadLocalRandom.current().nextInt(30));
            LightHouse.stat("homepage_icon_click","f1ghKrnIQaRpbWOX0HOO2EaOXQ19ymXD",paramMap,System.currentTimeMillis());
        }
        //注意：stat方法为异步发送，如果进程直接退出可能会导致部分消息没有发送出去，所以这里加一个sleep。
        Thread.sleep(10 * 1000);
        System.out.println("send ok!");
    }

    private static final String userId_RandomId = RandomID.id(10);

    private static final String dealerId_RandomId = RandomID.id(5);

    private static final AreaSource areaSource = AreaSource.getInstance();

    private static final String[] sceneArray = new String[]{"游戏","娱乐","电商","外卖","餐饮","教育","医疗"};

    @Test
    public void orderStatTest() throws Exception {
        //连接RPC模块注册中心，默认为RPC服务部署的前两个节点（一主一从）
        LightHouse.init("10.206.7.15:4061,10.206.7.5:4061");
        for(int i=0;i<1000;i++){
            HashMap<String,Object> paramMap = new HashMap<>();
            paramMap.put("userId",userId_RandomId +"_" + ThreadLocalRandom.current().nextLong(100L));
            String cityStr = areaSource.randomCity(",");
            String [] cityArray = cityStr.split(",");
            paramMap.put("province",cityArray[0]);
            paramMap.put("city",cityArray[1]);
            paramMap.put("dealerId",dealerId_RandomId + "_" + ThreadLocalRandom.current().nextInt(50));
            paramMap.put("scene",sceneArray[ThreadLocalRandom.current().nextInt(7)]);
            double amount = ThreadLocalRandom.current().nextDouble(1,9999);
            paramMap.put("amount",String.format("%.2f", amount));
            paramMap.put("orderId","order_"+i);
            LightHouse.stat("order_stat","AZ6ReXXskkRQuzcq33urcwWwPhpMqp1n",paramMap,System.currentTimeMillis());
        }
        //注意：stat方法为异步发送，如果进程直接退出可能会导致部分消息没有发送出去，所以这里加一个sleep。
        Thread.sleep(10 * 1000);
        System.out.println("send ok!");
    }
}
