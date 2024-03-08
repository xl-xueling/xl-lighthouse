package com.dtstep.lighthouse.insights.vo;

import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import com.dtstep.lighthouse.common.modal.Group;

import java.util.Set;

public class GroupVO extends Group {

    private Set<String> relatedColumns;

    public GroupVO(Group group){
        assert group != null;
        BeanCopyUtil.copy(group,this);
    }

    public Set<String> getRelatedColumns() {
        return relatedColumns;
    }

    public void setRelatedColumns(Set<String> relatedColumns) {
        this.relatedColumns = relatedColumns;
    }
}
