package com.dtstep.lighthouse.insights.dto;

import java.io.Serializable;
import java.util.List;

public class TreeNode implements Serializable {

    private String label;

    public String value;

    private List<TreeNode> children;

    public TreeNode(){}

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

}
