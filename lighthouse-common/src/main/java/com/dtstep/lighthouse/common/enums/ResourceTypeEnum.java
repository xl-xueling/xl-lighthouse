package com.dtstep.lighthouse.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum ResourceTypeEnum {

    System(1),

    Domain(2),

    Department(3),

    Project(4),

    Group(5),

    Stat(6),

    MetricSet(7),

    View(8),

    Caller(9),

    Alarm(10),

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
