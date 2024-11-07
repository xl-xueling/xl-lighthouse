package com.dtstep.lighthouse.core.wrapper;

import com.dtstep.lighthouse.common.entity.AlarmExtEntity;
import com.dtstep.lighthouse.common.entity.AlarmTemplateExtEntity;
import com.dtstep.lighthouse.common.enums.AlarmMatchEnum;
import com.dtstep.lighthouse.common.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.common.modal.Alarm;
import com.dtstep.lighthouse.common.modal.AlarmCondition;
import com.dtstep.lighthouse.common.modal.AlarmTemplate;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.storage.cmdb.CMDBStorageEngine;
import com.dtstep.lighthouse.core.storage.cmdb.CMDBStorageEngineProxy;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class AlarmDBWrapper {

    private static final Logger logger = LoggerFactory.getLogger(AlarmDBWrapper.class);

    private static final Integer _CacheExpireMinutes = 5;

    private static final CMDBStorageEngine<Connection> storageEngine = CMDBStorageEngineProxy.getInstance();

    private static final Cache<Integer, Optional<AlarmExtEntity>> ALARM_CACHE = Caffeine.newBuilder()
            .expireAfterWrite(_CacheExpireMinutes, TimeUnit.MINUTES)
            .maximumSize(100000)
            .softValues()
            .build();

    private static final Cache<Integer, Optional<List<AlarmExtEntity>>> STAT_ALARM_CACHE = Caffeine.newBuilder()
            .expireAfterWrite(_CacheExpireMinutes, TimeUnit.MINUTES)
            .maximumSize(100000)
            .softValues()
            .build();

    public static AlarmExtEntity queryById(Integer id){
        Optional<AlarmExtEntity> optional =  ALARM_CACHE.get(id, k -> actualQueryById(id));
        assert optional != null;
        return optional.orElse(null);
    }

    public static List<AlarmExtEntity> queryByStatId(Integer statId){
        Optional<List<AlarmExtEntity>> optional =  STAT_ALARM_CACHE.get(statId, k -> actualQueryByStatId(statId));
        assert optional != null;
        return optional.orElse(null);
    }

    private static Optional<AlarmExtEntity> actualQueryById(Integer id) {
        AlarmExtEntity alarmExtEntity = null;
        try{
            Alarm alarm = queryAlarmFromDB(id);
            alarmExtEntity = translate(alarm);
        }catch (Exception ex){
            logger.error("query alarm info error!", ex);
        }
        return Optional.ofNullable(alarmExtEntity);
    }

    public static AlarmExtEntity translate(Alarm alarm) throws Exception {
        if(alarm == null){
            return null;
        }
        AlarmExtEntity alarmExtEntity = new AlarmExtEntity(alarm);
        int templateId = alarm.getTemplateId();
        if(templateId != 0){
            AlarmTemplate alarmTemplate = queryAlarmTemplateFromDB(templateId);
            AlarmTemplateExtEntity alarmTemplateExtEntity;
            if(alarmTemplate != null){
                String config = alarmTemplate.getConfig();
                AlarmTemplateExtEntity.AlarmTemplateConfig alarmTemplateConfig = JsonUtil.toJavaObject(config, AlarmTemplateExtEntity.AlarmTemplateConfig.class);
                alarmTemplateExtEntity = new AlarmTemplateExtEntity(alarmTemplate);
                alarmTemplateExtEntity.setTemplateConfig(alarmTemplateConfig);
                alarmExtEntity.setTemplateList(List.of(alarmTemplateExtEntity));
            }
        }
        return alarmExtEntity;
    }

    private static Optional<List<AlarmExtEntity>> actualQueryByStatId(Integer statId){
        List<AlarmExtEntity> alarmExtEntityList = new ArrayList<>();
        try{
            List<Alarm> alarmList = queryAlarmListFromDB(statId);
            if(CollectionUtils.isNotEmpty(alarmList)){
                for(Alarm alarm : alarmList){
                    AlarmExtEntity alarmExtEntity = new AlarmExtEntity(alarm);
                    int templateId = alarm.getTemplateId();
                    if(templateId != 0){
                        AlarmTemplate alarmTemplate = queryAlarmTemplateFromDB(templateId);
                        AlarmTemplateExtEntity alarmTemplateExtEntity;
                        if(alarmTemplate != null){
                            String config = alarmTemplate.getConfig();
                            AlarmTemplateExtEntity.AlarmTemplateConfig alarmTemplateConfig = JsonUtil.toJavaObject(config, AlarmTemplateExtEntity.AlarmTemplateConfig.class);
                            alarmTemplateExtEntity = new AlarmTemplateExtEntity(alarmTemplate);
                            alarmTemplateExtEntity.setTemplateConfig(alarmTemplateConfig);
                            alarmExtEntity.setTemplateList(List.of(alarmTemplateExtEntity));
                        }
                    }
                    alarmExtEntityList.add(alarmExtEntity);
                }
            }
        }catch (Exception ex){
            logger.error("query alarm info error!", ex);
        }
        return Optional.of(alarmExtEntityList);
    }

    private static Alarm queryAlarmFromDB(Integer id) throws Exception {
        Connection conn = storageEngine.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        Alarm alarm;
        try{
            alarm = queryRunner.query(conn, String.format("select `id`,`title`,`unique_code`,`divide`,`state`,`match`,`conditions`,`template_id`,`recover`,`delay`,`dimens`,`create_time`,`update_time` from ldp_alarms where id = '%s'",id), new AlarmSetHandler());
        }finally {
            storageEngine.closeConnection();
        }
        return alarm;
    }

    private static List<Alarm> queryAlarmListFromDB(Integer statId) throws Exception {
        Connection conn = storageEngine.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        List<Alarm> alarmList;
        try{
            alarmList = queryRunner.query(conn, String.format("select `id`,`title`,`unique_code`,`divide`,`state`,`match`,`conditions`,`template_id`,`recover`,`delay`,`dimens`,`create_time`,`update_time` from ldp_alarms where resource_id = '%s' and resource_type = '%s'",statId, ResourceTypeEnum.Stat.getResourceType()), new AlarmListSetHandler());
        }finally {
            storageEngine.closeConnection();
        }
        return alarmList;
    }

    private static AlarmTemplate queryAlarmTemplateFromDB(Integer id) throws Exception {
        Connection conn = storageEngine.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        AlarmTemplate alarmTemplate;
        try{
            alarmTemplate = queryRunner.query(conn, String.format("select `id`,`title`,`config`,`user_ids`,`department_ids`,`create_time`,`update_time` from ldp_alarm_templates where id = '%s'",id), new AlarmTemplateSetHandler());
        }finally {
            storageEngine.closeConnection();
        }
        return alarmTemplate;
    }


    private static class AlarmSetHandler implements ResultSetHandler<Alarm> {

        @Override
        public Alarm handle(ResultSet rs) throws SQLException {
            Alarm alarm = null;
            if(rs.next()){
                alarm = new Alarm();
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String uniqueCode = rs.getString("unique_code");
                boolean divide = rs.getBoolean("divide");
                boolean state = rs.getBoolean("state");
                int match = rs.getInt("match");
                Integer delay = rs.getInt("delay");
                String conditions = rs.getString("conditions");
                Integer templateId = rs.getInt("template_id");
                boolean recover = rs.getBoolean("recover");
                String dimens = rs.getString("dimens");
                long createTime = rs.getTimestamp("create_time").getTime();
                long updateTime = rs.getTimestamp("update_time").getTime();
                alarm.setId(id);
                alarm.setTitle(title);
                alarm.setUniqueCode(uniqueCode);
                alarm.setDelay(delay);
                alarm.setDivide(divide);
                alarm.setMatch(AlarmMatchEnum.forValue(match));
                alarm.setDimens(dimens);
                alarm.setState(state);
                List<AlarmCondition> conditionList = JsonUtil.toJavaObjectList(conditions,AlarmCondition.class);
                alarm.setConditions(conditionList);
                alarm.setCreateTime(DateUtil.timestampToLocalDateTime(createTime));
                alarm.setUpdateTime(DateUtil.timestampToLocalDateTime(updateTime));
                alarm.setTemplateId(templateId);
                alarm.setRecover(recover);
            }
            return alarm;
        }
    }


    private static class AlarmListSetHandler implements ResultSetHandler<List<Alarm>> {

        @Override
        public List<Alarm> handle(ResultSet rs) throws SQLException {
            List<Alarm> alarmList = new ArrayList<>();
            while (rs.next()){
                Alarm alarm = new Alarm();
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String uniqueCode = rs.getString("unique_code");
                boolean divide = rs.getBoolean("divide");
                boolean state = rs.getBoolean("state");
                int match = rs.getInt("match");
                Integer delay = rs.getInt("delay");
                String conditions = rs.getString("conditions");
                Integer templateId = rs.getInt("template_id");
                boolean recover = rs.getBoolean("recover");
                String dimens = rs.getString("dimens");
                long createTime = rs.getTimestamp("create_time").getTime();
                long updateTime = rs.getTimestamp("update_time").getTime();
                alarm.setId(id);
                alarm.setTitle(title);
                alarm.setUniqueCode(uniqueCode);
                alarm.setDelay(delay);
                alarm.setDivide(divide);
                alarm.setMatch(AlarmMatchEnum.forValue(match));
                alarm.setDimens(dimens);
                alarm.setState(state);
                List<AlarmCondition> conditionList = JsonUtil.toJavaObjectList(conditions,AlarmCondition.class);
                alarm.setConditions(conditionList);
                alarm.setCreateTime(DateUtil.timestampToLocalDateTime(createTime));
                alarm.setUpdateTime(DateUtil.timestampToLocalDateTime(updateTime));
                alarm.setTemplateId(templateId);
                alarm.setRecover(recover);
                alarmList.add(alarm);
            }
            return alarmList;
        }
    }


    private static class AlarmTemplateSetHandler implements ResultSetHandler<AlarmTemplate> {

        @Override
        public AlarmTemplate handle(ResultSet rs) throws SQLException {
            AlarmTemplate alarmTemplate = null;
            if(rs.next()) {
                alarmTemplate = new AlarmTemplate();
                int id = rs.getInt("id");
                String config = rs.getString("config");
                Object userIds = rs.getObject("user_ids");
                if(userIds != null){
                    List<Integer> userIdList = JsonUtil.toJavaObjectList(userIds.toString(),Integer.class);
                    alarmTemplate.setUserIds(userIdList);
                }
                Object departmentIds = rs.getObject("department_ids");
                if(departmentIds != null){
                    List<Integer> departmentIdList = JsonUtil.toJavaObjectList(departmentIds.toString(),Integer.class);
                    alarmTemplate.setDepartmentIds(departmentIdList);
                }
                alarmTemplate.setId(id);
                alarmTemplate.setConfig(config);
            }
            return alarmTemplate;
        }
    }

}
