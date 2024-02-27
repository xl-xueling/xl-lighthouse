package com.dtstep.lighthouse.core.test;

import com.dtstep.lighthouse.common.enums.GroupStateEnum;
import com.dtstep.lighthouse.common.enums.StatStateEnum;
import com.dtstep.lighthouse.common.modal.Column;
import com.dtstep.lighthouse.common.modal.Group;
import com.dtstep.lighthouse.common.modal.Stat;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.dao.ConnectionManager;
import com.dtstep.lighthouse.core.dao.DBConnection;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import com.dtstep.lighthouse.core.wrapper.StatDBWrapper;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DBUtilsTest {

    @Test
    public void testQueryStat() throws Exception {
        LDPConfig.initWithHomePath("/Users/xueling/lighthouse");
        DBConnection dbConnection = ConnectionManager.getConnection();
        Connection conn = dbConnection.getConnection();
        System.out.println("conn:" + conn);
        QueryRunner queryRunner = new QueryRunner();
        ResultSetHandler<Stat> handler = new BeanHandler<Stat>(Stat.class);
        try{
            Stat stat = queryRunner.query(conn, "select * from ldp_stats where id = '1100507'", new ResultSetHandler<Stat>() {
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
                        Long expired = rs.getLong("expired");
                        Integer state = rs.getInt("state");
                        String renderConfig = rs.getString("render_config");
                        Integer metaId = rs.getInt("meta_id");
                        Long createTime = rs.getTimestamp("create_time").getTime();
                        Long updateTime = rs.getTimestamp("update_time").getTime();
                        String randomId = rs.getString("random_id");
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
                        stat.setRandomId(randomId);
                    }
                    return stat;
                }
            });
            System.out.println("stat is:" + stat);
        }finally {
            ConnectionManager.close(dbConnection);
        }
    }

    @Test
    public void testQueryStatByGroupId() throws Exception {
        LDPConfig.initWithHomePath("/Users/xueling/lighthouse");
        List<Stat> statList = StatDBWrapper.queryStatByGroupIDFromDB(100264);
        System.out.println("statList is:" + JsonUtil.toJSONString(statList));
    }

    @Test
    public void testQueryGroup() throws Exception {
        LDPConfig.initWithHomePath("/Users/xueling/lighthouse");
        DBConnection dbConnection = ConnectionManager.getConnection();
        Connection conn = dbConnection.getConnection();
        System.out.println("conn:" + conn);
        QueryRunner queryRunner = new QueryRunner();
        try{
            Group group = queryRunner.query(conn, "select * from ldp_groups where id = '100282'", new ResultSetHandler<Group>() {
                @Override
                public Group handle(ResultSet rs) throws SQLException {
                    Group group = null;
                    if(rs.next()){
                        group = new Group();
                        Integer id = rs.getInt("id");
                        String token = rs.getString("token");
                        Integer projectId = rs.getInt("project_id");
                        Integer debugMode = rs.getInt("debug_mode");
                        String columns = rs.getString("columns");
                        String desc = rs.getString("desc");
                        String secretKey = rs.getString("secret_key");
                        Long createTime = rs.getTimestamp("create_time").getTime();
                        Long updateTime = rs.getTimestamp("update_time").getTime();
                        Integer state = rs.getInt("state");
                        Long refreshTime = rs.getTimestamp("refresh_time").getTime();
                        group.setId(id);
                        group.setToken(token);
                        group.setProjectId(projectId);
                        group.setDebugMode(debugMode);
                        group.setDesc(desc);
                        group.setSecretKey(secretKey);
                        group.setCreateTime(DateUtil.timestampToLocalDateTime(createTime));
                        group.setUpdateTime(DateUtil.timestampToLocalDateTime(updateTime));
                        GroupStateEnum statStateEnum = GroupStateEnum.forValue(state);
                        group.setState(statStateEnum);
                        List<Column> columnList = JsonUtil.toJavaObjectList(columns,Column.class);
                        group.setColumns(columnList);
                        group.setRefreshTime(DateUtil.timestampToLocalDateTime(refreshTime));
                    }
                    return group;
                }
            });
            System.out.println("group is:" + JsonUtil.toJSONString(group));
        }finally {
            ConnectionManager.close(dbConnection);
        }
    }
}
