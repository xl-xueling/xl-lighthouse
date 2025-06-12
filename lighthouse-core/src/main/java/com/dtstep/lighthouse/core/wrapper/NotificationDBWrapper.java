package com.dtstep.lighthouse.core.wrapper;
/*
 * Copyright (C) 2022-2025 XueLing.雪灵
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
import com.dtstep.lighthouse.common.modal.Notification;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.storage.cmdb.CMDBStorageEngine;
import com.dtstep.lighthouse.core.storage.cmdb.CMDBStorageEngineProxy;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.math.BigInteger;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public class NotificationDBWrapper {

    private static final CMDBStorageEngine<Connection> storageEngine = CMDBStorageEngineProxy.getInstance();

    public static long insert(Notification notification) throws Exception {
        Connection conn = storageEngine.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        int id;
        try{
            ScalarHandler<BigInteger> keyHandler = new ScalarHandler<>();
            BigInteger result = queryRunner.insert(conn, "INSERT INTO ldp_notifications (`resource_id`, `resource_type`,`content`,`state`,`user_ids`,`department_ids`,`notification_type`,`p1`,`p2`,`p3`,`create_time`,`update_time`) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", keyHandler,notification.getResourceId(),notification.getResourceType().getResourceType(),
                    notification.getContent(),notification.getState().getState(),
                    CollectionUtils.isNotEmpty(notification.getUserIds()) ? JsonUtil.toJSONString(notification.getUserIds()) : null,
                    CollectionUtils.isNotEmpty(notification.getDepartmentIds()) ? JsonUtil.toJSONString(notification.getDepartmentIds()) : null,
                    notification.getNotificationType().getType(),
                    notification.getP1(),
                    notification.getP2(),
                    notification.getP3(),
                    notification.getCreateTime(),notification.getUpdateTime()
            );
            id = result.intValue();
        }finally {
            storageEngine.closeConnection();
        }
        return id;
    }

    public static int[] batchInsert(List<Notification> notifications) throws Exception {
        Connection conn = storageEngine.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        int[] result;
        LocalDateTime localDateTime = LocalDateTime.now();
        String sql = "INSERT INTO ldp_notifications (`resource_id`, `resource_type`,`content`,`state`,`user_ids`,`department_ids`,`notification_type`,`p1`,`p2`,`p3`,`create_time`,`update_time`) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Object[][] params = notifications.stream()
                .map(notification -> new Object[]{
                        notification.getResourceId(),
                        notification.getResourceType().getResourceType(),
                        notification.getContent(),
                        notification.getState().getState(),
                        CollectionUtils.isNotEmpty(notification.getUserIds()) ? JsonUtil.toJSONString(notification.getUserIds()) : null,
                        CollectionUtils.isNotEmpty(notification.getDepartmentIds()) ? JsonUtil.toJSONString(notification.getDepartmentIds()) : null,
                        notification.getNotificationType().getType(),
                        notification.getP1(),
                        notification.getP2(),
                        notification.getP3(),
                        localDateTime,
                        localDateTime
                })
                .toArray(Object[][]::new);
        try {
            result = queryRunner.batch(conn, sql, params);
        } finally {
            storageEngine.closeConnection();
        }
        return result;
    }
}
