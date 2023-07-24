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
import com.google.common.collect.Lists;
import com.dtstep.lighthouse.common.constant.FormulaConst;
import com.dtstep.lighthouse.common.entity.state.StatUnit;
import com.dtstep.lighthouse.common.entity.state.StatState;
import com.dtstep.lighthouse.common.entity.meta.MetaColumn;
import com.dtstep.lighthouse.common.entity.stat.StatVariableEntity;
import com.dtstep.lighthouse.common.enums.formula.CharTypeEnum;
import com.dtstep.lighthouse.common.enums.function.EmbedFunctionEnum;
import com.dtstep.lighthouse.common.enums.function.FunctionEnum;
import com.dtstep.lighthouse.common.enums.variable.EmbedVariableEnum;
import com.dtstep.lighthouse.common.exception.TemplateParseException;
import com.dtstep.lighthouse.common.util.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.parquet.Strings;

import java.io.StringReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public final class FormulaTranslate {

    public static Pair<String,List<StatState>> translate(final String stat) throws Exception {
        String completeState = stat;
        List<StatState> stateList = new ArrayList<>();
        StringReader stringReader = new StringReader(stat);
        int n,index = 0,functionIndex = 0;
        StringBuilder sbr = new StringBuilder();
        CharTypeEnum lastCharType = null;
        while ((n = stringReader.read()) != -1){
            CharTypeEnum charTypeEnum = CharTypeEnum.getCharTypeEnum((char)n);
            if(charTypeEnum == null){
                throw new TemplateParseException("formula parse error,unsupported characters exist,char:["+(char)n+"]");
            }
            if(lastCharType == null){
                sbr.append((char)n);
                index ++;
                lastCharType = charTypeEnum;
            }else if(isAppend(lastCharType,charTypeEnum)){
                sbr.append((char)n);
                index ++;
                lastCharType = charTypeEnum;
            }else{
                String temp = sbr.toString();
                if(charTypeEnum == CharTypeEnum.LBRACKET && FunctionEnum.isStatFunction(temp)){
                    String functionBody = getFunctionBody(stat.substring(index + 1));
                    if(containsStatFunction(functionBody)){
                        throw new TemplateParseException("formula parse error,nested stat function not support.");
                    }
                    if(temp.equals(FunctionEnum.AVG.getFunctionName())){
                        List<StatState> averageStateList = transAverageState(functionBody);
                        for (StatState tempState : averageStateList) {
                            tempState.setFunctionIndex(functionIndex++);
                            stateList.add(tempState);
                        }
                        completeState = stat.replace(String.format("%s(%s)",temp,functionBody),
                                String.format("%s/%s",averageStateList.get(0).getStateBody(),averageStateList.get(1).getStateBody()));
                    }else{
                        StatState statState = new StatState();
                        statState.setFunctionIndex(functionIndex++);
                        statState.setStateName(temp);
                        List<StatUnit> unitList = getFunctionUnit(functionBody);
                        statState.setUnitList(unitList);
                        statState.setStateBody(String.format("%s(%s)",temp,functionBody));
                        stateList.add(statState);
                    }
                    assert functionBody != null;
                    index = index + functionBody.length() + 2;
                    stringReader.skip(functionBody.length() + 1);
                }else{
                    sbr.setLength(0);
                    sbr.append((char)n);
                    index++;
                    lastCharType = charTypeEnum;
                }
            }
        }
        return Pair.of(completeState,stateList);
    }

    private static List<StatState> transAverageState(String formula) throws Exception{
        List<StatState> stateList = new ArrayList<>();
        StatState sumState = new StatState();
        sumState.setStateName("sum");
        List<StatUnit> unitList = getFunctionUnit(formula);
        sumState.setUnitList(unitList);
        sumState.setStateBody(String.format("%s(%s)","sum",formula));
        StatState.valid(sumState);
        stateList.add(sumState);
        StatState countState = new StatState();
        countState.setStateName("count");
        if(unitList.size() > 1){
            countState.setUnitList(unitList.subList(1,unitList.size()));
            List<String> unitOriginList = unitList.subList(1,unitList.size()).stream().map(StatUnit::getOrigin).collect(Collectors.toList());
            String countBody = Strings.join(unitOriginList,",");
            countState.setStateBody(String.format("%s(%s)","count",countBody));
        }else{
            countState.setUnitList(new ArrayList<>());
            countState.setStateBody("count()");
        }
        StatState.valid(countState);
        stateList.add(countState);
        return stateList;
    }

    public static boolean containsStatFunction(final String formula) throws Exception {
        for(FunctionEnum functionEnum : FunctionEnum.values()){
            Matcher m = Pattern.compile("[^0-9_a-zA-Z]*"+ functionEnum.getFunctionName()+"\\(.*\\)").matcher(formula);
            if(m.matches()){
                return true;
            }
        }
        return false;
    }


    private static List<String> getUnitPart(String functionBody) throws Exception {
        String temp = functionBody + '\u0001';
        List<String> unitList = new ArrayList<>();
        StringReader stringReader = new StringReader(temp);
        StringBuilder sbr = new StringBuilder();
        int level = 0;
        int n = 0;
        boolean constFlag = false;
        int index = 0;
        while ((n = stringReader.read()) != -1){
            char c = (char)n;
            CharTypeEnum charTypeEnum = CharTypeEnum.getCharTypeEnum(c);
            if(charTypeEnum == null){
                throw new TemplateParseException("formula parse error,unsupported characters exist,char:["+(char)n+"]");
            }
            if(charTypeEnum == CharTypeEnum.SINGLE_QUOTATION){
                if(constFlag){
                    if(index != temp.length() - 1){
                        char nextChar = temp.charAt(index + 1);
                        if(!isAppend(charTypeEnum, Objects.requireNonNull(CharTypeEnum.getCharTypeEnum(nextChar)))){
                            constFlag = false;
                        }
                    }
                }else{
                    constFlag = true;
                }
                sbr.append(c);
            }else if(constFlag){
                sbr.append(c);
            }else if(charTypeEnum == CharTypeEnum.COMMA){
                if(level == 0){
                    unitList.add(sbr.toString());
                    sbr.setLength(0);
                }else{
                    sbr.append(c);
                }
            }else if(charTypeEnum == CharTypeEnum.LBRACKET){
                level++;
                sbr.append(c);
            }else if(charTypeEnum == CharTypeEnum.RBRACKET){
                level--;
                sbr.append(c);
            }else if(charTypeEnum == CharTypeEnum.END){
                unitList.add(sbr.toString());
                sbr.setLength(0);
            }else{
                sbr.append(c);
            }
            index ++;
        }
        return unitList;
    }


    private static List<StatUnit> getFunctionUnit(final String functionBody) throws Exception{
        List<StatUnit> unitList = Lists.newArrayListWithExpectedSize(2);
        if(StringUtil.isEmpty(functionBody)){
            return unitList;
        }
        List<String> partList = getUnitPart(functionBody);
        for (String temp : partList) {
            StatUnit statUnit = new StatUnit();
            statUnit.setOrigin(temp);
            unitList.add(statUnit);
        }
        return unitList;
    }


    private static boolean isAppend(CharTypeEnum charTypeEnum1,CharTypeEnum charTypeEnum2){
        return charTypeEnum1.getLevel() == charTypeEnum2.getLevel();
    }

    private static String getFunctionBody(final String rest){
        StringBuilder sbr = new StringBuilder();
        int level = 0;
        for(int i=0;i<rest.length();i++){
            char c = rest.charAt(i);
            if(c == 40){
                level ++;
                sbr.append(c);
            }else if(c == 41){
                level --;
                if(level == -1){
                    return sbr.toString();
                }else{
                    sbr.append(c);
                }
            }else{
                sbr.append(c);
            }
        }
        return null;
    }

    public static List<MetaColumn> queryRelatedColumns(List<MetaColumn> columnList, String formula) throws Exception {
        List<MetaColumn> resultColumnList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(columnList)){
            for (MetaColumn metaColumn : columnList) {
                String columnName = metaColumn.getColumnName();
                if (columnName.equals(formula)) {
                    resultColumnList.add(metaColumn);
                } else {
                    Matcher m = Pattern.compile("(.*[^0-9_a-zA-Z]+|^)" + columnName + "([^0-9_a-zA-Z]+.*|$)").matcher(formula);
                    if (m.matches()) {
                        resultColumnList.add(metaColumn);
                    }
                }
            }
        }
        return resultColumnList;
    }

    private static final Pattern pattern_1 = Pattern.compile("'(.*?)'");

    //private static final Pattern pattern_2 = Pattern.compile("(\\$\\{)(.+?)(\\})");

    private static final Pattern pattern_2 = Pattern.compile("\\$[a-z_0-9A-Z]+");

    private static final Pattern pattern_3 = Pattern.compile("[a-z_0-9A-Z]+");


    public static void checkVariableExist(String strInfo,List<MetaColumn> columnList,List<StatVariableEntity> variableEntityList) throws Exception {
        StringBuffer sbr1 = new StringBuffer();
        Matcher m1 = pattern_1.matcher(strInfo);
        while (m1.find()){
            m1.appendReplacement(sbr1,FormulaConst.IDENTIFY_CONST);
        }
        m1.appendTail(sbr1);
        String removeConst = sbr1.toString();
        StringBuffer sbr2 = new StringBuffer();
        Matcher m2 = pattern_2.matcher(removeConst);
        while (m2.find()){
            if(EmbedVariableEnum.isVariableExist(m2.group())){
                m2.appendReplacement(sbr2,FormulaConst.IDENTIFY_EMBED);
            }else{
                throw new TemplateParseException(String.format("i18n(ldp_i18n_template_parse_1016,%s)",m2.group()));
            }
        }
        m2.appendTail(sbr2);
        String removeEmbed = sbr2.toString();
        Matcher m3 = pattern_3.matcher(removeEmbed);
        List<String> columnNameList = columnList.stream().map(MetaColumn::getColumnName).collect(Collectors.toList());
        List<String> variableNameList = variableEntityList.stream().map(StatVariableEntity::getVariableName).collect(Collectors.toList());
        while (m3.find()){
            String group = m3.group();
            if(StringUtil.isNumber(group)){
                continue;
            }
            if(FunctionEnum.isStatFunction(group) || EmbedFunctionEnum.isEmbedFunction(group)){
                continue;
            }
            if(columnNameList.contains(group) || variableNameList.contains(group)){
                continue;
            }
            if(group.startsWith(FormulaConst.IDENTIFY_CONST)){
                continue;
            }
            if(group.startsWith(FormulaConst.IDENTIFY_EMBED)){
                continue;
            }
            throw new TemplateParseException(String.format("i18n(ldp_i18n_template_parse_1016,%s)",group));
        }
    }

}
