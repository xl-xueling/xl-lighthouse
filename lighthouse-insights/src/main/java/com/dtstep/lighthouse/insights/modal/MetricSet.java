package com.dtstep.lighthouse.insights.modal;

import com.dtstep.lighthouse.insights.dto_bak.TreeNode;
import com.dtstep.lighthouse.insights.enums.PrivateTypeEnum;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class MetricSet implements Serializable {

    private Integer id;

    private String title;

    private PrivateTypeEnum privateType;

    private String desc;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private List<TreeNode> structure;

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

    public List<TreeNode> getStructure() {
        return structure;
    }

    public void setStructure(List<TreeNode> structure) {
        this.structure = structure;
    }
}
