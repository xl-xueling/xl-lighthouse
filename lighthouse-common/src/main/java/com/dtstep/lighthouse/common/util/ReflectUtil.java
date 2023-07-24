package com.dtstep.lighthouse.common.util;
/*
 * Copyright (C) 2022-2023 XueLing.雪灵
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


public final class ReflectUtil {

    private static final Logger logger = LoggerFactory.getLogger(ReflectUtil.class);
    public static final String DOT = ".";
    public static final String GET = "get";
    public static final String SET = "set";
    public static final String IS = "is";

    private ReflectUtil() {}

    public static void close(Object object) {
        try {
            invoke(object, "close");
        } catch (Exception var2) {
            logger.warn(var2.getMessage(), var2);
        }
    }

    public static void dispose(Object object) {
        try {
            invoke(object, "dispose");
        } catch (Exception var2) {
            logger.warn(var2.getMessage(), var2);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T run(Object object, String methodName) {
        Object result = null;
        try {
            result = invoke(object, methodName);
        } catch (Exception var4) {
            logger.warn(var4.getMessage(), var4);
        }
        return (T)result;
    }

    @SuppressWarnings("unchecked")
    public static <T> T run(Object object, String methodName, Object[] args) {
        Object result = null;
        try {
            result = invoke(object, methodName, args);
        } catch (Exception var5) {
            logger.warn(var5.getMessage(), var5);
        }
        return (T)result;
    }

    @SuppressWarnings("unchecked")
    public static <T> T run(Object object, String methodName, Object[] args, Class<?>[] argsType) {
        Object result = null;
        try {
            result = invoke(object, methodName, args, argsType);
        } catch (Exception var6) {
            logger.warn(var6.getMessage(), var6);
        }
        return (T)result;
    }

    public static <T> T getValue(Object object, String name) {
        if(name.endsWith("()")) {
            name = name.replace("()", "");
            return getMethodValue(object, name);
        } else {
            return getFieldValue(object, name);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Object object, String fieldName) {
        try {
            Field e = getField(object.getClass(), fieldName);
            if(e != null) {
                return (T)e.get(object);
            }
        } catch (Exception var3) {
            logger.warn(var3.getMessage(), var3);
        }
        return null;
    }

    private static <T> T getMethodValue(Object object, String methodName) {
        try {
            return invoke(object, methodName);
        } catch (Exception var3) {
            logger.warn(var3.getMessage(), var3);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T invoke(Object object, String methodName) {
        if(object == null) {
            return null;
        } else {
            try {
                Method e = getMethod(object.getClass(), methodName, new Class[0]);
                return e != null?(T)e.invoke(object, new Object[0]):null;
            } catch (Exception ex) {
               ex.printStackTrace();
            }
        }
        return null;
    }

    public static <T> T invoke(Object object, String methodName, Object[] args) {
        if(args != null && args.length != 0) {
            Class[] argTypes = new Class[args.length];
            for(int i = 0; i < args.length; ++i) {
                argTypes[i] = args[i].getClass();
            }
            return invoke(object, methodName, args, argTypes);
        } else {
            return invoke(object, methodName);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T invoke(Object object, String methodName, Object[] args, Class<?>[] argTypes) {
        if(object == null) {
            return null;
        } else {
            try {
                Method e = getMethod(object.getClass(), methodName, argTypes);
                return e != null?(T)e.invoke(object, args):null;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    private static Field getField(Class<?> clazz, String fieldName){
        Field field = null;
        try {
            field = clazz.getField(fieldName);
        } catch (NoSuchFieldException var6) {
            try {
                field = clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException var5) {
                if(clazz.getSuperclass() == null) {
                    return field;
                }
                return getField(clazz.getSuperclass(), fieldName);
            }
        }
        field.setAccessible(true);
        return field;
    }

    private static Method getMethod(Class<?> clazz, String methodName, Class<?>... argsType) throws NoSuchMethodException {
        Method method = null;
        try {
            method = clazz.getMethod(methodName, argsType);
        } catch (NoSuchMethodException var7) {
            try {
                method = clazz.getDeclaredMethod(methodName, argsType);
            } catch (NoSuchMethodException var6) {
                if(clazz.getSuperclass() == null) {
                    return method;
                }
                return getMethod(clazz.getSuperclass(), methodName, argsType);
            }
        }
        method.setAccessible(true);
        return method;
    }

    public static Class<?>[] getClassOfArgs(Object... args) {
        Class[] types = new Class[args.length];
        int i = -1;
        int var4 = args.length;
        for(int var5 = 0; var5 < var4; ++var5) {
            Object argv = args[var5];
            ++i;
            types[i] = argv.getClass();
        }
        return types;
    }
}