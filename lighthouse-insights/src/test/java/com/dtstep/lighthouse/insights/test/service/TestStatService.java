package com.dtstep.lighthouse.insights.test.service;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.entity.ResultCode;
import com.dtstep.lighthouse.core.formula.TemplateUtil;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dao.StatDao;
import com.dtstep.lighthouse.common.modal.RenderFilterConfig;
import com.dtstep.lighthouse.common.modal.Stat;
import com.dtstep.lighthouse.insights.service.StatService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
public class TestStatService {

    @Autowired
    private StatService statService;

    @Autowired
    private StatDao statDao;

    @Test
    public void testCreate() throws Exception{
        Stat stat = new Stat();
        String template = "<stat-item title=\"每分钟_uv统计\" stat=\"count()\"  dimens=\"province\"/>";
        stat.setTemplate(template);
        stat.setGroupId(100182);
        ResultCode result = statService.create(stat);
        System.out.println("result:" + result);
    }

    @Test
    public void testQueryList2() {
        Integer projectId = 23;
        List<Stat> statList = statService.queryByProjectId(projectId);
        System.out.println("size:" + statList.size());
    }
}
