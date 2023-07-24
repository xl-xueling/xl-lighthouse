package com.dtstep.lighthouse.core.batch;
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
import com.dtstep.lighthouse.core.batch.quartz.CronSequenceGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Deprecated
public class CronBatchHandler implements BatchInterface {

    private static final Logger logger = LoggerFactory.getLogger(CronBatchHandler.class);

    @Override
    public String getDisplayFormat(String timeParam,Date startTime,Date endTime) throws Exception {
        return "yyyy-MM-dd hh:mm:ss";
    }

    @Override
    public long getBatch(String timeParam, long t){
        CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(timeParam);
        return cronSequenceGenerator.next(new Date(t)).getTime();
    }

    @Override
    public long getBatch(int interval,TimeUnit timeUnit, long current) {
        return 0;
    }

    @Override
    public List<Long> queryBatchTimeList(String timeParam,long startTime,long endTime,int size) {
        CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(timeParam);
        List<Long> dates = new ArrayList<Long>();
        int i = 0;
        Date tempDate = new Date(startTime);
        while (true){
            tempDate = cronSequenceGenerator.next(tempDate);
            if(tempDate.getTime() >= endTime){
                break;
            }
            dates.add(tempDate.getTime());
            i ++;
        }
        return dates;
    }
}
