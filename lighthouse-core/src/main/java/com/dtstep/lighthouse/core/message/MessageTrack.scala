package com.dtstep.lighthouse.core.message

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
import com.dtstep.lighthouse.common.constant.StatConst
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity
import com.dtstep.lighthouse.common.entity.message.LightMessage
import com.dtstep.lighthouse.common.enums.result.MessageCaptchaEnum
import com.dtstep.lighthouse.core.batch.BatchAdapter
import com.dtstep.lighthouse.core.limited.RedisLimitedAspect
import com.dtstep.lighthouse.core.redis.RedisHandler

import java.util.concurrent.TimeUnit
import com.dtstep.lighthouse.common.constant.RedisConst
import com.dtstep.lighthouse.common.util.JsonUtil._
import com.dtstep.lighthouse.common.util.Md5Util


object MessageTrack {

  private final val limiter = RedisLimitedAspect.getInstance()

  def capture(groupId:Int, captcha:Int, message:LightMessage): Unit = {
    val batchTime = BatchAdapter.getBatch(1, TimeUnit.MINUTES, System.currentTimeMillis)
    val lockTrackKey = RedisConst.LOCK_TRACK_PREFIX + "_" + groupId  + "_" + batchTime
    if(limiter.tryAcquire(lockTrackKey,5,50,TimeUnit.MINUTES.toSeconds(5),1)){
      val trackKey = RedisConst.TRACK_PREFIX + "_" + groupId;
      message.setSystemTime(System.currentTimeMillis());
      RedisHandler.getInstance().limitSet(trackKey,toJSONString(message),StatConst.GROUP_MESSAGE_MAX_CACHE_SIZE,3 * 3600)
    }
  }

  import scala.collection.JavaConverters._

  def validMessage(message:LightMessage, statGroup: GroupExtEntity):MessageCaptchaEnum = {
    val columnList = statGroup.getColumnList
    if(!MessageValid.valid(message,columnList)) return MessageCaptchaEnum.PARAM_CHECK_FAILED
    MessageCaptchaEnum.SUCCESS
  }
}
