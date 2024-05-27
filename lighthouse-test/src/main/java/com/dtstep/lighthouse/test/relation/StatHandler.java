package com.dtstep.lighthouse.test.relation;

import com.dtstep.lighthouse.common.enums.StatStateEnum;
import com.dtstep.lighthouse.common.modal.Stat;
import com.dtstep.lighthouse.common.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StatHandler {

    private static final Logger logger = LoggerFactory.getLogger(StatHandler.class);

    public static List<Integer> createStats(Connection connection, List<Stat> statList) throws Exception {
        String insertSql = "INSERT INTO ldp_stats (title,group_id,project_id,template,timeparam,expired,`state`,meta_id,`desc`,create_time,refresh_time,update_time,random_id) " +
                "VALUES (?, ?, ?, ?, ?,?, ?, ?, ?,?, ?, ?,?)";
        PreparedStatement ps = null;
        ResultSet generatedKeys = null;
        List<Integer> ids = new ArrayList<>();
        try{
            ps = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
            for(Stat stat : statList) {
                long t = DateUtil.translateToTimeStamp(stat.getUpdateTime());
                ps.setString(1, stat.getTitle());
                ps.setInt(2, stat.getGroupId());
                ps.setInt(3, stat.getProjectId());
                ps.setString(4, stat.getTemplate());
                ps.setString(5, stat.getTimeparam());
                ps.setLong(6, stat.getExpired());
                ps.setInt(7, stat.getState().getState());
                ps.setInt(8, stat.getMetaId());
                ps.setString(9, stat.getDesc());
                ps.setTimestamp(10, new Timestamp(t));
                ps.setTimestamp(11, new Timestamp(t));
                ps.setTimestamp(12, new Timestamp(t));
                ps.setString(13, stat.getRandomId());
                ps.addBatch();
            }
            ps.executeBatch();
            ps.clearBatch();
            generatedKeys = ps.getGeneratedKeys();
            while (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                ids.add(id);
            }
        }catch (Exception ex){
            logger.error("insert stats info error!",ex);
            throw ex;
        }finally {
            if(generatedKeys != null){
                generatedKeys.close();
            }
            if(ps != null){
                ps.close();
            }
        }
        return ids;
    }

    public static void deleteByGroupId(Connection connection,Integer groupId) throws Exception {
        String sql = "DELETE FROM ldp_stats WHERE group_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            ps.executeUpdate();
        } catch (Exception ex) {
            logger.error("delete mysql data error!", ex);
            ex.printStackTrace();
        }
    }

    public static void stopByGroupId(Connection connection,Integer groupId) throws Exception {
        String sql = "UPDATE ldp_stats SET `state` = ? WHERE group_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, StatStateEnum.STOPPED.getState());
            ps.setInt(2, groupId);
            ps.executeUpdate();
        } catch (Exception ex) {
            logger.error("stopByGroupId data error!", ex);
            ex.printStackTrace();
        }
    }

    public static void startByGroupId(Connection connection,Integer groupId) throws Exception {
        String sql = "UPDATE ldp_stats SET `state` = ? WHERE group_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, StatStateEnum.RUNNING.getState());
            ps.setInt(2, groupId);
            ps.executeUpdate();
        } catch (Exception ex) {
            logger.error("startByGroupId data error!", ex);
            ex.printStackTrace();
        }
    }
}
