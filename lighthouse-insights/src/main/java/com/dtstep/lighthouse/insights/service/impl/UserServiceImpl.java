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
import com.dtstep.lighthouse.insights.modal.*;
import com.dtstep.lighthouse.insights.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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

    @Autowired
    private BaseService baseService;

    @Transactional
    @Override
    public void initAdmin() {
        if(!isUserNameExist(SystemConstant.DEFAULT_ADMIN_USER)){
            int adminId;
            User user = new User();
            user.setUsername(SystemConstant.DEFAULT_ADMIN_USER);
            user.setPassword(Md5Util.getMD5(SystemConstant.DEFAULT_PASSWORD));
            create(user,false);
            adminId = user.getId();
            Validate.isTrue(adminId != 0);
            Role role = roleService.queryRole(RoleTypeEnum.OPT_MANAGE_PERMISSION,0);
            Permission permission = new Permission();
            permission.setOwnerId(adminId);
            permission.setOwnerType(OwnerTypeEnum.USER);
            permission.setRoleId(role.getId());
            permissionService.create(permission);
        }
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
        Integer userId = user.getId();
        if(needApprove){
            Order order = new Order();
            order.setUserId(userId);
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
    @Cacheable(value = "normal",key = "#targetClass + '_' + 'queryById' + '_' + #id",cacheManager = "caffeineCacheManager",unless = "#result == null")
    public User cacheQueryById(int id) {
        System.out.println("cache query user by id:" + id);
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
        PageHelper.startPage(pageNum,pageSize);
        ListData<User> listData = null;
        try{
            List<User> userList = userDao.queryList(queryParam);
            listData = baseService.translateToListData(userList);
        }finally {
            PageHelper.clearPage();
        }
        return listData;
    }

    @Override
    public List<User> termQuery(String search) {
        return userDao.termQuery(search);
    }
}
