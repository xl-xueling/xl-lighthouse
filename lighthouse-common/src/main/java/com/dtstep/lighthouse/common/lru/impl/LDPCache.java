package com.dtstep.lighthouse.common.lru.impl;
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
import com.dtstep.lighthouse.common.lru.Cache;
import com.dtstep.lighthouse.common.lru.LRU;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.SoftReference;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;
import java.util.function.Function;

public final class LDPCache<K,V> implements Cache<K,V> {

    private static final Logger logger = LoggerFactory.getLogger(LDPCache.class);

    private Map<K, Node<K,V>> cache;

    private final DoublyLinkedList<K,V> doublyQueue;

    private final LRU<? super K, ? super V> builder;

    private final StampedLock lock = new StampedLock();

    private int size;

    public LDPCache(LRU<? super K, ? super V> builder){
        this.builder = builder;
        this.cache = new HashMap<>();
        this.doublyQueue = new DoublyLinkedList<>();
        this.size = 0;
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1,
                new BasicThreadFactory.Builder().namingPattern("ldp-cache-clear-schedule-pool-%d").daemon(true).build());
        service.scheduleWithFixedDelay(new ClearThread<>(this,lock),0,1, TimeUnit.MINUTES);
    }

    public LRU<? super K, ? super V> getBuilder() {
        return builder;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public V get(K k){
        long stamp = lock.tryOptimisticRead();
        V v = atomGet(k);
        if(!lock.validate(stamp)){
            stamp = lock.readLock();
            try{
                v = atomGet(k);
            }catch (Exception ex){
                ex.printStackTrace();
            }finally {
                lock.unlock(stamp);
            }
        }
        return v;
    }

    @Override
    public V get(K k, Function<? super K, ? extends V> f) {
        long stamp = lock.writeLock();
        V v = null;
        try{
            v = atomGet(k);
            if(v == null){
                v = f.apply(k);
                if(v != null){
                    atomPut(k,v);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            lock.unlock(stamp);
        }
        return v;
    }

    @Override
    public void remove(K k){
        long stamp = lock.writeLock();
        try{
            atomRemove(k);
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            lock.unlock(stamp);
        }
    }

    private void atomRemove(K k){
        Node<K,V> node = cache.get(k);
        if(node == null){
            return;
        }
        cache.remove(k);
        doublyQueue.remove(node);
        size --;
    }

    @Override
    public void put(K k, V v){
        if(v == null){
            return;
        }
        long stamp = lock.writeLock();
        try{
            atomPut(k,v);
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            lock.unlock(stamp);
        }
    }

    private V atomGet(K k){
        Node<K,V> node = cache.get(k);
        if(node == null){
            return null;
        }
        if(isExpired(node)){
            return null;
        }
        doublyQueue.moveToFirst(node);
        node.setAccessTime(System.currentTimeMillis());
        return node.getValue();
    }

    private void atomPut(K k, V v){
        long t = System.currentTimeMillis();
        Node<K,V> node = cache.get(k);
        if(node == null){
            node = new Node<>();
            node.setWriteTime(t);
            node.setAccessTime(t);
            node.setKey(k);
            node.setValue(v);
            cache.put(k,node);
            doublyQueue.addToFirst(node);
            size ++;
        }else{
            node.setWriteTime(t);
            node.setAccessTime(t);
            node.setKey(k);
            node.setValue(v);
            doublyQueue.moveToFirst(node);
        }
        if(size > builder.getMaximumSize()){
            K tailKey = doublyQueue.getTailKey();
            cache.remove(tailKey);
            doublyQueue.removeLast();
            size --;
        }
    }


    private boolean isExpired(Node<K,V> node){
        long expireAfterAccessSeconds = builder.getExpireAfterAccessSeconds();
        long expireAfterWriteSeconds = builder.getExpireAfterWriteSeconds();
        long t = System.currentTimeMillis();
        boolean expired = false;
        if (expireAfterAccessSeconds != -1 && (t - TimeUnit.SECONDS.toMillis(expireAfterAccessSeconds) > node.accessTime)) {
            expired = true;
        }
        if (expireAfterWriteSeconds != -1 && (t - TimeUnit.SECONDS.toMillis(expireAfterWriteSeconds) > node.writeTime)) {
            expired = true;
        }
        return expired;
    }

    public Map<K, Node<K, V>> getCache() {
        return cache;
    }

    public void setCache(Map<K, Node<K, V>> cache) {
        this.cache = cache;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    @Deprecated
    public String toString() {
        StringBuilder sbr = new StringBuilder();
        long stamp = lock.readLock();
        try{
            Node<K,V> node = this.doublyQueue.head;
            while (node != null){
                if(isExpired(node)){
                    node = node.next;
                }else{
                    sbr.append(node.key).append(":").append(node.value.get()).append(";");
                    node = node.next;
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            lock.unlock(stamp);
        }
        return ("data:" + sbr.toString() + ",data size:" + cache.size() + ",cache size:" + this.size);
    }

    public static class Node<K,V> {

        private K key;

        private SoftReference<V> value;

        private long accessTime;

        private long writeTime;

        private Node<K, V> pre;

        private Node<K,V> next;

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value.get();
        }

        public void setValue(V value) {
            this.value = new SoftReference<V>(value);
        }

        public long getAccessTime() {
            return accessTime;
        }

        public void setAccessTime(long accessTime) {
            this.accessTime = accessTime;
        }

        public long getWriteTime() {
            return writeTime;
        }

        public void setWriteTime(long writeTime) {
            this.writeTime = writeTime;
        }

        public Node<K, V> getPre() {
            return pre;
        }

        public void setPre(Node<K, V> pre) {
            this.pre = pre;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }
    }

    public static class DoublyLinkedList<K,V> {

        private Node<K,V> head,tail;

        public DoublyLinkedList(){
            head = tail = null;
        }

        public void addToFirst(Node<K,V> node){
            if(tail == null){
                head = tail = node;
            }else{
                node.next = head;
                head.pre = node;
                head = node;
            }
        }

        public Node<K, V> getHead() {
            return head;
        }

        public void setHead(Node<K, V> head) {
            this.head = head;
        }

        public Node<K, V> getTail() {
            return tail;
        }

        public void setTail(Node<K, V> tail) {
            this.tail = tail;
        }

        public void moveToFirst(Node<K,V> node){
            if(head == node){
                return;
            }
            if(node == tail){
                tail = node.pre;
                tail.next = null;
            }else{
                node.pre.next = node.next;
                node.next.pre = node.pre;
            }
            node.pre = null;
            node.next = head;
            head.pre = node;
            head = node;
        }

        public void remove(Node<K,V> node){
            if(head == tail){
                head = tail = null;
            } else if(node == tail){
                removeLast();
            }else if(node == head){
                removeFirst();
            }else{
                node.pre.next = node.next;
                node.next.pre = node.pre;
            }
        }

        public void removeLast(){
            if(tail == null){
                return;
            }
            if(head == tail){
                head = tail = null;
            }else{
                tail = tail.pre;
                tail.next = null;
            }
        }

        public void removeFirst(){
            if(head == null){
                return;
            }
            if(head == tail){
                head = tail = null;
            }else{
                head = head.next;
                head.pre = null;
            }
        }

        public K getTailKey(){
            return tail.key;
        }
    }

    public static class ClearThread<K,V> implements Runnable{

        private final LDPCache<K,V> ldpCache;

        private final StampedLock lock;

        private ClearThread(LDPCache<K,V> ldpCache,StampedLock lock){
            this.ldpCache = ldpCache;
            this.lock = lock;
        }

        @Override
        public void run() {
            long stamp = lock.writeLock();
            List<K> delKeys = new ArrayList<>();
            try{
                Map<K,Node<K,V>> cache = ldpCache.cache;
                for (Map.Entry<K, Node<K, V>> entry : cache.entrySet()) {
                    Node<K, V> node = entry.getValue();
                    K k = entry.getKey();
                    if(node == null || ldpCache.isExpired(node) || node.value == null || node.value.get() == null){
                        delKeys.add(k);
                    }
                }
                for (K delKey : delKeys) {
                    try{
                        ldpCache.atomRemove(delKey);
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
                if(logger.isDebugEnabled()){
                    logger.debug("finished clear ldp-cache,delete size:{},current size:{}",delKeys.size(),cache.size());
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }finally {
                lock.unlock(stamp);
            }
        }
    }
}
