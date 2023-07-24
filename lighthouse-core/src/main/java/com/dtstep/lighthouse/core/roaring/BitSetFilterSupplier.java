package com.dtstep.lighthouse.core.roaring;
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
import com.google.common.util.concurrent.Striped;
import com.dtstep.lighthouse.common.lru.Cache;
import com.dtstep.lighthouse.common.lru.LRU;
import com.dtstep.lighthouse.core.roaring.service.RoaringBitMapExtend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;

public final class BitSetFilterSupplier {

    private static final Logger logger = LoggerFactory.getLogger(BitSetFilterSupplier.class);

    private static final Cache<String, RoaringBitMapExtend> bitCache = LRU.newBuilder().maximumSize(200000)
            .expireAfterWrite(10,TimeUnit.MINUTES).softValues().build();

    private static final BitSetFilterSupplier instance = new BitSetFilterSupplier();

    private static final Striped<Lock> stripedLock = Striped.lazyWeakLock(300);

    private BitSetFilterSupplier(){}

    public static BitSetFilterSupplier getInstance(){
        return instance;
    }

    public boolean check(String bitKey, long bitIndex){
        Lock lock = stripedLock.get(bitKey);
        lock.lock();
        try{
            RoaringBitMapExtend roaringBitMap = bitCache.get(bitKey, k -> new RoaringBitMapExtend());
            if(roaringBitMap == null){
                return false;
            }
            return roaringBitMap.checkAndPut(bitIndex);
        }catch (Exception ex){
            logger.error("lighthouse stat,set bit process error,bitIndex:{}",bitIndex,ex);
        }finally {
            lock.unlock();
        }
        return false;
    }

}
