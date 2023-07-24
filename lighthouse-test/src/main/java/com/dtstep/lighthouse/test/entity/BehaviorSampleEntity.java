package com.dtstep.lighthouse.test.entity;

import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.entity.annotation.DBColumnAnnotation;
import com.dtstep.lighthouse.common.entity.annotation.DBNameAnnotation;

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
@DBNameAnnotation(name= SysConst.TEST_SCENE_BEHAVIOR_STAT)
public class BehaviorSampleEntity extends SampleEntity {

    @DBColumnAnnotation(basic="id")
    private int id;

    @DBColumnAnnotation(basic="behavior_id")
    private String behavior_id;

    @DBColumnAnnotation(basic="request_id")
    private String request_id;

    @DBColumnAnnotation(basic="imei")
    private String imei;

    @DBColumnAnnotation(basic="province")
    private String province;

    @DBColumnAnnotation(basic="city")
    private String city;

    @DBColumnAnnotation(basic="behavior_type")
    private int behavior_type;

    @DBColumnAnnotation(basic="item_id")
    private String item_id;

    @DBColumnAnnotation(basic="app_version")
    private String app_version;

    @DBColumnAnnotation(basic="os")
    private String os;

    @DBColumnAnnotation(basic="recall_no")
    private int recall_no;

    @DBColumnAnnotation(basic="abtest_no")
    private String abtest_no;

    @DBColumnAnnotation(basic="top_level")
    private String top_level;

    @DBColumnAnnotation(basic="sec_level")
    private String sec_level;

    @DBColumnAnnotation(basic="score")
    private String score;

    public String getBehavior_id() {
        return behavior_id;
    }

    public void setBehavior_id(String behavior_id) {
        this.behavior_id = behavior_id;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getBehavior_type() {
        return behavior_type;
    }

    public void setBehavior_type(int behavior_type) {
        this.behavior_type = behavior_type;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public int getRecall_no() {
        return recall_no;
    }

    public void setRecall_no(int recall_no) {
        this.recall_no = recall_no;
    }

    public String getAbtest_no() {
        return abtest_no;
    }

    public void setAbtest_no(String abtest_no) {
        this.abtest_no = abtest_no;
    }

    public String getTop_level() {
        return top_level;
    }

    public void setTop_level(String top_level) {
        this.top_level = top_level;
    }

    public String getSec_level() {
        return sec_level;
    }

    public void setSec_level(String sec_level) {
        this.sec_level = sec_level;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
