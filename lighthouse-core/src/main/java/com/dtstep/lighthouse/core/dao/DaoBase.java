package com.dtstep.lighthouse.core.dao;
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
import com.google.common.collect.Lists;
import com.dtstep.lighthouse.common.entity.annotation.DBColumnAnnotation;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class DaoBase {

    public static void setParams(PreparedStatement ps,Object value,int columnIndex) throws Exception {
        if (value != null)
        {
            Class<?> valueType = value.getClass();
            if (valueType.equals(String.class)) {
                ps.setString(columnIndex, value.toString());
            } else if (valueType.equals(Integer.TYPE) || (valueType.equals(Integer.class))) {
                ps.setInt(columnIndex, Integer.parseInt(value.toString()));
            } else if (valueType.equals(Long.TYPE) || (valueType.equals(Long.class))) {
                ps.setLong(columnIndex, Long.parseLong(value.toString()));
            } else if (valueType.equals(Short.TYPE) || (valueType.equals(Short.class))) {
                ps.setShort(columnIndex, Short.parseShort(value.toString()));
            } else if (valueType.equals(Date.class)) {
                ps.setTimestamp(columnIndex, new Timestamp(((Date)value).getTime()));
            } else if (valueType.equals(Boolean.TYPE) || (valueType.equals(Boolean.class))) {
                ps.setBoolean(columnIndex, Boolean.parseBoolean(value.toString()));
            } else if (valueType.equals(Double.TYPE) || (valueType.equals(Double.class))) {
                ps.setDouble(columnIndex, Double.parseDouble(value.toString()));
            } else if (valueType.equals(Float.TYPE) || (valueType.equals(Float.class))) {
                ps.setFloat(columnIndex, Float.parseFloat(value.toString()));
            } else if (valueType.equals(Byte.TYPE) || (valueType.equals(Byte.class))) {
                ps.setByte(columnIndex, Byte.parseByte(value.toString()));
            } else if (valueType.equals(byte[].class) || (valueType.equals(Byte[].class))) {
                assert value instanceof byte[];
                ps.setBytes(columnIndex, (byte[])value);
            } else if (valueType.equals(BigDecimal.class)) {
                ps.setBigDecimal(columnIndex, new BigDecimal(value.toString()));
            } else if (valueType.equals(Timestamp.class)) {
                ps.setTimestamp(columnIndex, (Timestamp)value);
            } else if (valueType.equals(java.sql.Date.class)) {
                ps.setTimestamp(columnIndex, new Timestamp(((java.sql.Date)value).getTime()));
            } else {
                ps.setObject(columnIndex, value);
            }
        }
        else
        {
            ps.setObject(columnIndex, null);
        }
    }

    public static List<Field> getAllFields(Class clazz){
        Field[] fields = clazz.getDeclaredFields();
        List<Field> fieldList = new ArrayList<>();
        Collections.addAll(fieldList, fields);
        return fieldList;
    }

    public static  <T> List<T> populateData(ResultSet resultSet, Class<T> clazz) throws Exception {
        List<T> dataList = Lists.newArrayList();
        if(Number.class.isAssignableFrom(clazz) || clazz == String.class){
            while (resultSet.next()){
                T temp = resultSet.getObject(1,clazz);
                if(temp != null){
                    dataList.add(temp);
                }
            }
        }else {
            List<Field> fieldList = getAllFields(clazz);
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsCount = rsmd.getColumnCount();
            List<String> columnNameList = Lists.newArrayList();
            for (int i = 0; i < columnsCount; i++) {
                columnNameList.add(rsmd.getColumnLabel(i + 1).toLowerCase());
            }
            while (resultSet.next())
            {
                T bean = clazz.newInstance();
                for (Field f : fieldList)
                {
                    String columnName = f.getName();
                    DBColumnAnnotation column = f.getAnnotation(DBColumnAnnotation.class);
                    if(column == null){
                        continue;
                    }
                    String dbColumnName = column.basic().toLowerCase();
                    if(StringUtils.isEmpty(dbColumnName)){
                        dbColumnName = column.extend().toLowerCase();
                    }
                    if (columnNameList.contains(dbColumnName))
                    {
                        Object columnValueObj = null;
                        Class<?> filedCls = f.getType();
                        Class paramCls = null;
                        if(filedCls == int.class){
                            columnValueObj = resultSet.getInt(dbColumnName);
                            paramCls = int.class;
                        }else if (filedCls == Integer.class) {
                            columnValueObj = resultSet.getInt(dbColumnName);
                            paramCls = Integer.class;
                        }else if (filedCls == String.class) {
                            columnValueObj = resultSet.getString(dbColumnName);
                            paramCls = String.class;
                        }else if (filedCls == Boolean.class) {
                            columnValueObj = resultSet.getBoolean(dbColumnName);
                            paramCls = Boolean.class;
                        }else if (filedCls == boolean.class) {
                            columnValueObj = resultSet.getBoolean(dbColumnName);
                            paramCls = boolean.class;
                        }else if (filedCls == Byte.class) {
                            columnValueObj = resultSet.getByte(dbColumnName);
                            paramCls = Byte.class;
                        }else if (filedCls == Short.class) {
                            columnValueObj = resultSet.getShort(dbColumnName);
                            paramCls = Short.class;
                        }else if (filedCls == short.class) {
                            columnValueObj = resultSet.getShort(dbColumnName);
                            paramCls = short.class;
                        }else if (filedCls == Long.class) {
                            columnValueObj = resultSet.getLong(dbColumnName);
                            paramCls = Long.class;
                        }else if (filedCls == long.class) {
                            columnValueObj = resultSet.getLong(dbColumnName);
                            paramCls = long.class;
                        }else if (filedCls == Float.class) {
                            columnValueObj = resultSet.getFloat(dbColumnName);
                            paramCls = Float.class;
                        }else if (filedCls == float.class) {
                            columnValueObj = resultSet.getFloat(dbColumnName);
                            paramCls = float.class;
                        }else if (filedCls == Double.class) {
                            columnValueObj = resultSet.getDouble(dbColumnName);
                            paramCls = Double.class;
                        }else if (filedCls == double.class) {
                            columnValueObj = resultSet.getDouble(dbColumnName);
                            paramCls = double.class;
                        }else if (filedCls == BigDecimal.class) {
                            columnValueObj = resultSet.getBigDecimal(dbColumnName);
                            paramCls = BigDecimal.class;
                        }else if(filedCls == Date.class){
                            Timestamp t = resultSet.getTimestamp(dbColumnName);
                            if(t != null){
                                columnValueObj = new Date(t.getTime());
                                paramCls = Date.class;
                            }
                        }else {
                            columnValueObj = resultSet.getObject(dbColumnName);
                            paramCls = columnValueObj.getClass();
                        }
                        if (columnValueObj != null)
                        {
                            Method setterMethod = clazz.getMethod("set"+upperFirst(columnName),paramCls);
                            setterMethod.invoke(bean, columnValueObj);
                        }
                    }
                }
                dataList.add(bean);
            }
        }
        return dataList;
    }

    public static String upperFirst(String toUpper){
        return toUpper.substring(0, 1).toUpperCase()+ toUpper.substring(1);
    }

}
