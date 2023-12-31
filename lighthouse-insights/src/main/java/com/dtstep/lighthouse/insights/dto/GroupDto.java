package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import com.dtstep.lighthouse.insights.modal.Group;
import com.dtstep.lighthouse.insights.modal.Project;

public class GroupDto extends Group {

    public GroupDto(Group group){
        assert group != null;
        BeanCopyUtil.copy(group,this);
    }
}
