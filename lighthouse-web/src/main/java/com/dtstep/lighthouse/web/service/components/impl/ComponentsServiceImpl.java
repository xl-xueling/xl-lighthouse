package com.dtstep.lighthouse.web.service.components.impl;
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
import com.dtstep.lighthouse.web.manager.user.UserManager;
import com.dtstep.lighthouse.web.param.ParamWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.entity.components.ComponentsEntity;
import com.dtstep.lighthouse.common.entity.components.ComponentsViewEntity;
import com.dtstep.lighthouse.common.entity.list.ListViewDataObject;
import com.dtstep.lighthouse.common.entity.paging.PageEntity;
import com.dtstep.lighthouse.common.entity.user.UserEntity;
import com.dtstep.lighthouse.common.enums.components.ComponentsTypeEnum;
import com.dtstep.lighthouse.common.enums.components.SysComponentsEnum;
import com.dtstep.lighthouse.common.exception.ComponentsCheckException;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import com.dtstep.lighthouse.core.sql.SqlBinder;
import com.dtstep.lighthouse.web.dao.ComponentsDao;
import com.dtstep.lighthouse.web.service.components.ComponentsService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;


@Service
public class ComponentsServiceImpl implements ComponentsService {

    private static final Logger logger = LoggerFactory.getLogger(ComponentsServiceImpl.class);

    @Autowired
    private UserManager userManager;

    @Autowired
    private ComponentsDao componentsDao;

    @Override
    public ListViewDataObject queryListByPage(UserEntity currentUser, int page, String search) throws Exception {
        int totalSize = countByParams(currentUser,search);
        HashMap<String,Object> urlMap = new HashMap<>();
        urlMap.put("search",search);
        ListViewDataObject listObject = new ListViewDataObject();
        String baseUrl = ParamWrapper.getBaseLink("/components/list.shtml",urlMap);
        PageEntity pageEntity = new PageEntity(baseUrl, page,totalSize, SysConst._LIST_PAGE_SIZE);
        listObject.setPageEntity(pageEntity);
        List<ComponentsViewEntity> viewEntityList = queryListByPage(currentUser,search,pageEntity);
        listObject.setDataList(viewEntityList);
        return listObject;
    }

    protected List<ComponentsViewEntity> queryListByPage(UserEntity currentUser,String search, PageEntity pageEntity) throws Exception {
        int userId = currentUser.getId();
        SqlBinder sqlBinder = new SqlBinder.Builder()
                .appendSegment("select * from ldp_components")
                .appendWhereSegment("(private_flag = 0 or user_id = ?)")
                .appendLike("title",search)
                .appendSegment("order by create_time desc limit ?,?").create();
        List<ComponentsEntity> entityList = DaoHelper.sql.getList(ComponentsEntity.class,sqlBinder.toString(), userId,pageEntity.getStartIndex() - 1, pageEntity.getPageSize());
        if(CollectionUtils.isEmpty(entityList)){
            return null;
        }
        List<ComponentsViewEntity> viewEntityList = new ArrayList<>();
        entityList.forEach(z -> {
            ComponentsViewEntity componentsViewEntity = new ComponentsViewEntity(z);
            try{
                UserEntity createUser = userManager.queryById(componentsViewEntity.getUserId());
                componentsViewEntity.setUserEntity(createUser);
            }catch (Exception ex){
                logger.error("query components create user error!",ex);
            }
            viewEntityList.add(componentsViewEntity);
        });
        return viewEntityList;
    }

    protected int countByParams(UserEntity userEntity, String search) throws Exception {
        int userId = userEntity.getId();
        SqlBinder sqlBinder = new SqlBinder.Builder()
                .appendSegment("select count(1) from ldp_components")
                .appendWhereSegment("(private_flag = 0 or user_id = ?)")
                .appendLike("title",search).create();
        return DaoHelper.sql.count(sqlBinder.toString(),userId);
    }

    @Override
    public List<ComponentsEntity> queryComponentsList(int userId, int startIndex, int limitSize) throws Exception {
        return DaoHelper.sql.getList(ComponentsEntity.class,"select * from ldp_components where (private_flag = 0 or user_id = ?) limit ?,?",userId,startIndex,limitSize);
    }

    @Override
    public void register(ComponentsEntity componentsEntity) throws Exception {
        int level = getLevel(componentsEntity.getData());
        componentsEntity.setLevel(level);
        componentsDao.save(componentsEntity);
    }

