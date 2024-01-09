package com.dtstep.lighthouse.insights.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum RecordTypeEnum {

    CREATE_DEPARTMENT(1),

    UPDATE_DEPARTMENT(2),

    DELETE_DEPARTMENT(3),

    CREATE_PROJECT(4),

    UPDATE_PROJECT(5),

    DELETE_PROJECT(6),

    CRATE_GROUP(7),

    UPDATE_GROUP(8),

    DELETE_GROUP(9),

    CREATE_STAT(10),

    UPDATE_STAT(11),

    DELETE_STAT(12),

    CREATE_METRIC(13),

    UPDATE_METRIC(14),

    DELETE_METRIC(15),

    GRANT_PERMISSION(16),

    RELEASE_PERMISSION(17),

    CHANGE_GROUP_STATE(18),

    CHANGE_STAT_STATE(19),

    ;

    RecordTypeEnum(int recordType){
        this.recordType = recordType;
    }

    @JsonValue
    private Integer recordType;

    public Integer getRecordType() {
        return recordType;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }

    @JsonCreator
    public static RecordTypeEnum forValue(int value){
        RecordTypeEnum[] values = RecordTypeEnum.values();
        return Stream.of(values).filter(it -> it.getRecordType() == value).findAny().orElse(null);
    }
}
