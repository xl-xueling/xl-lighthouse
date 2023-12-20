package com.dtstep.lighthouse.insights.service;

import org.springframework.stereotype.Service;

@Service
public interface SystemEnvService {

    void createSignKeyIfNotExist();
}
