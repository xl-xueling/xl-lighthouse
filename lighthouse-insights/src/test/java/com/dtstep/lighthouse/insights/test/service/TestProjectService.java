package com.dtstep.lighthouse.insights.test.service;

import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dto_bak.ProjectCreateParam;
import com.dtstep.lighthouse.insights.enums.PrivateTypeEnum;
import com.dtstep.lighthouse.insights.service.ProjectService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
public class TestProjectService {

    @Autowired
    private ProjectService projectService;

    @Test
    public void testCreate() throws Exception {
        ProjectCreateParam project = new ProjectCreateParam();
        project.setTitle("首页用户数据统计");
        LocalDateTime localDateTime = LocalDateTime.now();
        project.setCreateTime(localDateTime);
        project.setUpdateTime(localDateTime);
        project.setDepartmentId(10067);
        project.setPrivateType(PrivateTypeEnum.Private);
        project.setDesc("测试数据");
        int result = projectService.create(project);
        System.out.println("result:" + result);
    }
}
