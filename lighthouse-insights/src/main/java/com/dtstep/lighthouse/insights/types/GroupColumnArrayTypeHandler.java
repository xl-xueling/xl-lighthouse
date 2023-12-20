package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.insights.modal.Column;
import com.fasterxml.jackson.core.type.TypeReference;

public class GroupColumnArrayTypeHandler extends JsonArrayTypeHandler<Column> {

    public GroupColumnArrayTypeHandler()
    {
        super(new TypeReference<>() {});
    }
}
