package com.dtstep.lighthouse.insights.service.impl;
/*
 * Copyright (C) 2022-2024 XueLing.雪灵
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import com.dtstep.lighthouse.common.entity.FlowNode;
import com.dtstep.lighthouse.common.enums.*;
import com.dtstep.lighthouse.common.modal.*;
import com.dtstep.lighthouse.common.util.Md5Util;
import com.dtstep.lighthouse.common.entity.ListData;
import com.dtstep.lighthouse.common.entity.ResultCode;
import com.dtstep.lighthouse.insights.dao.OrderDao;
import com.dtstep.lighthouse.insights.dao.OrderDetailDao;
import com.dtstep.lighthouse.insights.dao.PermissionDao;
import com.dtstep.lighthouse.insights.dto.ApplyOrderQueryParam;
import com.dtstep.lighthouse.insights.dto.OrderProcessParam;
import com.dtstep.lighthouse.insights.dto.ApproveOrderQueryParam;
import com.dtstep.lighthouse.insights.service.*;
import com.dtstep.lighthouse.insights.vo.OrderDetailVO;
import com.dtstep.lighthouse.insights.vo.OrderVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

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

    @Autowired
    private ProjectService projectService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private StatService statService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private ViewService viewService;

    @Autowired
    private CallerService callerService;

    @Override
    public ListData<OrderVO> queryApproveList(ApproveOrderQueryParam queryParam, Integer pageNum, Integer pageSize) {
        Integer currentUserId = baseService.getCurrentUserId();
        queryParam.setApproveUserId(currentUserId);
        PageHelper.startPage(pageNum,pageSize,"create_time desc");
        List<OrderVO> orderDtoList = new ArrayList<>();
        PageInfo<Order> pageInfo;
        try{
            List<Order> orders = orderDao.queryApproveList(queryParam,pageNum,pageSize);
            pageInfo = new PageInfo<>(orders);
        }finally {
            PageHelper.clearPage();
        }
        for(Order order : pageInfo.getList()){
            try{
                OrderVO orderDto = translateApproveEntity(order);
                orderDtoList.add(orderDto);
            }catch (Exception ex){
                logger.error("translate item info error,itemId:{}!",order.getId(),ex);
            }
        }
        return ListData.newInstance(orderDtoList,pageInfo.getTotal(),pageNum,pageSize);
    }

    @Override
    public ListData<OrderVO> queryApplyList(ApplyOrderQueryParam queryParam, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize,"create_time desc");
        List<OrderVO> orderDtoList = new ArrayList<>();
        PageInfo<Order> pageInfo = null;
        try{
            List<Order> orders = orderDao.queryApplyList(queryParam,pageNum,pageSize);
            pageInfo = new PageInfo<>(orders);
        }finally {
            PageHelper.clearPage();
        }
        for(Order order : pageInfo.getList()){
            try{
                OrderVO orderVO = translateApplyEntity(order);
                orderDtoList.add(orderVO);
            }catch (Exception ex){
                logger.error("translate item info error,itemId:{}!",order.getId(),ex);
            }
        }
        return ListData.newInstance(orderDtoList,pageInfo.getTotal(),pageNum,pageSize);
    }

    @Override
    public OrderVO queryById(Integer id) throws Exception{
        Order order = orderDao.queryById(id);
        Validate.notNull(order);
        OrderVO orderVO = translateApproveEntity(order);
        List<OrderDetailVO> orderDetails = orderDetailService.queryList(id);
        orderVO.setOrderDetails(orderDetails);
        List<Integer> roleIds = orderVO.getSteps();
        HashMap<Integer,List<User>> adminsMap = new HashMap<>();
        for(Integer roleId : roleIds){
            List<User> admins = new ArrayList<>();
            List<Integer> adminIds = permissionDao.queryUserPermissionsByRoleId(roleId,5);
            for(Integer approveUserId : adminIds){
                User user = userService.cacheQueryById(approveUserId);
                if(user != null){
                    admins.add(user);
                }
            }
            adminsMap.put(roleId,admins);
        }
        orderVO.setAdminsMap(adminsMap);
        return orderVO;
    }

    private void checkAddRole(List<Role> list, Role role){
        List<Integer> roleIds = list.stream().map(Role::getId).collect(Collectors.toList());
        if(!roleIds.contains(role.getId())){
            list.add(role);
        }
    }

    private <T> List<Role> getApproveRoleList(User applyUser,OrderTypeEnum orderTypeEnum,T param) throws Exception {
        List<FlowNode> flow = orderTypeEnum.getDefaultWorkFlow();
        List<Role> roleList = new ArrayList<>();
        for(FlowNode flowNode : flow){
            RoleTypeEnum roleTypeEnum = flowNode.getRoleTypeEnum();
            if(roleTypeEnum == RoleTypeEnum.FULL_MANAGE_PERMISSION){
                Role role = roleService.queryRole(roleTypeEnum,0);
                checkAddRole(roleList,role);
            }else if(roleTypeEnum == RoleTypeEnum.OPT_MANAGE_PERMISSION){
                Role role = roleService.queryRole(roleTypeEnum,0);
                checkAddRole(roleList,role);
            }else if(roleTypeEnum == RoleTypeEnum.PROJECT_MANAGE_PERMISSION){
                if(orderTypeEnum == OrderTypeEnum.PROJECT_ACCESS || orderTypeEnum == OrderTypeEnum.CALLER_PROJECT_ACCESS){
                    Project project = (Project) param;
                    Role role = roleService.queryRole(roleTypeEnum,project.getId());
                    checkAddRole(roleList,role);
                }else if(orderTypeEnum == OrderTypeEnum.STAT_ACCESS || orderTypeEnum == OrderTypeEnum.CALLER_STAT_ACCESS){
                    Stat stat = (Stat) param;
                    Role role = roleService.queryRole(roleTypeEnum,stat.getProjectId());
                    checkAddRole(roleList,role);
                }
            }else if(roleTypeEnum == RoleTypeEnum.METRIC_MANAGE_PERMISSION){
                MetricSet metricSet = (MetricSet) param;
                Role role = roleService.queryRole(roleTypeEnum, metricSet.getId());
                checkAddRole(roleList,role);
            }else if(roleTypeEnum == RoleTypeEnum.VIEW_MANAGE_PERMISSION){
                View view = (View) param;
                Role role = roleService.queryRole(roleTypeEnum, view.getId());
                checkAddRole(roleList,role);
            }else if(roleTypeEnum == RoleTypeEnum.DEPARTMENT_MANAGE_PERMISSION){
                int departmentId = 0;
                FlowNode.Extend extend = flowNode.getExtend();
                boolean itemNear = extend.isItemNear();
                if (itemNear){
                    if(orderTypeEnum == OrderTypeEnum.PROJECT_ACCESS){
                        Project project = (Project) param;
                        departmentId = project.getDepartmentId();
                    }else if(orderTypeEnum == OrderTypeEnum.STAT_ACCESS){
                        Stat stat = (Stat) param;
                        Project project = projectService.queryById(stat.getProjectId());
                        departmentId = project.getDepartmentId();
                    }else{
                        throw new Exception();
                    }
                }else{
                    departmentId = applyUser.getDepartmentId();
                }
                Integer extendParam = (Integer)(extend.getParam());
                if(extendParam == 0){
                    Role role = roleService.queryRole(RoleTypeEnum.DEPARTMENT_MANAGE_PERMISSION,departmentId);
                    checkAddRole(roleList,role);
                }else{
                    String departmentFullPath = departmentService.getFullPath(departmentId);
                    String [] array = departmentFullPath.split(",");
                    if(extendParam - 1 < array.length){
                        Role role = roleService.queryRole(RoleTypeEnum.DEPARTMENT_MANAGE_PERMISSION,Integer.parseInt(array[extendParam - 1]));
                        checkAddRole(roleList,role);
                    }
                }
            }
        }
        return roleList;
    }

    @Transactional
    @Override
    public ResultCode submit(User applyUser, OrderTypeEnum orderTypeEnum, String reason, Map<String,Object> extendConfig) throws Exception{
        Validate.notNull(applyUser);
        Validate.notNull(orderTypeEnum);
        Order order = new Order();
        LocalDateTime localDateTime = LocalDateTime.now();
        order.setCreateTime(localDateTime);
        order.setUpdateTime(localDateTime);
        order.setOrderType(orderTypeEnum);
        order.setState(OrderStateEnum.PROCESSING);
        order.setReason(reason);
        order.setExtendConfig(extendConfig);
        order.setUserId(applyUser.getId());
        String hash = null;
        List<Role> roleList = null;
        if(order.getOrderType() == OrderTypeEnum.PROJECT_ACCESS){
            if(!extendConfig.containsKey("projectId")){
                return ResultCode.paramValidateFailed;
            }
            Integer projectId = (Integer) extendConfig.get("projectId");
            Project project = projectService.queryById(projectId);
            Validate.notNull(project);
            String message = order.getUserId() + "_" + order.getOrderType() + "_" + OrderStateEnum.PROCESSING + "_" + projectId;
            hash = Md5Util.getMD5(message);
            roleList = getApproveRoleList(applyUser,orderTypeEnum,project);
        }else if(order.getOrderType() == OrderTypeEnum.STAT_ACCESS){
            if(!extendConfig.containsKey("statId")){
                return ResultCode.paramValidateFailed;
            }
            Integer statId = (Integer) extendConfig.get("statId");
            Stat stat = statService.queryById(statId);
            Validate.notNull(stat);
            String message = order.getUserId() + "_" + order.getOrderType() + "_" + OrderStateEnum.PROCESSING + "_" + statId;
            hash = Md5Util.getMD5(message);
            roleList = getApproveRoleList(applyUser,orderTypeEnum,stat);
        }else if(order.getOrderType() == OrderTypeEnum.VIEW_ACCESS){
            if(!extendConfig.containsKey("viewId")){
                return ResultCode.paramValidateFailed;
            }
            Integer viewId = (Integer) extendConfig.get("viewId");
            View view = viewService.queryById(viewId);
            Validate.notNull(view);
            String message = order.getUserId() + "_" + order.getOrderType() + "_" + OrderStateEnum.PROCESSING + "_" + viewId;
            hash = Md5Util.getMD5(message);
            roleList = getApproveRoleList(applyUser,orderTypeEnum,view);
        }else if(order.getOrderType() == OrderTypeEnum.USER_PEND_APPROVE){
            String message = order.getUserId() + "_" + order.getOrderType() + "_" + OrderStateEnum.PROCESSING;
            hash = Md5Util.getMD5(message);
            roleList = getApproveRoleList(applyUser,orderTypeEnum,null);
        }else if(order.getOrderType() == OrderTypeEnum.LIMITING_SETTINGS){
            if(!extendConfig.containsKey("groupId")){
                return ResultCode.paramValidateFailed;
            }
            Integer groupId = (Integer) extendConfig.get("groupId");
            String strategy = (String) extendConfig.get("strategy");
            String message = order.getUserId() + "_" + order.getOrderType() + "_" + OrderStateEnum.PROCESSING + "_" + groupId + "_" + strategy;
            hash = Md5Util.getMD5(message);
            roleList = getApproveRoleList(applyUser,orderTypeEnum,null);
        }else if(order.getOrderType() == OrderTypeEnum.CALLER_PROJECT_ACCESS){
            if(!extendConfig.containsKey("projectId")){
                return ResultCode.paramValidateFailed;
            }
            if(!extendConfig.containsKey("callerId")){
                return ResultCode.paramValidateFailed;
            }
            Integer projectId = (Integer) extendConfig.get("projectId");
            Project project = projectService.queryById(projectId);
            Validate.notNull(project);
            Integer callerId = (Integer) extendConfig.get("callerId");
            Caller caller = callerService.queryById(callerId);
            Validate.notNull(caller);
            String message = order.getUserId() + "_" + order.getOrderType() + "_" + OrderStateEnum.PROCESSING + "_" + callerId + "_" + projectId;
            hash = Md5Util.getMD5(message);
            roleList = getApproveRoleList(applyUser,orderTypeEnum,project);
        }else if(order.getOrderType() == OrderTypeEnum.CALLER_STAT_ACCESS){
            if(!extendConfig.containsKey("statId")){
                return ResultCode.paramValidateFailed;
            }
            if(!extendConfig.containsKey("callerId")){
                return ResultCode.paramValidateFailed;
            }
            Integer statId = (Integer) extendConfig.get("statId");
            Stat stat = statService.queryById(statId);
            Validate.notNull(stat);
            Integer callerId = (Integer) extendConfig.get("callerId");
            Caller caller = callerService.queryById(callerId);
            Validate.notNull(caller);
            String message = order.getUserId() + "_" + order.getOrderType() + "_" + OrderStateEnum.PROCESSING + "_" + callerId + "_" + statId;
            hash = Md5Util.getMD5(message);
            roleList = getApproveRoleList(applyUser,orderTypeEnum,stat);
        }else if(order.getOrderType() == OrderTypeEnum.CALLER_VIEW_ACCESS){
            if(!extendConfig.containsKey("viewId")){
                return ResultCode.paramValidateFailed;
            }
            if(!extendConfig.containsKey("callerId")){
                return ResultCode.paramValidateFailed;
            }
            Integer viewId = (Integer) extendConfig.get("viewId");
            View view = viewService.queryById(viewId);
            Validate.notNull(view);
            Integer callerId = (Integer) extendConfig.get("callerId");
            Caller caller = callerService.queryById(callerId);
            Validate.notNull(caller);
            String message = order.getUserId() + "_" + order.getOrderType() + "_" + OrderStateEnum.PROCESSING + "_" + callerId + "_" + viewId;
            hash = Md5Util.getMD5(message);
            roleList = getApproveRoleList(applyUser,orderTypeEnum,view);
        }else{
            return ResultCode.orderTypeNotExists;
        }
        boolean isExist = orderDao.isExist(hash);
        if(isExist){
            return ResultCode.orderCreateRepeatSubmit;
        }
        order.setHash(hash);
        Validate.isTrue(CollectionUtils.isNotEmpty(roleList));
        order.setSteps(roleList.stream().map(Role::getId).collect(Collectors.toList()));
        order.setCurrentNode(CollectionUtils.isNotEmpty(roleList)?roleList.get(0).getId():null);
        orderDao.insert(order);
        int orderId = order.getId();
        List<OrderDetail> detailList = new ArrayList<>();
        for(int i=0;i<roleList.size();i++){
            Role role = roleList.get(i);
            OrderDetail orderDetail = new OrderDetail();
            RoleTypeEnum roleType = role.getRoleType();
            orderDetail.setCreateTime(localDateTime);
            orderDetail.setOrderId(orderId);
            orderDetail.setRoleType(roleType);
            orderDetail.setUserId(applyUser.getId());
            orderDetail.setRoleId(role.getId());
            orderDetail.setState(i == 0? ApproveStateEnum.PENDING:ApproveStateEnum.WAIT);
            detailList.add(orderDetail);
        }
        orderDetailDao.batchInsert(detailList);
        return ResultCode.success;
    }

    @Override
    public ResultCode batchSubmit(User applyUser, OrderTypeEnum orderTypeEnum, String reason, Map<String, Object> extendConfig) throws Exception {
        return null;
    }

    private OrderVO translateApproveEntity(Order order) throws Exception{
        Integer currentUserId = baseService.getCurrentUserId();
        OrderVO orderDto = new OrderVO(order);
        int applyUserId = orderDto.getUserId();
        orderDto.addPermission(PermissionEnum.AccessAble);
        Integer currentNode = order.getCurrentNode();
        if(permissionDao.existPermission(currentUserId,OwnerTypeEnum.USER,currentNode)){
            orderDto.addPermission(PermissionEnum.ManageAble);
        }
        User user = userService.cacheQueryById(applyUserId);
        orderDto.setUser(user);
        Object relatedObject = queryRelatedElement(order);
        orderDto.setExtend(relatedObject);
        return orderDto;
    }

    private OrderVO translateApplyEntity(Order order) throws Exception {
        OrderVO orderDto = new OrderVO(order);
        int applyUserId = orderDto.getUserId();
        if(orderDto.getState() == OrderStateEnum.PROCESSING){
            orderDto.addPermission(PermissionEnum.ManageAble);
        }else{
            orderDto.addPermission(PermissionEnum.AccessAble);
        }
        User user = userService.cacheQueryById(applyUserId);
        orderDto.setUser(user);
        Object relatedObject = queryRelatedElement(order);
        orderDto.setExtend(relatedObject);
        return orderDto;
    }

    @Override
    public Object queryRelatedElement(Order order) throws Exception {
        Map<String,Object> configMap = order.getExtendConfig();
        if(order.getOrderType() == OrderTypeEnum.PROJECT_ACCESS){
            Integer projectId = (Integer) configMap.get("projectId");
            return projectService.queryById(projectId);
        }else if(order.getOrderType() == OrderTypeEnum.STAT_ACCESS){
            Integer statId = (Integer) configMap.get("statId");
            return statService.queryById(statId);
        }else if(order.getOrderType() == OrderTypeEnum.VIEW_ACCESS){
            Integer viewId = (Integer) configMap.get("viewId");
            return viewService.queryById(viewId);
        }else if(order.getOrderType() == OrderTypeEnum.LIMITING_SETTINGS){
            Integer groupId = (Integer) configMap.get("groupId");
            return groupService.queryById(groupId);
        }else if(order.getOrderType() == OrderTypeEnum.USER_PEND_APPROVE){
            Integer userId = order.getUserId();
            return userService.cacheQueryById(userId);
        }else if(order.getOrderType() == OrderTypeEnum.CALLER_PROJECT_ACCESS){
            Integer projectId = (Integer) configMap.get("projectId");
            Project project = projectService.queryById(projectId);
            if(project == null){
                return null;
            }
            Integer callerId = (Integer) configMap.get("callerId");
            Caller caller = callerService.queryById(callerId);
            if(caller == null){
                return null;
            }
            Integer expired = (Integer) configMap.get("expired");
            if(expired == null){
                return null;
            }
            HashMap<String,Object> extendMap = new HashMap<>();
            extendMap.put("project",project);
            extendMap.put("caller",caller);
            extendMap.put("expired",expired);
            return extendMap;
        }else if(order.getOrderType() == OrderTypeEnum.CALLER_STAT_ACCESS){
            Integer statId = (Integer) configMap.get("statId");
            Stat stat = statService.queryById(statId);
            if(stat == null){
                return null;
            }
            Integer callerId = (Integer) configMap.get("callerId");
            Caller caller = callerService.queryById(callerId);
            if(caller == null){
                return null;
            }
            Integer expired = (Integer) configMap.get("expired");
            if(expired == null){
                return null;
            }
            HashMap<String,Object> extendMap = new HashMap<>();
            extendMap.put("stat",stat);
            extendMap.put("caller",caller);
            extendMap.put("expired",expired);
            return extendMap;
        }else if(order.getOrderType() == OrderTypeEnum.CALLER_VIEW_ACCESS){
            Integer viewId = (Integer) configMap.get("viewId");
            View view = viewService.queryById(viewId);
            if(view == null){
                return null;
            }
            Integer callerId = (Integer) configMap.get("callerId");
            Caller caller = callerService.queryById(callerId);
            if(caller == null){
                return null;
            }
            Integer expired = (Integer) configMap.get("expired");
            if(expired == null){
                return null;
            }
            HashMap<String,Object> extendMap = new HashMap<>();
            extendMap.put("view",view);
            extendMap.put("caller",caller);
            extendMap.put("expired",expired);
            return extendMap;
        }
        return null;
    }

    @Transactional
    @Override
    public int process(OrderProcessParam processParam) throws Exception{
        int currentUserId = baseService.getCurrentUserId();
        int state = processParam.getState();
        Order order = orderDao.queryById(processParam.getId());
        Validate.notNull(order);
        Validate.isTrue(order.getState() == OrderStateEnum.PROCESSING);
        int result = 0;
        if(state == 1){
            int currentNode = order.getCurrentNode();
            Validate.isTrue(permissionService.checkUserPermission(currentUserId,currentNode));
            result = agreeOrder(currentUserId,processParam,order);
        }else if(state == 2){
            int currentNode = order.getCurrentNode();
            Validate.isTrue(permissionService.checkUserPermission(currentUserId,currentNode));
            result = rejectOrder(currentUserId,processParam,order);
        }else if(state == 3){
            Validate.isTrue(currentUserId == order.getUserId());
            result = retractOrder(currentUserId,processParam,order);
        }
        return result;
    }

    private int agreeOrder(Integer currentUserId, OrderProcessParam processParam, Order order) throws Exception{
        LocalDateTime localDateTime = LocalDateTime.now();
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setRoleId(order.getCurrentNode());
        orderDetail.setReply(processParam.getReply());
        orderDetail.setOrderId(processParam.getId());
        orderDetail.setUserId(currentUserId);
        orderDetail.setProcessTime(localDateTime);
        orderDetail.setState(ApproveStateEnum.APPROVED);
        List<Integer> steps = order.getSteps();
        int stepIndex = steps.indexOf(order.getCurrentNode());
        orderDetailDao.updateDetail(orderDetail);
        if(stepIndex < steps.size() - 1){
            order.setCurrentNode(steps.get(stepIndex + 1));
        }else{
            order.setCurrentNode(0);
            order.setState(OrderStateEnum.APPROVED);
            orderAgreeCallback(order);
        }
        order.setUpdateTime(localDateTime);
        return orderDao.update(order);
    }

    private int retractOrder(Integer currentUserId,OrderProcessParam processParam,Order order){
        LocalDateTime localDateTime = LocalDateTime.now();
        List<OrderDetail> orderDetails = new ArrayList<>();
        List<Integer> steps = order.getSteps();
        int stepIndex = steps.indexOf(order.getCurrentNode());
        for(int i = stepIndex ;i < steps.size();i++){
            int roleId = steps.get(i);
            OrderDetail remainDetail = new OrderDetail();
            remainDetail.setProcessTime(localDateTime);
            remainDetail.setRoleId(roleId);
            remainDetail.setOrderId(processParam.getId());
            remainDetail.setState(ApproveStateEnum.SUSPEND);
            orderDetails.add(remainDetail);
        }
        order.setUpdateTime(LocalDateTime.now());
        order.setState(OrderStateEnum.RETRACTED);
        order.setCurrentNode(0);
        for (OrderDetail orderDetail : orderDetails) {
            orderDetailDao.updateDetail(orderDetail);
        }
        return orderDao.update(order);
    }

    private int rejectOrder(Integer currentUserId, OrderProcessParam processParam, Order order){
        List<OrderDetail> orderDetails = new ArrayList<>();
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setRoleId(order.getCurrentNode());
        orderDetail.setReply(processParam.getReply());
        orderDetail.setOrderId(processParam.getId());
        LocalDateTime localDateTime = LocalDateTime.now();
        orderDetail.setProcessTime(localDateTime);
        orderDetail.setUserId(currentUserId);
        orderDetail.setState(ApproveStateEnum.REJECTED);
        orderDetails.add(orderDetail);
        List<Integer> steps = order.getSteps();
        int stepIndex = steps.indexOf(order.getCurrentNode());
        if(stepIndex != steps.size() - 1){
            for(int i = stepIndex + 1;i < steps.size();i++){
                int roleId = steps.get(i);
                OrderDetail remainDetail = new OrderDetail();
                remainDetail.setProcessTime(localDateTime);
                remainDetail.setRoleId(roleId);
                remainDetail.setOrderId(processParam.getId());
                remainDetail.setState(ApproveStateEnum.SUSPEND);
                orderDetails.add(remainDetail);
            }
        }
        order.setUpdateTime(LocalDateTime.now());
        order.setState(OrderStateEnum.REJECTED);
        order.setCurrentNode(0);
        for (OrderDetail detail : orderDetails) {
            orderDetailDao.updateDetail(detail);
        }
        int result = orderDao.update(order);
        orderRejectCallback(order);
        return result;
    }

    private void orderAgreeCallback(Order order) throws Exception {
        Integer userId = order.getUserId();
        if(order.getOrderType() == OrderTypeEnum.USER_PEND_APPROVE){
            User userUpdateParam = new User();
            userUpdateParam.setId(userId);
            userUpdateParam.setState(UserStateEnum.USER_NORMAL);
            userUpdateParam.setUpdateTime(LocalDateTime.now());
            userService.update(userUpdateParam);
        }else if(order.getOrderType() == OrderTypeEnum.PROJECT_ACCESS){
            Integer projectId = (Integer) order.getExtendConfig().get("projectId");
            Role role = roleService.cacheQueryRole(RoleTypeEnum.PROJECT_ACCESS_PERMISSION,projectId);
            Validate.notNull(role);
            permissionService.grantPermission(userId,OwnerTypeEnum.USER,role.getId());
        }else if(order.getOrderType() == OrderTypeEnum.STAT_ACCESS){
            Integer statId = (Integer) order.getExtendConfig().get("statId");
            Role role = roleService.cacheQueryRole(RoleTypeEnum.STAT_ACCESS_PERMISSION,statId);
            Validate.notNull(role);
            permissionService.grantPermission(userId,OwnerTypeEnum.USER,role.getId());
        }else if(order.getOrderType() == OrderTypeEnum.VIEW_ACCESS){
            Integer viewId = (Integer) order.getExtendConfig().get("viewId");
            Role role = roleService.cacheQueryRole(RoleTypeEnum.VIEW_ACCESS_PERMISSION,viewId);
            Validate.notNull(role);
            permissionService.grantPermission(userId,OwnerTypeEnum.USER,role.getId());
        }else if(order.getOrderType() == OrderTypeEnum.CALLER_PROJECT_ACCESS){
            Integer projectId = (Integer) order.getExtendConfig().get("projectId");
            Role role = roleService.cacheQueryRole(RoleTypeEnum.PROJECT_ACCESS_PERMISSION,projectId);
            Validate.notNull(role);
            Integer callerId = (Integer) order.getExtendConfig().get("callerId");
            Integer expired = (Integer) order.getExtendConfig().get("expired");
            permissionService.grantPermission(callerId,OwnerTypeEnum.CALLER,role.getId(),expired);
        }else if(order.getOrderType() == OrderTypeEnum.CALLER_STAT_ACCESS){
            Integer statId = (Integer) order.getExtendConfig().get("statId");
            Role role = roleService.cacheQueryRole(RoleTypeEnum.STAT_ACCESS_PERMISSION,statId);
            Validate.notNull(role);
            Integer callerId = (Integer) order.getExtendConfig().get("callerId");
            Integer expired = (Integer) order.getExtendConfig().get("expired");
            permissionService.grantPermission(callerId,OwnerTypeEnum.CALLER,role.getId(),expired);
        }else if(order.getOrderType() == OrderTypeEnum.CALLER_VIEW_ACCESS){
            Integer viewId = (Integer) order.getExtendConfig().get("viewId");
            Role role = roleService.cacheQueryRole(RoleTypeEnum.VIEW_ACCESS_PERMISSION,viewId);
            Validate.notNull(role);
            Integer callerId = (Integer) order.getExtendConfig().get("callerId");
            Integer expired = (Integer) order.getExtendConfig().get("expired");
            permissionService.grantPermission(callerId,OwnerTypeEnum.CALLER,role.getId(),expired);
        }else if(order.getOrderType() == OrderTypeEnum.LIMITING_SETTINGS){
            Integer groupId = (Integer) order.getExtendConfig().get("groupId");
            String strategy = (String) order.getExtendConfig().get("strategy");
            Integer updateValue = (Integer) order.getExtendConfig().get("updateValue");
            Validate.isTrue(updateValue > 0);
            LimitingStrategyEnum limitingStrategyEnum = LimitingStrategyEnum.getEnum(strategy);
            Group group = groupService.queryById(groupId);
            GroupExtendConfig groupExtendConfig = group.getExtendConfig();
            groupExtendConfig.getLimitingConfig().put(limitingStrategyEnum,updateValue);
            group.setRefreshTime(LocalDateTime.now());
            if(group.getState() == GroupStateEnum.LIMITING){
                group.setState(GroupStateEnum.RUNNING);
            }
            groupService.update(group);
        }
    }

    private void orderRejectCallback(Order order){
        if(order.getOrderType() == OrderTypeEnum.USER_PEND_APPROVE){
            User userUpdateParam = new User();
            userUpdateParam.setId(order.getUserId());
            userUpdateParam.setState(UserStateEnum.USER_REJECT);
            userUpdateParam.setUpdateTime(LocalDateTime.now());
            userService.update(userUpdateParam);
        }
    }

    @Override
    @Cacheable(value = "ShortPeriod",key = "#targetClass + '_' + 'pendCount' + '_' +  #userId",cacheManager = "caffeineCacheManager",unless = "#result == null")
    public int pendCount(int userId) {
        return orderDao.pendCount(userId);
    }
}
