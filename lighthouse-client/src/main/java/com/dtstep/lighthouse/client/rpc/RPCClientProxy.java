package com.dtstep.lighthouse.client.rpc;
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
import com.dtstep.lighthouse.client.AuxHandler;
import com.dtstep.lighthouse.client.rpc.ice.ICERPCClientImpl;
import com.dtstep.lighthouse.client.rpc.standalone.StandaloneClientImpl;
import com.dtstep.lighthouse.common.enums.RunningMode;

public class RPCClientProxy {

    private static RPCClient instance;

    public static RPCClient instance(){
        if(instance != null){
            return instance;
        }else{
            RunningMode runningMode = AuxHandler.getRunningMode();
            if(runningMode == RunningMode.CLUSTER){
                instance = new ICERPCClientImpl();
            }else if(runningMode == RunningMode.STANDALONE){
                instance = new StandaloneClientImpl();
            }else{
                throw new RuntimeException("Running mode["+runningMode+"] not support!");
            }
            return instance;
        }
    }
}
