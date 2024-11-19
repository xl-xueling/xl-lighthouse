package com.dtstep.lighthouse.insights.vo;

import com.dtstep.lighthouse.common.modal.Alarm;
import com.dtstep.lighthouse.common.util.BeanCopyUtil;

public class AlarmVO extends Alarm {

    private Object extend;

    public AlarmVO(){}

    public AlarmVO(Alarm alarm){
        assert alarm != null;
        BeanCopyUtil.copy(alarm,this);
    }

    public Object getExtend() {
        return extend;
    }

    public void setExtend(Object extend) {
        this.extend = extend;
    }
}
