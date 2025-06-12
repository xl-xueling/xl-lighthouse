package com.dtstep.lighthouse.insights.vo;
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
import com.dtstep.lighthouse.common.entity.view.StatValue;

import java.util.List;

public class HomeVO {

    private Integer projectCount;

    private Integer ytdProjectCount;

    private Integer statCount;

    private Integer ytdStatCount;

    private Integer metricCount;

    private Integer ytdMetricCount;

    private Integer userCount;

    private List<StatValue> departmentStatCount;

    public Integer getProjectCount() {
        return projectCount;
    }

    public void setProjectCount(Integer projectCount) {
        this.projectCount = projectCount;
    }

    public Integer getYtdProjectCount() {
        return ytdProjectCount;
    }

    public void setYtdProjectCount(Integer ytdProjectCount) {
        this.ytdProjectCount = ytdProjectCount;
    }

    public Integer getStatCount() {
        return statCount;
    }

    public void setStatCount(Integer statCount) {
        this.statCount = statCount;
    }

    public Integer getYtdStatCount() {
        return ytdStatCount;
    }

    public void setYtdStatCount(Integer ytdStatCount) {
        this.ytdStatCount = ytdStatCount;
    }

    public Integer getMetricCount() {
        return metricCount;
    }

    public void setMetricCount(Integer metricCount) {
        this.metricCount = metricCount;
    }

    public Integer getYtdMetricCount() {
        return ytdMetricCount;
    }

    public void setYtdMetricCount(Integer ytdMetricCount) {
        this.ytdMetricCount = ytdMetricCount;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public List<StatValue> getDepartmentStatCount() {
        return departmentStatCount;
    }

    public void setDepartmentStatCount(List<StatValue> departmentStatCount) {
        this.departmentStatCount = departmentStatCount;
    }
}
