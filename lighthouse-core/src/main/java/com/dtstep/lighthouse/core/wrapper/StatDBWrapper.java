package com.dtstep.lighthouse.core.wrapper;

import com.dtstep.lighthouse.common.entity.ServiceResult;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.stat.TemplateEntity;
import com.dtstep.lighthouse.common.entity.state.StatState;
import com.dtstep.lighthouse.common.entity.state.StatUnit;
import com.dtstep.lighthouse.common.enums.GroupStateEnum;
import com.dtstep.lighthouse.common.enums.StatStateEnum;
import com.dtstep.lighthouse.common.modal.Column;
import com.dtstep.lighthouse.common.modal.RenderConfig;
import com.dtstep.lighthouse.common.modal.Stat;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.builtin.BuiltinLoader;
import com.dtstep.lighthouse.core.dao.ConnectionManager;
import com.dtstep.lighthouse.core.dao.DBConnection;
import com.dtstep.lighthouse.core.formula.FormulaTranslate;
import com.dtstep.lighthouse.core.template.TemplateContext;
import com.dtstep.lighthouse.core.template.TemplateParser;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class StatDBWrapper {

    private static final Logger logger = LoggerFactory.getLogger(StatDBWrapper.class);

    private static final LoadingCache<Integer, Optional<StatExtEntity>> statCache = Caffeine.newBuilder()
            .expireAfterWrite(3, TimeUnit.MINUTES)
            .softValues()
            .maximumSize(100000)
            .build(StatDBWrapper::actualQueryById);

    private final static LoadingCache<Integer, Optional<List<StatExtEntity>>> groupStatListCache = Caffeine.newBuilder()
            .expireAfterWrite(3, TimeUnit.MINUTES)
            .maximumSize(100000)
            .softValues()
            .build(StatDBWrapper::actualQueryListByGroupId);

    public static StatExtEntity queryById(int statId) {
        return Objects.requireNonNull(statCache.get(statId)).orElse(null);
    }

    public static Optional<StatExtEntity> actualQueryById(int statId) {
        if(BuiltinLoader.isBuiltinStat(statId)){
            return Optional.ofNullable(BuiltinLoader.getBuiltinStat(statId));
        }
        StatExtEntity statExtEntity = null;
        try{
            Stat statEntity = queryStatByIdFromDB(statId);
            if(statEntity != null){
                statExtEntity = combineExtInfo(statEntity,false);
            }
            return Optional.ofNullable(statExtEntity);
        }catch (Exception ex){
            logger.error("query stat info error,id:{}",statId,ex);
            return Optional.empty();
        }
    }

    private static class StatResultListHandler implements ResultSetHandler<List<Stat>> {

        @Override
        public List<Stat> handle(ResultSet rs) throws SQLException {
            List<Stat> statList = new ArrayList<>();
            while (rs.next()){
                Stat stat = new Stat();
                Integer id = rs.getInt("id");
                String title = rs.getString("title");
                Integer groupId = rs.getInt("group_id");
                Integer projectId = rs.getInt("project_id");
                Integer dataVersion = rs.getInt("data_version");
                String template = rs.getString("template");
                String timeparam = rs.getString("timeparam");
                Long expired = rs.getLong("expired");
                int state = rs.getInt("state");
                String renderConfig = rs.getString("render_config");
                Integer metaId = rs.getInt("meta_id");
                long createTime = rs.getTimestamp("create_time").getTime();
                long updateTime = rs.getTimestamp("update_time").getTime();
                String randomId = rs.getString("random_id");
                String columns = rs.getString("columns");
                stat.setId(id);
                stat.setTitle(title);
                stat.setGroupId(groupId);
                stat.setProjectId(projectId);
                stat.setTemplate(template);
                stat.setTimeparam(timeparam);
                stat.setDataVersion(dataVersion);
                stat.setExpired(expired);
                if(StringUtil.isNotEmpty(renderConfig)){
                    stat.setRenderConfig(JsonUtil.toJavaObject(renderConfig, RenderConfig.class));
                }
                StatStateEnum statStateEnum = StatStateEnum.getByState(state);
                stat.setState(statStateEnum);
                stat.setMetaId(metaId);
                stat.setCreateTime(DateUtil.timestampToLocalDateTime(createTime));
                stat.setUpdateTime(DateUtil.timestampToLocalDateTime(updateTime));
                stat.setRandomId(randomId);
                stat.setGroupColumns(columns);
                statList.add(stat);
            }
            return statList;
        }
    }

    private static class StatResultSetHandler implements ResultSetHandler<Stat> {

        @Override
        public Stat handle(ResultSet rs) throws SQLException {
            Stat stat = null;
            if(rs.next()){
                stat = new Stat();
                Integer id = rs.getInt("id");
                String title = rs.getString("title");
                Integer groupId = rs.getInt("group_id");
                Integer projectId = rs.getInt("project_id");
                String template = rs.getString("template");
                String timeparam = rs.getString("timeparam");
                Integer dataVersion = rs.getInt("data_version");
                Long expired = rs.getLong("expired");
                int state = rs.getInt("state");
                String renderConfig = rs.getString("render_config");
                Integer metaId = rs.getInt("meta_id");
                long createTime = rs.getTimestamp("create_time").getTime();
                long updateTime = rs.getTimestamp("update_time").getTime();
                long refreshTime = rs.getTimestamp("refresh_time").getTime();
                String randomId = rs.getString("random_id");
                String columns = rs.getString("columns");
                stat.setId(id);
                stat.setTitle(title);
                stat.setGroupId(groupId);
                stat.setProjectId(projectId);
                stat.setTemplate(template);
                stat.setTimeparam(timeparam);
                stat.setExpired(expired);
                StatStateEnum statStateEnum = StatStateEnum.getByState(state);
                stat.setState(statStateEnum);
                stat.setMetaId(metaId);
                stat.setCreateTime(DateUtil.timestampToLocalDateTime(createTime));
                stat.setUpdateTime(DateUtil.timestampToLocalDateTime(updateTime));
                stat.setRefreshTime(DateUtil.timestampToLocalDateTime(refreshTime));
                stat.setRandomId(randomId);
                stat.setGroupColumns(columns);
                stat.setDataVersion(dataVersion);
                if(StringUtil.isNotEmpty(renderConfig)){
                    stat.setRenderConfig(JsonUtil.toJavaObject(renderConfig, RenderConfig.class));
                }
            }
            return stat;
        }
    }

    private static Stat queryStatByIdFromDB(int statId) throws Exception {
        DBConnection dbConnection = ConnectionManager.getConnection();
        Connection conn = dbConnection.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        ResultSetHandler<Stat> handler = new BeanHandler<Stat>(Stat.class);
        Stat stat = null;
        try{
            stat = queryRunner.query(conn, String.format("select a.*,b.columns from ldp_stats a left join ldp_groups b on a.group_id = b.id where a.id = '%s'",statId), new StatResultSetHandler());
        }finally {
            ConnectionManager.close(dbConnection);
        }
        return stat;
    }

    public static List<Stat> queryStatByGroupIDFromDB(int groupId) throws Exception {
        DBConnection dbConnection = ConnectionManager.getConnection();
        Connection conn = dbConnection.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        List<Stat> statList;
        try{
            statList = queryRunner.query(conn, String.format("select a.*,b.columns from ldp_stats a left join ldp_groups b on a.group_id = b.id where a.group_id = '%s'",groupId), new StatResultListHandler());
        }finally {
            ConnectionManager.close(dbConnection);
        }
        return statList;
    }

    public static Optional<List<StatExtEntity>> actualQueryListByGroupId(int groupId) throws Exception {
        List<Stat> entityList = queryStatByGroupIDFromDB(groupId);
        if(CollectionUtils.isEmpty(entityList)){
            return Optional.empty();
        }
        List<StatExtEntity> extEntityList = new ArrayList<>();
        entityList.forEach(z -> {
            try{
                StatExtEntity statExtEntity = combineExtInfo(z,false);
                extEntityList.add(statExtEntity);
            }catch (Exception ex){
                logger.error("query stat info error,id:{}",z.getId(),ex);
            }
        });
        return Optional.of(extEntityList);
    }

    public static StatExtEntity combineExtInfo(Stat statEntity,boolean isBuiltIn) throws Exception {
        String groupColumns = statEntity.getGroupColumns();
        assert StringUtil.isNotEmpty(groupColumns);
        List<Column> groupColumnList = JsonUtil.toJavaObjectList(groupColumns,Column.class);
        StatExtEntity statExtEntity = new StatExtEntity(statEntity);
        String timeParam = statExtEntity.getTimeparam();
        String[] timeParamArr = timeParam.split("-");
        statExtEntity.setTimeParamInterval(Integer.parseInt(timeParamArr[0]));
        if("minute".equals(timeParamArr[1])){
            statExtEntity.setTimeUnit(TimeUnit.MINUTES);
        }else if("hour".equals(timeParamArr[1])){
            statExtEntity.setTimeUnit(TimeUnit.HOURS);
        }else if("day".equals(timeParamArr[1])){
            statExtEntity.setTimeUnit(TimeUnit.DAYS);
        }else if("second".equals(timeParamArr[1])){
            statExtEntity.setTimeUnit(TimeUnit.SECONDS);
        }
        StatStateEnum stateEnum = statExtEntity.getState();
        if(!isBuiltIn){
            GroupStateEnum groupStateEnum = GroupDBWrapper.getState(statEntity.getGroupId());
            if(groupStateEnum == GroupStateEnum.LIMITING && stateEnum == StatStateEnum.RUNNING){
                statExtEntity.setState(StatStateEnum.LIMITING);
            }else if(StatExtEntity.isLimitedExpired(statExtEntity)){
                changeState(statEntity.getId(),StatStateEnum.RUNNING,LocalDateTime.now());
            }
        }
        String template = statExtEntity.getTemplate();
        ServiceResult<TemplateEntity> serviceResult = TemplateParser.parseConfig(new TemplateContext(statEntity.getId(),template,timeParam,groupColumnList));
        TemplateEntity templateEntity = serviceResult.getData();
        statExtEntity.setTemplateEntity(templateEntity);
        List<Column> relatedColumns = new ArrayList<>();
        String stat = templateEntity.getStat();
        List<Column> statRelatedColumns = FormulaTranslate.queryRelatedColumns(groupColumnList,stat);
        if(CollectionUtils.isNotEmpty(statRelatedColumns)){
            relatedColumns.addAll(statRelatedColumns);
        }
        for(StatState statState : templateEntity.getStatStateList()){
            List<Column> stateRelatedColumns = new ArrayList<>();
            List<StatUnit> unitList = statState.getUnitList();
            if(CollectionUtils.isNotEmpty(unitList)){
                int index = StatState.isBitCountState(statState) ? 1 : 0;
                for(int i = index;i<unitList.size(); i++){
                    StatUnit statUnit = unitList.get(i);
                    List<Column> unitRelatedColumns = FormulaTranslate.queryRelatedColumns(groupColumnList,statUnit.getOrigin());
                    if(CollectionUtils.isNotEmpty(unitRelatedColumns)){
                        stateRelatedColumns.addAll(unitRelatedColumns);
                    }
                }
            }
            statState.setBuiltIn(isBuiltIn);
            statState.setStatId(statExtEntity.getId());
            statState.setGroupId(statExtEntity.getGroupId());
            statState.setRelatedColumnSet(stateRelatedColumns.stream().map(Column::getName).collect(Collectors.toSet()));
        }
        String dimens = templateEntity.getDimens();
        if(!StringUtil.isEmpty(dimens)){
            List<Column> dimensRelatedColumns = FormulaTranslate.queryRelatedColumns(groupColumnList,dimens);
            if(!dimensRelatedColumns.isEmpty()){
                relatedColumns.addAll(dimensRelatedColumns);
            }
        }
        statExtEntity.setRelatedColumnSet(relatedColumns.stream().map(Column::getName).collect(Collectors.toSet()));
        return statExtEntity;
    }

    public static int changeState(int statId, StatStateEnum statStateEnum, LocalDateTime date) throws Exception {
        DBConnection dbConnection = ConnectionManager.getConnection();
        Connection conn = dbConnection.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        int result;
        try{
            if(statStateEnum == StatStateEnum.LIMITING){
                result = queryRunner.update(conn, "update ldp_stats set state = ?,refresh_time = ? where id = ? and state = ?", statStateEnum.getState(),date, statId,StatStateEnum.RUNNING.getState());
            }else{
                result = queryRunner.update(conn, "update ldp_stats set state = ?,refresh_time = ? where id = ?", statStateEnum.getState(),date, statId);
            }
        }finally {
            ConnectionManager.close(dbConnection);
        }
        return result;
    }

    public static List<StatExtEntity> queryListByGroupId(int groupId) {
        return Objects.requireNonNull(groupStatListCache.get(groupId)).orElse(null);
    }

    public static List<StatExtEntity> queryRunningListByGroupId(int groupId){
        List<StatExtEntity> entityList = queryListByGroupId(groupId);
        if(CollectionUtils.isEmpty(entityList)){
            return null;
        }
        return entityList.stream().filter(x -> x.getState() == StatStateEnum.RUNNING).collect(Collectors.toList());
    }
}
