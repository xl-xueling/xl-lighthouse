package com.dtstep.lighthouse.common.serializer;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class HessianSerializer implements Serializer {

    public <T> byte[] serialize(T obj) throws Exception {
        try(ByteArrayOutputStream bos = new ByteArrayOutputStream()){
            HessianOutput ho = new HessianOutput(bos);
            ho.writeObject(obj);
            return bos.toByteArray();
        }catch (Exception ex){
            throw new RuntimeException("Failed to serialize object!", ex);
        }
    }

    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws Exception {
        try(ByteArrayInputStream bis = new ByteArrayInputStream(bytes)){
            HessianInput hi = new HessianInput(bis);
            return clazz.cast(hi.readObject());
        }catch (Exception ex){
            throw new RuntimeException("Failed to deserialize object!", ex);
        }
    }
}