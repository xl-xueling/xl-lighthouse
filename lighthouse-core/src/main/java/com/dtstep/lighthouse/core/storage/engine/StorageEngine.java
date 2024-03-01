package com.dtstep.lighthouse.core.storage.engine;

import com.dtstep.lighthouse.core.storage.LdpGet;
import com.dtstep.lighthouse.core.storage.LdpPut;
import com.dtstep.lighthouse.core.storage.Result;

import java.util.List;

public interface StorageEngine {

    void createNamespace(String namespace) throws Exception;

    void createTable(String tableName) throws Exception;

    void dropTable(String tableName) throws Exception;

    void put(String tableName, LdpPut put) throws Exception;

    void puts(String tableName, List<LdpPut> putList) throws Exception;

    void maxPuts(String tableName,List<LdpPut> putList) throws Exception;

    void minPuts(String tableName,List<LdpPut> putList) throws Exception;

    Result get(String tableName, LdpGet get) throws Exception;

    List<Result> gets(String tableName, List<LdpGet> gets) throws Exception;

    List<Result> scan(String tableName,String startRow,String endRow,int limit) throws Exception;

    void delete(String tableName,String key) throws Exception;

}
