package com.dtstep.lighthouse.insights.enums;

public enum ResourceTypeEnum {

    System(1),

    Department(2),

    Project(3),

    Group(4),

    Stat(5),

    Metric(6),

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
