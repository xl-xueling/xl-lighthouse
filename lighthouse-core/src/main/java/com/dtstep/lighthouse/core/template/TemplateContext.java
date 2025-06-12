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

import com.dtstep.lighthouse.common.modal.Column;

import java.util.List;

public final class TemplateContext {

    private int statId;

    private String template;

    private String timeParam;

    private List<Column> columnList;

    public TemplateContext(String template,String timeParam,List<Column> columnList){
        this.template = template;
        this.timeParam = timeParam;
        this.columnList = columnList;
    }

    public TemplateContext(int statId,String template,String timeParam,List<Column> columnList){
        this.statId = statId;
        this.template = template;
        this.timeParam = timeParam;
        this.columnList = columnList;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getTimeParam() {
        return timeParam;
    }

    public void setTimeParam(String timeParam) {
        this.timeParam = timeParam;
    }

    public List<Column> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<Column> columnList) {
        this.columnList = columnList;
    }

    public int getStatId() {
        return statId;
    }

    public void setStatId(int statId) {
        this.statId = statId;
    }
}
