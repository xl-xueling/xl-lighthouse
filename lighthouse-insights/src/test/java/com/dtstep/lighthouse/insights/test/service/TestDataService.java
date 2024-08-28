package com.dtstep.lighthouse.insights.test.service;

import com.dtstep.lighthouse.common.entity.ServiceResult;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.modal.StatDataObject;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.wrapper.StatDBWrapper;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.service.DataService;
import com.dtstep.lighthouse.insights.service.StatService;
import com.dtstep.lighthouse.insights.test.listener.SpringTestExecutionListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
@TestExecutionListeners(listeners = SpringTestExecutionListener.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class TestDataService {

    @Autowired
    private StatService statService;

    @Autowired
    private DataService dataService;

    @Test
    public void testDimensArrange() throws Exception{
        int statId = 1100599;
        StatExtEntity statExtEntity = StatDBWrapper.queryById(statId);
        System.out.println("statExtEntity:" + statExtEntity);
        LinkedHashMap<String, String[]> dimensParams = new LinkedHashMap<>();
        dimensParams.put("top_level",new String[]{"24,测试"});
        List<String> list = dataService.dimensArrangement(statExtEntity,dimensParams);
        System.out.println("dimensList is:" + JsonUtil.toJSONString(list));
        long t1 = DateUtil.getDayStartTime(System.currentTimeMillis());
        long t2 = DateUtil.getDayEndTime(System.currentTimeMillis());
        LocalDateTime localDateTime1 = DateUtil.timestampToLocalDateTime(t1);
        LocalDateTime localDateTime2 = DateUtil.timestampToLocalDateTime(t2);
        ServiceResult<List<StatDataObject>> objects = dataService.dataQuery(statExtEntity,localDateTime1,localDateTime2,list);
        System.out.println("objects is:" + JsonUtil.toJSONString(objects));
    }
}
