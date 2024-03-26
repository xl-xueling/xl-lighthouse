package com.dtstep.lighthouse.insights.controller;
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
import com.dtstep.lighthouse.common.modal.IDParam;
import com.dtstep.lighthouse.common.modal.ListSearchObject;
import com.dtstep.lighthouse.insights.vo.ResultData;
import com.dtstep.lighthouse.common.entity.ListData;
import com.dtstep.lighthouse.insights.dto.RecordQueryParam;
import com.dtstep.lighthouse.common.modal.Record;
import com.dtstep.lighthouse.insights.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class RecordsController {

    @Autowired
    private RecordService recordService;

    @PostMapping("/record/list")
    public ResultData<ListData<Record>> queryList(@Validated @RequestBody ListSearchObject<RecordQueryParam> searchObject){
        RecordQueryParam queryParam = searchObject.getQueryParamOrDefault(new RecordQueryParam());
        Integer pageNum = searchObject.getPagination().getPageNum();
        Integer pageSize = searchObject.getPagination().getPageSize();
        ListData<Record> listData = recordService.queryList(queryParam,pageNum,pageSize);
        return ResultData.success(listData);
    }

    @PostMapping("/record/statLimitList")
    public ResultData<ListData<Record>> queryStatLimitList(@Validated @RequestBody ListSearchObject<IDParam> searchObject){
        IDParam idParam = searchObject.getQueryParams();
        Integer statId = idParam.getId();
        Integer pageNum = searchObject.getPagination().getPageNum();
        Integer pageSize = searchObject.getPagination().getPageSize();
        ListData<Record> listData = recordService.queryStatLimitList(statId,pageNum,pageSize);
        return ResultData.success(listData);
    }
}
