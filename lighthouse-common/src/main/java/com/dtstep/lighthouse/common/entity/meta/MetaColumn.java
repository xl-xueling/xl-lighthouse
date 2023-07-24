package com.dtstep.lighthouse.common.entity.meta;
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

import com.dtstep.lighthouse.common.entity.annotation.valid.S_Illegal;
import com.dtstep.lighthouse.common.entity.annotation.valid.S_Length;
import com.dtstep.lighthouse.common.entity.annotation.valid.S_NotNull;
import com.dtstep.lighthouse.common.entity.annotation.valid.S_Pattern;
import com.dtstep.lighthouse.common.enums.meta.ColumnTypeEnum;

import java.io.Serializable;


public class MetaColumn implements Serializable{

    private static final long serialVersionUID = -2265569382426765950L;

    @S_NotNull
    @S_Pattern(pattern = "^(?!_)(?!.*?_$)[a-zA-Z0-9_]+$")
    @S_Length(min = 2,max = 20)
    private String columnName;

    private ColumnTypeEnum columnTypeEnum;

    @S_NotNull
    @S_Illegal
    private int columnType;

    private int columnLength = -1;

    @S_NotNull
    private String columnComment;

    public MetaColumn(){
    }
    public MetaColumn(String columnName){
        this.columnName = columnName;
        this.columnTypeEnum = ColumnTypeEnum.String;
    }

    private boolean isPartition = false;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public int getColumnLength() {
        return columnLength;
    }

    public void setColumnLength(int columnLength) {
        this.columnLength = columnLength;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public ColumnTypeEnum getColumnTypeEnum() {
        return columnTypeEnum;
    }

    public void setColumnTypeEnum(ColumnTypeEnum columnTypeEnum) {
        this.columnTypeEnum = columnTypeEnum;
        this.columnType = columnTypeEnum.getType();
    }

    public int getColumnType() {
        return columnType;
    }

    public void setColumnType(int columnType) {
        this.columnType = columnType;
        this.columnTypeEnum = ColumnTypeEnum.getColumnTypeEnum(columnType);
    }

    public boolean isPartition() {
        return isPartition;
    }

    public void setPartition(boolean isPartition) {
        this.isPartition = isPartition;
    }
}
