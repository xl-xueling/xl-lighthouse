package com.dtstep.lighthouse.insights.dto;
/*
 * Copyright (C) 2022-2025 XueLing.雪灵
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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class ChangePasswordParam implements Serializable {

    @NotNull
    private Integer id;

    @NotEmpty
    private String username;

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9_][a-zA-Z0-9_,.#!$%]{5,32}$")
    private String originPassword;

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9_][a-zA-Z0-9_,.#!$%]{5,32}$")
    private String password;

    public ChangePasswordParam(){}

    public ChangePasswordParam(Integer id,String originPassword,String password){
        this.id = id;
        this.originPassword = originPassword;
        this.password = password;
    }

    public ChangePasswordParam(Integer id,String password){
        this.id = id;
        this.originPassword = originPassword;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginPassword() {
        return originPassword;
    }

    public void setOriginPassword(String originPassword) {
        this.originPassword = originPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
