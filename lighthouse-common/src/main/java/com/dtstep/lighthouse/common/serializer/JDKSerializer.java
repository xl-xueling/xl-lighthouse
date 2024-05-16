package com.dtstep.lighthouse.common.serializer;

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
