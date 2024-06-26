package com.dtstep.lighthouse.insights.test.dao;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dao.OrderDetailDao;
import com.dtstep.lighthouse.common.modal.OrderDetail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
public class TestOrderDetailDao {

    @Autowired
    private OrderDetailDao orderDetailDao;

    @Test
    public void testQueryList() throws Exception {
        List<OrderDetail> orderDetailList = orderDetailDao.queryList(100167);
        System.out.println("orderDetailList:" + JsonUtil.toJSONString(orderDetailList));
    }
}
