package com.dtstep.lighthouse.core.preparing.handler.translate;
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
import com.dtstep.lighthouse.common.entity.message.LightMessage;
import com.dtstep.lighthouse.common.util.MessageHelper;
import com.dtstep.lighthouse.common.util.SnappyUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class DefaultTranslateHandler implements TranslateHandler {

    private static final Logger logger = LoggerFactory.getLogger(DefaultTranslateHandler.class);

    @Override
    public List<LightMessage> translate(byte[] bytes) throws Exception {
        String text = SnappyUtil.isCompress(bytes)? SnappyUtil.uncompressByte(bytes):new String(bytes, StandardCharsets.UTF_8);
        if(StringUtil.isEmpty(text)){
            return null;
        }
        List<LightMessage> lightMessages = new ArrayList<>();
        StringTokenizer stringTokenizer = new StringTokenizer(text, StatConst.SEPARATOR_LEVEL_0);
        while (stringTokenizer.hasMoreTokens()){
            String message = stringTokenizer.nextToken();
            try{
                LightMessage entity = MessageHelper.parseText(message);
                if(entity != null){
                    lightMessages.add(entity);
                }
            }catch (Exception ex){
                logger.error("translate message error,message:{}",text,ex);
            }
        }
        return lightMessages;
    }
}
