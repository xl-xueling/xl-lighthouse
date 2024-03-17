package com.dtstep.lighthouse.client;
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
import com.dtstep.lighthouse.client.rpc.RPCClientProxy;
import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.exception.LightSendException;
import com.dtstep.lighthouse.common.lru.Cache;
import com.dtstep.lighthouse.common.lru.LRU;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

final class AuxHandler {

    private static final Cache<String,Optional<GroupVerifyEntity>> groupHolder = LRU.newBuilder().maximumSize(5000).expireAfterWrite(2,TimeUnit.MINUTES).softValues().build();

    public static GroupVerifyEntity queryStatGroup(String token) {
        return groupHolder.get(token,k -> {
            GroupVerifyEntity groupVerifyEntity;
            try{
                groupVerifyEntity = RPCClientProxy.instance().queryGroup(token);
            }catch (Exception ex){
                ex.printStackTrace();
                throw new LightSendException();
            }
            return Optional.ofNullable(groupVerifyEntity);
        }).orElse(null);
    }

}
