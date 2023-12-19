package com.dtstep.lighthouse.insights.service.user.impl;

import com.dtstep.lighthouse.commonv2.entity.user.User;
import com.dtstep.lighthouse.insights.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.queryByUserName(username);
        return new AuthUserDetails(user);
    }
}


