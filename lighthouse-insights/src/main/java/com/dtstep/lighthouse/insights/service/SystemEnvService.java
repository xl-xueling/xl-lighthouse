package com.dtstep.lighthouse.insights.service;

public interface SystemEnvService {

    void createSignKeyIfNotExist();

    String getParam(String param);
}
