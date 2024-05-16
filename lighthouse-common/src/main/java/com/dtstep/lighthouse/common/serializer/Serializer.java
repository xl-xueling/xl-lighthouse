package com.dtstep.lighthouse.common.serializer;

import java.util.List;
import java.util.Map;

public interface Serializer {

    <T> byte[] serialize(T obj) throws Exception;

    <T> T deserialize(byte[] bytes, Class<T> clazz) throws Exception;

    <T> byte[] serializerList(List<T> list) throws Exception;

    <T> List<T> deserializeList(byte[] bytes, Class<T> clazz) throws Exception;

    <K,V> byte[] serializerMap(Map<K,V> map) throws Exception;

    <K,V> Map<K,V> deserializeMap(byte[] bytes,Class<? extends Map> clazz) throws Exception;
}
