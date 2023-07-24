package com.dtstep.lighthouse.web.service.user.impl;
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
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.entity.department.DepartmentEntity;
import com.dtstep.lighthouse.common.entity.department.DepartmentViewEntity;
import com.dtstep.lighthouse.common.entity.list.ListViewDataObject;
import com.dtstep.lighthouse.common.entity.paging.PageEntity;
import com.dtstep.lighthouse.common.entity.user.UserEntity;
import com.dtstep.lighthouse.common.entity.user.UserViewEntity;
import com.dtstep.lighthouse.common.enums.user.UserStateEnum;
import com.dtstep.lighthouse.core.dao.ConnectionManager;
import com.dtstep.lighthouse.core.dao.DBConnection;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import com.dtstep.lighthouse.core.sql.SqlBinder;
import com.dtstep.lighthouse.web.manager.department.DepartmentManager;
import com.dtstep.lighthouse.web.manager.privilege.PrivilegeManager;
import com.dtstep.lighthouse.web.service.user.UserService;
import com.dtstep.lighthouse.web.dao.UserDao;
import com.dtstep.lighthouse.web.manager.user.UserManager;
import com.dtstep.lighthouse.web.param.ParamWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private PrivilegeManager privilegeManager;

    @Autowired
    private DepartmentManager departmentManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    private UserDao userDao;

    @Override
    public ListViewDataObject queryListByPage(int page, int state, int departmentId,String search) throws Exception {
        int totalSize = countByParams(state,departmentId,search);
        HashMap<String,Object> urlMap = new HashMap<>();
        urlMap.put("state",state);
        urlMap.put("departmentId",departmentId);
        urlMap.put("search",search);
        ListViewDataObject listObject = new ListViewDataObject();
        String baseUrl = ParamWrapper.getBaseLink("/user/list.shtml",urlMap);
        PageEntity pageEntity = new PageEntity(baseUrl, page,totalSize, SysConst._LIST_PAGE_SIZE);
        listObject.setPageEntity(pageEntity);
        List<UserViewEntity> viewEntityList = queryListByPage(state,departmentId,search,pageEntity);
        listObject.setDataList(viewEntityList);
        return listObject;
    }
    
    protected int countByParams(int state, int departmentId,String search) throws Exception {
        List<Integer> departmentIdList = null;
        if(departmentId != -1){
            List<DepartmentViewEntity> departmentList = departmentManager.queryListByPid(departmentId);
            departmentIdList = departmentList.stream().map(DepartmentEntity::getId).collect(Collectors.toList());
        }
        SqlBinder sqlBinder = new SqlBinder.Builder()
                .appendSegment("select count(1) from ldp_user")
                .appendLike("user_name",search)
                .appendWhere("state",state)
                .appendInExceptNull("department_id",departmentIdList).create();
        return DaoHelper.sql.count(sqlBinder.toString());
    }

    protected List<UserViewEntity> queryListByPage(int state, int departmentId,String search, PageEntity pageEntity) throws Exception {
        List<Integer> departmentIdList = null;
        if(departmentId != -1){
            List<DepartmentViewEntity> departmentList = departmentManager.queryListByPid(departmentId);
            departmentIdList = departmentList.stream().map(DepartmentEntity::getId).collect(Collectors.toList());
        }
        SqlBinder sqlBinder = new SqlBinder.Builder()
                .appendSegment("select * from ldp_user")
                .appendLike("user_name",search)
                .appendWhere("state",state)
                .appendInExceptNull("department_id",departmentIdList)
                .appendSegment("order by id desc limit ?,?").create();
        List<UserEntity> userEntities = DaoHelper.sql.getList(UserEntity.class,sqlBinder.toString(), pageEntity.getStartIndex() - 1, pageEntity.getPageSize());
        if(CollectionUtils.isEmpty(userEntities)){
            return null;
        }
        List<UserViewEntity> viewEntityList = new ArrayList<>();
        userEntities.forEach(z -> {
            UserViewEntity userViewEntity = new UserViewEntity(z);
            int tempDepartmentId = z.getDepartmentId();
            try{
                DepartmentViewEntity departmentEntity = departmentManager.queryViewInfoById(tempDepartmentId);
                if(departmentEntity != null){
                    userViewEntity.setDepartmentFullName(departmentEntity.getFullPathName());
                }
                viewEntityList.add(userViewEntity);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        });
        return viewEntityList;
    }

    @Override
    public List<UserEntity> termQuery(String term,int pageSize) throws Exception {
        return DaoHelper.sql.getList(UserEntity.class,
                "select id,user_name from ldp_user where state = 1 and user_name like ? limit ?,?", (term + '%'), (pageSize - 1) * 30 ,30);
    }

    @Override
    public int save(UserEntity userEntity) throws Exception {
        Date date = new Date();
        userEntity.setCreateTime(date);
        userEntity.setLastTime(date);
        return userDao.saveUser(userEntity);
    }

    @Override
    public UserEntity queryById(int id) throws Exception{
        return userDao.queryById(id);
    }

    @Override
    public int countUserName(String userName) throws Exception {
        return DaoHelper.sql.count("select count(1) from ldp_user where user_name = ?", userName);
    }

    @Override
    public int countByState(UserStateEnum stateEnum) throws Exception {
        return userManager.countByState(stateEnum);
    }

    @Override
    public void deleteById(int userId) throws Exception {
        DBConnection connection = ConnectionManager.getConnection();
        ConnectionManager.beginTransaction(connection);
        try{
            userDao.delete(userId);
            privilegeManager.removeUserPrivilege(userId);
            ConnectionManager.commitTransaction(connection);
        }catch (Exception ex){
            ConnectionManager.rollbackTransaction(connection);
            throw ex;
        }finally {
            ConnectionManager.close(connection);
        }
    }

    @Override
    public void update(UserEntity userEntity) throws Exception {
       userDao.update(userEntity);
    }

    @Override
    public void changePassword(int userId, String password) throws Exception {
        userDao.changePassword(userId,password);
    }

    @Override
    public void changeState(int userId, UserStateEnum userStateEnum) throws Exception {
        Validate.notNull(userStateEnum);
        userDao.changeState(userId,userStateEnum);
    }
}
