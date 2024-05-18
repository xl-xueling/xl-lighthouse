package com.dtstep.lighthouse.core.tools;

import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.storage.engine.hbase.HBaseStorageEngine;
import org.apache.hadoop.hbase.TableName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HBaseTableRegionMerge {

    private static final Logger logger = LoggerFactory.getLogger(HBaseTableRegionMerge.class);

    public static void main(String[] args) throws Exception {
        LDPConfig.loadConfiguration();
        int targetSize = 6;
        if(args != null){
            targetSize = Integer.parseInt(args[0]);
        }
        HBaseStorageEngine storageEngine = new HBaseStorageEngine();
        TableName[] tableNames = storageEngine.listTables();
        if(tableNames == null || tableNames.length == 0){
            return;
        }
        for(TableName tableName : tableNames){
            String tableNameStr = tableName.getNameAsString();
            storageEngine.merge(tableNameStr,targetSize);
        }
        logger.info("Execution completed and the program exits!");
        System.exit(0);
    }
}
