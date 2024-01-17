package com.dtstep.lighthouse.insights.modal;

import com.dtstep.lighthouse.common.enums.RoleTypeEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Role implements Serializable {

    private Integer id;

    private RoleTypeEnum roleType;

    private Integer resourceId;

    private Integer pid;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String desc;

    public Role(RoleTypeEnum roleTypeEnum,Integer resourceId){
        this.roleType = roleTypeEnum;
        this.resourceId = resourceId;
        this.desc = roleTypeEnum.name();
    }

    public Role(RoleTypeEnum roleTypeEnum,Integer resourceId,Integer pid){
        this.roleType = roleTypeEnum;
        this.resourceId = resourceId;
        this.pid = pid;
        this.desc = roleTypeEnum.name();
    }

    public Role(RoleTypeEnum roleTypeEnum,Integer resourceId,Integer pid,String desc){
        this.roleType = roleTypeEnum;
        this.resourceId = resourceId;
        this.pid = pid;
        this.desc = desc;
    }

    public Role(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RoleTypeEnum getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleTypeEnum roleType) {
        this.roleType = roleType;
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
