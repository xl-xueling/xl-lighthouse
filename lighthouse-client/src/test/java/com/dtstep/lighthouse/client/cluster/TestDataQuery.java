package com.dtstep.lighthouse.client.cluster;

import com.dtstep.lighthouse.client.LightHouse;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestDataQuery extends ClientBaseTest{

    @Test
    public void testDataQuery() throws Exception {
        int id = 1100578;
        long batchTime = DateUtil.batchTime(1, TimeUnit.MINUTES,System.currentTimeMillis());
        List<StatValue> result = LightHouse.dataQuery(id,"5ONJTOU4JpvoclyI4E0Xbm6XIysis4O0UHCVQhy3",null, new ArrayList<>(Collections.singletonList(batchTime)));
        System.out.println("result:" + JsonUtil.toJSONString(result));
    }
}
