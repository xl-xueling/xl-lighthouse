package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import com.dtstep.lighthouse.insights.modal.Group;
import com.dtstep.lighthouse.insights.modal.Project;
import com.dtstep.lighthouse.insights.modal.Stat;

public class StatDto extends Stat {

    private Group group;

    private Project project;

    public StatDto(Stat stat){
        assert stat != null;
        BeanCopyUtil.copy(stat,this);
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
