package com.dtstep.lighthouse.insights.modal;


import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;

public class EnumTypeDeserializer extends JsonDeserializer<Object> implements ContextualDeserializer {

    private Class<? extends Object> propertyClass;

    public EnumTypeDeserializer() {}

    public EnumTypeDeserializer(Class<? extends Object> propertyClass) {
        this.propertyClass = propertyClass;
    }

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        String value;
        try {
            value = p.getValueAsString();
        } catch (JsonParseException e) {
            return null;
        }
        Field field = null;
        Field[] fields = propertyClass.getDeclaredFields();
        for (Field f : fields){
            Annotation annotation = f.getAnnotation(JsonValue.class);
            if(annotation != null){
                field = f;
                field.setAccessible(true);
            }
        }
        Field finalField = field;
        return Arrays.stream(propertyClass.getEnumConstants())
                .filter(e -> {
                    try{
                        if(finalField != null){
                            Object tempValue = finalField.get(e);
                            return (tempValue != null && tempValue.toString().equals(String.valueOf(value)));
                        }else{
                            return false;
                        }
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                    return false;
                })
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Enum type does not exist in ["+propertyClass+"],["+finalField.getName()+":"+value+"]!"));
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws
            JsonMappingException {
        return new EnumTypeDeserializer((Class<? extends Object>) property.getType().getRawClass());
    }
}
