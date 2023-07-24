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

public enum MetaTableTypeEnum {

    SYSTEM_RESULT_TABLE(1),

    SEQ_RESULT_TABLE(2),

    STAT_RESULT_TABLE(3),

    TEST_RESULT_TABLE(4),

    ;

    MetaTableTypeEnum(int type){
        this.type = type;
    }

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static MetaTableTypeEnum getMetaTableTypeEnum(int type){
        for(MetaTableTypeEnum metaTableTypeEnum : MetaTableTypeEnum.values()){
            if(metaTableTypeEnum.getType() == type){
                return metaTableTypeEnum;
            }
        }
        return null;
    }
}
