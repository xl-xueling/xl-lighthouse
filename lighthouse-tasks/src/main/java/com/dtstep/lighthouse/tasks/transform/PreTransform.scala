package com.dtstep.lighthouse.tasks.transform

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

import com.dtstep.lighthouse.common.entity.message.LightMessage
import com.dtstep.lighthouse.tasks.extract.DefaultMessageExtract
import com.dtstep.lighthouse.tasks.valid.DefaultValidHandler
import org.apache.spark.sql.{Dataset, SparkSession}


private[tasks] class PreTransform(spark:SparkSession) extends Transform[(Int,LightMessage)] with scala.Serializable{

  private val extract = new DefaultMessageExtract(spark)

  private val valid = new DefaultValidHandler(spark)

  override def process(ds: Dataset[String]): Dataset[(Int,LightMessage)] = {
    valid.valid(extract.extract(ds))
  }
}
