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
import com.dtstep.lighthouse.common.constant.RedisConst;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.ResultCode;
import com.dtstep.lighthouse.common.entity.ServiceResult;
import com.dtstep.lighthouse.common.entity.message.LightMessage;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.stat.TemplateEntity;
import com.dtstep.lighthouse.common.entity.state.StatState;
import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.common.enums.SwitchStateEnum;
import com.dtstep.lighthouse.common.modal.DebugParam;
import com.dtstep.lighthouse.common.modal.Group;
import com.dtstep.lighthouse.common.modal.IDParam;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.expression.embed.AviatorHandler;
import com.dtstep.lighthouse.core.formula.FormulaCalculate;
import com.dtstep.lighthouse.core.message.MessageValid;
import com.dtstep.lighthouse.core.redis.RedisHandler;
import com.dtstep.lighthouse.insights.controller.annotation.AuthPermission;
import com.dtstep.lighthouse.insights.dto.TrackParam;
import com.dtstep.lighthouse.insights.service.GroupService;
import com.dtstep.lighthouse.insights.service.StatService;
import com.dtstep.lighthouse.insights.vo.GroupVO;
import com.dtstep.lighthouse.insights.vo.ResultData;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@ControllerAdvice
public class TrackController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private StatService statService;

    @AuthPermission(roleTypeEnum = RoleTypeEnum.GROUP_MANAGE_PERMISSION,relationParam = "id")
    @PostMapping("/track/enableDebugMode")
    public ResultData<DebugParam> enableDebugMode(@Validated @RequestBody IDParam idParam) throws Exception {
        Integer id = idParam.getId();
        GroupVO groupVO = groupService.queryById(id);
        DebugParam debugPram;
        if(groupVO.getDebugMode() == SwitchStateEnum.OPEN
                && (groupVO.getDebugParam().getEndTime() - System.currentTimeMillis() > TimeUnit.MINUTES.toMillis(10))){
            debugPram = groupVO.getDebugParam();
            return ResultData.result(ServiceResult.result(ResultCode.debugModeSwitchAlreadyTurnON,debugPram));
        }else{
            debugPram = new DebugParam();
            long now = System.currentTimeMillis();
            debugPram.setStartTime(now);
            debugPram.setEndTime(DateUtil.getMinuteAfter(now, StatConst.DEBUG_MODEL_EXPIRE_MINUTES));
            groupVO.setDebugParam(debugPram);
            groupVO.setDebugMode(SwitchStateEnum.OPEN);
            groupVO.setRefreshTime(DateUtil.timestampToLocalDateTime(now));
            groupService.update(groupVO);
            return ResultData.success(debugPram);
        }
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.GROUP_MANAGE_PERMISSION,relationParam = "id")
    @PostMapping("/track/disableDebugMode")
    public ResultData<Integer> disableDebugMode(@Validated @RequestBody IDParam idParam) throws Exception {
        Integer id = idParam.getId();
        GroupVO groupVO = groupService.queryById(id);
        groupVO.setDebugMode(SwitchStateEnum.CLOSE);
        groupVO.setRefreshTime(LocalDateTime.now());
        groupService.update(groupVO);
        return ResultData.success();
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.GROUP_MANAGE_PERMISSION,relationParam = "groupId")
    @PostMapping("/track/messages")
    public ResultData<List<LinkedHashMap<String,Object>>> messages(@Validated @RequestBody TrackParam trackParam) throws Exception {
        Integer groupId = trackParam.getGroupId();
        Integer statId = trackParam.getStatId();
        GroupVO groupVO = groupService.queryById(groupId);
        Validate.notNull(groupVO);
        if(groupVO.getDebugMode() == SwitchStateEnum.CLOSE){
            return ResultData.result(ResultCode.trackDebugModeExpired);
        }
        DebugParam debugParam = groupVO.getDebugParam();
        String key = RedisConst.TRACK_PREFIX + "_" + groupId;
        List<String> list = RedisHandler.getInstance().lrange(key,0,StatConst.GROUP_MESSAGE_MAX_CACHE_SIZE);
        if(list == null){
            list = new ArrayList<>();
        }
        List<LinkedHashMap<String,Object>> resultList = new ArrayList<>();
        StatExtEntity statExtEntity = statService.queryById(statId);
        Validate.notNull(statExtEntity);
        TemplateEntity templateEntity = statExtEntity.getTemplateEntity();
        Group group = groupService.queryById(statExtEntity.getGroupId());
        for(int i=0;i<list.size();i++){
            LinkedHashMap<String,Object> linkedHashMap = new LinkedHashMap<>();
            LightMessage message = JsonUtil.toJavaObject(list.get(i),LightMessage.class);
            assert message != null;
            long messageTime = message.getTime();
            if(messageTime > debugParam.getEndTime() || messageTime < debugParam.getStartTime()){
                continue;
            }
            linkedHashMap.put("No",i + 1);
            linkedHashMap.put("messageTime",messageTime);
            linkedHashMap.put("processTime",message.getSystemTime());
            long batchTime = DateUtil.batchTime(statExtEntity.getTimeParamInterval(),statExtEntity.getTimeUnit(),messageTime);
            linkedHashMap.put("batchTime",batchTime);
            Map<String,String> paramMap = message.getParamMap();
            Set<String> relatedColumns = statExtEntity.getRelatedColumnSet();
            for(String dimens : relatedColumns){
                linkedHashMap.put(dimens,paramMap.get(dimens));
            }
            linkedHashMap.put("repeat",message.getRepeat());
            String params = paramMap.entrySet().stream().map(z -> z.getKey() + " = " + z.getValue()).collect(Collectors.joining(";"));
            linkedHashMap.put("params",params);
            boolean valid = MessageValid.valid(message,group.getColumns());
            linkedHashMap.put("valid",String.valueOf(valid));
            if(valid){
                List<StatState> statStates = templateEntity.getStatStateList();
                Map<String,Object> envMap = new HashMap<>(message.getParamMap());
                String formula = templateEntity.getCompleteStat();
                boolean isNil = false;
                for(StatState statState : statStates){
                    long result = FormulaCalculate.calculate(statState,envMap,batchTime);
                    if(result == StatConst.ILLEGAL_VAL || result == StatConst.NIL_VAL){
                        isNil = true;
                        linkedHashMap.put(statState.getStateBody(),"Nil");
                    }else{
                        BigDecimal stateValue = BigDecimal.valueOf(result).divide(BigDecimal.valueOf(1000D),3, RoundingMode.HALF_UP).stripTrailingZeros();
                        linkedHashMap.put(statState.getStateBody(),stateValue.toPlainString());
                        formula = formula.replace(statState.getStateBody(),stateValue.toPlainString());
                    }
                }
                if(isNil){
                    linkedHashMap.put("result","Nil");
                }else{
                    Object statValue = AviatorHandler.execute(formula);
                    if (statValue != null) {
                        if(statValue.getClass() == BigDecimal.class){
                            BigDecimal bigDecimal = ((BigDecimal) statValue).stripTrailingZeros();
                            linkedHashMap.put("result",bigDecimal.toPlainString());
                        }else {
                            linkedHashMap.put("result",new BigDecimal(statValue.toString()).setScale(3,RoundingMode.HALF_UP).stripTrailingZeros().toPlainString());
                        }
                    }
                }
            }else{
                linkedHashMap.put("result","Invalid");
            }
            resultList.add(linkedHashMap);
        }
        return ResultData.success(resultList);
    }

}
