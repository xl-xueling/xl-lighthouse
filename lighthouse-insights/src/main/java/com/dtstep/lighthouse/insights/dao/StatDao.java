package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.insights.dto.StatQueryParam;
import com.dtstep.lighthouse.insights.modal.Stat;

import java.util.List;

public interface StatDao {

    int insert(Stat stat);

    Stat queryById(Integer id);

    int update(Stat stat);

    List<Stat> queryList(StatQueryParam queryParam, Integer pageNum, Integer pageSize);
}
