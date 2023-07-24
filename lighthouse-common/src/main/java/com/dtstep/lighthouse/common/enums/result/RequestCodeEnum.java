package com.dtstep.lighthouse.common.enums.result;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


public enum RequestCodeEnum {

    SUCCESS("0","Success"),

    SYSTEM_ERROR("ldp_i18n_common_1001","系统异常"),

    REQUEST_PARAM_MISSING("ldp_i18n_common_1002","请求参数缺失"),

    PARAM_FORMAT_ERROR("ldp_i18n_common_1003","参数格式错误"),

    ILLEGAL_PARAMS("ldp_i18n_common_1004","参数格式不合法"),

    AUTHORITY_LIMIT("ldp_i18n_common_1005","权限限制执行该操作"),

    USER_LOGIN_FAILURE("ldp_i18n_user_login_1010","用户名或密码错误"),

    USER_STATUS_NOT_AVAILABLE("ldp_i18n_user_login_1011","该用户状态不可用，请联系管理员审核"),

    USER_REGISTER_USERNAME_EXIST("ldp_i18n_user_register_1015","用户名已存在"),

    USER_PASSWORD_NOT_RIGHT("ldp_i18n_change_password_1011","原密码输入错误"),

    USER_ADMIN_USERNAME_UPDATE_ERROR("ldp_i18n_update_user_1014","不可修改admin账户的用户名"),

    UNAUTHORIZED_ACCESS("ldp_i18n_authorize_1007","系统访问未经授权"),

    UNAUTHORIZED_ULIMIT_ACCESS("ldp_i18n_authorize_1008","系统授权受限，注册用户数超出限制"),

    AUTHORIZE_FAILED("ldp_i18n_authorize_1009","系统授权失败，注册码错误"),

    DEPARTMENT_DELETE_HAS_CHILD_NODE("ldp_i18n_department_delete_1004","该部门下面有子部门不可删除"),

    DEPARTMENT_ID_NOT_EXIST("ldp_i18n_department_update_1010","部门ID不存在"),

    DEPARTMENT_ADD_PID_NOT_EXIST("ldp_i18n_department_create_1009","部门PID不存在"),

    DEPARTMENT_ADD_NAME_EXIST("ldp_i18n_department_create_1010","部门名称已存在"),

    DEPARTMENT_LIMIT_MAX_LEVEL("ldp_i18n_department_create_1011","部门层级最大限制"),

    DEPARTMENT_DELETE_HAS_STAT_PROJECT("ldp_i18n_department_delete_1005","该部门下面有统计工程不可删除"),

    DEPARTMENT_DELETE_HAS_USER("ldp_i18n_department_delete_1006","该部门下面有用户不可删除"),

    PROJECT_NAME_ALREADY_EXIST("ldp_i18n_project_create_1023","统计工程名称已存在"),

    PROJECT_ID_NOT_EXIST("ldp_i18n_project_create_1024","统计工程ID不存在"),

    PROJECT_NOT_HAS_STAT_ITEM("ldp_i18n_project_list_1017","该统计工程下没有统计项存在"),

    PROJECT_HAVE_STAT_GROUP("ldp_i18n_project_delete_1001","该统计工程下有统计组存在"),

    GROUP_HAVE_STAT_ITEM("ldp_i18n_group_delete_1001","该统计组下有统计任务存在"),

    GROUP_ITEMS_EXCEEDS_LIMIT("ldp_i18n_group_create_1043","统计项数量超出统计组限制"),

    GROUP_TOKEN_EXIST("ldp_i18n_group_create_1044","统计组Token已存在"),

    GROUP_COLUMN_PROHIBITED("ldp_i18n_group_create_1045","禁止使用字段名【%s】"),

    TEMPLATE_FORMAT_ERROR("ldp_i18n_group_create_1046","统计项配置解析错误"),

    STAT_AT_LEAST_ONE("ldp_i18n_group_create_1047","统计项至少存在一个"),

    APPLY_NOT_SUPPORT("ldp_i18n_privilege_apply_1023","不支持该权限类型申请"),

    APPLY_HAS_ROLE_ALREADY("ldp_i18n_privilege_apply_1024","已经有该权限，不可重复申请"),

    APPLY_NOT_SUPPORT_RETRACT("ldp_i18n_privilege_apply_1025","该申请不支持撤回"),

    RELATIONS_EXCEED_LIMIT("ldp_i18n_favorite_stat_1004","搜藏数量超出限制"),

    APPROVE_NOT_AUTHORITY("ldp_i18n_privilege_approve_1011","不具有审批权限"),

    COLUMN_NAME_NOT_EXIST("ldp_i18n_display_1001","字段名【%s】不存在"),

    COLUMN_NAME_DUPLICATE("ldp_i18n_display_1002","筛选参数【%s】重复"),

    NO_FILTER_PARAMS_SELECTED("ldp_i18n_display_1003","该统计项没有可选择的筛选参数"),

    DISPLAY_NOT_MATCH_STAT("ldp_i18n_display_1004","暂无数据，没有匹配的统计项【dimens：%s】"),

    DISPLAY_QUERY_RESULT_EXCEED_LIMIT("ldp_i18n_display_1005","单次查询结果量超出限制"),

    DISPLAY_QUERY_DIMENS_EXCEED_LIMIT("ldp_i18n_display_1006","单次查询维度数量超出限制"),

    DISPLAY_EXPORT_RESULT_EXCEED_LIMIT("ldp_i18n_display_1007","单次导出结果量超出限制"),

    SITEMAP_STRUCT_EMPTY("ldp_i18n_sitemap_manage_1010","数据地图结构不可为空"),

    SITEMAP_NODES_EXCEED_LIMIT("ldp_i18n_sitemap_manage_1031","树结构节点数超出最大限制"),

    SITEBIND_ALREADY_EXIST("ldp_i18n_sitebind_update_1007","数据地图绑定关系已存在"),

    ;

    private String code;

    private String msg;

    RequestCodeEnum(String code, String msg){
        this.code = code;
        this.msg = msg;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static ObjectNode toJSON(Exception ex){
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("code", "-1");
        objectNode.put("msg",ex.getLocalizedMessage());
        return objectNode;
    }

    public static ObjectNode toJSON(RequestCodeEnum requestCodeEnum){
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("code", requestCodeEnum.getCode());
        objectNode.put("msg", String.format("i18n(%s)",requestCodeEnum.getCode()));
        return objectNode;
    }

    public static ObjectNode toJSON(RequestCodeEnum requestCodeEnum, String params){
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("code", requestCodeEnum.getCode());
        objectNode.put("msg", String.format("i18n(%s,%s)",requestCodeEnum.getCode(),params));
        return objectNode;
    }

    public static ObjectNode toJSON(String msg){
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("code", "-1");
        try{
            objectNode.put("msg", msg);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return objectNode;
    }
}
