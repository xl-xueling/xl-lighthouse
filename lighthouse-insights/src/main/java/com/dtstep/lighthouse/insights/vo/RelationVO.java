package com.dtstep.lighthouse.insights.vo;

import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import com.dtstep.lighthouse.insights.modal.Relation;

public class RelationVO extends Relation {

    private Object extend;

    public RelationVO(Relation relation){
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
