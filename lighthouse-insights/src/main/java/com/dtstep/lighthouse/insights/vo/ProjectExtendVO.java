package com.dtstep.lighthouse.insights.vo;

import com.dtstep.lighthouse.insights.dto_bak.TreeNode;
import com.dtstep.lighthouse.insights.modal.Project;

public class ProjectExtendVO extends ProjectVO {

    private TreeNode structure;

    public ProjectExtendVO(Project project) {
        super(project);
    }

    public TreeNode getStructure() {
        return structure;
    }

    public void setStructure(TreeNode structure) {
        this.structure = structure;
    }
}
