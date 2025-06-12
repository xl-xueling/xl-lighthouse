package com.dtstep.lighthouse.common.modal;
/*
 * Copyright (C) 2022-2025 XueLing.雪灵
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TreeNode implements Serializable {

    private String key;

    private String label;

    public Object value;

    private String type;

    private List<TreeNode> children;

    private TreeNode(){}

    public TreeNode(String label,Object value){
        this.label = label;
        this.value = value;
        this.key = String.valueOf(value);
    }

    public TreeNode(String label,Object value,String type){
        this.label = label;
        this.value = value;
        this.type = type;
        this.key = type + "_" + value;
    }

    public TreeNode(String key,String label,Object value,String type){
        this.key = key;
        this.type = type;
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void addChild(TreeNode treeNode){
        if(this.children == null){
            this.children = new ArrayList<>();
        }
        this.children.add(treeNode);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
