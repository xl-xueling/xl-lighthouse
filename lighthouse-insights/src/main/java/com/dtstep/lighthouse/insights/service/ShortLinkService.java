package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.common.modal.ShortLink;
import com.dtstep.lighthouse.insights.dto.LinkQueryParam;
import com.dtstep.lighthouse.insights.vo.ShortLinkVO;

import java.util.List;

public interface ShortLinkService {

    List<ShortLinkVO> queryList(LinkQueryParam queryParam) throws Exception;

    String createShortLink(ShortLink shortLink) throws Exception;
}
