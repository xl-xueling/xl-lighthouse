package com.dtstep.lighthouse.tasks.stream

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
import com.dtstep.lighthouse.tasks.part.ItemStatPartition
import org.apache.spark.sql.{Dataset, SparkSession}


private[tasks] class NormalStream(spark:SparkSession) extends Stream[(Int,LightMessage),(Int,String,Int)] with scala.Serializable{

  private val partition = new ItemStatPartition(spark)

  override def part(ds:Dataset[(Int,LightMessage)]): Dataset[(Int,String,Int)] = partition.part(ds)

}
