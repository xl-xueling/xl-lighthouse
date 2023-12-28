package com.dtstep.lighthouse.insights.modal;

import com.dtstep.lighthouse.insights.enums.FlowStateEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Flow implements Serializable {

    private Integer id;

    private Integer applyUserId;

    private Integer relationId;

    private FlowStateEnum state;

    private Integer approveUserId;

    private String config;

    private String hash;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer currentNode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(Integer applyUserId) {
        this.applyUserId = applyUserId;
    }

    public Integer getRelationId() {
        return relationId;
    }

    public void setRelationId(Integer relationId) {
        this.relationId = relationId;
    }

    public FlowStateEnum getState() {
        return state;
    }

    public void setState(FlowStateEnum state) {
        this.state = state;
    }

    public Integer getApproveUserId() {
        return approveUserId;
    }

    public void setApproveUserId(Integer approveUserId) {
        this.approveUserId = approveUserId;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
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

    public Integer getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(Integer currentNode) {
        this.currentNode = currentNode;
    }
}
