package com.dtstep.lighthouse.insights.modal;

import com.dtstep.lighthouse.insights.enums.OwnerTypeEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Permission implements Serializable {

    private Integer id;

    private Integer ownerId;

    private OwnerTypeEnum ownerType;

    private Integer roleId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public Permission(){}

    public Permission(Integer ownerId,OwnerTypeEnum ownerType,Integer roleId){
        this.ownerId = ownerId;
        this.ownerType = ownerType;
        this.roleId = roleId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public OwnerTypeEnum getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(OwnerTypeEnum ownerType) {
        this.ownerType = ownerType;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
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

}
