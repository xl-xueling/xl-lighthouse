package com.dtstep.lighthouse.core.message;
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
import com.dtstep.lighthouse.common.entity.meta.MetaColumn;
import com.dtstep.lighthouse.common.enums.meta.ColumnTypeEnum;
import com.dtstep.lighthouse.common.util.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import java.util.List;

public final class MessageValid {

    public static boolean valid(LightMessage message, List<MetaColumn> columnList) throws Exception {
        if(CollectionUtils.isEmpty(columnList)){
            return true;
        }
        for (MetaColumn metaColumn : columnList) {
            if (!valid(message, metaColumn)) {
                return false;
            }
        }
        return true;
    }


    public static boolean valid(LightMessage message,MetaColumn column) {
        String columnName = column.getColumnName();
        String value = message.getParamMap().get(columnName);
        if(StringUtil.isEmptyOrNullStr(value)){
            return true;
        }
        ColumnTypeEnum columnTypeEnum = column.getColumnTypeEnum();
        return columnTypeEnum != ColumnTypeEnum.Numeric || StringUtil.isNumber(value);
    }
}
