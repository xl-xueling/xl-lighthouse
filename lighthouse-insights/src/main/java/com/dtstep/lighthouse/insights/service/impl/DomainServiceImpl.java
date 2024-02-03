package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.insights.dao.DomainDao;
import com.dtstep.lighthouse.common.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.common.modal.Domain;
import com.dtstep.lighthouse.common.modal.ResourceDto;
import com.dtstep.lighthouse.insights.service.DomainService;
import com.dtstep.lighthouse.insights.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DomainServiceImpl implements DomainService {

    @Autowired
    private DomainDao domainDao;

    @Autowired
    private ResourceService resourceService;

    @Override
    public int create(Domain domain) {
        domainDao.insert(domain);
        int id = domain.getId();
        resourceService.addResourceCallback(ResourceDto.newResource(ResourceTypeEnum.Domain,id,ResourceTypeEnum.System,0));
        return id;
    }

    @Override
    public Domain queryById(Integer id) {
        return domainDao.queryById(id);
    }

    @Override
    public Domain queryDefault() {
        return domainDao.queryDefault();
    }
}
