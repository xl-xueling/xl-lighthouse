package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.common.modal.Domain;

public interface DomainService {

    int create(Domain domain);

    Domain queryById(Integer id);

    Domain queryDefault();
}
