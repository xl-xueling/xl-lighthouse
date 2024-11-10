package com.dtstep.lighthouse.insights.vo;

import com.dtstep.lighthouse.common.modal.Notification;
import com.dtstep.lighthouse.common.util.BeanCopyUtil;

public class NotificationVO extends Notification {

    private Object extend;

    public NotificationVO(Notification notification){
        assert notification != null;
        BeanCopyUtil.copy(notification,this);
    }

    public Object getExtend() {
        return extend;
    }

    public void setExtend(Object extend) {
        this.extend = extend;
    }
}
