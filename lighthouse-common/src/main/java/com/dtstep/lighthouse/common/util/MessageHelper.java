package com.dtstep.lighthouse.common.util;
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
import com.dtstep.lighthouse.common.entity.message.LightMessage;
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.sbr.StringBuilderHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public final class MessageHelper {

    private static final Logger logger = LoggerFactory.getLogger(MessageHelper.class);

    public static LightMessage parseText(String message){
        if(StringUtil.isEmpty(message)){
            return null;
        }
        LightMessage entity = new LightMessage();
        StringTokenizer stringTokenizer = new StringTokenizer(message, StatConst.SEPARATOR_LEVEL_1);
        int index = 0;
        while(stringTokenizer.hasMoreTokens()){
            String temp  = stringTokenizer.nextToken();
            if(index == 0){
                entity.setGroupId(Integer.parseInt(temp));
            }else if(index == 1){
                entity.setTime(Long.parseLong(temp));
            }else if(index == 2){
                HashMap<String,String> paramMap = new HashMap<>();
                if(!StringUtil.isEmptyOrNullStr(temp)){
                    StringTokenizer subTokenizer = new StringTokenizer(temp, StatConst.SEPARATOR_LEVEL_2);
                    while(subTokenizer.hasMoreTokens()){
                        String kv = subTokenizer.nextToken();
                        int separator = kv.indexOf(StatConst.SEPARATOR_LEVEL_3);
                        String key = kv.substring(0,separator);
                        String value = kv.substring(separator + 1);
                        if(value.contains(StatConst.DIMENS_SEPARATOR)){
                            value = value.replaceAll(StatConst.DIMENS_SEPARATOR,StatConst.DEFAULT_SPLIT_PLACEHOLDER);
                        }else if(value.contains(StatConst.MULTI_PAIR_SEPARATOR)){
                            value = value.replaceAll(StatConst.MULTI_PAIR_SEPARATOR,StatConst.DEFAULT_SPLIT_PLACEHOLDER);
                        }
                        paramMap.put(key,value);
                    }
                }
                entity.setParamMap(paramMap);
            }else if(index == 3){
                if(!StringUtil.isEmpty(temp)){
                    entity.setRepeat(Integer.parseInt(temp));
                }
            }
            index++;
        }
        return entity;
    }

    public static String serializeOfText(int groupId,LinkedHashMap<String,String> paramMap,long timestamp){
        StringBuilder sbr = StringBuilderHolder.Normal.getStringBuilder()
                .append(groupId).append(StatConst.SEPARATOR_LEVEL_1)
                .append(timestamp).append(StatConst.SEPARATOR_LEVEL_1);
        if(paramMap == null || paramMap.isEmpty()){
            sbr.append(StatConst.NULL_STR);
        }else{
            int i=0;
            for(Map.Entry<String, String> entry : paramMap.entrySet()){
                if(i != 0){
                    sbr.append(StatConst.SEPARATOR_LEVEL_2);
                }
                sbr.append(entry.getKey()).append(StatConst.SEPARATOR_LEVEL_3).append(entry.getValue());
                i++;
            }
        }
        return sbr.toString();
    }

    @Deprecated
    public static String serializeOfText(LightMessage lightMessage){
        if(lightMessage == null){
            return null;
        }
        StringBuilder sbr = StringBuilderHolder.Normal.getStringBuilder()
                .append(lightMessage.getGroupId()).append(StatConst.SEPARATOR_LEVEL_1)
                .append(lightMessage.getTime()).append(StatConst.SEPARATOR_LEVEL_1);
        Map<String,String> paramMap = lightMessage.getParamMap();
        if(paramMap == null || paramMap.isEmpty()){
            sbr.append(StatConst.NULL_STR);
        }else{
            int i=0;
            for(Map.Entry<String, String> entry : paramMap.entrySet()){
                if(i != 0){
                    sbr.append(StatConst.SEPARATOR_LEVEL_2);
                }
                sbr.append(entry.getKey()).append(StatConst.SEPARATOR_LEVEL_3).append(entry.getValue());
                i++;
            }
        }
        return sbr.toString();
    }

}
