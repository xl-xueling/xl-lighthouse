package com.dtstep.lighthouse.insights.config;
/*
 * Copyright (C) 2022-2024 XueLing.雪灵
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class DefaultAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(authentication.getClass() == SeedAuthenticationToken.class){
            return new SeedAuthenticationToken(((SeedAuthenticationToken) authentication).getUserId(),
                    ((SeedAuthenticationToken) authentication).getSeed());
        }else{
            String username = String.valueOf(authentication.getPrincipal());
            String password = String.valueOf(authentication.getCredentials());
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if(userDetails != null && passwordEncoder.matches(password,userDetails.getPassword())){
                return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
            }
            throw new BadCredentialsException("Authentication Error!");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication) || SeedAuthenticationToken.class.equals(authentication);
    }
}
