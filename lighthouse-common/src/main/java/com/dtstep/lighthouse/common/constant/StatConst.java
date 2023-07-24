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


import java.util.*;

public final class StatConst {

    private StatConst(){}

    public static final String SEPARATOR_LEVEL_0 = "\u0003";

    public static final String SEPARATOR_LEVEL_1 = "\u0004";

    public static final String SEPARATOR_LEVEL_2 = "\u0005";

    public static final String SEPARATOR_LEVEL_3 = "\u0006";

    public static final int NORMAL_BATCH_INTERVAL = 5;

    public static final int SEQUENCE_BATCH_INTERVAL = 5;

    public static final int LIMIT_BATCH_INTERVAL = 10;

    public static final String PREFIX_FORMULA_VARIABLE = "$";

    public static final String MULTI_PAIR_SEPARATOR = ",";

    public static final String K_V_MAPPING_SEPARATOR = "##";

    public static final String DIMENS_SEPARATOR = ";";

    public static final String DEFAULT_SPLIT_PLACEHOLDER = "_";

    public static final long STAT_MAXIMUM = 100000000000000L;

    public static final int BLOOM_MAX_THRESHOLD = 50000000;

    public static final long RESULT_MAXIMUM_CONTENT_SIZE = 322122547200L;

    public static final int GROUP_MAX_STAT_SIZE = 50;

    public static final String SYSTEM_STAT_RESULT_TABLE = "ldp_system_result";

    public static final String DIMENS_STORAGE_TABLE = "ldp_dimens_records";

    public static final int DIMENS_THRESHOLD_LIMIT_COUNT = 500;

    public static final int DIMENS_VALUE_STORAGE_DURATION_MAX_SIZE = 600;

    public static final int GROUP_MESSAGE_MAX_CACHE_SIZE = 1000;

    public static final int LIMIT_MAX_SIZE = 200;

    public static final int LIMIT_MAX_CACHE_SIZE = 5000;

    public static final String NULL_STR = "null";

    public static final long NIL_VAL = 999999999999999998L;

    public static final long ILLEGAL_VAL = 999999999999999997L;

    public static final long POSITIVE_INFINITY = 999999999999999999L;

    public static final long NEGATIVE_INFINITY = -999999999999999999L;

    public static final int LIMITED_EXPIRE_MINUTES = 15;

    public static final int EVENT_POOL_PUT_COST_THRESHOLD = 10000;

    public static final int DEBUG_MODEL_EXPIRE_MINUTES = 60;

    public static final String DB_RESULT_STORAGE_COLUMN = "v";

    public static final String DB_RESULT_STORAGE_EXTEND_COLUMN = "_1";

    public static final String DISTINCT_COLUMN_NAME = "_d";

    public static final int LIMIT_SALT = 3;

    public static final int DEFAULT_POOL_SLOT_SIZE = 8;

    public static final int QUERY_RESULT_DIMENS_SIZE = 50;

    public static final int QUERY_RESULT_LIMIT_SIZE = 100000;

    public static final int EXPORT_RESULT_LIMIT_SIZE = 300000;

    public static final Set<String> _KeyWordsSet = new HashSet<>(Arrays.asList("count","bitcount","token","sum","max","min","seq","avg","default","distinct","stat","limit","goto","if","else","then","stat-item","dimens","timestamp","where","while","for","loop","variable","variables","const"));

    public static final String BUILTIN_MSG_STAT = "_builtin_msg_stat";

    public static final String BUILTIN_RESULT_STAT = "_builtin_result_stat";

    public static final double client_backlog_factor = 3.0d;

    public static final double backlog_factor = 5.0d;

    public static final double storage_backlog_factor = 8.0d;

}
