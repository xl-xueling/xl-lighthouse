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
import com.dtstep.lighthouse.common.entity.ListData;
import com.dtstep.lighthouse.common.modal.Stat;
import com.dtstep.lighthouse.insights.dao.RecordDao;
import com.dtstep.lighthouse.insights.dao.StatDao;
import com.dtstep.lighthouse.insights.dto.RecordQueryParam;
import com.dtstep.lighthouse.common.modal.Record;
import com.dtstep.lighthouse.insights.service.BaseService;
import com.dtstep.lighthouse.insights.service.RecordService;
import com.dtstep.lighthouse.insights.service.StatService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordDao recordDao;

    @Autowired
    private StatDao statDao;

    @Override
    public int create(Record record) {
        return recordDao.insert(record);
    }

    @Override
    public ListData<Record> queryList(RecordQueryParam queryParam,Integer pageNum,Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        PageInfo<Record> pageInfo;
        try{
            List<Record> recordList = recordDao.queryList(queryParam);
            pageInfo = new PageInfo<>(recordList);
        }finally {
            PageHelper.clearPage();
        }
        return ListData.newInstance(pageInfo.getList(),pageInfo.getTotal(),pageNum,pageSize);
    }

    @Override
    public ListData<Record> queryStatLimitList(Integer statId, Integer pageNum, Integer pageSize) {
        Stat stat = statDao.queryById(statId);
        Validate.notNull(stat);
        PageHelper.startPage(pageNum,pageSize);
        PageInfo<Record> pageInfo;
        try{
            List<Record> recordList = recordDao.queryStatLimit(stat.getId(),stat.getGroupId());
            pageInfo = new PageInfo<>(recordList);
        }finally {
            PageHelper.clearPage();
        }
        return ListData.newInstance(pageInfo.getList(),pageInfo.getTotal(),pageNum,pageSize);
    }
}
