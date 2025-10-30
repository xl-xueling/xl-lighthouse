package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.common.enums.DefinitionsEnum;

import java.io.Serializable;

public class ViewDefinitionCreateParam implements Serializable {

    private String name;

    private DefinitionsEnum definitionsEnum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
