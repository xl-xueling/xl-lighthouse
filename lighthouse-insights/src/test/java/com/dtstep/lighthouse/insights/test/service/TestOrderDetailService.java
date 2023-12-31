package com.dtstep.lighthouse.insights.test.service;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dto.OrderDetailDto;
import com.dtstep.lighthouse.insights.service.OrderDetailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
public class TestOrderDetailService {

    @Autowired
    private OrderDetailService orderDetailService;

    @Test
    public void testQuery() throws Exception {
        int orderId = 100167;
        List<OrderDetailDto> detailDtoList = orderDetailService.queryList(orderId);
        System.out.println("detailList:" + JsonUtil.toJSONString(detailDtoList));
    }
}
