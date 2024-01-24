package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.insights.modal.RenderConfig;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Map;

public class RenderConfigTypeHandler extends BaseObjectTypeHandler<RenderConfig> {

    public RenderConfigTypeHandler()
    {
        super(new TypeReference<>() {});
    }

}
