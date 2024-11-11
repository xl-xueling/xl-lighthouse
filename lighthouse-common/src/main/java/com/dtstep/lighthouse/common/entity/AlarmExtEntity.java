package com.dtstep.lighthouse.common.entity;

import com.dtstep.lighthouse.common.modal.Alarm;
import com.dtstep.lighthouse.common.util.BeanCopyUtil;

public class AlarmExtEntity extends Alarm {

    private AlarmTemplateExtEntity templateExtEntity;

    public AlarmExtEntity(){}

    public AlarmExtEntity(Alarm alarm){
        assert alarm != null;
        BeanCopyUtil.copy(alarm,this);
    }

    public AlarmTemplateExtEntity getTemplateExtEntity() {
        return templateExtEntity;
    }

    public void setTemplateExtEntity(AlarmTemplateExtEntity templateExtEntity) {
        this.templateExtEntity = templateExtEntity;
    }
}
