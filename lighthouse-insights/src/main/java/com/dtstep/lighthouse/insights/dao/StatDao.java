package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.insights.modal.Stat;

public interface StatDao {

    int insert(Stat stat);

    Stat queryById(Integer id);

    int update(Stat stat);
}
