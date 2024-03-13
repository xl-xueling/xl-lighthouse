package com.dtstep.lighthouse.core.wrapper;
/*
 * Copyright (C) 2022-2024 XueLing.雪灵
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
import com.dtstep.lighthouse.common.modal.Record;
import com.dtstep.lighthouse.core.dao.ConnectionManager;
import com.dtstep.lighthouse.core.dao.DBConnection;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.Validate;
import java.sql.Connection;
import java.time.LocalDateTime;


public final class LimitingWrapper {

    public static void record(Record limitingRecord) throws Exception{
        Validate.notNull(limitingRecord);
        DBConnection dbConnection = ConnectionManager.getConnection();
        Connection conn = dbConnection.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        String sql = "INSERT INTO ldp_records (`resource_id`, `resource_type`,`record_type`,`create_time`) VALUES (?, ?, ?, ?)";
        LocalDateTime localDateTime = LocalDateTime.now();
        try{
            queryRunner.insert(conn,sql,new ScalarHandler<>(),limitingRecord.getResourceId(),limitingRecord.getResourceType().getResourceType()
                    ,limitingRecord.getRecordType().getRecordType(),localDateTime);
        }finally {
            ConnectionManager.close(dbConnection);
        }
    }
}
