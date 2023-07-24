package com.dtstep.lighthouse.common.exception;
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

import com.dtstep.lighthouse.common.enums.result.RequestCodeEnum;


public class LightViewIllegalException extends RuntimeException {

    private static final long serialVersionUID = 244781924361301937L;

    private RequestCodeEnum requestCodeEnum;

    public LightViewIllegalException(RequestCodeEnum requestCodeEnum) {
        super();
        this.requestCodeEnum = requestCodeEnum;
    }

    public LightViewIllegalException(RequestCodeEnum requestCodeEnum, String message){
        super(message);
        this.requestCodeEnum = requestCodeEnum;
    }

    public LightViewIllegalException(RequestCodeEnum requestCodeEnum, String message, Throwable cause) {
        super(message, cause);
        this.requestCodeEnum = requestCodeEnum;
    }

    public LightViewIllegalException(RequestCodeEnum requestCodeEnum, Throwable cause) {
        super(cause);
        this.requestCodeEnum = requestCodeEnum;
    }

    protected LightViewIllegalException(RequestCodeEnum requestCodeEnum, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.requestCodeEnum = requestCodeEnum;
    }

    public RequestCodeEnum getRequestCodeEnum() {
        return requestCodeEnum;
    }

    public void setRequestCodeEnum(RequestCodeEnum requestCodeEnum) {
        this.requestCodeEnum = requestCodeEnum;
    }
}
