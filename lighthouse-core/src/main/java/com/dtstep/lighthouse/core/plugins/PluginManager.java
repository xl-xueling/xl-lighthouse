package com.dtstep.lighthouse.core.plugins;

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
