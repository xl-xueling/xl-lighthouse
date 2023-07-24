package com.dtstep.lighthouse.common.entity.display;
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

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AbstractResult implements Result {

    private static final long serialVersionUID = 7836369600040063329L;

    private String xAxisFormat;

    private Map<String,String> dimensMapperInfo;

    AbstractResult(TimeUnit timeUnit){
        if(timeUnit == TimeUnit.MINUTES){
            this.xAxisFormat = "hh:mm";
        }else if(timeUnit == TimeUnit.HOURS){
            this.xAxisFormat = "yyyyMMddhh";
        }else if(timeUnit == TimeUnit.DAYS){
            this.xAxisFormat = "yyyyMMdd";
        }if(timeUnit == TimeUnit.SECONDS){
            this.xAxisFormat = "hh:mm:ss";
        }
    }

    public String getxAxisFormat() {
        return xAxisFormat;
    }

    public void setxAxisFormat(String xAxisFormat) {
        this.xAxisFormat = xAxisFormat;
    }

    public Map<String, String> getDimensMapperInfo() {
        return dimensMapperInfo;
    }

    public void setDimensMapperInfo(Map<String, String> dimensMapperInfo) {
        this.dimensMapperInfo = dimensMapperInfo;
    }
}
