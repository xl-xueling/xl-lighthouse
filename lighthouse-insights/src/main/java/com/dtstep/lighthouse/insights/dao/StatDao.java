package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.insights.dto.StatQueryParam;
import com.dtstep.lighthouse.insights.modal.Stat;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatDao {

    int insert(Stat stat);

    Stat queryById(Integer id);

    int update(Stat stat);

    List<Stat> queryList(StatQueryParam queryParam, Integer pageNum, Integer pageSize);
}
