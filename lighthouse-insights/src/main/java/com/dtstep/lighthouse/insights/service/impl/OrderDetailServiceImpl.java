package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.insights.dao.OrderDetailDao;
import com.dtstep.lighthouse.insights.vo.OrderDetailVO;
import com.dtstep.lighthouse.common.modal.OrderDetail;
import com.dtstep.lighthouse.common.modal.User;
import com.dtstep.lighthouse.insights.service.OrderDetailService;
import com.dtstep.lighthouse.insights.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    @Autowired
    private OrderDetailDao orderDetailDao;

    @Autowired
    private UserService userService;

    @Override
    public List<OrderDetailVO> queryList(Integer orderId) {
        List<OrderDetail> orderDetails = orderDetailDao.queryList(orderId);
        List<OrderDetailVO> dtoList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(orderDetails)){
            for(OrderDetail orderDetail : orderDetails){
                OrderDetailVO orderDetailVO = new OrderDetailVO(orderDetail);
                Integer userId = orderDetail.getUserId();
                if(userId != null){
                    User user = userService.cacheQueryById(userId);
                    orderDetailVO.setUser(user);
                }
                dtoList.add(orderDetailVO);
            }
        }
        return dtoList;
    }
}
