package com.dtstep.lighthouse.core.plugins;
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
import com.dtstep.lighthouse.common.util.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PluginManager {

    private static final Logger logger = LoggerFactory.getLogger(PluginManager.class);

    private static final Map<String,Plugin> plugins = new HashMap<>();

    private static final String PRO_STAT_PLUGIN_CLASS = "com.dtstep.lighthouse.pro.core.alarm.DefaultStatAlarmPluginImpl";

    private static final String PRO_NOTIFICATION_PLUGIN_CLASS = "com.dtstep.lighthouse.pro.core.notification.DefaultNotificationPluginImpl";

    static {
        loadStatAlarmPlugin();
    }

    private static void loadStatAlarmPlugin(){
        if(ReflectUtil.isClassPresent(PRO_STAT_PLUGIN_CLASS)){
            try{
                Class<?> clazz = Class.forName(PRO_STAT_PLUGIN_CLASS);
                if(StatAlarmPlugin.class.isAssignableFrom(clazz)){
                    StatAlarmPlugin alarmPlugin = (StatAlarmPlugin) clazz.getDeclaredConstructor().newInstance();
                    plugins.put(alarmPlugin.getName(),alarmPlugin);
                    alarmPlugin.initialize();
                    logger.info(String.format("Plugin[%s] loaded!",alarmPlugin.getName()));
                }
            } catch (Exception ex){
                logger.error("Plugin loaded failed!",ex);
            }
        }
    }

    private static void loadNotificationPlugin(){
        if(ReflectUtil.isClassPresent(PRO_NOTIFICATION_PLUGIN_CLASS)){
            try{
                Class<?> clazz = Class.forName(PRO_NOTIFICATION_PLUGIN_CLASS);
                if(NotificationPlugin.class.isAssignableFrom(clazz)){
                    NotificationPlugin notificationPlugin = (NotificationPlugin) clazz.getDeclaredConstructor().newInstance();
                    plugins.put(notificationPlugin.getName(),notificationPlugin);
                    notificationPlugin.initialize();
                    logger.info(String.format("Plugin[%s] loaded!",notificationPlugin.getName()));
                }
            } catch (Exception ex){
                logger.error("Plugin loaded failed!",ex);
            }
        }
    }

    public static Optional<StatAlarmPlugin> getAlarmPlugin() {
        Plugin plugin = plugins.get("StatAlarm");
        return Optional.ofNullable(plugin instanceof StatAlarmPlugin ? (StatAlarmPlugin) plugin : null);
    }

    public static Optional<NotificationPlugin> getNotificationPlugin() {
        Plugin plugin = plugins.get("Notification");
        return Optional.ofNullable(plugin instanceof NotificationPlugin ? (NotificationPlugin) plugin : null);
    }
}
