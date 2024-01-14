package com.dtstep.lighthouse.insights.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TreeNode implements Serializable {

    private String label;

    public String value;

    private int type;

    private List<TreeNode> children;

    public TreeNode(){}

    public TreeNode(String label,String value){
        this.label = label;
        this.value = value;
    }

    public TreeNode(String label,String value,int type){
        this.label = label;
        this.value = value;
        this.type = type;
    }

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void addChild(TreeNode treeNode) throws Exception {
        if(this.children == null){
            this.children = new ArrayList<>();
        }
        List<String> childKeys = children.stream().map(z -> z.value).collect(Collectors.toList());
        if(childKeys.contains(treeNode.getValue())){
            throw new Exception("Duplicate node error!");
        }
        this.children.add(treeNode);
    }
}
