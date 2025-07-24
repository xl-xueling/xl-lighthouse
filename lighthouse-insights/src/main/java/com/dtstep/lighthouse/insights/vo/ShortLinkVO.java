package com.dtstep.lighthouse.insights.vo;

import com.dtstep.lighthouse.common.modal.ShortLink;
import com.dtstep.lighthouse.common.util.BeanCopyUtil;

public class ShortLinkVO extends ShortLink {

    private CallerVO callerVO;

    private String link;

    public ShortLinkVO(ShortLink shortLink){
        assert shortLink != null;
        BeanCopyUtil.copy(shortLink,this);
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public CallerVO getCallerVO() {
        return callerVO;
    }

    public void setCallerVO(CallerVO callerVO) {
        this.callerVO = callerVO;
    }
}
