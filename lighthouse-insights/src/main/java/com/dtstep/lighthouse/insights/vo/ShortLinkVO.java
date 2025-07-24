package com.dtstep.lighthouse.insights.vo;

import com.dtstep.lighthouse.common.modal.ShortLink;

public class ShortLinkVO extends ShortLink {

    private CallerVO callerVO;

    public CallerVO getCallerVO() {
        return callerVO;
    }

    public void setCallerVO(CallerVO callerVO) {
        this.callerVO = callerVO;
    }
}
