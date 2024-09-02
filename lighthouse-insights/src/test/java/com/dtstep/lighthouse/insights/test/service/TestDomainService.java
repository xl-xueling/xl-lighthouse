package com.dtstep.lighthouse.insights.test.service;

import com.dtstep.lighthouse.common.modal.Domain;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.service.DomainService;
import com.dtstep.lighthouse.insights.test.listener.SpringTestExecutionListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
@TestExecutionListeners(listeners = SpringTestExecutionListener.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class TestDomainService {

    @Autowired
    private DomainService domainService;

    @Test
    public void testQueryDefault() throws Exception {
        for(int i=0;i<10;i++){
            Domain domain = domainService.queryDefault();
            System.out.println("domain:" + JsonUtil.toJSONString(domain));
        }
    }

    @Test
    public void testQueryById() throws Exception {
        for(int i=0;i<10;i++){
            Domain domain = domainService.queryById(8);
            System.out.println("domain:" + JsonUtil.toJSONString(domain));
        }
    }
}
