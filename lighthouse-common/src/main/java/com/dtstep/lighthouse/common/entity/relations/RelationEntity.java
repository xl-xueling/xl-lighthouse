package com.dtstep.lighthouse.common.entity.relations;
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
import com.dtstep.lighthouse.common.entity.annotation.valid.S_Integer;
import com.dtstep.lighthouse.common.entity.annotation.valid.S_NotNull;

import java.io.Serializable;
import java.util.Date;

@DBNameAnnotation(name="ldp_relations")
public class RelationEntity implements Serializable {

    private static final long serialVersionUID = 386718604588785857L;

    @DBColumnAnnotation(basic="id")
    private int id;

    @S_NotNull
    @S_Integer
    @DBColumnAnnotation(basic="relation_a")
    private int relationA;

    @S_NotNull
    @S_Integer
    @DBColumnAnnotation(basic="relation_b")
    private int relationB;

    @S_NotNull
    @S_Integer
    @DBColumnAnnotation(basic="relation_type")
    private int relationType;

    @DBColumnAnnotation(basic="create_time")
    private Date createTime;

    @DBColumnAnnotation(basic="hash")
    private String hash;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRelationA() {
        return relationA;
    }

    public void setRelationA(int relationA) {
        this.relationA = relationA;
    }

    public int getRelationB() {
        return relationB;
    }

    public void setRelationB(int relationB) {
        this.relationB = relationB;
    }

    public int getRelationType() {
        return relationType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setRelationType(int relationType) {
        this.relationType = relationType;
    }
}
