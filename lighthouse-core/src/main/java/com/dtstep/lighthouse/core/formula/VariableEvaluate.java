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
import com.dtstep.lighthouse.common.enums.variable.EmbedVariableEnum;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.StringUtil;

import java.util.Date;


public final class VariableEvaluate {

    public static Object evaluate(String variableName, String format,long batchTime) {
        if(StringUtil.isEmpty(variableName)){
            return null;
        }
        Object value;
        long time = 0L;
        if(variableName.equals(EmbedVariableEnum.batchDate.getVariableName())){
            time = batchTime;
        }else if(variableName.equals(EmbedVariableEnum.hour.getVariableName())){
            time = DateUtil.getHourStartTime(batchTime);
        }else if(variableName.equals(EmbedVariableEnum.lastHour.getVariableName())){
            time = DateUtil.getHourBefore(batchTime, 1);
        }else if(variableName.equals(EmbedVariableEnum.twoHoursBefore.getVariableName())){
            time = DateUtil.getHourBefore(batchTime, 2);
        }else if(variableName.equals(EmbedVariableEnum.threeHoursBefore.getVariableName())){
            time = DateUtil.getHourBefore(batchTime, 3);
        }else if(variableName.equals(EmbedVariableEnum.sixHoursBefore.getVariableName())){
            time = DateUtil.getHourBefore(batchTime, 6);
        }else if(variableName.equals(EmbedVariableEnum.twelveHoursBefore.getVariableName())){
            time = DateUtil.getHourBefore(batchTime, 12);
        }else if(variableName.equals(EmbedVariableEnum.today.getVariableName())){
            time = DateUtil.getDayStartTime(batchTime);
        }else if(variableName.equals(EmbedVariableEnum.yesterday.getVariableName())){
            time = DateUtil.getDayBefore(batchTime,1);
        }else if(variableName.equals(EmbedVariableEnum.twoDaysBefore.getVariableName())){
            time = DateUtil.getDayBefore(batchTime,2);
        }else if(variableName.equals(EmbedVariableEnum.threeDaysBefore.getVariableName())){
            time = DateUtil.getDayBefore(batchTime,3);
        }else if(variableName.equals(EmbedVariableEnum.sevenDaysBefore.getVariableName())){
            time = DateUtil.getDayBefore(batchTime,7);
        }else if(variableName.equals(EmbedVariableEnum.fourteenDaysBefore.getVariableName())){
            time = DateUtil.getDayBefore(batchTime,14);
        }else if(variableName.equals(EmbedVariableEnum.thirtyDaysBefore.getVariableName())){
            time = DateUtil.getDayBefore(batchTime,30);
        }else if(variableName.equals(EmbedVariableEnum.weekBegin.getVariableName())){
            time = DateUtil.getWeekStartTime(batchTime);
        }else if(variableName.equals(EmbedVariableEnum.weekEnd.getVariableName())){
            time = DateUtil.getWeekEndTime(batchTime);
        }else if(variableName.equals(EmbedVariableEnum.lastWeekBegin.getVariableName())){
            Date tempDate = new Date(DateUtil.getDayBefore(batchTime, 7));
            time = DateUtil.getWeekStartTime(tempDate.getTime());
        }else if(variableName.equals(EmbedVariableEnum.lastWeekEnd.getVariableName())){
            Date tempDate = new Date(DateUtil.getDayBefore(batchTime, 7));
            time = DateUtil.getWeekEndTime(tempDate.getTime());
        }else if(variableName.equals(EmbedVariableEnum.monthBegin.getVariableName())){
            time = DateUtil.getMonthStartTime(batchTime);
        }else if(variableName.equals(EmbedVariableEnum.monthEnd.getVariableName())){
            time = DateUtil.getMonthEndTime(batchTime);
        }else if(variableName.equals(EmbedVariableEnum.lastMonthBegin.getVariableName())){
            int year = DateUtil.getYear(batchTime);
            int month = DateUtil.getMonthIndexOfYear(batchTime);
            if(month == 1){
                year = year - 1;
                month = 12;
            }else{
                month = month -1;
            }
            time = DateUtil.getMonthStartTime(year,month);
        }else if(variableName.equals(EmbedVariableEnum.lastMonthEnd.getVariableName())){
            int year = DateUtil.getYear(batchTime);
            int month = DateUtil.getMonthIndexOfYear(batchTime);
            if(month == 1){
                year = year - 1;
                month = 12;
            }else{
                month = month -1;
            }
            time = DateUtil.getMonthEndTime(year, month);
        }

        if("timestamp".equals(format)){
            value = time;
        }else{
            value = DateUtil.formatTimeStamp(time,format);
        }
        if(value.getClass() == String.class){
            value = "'" + value.toString() + "'";
        }
        return value;
    }
}
