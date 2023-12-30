package com.dtstep.lighthouse.insights.test.dao;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dao.OrderDao;
import com.dtstep.lighthouse.insights.enums.OrderStateEnum;
import com.dtstep.lighthouse.insights.enums.OrderTypeEnum;
import com.dtstep.lighthouse.insights.modal.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
public class TestOrderDao {

    @Autowired
    private OrderDao orderDao;

    @Test
    public void testInsert(){
        Order order = new Order();
        LocalDateTime localDateTime = LocalDateTime.now();
        order.setDesc("sss");
        order.setUserId(123);
        order.setOrderType(OrderTypeEnum.PROJECT_ACCESS);
        List<Integer> ids = new ArrayList<>();
        ids.add(23);
        order.setSteps(List.of(23));
        order.setCurrentNode(23);
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("projectId",23);
        order.setExtendConfig(paramMap);
        order.setUpdateTime(localDateTime);
        order.setCreateTime(localDateTime);
        order.setHash("hash");
        order.setState(OrderStateEnum.APPROVED);
        int result = orderDao.insert(order);
        System.out.println("result:" + result);
    }

    @Test
    public void testQueryById(){
        int id = 100153;
        Order order = orderDao.queryById(id);
        System.out.println("order:" + JsonUtil.toJSONString(order));
    }
}
