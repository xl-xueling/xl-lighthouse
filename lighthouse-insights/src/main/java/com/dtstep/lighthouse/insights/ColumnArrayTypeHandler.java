package com.dtstep.lighthouse.insights;

import com.dtstep.lighthouse.insights.modal.Column;
import com.fasterxml.jackson.core.type.TypeReference;

public class ColumnArrayTypeHandler extends JsonArrayTypeHandler<Column> {

    public ColumnArrayTypeHandler()
    {
        super(new TypeReference<>() {});
    }
}
