package com.dtstep.lighthouse.core.storage.engine;

import com.dtstep.lighthouse.core.storage.LdpGet;
import com.dtstep.lighthouse.core.storage.LdpIncrement;
import com.dtstep.lighthouse.core.storage.LdpPut;
import com.dtstep.lighthouse.core.storage.Result;

import java.util.List;

public interface StorageEngine {

    void createNamespace(String namespace) throws Exception;

    void createTable(String tableName) throws Exception;

    boolean isTableExist(String tableName) throws Exception;

    void dropTable(String tableName) throws Exception;

    void put(String tableName, LdpPut ldpPut) throws Exception;

    void puts(String tableName, List<LdpPut> ldpPuts) throws Exception;

    void increment(String tableName, LdpIncrement ldpIncrement) throws Exception;

    void increments(String tableName,List<LdpIncrement> ldpIncrements) throws Exception;

    void maxPuts(String tableName,List<LdpPut> ldpPuts) throws Exception;

    void minPuts(String tableName,List<LdpPut> ldpPuts) throws Exception;

    <R> Result<R> get(String tableName, LdpGet ldpGet,Class<R> clazz) throws Exception;

    <R> List<Result<R>> gets(String tableName, List<LdpGet> ldpGets) throws Exception;

    <R> List<Result<R>> scan(String tableName,String startRow,String endRow,int limit) throws Exception;

    void delete(String tableName,String key) throws Exception;

}
