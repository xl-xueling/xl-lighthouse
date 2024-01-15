package com.dtstep.lighthouse.insights.modal;

import com.dtstep.lighthouse.insights.enums.RelationTypeEnum;
import com.dtstep.lighthouse.insights.enums.ResourceTypeEnum;

import java.time.LocalDateTime;

public class Relation {

    private Integer id;

    private Integer relationId;

    private RelationTypeEnum relationType;

    private Integer resourceId;

    private ResourceTypeEnum resourceTypeEnum;

    private LocalDateTime createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRelationId() {
        return relationId;
    }

    public void setRelationId(Integer relationId) {
        this.relationId = relationId;
    }

    public RelationTypeEnum getRelationType() {
        return relationType;
    }

    public void setRelationType(RelationTypeEnum relationType) {
        this.relationType = relationType;
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public ResourceTypeEnum getResourceTypeEnum() {
        return resourceTypeEnum;
    }

    public void setResourceTypeEnum(ResourceTypeEnum resourceTypeEnum) {
        this.resourceTypeEnum = resourceTypeEnum;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
