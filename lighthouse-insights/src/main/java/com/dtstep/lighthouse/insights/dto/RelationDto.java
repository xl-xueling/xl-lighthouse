package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import com.dtstep.lighthouse.insights.modal.Project;
import com.dtstep.lighthouse.insights.modal.Relation;

public class RelationDto extends Relation {

    private Object extend;

    public RelationDto(Relation relation){
        assert relation != null;
        BeanCopyUtil.copy(relation,this);
    }

    public Object getExtend() {
        return extend;
    }

    public void setExtend(Object extend) {
        this.extend = extend;
    }
}
