package com.dtstep.lighthouse.core.storage.cmdb;

public interface CMDBStorageEngine<CONNECTION> {

    CONNECTION getConnection() throws Exception;

    void closeConnection() throws Exception;
}
