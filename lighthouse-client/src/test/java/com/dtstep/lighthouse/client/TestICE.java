package com.dtstep.lighthouse.client;

import com.dtstep.lighthouse.client.rpc.ice.ICERPCClientImpl;
import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.util.JsonUtil;
import org.junit.Test;

public class TestICE {

    @Test
    public void test1() throws Exception {
        ICERPCClientImpl client = new ICERPCClientImpl();
        String s = "10.206.6.33:4061";
        client.init(s);
        for(int i=0;i<1000000;i++){
            try{
                GroupVerifyEntity groupVerifyEntity = client.queryGroup("whf:test_stat");
                System.out.println("groupVerifyEntity:" + JsonUtil.toJSONString(groupVerifyEntity));
            }catch (Exception ex){
                ex.printStackTrace();
            }
            Thread.sleep(10000);

        }
    }
}
