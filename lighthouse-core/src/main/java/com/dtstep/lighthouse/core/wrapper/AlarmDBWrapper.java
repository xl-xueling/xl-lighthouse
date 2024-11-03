package com.dtstep.lighthouse.core.wrapper;

import com.dtstep.lighthouse.common.entity.AlarmExtEntity;
import com.dtstep.lighthouse.common.enums.AlarmMatchEnum;
import com.dtstep.lighthouse.common.enums.CallerStateEnum;
import com.dtstep.lighthouse.common.modal.Alarm;
import com.dtstep.lighthouse.common.modal.AlarmCondition;
import com.dtstep.lighthouse.common.modal.Caller;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.storage.cmdb.CMDBStorageEngine;
import com.dtstep.lighthouse.core.storage.cmdb.CMDBStorageEngineProxy;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public static AlarmExtEntity queryById(Integer id){
        Optional<AlarmExtEntity> optional =  ALARM_CACHE.get(id, k -> actualQueryById(id));
        assert optional != null;
        return optional.orElse(null);
    }

    private static Optional<AlarmExtEntity> actualQueryById(Integer id) {
        AlarmExtEntity alarmExtEntity = null;
        try{
            Alarm alarm = queryAlarmFromDB(id);
        }catch (Exception ex){
            logger.error("query alarm info error!", ex);
        }
        return Optional.ofNullable(alarmExtEntity);
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
                System.out.println("alarm is:" + JsonUtil.toJSONString(alarm));
            }
            return alarm;
        }
    }



}