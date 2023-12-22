package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.enums.user.UserStateEnum;
import com.dtstep.lighthouse.commonv2.entity.user.Role;
import com.dtstep.lighthouse.commonv2.enums.AuthRoleTypeEnum;
import com.dtstep.lighthouse.insights.dao.DepartmentDao;
import com.dtstep.lighthouse.insights.dao.UserDao;
import com.dtstep.lighthouse.insights.dto.UserUpdateParam;
import com.dtstep.lighthouse.insights.modal.User;
import com.dtstep.lighthouse.insights.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
            Date date = new Date();
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setState(UserStateEnum.USER_PEND);
            user.setUpdateTime(date);
            user.setLastTime(date);
            user.setCreateTime(date);
            return userDao.insert(user);
        }
    }

    @Override
    public int update(UserUpdateParam user) {
        return userDao.update(user);
    }

    @Override
    public User queryById(int id) {
        return userDao.queryById(id);
    }

    @Override
    public User queryByUserName(String userName) {
        return userDao.queryByUserName(userName);
    }

}
