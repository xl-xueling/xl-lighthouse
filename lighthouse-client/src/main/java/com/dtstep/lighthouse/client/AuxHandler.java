package com.dtstep.lighthouse.client;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.exception.LightSendException;
import com.dtstep.lighthouse.common.exception.LightTimeOutException;
import com.dtstep.lighthouse.common.ice.AuxInterfacePrx;
import com.dtstep.lighthouse.common.lru.Cache;
import com.dtstep.lighthouse.common.lru.LRU;
import com.dtstep.lighthouse.common.util.StringUtil;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

final class AuxHandler {

    private static final Cache<String,Optional<GroupVerifyEntity>> groupHolder = LRU.newBuilder().maximumSize(5000).expireAfterWrite(2,TimeUnit.MINUTES).softValues().build();

    private static final Cache<Integer,Optional<StatExtEntity>> statHolder = LRU.newBuilder().maximumSize(5000).expireAfterWrite(2,TimeUnit.MINUTES).softValues().build();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static GroupVerifyEntity queryStatGroup(AuxInterfacePrx auxInterfacePrx, String token) throws Exception{
        return groupHolder.get(token,k -> {
            GroupVerifyEntity groupVerifyEntity = null;
            try{
                Lock lock = new ReentrantLock();
                Condition condition = lock.newCondition();
                Ice.AsyncResult asyncResult = auxInterfacePrx.begin_queryGroupByToken(token,new NotifyThread(lock,condition));
                lock.lock();
                try {
                    condition.await(5000, TimeUnit.MILLISECONDS);
                    if(!asyncResult.isCompleted()){
                        throw new LightTimeOutException();
                    }
                } finally {
                    lock.unlock();
                }
                String str = auxInterfacePrx.end_queryGroupByToken(asyncResult);
                if(!StringUtil.isEmpty(str)){
                    groupVerifyEntity = objectMapper.readValue(str, GroupVerifyEntity.class);
                }
            }catch (Exception ex){
                ex.printStackTrace();
                throw new LightSendException();
            }
            return Optional.ofNullable(groupVerifyEntity);
        }).orElse(null);
    }


    public static StatExtEntity queryStatEntity(AuxInterfacePrx auxInterfacePrx, int statId) throws Exception{
        return statHolder.get(statId,k -> {
            StatExtEntity statExtEntity = null;
            try{
                Lock lock = new ReentrantLock();
                Condition condition = lock.newCondition();
                lock.lock();
                Ice.AsyncResult asyncResult = auxInterfacePrx.begin_queryStatById(statId,new NotifyThread(lock,condition));
                try {
                    condition.await(5000, TimeUnit.MILLISECONDS);
                    if(!asyncResult.isCompleted()){
                        throw new LightTimeOutException();
                    }
                } finally {
                    lock.unlock();
                }
                String str = auxInterfacePrx.end_queryStatById(asyncResult);
                if(!StringUtil.isEmpty(str)){
                    statExtEntity = objectMapper.readValue(str, StatExtEntity.class);
                }
            }catch (Exception ex){
                ex.printStackTrace();
                throw new LightSendException();
            }
            return Optional.ofNullable(statExtEntity);
        }).orElse(null);
    }
}
