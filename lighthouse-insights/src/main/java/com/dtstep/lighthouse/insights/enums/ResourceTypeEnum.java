package com.dtstep.lighthouse.insights.enums;

public enum ResourceTypeEnum {

    Department(1),

    Project(2),

    Group(3),

    Stat(4),

    Metric(5),

    ;

    ResourceTypeEnum(Integer resourceType){
        this.resourceType = resourceType;
    }

    private Integer resourceType;

    public Integer getResourceType() {
        return resourceType;
    }

    public void setResourceType(Integer resourceType) {
        this.resourceType = resourceType;
    }
}
