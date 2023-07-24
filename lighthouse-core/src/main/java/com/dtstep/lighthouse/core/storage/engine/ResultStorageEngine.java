package com.dtstep.lighthouse.core.storage.engine;
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
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.view.StatValue;

import java.util.LinkedHashMap;
import java.util.List;

public abstract class ResultStorageEngine<W,R> implements StorageEngine<W,R> {

    public abstract void increment(List<W> list) throws Exception;

    public abstract void maxPut(List<W> list) throws Exception;

    public abstract void minPut(List<W> list) throws Exception;

    public abstract void put(List<W> list) throws Exception;

    public abstract R queryWithDimens(StatExtEntity statExtEntity, String dimensValue, long batchTime) throws Exception;

    public abstract List<R> queryWithDimens(StatExtEntity statExtEntity, String dimensValue, List<Long> batchTimeList) throws Exception;

    public abstract List<R> queryWithDimensList(StatExtEntity statExtEntity, List<String> dimensValueList, List<Long> batchTimeList) throws Exception;

    public abstract LinkedHashMap<String, StatValue> queryWithDimensList(StatExtEntity statExtEntity, List<String> dimensValueList, long batchTime) throws Exception;
}
