package com.dtstep.lighthouse.core.storage.warehouse;
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
import com.dtstep.lighthouse.core.storage.common.*;

import java.util.List;

public interface WarehouseStorageEngine {

    String getDefaultNamespace();

    void createNamespaceIfNotExist(String namespace) throws Exception;

    void createResultTable(String tableName) throws Exception;

    void createDimensTable(String tableName) throws Exception;

    boolean isTableExist(String tableName) throws Exception;

    void dropTable(String tableName) throws Exception;

    void put(String tableName, LdpPut ldpPut) throws Exception;

    void puts(String tableName, List<LdpPut> ldpPuts) throws Exception;

    void increment(String tableName, LdpIncrement ldpIncrement) throws Exception;

    void increments(String tableName,List<LdpIncrement> ldpIncrements) throws Exception;

    void putsWithCompare(String tableName, CompareOperator compareOperator, List<LdpPut> ldpPuts) throws Exception;

    <R> LdpResult<R> get(String tableName, LdpGet ldpGet, Class<R> clazz) throws Exception;

    <R> List<LdpResult<R>> gets(String tableName, List<LdpGet> ldpGets, Class<R> clazz) throws Exception;

    <R> List<LdpResult<R>> scan(String tableName, String startRow, String endRow, int limit,Class<R> clazz) throws Exception;

    void delete(String tableName,String key) throws Exception;

    void deletes(String tableName,List<String> keyList) throws Exception;

    boolean isAppendable(String tableName) throws Exception;

    long getTableMaxValidPeriod() throws Exception;

}
