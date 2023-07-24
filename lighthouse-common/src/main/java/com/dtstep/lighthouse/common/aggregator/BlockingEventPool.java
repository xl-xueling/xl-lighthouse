package com.dtstep.lighthouse.common.aggregator;
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
import com.dtstep.lighthouse.common.hash.HashUtil;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;

public class BlockingEventPool<T> implements EventPool<T> {

    private static final Logger logger = LoggerFactory.getLogger(BlockingEventPool.class);

    private String name;

    private final SlotsGroup<T> slotsGroup;

    private final int slotSize;

    public BlockingEventPool(int slotSize, int slotCapacity, Comparator<T> comparator){
        this.slotSize = slotSize;
        slotsGroup = new SlotsGroup<T>(slotSize, slotCapacity, comparator);
    }

    public BlockingEventPool(int slotCapacity,Comparator<T> comparator){
        this.slotSize = StatConst.DEFAULT_POOL_SLOT_SIZE;
        slotsGroup = new SlotsGroup<T>(slotSize, slotCapacity, comparator);
    }

    public BlockingEventPool(int slotSize, int slotCapacity){
        this.slotSize = slotSize;
        slotsGroup = new SlotsGroup<>(slotSize,slotCapacity);
    }

    public BlockingEventPool(String name,int slotSize, int slotCapacity){
        this.name = name;
        this.slotSize = slotSize;
        slotsGroup = new SlotsGroup<>(slotSize,slotCapacity);
    }


    @Override
    public void put(int slot, T t) throws Exception {
        if(slot < 0 || slot > slotSize){
            throw new IllegalArgumentException("Illegal parameter slot:" + slot + ",max size:" + slotSize);
        }
        if(t == null){
            return;
        }
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        slotsGroup.put(slot,t);
        long cost = stopWatch.getTime();
        if(cost > StatConst.EVENT_POOL_PUT_COST_THRESHOLD){
            logger.warn("Put elements to event pool[{}],slot[{}] cost exceeds threshold[{}ms>{}ms]!",name,slot,cost,StatConst.EVENT_POOL_PUT_COST_THRESHOLD);
        }
    }

    @Deprecated
    @Override
    public void put(T t) throws Exception {
        if(t == null){
            return;
        }
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int slot = HashUtil.getHashIndex(t.toString(),slotSize);
        slotsGroup.put(slot,t);
        long cost = stopWatch.getTime();
        if(cost > StatConst.EVENT_POOL_PUT_COST_THRESHOLD){
            logger.warn("Put elements to event pool[{}],slot[{}] cost exceeds threshold[{}ms>{}ms]!",name,slot,cost,StatConst.EVENT_POOL_PUT_COST_THRESHOLD);
        }
    }

    @Override
    public SlotsGroup.SlotWrapper<T> take(int slot) throws Exception {
        return slotsGroup.getSlotWrapper(slot);
    }

    public int slotSize(){
        return slotSize;
    }

    @Override
    public boolean isEmpty() throws Exception {
        for(int i=0;i<slotSize;i++){
            SlotsGroup.SlotWrapper<T> slotWrapper = take(i);
            if(!slotWrapper.getQueue().isEmpty()){
                return false;
            }
        }
        return true;
    }

    @Override
    public void clear() throws Exception {
        for(int i=0;i<slotSize;i++){
            SlotsGroup.SlotWrapper<T> slotWrapper = take(i);
            slotWrapper.getQueue().clear();
        }
    }
}
