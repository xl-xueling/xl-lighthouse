package com.dtstep.lighthouse.web.manager.meta;
/*
 * Copyright (C) 2022-2023 XueLing.雪灵
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.meta.MetaTableEntity;
import com.dtstep.lighthouse.common.enums.meta.MetaTableTypeEnum;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import com.dtstep.lighthouse.core.wrapper.MetaTableWrapper;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class MetaTableManager {

    private static final long RESULT_MAXIMUM_TIME_INTERVAL = TimeUnit.DAYS.toMillis(180);

    public List<MetaTableEntity> queryAllResultTables() throws Exception {
        return DaoHelper.sql.getList(MetaTableEntity.class,"select * from ldp_meta_table where `type` in (?,?) and state = 1"
                , MetaTableTypeEnum.STAT_RESULT_TABLE.getType(),MetaTableTypeEnum.SEQ_RESULT_TABLE.getType());
    }

    public int queryVolumeStatResultTable() throws Exception{
        MetaTableEntity metaTableEntity = DaoHelper.sql.getItem(MetaTableEntity.class, "select * from ldp_meta_table where type = ? " +
                "and content_size < ? order by create_time desc limit 1",MetaTableTypeEnum.STAT_RESULT_TABLE.getType(), StatConst.RESULT_MAXIMUM_CONTENT_SIZE);
        int metaId;
        if(metaTableEntity == null || (System.currentTimeMillis() - metaTableEntity.getCreateTime().getTime() > RESULT_MAXIMUM_TIME_INTERVAL)){
            metaId = MetaTableWrapper.createStatResultMetaTable();
        }else{
            metaId = metaTableEntity.getId();
        }
        return metaId;
    }

    public int queryVolumeSeqResultTable() throws Exception{
        MetaTableEntity metaTableEntity = DaoHelper.sql.getItem(MetaTableEntity.class, "select * from ldp_meta_table where type = ? " +
                "and content_size < ? order by create_time desc limit 1",MetaTableTypeEnum.SEQ_RESULT_TABLE.getType(), StatConst.RESULT_MAXIMUM_CONTENT_SIZE);
        int metaId;
        if(metaTableEntity == null || (System.currentTimeMillis() - metaTableEntity.getCreateTime().getTime() > RESULT_MAXIMUM_TIME_INTERVAL)){
            metaId = MetaTableWrapper.createSeqResultMetaTable();
        }else{
            metaId = metaTableEntity.getId();
        }
        return metaId;
    }

    public long getTableContentSize(String metaName) throws Exception{
        String namenode = LDPConfig.getVal(LDPConfig.KEY_HADOOP_NAMENODE_IPS);
        String clusterId = LDPConfig.getVal(LDPConfig.KEY_CLUSTER_ID);
        Configuration configuration=new Configuration();
        String nameNodeUrl = String.format("hdfs://%s:9000",namenode);
        configuration.set("fs.defaultFS", nameNodeUrl);
        URI uri = new URI(nameNodeUrl);
        FileSystem fileSystem= FileSystem.get(uri,configuration);
        String tablePath = String.format("/hbase/data/cluster_%s_ldp_hbasedb/%s",clusterId,metaName);
        return fileSystem.getContentSummary(new Path(tablePath)).getLength();
    }

    public void updateContentSize(String metaName,long contentSize) throws Exception {
        if(StringUtil.isEmpty(metaName) || contentSize <= 0){
            return;
        }
        DaoHelper.sql.execute("update ldp_meta_table set content_size = ? , update_time = ? where meta_name = ?",contentSize,new Date(),metaName);
    }

}
