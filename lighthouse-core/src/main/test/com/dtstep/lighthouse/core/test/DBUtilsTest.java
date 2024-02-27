package com.dtstep.lighthouse.core.test;

import com.dtstep.lighthouse.common.enums.StatStateEnum;
import com.dtstep.lighthouse.common.modal.Stat;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.dao.ConnectionManager;
import com.dtstep.lighthouse.core.dao.DBConnection;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
