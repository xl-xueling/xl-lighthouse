package com.dtstep.lighthouse.core.config;
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
import com.dtstep.lighthouse.common.enums.RunningMode;
import com.dtstep.lighthouse.common.exception.LDPUncaughtExceptionHandler;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.google.common.collect.Maps;
import com.dtstep.lighthouse.common.exception.ConfigParseException;
import com.dtstep.lighthouse.common.exception.InitializationException;
import com.dtstep.lighthouse.common.util.FileUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


public final class LDPConfig {

    private static final Logger logger = LoggerFactory.getLogger(LDPConfig.class);

    private static Map<String,String> paramMap = Maps.newHashMap();

    public static final String KEY_KAFKA_BOOTSTRAP_SERVERS = "kafka.bootstrap.servers";

    public static final String KEY_KAFKA_TOPIC_NAME = "kafka.topic.name";

    public static final String KEY_REDIS_CLUSTER = "redis.cluster";

    public static final String KEY_REDIS_CLUSTER_PASSWORD = "redis.cluster.password";

    public static final String KEY_LIMITING_GROUP_MESSAGE_SIZE_PER_SEC = "limiting.group.message.size.per.sec";

    public static final String KEY_LIMITING_STAT_RESULT_SIZE_PER_SEC = "limiting.stat.result.size.per.sec";

    public static final String KEY_LIGHTHOUSE_LIMITING_ENABLED = "limiting.enabled";

    public static final String KEY_LIGHTHOUSE_ICE_LOCATORS = "lighthouse.ice.locators";

    public static final String KEY_DATA_COMPRESSION_TYPE = "data.storage.compression.type";

    public static final String KEY_CLUSTER_ID = "lighthouse.cluster.id";

    public static final String KEY_RUNNING_MODE = "lighthouse.running.mode";

    public static final String KEY_HOME_PATH = "ldp_home";

    public static final String KEY_DATA_DIR = "ldp_data_dir";

    public static final String KEY_CMDB_STORAGE_ENGINE = "cmdb.storage.engine";

    public static final String KEY_WAREHOUSE_STORAGE_ENGINE = "warehouse.storage.engine";

    private static RunningMode runningMode;

    public static final AtomicBoolean isInit = new AtomicBoolean(false);

    public static void loadConfiguration() throws Exception {
        String ldpHome = System.getenv("LDP_HOME");
        if(StringUtil.isEmpty(ldpHome)){
            logger.error("lighthouse component start error,system environment variable[LDP_HOME] does not exist!");
            throw new InitializationException();
        }
        initWithHomePath(ldpHome);
    }

    public static synchronized void initWithHomePath(final String homePath) throws Exception {
        if(isInit.get()){
            return;
        }
        boolean is = FileUtil.isFileExist(homePath);
        if(!is){
            throw new FileNotFoundException(String.format("home path[%s] not found!",homePath));
        }
        String siteFilePath = homePath + "/conf/ldp-site.xml";
        is = FileUtil.isFileExist(siteFilePath);
        if(!is){
            throw new FileNotFoundException(String.format("configuration file[%s] not found!",siteFilePath));
        }
        paramMap.put("ldp_home",homePath);
        init(siteFilePath);
    }

    public static synchronized void init(final String confPath) throws Exception{
        if(isInit.get()){
            return;
        }
        boolean is = FileUtil.isFileExist(confPath);
        if(!is){
            throw new FileNotFoundException(String.format("configuration file[%s] not found!",confPath));
        }
        File file = new File(confPath);
        Document document = Jsoup.parse(file, "utf-8");
        parseProperties(document.select("configuration > property"), "", paramMap);
        for(String key : paramMap.keySet()){
            System.out.println("key:" + key + ",value:" + paramMap.get(key));
        }
        paramMap  = Collections.unmodifiableMap(paramMap);
        Thread.setDefaultUncaughtExceptionHandler(new LDPUncaughtExceptionHandler());
        isInit.set(true);
    }

    private static void parseProperties(Elements elements, String parentKey, Map<String, String> keyValuePairs) {
        for (Element element : elements) {
            if (element.tagName().equals("property")) {
                String name = element.selectFirst("name").text();
                String value = element.selectFirst("value").text();
                String fullKey = parentKey.isEmpty() ? name : parentKey + "." + name;
                keyValuePairs.put(fullKey, value);
                Elements childProperties = element.select("properties > property");
                if (!childProperties.isEmpty()) {
                    parseProperties(childProperties, fullKey, keyValuePairs);
                }
            }
        }
    }

    public static String getHomeDir(){
        return paramMap.get(KEY_HOME_PATH);
    }

    public static String getDataDir(){
        return paramMap.get(KEY_DATA_DIR);
    }

    public static RunningMode getRunningMode(){
        if(runningMode != null){
            return runningMode;
        }else{
            String mode = paramMap.get(KEY_RUNNING_MODE);
            if(StringUtil.isNotEmpty(mode) && mode.equals("standalone")){
                runningMode = RunningMode.STANDALONE;
            }else{
                runningMode = RunningMode.CLUSTER;
            }
            return runningMode;
        }
    }

    public static Map<String,String> getConf(){
        return paramMap;
    }

    public static String getVal(String param){
        if(paramMap.containsKey(param)){
            return paramMap.get(param);
        }
        return null;
    }

    public static <T> T getOrDefault(String param, T defaultValue, Class<T> clazz) {
        String temp = getVal(param);
        if(StringUtil.isEmpty(temp)){
            return defaultValue;
        }
        if(clazz == Integer.class){
            return clazz.cast(Integer.parseInt(temp));
        }else if(clazz == Boolean.class){
            return clazz.cast("true".equals(temp));
        }else if(clazz == Long.class){
            return clazz.cast(Long.parseLong(temp));
        }else if(clazz == String.class){
            return clazz.cast(temp);
        }
        return defaultValue;
    }

    public static boolean limitedEnable() {
        return getOrDefault(KEY_LIGHTHOUSE_LIMITING_ENABLED,true,Boolean.class);
    }
}
