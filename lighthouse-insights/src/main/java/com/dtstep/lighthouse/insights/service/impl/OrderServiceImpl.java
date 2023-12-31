package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.Md5Util;
import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dao.OrderDao;
import com.dtstep.lighthouse.insights.dao.PermissionDao;
import com.dtstep.lighthouse.insights.dto.OrderDto;
import com.dtstep.lighthouse.insights.dto.OrderQueryParam;
import com.dtstep.lighthouse.insights.dto.PermissionQueryParam;
import com.dtstep.lighthouse.insights.dto.UserDto;
import com.dtstep.lighthouse.insights.enums.OrderStateEnum;
import com.dtstep.lighthouse.insights.enums.OrderTypeEnum;
import com.dtstep.lighthouse.insights.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.Order;
import com.dtstep.lighthouse.insights.modal.Permission;
import com.dtstep.lighthouse.insights.modal.Role;
import com.dtstep.lighthouse.insights.modal.User;
import com.dtstep.lighthouse.insights.service.BaseService;
import com.dtstep.lighthouse.insights.service.OrderService;
import com.dtstep.lighthouse.insights.service.RoleService;
import com.dtstep.lighthouse.insights.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private BaseService baseService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionDao permissionDao;

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
    public ListData<OrderDto> queryApproveList(OrderQueryParam queryParam, Integer pageNum, Integer pageSize) {
        List<Order> orders = orderDao.queryApproveList(queryParam,pageNum,pageSize);
        ListData<OrderDto> listData = new ListData<>();
        List<OrderDto> orderDtoList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(orders)){
            for(Order order : orders){
                OrderDto orderDto = new OrderDto(order);
                int userId = orderDto.getUserId();
                int roleId = order.getCurrentNode();
                PermissionQueryParam permissionQueryParam = new PermissionQueryParam();
                permissionQueryParam.setRoleId(roleId);
                permissionQueryParam.setOwnerType(1);
                List<Permission> permissions = permissionDao.queryList(permissionQueryParam,1,5);
                if(CollectionUtils.isNotEmpty(permissions)){
                    List<Integer> userIdList = permissions.stream().map(z -> z.getOwnerId()).collect(Collectors.toList());
                    List<UserDto> admins = new ArrayList<>();
                    for(Integer approveUserId : userIdList){
                        UserDto user = userService.queryById(approveUserId);
                        admins.add(user);
                    }
                    orderDto.setAdmins(admins);
                }
                UserDto user = userService.queryById(userId);
                orderDto.setUser(user);
                orderDtoList.add(orderDto);
            }
        }
        listData.setList(orderDtoList);
        return listData;
    }
}
