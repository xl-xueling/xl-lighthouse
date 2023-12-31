package com.dtstep.lighthouse.insights.test.dao;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dao.ProjectDao;
import com.dtstep.lighthouse.insights.dto.ProjectCreateParam;
import com.dtstep.lighthouse.insights.dto.ProjectQueryParam;
import com.dtstep.lighthouse.insights.enums.PrivateTypeEnum;
import com.dtstep.lighthouse.insights.modal.Project;
import com.github.pagehelper.PageHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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
    public void testQueryList() throws Exception {
        ProjectQueryParam queryParam = new ProjectQueryParam();
        queryParam.setDepartmentIds(List.of(10068));
        List<Project> list = projectDao.queryList(queryParam,1,100);
        System.out.println("list:" + JsonUtil.toJSONString(list));
    }

    @Test
    public void testQueryOwnerList() throws Exception {
        ProjectQueryParam queryParam = new ProjectQueryParam();
//        queryParam.setOwner(110154);
//        queryParam.setDepartmentIds(List.of(10079));
        List<Project> list = projectDao.queryList(queryParam,1,100);
        for(int i=0;i<list.size();i++){
            System.out.println("project:" + list.get(i).getTitle());
        }
    }

    @Test
    public void testCount() throws Exception {
        int count = projectDao.countByDepartmentId(1);
        System.out.println("count:" + count);
    }


}
