package com.dtstep.lighthouse.web.service.meta.impl;
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
import com.dtstep.lighthouse.common.entity.meta.MetaTableEntity;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import com.dtstep.lighthouse.core.wrapper.MetaTableWrapper;
import com.dtstep.lighthouse.web.manager.meta.MetaTableManager;
import com.dtstep.lighthouse.web.service.meta.MetaTableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class MetaTableServiceImpl implements MetaTableService {

    private static final Logger logger = LoggerFactory.getLogger(MetaTableServiceImpl.class);

    @Autowired
    private MetaTableManager metaTableManager;

    @Override
    public MetaTableEntity queryById(int metaId) {
        return MetaTableWrapper.queryById(metaId);
    }

    @Override
    public List<MetaTableEntity> queryAllResultTables() throws Exception {
        return metaTableManager.queryAllResultTables();
    }

    @Override
    public void delete(int id) throws Exception {
        DaoHelper.sql.execute("delete from ldp_meta_table where id = ?", id);
        MetaTableWrapper.removeMetaTableCache(id);
    }

    @Override
    public void update(MetaTableEntity metaTableEntity) throws Exception {
        DaoHelper.sql.execute("update ldp_meta_table set update_time = ?,record_size = ? where id = ?",new Date(),metaTableEntity.getRecordSize(),metaTableEntity.getId());
        MetaTableWrapper.removeMetaTableCache(metaTableEntity.getId());
    }
}
