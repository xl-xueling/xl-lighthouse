package com.dtstep.lighthouse.insights.types;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Map;

public class ListConfigTypeHandler extends BaseObjectTypeHandler<List<Object>> {

    public ListConfigTypeHandler()
    {
        super(new TypeReference<>() {});
    }

}
