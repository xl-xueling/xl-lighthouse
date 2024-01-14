package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.insights.modal.Project;

import java.util.List;

public class ProjectExtendDto extends ProjectDto {

    private List<TreeNode> structure;

    public ProjectExtendDto(Project project) {
        super(project);
    }

    public List<TreeNode> getStructure() {
        return structure;
    }

    public void setStructure(List<TreeNode> structure) {
        this.structure = structure;
    }
}
