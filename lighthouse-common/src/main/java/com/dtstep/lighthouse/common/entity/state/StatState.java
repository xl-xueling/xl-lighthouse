package com.dtstep.lighthouse.common.entity.state;
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

import com.dtstep.lighthouse.common.enums.function.FunctionEnum;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Set;


public final class StatState implements Serializable {

    private static final long serialVersionUID = 7711305424710115418L;

    private int statId;

    private int groupId;

    private String stateBody;

    private FunctionEnum functionEnum;

    private String stateName;

    private int functionIndex;

    private List<StatUnit> unitList;

    private Set<String> relatedColumnSet;

    private boolean isBuiltIn = false;

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
        this.setFunctionEnum(FunctionEnum.getStatFunctionEnum(stateName));
    }

    public int getFunctionIndex() {
        return functionIndex;
    }

    public void setFunctionIndex(int functionIndex) {
        this.functionIndex = functionIndex;
    }

    public List<StatUnit> getUnitList() {
        return unitList;
    }

    public void setUnitList(List<StatUnit> unitList) {
        this.unitList = unitList;
    }

    public static void valid(StatState statState) throws Exception{
    }

    public String getStateBody() {
        return stateBody;
    }

    public void setStateBody(String stateBody) {
        this.stateBody = stateBody;
    }

    public Set<String> getRelatedColumnSet() {
        return relatedColumnSet;
    }

    public void setRelatedColumnSet(Set<String> relatedColumnSet) {
        this.relatedColumnSet = relatedColumnSet;
    }

    public FunctionEnum getFunctionEnum() {
        return functionEnum;
    }

    public void setFunctionEnum(FunctionEnum functionEnum) {
        this.functionEnum = functionEnum;
    }

    public int getStatId() {
        return statId;
    }

    public void setStatId(int statId) {
        this.statId = statId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public boolean isBuiltIn() {
        return isBuiltIn;
    }

    public void setBuiltIn(boolean builtIn) {
        isBuiltIn = builtIn;
    }

    public static boolean isBitCountState(StatState statState){
        return statState.getFunctionEnum() == FunctionEnum.BITCOUNT;
    }

    public static boolean isCountState(StatState statState){
        return statState.getFunctionEnum() == FunctionEnum.COUNT;
    }

    public static int getFilterStartIndex(StatState statState){
        return statState.getFunctionEnum() == FunctionEnum.COUNT ? 0 : 1;
    }

    public static String getFirstUnit(StatState statState){
        return CollectionUtils.isEmpty(statState.getUnitList()) ? null : statState.getUnitList().get(0).getOrigin();
    }


}
