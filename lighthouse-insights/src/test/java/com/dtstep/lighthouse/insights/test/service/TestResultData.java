package com.dtstep.lighthouse.insights.test.service;

import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
public class TestResultData {

    @Test
    public void testMessage(){
    }
}
