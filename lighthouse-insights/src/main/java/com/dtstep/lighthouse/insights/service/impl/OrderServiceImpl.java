package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.Md5Util;
import com.dtstep.lighthouse.insights.dao.OrderDao;
import com.dtstep.lighthouse.insights.enums.OrderStateEnum;
import com.dtstep.lighthouse.insights.enums.OrderTypeEnum;
import com.dtstep.lighthouse.insights.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.Order;
import com.dtstep.lighthouse.insights.modal.Role;
import com.dtstep.lighthouse.insights.service.BaseService;
import com.dtstep.lighthouse.insights.service.OrderService;
import com.dtstep.lighthouse.insights.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private BaseService baseService;

    @Autowired
    private RoleService roleService;

    @Override
    public int create(Order order) {
        int userId = baseService.getCurrentUserId();
        order.setUserId(userId);
        LocalDateTime localDateTime = LocalDateTime.now();
        order.setCreateTime(localDateTime);
        order.setUpdateTime(localDateTime);
        order.setState(OrderStateEnum.PENDING);
        Map<String,Object> configMap = order.getExtendConfig();
        List<Integer> steps = new ArrayList<>();
        String hash;
        if(order.getOrderType() == OrderTypeEnum.PROJECT_ACCESS){
            int projectId = (Integer) configMap.get("projectId");
            Role role = roleService.queryRole(RoleTypeEnum.PROJECT_MANAGE_PERMISSION,projectId);
            System.out.println("role is:" + JsonUtil.toJSONString(role));
            String message = userId + "_" + OrderTypeEnum.PROJECT_ACCESS.getOrderType() + "_" + projectId;
            hash = Md5Util.getMD5(message);
            order.setHash(hash);
            order.setCurrentNode(role.getId());
            steps.add(role.getId());
        }
        order.setSteps(steps);
        return orderDao.insert(order);
    }
}
