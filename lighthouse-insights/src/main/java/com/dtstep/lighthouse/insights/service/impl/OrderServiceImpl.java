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
    public ListData<OrderVO> queryApproveList(OrderQueryParam queryParam, Integer pageNum, Integer pageSize) {
        Integer currentUserId = baseService.getCurrentUserId();
        queryParam.setApproveUserId(currentUserId);
        PageHelper.startPage(pageNum,pageSize);
        List<OrderVO> orderDtoList = new ArrayList<>();
        try{
            List<Order> orders = orderDao.queryApproveList(queryParam,pageNum,pageSize);
            for(Order order : orders){
                OrderVO orderDto = translate(order);
                orderDtoList.add(orderDto);
            }
        }finally {
            PageHelper.clearPage();
        }
        return baseService.translateToListData(orderDtoList);
    }

    @Override
    public OrderVO queryById(Integer id) {
        Order order = orderDao.queryById(id);
        Validate.notNull(order);
        OrderVO orderVO = translate(order);
        List<OrderDetailDto> orderDetails = orderDetailService.queryList(id);
        orderVO.setOrderDetails(orderDetails);
        List<Integer> roleIds = orderVO.getSteps();
        HashMap<Integer,List<User>> adminsMap = new HashMap<>();
        for(Integer roleId : roleIds){
            List<User> admins = new ArrayList<>();
            List<Integer> adminIds = permissionDao.queryUserPermissionsByRoleId(roleId,4);
            for(Integer approveUserId : adminIds){
                User user = userService.cacheQueryById(approveUserId);
                admins.add(user);
            }
            adminsMap.put(roleId,admins);
        }
        return orderVO;
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
        Validate.notNull(applyUser);
        Validate.notNull(orderTypeEnum);
        Order order = new Order();
        LocalDateTime localDateTime = LocalDateTime.now();
        order.setCreateTime(localDateTime);
        order.setUpdateTime(localDateTime);
        order.setOrderType(orderTypeEnum);
        order.setState(OrderStateEnum.PROCESSING);
        Map<String,Object> configMap = order.getExtendConfig();
        List<Integer> steps = new ArrayList<>();
        Map<Integer,RoleTypeEnum> roleTypeMap = new HashMap<>();
        String hash;
        List<Role> roleList = getApproveRoleList(applyUser,orderTypeEnum,param);
        order.setSteps(roleList.stream().map(z -> z.getId()).collect(Collectors.toList()));
        order.setCurrentNode(CollectionUtils.isNotEmpty(roleList)?roleList.get(0).getId():null);
        if(order.getOrderType() == OrderTypeEnum.PROJECT_ACCESS){

        }else if(order.getOrderType() == OrderTypeEnum.USER_PEND_APPROVE){
            User user = (User) param;
            String message = order.getOrderType() + "_" + order.getUserId();
            order.setHash(Md5Util.getMD5(message));
        }
        order.setUserId(applyUser.getId());
        orderDao.insert(order);
        int orderId = order.getId();
        List<OrderDetail> detailList = new ArrayList<>();
        for(int i=0;i<roleList.size();i++){
            Role role = roleList.get(i);
            OrderDetail orderDetail = new OrderDetail();
            RoleTypeEnum roleType = roleTypeMap.get(role.getId());
            orderDetail.setCreateTime(localDateTime);
            orderDetail.setOrderId(orderId);
            orderDetail.setRoleType(roleType);
            orderDetail.setUserId(applyUser.getId());
            orderDetail.setRoleId(role.getId());
            orderDetail.setState(i == 0?ApproveStateEnum.PENDING:ApproveStateEnum.WAIT);
            detailList.add(orderDetail);
        }
        orderDetailDao.batchInsert(detailList);
        return orderId;
    }

    @Override
    public ListData<Order> queryApplyList(OrderQueryParam queryParam, Integer pageNum, Integer pageSize) {
        List<Order> orders = orderDao.queryApplyList(queryParam,pageNum,pageSize);
        ListData<Order> listData = new ListData<>();
        listData.setList(orders);
        return listData;
    }

    private OrderVO translate(Order order){
        Integer currentUserId = baseService.getCurrentUserId();
        OrderVO orderDto = new OrderVO(order);
        int applyUserId = orderDto.getUserId();
        List<Integer> roleIds = order.getSteps();
        orderDto.addPermission(PermissionInfo.PermissionEnum.AccessAble);
        Integer currentNode = order.getCurrentNode();
        if(permissionDao.existPermission(currentUserId,OwnerTypeEnum.USER,currentNode)){
            orderDto.addPermission(PermissionInfo.PermissionEnum.ManageAble);
        }
        User user = userService.cacheQueryById(applyUserId);
        orderDto.setUser(user);
        return orderDto;
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
