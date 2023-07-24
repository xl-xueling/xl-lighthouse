package com.dtstep.lighthouse.web.controller.data;
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
import com.dtstep.lighthouse.common.entity.display.BarWithTimeLineResult;
import com.dtstep.lighthouse.common.entity.display.MultiLineResult;
import com.dtstep.lighthouse.common.util.*;
import com.dtstep.lighthouse.web.controller.annotation.AuthorityPermission;
import com.dtstep.lighthouse.web.service.stat.StatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.stat.FilterParam;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.user.UserEntity;
import com.dtstep.lighthouse.common.entity.view.LimitValue;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.entity.view.TimeLineEntity;
import com.dtstep.lighthouse.common.enums.result.RequestCodeEnum;
import com.dtstep.lighthouse.common.enums.role.PrivilegeTypeEnum;
import com.dtstep.lighthouse.common.enums.stat.LimitTypeEnum;
import com.dtstep.lighthouse.core.batch.BatchAdapter;
import com.dtstep.lighthouse.core.storage.proxy.LimitStorageProxy;
import com.dtstep.lighthouse.web.param.ParamWrapper;
import com.dtstep.lighthouse.web.service.data.DataService;
import com.dtstep.lighthouse.web.controller.base.BaseController;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.Validate;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@ControllerAdvice
public class DataController extends BaseController {

    @Autowired
    private StatService statService;

    @Autowired
    private DataService dataService;

    @AuthorityPermission(relationParam = "statId", roleTypeEnum = PrivilegeTypeEnum.STAT_ITEM_USER)
    @AuthorityPermission(relationParam = "siteId", roleTypeEnum = PrivilegeTypeEnum.SITE_MAP_USER)
    @RequestMapping("/data/stat.shtml")
    public @ResponseBody
    ObjectNode statData(HttpServletRequest request) throws Exception {
        int statId = ParamWrapper.getIntValue(request, "statId");
        Date startDate = ParamWrapper.getDateValue(request,"startDate","yyyy-MM-dd");
        Date endDate = ParamWrapper.getDateValue(request,"endDate","yyyy-MM-dd");
        Validate.notNull(startDate);
        Validate.notNull(endDate);
        StatExtEntity statExtEntity = statService.queryById(statId);
        Validate.notNull(statExtEntity);
        UserEntity userEntity = ParamWrapper.getCurrentUser(request);
        MultiLineResult result = new MultiLineResult(statExtEntity.getTimeUnit());
        long periodStart = DateUtil.getDayStartTime(startDate.getTime());
        long periodEnd = DateUtil.getDayEndTime(endDate.getTime());
        String filterParams = ParamWrapper.getValue(request, "dimens");
        HashMap<String, String> dimensMapperMap = null;
        if (!StringUtil.isEmpty(filterParams)) {
            List<FilterParam> paramsList = JsonUtil.toJavaObjectList(filterParams,FilterParam.class);
            Set<String> filterSet = dataService.transformFilterSet(paramsList);
            if(CollectionUtils.isNotEmpty(filterSet)){
                statExtEntity = statService.queryMatchStat(userEntity, statExtEntity, filterSet);
                if (statExtEntity == null) {
                    return RequestCodeEnum.toJSON(RequestCodeEnum.DISPLAY_NOT_MATCH_STAT, String.join(";", filterSet));
                }
                dimensMapperMap = dataService.initDimensMapperMap(statExtEntity,paramsList);
            }
        }
        List<Long> batchList = BatchAdapter.queryBatchTimeList(statExtEntity.getTimeParam(), periodStart, periodEnd);
        if (MapUtils.isNotEmpty(dimensMapperMap)) {
            if (dimensMapperMap.size() > StatConst.QUERY_RESULT_DIMENS_SIZE) {
                return RequestCodeEnum.toJSON(RequestCodeEnum.DISPLAY_QUERY_DIMENS_EXCEED_LIMIT);
            }
            if (dimensMapperMap.size() * batchList.size() > StatConst.QUERY_RESULT_LIMIT_SIZE) {
                return RequestCodeEnum.toJSON(RequestCodeEnum.DISPLAY_QUERY_RESULT_EXCEED_LIMIT);
            }
            List<String> dimensList = Lists.newArrayList(dimensMapperMap.keySet());
            List<StatValue> dataList = dataService.queryWithDimensList(statExtEntity,dimensList,batchList);
            Map<String,List<StatValue>> dataMap = dataList.stream().collect(Collectors.groupingBy(StatValue::getDimens));
            for(String dimensValue : dataMap.keySet()){
                result.getDataMap().put(dimensValue,dataMap.get(dimensValue).stream().sorted(Comparator.comparing(StatValue::getBatchTime)).collect(Collectors.toList()));
            }
        } else if (StringUtil.isEmpty(statExtEntity.getTemplateEntity().getDimens())) {
            List<StatValue> valueList = dataService.queryWithDimens(statExtEntity,null, batchList);
            result.getDataMap().put("value", new ArrayList<>(valueList));
        }
        result.setDimensMapperInfo(dimensMapperMap);
        result.setBatchList(batchList);
        ObjectNode objectNode = JsonUtil.createObjectNode();
        objectNode.put("code", "0");
        objectNode.putPOJO("data", result);
        int relationId = statExtEntity.getId();
        if (statId != relationId) {
            objectNode.put("template", StringEscapeUtils.escapeHtml4(statExtEntity.getTemplate()));
            objectNode.put("relationId", relationId);
        }
        return objectNode;
    }


