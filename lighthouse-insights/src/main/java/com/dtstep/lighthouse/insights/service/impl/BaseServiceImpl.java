package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.insights.modal.User;
import com.dtstep.lighthouse.insights.service.BaseService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class BaseServiceImpl implements BaseService {

    @Override
    public Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Integer) authentication.getPrincipal();
    }
}
