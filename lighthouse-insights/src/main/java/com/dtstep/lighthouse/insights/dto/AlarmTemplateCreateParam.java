package com.dtstep.lighthouse.insights.dto;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;

public class AlarmTemplateCreateParam implements Serializable {

    private List<String> days;

    private LocalTime startTime;

    private LocalTime endTime;

    private List<Integer> userIds;

    private List<Integer> departmentIds;

    public List<String> getDays() {
        return days;
    }

    public void setDays(List<String> days) {
        this.days = days;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public List<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }

    public List<Integer> getDepartmentIds() {
        return departmentIds;
    }

    public void setDepartmentIds(List<Integer> departmentIds) {
        this.departmentIds = departmentIds;
    }
}
