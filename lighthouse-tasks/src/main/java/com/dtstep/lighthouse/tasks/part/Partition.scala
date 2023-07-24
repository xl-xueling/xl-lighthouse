package com.dtstep.lighthouse.tasks.part

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

import com.dtstep.lighthouse.common.entity.group.GroupExtEntity
import com.dtstep.lighthouse.common.entity.message.LightMessage
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity
import com.dtstep.lighthouse.common.enums.limiting.LimitingStrategyEnum
import com.dtstep.lighthouse.core.wrapper.StatDBWrapper
import org.apache.commons.collections.CollectionUtils
import org.apache.spark.sql.Dataset

import scala.collection.JavaConverters.asScalaBufferConverter

private[tasks] trait Partition[T] extends Serializable {

  def part(ds:Dataset[(Int,LightMessage)]) :Dataset[T]

  def getEffectiveStats(groupId:Int):List[StatExtEntity] = {
    val list = StatDBWrapper.queryRunningListByGroupId(groupId);
    if(CollectionUtils.isEmpty(list)) Nil else list.asScala.toList;
  }

  def getThreshold(group:GroupExtEntity, strategy:LimitingStrategyEnum):Int = {
    group.getLimitedThresholdMap.getOrDefault(strategy.getStrategy,-1);
  }
}
