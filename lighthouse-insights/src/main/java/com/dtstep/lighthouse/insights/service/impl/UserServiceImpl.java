package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.enums.user.UserStateEnum;
import com.dtstep.lighthouse.commonv2.entity.user.Role;
import com.dtstep.lighthouse.commonv2.enums.AuthRoleTypeEnum;
import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dao.DepartmentDao;
import com.dtstep.lighthouse.insights.dao.UserDao;
import com.dtstep.lighthouse.insights.dto.ChangePasswordParam;
import com.dtstep.lighthouse.insights.dto.UserQueryParam;
import com.dtstep.lighthouse.insights.dto.UserUpdateParam;
import com.dtstep.lighthouse.insights.modal.User;
import com.dtstep.lighthouse.insights.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Override
    public int create(User user) {
        boolean isExist = userDao.isUserNameExist(user.getUsername());
        if(isExist){
            return -1;
        }else{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            LocalDateTime localDateTime = LocalDateTime.now();
            user.setCreateTime(localDateTime);
            user.setUpdateTime(localDateTime);
            user.setLastTime(localDateTime);
            userDao.insert(user);
            return user.getId();
        }
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
