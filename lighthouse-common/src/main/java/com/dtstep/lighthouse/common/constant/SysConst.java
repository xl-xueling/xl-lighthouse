package com.dtstep.lighthouse.common.constant;
/*
 * Copyright (C) 2022-2024 XueLing.雪灵
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


public final class SysConst {

    private SysConst(){}

    public static final int _COMPRESS_THRESHOLD_SIZE = 256;

    public static final String _MESSAGE_PREFIX = "LDP_V1";

    public static final String _SYS_INIT_PASSWORD = "123456";

    public static final String _ENCRYPT_KEY = "1234567812345678";

    public static final int _LIST_PAGE_SIZE = 25;

    @Deprecated
    public static final int _RESULT_STORAGE_PRE_PARTITIONS_SIZE = 8;

    public static final int _DIMENS_STORAGE_PRE_PARTITIONS_SIZE = 4;

    public static String[] _DBKeyPrefixArray = new String[] { "11|", "12|", "13|", "14|","15|", "16|", "17|", "18|","19|" ,"20|" ,"21|" , "22|", "23|", "24|", "25|","26|", "27|", "28|","29|" ,"30|","31|"};

    public static final String _ADMIN_USER_NAME = "admin";

    public static final int DEPARTMENT_LEVEL_LIMIT = 3;

    public static final int RELATION_SIZE_LIMIT = 50;

    public static final String NULL_NUMERIC_VALUE = "-1";

    public static final String _VERSION = "2.1.0";

    public static final boolean _PRO_EDITION_FLAG = false;

    public static final String FULL_PATH_SPLIT_CHAR = ",";

    public static final String TREE_ROOT_NODE_NAME = "-1";

    public static final int SITE_MAP_NODE_MAX_LIMIT = 20;

    public static final int DEBUG_MODE_DURATION_TIME = 30;

    public static final String TEST_SCENE_BEHAVIOR_STAT = "test_scene_behavior_stat";

    public static final String PARAM_SIGN_KEY = "sign_key";

    public final static String AUTH_ACCESS_PARAM = "accessKey";

    public final static String AUTH_REFRESH_PARAM = "refreshKey";

    public final static String DEFAULT_ADMIN_USER = "admin";

    public final static String DEFAULT_PASSWORD = "123456";

    public final static boolean REGISTER_NEED_APPROVE = true;

    public final static int DEPARTMENT_MAX_LEVEL = 4;

    public final static int PROJECT_MAX_GROUP_SIZE = 10;

    public final static int USER_STAR_METRICSET_LIMIT = 100;

    public final static int USER_STAR_PROJECT_LIMIT = 100;
}
