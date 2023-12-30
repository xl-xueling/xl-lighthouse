package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.enums.user.UserStateEnum;
import com.dtstep.lighthouse.common.util.Md5Util;
import com.dtstep.lighthouse.commonv2.constant.SystemConstant;
import com.dtstep.lighthouse.commonv2.enums.AuthRoleTypeEnum;
import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dao.DepartmentDao;
import com.dtstep.lighthouse.insights.dao.UserDao;
import com.dtstep.lighthouse.insights.dto.ChangePasswordParam;
import com.dtstep.lighthouse.insights.dto.UserQueryParam;
import com.dtstep.lighthouse.insights.dto.UserUpdateParam;
import com.dtstep.lighthouse.insights.enums.OrderTypeEnum;
import com.dtstep.lighthouse.insights.enums.OwnerTypeEnum;
import com.dtstep.lighthouse.insights.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.Order;
import com.dtstep.lighthouse.insights.modal.Permission;
import com.dtstep.lighthouse.insights.modal.Role;
import com.dtstep.lighthouse.insights.modal.User;
import com.dtstep.lighthouse.insights.service.OrderService;
import com.dtstep.lighthouse.insights.service.PermissionService;
import com.dtstep.lighthouse.insights.service.RoleService;
import com.dtstep.lighthouse.insights.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PermissionService permissionService;

    @Transactional
    @Override
    public int initAdmin() {
        int adminId;
        if(!isUserNameExist(SystemConstant.DEFAULT_ADMIN_USER)){
            User user = new User();
            user.setUsername(SystemConstant.DEFAULT_ADMIN_USER);
            user.setPassword(Md5Util.getMD5(SystemConstant.DEFAULT_PASSWORD));
            create(user,false);
            adminId = user.getId();
            Role role = roleService.queryRole(RoleTypeEnum.OPT_MANAGE_PERMISSION,0);
            Permission permission = new Permission();
            permission.setOwnerId(adminId);
            permission.setOwnerType(OwnerTypeEnum.USER);
            permission.setRoleId(role.getId());
            permissionService.create(permission);
        }
        return 0;
    }

    @Transactional
    @Override
    public int create(User user,boolean needApprove) {
        boolean isExist = userDao.isUserNameExist(user.getUsername());
        if(isExist){
            return -1;
        }
        if(needApprove){
            user.setState(UserStateEnum.USER_PEND);
        }else{
            user.setState(UserStateEnum.USR_NORMAL);
        }
        LocalDateTime localDateTime = LocalDateTime.now();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreateTime(localDateTime);
        user.setUpdateTime(localDateTime);
        user.setLastTime(localDateTime);
        userDao.insert(user);
        int userId = user.getId();
        if(needApprove){
            Order order = new Order();
            order.setUserId(userId);
            Role role = roleService.queryRole(RoleTypeEnum.OPT_MANAGE_PERMISSION,1);
            order.setSteps(List.of(role.getId()));
            order.setCurrentNode(role.getId());
            order.setCreateTime(localDateTime);
            order.setUpdateTime(localDateTime);
            String origin = userId + "_" + "register";
            order.setHash(Md5Util.getMD5(origin));
            order.setOrderType(OrderTypeEnum.USER_PEND_APPROVE);
            orderService.create(order);
        }
        return userId;
    }

    @Override
    public int update(UserUpdateParam user) {
        return userDao.update(user);
    }

    @Override
    public int changePassword(ChangePasswordParam updateParam) {
        updateParam.setPassword(passwordEncoder.encode(updateParam.getPassword()));
        return userDao.changePassword(updateParam);
    }

    @Override
    public User queryById(int id) {
        return userDao.queryById(id);
    }

    @Override
    public boolean isUserNameExist(String username) {
        return userDao.isUserNameExist(username);
    }

    @Override
    public User queryAllInfoById(int id) {
        return userDao.queryAllInfoById(id);
    }

    @Override
    public User queryByUserName(String userName) {
        return userDao.queryByUserName(userName);
    }

    @Override
    public ListData<User> queryList(UserQueryParam queryParam, Integer pageNum, Integer pageSize) {
        List<User> userList = userDao.queryList(queryParam,pageNum,pageSize);
        ListData<User> listData = new ListData<>();
        listData.setList(userList);
        int total = userDao.count(queryParam);
        listData.setTotal(total);
        listData.setPageNum(pageNum);
        listData.setPageSize(pageSize);
        return listData;
    }
}
