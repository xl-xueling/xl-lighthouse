package com.dtstep.lighthouse.ice.servant;
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
import Ice.Current;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.ice._AuxInterfaceDisp;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.wrapper.GroupDBWrapper;
import com.dtstep.lighthouse.core.wrapper.StatDBWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


final class AuxI extends _AuxInterfaceDisp {

    private static final long serialVersionUID = -2277521746711908866L;

    private static final Logger logger = LoggerFactory.getLogger(AuxI.class);

    public AuxI() {}

    @Override
    public String queryGroupByToken(String token, Current __current) {
        if(StringUtil.isEmpty(token)){
            return null;
        }
        String result = null;
        try{
            GroupExtEntity groupExtEntity = GroupDBWrapper.queryByToken(token);
            if(groupExtEntity != null){
                GroupVerifyEntity groupVerifyEntity = new GroupVerifyEntity();
                groupVerifyEntity.setVerifyKey(groupExtEntity.getVerifyKey());
                groupVerifyEntity.setRelationColumns(groupExtEntity.getRelatedColumns());
                groupVerifyEntity.setState(groupExtEntity.getState());
                groupVerifyEntity.setGroupId(groupExtEntity.getId());
                groupVerifyEntity.setToken(groupExtEntity.getToken());
                groupVerifyEntity.setMinTimeParam(groupExtEntity.getMinTimeParam());
                ObjectMapper mapper = new ObjectMapper();
                result = mapper.writeValueAsString(groupVerifyEntity);
            }
        }catch (Exception ex){
            logger.error("lighthouse ice,query group by token error,token:{}",token,ex);
        }
        return result;
    }

    @Override
    public String queryStatById(int statId, Current __current) {
        String result = null;
        try{
            StatExtEntity statExtEntity = StatDBWrapper.queryById(statId);
            if(statExtEntity != null){
                ObjectMapper mapper = new ObjectMapper();
                result = mapper.writeValueAsString(statExtEntity);
            }
        }catch (Exception ex){
            logger.error("lighthouse ice,query stat by id error,statId:{}",statId,ex);
        }
        return result;
    }

}
