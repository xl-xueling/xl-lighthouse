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


public class ComponentsCheckException extends RuntimeException {

    private static final long serialVersionUID = 473618047053845899L;

    public ComponentsCheckException() {
        super();
    }

    public ComponentsCheckException(String message) {
        super(message);
    }

    public ComponentsCheckException(String message, Throwable cause) {
        super(message, cause);
    }

    public ComponentsCheckException(Throwable cause) {
        super(cause);
    }

    protected ComponentsCheckException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
