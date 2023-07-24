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
import com.dtstep.lighthouse.common.entity.user.UserEntity;
import com.dtstep.lighthouse.common.util.BeanCopyUtil;

import java.util.List;

public class SiteMapViewEntity extends SiteMapEntity {

    private static final long serialVersionUID = -2849466585161536284L;

    private List<UserEntity> admins;

    private List<Integer> privilegeIds;

    private boolean isApply;

    public SiteMapViewEntity(SiteMapEntity siteMapEntity){
        assert siteMapEntity != null;
        BeanCopyUtil.copy(siteMapEntity,this);
    }

    private boolean isFocus = false;

    public boolean isFocus() {
        return isFocus;
    }

    public void setFocus(boolean focus) {
        isFocus = focus;
    }

    public List<UserEntity> getAdmins() {
        return admins;
    }

    public void setAdmins(List<UserEntity> admins) {
        this.admins = admins;
    }

    public List<Integer> getPrivilegeIds() {
        return privilegeIds;
    }

    public void setPrivilegeIds(List<Integer> privilegeIds) {
        this.privilegeIds = privilegeIds;
    }

    public boolean isApply() {
        return isApply;
    }

    public void setApply(boolean apply) {
        isApply = apply;
    }
}
