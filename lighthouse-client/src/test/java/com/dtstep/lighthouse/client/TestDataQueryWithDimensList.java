package com.dtstep.lighthouse.client;

import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.serializer.KryoSerializer;
import com.dtstep.lighthouse.common.serializer.Serializer;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class TestDataQueryWithDimensList extends ClientBaseTest {

    @Test
    public void testDataQueryWithDimensList() throws Exception {
        int id = 1100580;
        List<String> list = new ArrayList<>();
        list.add("常德市");
        long batchTime = DateUtil.batchTime(5, TimeUnit.MINUTES,System.currentTimeMillis());
        Map<String,List<StatValue>> map = LightHouse.dataQueryWithDimensList(id,"5ONJTOU4JpvoclyI4E0Xbm6XIysis4O0UHCVQhy3",list,new ArrayList<>(Collections.singletonList(batchTime)));
        Serializer serializer = new KryoSerializer();
        byte[] bytes = serializer.serializerMap(map);
        Map<String,List<StatValue>> v = new HashMap<>();
        serializer.deserializeMap(bytes, HashMap.class);
        System.out.println("map:" + JsonUtil.toJSONString(map));
    }

}
