package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.common.modal.MetaTable;
import com.dtstep.lighthouse.insights.dto.MetaTableQueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MetaTableDao {

    int insert(MetaTable metaTable);

    MetaTable queryById(Integer id);

    MetaTable getCurrentStorageTable(@Param("queryParam")MetaTableQueryParam queryParam);
}