    @AuthorityPermission(relationParam = "statId", roleTypeEnum = PrivilegeTypeEnum.STAT_ITEM_USER)
    @AuthorityPermission(relationParam = "siteId", roleTypeEnum = PrivilegeTypeEnum.SITE_MAP_USER)
    @RequestMapping("/data/limit.shtml")
    public @ResponseBody
    ObjectNode limitData(HttpServletRequest request) throws Exception {
        int statId = ParamWrapper.getIntValue(request, "statId");
        int currentIndex = ParamWrapper.getIntValueOrElse(request, "currentIndex", -1);
        StatExtEntity statExtEntity = statService.queryById(statId);
        Validate.notNull(statExtEntity);
        Date startDate = ParamWrapper.getDateValue(request,"startDate","yyyy-MM-dd");
        Date endDate = ParamWrapper.getDateValue(request,"endDate","yyyy-MM-dd");
        Validate.notNull(startDate);
        Validate.notNull(endDate);
        long periodStart = DateUtil.getDayStartTime(startDate.getTime());
        long periodEnd = DateUtil.getDayEndTime(endDate.getTime());
        List<Long> batchList = BatchAdapter.queryBatchTimeList(statExtEntity.getTimeParam(), periodStart, periodEnd);
        Long curBatch;
        if (currentIndex == -1) {
            currentIndex = batchList.size() - 1;
            curBatch = batchList.get(currentIndex);
        } else {
            curBatch = batchList.get(currentIndex);
        }
        BarWithTimeLineResult result = new BarWithTimeLineResult(statExtEntity.getTimeUnit());
        List<LimitValue> limitValues = LimitStorageProxy.queryLimitDimens(statExtEntity,curBatch);
        List<String> dimensList = limitValues.stream().map(LimitValue::getDimensValue).collect(Collectors.toList());
        LinkedHashMap<String, StatValue> valueMap = dataService.queryWithDimensList(statExtEntity, dimensList,curBatch);
        List<Object> valueList;
        if (statExtEntity.getTemplateEntity().getLimitTypeEnum() == LimitTypeEnum.TOP) {
            valueList = valueMap.values().stream().sorted(desc).collect(Collectors.toList());
        } else {
            valueList = valueMap.values().stream().sorted(asc).collect(Collectors.toList());
        }
        result.setValueList(valueList);
        TimeLineEntity timeLineEntity = new TimeLineEntity();
        timeLineEntity.setCurrentIndex(currentIndex);
        timeLineEntity.setTimeList(batchList.stream().map(x -> DateUtil.formatTimeStamp(x, "yyyy-MM-dd HH:mm")).collect(Collectors.toList()));
        result.setTimeLine(timeLineEntity);
        result.setBatchTime(curBatch);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("code", "0");
        objectNode.putPOJO("data", result);
        return objectNode;
    }

    private static final Comparator<StatValue> desc = (e1, e2) -> {
        if (Double.parseDouble(e1.getValue().toString()) > Double.parseDouble(e2.getValue().toString())) {
            return -1;
        } else {
            return 1;
        }
    };

    private static final Comparator<StatValue> asc = (e1, e2) -> {
        if (Double.parseDouble(e1.getValue().toString()) > Double.parseDouble(e2.getValue().toString())) {
            return 1;
        } else {
            return -1;
        }
    };
}
