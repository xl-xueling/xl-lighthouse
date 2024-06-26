package com.dtstep.lighthouse.client.cluster;

import com.dtstep.lighthouse.client.LightHouse;
import com.dtstep.lighthouse.client.rpc.ice.ICERPCClientImpl;
import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.util.JsonUtil;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TestICE {

    private static final ScheduledExecutorService service = Executors.newScheduledThreadPool(3);

    @Test
    public void test1() throws Exception {
        ICERPCClientImpl client = new ICERPCClientImpl();
        String s = "10.206.6.13:4061";
        Properties properties = new Properties();
        properties.setProperty("lighthouse_process_frequency","600");
        properties.setProperty("lighthouse_process_batch","300");
        LightHouse.init(s,properties);
        service.schedule(() -> {
            System.out.println("---Stop execute...");
            LightHouse.stop();
        },1,TimeUnit.MINUTES);
        for(int i=0;i<10000;i++){
            try{
                GroupVerifyEntity groupVerifyEntity = client.queryGroupInfo("RrY:test_stat");
                System.out.println("groupVerifyEntity:" + JsonUtil.toJSONString(groupVerifyEntity));
            }catch (Exception ex){
                ex.printStackTrace();
            }
            Thread.sleep(10000);
        }
        Thread.sleep(10000000000L);
    }


}
