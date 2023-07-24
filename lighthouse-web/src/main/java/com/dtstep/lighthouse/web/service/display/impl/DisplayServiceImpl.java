package com.dtstep.lighthouse.web.service.display.impl;
/*
 * Copyright (C) 2022-2023 XueLing.雪灵
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
import com.dtstep.lighthouse.web.manager.components.ComponentsManager;
import com.dtstep.lighthouse.web.manager.group.GroupManager;
import com.dtstep.lighthouse.web.manager.stat.StatManager;
import com.dtstep.lighthouse.web.service.display.DisplayService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.entity.components.ComponentsEntity;
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.entity.project.ProjectEntity;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.tree.ZTreeViewNode;
import com.dtstep.lighthouse.common.enums.components.SysComponentsEnum;
import com.dtstep.lighthouse.common.enums.display.DisplayTypeEnum;
import com.dtstep.lighthouse.common.enums.stat.StatStateEnum;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.builtin.BuiltinLoader;
import com.dtstep.lighthouse.core.wrapper.DimensDBWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;


@Service
public class DisplayServiceImpl implements DisplayService {

    private static final Logger logger = LoggerFactory.getLogger(DisplayServiceImpl.class);

    @Autowired
    private GroupManager groupManager;

    @Autowired
    private StatManager statManager;

    @Autowired
    private ComponentsManager componentsManager;

    @Override
    public List<String> loadDimens(String token, String dimens, String last, int limitSize) {
        return DimensDBWrapper.loadDimension(token,dimens,last,limitSize);
    }

    @Override
    public List<DisplayTypeEnum> getDisplayTypeList(){
        return Lists.newArrayList(DisplayTypeEnum.LineChart,DisplayTypeEnum.AreaChart,DisplayTypeEnum.BarChart);
    }

    @Override
    public List<ZTreeViewNode> queryProjectTreeInfo(ProjectEntity projectEntity) throws Exception {
        List<ZTreeViewNode> zTreeViewNodes = new ArrayList<>();
        ZTreeViewNode rootNode = new ZTreeViewNode();
        rootNode.setId(String.valueOf(projectEntity.getId()));
        rootNode.setpId(SysConst.TREE_ROOT_NODE_NAME);
        rootNode.setType(1);
        rootNode.setOpen(true);
        rootNode.setName("i18n(ldp_i18n_display_project_1004)" + projectEntity.getName());
        rootNode.setIcon("/static/extend/png/depart.png");
        zTreeViewNodes.add(rootNode);
        int projectId = projectEntity.getId();
        List<GroupExtEntity> groupList;
        if(BuiltinLoader.isBuiltinProject(projectId)){
            groupList = BuiltinLoader.getAllGroups();
        }else {
            groupList = groupManager.queryListByProjectId(projectEntity.getId());
        }
        if(CollectionUtils.isEmpty(groupList)){
            return zTreeViewNodes;
        }
        for (GroupExtEntity groupExtEntity : groupList) {
            ZTreeViewNode groupNode = new ZTreeViewNode();
            groupNode.setName("i18n(ldp_i18n_display_project_1005)" + groupExtEntity.getToken());
            groupNode.setIcon("/static/extend/png/plugin.png");
            groupNode.setId(String.valueOf(groupExtEntity.getId()));
            groupNode.setpId(String.valueOf(projectEntity.getId()));
            groupNode.setType(2);
            zTreeViewNodes.add(groupNode);
            List<StatExtEntity> itemList = null;
            if(BuiltinLoader.isBuiltinProject(projectId)){
                itemList = BuiltinLoader.getBuiltinStatByGroupId(groupExtEntity.getId());
            }else{
                itemList = statManager.queryListByGroupId(groupExtEntity.getId());
            }
            if (CollectionUtils.isNotEmpty(itemList)) {
                for (StatExtEntity statExtEntity : itemList) {
                    ZTreeViewNode statNode = new ZTreeViewNode();
                    statNode.setId(String.valueOf(statExtEntity.getId()));
                    statNode.setpId(String.valueOf(groupExtEntity.getId()));
                    statNode.setName(statExtEntity.getTitle());
                    if(statExtEntity.getStatStateEnum() == StatStateEnum.RUNNING){
                        statNode.setIcon("/static/extend/png/running.png");
                    }else if(statExtEntity.getStatStateEnum() == StatStateEnum.LIMITING){
                        statNode.setIcon("/static/extend/png/pause.png");
                    }else{
                        statNode.setIcon("/static/extend/png/stop.png");
                    }
                    statNode.setType(3);
                    zTreeViewNodes.add(statNode);
                }
            }
        }
        return zTreeViewNodes;
    }

    @Override
    public ArrayNode queryPageFilterConfig(StatExtEntity statExtEntity) throws Exception {
        String[] dimensArr = statExtEntity.getTemplateEntity().getDimensArr();
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode arrayNode = objectMapper.createArrayNode();
        String filterConfig = statExtEntity.getFilterConfig();
        if(StringUtil.isEmpty(filterConfig)){
            if(dimensArr != null){
                for(String dimens : dimensArr){
                    List<String> dimensValueList = loadDimens(statExtEntity.getToken(),dimens,null,StatConst.DIMENS_THRESHOLD_LIMIT_COUNT);
                    ObjectNode objectNode = arrayNode.addObject();
                    objectNode.put("filter",dimens);
                    objectNode.put("label",dimens);
                    objectNode.put("level","1");
                    objectNode.put("type",SysComponentsEnum.SYS_COMP_MULTI_SELECTOR.getComponentsTypeEnum().getComponentsType());
                    objectNode.put("componentsId",SysComponentsEnum.SYS_COMP_MULTI_SELECTOR.getComponentsId());
                    ArrayNode dimensArrayNode = objectNode.putArray("data");
                    if(CollectionUtils.isNotEmpty(dimensValueList)){
                        dimensValueList.forEach(z -> {
                            ObjectNode subNode = dimensArrayNode.addObject();
                            subNode.put("name",z);
                            subNode.put("id",z);
                        });
                    }
                }
            }
        }else{
            JsonNode filterConfigArr = objectMapper.readTree(filterConfig);
            for (JsonNode jsonNode : filterConfigArr) {
                JsonNode componentsIdNode = jsonNode.get("componentsId");
                int componentsId;
                if(componentsIdNode == null){
                    componentsId = SysComponentsEnum.SYS_COMP_MULTI_SELECTOR.getComponentsId();
                }else{
                    componentsId = componentsIdNode.asInt();
                }
                int type = jsonNode.get("type").asInt();
                String filter = jsonNode.get("filter").asText();
                String label = jsonNode.get("label").asText();
                if(SysComponentsEnum.isSysComponents(componentsId)){
                    List<String> dimensValueList = loadDimens(statExtEntity.getToken(),filter,null,StatConst.DIMENS_THRESHOLD_LIMIT_COUNT);
                    ObjectNode objectNode = arrayNode.addObject();
                    objectNode.put("filter",filter);
                    objectNode.put("label",label);
                    objectNode.put("level","1");
                    objectNode.put("type",type);
                    objectNode.put("componentsId",componentsId);
                    ArrayNode dimensArrayNode = objectNode.putArray("data");
                    if(CollectionUtils.isNotEmpty(dimensValueList)){
                        dimensValueList.forEach(z -> {
                            ObjectNode subNode = dimensArrayNode.addObject();
                            subNode.put("name",z);
                            subNode.put("id",z);
                        });
                    }
                }else{
                    ComponentsEntity componentsEntity = componentsManager.queryById(componentsId);
                    if(componentsEntity != null){
                        ObjectNode objectNode = arrayNode.addObject();
                        objectNode.put("filter",filter);
                        JsonNode dataNode = objectMapper.readTree(componentsEntity.getData());
                        objectNode.put("level",componentsEntity.getLevel());
                        objectNode.put("data", dataNode);
                        objectNode.put("label",label);
                        objectNode.put("type",type);
                        objectNode.put("componentsId",componentsId);
                    }
                }
            }
        }
        return arrayNode;
    }
}
