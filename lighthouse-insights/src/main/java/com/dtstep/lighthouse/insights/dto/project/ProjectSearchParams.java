package com.dtstep.lighthouse.insights.dto.project;

public class ProjectSearchParams {

    private Integer ownerId;

    private Integer departmentId;

    private String search;

    public Integer getDepartmentId() {
        return departmentId;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }
}
