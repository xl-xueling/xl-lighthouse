package com.dtstep.lighthouse.common.constant;
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


public final class RedisConst {

    public static final String RT_BLOOM_DISTINCT_PREFIX = "b_d_";

    public static final String RT_HYPER_DISTINCT_PREFIX = "h_d_";

    public static final String LOCK_LIMIT_PREFIX = "lock_limit";

    public static final String LIMIT_N_PREFIX = "limitN";

    public static final String TRACK_PREFIX = "group_track";

    public static final String LOCK_TRACK_PREFIX = "lock_group_track";

    public static final String LIMITED_MESSAGE_SIZE_PREFIX = "limited_m_s";

    public static final String LIMITED_DIMENS_SIZE_PREFIX = "limited_d_s";

    public static final int DEFAULT_DISTINCT_PART_SIZE = 10;

    public static final int DISTINCT_LIMIT_THRESHOLD = 8000;

    private RedisConst(){}
}
