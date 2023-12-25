package com.dtstep.lighthouse.insights.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum LimitedStrategyEnum {

    GROUP_MESSAGE_LIMITED_STRATEGY("group_message_limited_strategy"),

    STAT_RESULT_LIMITED_STRATEGY("stat_result_limited_strategy"),

    ;

    LimitedStrategyEnum(String name){
        this.name = name;
    }

    @JsonValue
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
