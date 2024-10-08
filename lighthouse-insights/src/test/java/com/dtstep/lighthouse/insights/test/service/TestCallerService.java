package com.dtstep.lighthouse.insights.test.service;

import com.dtstep.lighthouse.common.entity.ListData;
import com.dtstep.lighthouse.common.modal.CallerQueryParam;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.service.CallerService;
import com.dtstep.lighthouse.insights.test.listener.SpringTestExecutionListener;
import com.dtstep.lighthouse.insights.vo.CallerVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
@TestExecutionListeners(listeners = SpringTestExecutionListener.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class TestCallerService {

    @Autowired
    private CallerService callerService;

    @Test
    public void testQuery() throws Exception {
        CallerQueryParam queryParam = new CallerQueryParam();
        queryParam.setSearch("demo");
        queryParam.setDepartmentIds(List.of(10251));
        ListData<CallerVO> listData = callerService.queryList(queryParam,1,10);
        List<CallerVO> voList = listData.getList();
        for(CallerVO callerVO : voList){
            System.out.println("callerVO:" + JsonUtil.toJSONString(callerVO));
        }
    }
}
