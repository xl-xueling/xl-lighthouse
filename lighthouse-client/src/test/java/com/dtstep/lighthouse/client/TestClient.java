package com.dtstep.lighthouse.client;

import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.util.DateUtil;
import org.junit.Test;

import java.util.List;

public class TestClient {

    static {
        try{
            //修改rpc服务地址,一主一从，默认为部署集群的前两个节点
            LightHouse.init("10.206.6.11:4061,10.206.6.28:4061");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Test
    public void testDataQuery() throws Exception {
        int statId = 11005772;
        List<StatValue> valueList = LightHouse.dataQuery(statId,"XutZtE6xsPQK8pWWDp2qqeLCspd5frkuqAc4Y4Ns",null,1714233600000L,1714320000000L);
        System.out.println("valueList:" + valueList);
        for(int i=0;i<valueList.size();i++){
            StatValue statValue = valueList.get(i);
            System.out.println("batchTime:" + DateUtil.formatTimeStamp(statValue.getBatchTime(),"yyyy-MM-dd HH:mm:ss") + ",value:" + statValue.getValue());
        }
    }
}
