package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.insights.modal.User;
import com.dtstep.lighthouse.insights.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public AuthUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.queryAllInfoByUserName(username);
        if(user == null){
            return null;
        }else{
            return new AuthUserDetails(user);
        }
    }
}


