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

public class SiteMapBindViewEntity extends SiteMapBindEntity {

    private static final long serialVersionUID = 2070570776585334402L;

    private String bindPath;

    private List<UserEntity> admins;

    private String siteConfig;

    private String stateDesc;

    private String elementName;

    private String elementLink;

    public SiteMapBindViewEntity(SiteMapBindEntity siteMapBindEntity){
        assert siteMapBindEntity != null;
        BeanCopyUtil.copy(siteMapBindEntity,this);
    }

    public String getBindPath() {
        return bindPath;
    }

    public void setBindPath(String bindPath) {
        this.bindPath = bindPath;
    }

    public List<UserEntity> getAdmins() {
        return admins;
    }

    public void setAdmins(List<UserEntity> admins) {
        this.admins = admins;
    }

    public String getStateDesc() {
        return stateDesc;
    }

    public void setStateDesc(String stateDesc) {
        this.stateDesc = stateDesc;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getElementLink() {
        return elementLink;
    }

    public void setElementLink(String elementLink) {
        this.elementLink = elementLink;
    }

    public String getSiteConfig() {
        return siteConfig;
    }

    public void setSiteConfig(String siteConfig) {
        this.siteConfig = siteConfig;
    }
}
