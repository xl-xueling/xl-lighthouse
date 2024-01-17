package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.enums.UserStateEnum;
import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dao.DepartmentDao;
import com.dtstep.lighthouse.insights.dao.UserDao;
import com.dtstep.lighthouse.insights.dto.UserCreateParam;
import com.dtstep.lighthouse.insights.dto_bak.*;
import com.dtstep.lighthouse.insights.enums.OrderTypeEnum;
import com.dtstep.lighthouse.insights.enums.OwnerTypeEnum;
import com.dtstep.lighthouse.insights.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.*;
import com.dtstep.lighthouse.insights.service.*;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Autowired
    private ResourceService resourceService;

    @Transactional
    @Override
    public int create(User user, boolean needApprove) {
        user.setState(needApprove?UserStateEnum.USER_PEND:UserStateEnum.USR_NORMAL);
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
            order.setOrderType(OrderTypeEnum.USER_PEND_APPROVE);
            orderService.create(order);
        }
        return userId;
    }

    @Override
    public int update(User user) {
        user.setUpdateTime(LocalDateTime.now());
        return userDao.update(user);
    }

    @Override
    public User queryBasicInfoById(int id) {
        return userDao.queryBasicInfoById(id);
    }

    @Override
    @Cacheable(value = "LongPeriod",key = "#targetClass + '_' + 'queryById' + '_' + #id",cacheManager = "caffeineCacheManager",unless = "#result == null")
    public User cacheQueryById(int id) {
        return userDao.queryBasicInfoById(id);
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

    private UserDto translate(User user,PermissionInfo.PermissionEnum permission){
        UserDto userDto = new UserDto(user);
        userDto.addPermission(permission);
        return userDto;
    }

    @Override
    public ListData<UserDto> queryList(UserQueryParam queryParam, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        ListData<UserDto> listData = null;
        try{
            List<User> userList = userDao.queryList(queryParam);
            List<UserDto> dtoList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(userList)){
                int userId = baseService.getCurrentUserId();
                Role optManageRole = roleService.cacheQueryRole(RoleTypeEnum.OPT_MANAGE_PERMISSION,0);
                boolean hasOptManageRole = permissionService.checkUserPermission(userId,optManageRole.getId());
                Role systemManageRole = roleService.cacheQueryRole(RoleTypeEnum.FULL_MANAGE_PERMISSION,0);
                boolean hasSysManageRole = permissionService.checkUserPermission(userId,systemManageRole.getId());
                PermissionInfo.PermissionEnum managePermission = hasOptManageRole? PermissionInfo.PermissionEnum.ManageAble :null;
                for(int i=0;i<userList.size();i++){
                    UserDto userDto = translate(userList.get(i),managePermission);
                    dtoList.add(userDto);
                }
            }
            listData = baseService.translateToListData(dtoList);
        }finally {
            PageHelper.clearPage();
        }
        return listData;
    }

    @Override
    public List<User> termQuery(String search) {
        return userDao.termQuery(search);
    }

    @Transactional
    @Override
    public int deleteById(int id) {
        PermissionQueryParam queryParam = new PermissionQueryParam();
        queryParam.setOwnerId(id);
        queryParam.setOwnerType(OwnerTypeEnum.USER);
        permissionService.delete(queryParam);
        return userDao.deleteById(id);
    }

    @Override
    public int count(UserQueryParam queryParam) {
        return userDao.count(queryParam);
    }
}
