package com.dtstep.lighthouse.core.template;
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
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.meta.MetaColumn;
import com.dtstep.lighthouse.common.entity.stat.StatVariableEntity;
import com.dtstep.lighthouse.common.entity.stat.TemplateEntity;
import com.dtstep.lighthouse.common.entity.state.StatState;
import com.dtstep.lighthouse.common.enums.function.FunctionEnum;
import com.dtstep.lighthouse.common.enums.stat.LimitTypeEnum;
import com.dtstep.lighthouse.common.exception.TemplateParseException;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.formula.FormulaTranslate;
import com.dtstep.lighthouse.core.formula.ImitateCompile;
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

public final class IterativeParsePattern implements Parser {

    @Override
    public TemplateEntity parse(TemplateContext context) throws Exception {
        TemplateEntity templateEntity = new TemplateEntity();
        String template = context.getTemplate();
        Document document = Jsoup.parse(template,"", org.jsoup.parser.Parser.xmlParser());
        Elements elements = document.select("stat-item");
        if(elements == null || elements.size() == 0){
            throw new TemplateParseException("i18n(ldp_i18n_template_parse_1001)");
        }
        if(elements.size() > 1){
            throw new TemplateParseException("i18n(ldp_i18n_template_parse_1020)");
        }
        Element element = elements.get(0);
        Attributes attributes = element.attributes();
        List<String> fieldList = TemplateEntity.getStatAttr();
        for (Attribute attribute : attributes) {
            if (!fieldList.contains(attribute.getKey())) {
                throw new TemplateParseException(String.format("i18n(ldp_i18n_template_parse_1002,%s)", attribute.getKey()));
            }else if(StringUtil.isEmpty(attribute.getValue())){
                throw new TemplateParseException(String.format("i18n(ldp_i18n_template_parse_1003,%s)", attribute.getKey()));
            }
        }
        String title = element.attr("title");
        if(StringUtil.isEmpty(title)){
            throw new TemplateParseException(String.format("i18n(ldp_i18n_template_parse_1003,%s)", "title"));
        }
        if(StringUtil.getBLen(title.trim()) < 5){
            throw new TemplateParseException(String.format("i18n(ldp_i18n_template_parse_1018,%s,%s,%s)",title,title.length(),5));
        }
        if(StringUtil.getBLen(title.trim()) >= 35){
            throw new TemplateParseException(String.format("i18n(ldp_i18n_template_parse_1004,%s)",title));
        }
        templateEntity.setTitle(title);
        String stat = element.attr("stat");
        if(StringUtil.isEmpty(stat)){
            throw new TemplateParseException("i18n(ldp_i18n_template_parse_1005)");
        }
        templateEntity.setStat(stat);
        List<Element> variableElements = element.getElementsByTag("variables");
        List<StatVariableEntity> variableEntityList = new ArrayList<>();
        templateEntity.setVariableEntityList(variableEntityList);
        if(variableElements != null && variableElements.size() == 1){
            List<Element> childVariables = variableElements.get(0).children();
            for (Element variableElement : childVariables) {
                String name = variableElement.attr("name");
                if (StringUtil.isEmpty(name)) {
                    throw new TemplateParseException("i18n(ldp_i18n_template_parse_1006)");
                }
                StatVariableEntity statVariableEntity = new StatVariableEntity();
                statVariableEntity.setVariableName(name);
                String value = variableElement.attr("value");
                if (StringUtil.isEmpty(value)) {
                    value = variableElement.text();
                    if (StringUtil.isEmpty(value)) {
                        throw new TemplateParseException("i18n(ldp_i18n_template_parse_1007)");
                    }
                }
                statVariableEntity.setValue(value);
                variableEntityList.add(statVariableEntity);
            }
            templateEntity.setVariableEntityList(variableEntityList);
        }
        List<MetaColumn> columnList = context.getColumnList();
        String dimensFormula = element.attr("dimens");
        if(!StringUtil.isEmpty(dimensFormula)){
            String[] dimensArray = dimensFormula.split(StatConst.DIMENS_SEPARATOR);
            FormulaTranslate.checkVariableExist(dimensFormula,columnList,variableEntityList);
            for(String dimens:dimensArray){
                boolean checkFlag = ImitateCompile.imitateDimensFormula(context.getStatId(),dimens,columnList);
                if(!checkFlag){
                    throw new TemplateParseException(String.format("i18n<<ldp_i18n_template_parse_1019#%s>>",dimensFormula));
                }
            }
            templateEntity.setDimens(dimensFormula);
            templateEntity.setDimensArr(dimensArray);
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
                throw new TemplateParseException(String.format("i18n(ldp_i18n_template_parse_1009,%s)",limit));
            }
            if(StringUtil.isEmpty(sizeStr) || !StringUtil.isInt(sizeStr) || Integer.parseInt(sizeStr) < 0){
                throw new TemplateParseException(String.format("i18n(ldp_i18n_template_parse_1009,%s)",limit));
            }
            if(Integer.parseInt(sizeStr) > StatConst.LIMIT_MAX_SIZE){
                throw new TemplateParseException(String.format("i18n(ldp_i18n_template_parse_1010,%s,%s)",sizeStr,StatConst.LIMIT_MAX_SIZE));
            }
            String timeParam = context.getTimeParam();
            if(!StringUtil.isEmpty(timeParam) && timeParam.endsWith("minute")){
                int duration = Integer.parseInt(timeParam.split("-")[0]);
                if(duration < 5){
                    throw new TemplateParseException("i18n(ldp_i18n_template_parse_1011)");
                }
            }

            if(StringUtil.isEmpty(dimensFormula)){
                throw new TemplateParseException("i18n(ldp_i18n_template_parse_1012)");
            }
            templateEntity.setLimit(limit);
            templateEntity.setLimitSize(Integer.parseInt(sizeStr));
        }

