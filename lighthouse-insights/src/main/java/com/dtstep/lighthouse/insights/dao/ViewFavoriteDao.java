package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.common.modal.ViewFavorite;
import com.dtstep.lighthouse.insights.dto.ViewFavoriteQueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViewFavoriteDao {

    int insert(ViewFavorite viewFavorite);

    List<ViewFavorite> queryList(@Param("queryParam") ViewFavoriteQueryParam queryParam);

    void deleteById(Integer id);

    void deleteByCategoryId(Integer categoryId);

}
