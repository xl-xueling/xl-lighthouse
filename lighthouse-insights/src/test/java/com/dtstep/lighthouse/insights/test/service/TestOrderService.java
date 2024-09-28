package com.dtstep.lighthouse.insights.test.service;

import com.dtstep.lighthouse.common.entity.ResultCode;
import com.dtstep.lighthouse.common.enums.OrderTypeEnum;
import com.dtstep.lighthouse.common.modal.User;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.service.OrderService;
import com.dtstep.lighthouse.insights.service.UserService;
import com.dtstep.lighthouse.insights.test.listener.SpringTestExecutionListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
@TestExecutionListeners(listeners = SpringTestExecutionListener.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class TestOrderService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Test
    public void testCreateOrder() throws Exception {
        System.out.println("---Test");
        User user = userService.queryById(110239);
        Map<String,Object> extendConfig = new HashMap<>();
        extendConfig.put("callerId",11017);
        extendConfig.put("projectId",11137);
        ResultCode resultCode = orderService.submit(user, OrderTypeEnum.CALLER_PROJECT_ACCESS,"test",extendConfig);
        System.out.println("resultCode:" + resultCode);
    }
}
