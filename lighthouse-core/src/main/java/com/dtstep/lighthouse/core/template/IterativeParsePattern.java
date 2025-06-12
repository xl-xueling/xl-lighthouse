package com.dtstep.lighthouse.core.template;
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
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.ServiceResult;
import com.dtstep.lighthouse.common.entity.stat.TemplateEntity;
import com.dtstep.lighthouse.common.entity.state.StatState;
import com.dtstep.lighthouse.common.enums.function.FunctionEnum;
import com.dtstep.lighthouse.common.enums.LimitTypeEnum;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.common.entity.ResultCode;
import com.dtstep.lighthouse.core.formula.FormulaTranslate;
import com.dtstep.lighthouse.core.formula.TemplateUtil;
import com.dtstep.lighthouse.common.modal.Column;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class IterativeParsePattern implements Parser {

    @Override
    public ServiceResult<TemplateEntity> parseConfig(TemplateContext context) {
        TemplateEntity templateEntity = new TemplateEntity();
        String template = context.getTemplate();
        Document document = Jsoup.parse(template,"", org.jsoup.parser.Parser.xmlParser());
        Elements elements = document.select("stat-item");
        if(elements == null || elements.size() == 0){
            return ServiceResult.result(ResultCode.templateParserNoValidItem);
        }
        if(elements.size() > 1){
            return ServiceResult.result(ResultCode.templateParserValidFailed);
        }
        Element element = elements.get(0);
        Attributes attributes = element.attributes();
        List<String> fieldList = TemplateEntity.getTemplateAttrs();
        for (Attribute attribute : attributes) {
            if (!fieldList.contains(attribute.getKey())) {
                return ServiceResult.result(ResultCode.getExtendResultCode(ResultCode.templateParserInValidAttrExist,attribute.getKey()));
            }else if(StringUtil.isEmpty(attribute.getValue())){
                return ServiceResult.result(ResultCode.getExtendResultCode(ResultCode.templateParserAttrCannotBeEmpty,attribute.getKey()));
            }
        }
        String title = element.attr("title");
        if(StringUtil.isEmpty(title)){
            return ServiceResult.result(ResultCode.templateParserTitleCannotBeEmpty);
        }
        if(StringUtil.getBLen(title.trim()) < 5){
            return ServiceResult.result(ResultCode.templateParserTitleLengthValidFailed);
        }
        if(StringUtil.getBLen(title.trim()) >= 40){
            return ServiceResult.result(ResultCode.templateParserTitleLengthValidFailed);
        }
        templateEntity.setTitle(title);
        String stat = element.attr("stat");
        if(StringUtil.isEmpty(stat)){
            return ServiceResult.result(ResultCode.templateParserStatCannotBeEmpty);
        }
        templateEntity.setStat(stat);
        List<Column> columnList = context.getColumnList();
        List<String> groupColumnList = columnList.stream().map(Column::getName).collect(Collectors.toList());
        List<String> statFormulaColumnsList = extractColumnsList(stat);
        statFormulaColumnsList.removeAll(groupColumnList);
        if(statFormulaColumnsList.size() > 0){
            return ServiceResult.result(ResultCode.getExtendResultCode(ResultCode.templateParserColumnNotExist,String.join(",", statFormulaColumnsList)));
        }
        String dimensFormula = element.attr("dimens");
        if(!StringUtil.isEmpty(dimensFormula)){
            String[] dimensArray = TemplateUtil.split(dimensFormula);
            for(String dimens:dimensArray){
                if(StringUtil.isLetterNumOrUnderLine(dimens) && !groupColumnList.contains(dimens)){
                    return ServiceResult.result(ResultCode.getExtendResultCode(ResultCode.templateParserDimensNotExist,dimens));
                }
                boolean checkFlag = ImitateCompile.imitateDimensFormula(context.getStatId(),dimens,columnList);
                if(!checkFlag){
                    return ServiceResult.result(ResultCode.getExtendResultCode(ResultCode.templateParserDimensValidFailed,dimens));
                }
            }
            if(dimensArray.length > 5){
                return ServiceResult.result(ResultCode.getExtendResultCode(ResultCode.dimensColumnsExceedLimit,String.format("%s > %s",dimensArray.length,5)));
            }
            templateEntity.setDimens(dimensFormula);
            templateEntity.setDimensArray(dimensArray);
            List<String> dimensFormulaColumnsList = extractColumnsList(dimensFormula);
            dimensFormulaColumnsList.removeAll(groupColumnList);
            if(dimensFormulaColumnsList.size() > 0){
                return ServiceResult.result(ResultCode.getExtendResultCode(ResultCode.templateParserColumnNotExist,String.join(",", dimensFormulaColumnsList)));
            }
        }
        String limit = element.attr("limit");
        if(!StringUtil.isEmpty(limit)){
            String sizeStr;
            if(limit.startsWith("top")){
                sizeStr = limit.substring(3);
                templateEntity.setLimitTypeEnum(LimitTypeEnum.TOP);
            }else if(limit.startsWith("last")){
                sizeStr = limit.substring(4);
                templateEntity.setLimitTypeEnum(LimitTypeEnum.LAST);
            }else{
                return ServiceResult.result(ResultCode.getExtendResultCode(ResultCode.templateParserLimitValidFailed,limit));
            }
            if(StringUtil.isEmpty(sizeStr) || !StringUtil.isInt(sizeStr) || Integer.parseInt(sizeStr) < 0){
                return ServiceResult.result(ResultCode.getExtendResultCode(ResultCode.templateParserLimitValidFailed,limit));
            }
            if(Integer.parseInt(sizeStr) > StatConst.LIMIT_MAX_SIZE){
                return ServiceResult.result(ResultCode.getExtendResultCode(ResultCode.templateParserLimitValueExceed,sizeStr));
            }
            String timeParam = context.getTimeParam();
            if(!StringUtil.isEmpty(timeParam) && timeParam.endsWith("minute")){
                int duration = Integer.parseInt(timeParam.split("-")[0]);
                if(duration < 5){
                    return ServiceResult.result(ResultCode.templateParserLimitMinuteNotSupport);
                }
            }

            if(StringUtil.isEmpty(dimensFormula)){
                return ServiceResult.result(ResultCode.templateParserLimitDimensExistTogether);
            }
            templateEntity.setLimit(limit);
            templateEntity.setLimitSize(Integer.parseInt(sizeStr));
        }

        Pair<String,List<StatState>> statePair;
        try{
            statePair = FormulaTranslate.translate(stat);
        }catch (Exception ex){
            return ServiceResult.result(ResultCode.getExtendResultCode(ResultCode.templateParserStatValidFailed,stat));
        }

        if(CollectionUtils.isEmpty(statePair.getRight())){
            return ServiceResult.result(ResultCode.getExtendResultCode(ResultCode.templateParserStatValidFailed,stat));
        }
        String completeStat = statePair.getLeft();
        boolean checkFlag = ImitateCompile.imitateStatFormula(context.getStatId(),completeStat,columnList);
        if(!checkFlag){
            return ServiceResult.result(ResultCode.getExtendResultCode(ResultCode.templateParserStatValidFailed,stat));
        }
        templateEntity.setCompleteStat(completeStat);
        List<StatState> stateList = statePair.getRight();
        for(StatState statState : stateList){
            if(!StatState.isCountState(statState) && CollectionUtils.isEmpty(statState.getUnitList())){
                return ServiceResult.result(ResultCode.getExtendResultCode(ResultCode.templateParserStatValidFailed,stat));
            }
        }

        if(stateList.size() > 3){
            return ServiceResult.result(ResultCode.templateParserStateExceedLimit);
        }

        boolean isSequence = stateList.stream().anyMatch(x -> x.getFunctionEnum() == FunctionEnum.SEQ);
        if(isSequence && stateList.size() > 1){
            return ServiceResult.result(ResultCode.templateParserSeqTogether);
        }
        templateEntity.setStatStateList(stateList);
        return ServiceResult.result(ResultCode.success,templateEntity);
    }

    private static final Pattern columnExtractPattern = Pattern.compile("(?![^']*'[^']*(?:'[^']*')*$)\\b(?=\\w*[a-zA-Z])\\w+\\b(?!\\s*\\()");

    public static List<String> extractColumnsList(String input) {
        String replaceInput = replaceQuotedContent(input);
        List<String> validMatches = new ArrayList<>();
        Matcher matcher = columnExtractPattern.matcher(replaceInput);
        while (matcher.find()) {
            String match = matcher.group();
            if (isValidMatch(match)) {
                validMatches.add(match);
            }
        }
        return validMatches;
    }

    public static String replaceQuotedContent(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        StringBuilder result = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '\'') {
                if (!inQuotes) {
                    inQuotes = true;
                    result.append('-');
                } else {
                    inQuotes = false;
                }
            } else {
                if (inQuotes) {
                    result.append('-');
                } else {
                    result.append(c);
                }
            }
        }
        return result.toString();
    }

    private static boolean isValidMatch(String match) {
        return match.matches(".*[a-zA-Z].*");
    }
}
