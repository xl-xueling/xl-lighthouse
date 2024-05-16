package com.dtstep.lighthouse.common.serializer;

public class SerializerProxy {

    private static Serializer serializer = null;

    public static Serializer instance(){
        if(serializer == null){
            serializer = new KryoSerializer();
        }
        return serializer;
    }
}
