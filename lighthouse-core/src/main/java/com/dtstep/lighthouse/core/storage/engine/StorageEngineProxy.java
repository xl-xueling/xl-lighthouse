package com.dtstep.lighthouse.core.storage.engine;


import com.dtstep.lighthouse.core.storage.engine.hbase.HBaseStorageEngine;

public class StorageEngineProxy {

    private static final StorageEngine storageEngine = new HBaseStorageEngine();

    public static StorageEngine getInstance(){
        return storageEngine;
    }
}
