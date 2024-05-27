package com.dtstep.lighthouse.test.relation;

import com.dtstep.lighthouse.common.modal.Project;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ProjectHandler {

    private static final Logger logger = LoggerFactory.getLogger(ProjectHandler.class);

    public static Integer createProject(Connection connection,Project project) throws Exception {
        try {
            String insertSql = "INSERT INTO ldp_projects (title,department_id,private_type,`desc`,`create_time`, `update_time`) VALUES (?, ?, ?,?, ?, ?)";
            QueryRunner queryRunner = new QueryRunner();
            BigInteger projectId = queryRunner.insert(connection, insertSql, new ScalarHandler<>(), project.getTitle(), project.getDepartmentId(),
                    project.getPrivateType().getPrivateType()
                    ,project.getDesc(),project.getCreateTime(),project.getUpdateTime());
            return projectId.intValue();
        } catch (Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }

    public static void delete(Connection connection,Integer projectId) throws Exception {
        String sql = "DELETE FROM ldp_projects WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, projectId);
            ps.executeUpdate();
        } catch (Exception ex) {
            logger.error("delete mysql data error!", ex);
            ex.printStackTrace();
        }
    }
}
