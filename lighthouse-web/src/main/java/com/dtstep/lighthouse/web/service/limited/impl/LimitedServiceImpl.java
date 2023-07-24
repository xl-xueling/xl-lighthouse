package com.dtstep.lighthouse.web.service.limited.impl;
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
import com.dtstep.lighthouse.common.entity.limiting.LimitingEntity;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import com.dtstep.lighthouse.core.wrapper.StatDBWrapper;
import com.dtstep.lighthouse.web.service.limited.LimitedService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LimitedServiceImpl implements LimitedService {

    private static final int LIST_PAGE_SIZE = 10;

    @Override
    public List<LimitingEntity> queryStatLimitedListByPage(int statId, int page) throws Exception {
        StatExtEntity statExtEntity = StatDBWrapper.queryById(statId);
        int groupId = statExtEntity.getGroupId();
        return DaoHelper.sql.getList(LimitingEntity.class,"select * from ldp_limited_records " +
                        "where ((relation_id = ? and relation_type = ?) or (relation_id = ? and relation_type = ?)) order by create_time desc limit ?,? "
                ,groupId,1,statId,2,(page - 1) * LIST_PAGE_SIZE,LIST_PAGE_SIZE);
    }

    @Override
    public List<LimitingEntity> queryGroupLimitedListByPage(int groupId, int page) throws Exception {
        return DaoHelper.sql.getList(LimitingEntity.class,"select * from ldp_limited_records " +
                        "where (relation_id = ? and relation_type = ?) order by create_time desc limit ?,? "
                ,groupId,1,(page - 1) * LIST_PAGE_SIZE,LIST_PAGE_SIZE);
    }
}
