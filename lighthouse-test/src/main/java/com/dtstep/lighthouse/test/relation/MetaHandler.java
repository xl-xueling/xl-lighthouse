package com.dtstep.lighthouse.test.relation;

import com.dtstep.lighthouse.common.modal.MetaTable;
import com.dtstep.lighthouse.core.storage.cmdb.CMDBStorageEngineProxy;
import com.dtstep.lighthouse.core.storage.warehouse.WarehouseStorageEngineProxy;
import com.dtstep.lighthouse.core.wrapper.MetaTableWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class MetaHandler {

    private static final Logger logger = LoggerFactory.getLogger(MetaHandler.class);

    public static Integer createMeta(Connection conn,MetaTable metaTable) throws Exception {
        String metaName = metaTable.getMetaName();
        try{
            WarehouseStorageEngineProxy.getInstance().createResultTable(metaName);
            logger.info("create stat storage table,create hbase table success,metaName:{}",metaName);
        }catch (Exception ex){
            logger.error("create stat storage table,create hbase table error,metaName:{}",metaName,ex);
            throw ex;
        }

        String insertSql = "INSERT INTO ldp_metas (meta_name,type,state,create_time,update_time) VALUES (?, ?, ?, ?, ?)";
        QueryRunner queryRunner = new QueryRunner();
        BigInteger metaId = queryRunner.insert(conn, insertSql, new ScalarHandler<>(),
                metaTable.getMetaName(),
                metaTable.getMetaTableType().getType(),
                metaTable.getState().getState(),
                metaTable.getCreateTime(),
                metaTable.getUpdateTime());
        return metaId.intValue();
    }

    private static Integer queryDBValidMetaId() throws Exception {
        Connection conn;
        try {
            conn = CMDBStorageEngineProxy.getInstance().getConnection();
            QueryRunner qr = new QueryRunner();
            List<Map<String, Object>> list = qr.query(conn,
                    "select id from ldp_metas where `state` = ? and `type` = ? order by create_time desc limit 1",new MapListHandler(),1,3);
            if(CollectionUtils.isEmpty(list)){
                return -1;
            }
            Map<String,Object> infoMap = list.get(0);
            return (Integer) infoMap.get("id");
        }catch (Exception ex){
            logger.error("query valid meta info error!",ex);
            ex.printStackTrace();
            throw ex;
        }finally {
            CMDBStorageEngineProxy.getInstance().closeConnection();
        }
    }

    public static Integer queryValidMetaId() throws Exception {
        Integer metaId = MetaHandler.queryDBValidMetaId();
        if(metaId == -1){
            metaId = MetaTableWrapper.createStatStorageAndMetaTable();
        }
        return metaId;
    }
}
