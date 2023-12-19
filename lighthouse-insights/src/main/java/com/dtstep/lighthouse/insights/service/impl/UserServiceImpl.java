package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.commonv2.entity.user.Role;
import com.dtstep.lighthouse.commonv2.enums.AuthRoleTypeEnum;
import com.dtstep.lighthouse.insights.dao.UserMapper;
import com.dtstep.lighthouse.insights.modal.User;
import com.dtstep.lighthouse.insights.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Override
    public int create(User user) {
        return userMapper.insert(user);
    }

    @Override
    public User queryById(int id) {
        return null;
    }

    @Override
    public User queryByUserName(String userName) {
        if (!"admin".equals(userName)) {
            throw new RuntimeException();
        }
        List<Role> roles = List.of( new Role(AuthRoleTypeEnum.USER));
        User user = new User();
        user.setId(1);
        user.setUsername(userName);
        user.setPassword(passwordEncoder.encode("123456"));
        user.setRoles(roles);
        return user;
    }

}