    @Override
    public int getLevel(String data) throws Exception {
        JsonNode jsonNode = JsonUtil.readTree(data);
        Validate.notNull(jsonNode);
        return getLevel(jsonNode,1);
    }

    private int getLevel(JsonNode jsonNode, int level){
        List<JsonNode> nodes = jsonNode.findValues("children");
        if(CollectionUtils.isNotEmpty(nodes)){
            JsonNode curNode = nodes.get(0);
            if(curNode == null || curNode.get(0).get("id") == null){
                return level;
            }
            int baseLevel = level + 1;
            for(JsonNode subNode : nodes){
                if(subNode != null){
                    int tempLevel = getLevel(subNode,baseLevel);
                    if(tempLevel > level){
                        level = tempLevel;
                    }
                }
            }
        }
        return level;
    }

    @Override
    public void delete(int componentsId) throws Exception {
        componentsDao.delete(componentsId);
    }

    @Override
    public void update(ComponentsEntity componentsEntity) throws Exception{
        componentsDao.update(componentsEntity);
    }

    @Override
    public ComponentsEntity queryById(int id) throws Exception {
        return componentsDao.queryById(id);
    }

    @Override
    public void checkData(String data, int componentsType) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(data);
        ComponentsTypeEnum componentsTypeEnum = ComponentsTypeEnum.getComponentsType(componentsType);
        if(componentsTypeEnum == null){
            throw new ComponentsCheckException("i18n(ldp_i18n_components_create_1016)");
        }
        dataFormat(jsonNode,componentsTypeEnum);
        int maxLevel = getLevel(data);
        String levelPattern = componentsTypeEnum.getLevelPattern();
        if(StringUtil.isNotEmpty(levelPattern)){
            boolean isValid = Pattern.compile(levelPattern).matcher(String.valueOf(maxLevel)).matches();
            if(!isValid){
                throw new ComponentsCheckException(String.format("i18n(ldp_i18n_components_create_1017,%s)", levelPattern));
            }
        }
    }

    private void dataFormat(JsonNode jsonNode, ComponentsTypeEnum componentsTypeEnum){
        int size = jsonNode.size();
        for(int i=0;i<size;i++){
            JsonNode subNode = jsonNode.get(i);
            Iterator<String> it = subNode.fieldNames();
            while (it.hasNext()){
                String fieldName = it.next();
                switch (fieldName) {
                    case "name":
                        JsonNode nameNode = subNode.get("name");
                        if(nameNode == null || StringUtil.isEmpty(nameNode.asText())){
                            throw new ComponentsCheckException("i18n(ldp_i18n_components_create_1018)");
                        }
                    case "id":
                        JsonNode idNode = subNode.get("id");
                        if(idNode == null || StringUtil.isEmpty(idNode.asText())){
                            throw new ComponentsCheckException("i18n(ldp_i18n_components_create_1019)");
                        }
                        break;
                    case "children":
                        if (componentsTypeEnum != ComponentsTypeEnum.CascadeRatioSelectionBox && componentsTypeEnum != ComponentsTypeEnum.CascadeMultipleSelectionBox) {
                            throw new ComponentsCheckException(String.format("i18n(ldp_i18n_components_create_1020,%s)", componentsTypeEnum.name()));
                        }
                        JsonNode childRen = subNode.get("children");
                        dataFormat(childRen, componentsTypeEnum);
                        break;
                    default:
                        throw new ComponentsCheckException(String.format("i18n(ldp_i18n_components_create_1021,%s)", fieldName));
                }
            }
        }
    }

    @Override
    public ArrayNode querySystemComponentsList() {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode arrayNode = objectMapper.createArrayNode();
        for(SysComponentsEnum sysComponentsEnum : SysComponentsEnum.values()){
            ObjectNode objectNode = arrayNode.addObject();
            objectNode.put("level",1);
            ArrayNode dataNode = objectMapper.createArrayNode();
            objectNode.put("data", dataNode);
            objectNode.put("type",sysComponentsEnum.getComponentsTypeEnum().getComponentsType());
            objectNode.put("title",sysComponentsEnum.getComponentsTypeEnum().getDesc());
            objectNode.put("id",sysComponentsEnum.getComponentsId());
        }
        return arrayNode;
    }

}
