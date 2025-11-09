package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.common.modal.ShortLink;
import com.dtstep.lighthouse.insights.dto.LinkQueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShortLinkDao {

    int insert(ShortLink shortLink);

    int update(ShortLink shortLink);

    ShortLink queryById(Integer id);

    ShortLink queryByCode(String shortCode);

    List<ShortLink> queryList(@Param("queryParam") LinkQueryParam queryParam);

    boolean isExist(String shortCode);
}
