package com.dtstep.lighthouse.insights.test.service;

import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.modal.Stat;
import com.dtstep.lighthouse.insights.service.StatService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
public class TestStatService {

    @Autowired
    private StatService statService;

    @Test
    public void testCreate(){
        Stat stat = new Stat();
        String template = "<stat-item title=\"每分钟_uv统计\" stat=\"count()\"  dimens=\"province\"/>";
        stat.setTemplate(template);
        stat.setGroupId(100182);
        int result = statService.create(stat);
        System.out.println("result:" + result);
    }
}
