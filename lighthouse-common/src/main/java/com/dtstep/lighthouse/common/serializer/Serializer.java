package com.dtstep.lighthouse.common.serializer;

public interface Serializer {

    <T> byte[] serialize(T obj) throws Exception;

    <T> T deserialize(byte[] bytes, Class<T> clazz) throws Exception;
}
