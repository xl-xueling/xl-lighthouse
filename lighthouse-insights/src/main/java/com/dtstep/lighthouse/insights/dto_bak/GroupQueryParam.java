package com.dtstep.lighthouse.insights.dto_bak;

import java.io.Serializable;

public class GroupQueryParam implements Serializable {

    private Integer id;

    private Integer projectId;

    private String token;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
