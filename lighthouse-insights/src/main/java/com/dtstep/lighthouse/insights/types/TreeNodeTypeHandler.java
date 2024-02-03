package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.common.modal.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;

public class TreeNodeTypeHandler extends BaseObjectTypeHandler<TreeNode> {

    public TreeNodeTypeHandler()
    {
        super(new TypeReference<>() {});
    }

}
