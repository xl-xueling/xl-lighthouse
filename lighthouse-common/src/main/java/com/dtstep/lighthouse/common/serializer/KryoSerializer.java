package com.dtstep.lighthouse.common.serializer;
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

import com.dtstep.lighthouse.common.entity.group.GroupVerifyEntity;
import com.dtstep.lighthouse.common.entity.stat.StatVerifyEntity;
import com.dtstep.lighthouse.common.entity.stat.TimeParam;
import com.dtstep.lighthouse.common.entity.view.LimitValue;
import com.dtstep.lighthouse.common.entity.view.StatValue;
import com.dtstep.lighthouse.common.enums.GroupStateEnum;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KryoSerializer implements Serializer {

    private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.setReferences(true);
        kryo.setRegistrationRequired(false);
        kryo.register(LocalDateTime.class);
        kryo.register(LimitValue.class);
        kryo.register(StatValue.class);
        kryo.register(StatVerifyEntity.class);
        kryo.register(GroupVerifyEntity.class);
        kryo.register(GroupStateEnum.class);
        kryo.register(TimeParam.class);
        return kryo;
    });

    @Override
    public <T> byte[] serialize(T obj) throws Exception {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             Output output = new Output(byteArrayOutputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            kryo.writeObject(output, obj);
            output.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize object!", e);
        }
    }


    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws Exception {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             Input input = new Input(byteArrayInputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            return kryo.readObject(input, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize object!", e);
        }
    }


    @Override
    public <T> byte[] serializerList(List<T> list) throws Exception {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             Output output = new Output(byteArrayOutputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            kryo.writeObject(output, list);
            output.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize list object!", e);
        }
    }

    @Override
    public <T> List<T> deserializeList(byte[] bytes, Class<T> clazz) throws Exception {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             Input input = new Input(byteArrayInputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            return kryo.readObject(input, ArrayList.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize list object!", e);
        }
    }

    @Override
    public <K, V> byte[] serializerMap(Map<K,V> map) throws Exception {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             Output output = new Output(byteArrayOutputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            kryo.writeObject(output, map);
            output.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize map object!", e);
        }
    }

    @Override
    public <K, V> Map<K, V> deserializeMap(byte[] bytes, Class<? extends Map> clazz) throws Exception {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             Input input = new Input(byteArrayInputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            return kryo.readObject(input, HashMap.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize list object!", e);
        }
    }
}
