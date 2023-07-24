package com.dtstep.lighthouse.web.controller.track;
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
import avro.shaded.com.google.common.base.Joiner;
import com.dtstep.lighthouse.common.constant.RedisConst;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.message.MessageValid;
import com.dtstep.lighthouse.web.controller.annotation.AuthorityPermission;
import com.dtstep.lighthouse.web.manager.group.GroupManager;
import com.dtstep.lighthouse.web.manager.stat.StatManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.state.StatState;
import com.dtstep.lighthouse.common.entity.message.LightMessage;
import com.dtstep.lighthouse.common.entity.meta.MetaColumn;
import com.dtstep.lighthouse.common.enums.result.RequestCodeEnum;
import com.dtstep.lighthouse.common.enums.role.PrivilegeTypeEnum;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.batch.BatchAdapter;
import com.dtstep.lighthouse.core.builtin.BuiltinLoader;
import com.dtstep.lighthouse.core.formula.FormulaCalculate;
import com.dtstep.lighthouse.core.expression.embed.AviatorHandler;
import com.dtstep.lighthouse.core.formula.FormulaTranslate;
import com.dtstep.lighthouse.web.param.ParamWrapper;
import com.dtstep.lighthouse.core.redis.RedisHandler;
import com.dtstep.lighthouse.web.controller.base.BaseController;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static com.dtstep.lighthouse.common.constant.StatConst.ILLEGAL_VAL;
import static com.dtstep.lighthouse.common.constant.StatConst.NIL_VAL;


