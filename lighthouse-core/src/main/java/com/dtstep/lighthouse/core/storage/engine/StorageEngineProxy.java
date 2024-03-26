package com.dtstep.lighthouse.core.storage.engine;


import com.dtstep.lighthouse.core.storage.engine.mysql.MySQLStorageEngine;

public class StorageEngineProxy {

    private static final StorageEngine storageEngine = new MySQLStorageEngine();

    public static StorageEngine getInstance(){
        return storageEngine;
    }
}
