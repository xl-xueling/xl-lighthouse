package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.common.modal.Column;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

public class GroupColumnArrayTypeHandler extends BaseObjectTypeHandler<List<Column>> {

    public GroupColumnArrayTypeHandler()
    {
        super(new TypeReference<>() {});
    }
}
