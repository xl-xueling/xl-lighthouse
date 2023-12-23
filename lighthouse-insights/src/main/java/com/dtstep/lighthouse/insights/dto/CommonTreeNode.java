package com.dtstep.lighthouse.insights.dto;

import java.io.Serializable;
import java.util.List;

public class CommonTreeNode implements Serializable {

    public String key;

    private String title;

    private List<CommonTreeNode> children;

    public CommonTreeNode(String key,String title){
        this.key = key;
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<CommonTreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<CommonTreeNode> children) {
        this.children = children;
    }

}
