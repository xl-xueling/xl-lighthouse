package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.common.modal.Definitions;
import com.dtstep.lighthouse.insights.dto.ComponentQueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DefinitionsDao {

    int insert(Definitions definitions);

    Definitions queryById(Integer id);

    List<Definitions> queryList(@Param("queryParam") ComponentQueryParam queryParam);
}
