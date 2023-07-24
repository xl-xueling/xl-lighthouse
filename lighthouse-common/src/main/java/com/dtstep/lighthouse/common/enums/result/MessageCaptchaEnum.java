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


public enum MessageCaptchaEnum {

    SUCCESS(0,"正确消息"),

    PARAM_CHECK_FAILED(1,"参数校验失败"),

    OTHER_FAILED(2,"其他错误");


    private int captcha;

    private String msg;

    MessageCaptchaEnum(int captcha, String desc){
        this.captcha = captcha;
        this.msg = desc;
    }

    public int getCaptcha() {
        return captcha;
    }

    public void setCaptcha(int captcha) {
        this.captcha = captcha;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static MessageCaptchaEnum getMessageCaptcha(int captcha){
        for(MessageCaptchaEnum messageCaptchaEnum : MessageCaptchaEnum.values()){
            if(captcha == messageCaptchaEnum.getCaptcha()){
                return messageCaptchaEnum;
            }
        }
        return null;
    }
}
