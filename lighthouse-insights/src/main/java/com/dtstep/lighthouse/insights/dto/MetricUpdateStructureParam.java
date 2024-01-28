package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.insights.dto_bak.TreeNode;

import java.io.Serializable;
import java.util.List;

public class MetricUpdateStructureParam implements Serializable {

    private Integer id;

    private List<TreeNode> structure;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<TreeNode> getStructure() {
        return structure;
    }

    public void setStructure(List<TreeNode> structure) {
        this.structure = structure;
    }
}
