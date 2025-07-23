package com.dtstep.lighthouse.common.modal;

import com.dtstep.lighthouse.common.enums.LinkTypeEnum;
import com.dtstep.lighthouse.common.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.common.enums.SwitchStateEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ShortLink implements Serializable {

    private Integer id;

    private String shortCode;

    private String fullUrl;

    private Integer resourceId;

    private ResourceTypeEnum resourceType;

    private LinkTypeEnum linkType;

    private String params;

    private SwitchStateEnum state = SwitchStateEnum.CLOSE;

    private LocalDateTime createTime;

    private LocalDateTime expireTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public ResourceTypeEnum getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceTypeEnum resourceType) {
        this.resourceType = resourceType;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public SwitchStateEnum getState() {
        return state;
    }

    public void setState(SwitchStateEnum state) {
        this.state = state;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

    public LinkTypeEnum getLinkType() {
        return linkType;
    }

    public void setLinkType(LinkTypeEnum linkType) {
        this.linkType = linkType;
    }
}
