package com.dtstep.lighthouse.insights.template;
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

import com.dtstep.lighthouse.common.entity.state.StatState;
import com.dtstep.lighthouse.core.expression.embed.AviatorHandler;
import com.dtstep.lighthouse.core.formula.FormulaCalculate;
import com.dtstep.lighthouse.core.formula.FormulaTranslate;
import com.dtstep.lighthouse.insights.enums.ColumnTypeEnum;
import com.dtstep.lighthouse.insights.modal.Column;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public final class ImitateCompile {

    private static final Logger logger = LoggerFactory.getLogger(ImitateCompile.class);

    public static boolean imitateStatFormula(int statId, final String formula, List<Column> columnList){
        try{
            String tempFormula = formula;
            Pair<String, List<StatState>> pair = FormulaTranslate.translate(formula);
            Map<String,Object> paramMap = new HashMap<>();
            if(columnList != null){
                for (Column metaColumn : columnList) {
                    if (metaColumn.getType() == ColumnTypeEnum.STRING) {
                        paramMap.put(metaColumn.getName(), UUID.randomUUID().toString());
                    } else if (metaColumn.getType() == ColumnTypeEnum.NUMBER) {
                        paramMap.put(metaColumn.getName(), new Random().nextInt(10000));
                    }
                }
            }
            List<StatState> stateList = pair.getValue();
            for (StatState statState : stateList) {
                boolean valid = FormulaCalculate.valid(statState, paramMap, System.currentTimeMillis());
                if (!valid) {
                    logger.error("xl-formula stat format check failed,statId:[{}],formula:[{}]!",statId,formula);
                    return false;
                }
                tempFormula = tempFormula.replace(statState.getStateBody(), String.valueOf(1.0D));
            }
            AviatorHandler.compileStatFormula(tempFormula);
        }catch (Exception ex){
            logger.error("xl-formula stat format check failed,statId:[{}],formula:[{}]!",statId,formula);
            return false;
        }
        return true;
    }


    public static boolean imitateDimensFormula(int statId, final String formula, List<Column> columnList){
        try{
            Map<String,Object> paramMap = new HashMap<>();
            if(columnList != null){
                for (Column metaColumn : columnList) {
                    if (metaColumn.getType() == ColumnTypeEnum.STRING) {
                        paramMap.put(metaColumn.getName(), UUID.randomUUID().toString());
                    } else if (metaColumn.getType() == ColumnTypeEnum.NUMBER) {
                        paramMap.put(metaColumn.getName(), new Random().nextInt(10000));
                    }
                }
            }
            AviatorHandler.compileDimensFormula(formula,paramMap);
        }catch (Exception ex){
            logger.error("xl-formula dimens format check failed,statId:[{}],formula:[{}]!",statId,formula);
            return false;
        }
        return true;
    }
}
