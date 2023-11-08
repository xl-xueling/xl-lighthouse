package com.dtstep.lighthouse.insights.dto.project;

import java.util.Date;
import java.util.List;

public class ProjectDTO {

    private int id;

    private String name;

    private int departmentId;

    private int createUserId;

    private List<Integer> adminsId;

    private Date createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(int createUserId) {
        this.createUserId = createUserId;
    }

    public List<Integer> getAdminsId() {
        return adminsId;
    }

    public void setAdminsId(List<Integer> adminsId) {
        this.adminsId = adminsId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
