package com.dtstep.lighthouse.insights.types;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Map;

public class MapConfigTypeHandler extends BaseObjectTypeHandler<Map<String,Object>> {

    public MapConfigTypeHandler()
    {
        super(new TypeReference<>() {});
    }

}
