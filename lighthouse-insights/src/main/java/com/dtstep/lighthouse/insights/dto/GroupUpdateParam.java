package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.common.modal.Column;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

public class GroupUpdateParam implements Serializable {

    @NotNull
    private Integer id;

    @NotEmpty
    private String token;

    @NotNull
    private Integer projectId;

    @NotEmpty
    private List<Column> columns;

    @NotEmpty
    private String desc;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
