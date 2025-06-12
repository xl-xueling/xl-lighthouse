package com.dtstep.lighthouse.common.modal;
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
import com.dtstep.lighthouse.common.enums.MetaTableStateEnum;
import com.dtstep.lighthouse.common.enums.MetaTableTypeEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

public class MetaTable implements Serializable {

    private static final long serialVersionUID = -6046396594919532140L;

    private int id;

    private String metaName;

    private MetaTableTypeEnum metaTableType;

    private MetaTableStateEnum state;

    private String desc;

    private long recordSize;

    private long contentSize;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMetaName() {
        return metaName;
    }

    public void setMetaName(String metaName) {
        this.metaName = metaName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getRecordSize() {
        return recordSize;
    }

    public void setRecordSize(long recordSize) {
        this.recordSize = recordSize;
    }

    public long getContentSize() {
        return contentSize;
    }

    public void setContentSize(long contentSize) {
        this.contentSize = contentSize;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public MetaTableStateEnum getState() {
        return state;
    }

    public void setState(MetaTableStateEnum state) {
        this.state = state;
    }

    public MetaTableTypeEnum getMetaTableType() {
        return metaTableType;
    }

    public void setMetaTableType(MetaTableTypeEnum metaTableType) {
        this.metaTableType = metaTableType;
    }
}
