package com.dtstep.lighthouse.insights.service;

public interface InitService {

    void initRole();

    void initDepartment();

    void initAdmin() throws Exception;

    void initDefaultDomain() throws Exception;
}
