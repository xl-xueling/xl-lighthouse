package com.dtstep.lighthouse.test.example;

import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.modal.Group;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.storage.cmdb.CMDBStorageEngineProxy;
import com.dtstep.lighthouse.test.relation.GroupHandler;
import com.dtstep.lighthouse.test.relation.StatHandler;

import java.sql.Connection;

public class StopExample {

    public static void main(String[] args) throws Exception {
        LDPConfig.loadConfiguration();
        stopExample();
    }

    public static void stopExample() throws Exception {
        Group groupInfo = GroupHandler.queryGroupInfo(SysConst.TEST_SCENE_BEHAVIOR_STAT);
        if(groupInfo == null){
            return;
        }
        Connection conn = null;
        try {
            conn = CMDBStorageEngineProxy.getInstance().getConnection();
            conn.setAutoCommit(false);
            StatHandler.stopByGroupId(conn,groupInfo.getId());
            conn.commit();
        }catch (Exception ex){
            ex.printStackTrace();
            if(conn != null){
                conn.rollback();
            }
        }finally {
            CMDBStorageEngineProxy.getInstance().closeConnection();
        }
    }
}
