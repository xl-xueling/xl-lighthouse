package com.dtstep.lighthouse.client.cluster;

import com.dtstep.lighthouse.client.LightHouse;
import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.enums.RunningMode;
import com.dtstep.lighthouse.common.util.DateUtil;
import org.junit.Test;

import java.util.List;

public class TestClient extends ClientBaseTest {

//    static {
//        try{
//            //修改rpc服务地址,一主一从，默认为部署集群的前两个节点
//            LightHouse.init("127.0.0.1:4062", RunningMode.STANDALONE);
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
//    }

    @Test
    public void testDataQuery() throws Exception {
        int statId = 1100578;
        List<StatValue> valueList = LightHouse.dataQuery(statId,"5ONJTOU4JpvoclyI4E0Xbm6XIysis4O0UHCVQhy32",null,1714233600000L,1714320000000L);
        System.out.println("valueList:" + valueList);
        for(int i=0;i<valueList.size();i++){
            StatValue statValue = valueList.get(i);
            System.out.println("batchTime:" + DateUtil.formatTimeStamp(statValue.getBatchTime(),"yyyy-MM-dd HH:mm:ss") + ",value:" + statValue.getValue());
        }
    }

    @Test
    public void testStandalone() throws Exception{
        System.out.println("init standalone.");
        String token = "Gjd:feed_behavior_stat";
        LightHouse.init("127.0.0.1:4061", RunningMode.STANDALONE);
        System.out.println("ok.");
        GroupVerifyEntity groupVerifyEntity = LightHouse.queryGroupInfo(token);
        System.out.println("groupVerifyEntity:" + groupVerifyEntity.getVerifyKey());
    }
}
