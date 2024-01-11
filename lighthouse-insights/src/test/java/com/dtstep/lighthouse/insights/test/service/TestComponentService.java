package com.dtstep.lighthouse.insights.test.service;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dto.TreeNode;
import com.dtstep.lighthouse.insights.service.ComponentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
public class TestComponentService {

    @Autowired
    private ComponentService componentService;

    @Test
    public void testVerify() {
        List<TreeNode> list = new ArrayList<>();
        TreeNode treeNode1 = new TreeNode();
        treeNode1.setLabel("1");
        treeNode1.setValue("beijing");
        list.add(treeNode1);
        TreeNode treeNode2 = new TreeNode();
        treeNode2.setLabel("2");
        treeNode2.setValue("shanghai");
        list.add(treeNode2);
        String json = JsonUtil.toJSONString(list);
        System.out.println(json);
        String json2 = "[{\"value\":\"beijing\",\"label\":\"1\"},{\"value\":\"ss\"}]";
        ResultCode resultCode = componentService.verify(json2);
        System.out.println("resultCode:" + JsonUtil.toJSONString(resultCode));
        System.out.println("resultCode:" + JsonUtil.toJSONString(resultCode.getParams()));
    }
}
