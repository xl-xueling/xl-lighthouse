package com.dtstep.lighthouse.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum RelationTypeEnum {

    MetricSetBindRelation(1),

    UserStarMetricSetRelation(2),

    UserStarProjectRelation(3),

    StatPrecedingIndicator(4),

    StatParallelIndicator(5),

    StatSubsequentIndicator(6),

    UserStarViewRelation(7),

    StatBindAlarmRelation(8),

    UserCacheStatFiltersRelation(9),

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
    public static RelationTypeEnum forValue(int relationType){
        RelationTypeEnum[] values = RelationTypeEnum.values();
        return Stream.of(values).filter(it -> it.getRelationType() == relationType).findAny().orElse(null);
    }
}
