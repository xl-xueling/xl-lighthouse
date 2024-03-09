package com.dtstep.lighthouse.insights;
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

import com.dtstep.lighthouse.core.config.LDPConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication(scanBasePackages = {"com.dtstep.lighthouse.insights"})
public class LightHouseInsightsApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication springApplication = new SpringApplication(LightHouseInsightsApplication.class);
        LDPConfig.loadConfiguration();
        Map<String,Object> defaultProperties = new HashMap<>();
        defaultProperties.put("spring.config.name","lighthouse-insights");
        defaultProperties.put("spring.redis.cluster.nodes", LDPConfig.getVal(LDPConfig.KEY_REDIS_CLUSTER));
        defaultProperties.put("spring.datasource.url", LDPConfig.getVal(LDPConfig.KEY_DB_ConnectionURL));
        defaultProperties.put("spring.datasource.driverClassName", LDPConfig.getVal(LDPConfig.KEY_DB_DriverClassName));
        defaultProperties.put("spring.datasource.username", LDPConfig.getVal(LDPConfig.KEY_DB_ConnectionUserName));
        defaultProperties.put("spring.datasource.password", LDPConfig.getVal(LDPConfig.KEY_DB_ConnectionPassword));
        springApplication.setDefaultProperties(defaultProperties);
        springApplication.run(args);
    }

}
