package com.dtstep.lighthouse.insights.test.dao;

import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dao.ProjectDao;
import com.dtstep.lighthouse.insights.dto.ProjectCreateParam;
import com.dtstep.lighthouse.common.enums.PrivateTypeEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
public class TestProjectDao {

    @Autowired
    private ProjectDao projectDao;

    @Test
    public void testCreateProject() throws Exception {
        ProjectCreateParam project = new ProjectCreateParam();
        project.setTitle("title");
        project.setDepartmentId(1);
        project.setPrivateType(PrivateTypeEnum.Private);
        project.setDesc("desc");
        project.setCreateTime(LocalDateTime.now());
        project.setUpdateTime(LocalDateTime.now());
        projectDao.insert(project);
    }


    @Test
    public void testCount() throws Exception {
        int count = projectDao.countByDepartmentId(1);
        System.out.println("count:" + count);
    }


}
