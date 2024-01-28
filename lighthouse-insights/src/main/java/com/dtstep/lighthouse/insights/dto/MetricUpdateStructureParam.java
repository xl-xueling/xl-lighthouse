package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.insights.dto_bak.TreeNode;

import java.io.Serializable;
import java.util.List;

public class MetricUpdateStructureParam implements Serializable {

    private Integer id;

    private TreeNode structure;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TreeNode getStructure() {
        return structure;
    }

    public void setStructure(TreeNode structure) {
        this.structure = structure;
    }
}
