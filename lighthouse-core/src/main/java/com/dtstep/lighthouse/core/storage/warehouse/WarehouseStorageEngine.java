package com.dtstep.lighthouse.core.storage.warehouse;

import com.dtstep.lighthouse.core.storage.*;

import java.util.List;

public interface WarehouseStorageEngine {

    String getDefaultNamespace();

    void createNamespaceIfNotExist(String namespace) throws Exception;

    void createResultTable(String tableName) throws Exception;

    void createDimensTable(String tableName) throws Exception;

    boolean isTableExist(String tableName) throws Exception;

    void dropTable(String tableName) throws Exception;

    void put(String tableName, LdpPut ldpPut) throws Exception;

    void puts(String tableName, List<LdpPut> ldpPuts) throws Exception;

    void increment(String tableName, LdpIncrement ldpIncrement) throws Exception;

    void increments(String tableName,List<LdpIncrement> ldpIncrements) throws Exception;

    void putsWithCompare(String tableName, CompareOperator compareOperator,List<LdpPut> ldpPuts) throws Exception;

    <R> LdpResult<R> get(String tableName, LdpGet ldpGet, Class<R> clazz) throws Exception;

    <R> List<LdpResult<R>> gets(String tableName, List<LdpGet> ldpGets, Class<R> clazz) throws Exception;

    <R> List<LdpResult<R>> scan(String tableName, String startRow, String endRow, int limit,Class<R> clazz) throws Exception;

    void delete(String tableName,String key) throws Exception;

    boolean isWritable(String tableName) throws Exception;

}