@RestController
@ControllerAdvice
public class TrackController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(TrackController.class);

    @Autowired
    private GroupManager groupManager;

    @Autowired
    private StatManager statManager;

    @RequestMapping("/track/stat.shtml")
    public ModelAndView track(HttpServletRequest request, ModelMap model) throws Exception{
        int statId = ParamWrapper.getIntValueOrElse(request,"statId",-1);
        StatExtEntity statExtEntity = statManager.queryById(statId);
        Validate.notNull(statExtEntity);
        GroupExtEntity groupExtEntity = groupManager.actualQueryById(statExtEntity.getGroupId());
        Validate.notNull(groupExtEntity);
        StatExtEntity fixStatExtEntity = BuiltinLoader.getBuiltinStat(1013);
        model.addAttribute("fixStatEntity", fixStatExtEntity);
        model.addAttribute("statEntity", statExtEntity);
        model.addAttribute("groupEntity", groupExtEntity);
        return new ModelAndView("/track/stat_track",model);
    }

    @AuthorityPermission(relationParam = "statId",roleTypeEnum = PrivilegeTypeEnum.STAT_ITEM_ADMIN)
    @RequestMapping(value = "/track/stateSubmit.shtml")
    public @ResponseBody
    ObjectNode trackStateSubmit(HttpServletRequest request) throws Exception{
        int statId = ParamWrapper.getIntValueOrElse(request,"statId",-1);
        int state = ParamWrapper.getIntValueOrElse(request,"state",0);
        StatExtEntity statExtEntity = statManager.queryById(statId);
        Validate.notNull(statExtEntity);
        groupManager.changeDebugMode(statExtEntity.getGroupId(),state);
        return RequestCodeEnum.toJSON(RequestCodeEnum.SUCCESS);
    }


    @RequestMapping(value = "/track/records.shtml")
    public @ResponseBody
    ObjectNode records(HttpServletRequest request) throws Exception{
        int statId = ParamWrapper.getIntValueOrElse(request,"statId",-1);
        StatExtEntity statExtEntity = statManager.queryById(statId);
        Validate.notNull(statExtEntity);
        GroupExtEntity groupExtEntity = groupManager.queryById(statExtEntity.getGroupId());
        List<LinkedHashMap<String,String>> dataList = Lists.newArrayList();
        long startTime = ParamWrapper.getLongValueOrElse(request,"startTime",0L);
        long endTime = ParamWrapper.getLongValueOrElse(request,"endTime",0L);
        List<LightMessage> messageList = null;
        String key = RedisConst.TRACK_PREFIX + "_" + statExtEntity.getGroupId();
        List<String> list = RedisHandler.getInstance().lrange(key,0, StatConst.GROUP_MESSAGE_MAX_CACHE_SIZE);
        if(CollectionUtils.isNotEmpty(list)){
            messageList = list.stream().map(x -> JsonUtil.toJavaObject(x, LightMessage.class))
                    .filter(Objects::nonNull).filter(x -> x.getSystemTime() >= startTime && x.getSystemTime() <= endTime
                            && x.getSystemTime() > statExtEntity.getCreateTime().getTime()).collect(Collectors.toList());
        }
        if(CollectionUtils.isEmpty(messageList)){
            return RequestCodeEnum.toJSON(RequestCodeEnum.SUCCESS);
        }
        List<String> nameList = Lists.newArrayList();
        nameList.add("detail");
        nameList.add("repeat");
        nameList.add("param_check");
        Set<String> columnNameList = statExtEntity.getRelatedColumnSet();
        nameList.addAll(columnNameList);
        if(StringUtil.isNotEmpty(statExtEntity.getTemplateEntity().getDimens())){
            nameList.add("dimens");
        }
        nameList.add("batch_time");
        nameList.add("system_time");
        List<StatState> stateList = statExtEntity.getTemplateEntity().getStatStateList();
        for (StatState statState : stateList) {
            nameList.add(statState.getStateBody());
        }
        nameList.add("result");
        for (LightMessage lightMessage : messageList) {
            LinkedHashMap<String, String> paramMap = new LinkedHashMap<>();
            long timestamp = lightMessage.getTime();
            long batchTime = BatchAdapter.getBatch(statExtEntity.getTimeParamInterval(), statExtEntity.getTimeUnit(), timestamp);
            paramMap.put("batch_time", DateUtil.formatTimeStamp(batchTime, "yyyy-MM-dd HH:mm:ss"));
            paramMap.put("system_time",DateUtil.formatTimeStamp(lightMessage.getSystemTime(), "yyyy-MM-dd HH:mm:ss"));
            String params = Joiner.on("\n").withKeyValueSeparator("=").join(lightMessage.getParamMap());
            String detail = params + "\n" + "timestamp="+DateUtil.formatTimeStamp(timestamp, "yyyy-MM-dd HH:mm:ss");
            paramMap.put("detail",detail);
            paramMap.put("repeat",String.valueOf(lightMessage.getRepeat()));
            boolean isInValid = !MessageValid.valid(lightMessage,groupExtEntity.getColumnList());
            boolean isNil = false;
            columnNameList.forEach(x -> {
                Object object = lightMessage.getParamMap().get(x);
                paramMap.put(x, String.valueOf(object));
            });
            paramMap.put("param_check", String.valueOf(!isInValid));
            String formula = statExtEntity.getTemplateEntity().getCompleteStat();
            HashMap<String,Object> envMap = new HashMap<>(paramMap);
            if(StringUtil.isNotEmpty(statExtEntity.getTemplateEntity().getDimens())){
                Object dimensValue = AviatorHandler.execute(statExtEntity.getTemplateEntity().getDimens(),envMap);
                paramMap.put("dimens",String.valueOf(dimensValue));
            }
            for (StatState statState : stateList) {
                long stateResult = FormulaCalculate.calculate(statState, envMap, batchTime);
                if (stateResult == ILLEGAL_VAL) {
                    paramMap.put(statState.getStateBody(), "Invalid");
                    isInValid = true;
                } else if (stateResult == NIL_VAL) {
                    paramMap.put(statState.getStateBody(), "Nil");
                    isNil = true;
                } else {
                    BigDecimal value = StatState.isCountState(statState) || StatState.isBitCountState(statState)
                            ? BigDecimal.valueOf(stateResult) : BigDecimal.valueOf(stateResult).divide(BigDecimal.valueOf(1000D),3, RoundingMode.HALF_UP);
                    paramMap.put(statState.getStateBody(), value.toString());
                    formula = formula.replace(statState.getStateBody(), value.toString());
                }
            }
            try {
                Object result;
                if(isInValid){
                    result = "Invalid";
                }else if(isNil){
                    result = "Nil";
                }else{
                    result = AviatorHandler.execute(formula);
                }
                paramMap.put("result", String.valueOf(result));
            } catch (Exception ex) {
                logger.error("track stat process error,statId:{}!",statId,ex);
            }
            dataList.add(paramMap);
        }
        ObjectNode resultNode = JsonUtil.createObjectNode();
        resultNode.put("code","0");
        resultNode.putPOJO("data",dataList);
        resultNode.putPOJO("columns",nameList);
        return resultNode;
    }

}
