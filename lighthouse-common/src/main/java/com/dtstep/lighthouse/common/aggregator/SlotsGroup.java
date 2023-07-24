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
import com.dtstep.lighthouse.common.queue.BoundedPriorityBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public final class SlotsGroup<T> {

    private static final Logger logger = LoggerFactory.getLogger(SlotsGroup.class);

    private final HashMap<Integer, SlotWrapper<T>> slotsMap = new HashMap<>();

    public SlotsGroup(int size, int capacity, Comparator<T> comparator){
        for(int i=0;i<size;i++){
            slotsMap.put(i,new SlotWrapper<T>(capacity,comparator));
        }
    }

    public SlotsGroup(int size, int capacity){
        for(int i=0;i<size;i++){
            slotsMap.put(i,new SlotWrapper<T>(capacity));
        }
    }

    public SlotWrapper<T> getSlotWrapper(int slot) {
        return slotsMap.get(slot);
    }

    public void put(int slot,T t) throws Exception {
        BlockingQueue<T> queue = slotsMap.get(slot).getQueue();
        try{
            queue.put(t);
        }catch (InterruptedException ex){
            logger.error("event pool put error!",ex);
            Thread.currentThread().interrupt();
        }
    }


    public static class SlotWrapper<T> {

        private BlockingQueue<T> queue;

        private long lastAccessTime;

        private int capacity;

        public SlotWrapper(int capacity){
            this.queue = new BoundedPriorityBlockingQueue<T>(capacity);
            this.capacity = capacity;
            this.lastAccessTime = System.currentTimeMillis();
        }

        public SlotWrapper(int capacity, Comparator<T> comparator){
            this.queue = new BoundedPriorityBlockingQueue<T>(capacity,comparator);
            this.lastAccessTime = System.currentTimeMillis();
        }

        public BlockingQueue<T> getQueue() {
            return queue;
        }

        public void setQueue(LinkedBlockingQueue<T> queue) {
            this.queue = queue;
        }

        public long getLastAccessTime() {
            return lastAccessTime;
        }

        public void setLastAccessTime(long lastAccessTime) {
            this.lastAccessTime = lastAccessTime;
        }

        public int getCapacity() {
            return capacity;
        }

        public void setCapacity(int capacity) {
            this.capacity = capacity;
        }

        public List<T> getEvents(int batch) throws Exception{
            List<T> list = new ArrayList<>();
            queue.drainTo(list,batch);
            this.setLastAccessTime(System.currentTimeMillis());
            return list;
        }

        public int size() throws Exception{
            return queue.size();
        }
    }
}


