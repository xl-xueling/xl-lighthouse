package com.dtstep.lighthouse.core.storage.engine;

import com.dtstep.lighthouse.core.storage.Get;
import com.dtstep.lighthouse.core.storage.Put;
import com.dtstep.lighthouse.core.storage.Result;

import java.util.List;

public interface StorageEngine {

    void createNamespace(String namespace) throws Exception;

    void createTable(String tableName) throws Exception;

    void dropTable(String tableName) throws Exception;

    void put(String tableName, Put put) throws Exception;

    void puts(String tableName, List<Put> putList) throws Exception;

    void maxPuts(String tableName,List<Put> putList) throws Exception;

    void minPuts(String tableName,List<Put> putList) throws Exception;

    Result get(String tableName, Get get) throws Exception;

    List<Result> gets(String tableName, List<Get> gets) throws Exception;

    List<Result> scan(String tableName,String startRow,String endRow,int limit) throws Exception;

    void delete(String tableName,String key) throws Exception;

}
