package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.common.entity.annotation.EnumSerializerAnnotation;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class EnumTypeSerializer extends JsonSerializer<Enum<?>> {

    @Override
    public void serialize(Enum<?> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        Field field = null;
        Field[] fields = value.getClass().getDeclaredFields();
        for (Field f : fields){
            Annotation dbEnumMapperAnnotation = f.getAnnotation(EnumSerializerAnnotation.class);
            if(dbEnumMapperAnnotation != null){
                field = f;
                field.setAccessible(true);
            }
        }
        Object obj;
        if(field != null){
            try{
                obj = field.get(value);
            }catch (Exception ex){
                obj = value.name();
            }
        }else {
            obj = value.name();
        }
        gen.writeObject(obj);
    }
}