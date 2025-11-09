package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.enums.LinkTypeEnum;
import com.dtstep.lighthouse.common.modal.ShortLink;
import com.dtstep.lighthouse.common.random.RandomID;
import com.dtstep.lighthouse.insights.dao.ShortLinkDao;
import com.dtstep.lighthouse.insights.dto.LinkQueryParam;
import com.dtstep.lighthouse.insights.service.ShortLinkService;
import com.dtstep.lighthouse.insights.vo.ShortLinkVO;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShortLinkServiceImpl implements ShortLinkService {

    private static final Logger logger = LoggerFactory.getLogger(ShortLinkServiceImpl.class);

    @Autowired
    private ShortLinkDao shortLinkDao;

    @Override
    public String createShortLink(ShortLink shortLink) throws Exception {
        String shortCode;
        int attempts = 0;
        final int MAX_ATTEMPTS = 30;
        while (true) {
            shortCode = RandomID.id(6);
            boolean isExist = shortLinkDao.isExist(shortCode);
            if (!isExist) {
                shortLink.setShortCode(shortCode);
                break;
            }
            attempts++;
            if (attempts >= MAX_ATTEMPTS) {
                throw new Exception();
            }
        }
        shortLinkDao.insert(shortLink);
        return shortCode;
    }

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

    @Override
    public ShortLinkVO queryByCode(String shortCode) throws Exception {
        ShortLink shortLink = shortLinkDao.queryByCode(shortCode);
        return translate(shortLink);
    }

    private ShortLinkVO translate(ShortLink shortLink) throws Exception {
        if(shortLink == null){
            return null;
        }
        ShortLinkVO shortLinkVO = new ShortLinkVO(shortLink);
        if(shortLink.getLinkType() == LinkTypeEnum.VIEW_SHARE_LINK){
            shortLinkVO.setLink(SysConst.SHORT_LINK_PREFIX_VIEW_PUBLIC + shortLink.getShortCode());
        }
        return shortLinkVO;
    }

    @Override
    public void updateShortLink(ShortLink shortLink) throws Exception {
        shortLinkDao.update(shortLink);
    }
}
