package com.dtstep.lighthouse.common.modal;
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
import com.dtstep.lighthouse.common.entity.annotation.DBColumnAnnotation;
import com.dtstep.lighthouse.common.entity.annotation.DBNameAnnotation;
import com.dtstep.lighthouse.common.enums.MetaTableTypeEnum;

import java.io.Serializable;
import java.util.Date;

public class MetaTable implements Serializable {

    private static final long serialVersionUID = -6046396594919532140L;

    private int id;

    private String metaName;

    private int type;

    private int state;

    private String desc;

    private MetaTableTypeEnum metaTableTypeEnum;

    private String template;

    private long recordSize;

    private long contentSize;

    private Date createTime;

    private Date updateTime;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.metaTableTypeEnum = MetaTableTypeEnum.getMetaTableTypeEnum(type);
        this.type = type;
    }

    public MetaTableTypeEnum getMetaTableTypeEnum() {
        return metaTableTypeEnum;
    }

    public void setMetaTableTypeEnum(MetaTableTypeEnum metaTableTypeEnum) {
        this.type = metaTableTypeEnum.getType();
        this.metaTableTypeEnum = metaTableTypeEnum;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template.replace("'","\'");
        this.template = template.replace("\"","\"");
        this.template = template;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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
}
