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
import com.dtstep.lighthouse.common.entity.annotation.DBColumnAnnotation;
import com.dtstep.lighthouse.common.entity.annotation.DBNameAnnotation;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public final class DaoHandler implements IDao {

    private static final Logger logger = LoggerFactory.getLogger(DaoHandler.class);

    DaoHandler(){}

    @Override
    public <T> List<T> getList(Class<T> clazz, String sql, Object... param) throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        DBConnection dbConnection = ConnectionManager.getConnection();
        Connection conn = dbConnection.getConnection();
        ResultSet rs = null;
        PreparedStatement ps = null;
        List<T> list;
        try{
            ps = conn.prepareStatement(sql);
            if(param != null){
                for(int i=0;i<param.length;i++){
                    Object obj = param[i];
                    DaoBase.setParams(ps, obj, i + 1);
                }
            }
            rs = ps.executeQuery();
            list = DaoBase.populateData(rs,clazz);
        }catch (Exception ex){
            logger.error("db query error.sql:{}",sql,ex);
            throw ex;
        }finally {
            close(ps,rs,dbConnection);
        }
        logger.debug("db query,sql:{},cost:{}",sql, stopWatch.getTime());
        return list;
    }

    @Override
    public <T> T getItem(Class<T> clazz, String sql, Object... param) throws Exception {
        if(clazz.isPrimitive()){
            throw new InstantiationException("type not support,clazz:" + clazz);
        }
        List<T> list = getList(clazz,sql,param);
        T t = null;
        if(list != null && list.size() == 1){
            t = list.get(0);
        }
        return t;
    }

    @Override
    public int insert(Object obj) throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        DBConnection dbConnection = ConnectionManager.getConnection();
        Connection conn = dbConnection.getConnection();
        Class t = obj.getClass();
        Field[] fields = t.getDeclaredFields();
        Annotation[] annotations = t.getAnnotations();
        PreparedStatement ps = null;
        int preparedIndex = 1;
        StringBuilder columnsBuffer = new StringBuilder();
        StringBuilder placeBuffer = new StringBuilder();
        Annotation an = annotations[0];
        DBNameAnnotation entity = (DBNameAnnotation)an;
        String tableName = entity.name();
        HashMap<Integer,Object> paramMap = new HashMap<>();
        int id = -1;
        for(Field f : fields){
            DBColumnAnnotation tt = f.getAnnotation(DBColumnAnnotation.class);
            f.setAccessible(true);
            if(f.get(obj) == null || tt == null || StringUtil.isEmpty(tt.basic())){
                continue;
            }
            paramMap.put(preparedIndex,f.get(obj));
            if(preparedIndex != 1){
                columnsBuffer.append(",");
                placeBuffer.append(",");
            }
            columnsBuffer.append("`"+tt.basic()+"`");
            placeBuffer.append("?");
            preparedIndex++;
        }
        String sql = String.format("insert into %s (%s) values (%s)",tableName,columnsBuffer.toString(),placeBuffer.toString());
        try{
            ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            for(Integer index : paramMap.keySet()){
                Object value = paramMap.get(index);
                if(value.getClass() == Integer.class){
                    ps.setInt(index, (int) value);
                }else if(value.getClass() == String.class){
                    ps.setString(index, (String) value);
                }else if(value.getClass() == Double.class){
                    ps.setDouble(index, (double) value);
                }else if(value.getClass() == Float.class){
                    ps.setFloat(index, (float) value);
                }else if(value.getClass() == Long.class){
                    ps.setLong(index, (long) value);
                }else if(value.getClass() == Date.class){
                    Date d = (Date)value;
                    ps.setTimestamp(index, new Timestamp(d.getTime()));
                }
            }
            ps.executeUpdate();
            ResultSet resultSet = ps.getGeneratedKeys();
            if (resultSet != null && resultSet.next()) {
                id=resultSet.getInt(1);
            }
        }catch (Exception ex){
            logger.error("db insert error.sql:{}", sql, ex);
            throw ex;
        }finally {
            close(ps, null, dbConnection);
        }
        logger.info("db insert,sql:{},cost:{}",sql, stopWatch.getTime());
        return id;
    }

    private String combineSql(Object obj) throws Exception{
        Class t = obj.getClass();
        Field[] fields = t.getDeclaredFields();
        Annotation[] annotations = t.getAnnotations();
        Annotation an = annotations[0];
        DBNameAnnotation entity = (DBNameAnnotation)an;
        String tableName = entity.name();
        StringBuilder columnBuffer = new StringBuilder();
        StringBuilder valueBuffer = new StringBuilder();
        columnBuffer.append("(");
        valueBuffer.append("(");
        int count = 0;
        for(Field f : fields){
            DBColumnAnnotation tt = f.getAnnotation(DBColumnAnnotation.class);
            f.setAccessible(true);
            if(f.get(obj) == null || tt == null || StringUtil.isEmpty(tt.basic())){
                continue;
            }
            count ++;
            if(count != 1){
                columnBuffer.append(",");
                valueBuffer.append(",");
            }
            if(f.getType() == Integer.class || f.getType() == int.class){
                if(f.get(obj)!=null){
                    valueBuffer.append(Integer.valueOf(f.get(obj).toString()));
                }
            }else if(f.getType() == Long.class || f.getType() == long.class){
                if(f.get(obj)!=null){
                    valueBuffer.append(Long.valueOf(f.get(obj).toString()));
                }
            }else if(f.getType() == Double.class || f.getType() == double.class){
                if(f.get(obj)!=null){
                    valueBuffer.append(Double.valueOf(f.get(obj).toString()));
                }
            }else if(f.getType() == Float.class || f.getType() == float.class){
                if(f.get(obj)!=null){
                    valueBuffer.append(Float.valueOf(f.get(obj).toString()));
                }
            }else if(f.getType().equals(String.class)){
                if(f.get(obj)!=null){
                    String temp = f.get(obj).toString();
                    temp = StringUtil.escape(temp);
                    temp = "'"+temp+"'";
                    valueBuffer.append(temp);
                }
            }else if(f.getType().equals(Date.class)) {
                Date date = (Date)f.get(obj);
                valueBuffer.append("'").append(DateUtil.formatTimeStamp(date.getTime(), "yyyy-MM-dd HH:mm:ss")).append("'");
            }
            columnBuffer.append(tt.basic());
        }

        columnBuffer.append(")");
        valueBuffer.append(")");
        return "insert into " + tableName + " " + columnBuffer.toString() + " values " + valueBuffer.toString();
    }

    @Override
    public int execute(String sql, Object... param) throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        DBConnection dbConnection = ConnectionManager.getConnection();
        Connection conn = dbConnection.getConnection();
        PreparedStatement ps = null;
        int res = 0;
        try {
            ps = conn.prepareStatement(sql);
            if (param != null) {
                for (int i = 0; i < param.length; i++) {
                    Object obj = param[i];
                    DaoBase.setParams(ps, obj, i + 1);
                }
            }
            res = ps.executeUpdate();
        }catch (Exception ex){
            logger.error("execute sql error,sql:{}",sql,ex);
        }finally {
            close(ps, null, dbConnection);
        }
        logger.debug("db execute,sql:{},cost:{}",sql, stopWatch.getTime());
        return res;
    }

    @Override
    public <T> List<Integer> insertList(List<T> paramObjectList) throws Exception {
        DBConnection dbConnection = ConnectionManager.getConnection();
        Connection conn = dbConnection.getConnection();
        List<String> sqlList = new ArrayList<>();
        for (T t : paramObjectList) {
            sqlList.add(combineSql(t));
        }
        List<Integer> ids = new ArrayList<>();
        Statement ps = conn.createStatement();
        try{
            if(!dbConnection.isTransactionSwitch()){
                conn.setAutoCommit(false);
            }
            for(String s : sqlList){
                ps.addBatch(s);
            }
            ps.executeBatch();
            if(!dbConnection.isTransactionSwitch()){
                conn.commit();
            }
            ResultSet resultSet = ps.getGeneratedKeys();
            if (resultSet != null) {
                while (resultSet.next()){
                    int id = resultSet.getInt(1);
                    ids.add(id);
                }
            }
        }catch (Exception e){
            logger.error("db insert list error!", e);
            if(!dbConnection.isTransactionSwitch()){
                conn.rollback();
            }
            throw e;
        }finally {
            close(ps, null, dbConnection);
        }
        return ids;
    }

    @Override
    public int count(String sql, Object... param) throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        DBConnection dbConnection = ConnectionManager.getConnection();
        Connection conn = dbConnection.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;
        try {
            ps = conn.prepareStatement(sql);
            if (param != null) {
                for (int i = 0; i < param.length; i++) {
                    Object obj = param[i];
                    DaoBase.setParams(ps, obj, i + 1);
                }
            }
            rs = ps.executeQuery();
            if(rs.next()) {
                count = rs.getInt(1);
            }
        }catch (Exception ex){
            logger.error("db count error,sql:{}",sql,ex);
        }finally {
            close(ps, rs, dbConnection);
        }
        logger.debug("db count,sql:{},cost:{}",sql, stopWatch.getTime());
        return count;
    }


    private void close(Statement ps,ResultSet rs,DBConnection connection) throws Exception{
        if(ps != null){
            ConnectionManager.close(ps);
        }
        if(rs != null){
            ConnectionManager.close(rs);
        }
        if(connection != null){
            ConnectionManager.close(connection);
        }
    }
}
