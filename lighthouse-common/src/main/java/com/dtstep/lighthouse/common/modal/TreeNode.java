package com.dtstep.lighthouse.common.modal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TreeNode implements Serializable {

    private String key;

    private String label;

    public Object value;

    private String type;

    private List<TreeNode> children;

    private TreeNode(){}

    public TreeNode(String label,Object value){
        this.label = label;
        this.value = value;
        this.key = String.valueOf(value);
    }

    public TreeNode(String label,Object value,String type){
        this.label = label;
        this.value = value;
        this.type = type;
        this.key = type + "_" + value;
    }

    public TreeNode(String key,String label,Object value,String type){
        this.key = key;
        this.type = type;
        this.label = label;
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

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void addChild(TreeNode treeNode){
        if(this.children == null){
            this.children = new ArrayList<>();
        }
        this.children.add(treeNode);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
