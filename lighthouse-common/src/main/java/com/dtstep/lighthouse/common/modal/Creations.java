package com.dtstep.lighthouse.common.modal;

import com.dtstep.lighthouse.common.enums.CreationsTypeEnum;
import com.dtstep.lighthouse.common.enums.PrivateTypeEnum;

import java.time.LocalDateTime;

public class Creations {

    private Integer id;

    private CreationsTypeEnum type;

    private Integer userId;

    private String name;

    private Integer cateId;

    private Object config;

    private String desc;

    private PrivateTypeEnum privateType;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CreationsTypeEnum getType() {
        return type;
    }

    public void setType(CreationsTypeEnum type) {
        this.type = type;
    }

    public Integer getCateId() {
        return cateId;
    }

    public void setCateId(Integer cateId) {
        this.cateId = cateId;
    }

    public Object getConfig() {
        return config;
    }

    public void setConfig(Object config) {
        this.config = config;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public PrivateTypeEnum getPrivateType() {
        return privateType;
    }

    public void setPrivateType(PrivateTypeEnum privateType) {
        this.privateType = privateType;
    }
}
