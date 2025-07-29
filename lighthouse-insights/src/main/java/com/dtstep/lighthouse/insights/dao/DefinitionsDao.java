package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.common.modal.Definition;
import com.dtstep.lighthouse.insights.dto.DefinitionsQueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DefinitionsDao {

    int insert(Definition definition);

    int update(Definition definition);

    void deleteById(Integer id);

    List<Definition> queryList(@Param("queryParam") DefinitionsQueryParam queryParam);

    int count(@Param("queryParam") DefinitionsQueryParam queryParam);
}
