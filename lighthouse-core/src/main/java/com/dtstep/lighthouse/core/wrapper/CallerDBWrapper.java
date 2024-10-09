package com.dtstep.lighthouse.core.wrapper;

import com.dtstep.lighthouse.common.entity.ResultCode;
import com.dtstep.lighthouse.common.modal.Caller;
import com.dtstep.lighthouse.common.util.DateUtil;
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
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class CallerDBWrapper {

    private static final Logger logger = LoggerFactory.getLogger(CallerDBWrapper.class);

    private static final Integer _CacheExpireMinutes = 5;

    private static final CMDBStorageEngine<Connection> storageEngine = CMDBStorageEngineProxy.getInstance();

    private static final Cache<Object, Optional<Caller>> CALLER_CACHE = Caffeine.newBuilder()
            .expireAfterWrite(_CacheExpireMinutes, TimeUnit.MINUTES)
            .maximumSize(100000)
            .softValues()
            .build();

    public static Caller queryByName(String callerName){
        Optional<Caller> optional =  CALLER_CACHE.get(callerName, k -> actualQueryByName(callerName));
        assert optional != null;
        return optional.orElse(null);
    }

    private static Optional<Caller> actualQueryByName(String callerName) {
        Caller caller = null;
        try{
            caller = queryCallerByNameFromDB(callerName);
        }catch (Exception ex){
            logger.error("query caller info error!", ex);
        }
        return Optional.ofNullable(caller);
    }

    private static Caller queryCallerByNameFromDB(String callerName) throws Exception {
        Connection conn = storageEngine.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        Caller caller;
        try{
            caller = queryRunner.query(conn, String.format("select * from ldp_callers where name = '%s'",callerName), new CallerSetHandler());
        }finally {
            storageEngine.closeConnection();
        }
        return caller;
    }

    private static class CallerSetHandler implements ResultSetHandler<Caller> {

        @Override
        public Caller handle(ResultSet rs) throws SQLException {
            Caller caller = null;
            if(rs.next()){
                caller = new Caller();
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String secretKey = rs.getString("secret_key");
                long createTime = rs.getTimestamp("create_time").getTime();
                long updateTime = rs.getTimestamp("update_time").getTime();
                caller.setId(id);
                caller.setName(name);
                caller.setSecretKey(secretKey);
                caller.setCreateTime(DateUtil.timestampToLocalDateTime(createTime));
                caller.setUpdateTime(DateUtil.timestampToLocalDateTime(updateTime));
            }
            return caller;
        }
    }

    public static ResultCode verify(String callerName,String secretKey) throws Exception {
        Caller caller = queryByName(callerName);
        if(caller == null){
            return ResultCode.getExtendResultCode(ResultCode.apiCallerNotExist,callerName);
        }
        if(!caller.getSecretKey().equals(secretKey)){
            return ResultCode.getExtendResultCode(ResultCode.apiCallerKeyIncorrect,callerName);
        }
        return ResultCode.success;
    }
}
