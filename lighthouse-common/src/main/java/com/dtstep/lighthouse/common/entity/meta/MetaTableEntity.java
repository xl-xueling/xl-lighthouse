package com.dtstep.lighthouse.common.entity.meta;
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


import com.dtstep.lighthouse.common.entity.annotation.DBColumnAnnotation;
import com.dtstep.lighthouse.common.entity.annotation.DBNameAnnotation;
import com.dtstep.lighthouse.common.enums.meta.MetaTableTypeEnum;

import java.io.Serializable;
import java.util.Date;


@DBNameAnnotation(name="ldp_meta_table")
public class MetaTableEntity implements Serializable {

    private static final long serialVersionUID = -6046396594919532140L;

    @DBColumnAnnotation(basic="id")
    private int id;

    @DBColumnAnnotation(basic="meta_name")
    private String metaName;

    @DBColumnAnnotation(basic="type")
    private int type;

    @DBColumnAnnotation(basic="state")
    private int state;

    @DBColumnAnnotation(basic="desc")
    private String desc;

    private MetaTableTypeEnum metaTableTypeEnum;

    @DBColumnAnnotation(basic="template")
    private String template;

    @DBColumnAnnotation(basic="record_size")
    private long recordSize;

    @DBColumnAnnotation(basic="content_size")
    private long contentSize;

    @DBColumnAnnotation(basic="create_time")
    private Date createTime;

    @DBColumnAnnotation(basic="update_time")
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
