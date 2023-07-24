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

import com.dtstep.lighthouse.common.entity.view.TimeLineEntity;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class BarWithTimeLineResult extends AbstractResult{

    private static final long serialVersionUID = 7003375309827224568L;

    private long batchTime;

    private List<Object> valueList;

    private TimeLineEntity timeLine;

    public BarWithTimeLineResult(TimeUnit timeParamUnit){
        super(timeParamUnit);
    }

    public TimeLineEntity getTimeLine() {
        return timeLine;
    }

    public void setTimeLine(TimeLineEntity timeLine) {
        this.timeLine = timeLine;
    }

    public long getBatchTime() {
        return batchTime;
    }

    public void setBatchTime(long batchTime) {
        this.batchTime = batchTime;
    }

    public List<Object> getValueList() {
        return valueList;
    }

    public void setValueList(List<Object> valueList) {
        this.valueList = valueList;
    }
}
