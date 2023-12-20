package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.commonv2.entity.user.Role;
import com.dtstep.lighthouse.commonv2.enums.AuthRoleTypeEnum;
import com.dtstep.lighthouse.insights.dao.UserDao;
import com.dtstep.lighthouse.insights.modal.User;
import com.dtstep.lighthouse.insights.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public int create(User user) {
        return userDao.insert(user);
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
