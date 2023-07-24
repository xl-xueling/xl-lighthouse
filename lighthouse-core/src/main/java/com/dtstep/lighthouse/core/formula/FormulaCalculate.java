package com.dtstep.lighthouse.core.formula;
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
import com.dtstep.lighthouse.core.expression.embed.AviatorHandler;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.state.StatUnit;
import com.dtstep.lighthouse.common.entity.state.StatState;
import com.dtstep.lighthouse.common.enums.variable.EmbedVariableEnum;
import com.dtstep.lighthouse.common.exception.TemplateParseException;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dtstep.lighthouse.common.constant.StatConst.ILLEGAL_VAL;
import static com.dtstep.lighthouse.common.constant.StatConst.NIL_VAL;


public final class FormulaCalculate {

    private static final Logger logger = LoggerFactory.getLogger(FormulaCalculate.class);

    public static long calculate(StatState statState,Map<String,Object> envMap,long batchTime){
        long res;
        try{
            res = doCalculate(statState,envMap,batchTime);
        }catch (Exception ex){
            logger.error("formula calculate error,state:{}", JsonUtil.toJSONString(statState),ex);
            res = ILLEGAL_VAL;
        }
        return res;
    }

    private static Long doCalculate(StatState statState, Map<String,Object> envMap, long batchTime) throws Exception{
        List<StatUnit> list = statState.getUnitList();
        double radix;
        int filterStartIndex = StatState.getFilterStartIndex(statState);
        if(StatState.isCountState(statState) || StatState.isBitCountState(statState)){
            radix = 1d;
        }else{
            String origin = StatState.getFirstUnit(statState);
            combineVariableEnvMap(origin,envMap,batchTime);
            String radixStr = String.valueOf(AviatorHandler.execute(origin,envMap));
            if(!StringUtil.isNumber(radixStr)){
                return ILLEGAL_VAL;
            }
            radix = Double.parseDouble(radixStr);
            if(radix > StatConst.STAT_MAXIMUM){
                return StatConst.ILLEGAL_VAL;
            }
        }
        if(list.size() > filterStartIndex){
            for(int i = filterStartIndex;i < list.size();i++){
                StatUnit unit = list.get(i);
                String origin = unit.getOrigin();
                combineVariableEnvMap(origin,envMap,batchTime);
                if(!AviatorHandler.check(origin,envMap)){
                    return NIL_VAL;
                }
            }
        }
        if(radix % 1 == 0){
            return StatState.isCountState(statState) || StatState.isBitCountState(statState) ? (long)radix:(long)(radix * 1000);
        }else{
            return StatState.isCountState(statState) || StatState.isBitCountState(statState) ? (long)radix:BigDecimal.valueOf(radix).multiply(BigDecimal.valueOf(1000)).longValue();
        }
    }

    public static boolean valid(StatState statState, Map<String,Object> paramMap, long batchTime) throws Exception{
        List<StatUnit> list = statState.getUnitList();
        double radix;
        int filterStartIndex = StatState.getFilterStartIndex(statState);
        Map<String, Object> envMap = null;
        if(!StatState.isCountState(statState) && !StatState.isBitCountState(statState)){
            envMap = new HashMap<>(paramMap);
            String origin = list.get(0).getOrigin();
            combineVariableEnvMap(origin,envMap,batchTime);
            String radixStr = String.valueOf(AviatorHandler.execute(origin,envMap));
            if(!StringUtil.isNumber(radixStr)){
                return false;
            }
            radix = Double.parseDouble(radixStr);
            if(radix > StatConst.STAT_MAXIMUM){
                return false;
            }
        }
        if(list.size() > filterStartIndex){
            if(envMap == null){
                envMap = new HashMap<>(paramMap);
            }
            for(int i = filterStartIndex;i < list.size();i++){
                StatUnit unit = list.get(i);
                String origin = unit.getOrigin();
                combineVariableEnvMap(origin,envMap,batchTime);
                if(!AviatorHandler.isBoolFormula(origin,envMap)){
                    return false;
                }
            }
        }
        return true;
    }

    private static final Pattern pattern = Pattern.compile("\\$[a-z_0-9A-Z-: ]+");

    public static void combineVariableEnvMap(final String formula,Map<String,Object> envMap,long batchTime) throws Exception{
        if(StringUtil.isEmpty(formula)){
            return;
        }
        if(formula.contains("$")){
            Matcher m = pattern.matcher(formula);
            while (m.find()){
                String group = m.group();
                String variable = null;
                if(group.startsWith("$")){
                    variable = group.substring(1);
                }
                if(StringUtil.isEmpty(variable)){
                    continue;
                }
                int index = variable.indexOf("_");
                String format = "yyyy-MM-dd HH:mm:ss";
                String variableName;
                if(index == -1){
                    variableName = variable;
                }else{
                    variableName = variable.substring(0,index);
                    format = variable.substring(index + 1);
                }
                if(EmbedVariableEnum.isVariableExist(variableName)){
                    Object value = VariableEvaluate.evaluate(variableName,format, batchTime);
                    envMap.put(group,value);
                }else{
                    throw new TemplateParseException("formula parse error,variable["+m.group()+"] not exist.");
                }
            }
        }
    }


    public static Object parseVariableEntity(String customVariable,Map<String,Object> envMap,long batchTime) throws Exception {
        if(StringUtil.isEmpty(customVariable) || MapUtils.isEmpty(envMap)){
            return null;
        }else if(envMap.containsKey(customVariable)){
            return envMap.get(customVariable);
        }else{
            combineVariableEnvMap(customVariable,envMap,batchTime);
            return AviatorHandler.execute(customVariable,envMap);
        }
    }
}
