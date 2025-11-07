package com.dtstep.lighthouse.common.modal;

import com.dtstep.lighthouse.common.enums.PrivateTypeEnum;
import com.dtstep.lighthouse.common.enums.SwitchStateEnum;
import com.dtstep.lighthouse.common.enums.ViewStateEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

public class View implements Serializable {

    private Integer id;

    private String title;

    private Integer userId;

    private Integer callerId;

    private PrivateTypeEnum privateType;

    private ViewStateEnum state;

    private String desc;

    private Integer version;

    private SwitchStateEnum shareLinkEnabled;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

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

    public PrivateTypeEnum getPrivateType() {
        return privateType;
    }

    public void setPrivateType(PrivateTypeEnum privateType) {
        this.privateType = privateType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public ViewStateEnum getState() {
        return state;
    }

    public void setState(ViewStateEnum state) {
        this.state = state;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getCallerId() {
        return callerId;
    }

    public void setCallerId(Integer callerId) {
        this.callerId = callerId;
    }

    public SwitchStateEnum getShareLinkEnabled() {
        return shareLinkEnabled;
    }

    public void setShareLinkEnabled(SwitchStateEnum shareLinkEnabled) {
        this.shareLinkEnabled = shareLinkEnabled;
    }
}
