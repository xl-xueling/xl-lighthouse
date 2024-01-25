package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.insights.enums.RelationTypeEnum;

import java.io.Serializable;

public class RelationQueryParam implements Serializable {

    private Integer relationId;

    private RelationTypeEnum relationType;

    private String search;

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

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
