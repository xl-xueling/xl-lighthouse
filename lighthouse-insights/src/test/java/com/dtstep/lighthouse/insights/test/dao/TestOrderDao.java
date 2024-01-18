package com.dtstep.lighthouse.insights.test.dao;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dao.OrderDao;
import com.dtstep.lighthouse.insights.dto.OrderQueryParam;
import com.dtstep.lighthouse.common.enums.OrderStateEnum;
import com.dtstep.lighthouse.common.enums.OrderTypeEnum;
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

    @Test
    public void testApproveList(){
        OrderQueryParam queryParam = new OrderQueryParam();
        queryParam.setApproveUserId(110163);
        queryParam.setStates(List.of(0,1,2,3));
        queryParam.setTypes(List.of(1,2,3,6));
        List<Order> orders = orderDao.queryApproveList(queryParam,1,100);
        System.out.println("orders:" + JsonUtil.toJSONString(orders));
    }

    @Test
    public void testUpdate(){
        Order order = new Order();
        order.setState(OrderStateEnum.APPROVED);
        order.setId(100152);
        order.setUpdateTime(LocalDateTime.now());
        order.setCurrentNode(0);
        int result = orderDao.update(order);
        System.out.println("result:" + result);
    }
}
