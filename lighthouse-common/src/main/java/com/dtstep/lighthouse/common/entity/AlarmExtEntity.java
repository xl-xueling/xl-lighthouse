package com.dtstep.lighthouse.common.entity;

import com.dtstep.lighthouse.common.modal.Alarm;
import com.dtstep.lighthouse.common.util.BeanCopyUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class AlarmExtEntity extends Alarm {

    private AlarmTemplateExtEntity templateExtEntity;

    private LinkedHashMap<String, Pattern> dimensMatchMap;

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

    public LinkedHashMap<String, Pattern> getDimensMatchMap() {
        return dimensMatchMap;
    }

    public void setDimensMatchMap(LinkedHashMap<String, Pattern> dimensMatchMap) {
        this.dimensMatchMap = dimensMatchMap;
    }
}
