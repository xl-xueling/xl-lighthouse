package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.common.modal.ShortLink;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortLinkDao {

    int insert(ShortLink shortLink);

    int update(ShortLink shortLink);

    ShortLink queryById(Integer id);
}
