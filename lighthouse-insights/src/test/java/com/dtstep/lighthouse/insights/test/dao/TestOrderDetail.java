package com.dtstep.lighthouse.insights.test.dao;

import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dao.OrderDetailDao;
import com.dtstep.lighthouse.insights.enums.ApproveStateEnum;
import com.dtstep.lighthouse.insights.modal.OrderDetail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
public class TestOrderDetail {

    @Autowired
    private OrderDetailDao orderDetailDao;

    @Test
    public void testCreate() throws Exception {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(1);
        orderDetail.setState(ApproveStateEnum.APPROVED);
        orderDetail.setRoleId(1);
        orderDetail.setReply("sss");
        orderDetailDao.insert(orderDetail);
    }
}
