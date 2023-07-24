package com.dtstep.lighthouse.common.entity.sitemap;
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
import com.dtstep.lighthouse.common.entity.annotation.valid.S_Illegal;
import com.dtstep.lighthouse.common.entity.annotation.valid.S_Integer;
import com.dtstep.lighthouse.common.entity.annotation.valid.S_Length;
import com.dtstep.lighthouse.common.entity.annotation.valid.S_NotNull;
import com.dtstep.lighthouse.common.entity.list.IListEntity;

import java.io.Serializable;
import java.util.Date;

@DBNameAnnotation(name="ldp_stat_sitemap")
public class SiteMapEntity implements Serializable, IListEntity {

    private static final long serialVersionUID = -2127414334727921735L;

    @S_Integer
    @DBColumnAnnotation(basic="id")
    private int id;

    @S_NotNull
    @S_Length(min = 4,max = 30)
    @S_Illegal
    @DBColumnAnnotation(basic="name")
    private String name;

    @S_NotNull
    @DBColumnAnnotation(basic="config")
    private String config;

    @DBColumnAnnotation(basic="star")
    private int star;

    @DBColumnAnnotation(basic="user_id")
    private int userId;

    @S_NotNull
    @S_Length(min = 4,max = 50)
    @DBColumnAnnotation(basic="desc")
    private String desc;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }
}
