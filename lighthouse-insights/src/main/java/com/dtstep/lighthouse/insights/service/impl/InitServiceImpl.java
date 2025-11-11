package com.dtstep.lighthouse.insights.service.impl;
/*
 * Copyright (C) 2022-2025 XueLing.雪灵
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
import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.random.RandomID;
import com.dtstep.lighthouse.common.util.Md5Util;
import com.dtstep.lighthouse.common.enums.OwnerTypeEnum;
import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.common.modal.Department;
import com.dtstep.lighthouse.common.modal.Domain;
import com.dtstep.lighthouse.common.modal.Role;
import com.dtstep.lighthouse.common.modal.User;
import com.dtstep.lighthouse.core.storage.cmdb.CMDBStorageEngine;
import com.dtstep.lighthouse.core.storage.cmdb.CMDBStorageEngineProxy;
import com.dtstep.lighthouse.core.storage.warehouse.WarehouseStorageEngine;
import com.dtstep.lighthouse.core.storage.warehouse.WarehouseStorageEngineProxy;
import com.dtstep.lighthouse.core.tools.CMDBUtil;
import com.dtstep.lighthouse.insights.service.*;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class InitServiceImpl implements InitService {

    private static final Logger logger = LoggerFactory.getLogger(InitServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DomainService domainService;

    @Transactional
    @Override
    public void initRole() {
        if(!roleService.isRoleExist(RoleTypeEnum.FULL_MANAGE_PERMISSION,0)){
            Role fullManageRole = new Role(RoleTypeEnum.FULL_MANAGE_PERMISSION,0);
            int result = roleService.create(fullManageRole);
            Validate.isTrue(result > 0);
        }
        if(!roleService.isRoleExist(RoleTypeEnum.FULL_ACCESS_PERMISSION,0)){
            Role fullAccessRole = new Role(RoleTypeEnum.FULL_ACCESS_PERMISSION,0);
            int result = roleService.create(fullAccessRole);
            Validate.isTrue(result > 0);
        }
        if(!roleService.isRoleExist(RoleTypeEnum.OPT_MANAGE_PERMISSION,0)){
            Role parentRole = roleService.cacheQueryRole(RoleTypeEnum.FULL_MANAGE_PERMISSION,0);
            Role optManageRole = new Role(RoleTypeEnum.OPT_MANAGE_PERMISSION,0, parentRole.getId());
            int result = roleService.create(optManageRole);
            Validate.isTrue(result > 0);
        }
        if(!roleService.isRoleExist(RoleTypeEnum.OPT_ACCESS_PERMISSION,0)){
            Role parentRole = roleService.cacheQueryRole(RoleTypeEnum.FULL_ACCESS_PERMISSION,0);
            Role optAccessRole = new Role(RoleTypeEnum.OPT_ACCESS_PERMISSION,0, parentRole.getId());
            int result = roleService.create(optAccessRole);
            Validate.isTrue(result > 0);
        }
    }

    @Transactional
    @Override
    public void initDepartment() {
        int level = departmentService.getChildLevel(0);
        if(level == 0){
            Department department = new Department();
            department.setName("First Department");
            department.setPid(0);
            int result = departmentService.create(department);
            Validate.isTrue(result > 0);
        }
    }

    @Transactional
    @Override
    public void initAdmin() throws Exception{
        if(!userService.isUserNameExist(SysConst.DEFAULT_ADMIN_USER)){
            List<Department> departmentList = departmentService.queryAll();
            Validate.notNull(departmentList);
            int adminId;
            User user = new User();
            user.setUsername(SysConst.DEFAULT_ADMIN_USER);
            user.setDepartmentId(departmentList.get(0).getId());
            user.setPassword(Md5Util.getMD5(SysConst.DEFAULT_PASSWORD));
            userService.create(user,false);
            adminId = user.getId();
            Validate.isTrue(adminId != 0);
            int result = resourceService.grantPermission(adminId, OwnerTypeEnum.USER,0, RoleTypeEnum.OPT_MANAGE_PERMISSION);
            Validate.isTrue(result > 0);
        }else{
            User user = userService.queryAllInfoByUserName(SysConst.DEFAULT_ADMIN_USER);
            Role role = roleService.cacheQueryRole(RoleTypeEnum.OPT_MANAGE_PERMISSION,0);
            Validate.notNull(role);
            if(!permissionService.existPermission(user.getId(),OwnerTypeEnum.USER,role.getId())){
                int result = permissionService.grantPermission(user.getId(),OwnerTypeEnum.USER,role.getId());
                Validate.isTrue(result > 0);
            }
        }
    }


    @Transactional
    @Override
    public void initDefaultDomain() throws Exception {
        Domain defaultDomain = domainService.queryDefault();
        if(defaultDomain == null){
            Domain domain = new Domain();
            LocalDateTime localDateTime = LocalDateTime.now();
            domain.setCreateTime(localDateTime);
            domain.setUpdateTime(localDateTime);
            domain.setName("Default-Domain-" + System.currentTimeMillis());
            domain.setDefaultTokenPrefix(RandomID.id(3));
            int result = domainService.create(domain);
            Validate.isTrue(result > 0);
        }
    }

    @Override
    public void initStorageEngine() throws Exception {
        WarehouseStorageEngine dbEngine = WarehouseStorageEngineProxy.getInstance();
        String namespace = dbEngine.getDefaultNamespace();
        dbEngine.createNamespaceIfNotExist(namespace);
        String dimensTableName = StatConst.DIMENS_STORAGE_TABLE;
        if(!dbEngine.isTableExist(dimensTableName)){
            dbEngine.createDimensTable(dimensTableName);
        }
        String sysStatTableName = StatConst.SYSTEM_STAT_RESULT_TABLE;
        if(!dbEngine.isTableExist(sysStatTableName)){
            dbEngine.createResultTable(sysStatTableName);
        }
    }

    @Override
    public void createCMDBTablesIfNotExist() throws Exception {
        String ldpHome = System.getenv("LDP_HOME");
        String upgradeSqlFile = ldpHome + "/conf/ldp_upgrade.sql";
        File sqlFile = new File(upgradeSqlFile);
        if (!sqlFile.exists()) {
            logger.info("cmdb database update file does not exist.");
            return;
        }
        CMDBStorageEngine<Connection> storageEngine = CMDBStorageEngineProxy.getInstance();
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = storageEngine.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            BufferedReader br = new BufferedReader(new FileReader(upgradeSqlFile));
            String line;
            StringBuilder sql = new StringBuilder();
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("--") || line.startsWith("//") || line.startsWith("/*")) {
                    continue;
                }
                sql.append(line);
                if (line.endsWith(";")) {
                    try {
                        stmt.execute(sql.toString());
                    } catch (SQLException ex) {
                        logger.error("execute cmdb upgrade error!",ex);
                    }
                    sql.setLength(0);
                }
            }
            conn.commit();
            br.close();
        } catch (SQLException | IOException e) {
            logger.error("execute update cmdb error!",e);
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw e;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    @Override
    public void createCMDBColumnsIfNotExist() throws Exception {
        CMDBUtil.addColumnIfNotExist("ldp_relations","config","MEDIUMTEXT");
        CMDBUtil.addColumnIfNotExist("ldp_views","caller_id","INT");
        CMDBUtil.addColumnIfNotExist("ldp_views","sharelink_enabled","TINYINT(1)");
    }

    @Override
    public void createCMDBIndexIfNotExist() throws Exception {
        CMDBUtil.addIndexIfNotExist("ldp_alarms","index_uniqueCode","unique_code");
    }
}
