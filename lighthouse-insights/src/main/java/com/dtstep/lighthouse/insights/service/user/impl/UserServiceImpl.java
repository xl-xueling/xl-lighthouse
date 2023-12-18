package com.dtstep.lighthouse.insights.service.user.impl;


import com.dtstep.lighthouse.insights.security.Role;
import com.dtstep.lighthouse.insights.security.RoleTypeEnum;
import com.dtstep.lighthouse.insights.security.User;
import com.dtstep.lighthouse.insights.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public User getUserByName(String userName) {
        if (!"admin".equals(userName)) {
            throw new RuntimeException();
        }
        List<Role> roles = List.of( new Role(RoleTypeEnum.USER));
        return new User(userName, passwordEncoder.encode("123456"), roles);
    }
}
