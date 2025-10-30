package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.common.enums.DefinitionsEnum;

import java.io.Serializable;

public class ViewDefinitionUpdateParam implements Serializable {

    private Integer id;

    private String name;

    private DefinitionsEnum definitionsEnum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DefinitionsEnum getDefinitionsEnum() {
        return definitionsEnum;
    }

    public void setDefinitionsEnum(DefinitionsEnum definitionsEnum) {
        this.definitionsEnum = definitionsEnum;
    }
}
