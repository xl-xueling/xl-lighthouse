package com.dtstep.lighthouse.insights.vo;

import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import com.dtstep.lighthouse.common.modal.Group;

import java.util.Set;

public class GroupVO extends GroupExtEntity {

    private Set<String> relatedColumns;

    public GroupVO(GroupExtEntity groupExtEntity){
        assert groupExtEntity != null;
        BeanCopyUtil.copy(groupExtEntity,this);
    }

    public Set<String> getRelatedColumns() {
        return relatedColumns;
    }

    public void setRelatedColumns(Set<String> relatedColumns) {
        this.relatedColumns = relatedColumns;
    }
}
