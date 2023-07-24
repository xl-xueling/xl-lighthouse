package com.dtstep.lighthouse.test.mode;
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
import com.apifan.common.random.source.AreaSource;
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.test.entity.BehaviorSampleEntity;
import com.github.javafaker.Faker;
import com.dtstep.lighthouse.common.key.RandomID;

import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class BehaviorModalSample implements ModalSample<BehaviorSampleEntity> {

    private static final Faker faker = new Faker(Locale.CHINA);

    private static final AreaSource areaSource = AreaSource.getInstance();

    private static final String imei_RandomId = RandomID.id(30);

    @Override
    public BehaviorSampleEntity generateSample() throws Exception {
        String uuid = RandomID.id(32);
        BehaviorSampleEntity behaviorSampleEntity = new BehaviorSampleEntity();
        behaviorSampleEntity.setBehavior_id(uuid);
        behaviorSampleEntity.setBehavior_type(ThreadLocalRandom.current().nextInt(1,5));
        behaviorSampleEntity.setAbtest_no("abtest_" + ThreadLocalRandom.current().nextInt(1,30));
        behaviorSampleEntity.setApp_version(ThreadLocalRandom.current().nextInt(1,3) + "." + ThreadLocalRandom.current().nextInt(1,5) + "." + ThreadLocalRandom.current().nextInt(1,5));
        if(ThreadLocalRandom.current().nextInt(10) < 7){
            behaviorSampleEntity.setOs("android");
        }else{
            behaviorSampleEntity.setOs("ios");
        }
        String cityStr = areaSource.randomCity(",");
        String [] cityArray = cityStr.split(",");
        behaviorSampleEntity.setProvince(cityArray[0]);
        behaviorSampleEntity.setCity(cityArray[1]);
        behaviorSampleEntity.setImei(imei_RandomId +"_" + ThreadLocalRandom.current().nextLong(5000000L));
        behaviorSampleEntity.setItem_id(String.valueOf(ThreadLocalRandom.current().nextLong(100000,1000000)));
        int topLevel = ThreadLocalRandom.current().nextInt(1,30);
        int secLevel = topLevel * 100 + ThreadLocalRandom.current().nextInt(1,40);
        behaviorSampleEntity.setTop_level(String.valueOf(topLevel));
        behaviorSampleEntity.setSec_level(String.valueOf(secLevel));
        behaviorSampleEntity.setRecall_no(ThreadLocalRandom.current().nextInt(1,100));
        double d = ThreadLocalRandom.current().nextDouble(1,999999999);
        behaviorSampleEntity.setScore(String.format("%.3f", d));
        return behaviorSampleEntity;
    }

    @Override
    public String getToken() {
        return SysConst.TEST_SCENE_BEHAVIOR_STAT;
    }
}
