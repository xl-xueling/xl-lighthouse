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


public enum FunctionEnum {

    COUNT(1,"count"),

    BITCOUNT(2,"bitcount"),

    AVG(3,"avg"),

    MAX(4,"max"),

    MIN(5,"min"),

    SUM(6,"sum"),

    SEQ(7,"seq"),

    ;

    private int functionId;

    private String functionName;

    FunctionEnum(int functionId, String functionName){
        this.functionId = functionId;
        this.functionName = functionName;
    }

    public int getFunctionId() {
        return functionId;
    }

    public void setFunctionId(int functionId) {
        this.functionId = functionId;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public static boolean isStatFunction(String temp){
        for(FunctionEnum functionEnum : FunctionEnum.values()){
            if(functionEnum.functionName.equals(temp)){
                return true;
            }
        }
        return false;
    }

    public static FunctionEnum getStatFunctionEnum(String functionName){
        for(FunctionEnum functionEnum : FunctionEnum.values()){
            if(functionEnum.getFunctionName().equals(functionName)){
                return functionEnum;
            }
        }
        return null;
    }
}
