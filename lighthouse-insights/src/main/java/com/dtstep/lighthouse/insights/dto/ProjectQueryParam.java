package com.dtstep.lighthouse.insights.dto;

import java.util.Date;

public class ProjectQueryParam {

    private Integer id;

    private String search;

    private Integer departmentId;

    private Date createStartTime;

    private Date createEndTime;

    private Integer privateType;

    private Integer owner;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Date getCreateStartTime() {
        return createStartTime;
    }

    public void setCreateStartTime(Date createStartTime) {
        this.createStartTime = createStartTime;
    }

    public Date getCreateEndTime() {
        return createEndTime;
    }

    public void setCreateEndTime(Date createEndTime) {
        this.createEndTime = createEndTime;
    }

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
    }

    public Integer getPrivateType() {
        return privateType;
    }

    public void setPrivateType(Integer privateType) {
        this.privateType = privateType;
    }
}
