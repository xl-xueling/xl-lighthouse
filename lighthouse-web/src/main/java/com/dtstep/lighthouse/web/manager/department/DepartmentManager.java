package com.dtstep.lighthouse.web.manager.department;
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
import com.dtstep.lighthouse.core.dao.ConnectionManager;
import com.dtstep.lighthouse.core.dao.DBConnection;
import com.google.common.collect.Lists;
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.entity.department.DepartmentEntity;
import com.dtstep.lighthouse.common.entity.department.DepartmentViewEntity;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import com.dtstep.lighthouse.web.dao.DepartmentDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DepartmentManager {

    @Autowired
    private DepartmentDao departmentDao;

    @Cacheable(value = "normal",key = "#targetClass + '_' + 'queryAllViewInfo'",cacheManager = "caffeineCacheManager",unless = "#result == null")
    public List<DepartmentViewEntity> queryAllViewInfo() throws Exception {
        List<DepartmentEntity> allList = departmentDao.queryAll();
        if(CollectionUtils.isEmpty(allList)){
            return null;
        }
        Map<Integer,DepartmentEntity> departmentEntityMap = null;
        if(CollectionUtils.isNotEmpty(allList)){
            departmentEntityMap = allList.stream().collect(Collectors.toMap(DepartmentEntity::getId, x -> x));
        }
        List<DepartmentViewEntity> departmentViewEntityList = new ArrayList<>();
        for(DepartmentEntity departmentEntity : allList){
            DepartmentViewEntity departmentViewEntity = combineViewInfo(departmentEntityMap,departmentEntity);
            if(departmentViewEntity != null){
                departmentViewEntityList.add(departmentViewEntity);
            }
        }
        return departmentViewEntityList;
    }

    @Cacheable(value = "normal",key = "#targetClass + '_' + 'queryListByPid' + '_' + #pid",cacheManager = "caffeineCacheManager",unless = "#result == null")
    public List<DepartmentViewEntity> queryListByPid(int pid) throws Exception {
        List<DepartmentViewEntity> allList = queryAllViewInfo();
        if(pid == -1){
            return allList;
        }
        List<DepartmentViewEntity> resultList = Lists.newArrayList();
        for (DepartmentViewEntity tempEntity : allList) {
            String fullPath = tempEntity.getFullPath();
            if (Arrays.asList(fullPath.split(SysConst.FULL_PATH_SPLIT_CHAR)).contains(String.valueOf(pid))) {
                resultList.add(tempEntity);
            }
        }
        return resultList;
    }

    @Cacheable(value = "normal",key = "#targetClass + '_' + 'queryViewInfoById' + '_' + #id",cacheManager = "caffeineCacheManager",unless = "#result == null")
    public DepartmentViewEntity queryViewInfoById(int id) throws Exception {
        return combineViewInfo(null,departmentDao.queryById(id));
    }

    public boolean isExist(int departmentId) throws Exception {
        return DaoHelper.sql.count("select count(1) from ldp_department where id = ?", departmentId) == 1;
    }

    public int count() throws Exception {
        return DaoHelper.sql.count("select count(1) from ldp_department");
    }

    public void init() throws Exception {
        DBConnection dbConnection = ConnectionManager.getConnection();
        ConnectionManager.beginTransaction(dbConnection);
        try{
            Date date = new Date();
            DepartmentEntity departmentEntity = new DepartmentEntity();
            departmentEntity.setCreateTime(date);
            departmentEntity.setUpdateTime(date);
            departmentEntity.setName("First-Department");
            departmentEntity.setPid(0);
            departmentEntity.setLevel(1);
            int id = departmentDao.save(departmentEntity);
            String fullPath =  String.valueOf(id);
            departmentDao.updateFullPath(fullPath,id);
            ConnectionManager.commitTransaction(dbConnection);
        }catch (Exception ex){
            ConnectionManager.rollbackTransaction(dbConnection);
            throw ex;
        }finally {
            ConnectionManager.close(dbConnection);
        }
    }

    public DepartmentViewEntity combineViewInfo(Map<Integer,DepartmentEntity> allMap, DepartmentEntity departmentEntity) throws Exception {
        if(departmentEntity == null){
            return null;
        }
        DepartmentViewEntity departmentViewEntity = new DepartmentViewEntity(departmentEntity);
        String fullPath = departmentEntity.getFullPath();
        String [] idArray = fullPath.split(SysConst.FULL_PATH_SPLIT_CHAR);
        StringBuilder sbr = new StringBuilder();
        int i = 0;
        for(String idStr : idArray){
            int tempId = Integer.parseInt(idStr);
            DepartmentEntity tempDepartment;
            if(allMap != null && allMap.containsKey(tempId)){
                tempDepartment = allMap.get(tempId);
            }else{
                tempDepartment = departmentDao.queryById(tempId);
            }
            if(i != 0){
                sbr.append("_");
            }
            sbr.append(tempDepartment.getName());
            i++;
        }
        departmentViewEntity.setFullPathName(sbr.toString());
        return departmentViewEntity;
    }
}
