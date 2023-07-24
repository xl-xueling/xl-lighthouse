package com.dtstep.lighthouse.core.limited.device;
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
import com.dtstep.lighthouse.core.callback.CacheValue;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.enums.limiting.LimitingStrategyEnum;

public interface CountingDevice {

    boolean tryRequire(Params params) throws Exception;

    class Params {

        private StatExtEntity builtinStat;

        private int relationId;

        private int step;

        private int permitsPerSecond;

        private LimitingStrategyEnum strategyEnum;

        public Params(){}

        public Params(LimitingStrategyEnum strategyEnum, StatExtEntity builtInStat, int relationId, int permitsPerSecond, int step){
            this.strategyEnum = strategyEnum;
            this.builtinStat = builtInStat;
            this.relationId = relationId;
            this.permitsPerSecond = permitsPerSecond;
            this.step = step;
        }

        public StatExtEntity getBuiltinStat() {
            return builtinStat;
        }

        public void setBuiltinStat(StatExtEntity builtinStat) {
            this.builtinStat = builtinStat;
        }

        public int getRelationId() {
            return relationId;
        }

        public void setRelationId(int relationId) {
            this.relationId = relationId;
        }

        public int getStep() {
            return step;
        }

        public void setStep(int step) {
            this.step = step;
        }

        public int getPermitsPerSecond() {
            return permitsPerSecond;
        }

        public void setPermitsPerSecond(int permitsPerSecond) {
            this.permitsPerSecond = permitsPerSecond;
        }

        public LimitingStrategyEnum getStrategyEnum() {
            return strategyEnum;
        }

        public void setStrategyEnum(LimitingStrategyEnum strategyEnum) {
            this.strategyEnum = strategyEnum;
        }
    }

    class Value extends CacheValue {

        private long v;

        public long getV() {
            return v;
        }

        public void setV(long v) {
            this.v = v;
        }

        public static Value newVar(){
            Value value = new Value();
            value.setCreateTime(System.currentTimeMillis());
            value.setAccessTime(System.currentTimeMillis());
            return value;
        }
    }
}
