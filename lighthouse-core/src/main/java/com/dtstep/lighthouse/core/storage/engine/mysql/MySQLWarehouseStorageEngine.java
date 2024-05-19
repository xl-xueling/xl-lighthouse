package com.dtstep.lighthouse.core.storage.engine.mysql;

import com.dtstep.lighthouse.core.storage.*;
import com.dtstep.lighthouse.core.storage.engine.WarehouseStorageEngine;

import java.util.List;

public class MySQLWarehouseStorageEngine implements WarehouseStorageEngine {

    @Override
    public String getDefaultNamespace() {
        return null;
    }

    @Override
    public void createNamespaceIfNotExist(String namespace) throws Exception {
        return;
    }

    @Override
    public void createTable(String tableName) throws Exception {

    }

    @Override
    public boolean isTableExist(String tableName) throws Exception {
        return false;
    }

    @Override
    public void dropTable(String tableName) throws Exception {

    }

    @Override
    public void put(String tableName, LdpPut ldpPut) throws Exception {

    }

    @Override
    public void puts(String tableName, List<LdpPut> ldpPuts) throws Exception {

    }

    @Override
    public void increment(String tableName, LdpIncrement ldpIncrement) throws Exception {

    }

    @Override
    public void increments(String tableName, List<LdpIncrement> ldpIncrements) throws Exception {

    }

    @Override
    public void putsWithCompare(String tableName, CompareOperator compareOperator, List<LdpPut> ldpPuts) throws Exception {

    }

    @Override
    public <R> LdpResult<R> get(String tableName, LdpGet ldpGet, Class<R> clazz) throws Exception {
        return null;
    }

    @Override
    public <R> List<LdpResult<R>> gets(String tableName, List<LdpGet> ldpGets, Class<R> clazz) throws Exception {
        return null;
    }

    @Override
    public <R> List<LdpResult<R>> scan(String tableName, String startRow, String endRow, int limit, Class<R> clazz) throws Exception {
        return null;
    }

    @Override
    public void delete(String tableName, String key) throws Exception {

    }

    @Override
    public long getMaxRecordSize() {
        return 0;
    }

    @Override
    public long getMaxContentSize() {
        return 0;
    }

    @Override
    public long getMaxTimeInterval() {
        return 0;
    }

    @Override
    public long getRecordSize(String tableName) {
        return 0;
    }

    @Override
    public long getContentSize(String tableName) {
        return 0;
    }
}
