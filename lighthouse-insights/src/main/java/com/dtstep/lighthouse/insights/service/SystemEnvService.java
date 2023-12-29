package com.dtstep.lighthouse.insights.service;

public interface SystemEnvService {

    void generateSignKeyIfNotExist();

    String getParam(String param);
}
