package com.dtstep.lighthouse.core.preparing.handler.valid;
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
import com.dtstep.lighthouse.common.entity.message.LightMessage;
import com.dtstep.lighthouse.common.enums.ColumnTypeEnum;
import com.dtstep.lighthouse.common.modal.Column;
import com.dtstep.lighthouse.common.util.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public final class MessageValid {

    private static final Logger logger = LoggerFactory.getLogger(MessageValid.class);

    public static boolean valid(int groupId,LightMessage message, List<Column> columnList) throws Exception {
        if(CollectionUtils.isEmpty(columnList)){
            return true;
        }
        for (Column metaColumn : columnList) {
            if (!valid(groupId,message, metaColumn)) {
                return false;
            }
        }
        return true;
    }


    public static boolean valid(int groupId,LightMessage message,Column column) {
        String columnName = column.getName();
        String value = message.getParamMap().get(columnName);
        if(StringUtil.isEmptyOrNullStr(value)){
            return true;
        }
        ColumnTypeEnum columnTypeEnum = column.getType();
        boolean is = columnTypeEnum != ColumnTypeEnum.NUMBER || StringUtil.isNumber(value);
        if(!is){
            logger.error("valid group {} message failed,column:{},column type:{}",groupId,columnName,columnTypeEnum.getType());
        }
        return is;
    }
}
