package com.dtstep.lighthouse.web.service.relations.impl;
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

import com.dtstep.lighthouse.common.entity.relations.RelationEntity;
import com.dtstep.lighthouse.common.enums.relations.RelationTypeEnum;
import com.dtstep.lighthouse.core.dao.ConnectionManager;
import com.dtstep.lighthouse.core.dao.DBConnection;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import com.dtstep.lighthouse.web.manager.relations.RelationManager;
import com.dtstep.lighthouse.web.service.relations.RelationService;
import com.dtstep.lighthouse.web.dao.RelationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RelationServiceImpl implements RelationService {

    @Autowired
    private RelationDao relationDao;

    @Autowired
    private RelationManager relationManager;

    @Override
    public void save(RelationEntity relationEntity) throws Exception {
        DBConnection connection = ConnectionManager.getConnection();
        ConnectionManager.beginTransaction(connection);
        try{
            int relationType = relationEntity.getRelationType();
            RelationTypeEnum relationTypeEnum = RelationTypeEnum.getInstance(relationType);
            assert relationTypeEnum != null;
            relationManager.delete(relationEntity.getRelationA(), relationEntity.getRelationB(), relationTypeEnum);
            relationDao.save(relationEntity);
            ConnectionManager.commitTransaction(connection);
        }catch (Exception ex){
            ConnectionManager.rollbackTransaction(connection);
            throw ex;
        }finally {
            ConnectionManager.close(connection);
        }
    }

    @Override
    public int count(int userId, int relationType) throws Exception {
        return DaoHelper.sql.count("select count(1) from ldp_relations where relation_a =? and relation_type=?",userId, relationType);
    }

    @Override
    public void delete(int userId, int relationId, RelationTypeEnum relationTypeEnum) throws Exception {
        relationManager.delete(userId,relationId,relationTypeEnum);
    }
}
