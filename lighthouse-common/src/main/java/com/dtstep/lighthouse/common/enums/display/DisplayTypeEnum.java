package com.dtstep.lighthouse.common.enums.display;
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

import com.dtstep.lighthouse.common.entity.display.BarResult;
import com.dtstep.lighthouse.common.entity.display.LineResult;
import com.dtstep.lighthouse.common.entity.display.Result;
import com.dtstep.lighthouse.common.entity.display.TableDataResult;


public enum DisplayTypeEnum {

    LineChart(0,"LineChart", LineResult.class),

    AreaChart(1,"AreaChart",LineResult.class),

    BarChart(2,"BarChart", BarResult.class),

    Table(3,"Table", TableDataResult.class)

    ;

    private int displayType;

    private String chartName;

    private Class<? extends Result> valueType;

    DisplayTypeEnum(int displayType,String chartName,Class<? extends Result> valueType){
        this.displayType = displayType;
        this.chartName = chartName;
        this.valueType = valueType;
    }

    public int getDisplayType() {
        return displayType;
    }

    public void setDisplayType(int displayType) {
        this.displayType = displayType;
    }

    public String getChartName() {
        return chartName;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    public Class<? extends Result> getValueType() {
        return valueType;
    }

    public void setValueType(Class<? extends Result> valueType) {
        this.valueType = valueType;
    }

    public static DisplayTypeEnum getType(int displayType){
        for(DisplayTypeEnum displayTypeEnum : DisplayTypeEnum.values()){
            if(displayTypeEnum.getDisplayType() == displayType){
                return displayTypeEnum;
            }
        }
        return null;
    }
}
