package com.dtstep.lighthouse.insights.dto_bak;

import com.dtstep.lighthouse.insights.modal.Project;

public class ProjectExtendDto extends ProjectDto {

    private TreeNode structure;

    public ProjectExtendDto(Project project) {
        super(project);
    }

    public TreeNode getStructure() {
        return structure;
    }

    public void setStructure(TreeNode structure) {
        this.structure = structure;
    }
}
