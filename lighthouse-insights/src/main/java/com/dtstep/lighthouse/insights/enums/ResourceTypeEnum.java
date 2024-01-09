package com.dtstep.lighthouse.insights.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum ResourceTypeEnum {

    System(1),

    Department(2),

    Project(3),

    Group(4),

    Stat(5),

    Metric(6),

    Order(7),

    ;

    ResourceTypeEnum(Integer resourceType){
        this.resourceType = resourceType;
    }

    @JsonValue
    private Integer resourceType;

    public Integer getResourceType() {
        return resourceType;
    }

    public void setResourceType(Integer resourceType) {
        this.resourceType = resourceType;
    }

    @JsonCreator
    public static ResourceTypeEnum forValue(int value){
        ResourceTypeEnum[] values = ResourceTypeEnum.values();
        return Stream.of(values).filter(it -> it.getResourceType() == value).findAny().orElse(null);
    }
}
