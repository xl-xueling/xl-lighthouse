package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.modal.ShortLink;
import com.dtstep.lighthouse.insights.dao.ShortLinkDao;
import com.dtstep.lighthouse.insights.dto.LinkQueryParam;
import com.dtstep.lighthouse.insights.service.CallerService;
import com.dtstep.lighthouse.insights.service.ShortLinkService;
import com.dtstep.lighthouse.insights.vo.CallerVO;
import com.dtstep.lighthouse.insights.vo.ShortLinkVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShortLinkServiceImpl implements ShortLinkService {

    @Autowired
    private ShortLinkDao shortLinkDao;

    @Autowired
    private CallerService callerService;

    @Override
    public List<ShortLinkVO> queryList(LinkQueryParam queryParam) throws Exception {
        List<ShortLink> shortLinkList = shortLinkDao.queryList(queryParam);
        List<ShortLinkVO> voList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(shortLinkList)){
            for(ShortLink shortLink : shortLinkList){
                ShortLinkVO shortLinkVO = translate(shortLink);
                voList.add(shortLinkVO);
            }
        }
        return voList;
    }

    private ShortLinkVO translate(ShortLink shortLink) throws Exception {
        ShortLinkVO shortLinkVO = new ShortLinkVO(shortLink);
        Integer callerId = shortLinkVO.getCallerId();
        if(callerId != null){
            CallerVO callerVO = callerService.queryById(callerId);
            shortLinkVO.setCallerVO(callerVO);
        }
        return shortLinkVO;
    }
}
