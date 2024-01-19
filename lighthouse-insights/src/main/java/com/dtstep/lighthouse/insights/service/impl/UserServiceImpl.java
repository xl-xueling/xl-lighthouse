package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.enums.UserStateEnum;
import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dao.DepartmentDao;
import com.dtstep.lighthouse.insights.dao.UserDao;
import com.dtstep.lighthouse.insights.dto.PermissionQueryParam;
import com.dtstep.lighthouse.insights.dto.UserQueryParam;
import com.dtstep.lighthouse.insights.dto_bak.*;
import com.dtstep.lighthouse.common.enums.OrderTypeEnum;
import com.dtstep.lighthouse.insights.enums.OwnerTypeEnum;
import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.*;
import com.dtstep.lighthouse.insights.service.*;
import com.dtstep.lighthouse.insights.vo.UserVO;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public int create(User user, boolean needApprove) throws Exception{
        user.setState(needApprove?UserStateEnum.USER_PEND:UserStateEnum.USER_NORMAL);
        LocalDateTime localDateTime = LocalDateTime.now();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreateTime(localDateTime);
        user.setUpdateTime(localDateTime);
        user.setLastTime(localDateTime);
        userDao.insert(user);
        int userId = user.getId();
        if(needApprove){
            orderService.submit(user,OrderTypeEnum.USER_PEND_APPROVE,null,null);
        }
        return userId;
    }

    @Override
    public int update(User user) {
        user.setUpdateTime(LocalDateTime.now());
        return userDao.update(user);
    }


    @Override
    @Cacheable(value = "LongPeriod",key = "#targetClass + '_' + 'queryById' + '_' + #id",cacheManager = "caffeineCacheManager",unless = "#result == null")
    public User cacheQueryById(int id) {
        return userDao.queryById(id);
    }

    @Override
    public boolean isUserNameExist(String username) {
        return userDao.isUserNameExist(username);
    }

    @Override
    public User queryById(int id) {
        return userDao.queryById(id);
    }

    @Override
    public User queryAllInfoByUserName(String userName) {
        return userDao.queryAllInfoByUserName(userName);
    }

    @Override
    public ListData<UserVO> queryList(UserQueryParam queryParam, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        ListData<UserVO> listData = null;
        try{
            List<User> userList = userDao.queryList(queryParam);
            List<UserVO> voList = new ArrayList<>();
            for(User user : userList){
                UserVO vo = new UserVO(user);
                vo.addPermission(PermissionEnum.ManageAble);
                voList.add(vo);
            }
            listData = baseService.translateToListData(voList);
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

    @Override
    public String queryUserPassword(Integer id) {
        return userDao.queryUserPassword(id);
    }

    @Override
    @Cacheable(value = "LongPeriod",key = "#targetClass + '_' + 'getUserPermissions' + '_' + #id",cacheManager = "caffeineCacheManager",unless = "#result == null")
    public Set<PermissionEnum> getUserPermissions(Integer id) {
        Role optManageRole = roleService.cacheQueryRole(RoleTypeEnum.OPT_MANAGE_PERMISSION,0);
        boolean hasOptManageRole = permissionService.checkUserPermission(id,optManageRole.getId());
        Role sysManageRole = roleService.cacheQueryRole(RoleTypeEnum.FULL_MANAGE_PERMISSION,0);
        boolean hasSysManageRole = permissionService.checkUserPermission(id,sysManageRole.getId());
        Set<PermissionEnum> sets = new HashSet<>();
        if(hasOptManageRole){
            sets.add(PermissionEnum.OperationManageAble);
        }
        if(hasSysManageRole){
            sets.add(PermissionEnum.SystemManageAble);
        }
        return sets;
    }
}
