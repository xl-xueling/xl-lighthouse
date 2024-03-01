package com.dtstep.lighthouse.core.storage.engine.hbase;

import com.dtstep.lighthouse.core.storage.Get;
import com.dtstep.lighthouse.core.storage.Put;
import com.dtstep.lighthouse.core.storage.Result;
import com.dtstep.lighthouse.core.storage.engine.StorageEngine;

import java.util.List;

public class HBaseStorageEngine implements StorageEngine {

    @Override
    public int createNamespace(String namespace) throws Exception {

        return 0;
    }

    @Override
    public int createTable(String tableName) throws Exception {
        return 0;
    }

    @Override
    public int dropTable(String tableName) throws Exception {
        return 0;
    }

    @Override
    public int put(String tableName, Put put) throws Exception {
        return 0;
    }

    @Override
    public int puts(String tableName, List<Put> putList) throws Exception {
        return 0;
    }

    @Override
    public Result get(String tableName, Get get) throws Exception {
        return null;
    }

    @Override
    public List<Result> gets(String tableName, List<Get> gets) throws Exception {
        return null;
    }

    @Override
    public List<Result> scan(String tableName, String startRow, String endRow, int limit) throws Exception {
        return null;
    }

    @Override
    public int delete(String tableName, String key) throws Exception {
        return 0;
    }
}
