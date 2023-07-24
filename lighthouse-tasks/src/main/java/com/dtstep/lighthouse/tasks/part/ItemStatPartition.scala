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

import com.dtstep.lighthouse.common.constant.StatConst
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity
import com.dtstep.lighthouse.common.entity.message.LightMessage
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity
import com.dtstep.lighthouse.common.entity.state.StatState
import com.dtstep.lighthouse.common.enums.limiting.LimitingStrategyEnum
import com.dtstep.lighthouse.common.enums.result.MessageCaptchaEnum
import com.dtstep.lighthouse.common.enums.stat.GroupStateEnum
import com.dtstep.lighthouse.common.exception.LimitingException
import com.dtstep.lighthouse.common.hash.HashUtil
import com.dtstep.lighthouse.common.sbr.StringBuilderHolder
import com.dtstep.lighthouse.common.util.{DateUtil, JsonUtil, StringUtil}
import com.dtstep.lighthouse.core.batch.BatchAdapter
import com.dtstep.lighthouse.core.builtin.BuiltinLoader
import com.dtstep.lighthouse.core.distinct.RedisRoaringFilter
import com.dtstep.lighthouse.core.formula.FormulaCalculate
import com.dtstep.lighthouse.core.limited.LimitedContext
import com.dtstep.lighthouse.core.wrapper.{DimensDBWrapper, GroupDBWrapper}
import com.dtstep.lighthouse.tasks.monitor.BuiltinMonitor
import com.google.common.collect.ImmutableMap

import java.util
import com.google.common.hash_snp.Hashing
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity
import com.dtstep.lighthouse.core.wrapper.GroupDBWrapper
import org.apache.spark.sql.{Dataset, Encoder, Encoders, SparkSession}
import org.slf4j.LoggerFactory
import com.dtstep.lighthouse.common.util.StringUtil
import org.apache.spark.internal.Logging

import java.nio.charset.StandardCharsets
import scala.collection.mutable.ListBuffer
import scala.reflect.ClassTag

