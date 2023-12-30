package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.Md5Util;
import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dao.OrderDao;
import com.dtstep.lighthouse.insights.dto.OrderQueryParam;
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
            String message = order.getUserId() + "_" + OrderTypeEnum.PROJECT_ACCESS.getOrderType() + "_" + projectId;
            order.setHash(Md5Util.getMD5(message));
            order.setCurrentNode(role.getId());
            steps.add(role.getId());
        }else if(order.getOrderType() == OrderTypeEnum.USER_PEND_APPROVE){
            Role role = roleService.queryRole(RoleTypeEnum.OPT_MANAGE_PERMISSION,0);
            String message = order.getUserId() + "_" + "register";
            order.setHash(Md5Util.getMD5(message));
            order.setCurrentNode(role.getId());
            steps.add(role.getId());
        }
        order.setSteps(steps);
        return orderDao.insert(order);
    }

    @Override
    public ListData<Order> queryApplyList(OrderQueryParam queryParam, Integer pageNum, Integer pageSize) {
        List<Order> orders = orderDao.queryApplyList(queryParam,pageNum,pageSize);
        ListData<Order> listData = new ListData<>();
        listData.setList(orders);
        return listData;
    }

    @Override
    public ListData<Order> queryApproveList(OrderQueryParam queryParam, Integer pageNum, Integer pageSize) {
        List<Order> orders = orderDao.queryApproveList(queryParam,pageNum,pageSize);
        ListData<Order> listData = new ListData<>();
        listData.setList(orders);
        return listData;
    }
}
