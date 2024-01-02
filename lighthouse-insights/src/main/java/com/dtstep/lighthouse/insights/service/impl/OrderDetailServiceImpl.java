package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.insights.dao.OrderDetailDao;
import com.dtstep.lighthouse.insights.dto.OrderDetailDto;
import com.dtstep.lighthouse.insights.modal.OrderDetail;
import com.dtstep.lighthouse.insights.modal.User;
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
    public List<OrderDetailDto> queryList(Integer orderId) {
        List<OrderDetail> orderDetails = orderDetailDao.queryList(orderId);
        List<OrderDetailDto> dtoList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(orderDetails)){
            for(OrderDetail orderDetail : orderDetails){
                OrderDetailDto orderDetailDto = new OrderDetailDto(orderDetail);
                Integer userId = orderDetail.getUserId();
                if(userId != null){
                    User user = userService.queryById(userId);
                    orderDetailDto.setUser(user);
                }
                dtoList.add(orderDetailDto);
            }
        }
        return dtoList;
    }
}
