package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.enums.user.UserStateEnum;
import com.dtstep.lighthouse.common.util.Md5Util;
import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dao.OrderDao;
import com.dtstep.lighthouse.insights.dao.OrderDetailDao;
import com.dtstep.lighthouse.insights.dao.PermissionDao;
import com.dtstep.lighthouse.insights.dto.*;
import com.dtstep.lighthouse.insights.enums.ApproveStateEnum;
import com.dtstep.lighthouse.insights.enums.OrderStateEnum;
import com.dtstep.lighthouse.insights.enums.OrderTypeEnum;
import com.dtstep.lighthouse.insights.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.*;
import com.dtstep.lighthouse.insights.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private OrderDetailDao orderDetailDao;

    @Autowired
    private BaseService baseService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private OrderDetailService orderDetailService;

    @Override
    public int create(Order order) {
        LocalDateTime localDateTime = LocalDateTime.now();
        order.setCreateTime(localDateTime);
        order.setUpdateTime(localDateTime);
        order.setState(OrderStateEnum.PROCESSING);
        Map<String,Object> configMap = order.getExtendConfig();
        List<Integer> steps = new ArrayList<>();
        Map<Integer,RoleTypeEnum> roleTypeMap = new HashMap<>();
        String hash;
        if(order.getOrderType() == OrderTypeEnum.PROJECT_ACCESS){
            int projectId = (Integer) configMap.get("projectId");
            Role role = roleService.queryRole(RoleTypeEnum.PROJECT_MANAGE_PERMISSION,projectId);
            String message = order.getUserId() + "_" + OrderTypeEnum.PROJECT_ACCESS.getOrderType() + "_" + projectId;
            order.setHash(Md5Util.getMD5(message));
            order.setCurrentNode(role.getId());
            roleTypeMap.put(role.getId(),RoleTypeEnum.PROJECT_MANAGE_PERMISSION);
            steps.add(role.getId());
        }else if(order.getOrderType() == OrderTypeEnum.USER_PEND_APPROVE){
            Role role = roleService.queryRole(RoleTypeEnum.OPT_MANAGE_PERMISSION,0);
            String message = order.getUserId() + "_" + "register";
            order.setHash(Md5Util.getMD5(message));
            order.setCurrentNode(role.getId());
            roleTypeMap.put(role.getId(),RoleTypeEnum.OPT_MANAGE_PERMISSION);
            steps.add(role.getId());
        }
        order.setSteps(steps);
        orderDao.insert(order);
        int orderId = order.getId();
        for(Integer roleId : steps){
            OrderDetail orderDetail = new OrderDetail();
            RoleTypeEnum roleType = roleTypeMap.get(roleId);
            orderDetail.setCreateTime(localDateTime);
            orderDetail.setOrderId(orderId);
            orderDetail.setRoleType(roleType);
            orderDetail.setState(ApproveStateEnum.PENDING);
            orderDetail.setRoleId(roleId);
            orderDetailDao.insert(orderDetail);
        }
        return orderId;
    }

    @Override
    public ListData<Order> queryApplyList(OrderQueryParam queryParam, Integer pageNum, Integer pageSize) {
        List<Order> orders = orderDao.queryApplyList(queryParam,pageNum,pageSize);
        ListData<Order> listData = new ListData<>();
        listData.setList(orders);
        return listData;
    }

    private OrderDto translate(Order order){
        OrderDto orderDto = new OrderDto(order);
        int userId = orderDto.getUserId();
        List<Integer> roleIds = order.getSteps();
        HashMap<Integer,List<UserDto>> adminsMap = new HashMap<>();
        for(Integer roleId : roleIds){
            PermissionQueryParam permissionQueryParam = new PermissionQueryParam();
            permissionQueryParam.setRoleId(roleId);
            permissionQueryParam.setOwnerType(1);
            List<UserDto> admins = new ArrayList<>();
            List<Permission> permissions = permissionDao.queryList(permissionQueryParam,1,4);
            if(CollectionUtils.isNotEmpty(permissions)){
                List<Integer> userIdList = permissions.stream().map(z -> z.getOwnerId()).collect(Collectors.toList());
                for(Integer approveUserId : userIdList){
                    UserDto user = userService.queryById(approveUserId);
                    admins.add(user);
                }
            }
            adminsMap.put(roleId,admins);
        }
        orderDto.setAdminsMap(adminsMap);
        UserDto user = userService.queryById(userId);
        List<OrderDetailDto> orderDetails = orderDetailService.queryList(order.getId());
        orderDto.setOrderDetails(orderDetails);
        orderDto.setUser(user);
        return orderDto;
    }

    @Override
    public ListData<OrderDto> queryApproveList(OrderQueryParam queryParam, Integer pageNum, Integer pageSize) {
        List<Order> orders = orderDao.queryApproveList(queryParam,pageNum,pageSize);
        ListData<OrderDto> listData = new ListData<>();
        List<OrderDto> orderDtoList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(orders)){
            for(Order order : orders){
                OrderDto orderDto = translate(order);
                orderDtoList.add(orderDto);
            }
        }
        listData.setList(orderDtoList);
        return listData;
    }

    @Transactional
    @Override
    public int approve(OrderApproveParam approveParam) {
        int result = approveParam.getResult();
        Order dbOrder = orderDao.queryById(approveParam.getId());
        Validate.isTrue(approveParam.getRoleId().intValue() == dbOrder.getCurrentNode().intValue());
        Validate.notNull(dbOrder);
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setRoleId(approveParam.getRoleId());
        orderDetail.setReply(approveParam.getReply());
        orderDetail.setOrderId(approveParam.getId());
        LocalDateTime localDateTime = LocalDateTime.now();
        orderDetail.setApproveTime(localDateTime);
        orderDetail.setCreateTime(localDateTime);
        if(result == 1){
            dbOrder.setState(OrderStateEnum.APPROVED);
            orderDetail.setState(ApproveStateEnum.APPROVED);
        }else if(result == 2){
            dbOrder.setState(OrderStateEnum.REJECTED);
            orderDetail.setState(ApproveStateEnum.REJECTED);
        }
        dbOrder.setUpdateTime(LocalDateTime.now());
        dbOrder.setCurrentNode(0);
        orderDao.update(dbOrder);
        if(dbOrder.getOrderType() == OrderTypeEnum.USER_PEND_APPROVE){
            UserUpdateParam userUpdateParam = new UserUpdateParam();
            userUpdateParam.setState(UserStateEnum.USR_NORMAL);
            userUpdateParam.setUpdateTime(LocalDateTime.now());
            userService.update(userUpdateParam);
        }
        orderDetailDao.insert(orderDetail);
        return 0;
    }
}
