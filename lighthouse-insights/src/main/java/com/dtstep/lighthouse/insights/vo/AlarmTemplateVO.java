package com.dtstep.lighthouse.insights.vo;

import com.dtstep.lighthouse.common.modal.AlarmTemplate;
import com.dtstep.lighthouse.common.modal.Department;
import com.dtstep.lighthouse.common.modal.User;

import java.time.LocalTime;
import java.util.List;

public class AlarmTemplateVO extends AlarmTemplate {

    private List<String> weekdays;

    private LocalTime startTime;

    private LocalTime endTime;

    private List<User> userList;

    private List<Department> departmentList;

    public List<String> getWeekdays() {
        return weekdays;
    }

    public void setWeekdays(List<String> weekdays) {
        this.weekdays = weekdays;
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

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public List<Department> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<Department> departmentList) {
        this.departmentList = departmentList;
    }
}
