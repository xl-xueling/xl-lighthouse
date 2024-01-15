package com.dtstep.lighthouse.insights.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum RelationTypeEnum {

    MetricSetBindRelation(1),

    UserPickUpMetricSetRelation(2),

    StatPrecedingIndicator(3),

    StatParallelIndicator(4),

    StatSubsequentIndicator(5),


    ;

    RelationTypeEnum(int relationType){
        this.relationType = relationType;
    }

    @JsonValue
    private Integer relationType;

    public Integer getRelationType() {
        return relationType;
    }

    public void setRelationType(Integer relationType) {
        this.relationType = relationType;
    }

    @JsonCreator
    public static PrivateTypeEnum forValue(int privateType){
        PrivateTypeEnum[] values = PrivateTypeEnum.values();
        return Stream.of(values).filter(it -> it.getPrivateType() == privateType).findAny().orElse(null);
    }
}
