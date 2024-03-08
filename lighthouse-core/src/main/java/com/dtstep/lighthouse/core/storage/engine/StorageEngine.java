package com.dtstep.lighthouse.core.storage.engine;

import com.dtstep.lighthouse.core.storage.*;

import java.util.List;

public interface StorageEngine {

    String getDefaultNamespace();

    void createNamespaceIfNotExist(String namespace) throws Exception;

    void createTable(String tableName) throws Exception;

    boolean isTableExist(String tableName) throws Exception;

    void dropTable(String tableName) throws Exception;

    long getMaxRecordSize();

    long getMaxContentSize();

    long getMaxTimeInterval();

    long getRecordSize(String tableName);

    long getContentSize(String tableName);

    void put(String tableName, LdpPut ldpPut) throws Exception;

    void puts(String tableName, List<LdpPut> ldpPuts) throws Exception;

    void increment(String tableName, LdpIncrement ldpIncrement) throws Exception;

    void increments(String tableName,List<LdpIncrement> ldpIncrements) throws Exception;

    void putsWithCompare(String tableName, CompareOperator compareOperator,List<LdpPut> ldpPuts) throws Exception;

    <R> LdpResult<R> get(String tableName, LdpGet ldpGet, Class<R> clazz) throws Exception;

    <R> List<LdpResult<R>> gets(String tableName, List<LdpGet> ldpGets, Class<R> clazz) throws Exception;

    <R> List<LdpResult<R>> scan(String tableName, String startRow, String endRow, int limit,Class<R> clazz) throws Exception;

    void delete(String tableName,String key) throws Exception;

}
