package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.common.modal.Definitions;
import com.dtstep.lighthouse.insights.dto.DefinitionsQueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DefinitionsDao {

    int insert(Definitions definitions);

    void deleteById(Integer id);

    List<Definitions> queryList(@Param("queryParam") DefinitionsQueryParam queryParam);
}
