package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.common.modal.Creations;
import com.dtstep.lighthouse.insights.dto.CreationsQueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreationsDao {

    int insert(Creations creations);

    int update(Creations creations);

    List<Creations> queryList(@Param("queryParam") CreationsQueryParam queryParam);

    int count(@Param("queryParam") CreationsQueryParam queryParam);

    void deleteById(Integer id);
}
