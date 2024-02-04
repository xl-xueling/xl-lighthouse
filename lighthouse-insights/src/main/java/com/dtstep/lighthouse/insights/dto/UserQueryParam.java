package com.dtstep.lighthouse.insights.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class UserQueryParam implements Serializable {

    private String search;

    private List<Integer> departmentIds;

    private LocalDateTime createStartTime;

    private LocalDateTime createEndTime;

    private List<Integer> states;

    public List<Integer> getStates() {
        return states;
    }

    public void setStates(List<Integer> states) {
        this.states = states;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public List<Integer> getDepartmentIds() {
        return departmentIds;
    }

    public void setDepartmentIds(List<Integer> departmentIds) {
        this.departmentIds = departmentIds;
    }

    public LocalDateTime getCreateStartTime() {
        return createStartTime;
    }

    public void setCreateStartTime(LocalDateTime createStartTime) {
        this.createStartTime = createStartTime;
    }

    public LocalDateTime getCreateEndTime() {
        return createEndTime;
    }

    public void setCreateEndTime(LocalDateTime createEndTime) {
        this.createEndTime = createEndTime;
    }
}
