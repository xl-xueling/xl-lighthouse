package com.dtstep.lighthouse.insights.vo;

import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import com.dtstep.lighthouse.insights.modal.Group;

public class GroupVO extends Group {

    public GroupVO(Group group){
        assert group != null;
        BeanCopyUtil.copy(group,this);
    }
}
