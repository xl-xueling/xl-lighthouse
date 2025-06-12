package com.dtstep.lighthouse.insights.service.impl;
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
import com.dtstep.lighthouse.common.enums.MetaTableTypeEnum;
import com.dtstep.lighthouse.common.modal.MetaTable;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.core.storage.warehouse.WarehouseStorageEngine;
import com.dtstep.lighthouse.core.storage.warehouse.WarehouseStorageEngineProxy;
import com.dtstep.lighthouse.core.wrapper.MetaTableWrapper;
import com.dtstep.lighthouse.insights.dao.MetaTableDao;
import com.dtstep.lighthouse.insights.dto.MetaTableQueryParam;
import com.dtstep.lighthouse.insights.service.MetaTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class MetaTableServiceImpl implements MetaTableService {

    @Autowired
    private MetaTableDao metaTableDao;

    @Override
    public int getCurrentStatResultTable() throws Exception {
        WarehouseStorageEngine warehouseStorageEngine = WarehouseStorageEngineProxy.getInstance();
        MetaTableQueryParam metaTableQueryParam = new MetaTableQueryParam();
        long maxTimeInterval = warehouseStorageEngine.getTableMaxValidPeriod();
        long timestamp = DateUtil.getSecondBefore(System.currentTimeMillis(),maxTimeInterval);
        LocalDateTime startDate = DateUtil.timestampToLocalDateTime(timestamp);
        metaTableQueryParam.setStartDate(startDate);
        metaTableQueryParam.setMetaTableTypeEnum(MetaTableTypeEnum.STAT_RESULT_TABLE);
        MetaTable metaTable = metaTableDao.getCurrentStorageTable(metaTableQueryParam);
        int metaId;
        if(metaTable == null || !warehouseStorageEngine.isAppendable(metaTable.getMetaName())){
            metaId = MetaTableWrapper.createStatStorageAndMetaTable();
        }else{
            metaId = metaTable.getId();

        }
        return metaId;
    }

}
