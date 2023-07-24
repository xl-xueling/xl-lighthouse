package com.dtstep.lighthouse.web.manager.tasks;
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
import com.dtstep.lighthouse.common.entity.paging.PageEntity;
import com.dtstep.lighthouse.common.entity.tasks.TasksHistoryEntity;
import com.dtstep.lighthouse.common.enums.tasks.TaskTypeEnum;
import com.dtstep.lighthouse.core.dao.ConnectionManager;
import com.dtstep.lighthouse.core.dao.DBConnection;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TasksHistoryManager{

    private static final Logger logger = LoggerFactory.getLogger(TasksHistoryManager.class);

    public int tryRequire(TasksHistoryEntity tasksHistoryEntity) throws Exception {
        DBConnection dbConnection = ConnectionManager.getConnection();
        ConnectionManager.beginTransaction(dbConnection);
        int taskType = tasksHistoryEntity.getTaskType();
        String batch = tasksHistoryEntity.getBatch();
        int id = 0;
        try{
            boolean needExecute = DaoHelper.sql.count("select count(1) from ldp_tasks_history where task_type = ? and batch = ? for update",taskType,batch) == 0;
            if(needExecute){
                id = DaoHelper.sql.insert(tasksHistoryEntity);
            }
            ConnectionManager.commitTransaction(dbConnection);
        }catch (Exception ex){
            ConnectionManager.rollbackTransaction(dbConnection);
            throw ex;
        }finally {
            ConnectionManager.close(dbConnection);
        }
        return id;
    }

    public void update(TasksHistoryEntity entity) throws Exception{
        DaoHelper.sql.execute("update ldp_tasks_history set end_time = ?,state = ?,task_result = ? where id = ?",entity.getEndTime(),entity.getState(),entity.getTaskResult(),entity.getId());
    }

    public List<TasksHistoryEntity> queryList(TaskTypeEnum taskTypeEnum, PageEntity pageEntity) throws Exception {
        return DaoHelper.sql.getList(TasksHistoryEntity.class,"select * from ldp_tasks_history where task_type = ? order by start_time desc limit ?,?"
                , taskTypeEnum.getTaskType(),pageEntity.getStartIndex() - 1, pageEntity.getPageSize());
    }

    public int count(TaskTypeEnum taskTypeEnum) throws Exception {
        return DaoHelper.sql.count("select count(1) from ldp_tasks_history where task_type = ?", taskTypeEnum.getTaskType());
    }
}
