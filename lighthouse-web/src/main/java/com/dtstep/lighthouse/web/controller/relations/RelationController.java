package com.dtstep.lighthouse.web.controller.relations;
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
import com.dtstep.lighthouse.web.manager.project.ProjectManager;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.entity.relations.RelationEntity;
import com.dtstep.lighthouse.common.enums.relations.RelationTypeEnum;
import com.dtstep.lighthouse.common.enums.result.RequestCodeEnum;
import com.dtstep.lighthouse.web.manager.stat.StatManager;
import com.dtstep.lighthouse.web.param.ParamWrapper;
import com.dtstep.lighthouse.web.controller.base.BaseController;
import com.dtstep.lighthouse.web.service.relations.RelationService;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@ControllerAdvice
public class RelationController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(RelationController.class);

    @Autowired
    private RelationService relationService;

    @Autowired
    private StatManager statManager;

    @Autowired
    private ProjectManager projectManager;

    @RequestMapping("/relations/create/submit.shtml")
    public @ResponseBody
    ObjectNode create(HttpServletRequest request) throws Exception {
        int userId = ParamWrapper.getCurrentUserId(request);
        int relationB = ParamWrapper.getIntValue(request,"relationB");
        int relationType = ParamWrapper.getIntValue(request,"relationType");
        ParamWrapper.valid(RelationEntity.class,"relationB",relationB);
        ParamWrapper.valid(RelationEntity.class,"relationType",relationType);
        RelationTypeEnum relationTypeEnum = RelationTypeEnum.getInstance(relationType);
        Validate.notNull(relationTypeEnum);
        if(relationTypeEnum == RelationTypeEnum.FAVORITE_ITEM){
            Validate.isTrue(statManager.isExist(relationB));
        }else if(relationTypeEnum == RelationTypeEnum.FAVORITE_PROJECT){
            Validate.isTrue(projectManager.isExist(relationB));
        }
        if(relationService.count(userId,relationType) > SysConst.RELATION_SIZE_LIMIT){
            logger.info("add submit,the number of favorites exceeds the limit,relationA:{},relationB:{},favoritesType:{}",userId,relationB,relationType);
            return RequestCodeEnum.toJSON(RequestCodeEnum.RELATIONS_EXCEED_LIMIT);
        }
        RelationEntity relationEntity = new RelationEntity();
        relationEntity.setRelationType(relationType);
        relationEntity.setRelationA(userId);
        relationEntity.setRelationB(relationB);
        try{
            relationService.save(relationEntity);
        }catch (Exception ex){
            logger.error("relations create error!",ex);
            return RequestCodeEnum.toJSON(RequestCodeEnum.SYSTEM_ERROR);
        }
        return RequestCodeEnum.toJSON(RequestCodeEnum.SUCCESS);
    }

    @RequestMapping("/relations/remove/submit.shtml")
    public @ResponseBody
    ObjectNode remove(HttpServletRequest request) throws Exception{
        int userId = ParamWrapper.getCurrentUserId(request);
        int relationB = ParamWrapper.getIntValue(request,"relationB");
        int relationType = ParamWrapper.getIntValue(request,"relationType");
        ParamWrapper.valid(RelationEntity.class,"relationB",relationB);
        ParamWrapper.valid(RelationEntity.class,"relationType",relationType);
        RelationTypeEnum relationTypeEnum = RelationTypeEnum.getInstance(relationType);
        Validate.notNull(relationTypeEnum);
        if(relationTypeEnum == RelationTypeEnum.FAVORITE_ITEM){
            Validate.isTrue(statManager.isExist(relationB));
        }else if(relationTypeEnum == RelationTypeEnum.FAVORITE_PROJECT){
            Validate.isTrue(projectManager.isExist(relationB));
        }
        try{
            relationService.delete(userId,relationB,relationTypeEnum);
        }catch (Exception ex){
            logger.error("relations remove error!",ex);
            return RequestCodeEnum.toJSON(RequestCodeEnum.SYSTEM_ERROR);
        }
        return RequestCodeEnum.toJSON(RequestCodeEnum.SUCCESS);
    }

}
