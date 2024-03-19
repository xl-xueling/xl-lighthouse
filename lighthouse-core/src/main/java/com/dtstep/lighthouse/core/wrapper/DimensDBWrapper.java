package com.dtstep.lighthouse.core.wrapper;
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
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.core.formula.FormulaCalculate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;


public final class DimensDBWrapper {

    private static final Logger logger = LoggerFactory.getLogger(DimensDBWrapper.class);

    public static String getDimensValue(Map<String,Object> envMap,String[] dimensArr,long batchTime) throws Exception {
        if(dimensArr == null){
            return null;
        }else if(dimensArr.length == 1 && envMap.containsKey(dimensArr[0])){
            return String.valueOf(envMap.get(dimensArr[0]));
        }else{
            StringBuilder sbr = new StringBuilder(32);
            for(int i=0;i<dimensArr.length;i++){
                if(i != 0){
                    sbr.append(StatConst.DIMENS_SEPARATOR);
                }
                String subDimens = dimensArr[i];
                if(envMap.containsKey(subDimens)){
                    subDimens = String.valueOf(envMap.get(subDimens));
                }else{
                    subDimens = String.valueOf(FormulaCalculate.parseVariableEntity(subDimens,envMap,batchTime));
                }
                sbr.append(subDimens);
            }
            return sbr.toString();
        }
    }

}
