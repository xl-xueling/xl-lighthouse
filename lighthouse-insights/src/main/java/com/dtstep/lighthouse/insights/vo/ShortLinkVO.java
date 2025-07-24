package com.dtstep.lighthouse.insights.vo;

import com.dtstep.lighthouse.common.modal.ShortLink;
import com.dtstep.lighthouse.common.util.BeanCopyUtil;

public class ShortLinkVO extends ShortLink {

    private CallerVO callerVO;

    public ShortLinkVO(ShortLink shortLink){
        assert shortLink != null;
        BeanCopyUtil.copy(shortLink,this);
    }

    public CallerVO getCallerVO() {
        return callerVO;
    }

    public void setCallerVO(CallerVO callerVO) {
        this.callerVO = callerVO;
    }
}
