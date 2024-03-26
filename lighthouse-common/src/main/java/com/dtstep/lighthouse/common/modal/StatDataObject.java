package com.dtstep.lighthouse.common.modal;
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
import com.dtstep.lighthouse.common.entity.view.StatValue;

import java.util.List;

public class StatDataObject {

    private Integer statId;

    private String dimensValue;

    private String displayDimensValue;

    private List<StatValue> valuesList;

    public String getDimensValue() {
        return dimensValue;
    }

    public void setDimensValue(String dimensValue) {
        this.dimensValue = dimensValue;
    }

    public Integer getStatId() {
        return statId;
    }

    public void setStatId(Integer statId) {
        this.statId = statId;
    }

    public List<StatValue> getValuesList() {
        return valuesList;
    }

    public void setValuesList(List<StatValue> valuesList) {
        this.valuesList = valuesList;
    }

    public String getDisplayDimensValue() {
        return displayDimensValue;
    }

    public void setDisplayDimensValue(String displayDimensValue) {
        this.displayDimensValue = displayDimensValue;
    }
}
