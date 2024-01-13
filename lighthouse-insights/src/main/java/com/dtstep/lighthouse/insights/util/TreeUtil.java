package com.dtstep.lighthouse.insights.util;

import com.dtstep.lighthouse.insights.dto.CommonTreeNode;
import com.dtstep.lighthouse.insights.dto.TreeNode;
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


    public static int getMaxLevel(List<TreeNode> root) {
        if (root == null || root.isEmpty()) {
            return 0;
        }
        int maxLevel = 0;
        List<TreeNode> levelNodes = new ArrayList<>(root);
        while (!levelNodes.isEmpty()) {
            int size = levelNodes.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = levelNodes.remove(0);
                if (node.getChildren() != null) {
                    levelNodes.addAll(node.getChildren());
                }
            }
            maxLevel++;
        }

        return maxLevel;
    }
}
