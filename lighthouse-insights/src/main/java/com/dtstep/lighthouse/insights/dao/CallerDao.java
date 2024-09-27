package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.common.modal.Caller;
import com.dtstep.lighthouse.common.modal.CallerQueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CallerDao {

    int insert(Caller caller);

    int update(Caller caller);

    int deleteById(Integer id);

    Caller queryById(Integer id);

    int count(@Param("queryParam") CallerQueryParam queryParam);

    List<Caller> queryList(@Param("queryParam") CallerQueryParam queryParam);

    String getSecretKey(Integer id);
}