        FormulaTranslate.checkVariableExist(stat,columnList,variableEntityList);
        Pair<String,List<StatState>> statePair;
        try{
            statePair = FormulaTranslate.translate(stat);
        }catch (Exception ex){
            throw new TemplateParseException(String.format("i18n<<ldp_i18n_template_parse_1013#%s>>",stat));
        }

        if(CollectionUtils.isEmpty(statePair.getRight())){
            throw new TemplateParseException(String.format("i18n<<ldp_i18n_template_parse_1013#%s>>",stat));
        }
        String completeStat = statePair.getLeft();
        boolean checkFlag = ImitateCompile.imitateStatFormula(context.getStatId(),completeStat,columnList);
        if(!checkFlag){
            throw new TemplateParseException(String.format("i18n<<ldp_i18n_template_parse_1013#%s>>",stat));
        }
        templateEntity.setCompleteStat(completeStat);
        List<StatState> stateList = statePair.getRight();
        for(StatState statState : stateList){
            if(!StatState.isCountState(statState) && CollectionUtils.isEmpty(statState.getUnitList())){
                throw new TemplateParseException(String.format("i18n<<ldp_i18n_template_parse_1013#%s>>",stat));
            }
        }

        if(stateList.size() > 3){
            throw new TemplateParseException(String.format("i18n<<ldp_i18n_template_parse_1017#%s#%s>>",stateList.size(),3));
        }

        boolean isSequence = stateList.stream().anyMatch(x -> x.getFunctionEnum() == FunctionEnum.SEQ);
        if(isSequence && stateList.size() > 1){
            throw new TemplateParseException(String.format("i18n<<ldp_i18n_template_parse_1014#%s>>",stat));
        }
        if(isSequence && !StringUtil.isEmpty(limit)){
            throw new TemplateParseException(String.format("i18n<<ldp_i18n_template_parse_1015#%s>>",stat));
        }
        templateEntity.setStatStateList(stateList);
        return templateEntity;
    }
}
