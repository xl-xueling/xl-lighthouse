package com.dtstep.lighthouse.common.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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
}
