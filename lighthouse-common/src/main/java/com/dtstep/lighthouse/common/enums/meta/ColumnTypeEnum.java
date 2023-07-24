package com.dtstep.lighthouse.common.enums.meta;
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

import com.dtstep.lighthouse.common.util.StringUtil;


public enum  ColumnTypeEnum implements java.io.Serializable{

    String(1,"string"),

    Numeric(2,"numeric"),

    TimeStamp(3,"timestamp");

    private int type;

    private String symbol;

    ColumnTypeEnum(int type,String symbol){
        this.type = type;
        this.symbol = symbol;
    }

    public static ColumnTypeEnum getColumnTypeEnumBySymbol(String symbol){
        if(StringUtil.isEmpty(symbol)){
            return null;
        }
        for(ColumnTypeEnum columnTypeEnum:ColumnTypeEnum.values()){
            if(symbol.toLowerCase().equals(columnTypeEnum.getSymbol())){
                return columnTypeEnum;
            }
        }
        return null;
    }

    public static ColumnTypeEnum getColumnTypeEnum(int type){
        for(ColumnTypeEnum columnTypeEnum:ColumnTypeEnum.values()){
            if(type == columnTypeEnum.getType()){
                return columnTypeEnum;
            }
        }
        return null;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
