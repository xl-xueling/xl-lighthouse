package com.dtstep.lighthouse.insights.enums;

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

    CHANGE_GROUP_STATE_RUNNING(18),

    CHANGE_GROUP_STATE_STOPPED(19),

    CHANGE_GROUP_STATE_LIMITED(20),

    CHANGE_GROUP_STATE_FROZEN(21),

    CHANGE_STAT_STATE_RUNNING(22),

    CHANGE_STAT_STATE_STOPPED(23),

    CHANGE_STAT_STATE_LIMITED(24),

    CHANGE_STAT_STATE_FROZEN(25),

    ;

    RecordTypeEnum(int recordType){
        this.recordType = recordType;
    }

    private Integer recordType;

    public Integer getRecordType() {
        return recordType;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }
}
