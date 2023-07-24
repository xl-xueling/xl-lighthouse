package com.dtstep.lighthouse.tasks.init

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

import com.dtstep.lighthouse.core.config.LDPConfig
import org.apache.spark.SparkFiles
import org.apache.spark.internal.Logging
import org.slf4j.LoggerFactory

private[tasks] object InitialServant extends Logging {

  private val logger = LoggerFactory.getLogger(InitialServant.getClass)

  val init: Unit = {
    try{
      if(!LDPConfig.isInit.get()){
        val path = SparkFiles.get("ldp-site.xml")
        LDPConfig.init(path)
        logInfo("lighthouse init success!")
      }
    }catch {
      case ex:Exception =>
        logError("lighthouse init error!",ex);
        throw ex;
    }
  }

  val isInit:Boolean = {
    LDPConfig.isInit.get();
  }
}
