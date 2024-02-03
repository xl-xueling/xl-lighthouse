package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.common.modal.GroupExtendConfig;
import com.fasterxml.jackson.core.type.TypeReference;

public class GroupExtendConfigTypeHandler extends BaseObjectTypeHandler<GroupExtendConfig> {

    public GroupExtendConfigTypeHandler()
    {
        super(new TypeReference<>() {});
    }

}
