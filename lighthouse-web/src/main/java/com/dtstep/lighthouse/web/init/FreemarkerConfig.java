package com.dtstep.lighthouse.web.init;
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
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.core.config.LDPConfig;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Configuration
public class FreemarkerConfig {

    @Resource
    private freemarker.template.Configuration configuration;

    @PostConstruct
    public void setConfigure() throws Exception {
        configuration.setSharedVariable("_ldp_version", SysConst._VERSION);
        configuration.setSharedVariable("_ldp_cluster", LDPConfig.getVal(LDPConfig.KEY_CLUSTER_ID));
    }

    public freemarker.template.Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(freemarker.template.Configuration configuration) {
        this.configuration = configuration;
    }
}
