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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;

public class JDKSerializer implements Serializer{

    @Override
    public <T> byte[] serialize(T obj) throws Exception {
        try(ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos)){
            oos.writeObject(obj);
            oos.flush();
            return bos.toByteArray();
        }catch (Exception ex){
            throw new RuntimeException("Failed to serialize object!", ex);
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws Exception {
        try(ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);){
            return clazz.cast(ois.readObject());
        }catch (Exception ex){
            throw new RuntimeException("Failed to deserialize object!", ex);
        }
    }

    @Override
    public <T> byte[] serializerList(List<T> list) throws Exception {
        try(ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos)){
            oos.writeObject(list);
            oos.flush();
            return bos.toByteArray();
        }catch (Exception ex){
            throw new RuntimeException("Failed to serialize list object!", ex);
        }
    }

    @Override
    public <T> List<T> deserializeList(byte[] bytes, Class<T> clazz) throws Exception {
        try(ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);){
            return (List<T>)ois.readObject();
        }catch (Exception ex){
            throw new RuntimeException("Failed to deserialize list object!", ex);
        }
    }

    @Override
    public <K, V> byte[] serializerMap(Map<K, V> map) throws Exception {
        try(ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos)){
            oos.writeObject(map);
            oos.flush();
            return bos.toByteArray();
        }catch (Exception ex){
            throw new RuntimeException("Failed to serialize list object!", ex);
        }
    }

    @Override
    public <K, V> Map<K, V> deserializeMap(byte[] bytes, Class<? extends Map> clazz) throws Exception {
        try(ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);){
            return (Map<K, V>)ois.readObject();
        }catch (Exception ex){
            throw new RuntimeException("Failed to deserialize list object!", ex);
        }
    }
}
