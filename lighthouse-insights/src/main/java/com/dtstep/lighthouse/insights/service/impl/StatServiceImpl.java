package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.insights.dao.StatDao;
import com.dtstep.lighthouse.insights.modal.Stat;
import com.dtstep.lighthouse.insights.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatServiceImpl implements StatService {

    @Autowired
    private StatDao statDao;

    @Override
    public int create(Stat stat) {
        return statDao.insert(stat);
    }
}
