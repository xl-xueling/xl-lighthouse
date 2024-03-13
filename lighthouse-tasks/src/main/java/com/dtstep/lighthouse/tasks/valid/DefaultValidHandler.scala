package com.dtstep.lighthouse.tasks.valid

/*
 * Copyright (C) 2022-2024 XueLing.雪灵
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

import com.dtstep.lighthouse.common.constant.{RedisConst, StatConst}
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity
import com.dtstep.lighthouse.common.entity.message.LightMessage
import com.dtstep.lighthouse.common.enums.{GroupStateEnum, LimitingStrategyEnum}
import com.dtstep.lighthouse.common.enums.result.MessageCaptchaEnum
import com.dtstep.lighthouse.common.util.{DateUtil, JsonUtil}
import com.dtstep.lighthouse.common.util.JsonUtil.toJSONString
import com.dtstep.lighthouse.core.batch.BatchAdapter
import com.dtstep.lighthouse.core.limited.{LimitedContext, RedisLimitedAspect}
import com.dtstep.lighthouse.core.message.MessageValid
import com.dtstep.lighthouse.core.redis.RedisHandler
import com.dtstep.lighthouse.core.wrapper.GroupDBWrapper
import org.apache.spark.SparkEnv
import org.apache.spark.sql.{Dataset, SparkSession}
import org.slf4j.LoggerFactory

import java.util.concurrent.TimeUnit

private[tasks] class DefaultValidHandler(spark: SparkSession) extends ValidHandler[Int,LightMessage] {

  private val logger = LoggerFactory.getLogger(classOf[DefaultValidHandler]);

  import spark.implicits._;

  import spark.sqlContext.implicits._;

  private val executorSize = SparkEnv.get.conf.get("spark.executor.instances");

  override def valid(message: LightMessage): (Int,LightMessage) = try{
    val groupEntity = GroupDBWrapper.queryById(message.getGroupId);
    if(groupEntity == null || groupEntity.getState != GroupStateEnum.RUNNING) return null
    val threshold = getThreshold(groupEntity,LimitingStrategyEnum.GROUP_MESSAGE_SIZE_LIMITING);
    if (!LimitedContext.getInstance().tryAcquire(groupEntity,threshold,message.getRepeat)) {
      logger.error(s"limited trigger strategy:GROUP_MESSAGE_SIZE_LIMIT," +
        s"group id:${groupEntity.getId},threshold:${threshold * 60L}")
      return null;
    }
    val resultCodeEnum = validMessage(message,groupEntity);
    if(groupEntity.getDebugMode == 1){
      capture(groupEntity.getId,message);
    }
    if(logger.isTraceEnabled()){
      logger.trace(s"The group of message matching is:${groupEntity.getId},valid result:${resultCodeEnum}");
    }
    (resultCodeEnum.getCaptcha,message)
  }catch {
      case ex:Exception => logger.error("valid message error!",ex);
        null;
  }

  def getThreshold(group:GroupExtEntity, strategy:LimitingStrategyEnum):Int = {
    group.getExtendConfig.getLimitingConfig.getOrDefault(strategy,-1);
  }

  def capture(groupId:Int, message:LightMessage): Unit = {
    val batchTime = DateUtil.batchTime(1, TimeUnit.MINUTES, System.currentTimeMillis)
    val lockTrackKey = RedisConst.LOCK_TRACK_PREFIX + "_" + groupId  + "_" + batchTime
    if(RedisLimitedAspect.getInstance().tryAcquire(lockTrackKey,5,50,TimeUnit.MINUTES.toSeconds(5),1)){
      val trackKey = RedisConst.TRACK_PREFIX + "_" + groupId;
      message.setSystemTime(System.currentTimeMillis());
      RedisHandler.getInstance().limitSet(trackKey,toJSONString(message),StatConst.GROUP_MESSAGE_MAX_CACHE_SIZE,3 * 3600)
    }
  }

  import scala.collection.JavaConverters._

  def validMessage(message:LightMessage, statGroup: GroupExtEntity):MessageCaptchaEnum = {
    val columnList = statGroup.getColumns
    if(!MessageValid.valid(message,columnList)) return MessageCaptchaEnum.PARAM_CHECK_FAILED
    MessageCaptchaEnum.SUCCESS
  }

  import org.apache.spark.sql.{Encoder,Encoders}

  import scala.reflect.ClassTag

  implicit def single[A](implicit c: ClassTag[(Int,LightMessage)]): Encoder[(Int,LightMessage)] = Encoders.kryo[(Int,LightMessage)](c)

  implicit def tuple[A1, A2](implicit e1: Encoder[A1],e2: Encoder[A2]): Encoder[(A1,A2)] = Encoders.tuple[A1,A2](e1, e2)

  override def valid(ds:Dataset[LightMessage]):Dataset[(Int,LightMessage)] = {
    ds.map(x => valid(x)).filter(x => x != null)
  }
}

