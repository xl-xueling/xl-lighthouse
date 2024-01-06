package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.insights.modal.Project;

import java.util.List;

public class ProjectExtendDto extends ProjectDto {

    private List<CommonTreeNode> structure;

    public ProjectExtendDto(Project project) {
        super(project);
    }

    public List<CommonTreeNode> getStructure() {
        return structure;
    }

    public void setStructure(List<CommonTreeNode> structure) {
        this.structure = structure;
    }
}
