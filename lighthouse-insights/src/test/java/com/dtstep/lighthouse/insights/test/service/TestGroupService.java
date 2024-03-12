package com.dtstep.lighthouse.insights.test.service;

import com.dtstep.lighthouse.common.modal.Stat;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.service.GroupService;
import com.dtstep.lighthouse.insights.vo.GroupVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
public class TestGroupService {

    @Autowired
    private GroupService groupService;

    @Test
    public void testQueryById() throws Exception{
        int groupId = 100285;
        GroupVO groupVo = groupService.queryById(groupId);
        System.out.println("groupVo is:" + JsonUtil.toJSONString(groupVo));
    }
}
