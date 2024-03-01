package com.dtstep.lighthouse.core.storage.engine;

import java.util.List;

public interface StorageEngine {

    void put(String metaName, Put put) throws Exception;

    void puts(String metaName, List<Put> putList) throws Exception;

    Result get(String metaName, Get get) throws Exception;

    List<Result> gets(String metaName, List<Get> gets) throws Exception;

    List<Result> scan(String metaName,String startRow,String endRow,int limit) throws Exception;
}
