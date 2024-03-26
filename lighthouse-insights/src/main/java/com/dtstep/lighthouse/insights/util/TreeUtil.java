package com.dtstep.lighthouse.insights.util;
/*
 * Copyright (C) 2022-2024 XueLing.雪灵
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import com.dtstep.lighthouse.common.modal.CommonTreeNode;
import com.dtstep.lighthouse.common.modal.TreeNode;
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

    public static List<String> getAllKeys(List<TreeNode> nodes) {
        List<String> values = new ArrayList<>();
        for (TreeNode node : nodes) {
            values.add(node.getKey());
            if (CollectionUtils.isNotEmpty(node.getChildren())) {
                values.addAll(getAllKeys(node.getChildren()));
            }
        }
        return values;
    }


    public static TreeNode findNodeByValue(List<TreeNode> nodes, String key) {
        for (TreeNode node : nodes) {
            if (node.getKey().equals(key)) {
                return node;
            }
            if (CollectionUtils.isNotEmpty(node.getChildren())) {
                TreeNode childResult = findNodeByValue(node.getChildren(), key);
                if (childResult != null) {
                    return childResult;
                }
            }
        }
        return null;
    }



}
