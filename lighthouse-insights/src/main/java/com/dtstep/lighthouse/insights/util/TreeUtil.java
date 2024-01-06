package com.dtstep.lighthouse.insights.util;

import com.dtstep.lighthouse.insights.dto.CommonTreeNode;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class TreeUtil {

    public static List<CommonTreeNode> buildTree(List<CommonTreeNode> list, String parentId) {
        List<CommonTreeNode> tempList = new ArrayList<>();
        for (CommonTreeNode node : list) {
            if (node.getPid().equals(parentId) ) {
                List<CommonTreeNode> childNode = buildTree(list, node.getId());
                if(CollectionUtils.isNotEmpty(childNode)){
                    node.setChildren(childNode);
                }
                tempList.add(node);
            }
        }
        return tempList;
    }
}
