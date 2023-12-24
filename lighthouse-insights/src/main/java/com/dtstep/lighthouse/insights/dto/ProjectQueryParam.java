package com.dtstep.lighthouse.insights.dto;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ProjectQueryParam {

    private Integer id;

    private String title;

    private List<Integer> departmentIds;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        if(createEndTime != null){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(createEndTime);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            this.createEndTime = calendar.getTime();
        }
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

    public List<Integer> getDepartmentIds() {
        return departmentIds;
    }

    public void setDepartmentIds(List<Integer> departmentIds) {
        this.departmentIds = departmentIds;
    }
}
