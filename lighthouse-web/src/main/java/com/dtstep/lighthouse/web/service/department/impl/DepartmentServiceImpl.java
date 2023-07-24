package com.dtstep.lighthouse.web.service.department.impl;
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
import com.dtstep.lighthouse.common.entity.tree.ZTreeViewNode;
import com.dtstep.lighthouse.core.dao.ConnectionManager;
import com.dtstep.lighthouse.core.dao.DBConnection;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import com.dtstep.lighthouse.core.sql.SqlBinder;
import com.dtstep.lighthouse.web.manager.department.DepartmentManager;
import com.dtstep.lighthouse.web.dao.DepartmentDao;
import com.dtstep.lighthouse.web.param.ParamWrapper;
import com.dtstep.lighthouse.web.service.department.DepartmentService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private DepartmentManager departmentManager;

    @Override
    public ListViewDataObject queryListByPage(int page, int level,String search) throws Exception {
        int totalSize = countByParams(level,search);
        HashMap<String,Object> urlMap = new HashMap<>();
        urlMap.put("level",level);
        urlMap.put("search",search);
        ListViewDataObject listObject = new ListViewDataObject();
        String baseUrl = ParamWrapper.getBaseLink("/department/list.shtml",urlMap);
        PageEntity pageEntity = new PageEntity(baseUrl, page,totalSize, SysConst._LIST_PAGE_SIZE);
        listObject.setPageEntity(pageEntity);
        List<DepartmentViewEntity> viewEntityList = queryListByPage(level,search,pageEntity);
        listObject.setDataList(viewEntityList);
        return listObject;
    }

    protected List<DepartmentViewEntity> queryListByPage(int level, String search, PageEntity pageEntity) throws Exception {
        SqlBinder sqlBinder = new SqlBinder.Builder()
                .appendSegment("select id from ldp_department")
                .appendWhere("level",level)
                .appendLike("name",search)
                .appendSegment("limit ?,?").create();
        List<Integer> departmentIdList = DaoHelper.sql.getList(Integer.class,sqlBinder.toString(),pageEntity.getStartIndex() - 1, pageEntity.getPageSize());
        if(CollectionUtils.isEmpty(departmentIdList)){
            return null;
        }
        List<DepartmentViewEntity> departmentViewEntityList = new ArrayList<>();
        for(Integer id : departmentIdList){
            DepartmentViewEntity departmentViewEntity = departmentManager.queryViewInfoById(id);
            if(departmentViewEntity != null){
                departmentViewEntityList.add(departmentViewEntity);
            }
        }
        return departmentViewEntityList;
    }

    @Override
    public DepartmentEntity queryById(int id) throws Exception {
        return departmentDao.queryById(id);
    }

    protected int countByParams(int level,String search) throws Exception {
        SqlBinder sqlBinder = new SqlBinder.Builder()
                .appendSegment("select count(1) from ldp_department")
                .appendWhere("level",level)
                .appendLike("name",search).create();
        return DaoHelper.sql.count(sqlBinder.toString());
    }

    @Override
    public int save(DepartmentEntity departmentEntity) throws Exception {
        DBConnection dbConnection = ConnectionManager.getConnection();
        ConnectionManager.beginTransaction(dbConnection);
        int id;
        try{
            id = departmentDao.save(departmentEntity);
            int pid = departmentEntity.getPid();
            String fullPath;
            if(pid == 0){
                fullPath = String.valueOf(id);
            }else{
                DepartmentEntity parent = queryById(pid);
                fullPath = parent.getFullPath() + SysConst.FULL_PATH_SPLIT_CHAR + id;
            }
            departmentDao.updateFullPath(fullPath,id);
            ConnectionManager.commitTransaction(dbConnection);
        }catch (Exception ex){
            ConnectionManager.rollbackTransaction(dbConnection);
            throw ex;
        }finally {
            ConnectionManager.close(dbConnection);
        }
        return id;
    }

    @Override
    public void update(DepartmentEntity departmentEntity) throws Exception {
        departmentDao.update(departmentEntity);
    }

    @Override
    public void delete(int id) throws Exception {
        departmentDao.delete(id);
    }

    @Override
    public int countChild(int pid) throws Exception {
        return DaoHelper.sql.count("select count(1) from ldp_department where pid = ?", pid);
    }

    @Override
    public boolean isExist(int departmentId) throws Exception {
        return DaoHelper.sql.count("select count(1) from ldp_department where id = ?", departmentId) == 1;
    }

    @Override
    public List<ZTreeViewNode> queryTreeInfo(int pid) throws Exception {
        List<ZTreeViewNode> viewNodeList = new ArrayList<>();
        String [] treeOpenArray = null;
        if(pid != 0){
            DepartmentEntity tempDepartment = queryById(pid);
            treeOpenArray = tempDepartment.getFullPath().split(SysConst.FULL_PATH_SPLIT_CHAR);
        }
        ZTreeViewNode rootNode = new ZTreeViewNode();
        rootNode.setId("0");
        rootNode.setpId(SysConst.TREE_ROOT_NODE_NAME);
        rootNode.setName("i18n(ldp_i18n_department_manage_1007)");
        rootNode.setIcon("/static/extend/png/root.png");
        rootNode.setOpen(true);
        viewNodeList.add(rootNode);
        List<DepartmentEntity> departmentEntityList = departmentDao.queryAll();
        if(CollectionUtils.isNotEmpty(departmentEntityList)){
            for(DepartmentEntity obj : departmentEntityList){
                ZTreeViewNode zTreeViewNode = new ZTreeViewNode();
                zTreeViewNode.setId(String.valueOf(obj.getId()));
                zTreeViewNode.setpId(String.valueOf(obj.getPid()));
                zTreeViewNode.setName(obj.getName());
                if(treeOpenArray != null && Arrays.asList(treeOpenArray).contains(String.valueOf(obj.getId()))){
                    zTreeViewNode.setOpen(true);
                }
                viewNodeList.add(zTreeViewNode);
            }
        }
        return viewNodeList;
    }
}
