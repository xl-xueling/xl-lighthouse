package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.common.modal.Asset;
import com.dtstep.lighthouse.common.modal.AssetQueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetDao {

    int insert(Asset asset);

    int deleteById(Integer id);

    Asset queryById(Integer id);

    List<Asset> queryList(@Param("queryParam") AssetQueryParam queryParam);
}
