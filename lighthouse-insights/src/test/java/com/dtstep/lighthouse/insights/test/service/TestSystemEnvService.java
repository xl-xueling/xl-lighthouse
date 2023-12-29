package com.dtstep.lighthouse.insights.test.service;

import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.service.SystemEnvService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
public class TestSystemEnvService {

    @Autowired
    private SystemEnvService systemEnvService;

    @Test
    public void testCreateSignKey(){
        systemEnvService.generateSignKeyIfNotExist();
    }

    @Test
    public void getParam(){
        String s = systemEnvService.getParam("secret_key");
        System.out.println("s:" + s);
    }
}
