package com.dtstep.lighthouse.core.batch;
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
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.core.sort.SortOperator;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class IntervalBatchHandler implements BatchInterface{

    @Override
    public String getDisplayFormat(String timeParam,Date startTime,Date endTime) {
        long interval =  DateUtil.daysBetween(startTime.getTime(),endTime.getTime());
        if(timeParam.endsWith("hour")){
            if(interval >= 1){
                return "MMddhh";
            }else{
                return "hh:mm";
            }
        }else if(timeParam.endsWith("minute")){
            if(interval >= 1){
                return "MMddhhmm";
            }else{
                return "hh:mm";
            }
        }else if(timeParam.endsWith("day")){
            return "yyyyMMdd";
        }
        return null;
    }

    @Override
    public List<Long> queryBatchTimeList(String timeParam, long startDate,long endDate, int limitSize) throws Exception {
        long tempTime = endDate;
        List<Long> dateList = Lists.newArrayList();
        while (tempTime > startDate){
            if(limitSize != -1 && dateList.size() >= limitSize){
                break;
            }
            long t = getBatch(timeParam,tempTime);
            dateList.add(t);
            tempTime = (t - 1);
        }
        SortOperator.sortList(dateList);
        return dateList;
    }

    @Override
    public long getBatch(int interval,TimeUnit timeUnit, long current){
        return DateUtil.batchTime(interval,timeUnit,current);
    }

    @Deprecated
    @Override
    public long getBatch(String timeParam, long t) {
        String[] timeParamArr = timeParam.split("-");
        int interval = Integer.parseInt(timeParamArr[0]);
        TimeUnit timeUnit = null;
        if("second".equals(timeParamArr[1])){
            timeUnit = TimeUnit.SECONDS;
        }else if("minute".equals(timeParamArr[1])){
            timeUnit = TimeUnit.MINUTES;
        }else if("hour".equals(timeParamArr[1])){
            timeUnit = TimeUnit.HOURS;
        }else if("day".equals(timeParamArr[1])){
            timeUnit = TimeUnit.DAYS;
        }
        return getBatch(interval,timeUnit,t);
    }
}
