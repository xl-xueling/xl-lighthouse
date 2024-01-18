package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.insights.modal.Domain;
import org.apache.xmlbeans.impl.store.DomImpl;

public interface DomainService {

    int create(Domain domain);

    Domain queryById(Integer id);

    Domain queryDefault();
}
