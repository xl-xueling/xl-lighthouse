package com.dtstep.lighthouse.insights.test.service;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dto_bak.CommonTreeNode;
import com.dtstep.lighthouse.insights.service.DepartmentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
public class TestDepartmentService {

    @Autowired
    private DepartmentService departmentService;

    @Test
    public void testQueryAll(){
        List<CommonTreeNode> nodeList = departmentService.queryTreeFormat();
        System.out.println("nodeList:" + JsonUtil.toJSONString(nodeList));

    }
}
