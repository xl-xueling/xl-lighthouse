package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.insights.dto.StatQueryParam;
import com.dtstep.lighthouse.common.modal.Stat;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatDao {

    int insert(Stat stat);

    Stat queryById(Integer id);

    int deleteById(Integer id);

    List<Stat> queryByProjectId(Integer projectId);

    int count(@Param("queryParam")StatQueryParam queryParam);

    int update(Stat stat);

    List<Stat> queryJoinList(@Param("queryParam") StatQueryParam queryParam);
}
