package com.dtstep.lighthouse.insights.dto;

import java.io.Serializable;
import java.util.List;

public class CommonTreeNode implements Serializable {

    public String id;

    private String name;

    private String pid;

    private String type;

    private List<CommonTreeNode> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<CommonTreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<CommonTreeNode> children) {
        this.children = children;
    }

}