private[tasks] class ItemStatPartition(spark:SparkSession) extends Partition[(Int,String, Int)] with Logging {

  private val logger = LoggerFactory.getLogger(classOf[ItemStatPartition]);

  import spark.implicits._;

  import spark.sqlContext.implicits._;

  import scala.collection.JavaConverters._;

  implicit def single[A](implicit c: ClassTag[A]): Encoder[A] = Encoders.kryo[A](c)

  implicit val _tupleEncoder: Encoder[(Int,String, Int)] = org.apache.spark.sql.Encoders.kryo[(Int,String, Int)]

  implicit val _listEncoder: Encoder[List[(Int,String, Int)]] = org.apache.spark.sql.Encoders.kryo[List[(Int,String, Int)]]

  implicit def _tupleEncoder2[A1, A2](implicit e1: Encoder[A1],e2: Encoder[A2]): Encoder[(A1,A2)] = Encoders.tuple[A1,A2](e1, e2)

  import scala.util.control.Breaks._

  override def part(ds: Dataset[(Int, LightMessage)]): Dataset[(Int,String, Int)] = {
    ds.map(x => issueMessage(x._1,x._2)).filter(x => x != null && x.nonEmpty).flatMap(x => x)
  }

  private def issueMessage(captcha: Int,message: LightMessage):List[(Int,String, Int)] = {
    var list:List[(Int,String, Int)] = null;
    try list = expand(captcha,message) catch {
      case ex:Exception => logError("issue message error!",ex);
    }
    list
  }

  private def expand(captcha: Int,message: LightMessage):List[(Int,String, Int)] = {
    val groupEntity = GroupDBWrapper.queryById(message.getGroupId);
    if(groupEntity == null || groupEntity.getState != GroupStateEnum.RUNNING.getState){
      return null;
    }
    if(!groupEntity.isBuiltIn){
      BuiltinMonitor.getInstance().monitor(StatConst.BUILTIN_MSG_STAT,ImmutableMap.of("groupId",groupEntity.getId.toString,"captcha",captcha.toString),message.getRepeat);
    }
    val list = new ListBuffer[(Int,String, Int)]
    if(captcha != MessageCaptchaEnum.SUCCESS.getCaptcha){
      return list.toList
    }
    val statList = getEffectiveStats(groupEntity.getId);
    for(statEntity <- statList) {
      list.++=(append(statEntity, groupEntity, message))
    }
    list.toList
  }

  def append(statEntity: StatExtEntity,groupEntity:GroupExtEntity,message:LightMessage): ListBuffer[(Int,String,Int)] ={
    val list = new ListBuffer[(Int,String, Int)]
    val templateEntity = statEntity.getTemplateEntity
    var dimensValue: String = null
    val batchTime = BatchAdapter.getBatch(statEntity.getTimeParamInterval, statEntity.getTimeUnit, message.getTime)
    val envMap = new util.HashMap[String, AnyRef](message.getParamMap);
    if (!StringUtil.isEmpty(templateEntity.getDimens)) {
      dimensValue = DimensDBWrapper.getDimensValue(envMap, templateEntity.getDimensArr,batchTime);
      val threshold = getThreshold(groupEntity,LimitingStrategyEnum.STAT_RESULT_SIZE_LIMIT);
      if (!LimitedContext.getInstance().tryAcquire(statEntity,threshold,1)) {
        logError(s"limited trigger strategy:STAT_RESULT_SIZE_LIMIT,token:${groupEntity.getToken},stat:${statEntity.getId},threshold:${threshold}")
        return list;
      }
    }
    val statStateList = templateEntity.getStatStateList.asScala;
    for (statState <- statStateList) {
      try {
        val isDistinct = StatState.isBitCountState(statState);
        var distinctValue:String = null
        if(isDistinct){
          distinctValue = String.valueOf(FormulaCalculate.parseVariableEntity(StatState.getFirstUnit(statState), envMap, batchTime));
          logTrace(s"expand message,stat:${statEntity.getId},envMap:${JsonUtil.toJSONString(envMap)}" +
            s",batchTime:${DateUtil.formatTimeStamp(batchTime,"yyyy-MM-dd HH:mm:ss")},distinctValue:${distinctValue}")
          distinctValue = RedisRoaringFilter.getInstance().concatDistinctValue(distinctValue,dimensValue,batchTime)
          val hash = Hashing.murmur3_128.hashBytes(distinctValue.getBytes(StandardCharsets.UTF_8)).asLong
          distinctValue = java.lang.Long.toString(Math.abs(hash), 36);
          envMap.put(StatConst.DISTINCT_COLUMN_NAME,distinctValue);
        }
        val aggregateKey = BatchAdapter.generateBatchKey(statEntity, statState.getFunctionIndex, dimensValue, batchTime)
        if(!groupEntity.isBuiltIn){
          BuiltinMonitor.getInstance().monitor(StatConst.BUILTIN_RESULT_STAT,ImmutableMap.of("resultKey",aggregateKey,"statId",statEntity.getId.toString),message.getRepeat);
        }
        val data = envMap.asScala.filter(x => statState.getRelatedColumnSet.contains(x._1) || x._1.equals(StatConst.DISTINCT_COLUMN_NAME))
          .map(x => {x._1 + StatConst.SEPARATOR_LEVEL_3 + x._2}).mkString(StatConst.SEPARATOR_LEVEL_2);
        val result = StringBuilderHolder.Smaller.getStringBuilder.append(aggregateKey)
          .append(StatConst.SEPARATOR_LEVEL_1).append(statEntity.getId)
          .append(StatConst.SEPARATOR_LEVEL_1).append(data)
          .append(StatConst.SEPARATOR_LEVEL_1).append(dimensValue)
          .append(StatConst.SEPARATOR_LEVEL_1).append(statState.getFunctionIndex)
          .append(StatConst.SEPARATOR_LEVEL_1).append(batchTime)
          .toString
        logTrace(s"deliver stat message,statId:${statEntity.getId},distinct:${distinctValue},evnMap:${JsonUtil.toJSONString(envMap)}" +
          s",dimens:${dimensValue},repeat:${message.getRepeat},result:${result},batch:${DateUtil.formatTimeStamp(batchTime,"yyyy-MM-dd HH:mm:ss")}");
        list.+=((getSlot(aggregateKey),result, message.getRepeat));
      } catch {
        case ex: Exception => logger.error(s"deliver message error,group:${message.getGroupId}", ex);
      }
    }
    list;
  }

  def getSlot(aggregateKey:String):Int = {
    HashUtil.getHashIndex(aggregateKey,StatConst.DEFAULT_POOL_SLOT_SIZE);
  }

}
