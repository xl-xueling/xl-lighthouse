package com.dtstep.lighthouse.insights.vo;

import com.dtstep.lighthouse.common.entity.view.StatValue;

import java.util.List;

public class HomeVO {

    private Integer projectCount;

    private Integer ytdProjectCount;

    private Integer statCount;

    private Integer ytdStatCount;

    private Integer metricCount;

    private Integer ytdMetricCount;

    private Integer userCount;

    private List<StatValue> departmentStatCount;

    public Integer getProjectCount() {
        return projectCount;
    }

    public void setProjectCount(Integer projectCount) {
        this.projectCount = projectCount;
    }

    public Integer getYtdProjectCount() {
        return ytdProjectCount;
    }

    public void setYtdProjectCount(Integer ytdProjectCount) {
        this.ytdProjectCount = ytdProjectCount;
    }

    public Integer getStatCount() {
        return statCount;
    }

    public void setStatCount(Integer statCount) {
        this.statCount = statCount;
    }

    public Integer getYtdStatCount() {
        return ytdStatCount;
    }

    public void setYtdStatCount(Integer ytdStatCount) {
        this.ytdStatCount = ytdStatCount;
    }

    public Integer getMetricCount() {
        return metricCount;
    }

    public void setMetricCount(Integer metricCount) {
        this.metricCount = metricCount;
    }

    public Integer getYtdMetricCount() {
        return ytdMetricCount;
    }

    public void setYtdMetricCount(Integer ytdMetricCount) {
        this.ytdMetricCount = ytdMetricCount;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public List<StatValue> getDepartmentStatCount() {
        return departmentStatCount;
    }

    public void setDepartmentStatCount(List<StatValue> departmentStatCount) {
        this.departmentStatCount = departmentStatCount;
    }
}
