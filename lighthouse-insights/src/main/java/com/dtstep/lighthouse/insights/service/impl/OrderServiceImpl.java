package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.entity.FlowNode;
import com.dtstep.lighthouse.common.enums.OrderStateEnum;
import com.dtstep.lighthouse.common.enums.OrderTypeEnum;
import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.common.enums.UserStateEnum;
import com.dtstep.lighthouse.common.util.Md5Util;
import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dao.OrderDao;
import com.dtstep.lighthouse.insights.dao.OrderDetailDao;
import com.dtstep.lighthouse.insights.dao.PermissionDao;
import com.dtstep.lighthouse.insights.dto_bak.*;
import com.dtstep.lighthouse.insights.enums.*;
import com.dtstep.lighthouse.insights.modal.*;
import com.dtstep.lighthouse.insights.service.*;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
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

    @Autowired
    private ProjectService projectService;

    @Autowired
    private DepartmentService departmentService;

    @Override
    public ExtendOrderDto queryById(Integer id) {
        Order order = orderDao.queryById(id);
        if(order != null){
            return translateExtend(order);
        }else{
            return null;
        }
    }

    private void checkAddRole(List<Role> list,Role role){
        List<Integer> roleIds = list.stream().map(z -> z.getId()).collect(Collectors.toList());
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
                if(orderTypeEnum == OrderTypeEnum.PROJECT_ACCESS){
                    Project project = (Project) param;
                    Role role = roleService.queryRole(roleTypeEnum,project.getId());
                    checkAddRole(roleList,role);
                }else if(orderTypeEnum == OrderTypeEnum.STAT_ACCESS){
                    Stat stat = (Stat) param;
                    Role role = roleService.queryRole(roleTypeEnum,stat.getProjectId());
                    checkAddRole(roleList,role);
                }
            }else if(roleTypeEnum == RoleTypeEnum.METRIC_MANAGE_PERMISSION){
                MetricSet metricSet = (MetricSet) param;
                Role role = roleService.queryRole(roleTypeEnum, metricSet.getId());
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

    @Override
    public <T> int submit(User applyUser,OrderTypeEnum orderTypeEnum, T param) throws Exception{
        Order order = new Order();
        LocalDateTime localDateTime = LocalDateTime.now();
        order.setCreateTime(localDateTime);
        order.setUpdateTime(localDateTime);
        order.setState(OrderStateEnum.PROCESSING);
        Map<String,Object> configMap = order.getExtendConfig();
        List<Integer> steps = new ArrayList<>();
        Map<Integer,RoleTypeEnum> roleTypeMap = new HashMap<>();
        String hash;
        List<Role> roleList = getApproveRoleList(applyUser,orderTypeEnum,param);
        order.setSteps(roleList.stream().map(z -> z.getId()).collect(Collectors.toList()));
        if(order.getOrderType() == OrderTypeEnum.PROJECT_ACCESS){

        }else if(order.getOrderType() == OrderTypeEnum.USER_PEND_APPROVE){
            User user = (User) param;
            String message = order.getOrderType() + "_" + order.getUserId();
            order.setHash(Md5Util.getMD5(message));
        }
        orderDao.insert(order);
        int orderId = order.getId();
        for(int i=0;i<roleList.size();i++){
            Role role = roleList.get(i);
            OrderDetail orderDetail = new OrderDetail();
            RoleTypeEnum roleType = roleTypeMap.get(role.getId());
            orderDetail.setCreateTime(localDateTime);
            orderDetail.setOrderId(orderId);
            orderDetail.setRoleType(roleType);
            orderDetail.setState(i == 0?ApproveStateEnum.PENDING:ApproveStateEnum.WAIT);
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
        Integer currentUserId = baseService.getCurrentUserId();
        OrderDto orderDto = new OrderDto(order);
        int applyUserId = orderDto.getUserId();
        List<Integer> roleIds = order.getSteps();
        for(Integer roleId : roleIds){
            boolean hashPermission = permissionDao.checkUserPermission(currentUserId,roleId);
            if(hashPermission){
                orderDto.addPermission(PermissionInfo.PermissionEnum.AccessAble);
            }
        }
        Integer currentNode = order.getCurrentNode();
        if(permissionDao.checkUserPermission(currentUserId,currentNode)){
            orderDto.addPermission(PermissionInfo.PermissionEnum.ManageAble);
        }
        User user = userService.cacheQueryById(applyUserId);
        orderDto.setUser(user);
        return orderDto;
    }


    private ExtendOrderDto translateExtend(Order order){
        Integer currentUserId = baseService.getCurrentUserId();
        ExtendOrderDto extendOrderDto = new ExtendOrderDto(order);
        int applyUserId = extendOrderDto.getUserId();
        List<Integer> roleIds = order.getSteps();
        HashMap<Integer,List<User>> adminsMap = new HashMap<>();
        for(Integer roleId : roleIds){
            PermissionQueryParam permissionQueryParam = new PermissionQueryParam();
            permissionQueryParam.setRoleId(roleId);
            permissionQueryParam.setOwnerType(OwnerTypeEnum.USER);
            List<User> admins = new ArrayList<>();
            PageHelper.startPage(1,4);
            List<Permission> permissions = null;
            try{
                permissions = permissionDao.queryList(permissionQueryParam);
            }finally {
                PageHelper.clearPage();
            }
            if(CollectionUtils.isNotEmpty(permissions)){
                List<Integer> userIdList = permissions.stream().map(z -> z.getOwnerId()).collect(Collectors.toList());
                for(Integer approveUserId : userIdList){
                    User user = userService.cacheQueryById(approveUserId);
                    admins.add(user);
                }
            }
            adminsMap.put(roleId,admins);
            boolean hashPermission = permissionDao.checkUserPermission(currentUserId,roleId);
            if(hashPermission){
                extendOrderDto.addPermission(PermissionInfo.PermissionEnum.AccessAble);
            }
        }
        Integer currentNode = order.getCurrentNode();
        if(permissionDao.checkUserPermission(currentUserId,currentNode)){
            extendOrderDto.addPermission(PermissionInfo.PermissionEnum.ManageAble);
        }
        extendOrderDto.setAdminsMap(adminsMap);
        User user = userService.cacheQueryById(applyUserId);
        List<OrderDetailDto> orderDetails = orderDetailService.queryList(order.getId());
        extendOrderDto.setOrderDetails(orderDetails);
        extendOrderDto.setUser(user);
        return extendOrderDto;
    }

    @Override
    public ListData<OrderDto> queryApproveList(OrderQueryParam queryParam, Integer pageNum, Integer pageSize) {
        Integer currentUserId = baseService.getCurrentUserId();
        queryParam.setApproveUserId(currentUserId);
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
    public void approve(OrderApproveParam approveParam) {
        int result = approveParam.getResult();
        Order order = orderDao.queryById(approveParam.getId());
        Validate.isTrue(approveParam.getRoleId().intValue() == order.getCurrentNode().intValue());
        Validate.notNull(order);
        List<Integer> steps = order.getSteps();
        List<OrderDetail> orderDetails = new ArrayList<>();
        int stepIndex = steps.indexOf(order.getCurrentNode());
        LocalDateTime localDateTime = LocalDateTime.now();
        if(result == 1){
            order.setState(OrderStateEnum.APPROVED);
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setRoleId(approveParam.getRoleId());
            orderDetail.setReply(approveParam.getReply());
            orderDetail.setOrderId(approveParam.getId());
            orderDetail.setUserId(approveParam.getUserId());
            orderDetail.setProcessTime(localDateTime);
            orderDetail.setState(ApproveStateEnum.APPROVED);
            if(stepIndex != steps.size() - 1){
                order.setCurrentNode(steps.get(stepIndex + 1));
            }else{
                order.setCurrentNode(0);
            }
            orderDetails.add(orderDetail);
            approveCallback(order);
        }else if(result == 2){
            order.setState(OrderStateEnum.REJECTED);
            order.setCurrentNode(0);
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setRoleId(approveParam.getRoleId());
            orderDetail.setReply(approveParam.getReply());
            orderDetail.setOrderId(approveParam.getId());
            orderDetail.setProcessTime(localDateTime);
            orderDetail.setUserId(approveParam.getUserId());
            orderDetail.setState(ApproveStateEnum.REJECTED);
            orderDetails.add(orderDetail);
            if(stepIndex != steps.size() - 1){
                for(int i = stepIndex + 1;i < steps.size();i++){
                    int roleId = steps.get(i);
                    OrderDetail tempOrderDetail = new OrderDetail();
                    tempOrderDetail.setProcessTime(localDateTime);
                    tempOrderDetail.setRoleId(roleId);
                    tempOrderDetail.setOrderId(approveParam.getId());
                    tempOrderDetail.setState(ApproveStateEnum.SUSPEND);
                    orderDetails.add(tempOrderDetail);
                }
            }
        }
        order.setUpdateTime(LocalDateTime.now());
        orderDao.update(order);
        for(int i=0;i<orderDetails.size();i++){
            orderDetailDao.updateDetail(orderDetails.get(i));
        }
    }

    private void approveCallback(Order order){
        if(order.getOrderType() == OrderTypeEnum.USER_PEND_APPROVE){
            User userUpdateParam = new User();
            userUpdateParam.setId(order.getUserId());
            userUpdateParam.setState(UserStateEnum.USR_NORMAL);
            userUpdateParam.setUpdateTime(LocalDateTime.now());
            userService.update(userUpdateParam);
        }
    }
}
