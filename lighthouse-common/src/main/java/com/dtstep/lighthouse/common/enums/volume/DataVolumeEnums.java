package com.dtstep.lighthouse.common.enums.volume;
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


public enum DataVolumeEnums {

    @Deprecated
    VOLUME_1(1,1,false),

    @Deprecated
    VOLUME_2(2,2,false),

    @Deprecated
    VOLUME_3(3,3,false),

    @Deprecated
    VOLUME_4(4,5,false),

    @Deprecated
    VOLUME_5(5,8,true),

    @Deprecated
    VOLUME_6(6,10,true),

    VOLUME_DEFAULT(-1,20,false);

    @Deprecated
    private int level;

    private int prePartitions;

    @Deprecated
    private boolean isStandalone;

    DataVolumeEnums(int level, int prePartitions, boolean isStandalone){
        this.level = level;
        this.prePartitions = prePartitions;
        this.isStandalone = isStandalone;
    }

    public static DataVolumeEnums getDataVolume(int level){
        for(DataVolumeEnums dataVolumeEnums : DataVolumeEnums.values()){
            if(dataVolumeEnums.getLevel() == level){
                return dataVolumeEnums;
            }
        }
        return DataVolumeEnums.VOLUME_1;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPrePartitions() {
        return prePartitions;
    }

    public void setPrePartitions(int prePartitions) {
        this.prePartitions = prePartitions;
    }

    public boolean isStandalone() {
        return isStandalone;
    }

    public void setStandalone(boolean isStandalone) {
        this.isStandalone = isStandalone;
    }
}
