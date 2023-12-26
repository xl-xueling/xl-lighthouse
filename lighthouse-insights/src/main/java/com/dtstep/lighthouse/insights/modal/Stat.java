package com.dtstep.lighthouse.insights.modal;

import com.dtstep.lighthouse.common.enums.stat.StatStateEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Stat implements Serializable {

    private Integer id;

    private String title;

    private String template;

    private String timeparam;

    private Long expired;

    private StatStateEnum state;

    private Integer projectId;

    private Integer groupId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String randomId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getTimeparam() {
        return timeparam;
    }

    public void setTimeparam(String timeparam) {
        this.timeparam = timeparam;
    }

    public Long getExpired() {
        return expired;
    }

    public void setExpired(Long expired) {
        this.expired = expired;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public StatStateEnum getState() {
        return state;
    }

    public void setState(StatStateEnum state) {
        this.state = state;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getRandomId() {
        return randomId;
    }

    public void setRandomId(String randomId) {
        this.randomId = randomId;
    }
}
