package com.dtstep.lighthouse.common.enums.function;
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

public enum  EmbedFunctionEnum {

    LEFT("left"),

    RIGHT("right"),

    SUBSTRING("substr"),

    IS_EMPTY("isEmpty"),

    IS_NULL("isNull"),

    IS_NUMERIC("isNumeric"),

    START_WITH("startWith"),

    ENDS_WITH("endsWith"),

    LEN("len"),

    LOWER("toLower"),

    UPPER("toUpper"),

    CONTAINS("contains"),

    TRIM("trim"),

    DATE_PARSE("dateParse"),

    DATE_FORMAT("dateFormat"),

    IS_IN("isIn"),

    DAY_SUB("daySub"),

    HOUR_SUB("hourSub"),

    MINUTE_SUB("minuteSub"),

    SPLIT("split"),

    REVERSE("reverse"),

    REPLACE("replace"),

    MD5("md5"),

    HASHCODE("hashcode"),

    SECTION("section");

    private String functionName;

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    EmbedFunctionEnum(String functionName){
        this.functionName = functionName;
    }

    public static boolean isEmbedFunction(String temp){
        for(EmbedFunctionEnum embedFunctionEnum : EmbedFunctionEnum.values()){
            if(embedFunctionEnum.functionName.equals(temp)){
                return true;
            }
        }
        return false;
    }
}
