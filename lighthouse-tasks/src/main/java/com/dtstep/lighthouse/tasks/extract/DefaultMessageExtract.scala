package com.dtstep.lighthouse.tasks.extract

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
import com.dtstep.lighthouse.common.entity.message.LightMessage
import com.dtstep.lighthouse.common.util.{MessageHelper, StringUtil}
import com.dtstep.lighthouse.tasks.init.InitialServant

import java.util.StringTokenizer
import org.apache.spark.sql.{Dataset, SparkSession}
import org.slf4j.LoggerFactory
import com.dtstep.lighthouse.common.util.StringUtil

import scala.collection.mutable.ListBuffer;

private[tasks] class DefaultMessageExtract(spark:SparkSession) extends MessageExtract{

  private val logger = LoggerFactory.getLogger(classOf[DefaultMessageExtract]);

  import spark.implicits._;

  import spark.sqlContext.implicits._;

  override def extract(message:String):List[LightMessage] = {
    if(!InitialServant.isInit){
      InitialServant.init;
    }
    val list = new ListBuffer[LightMessage]();
    try{
      if(logger.isTraceEnabled()){
        logger.trace(s"receive message:${message}");
      }
      val messageArray = new StringTokenizer(message, StatConst.SEPARATOR_LEVEL_0);
      while(messageArray.hasMoreTokens){
        val messageStr = messageArray.nextToken();
        try{
          val entity = MessageHelper.parseText(messageStr);
          if(entity != null){
            list += entity
          }
        }catch {
          case ex:Exception => logger.error(s"process message error,message:${messageStr}",ex)
        }
      }
    }catch {
      case ex:Exception => logger.error(s"extract message error!",ex)
    }
    list.toList
  }


  import org.apache.spark.sql.{Encoder,Encoders}

  import scala.reflect.ClassTag

  implicit def single[A](implicit c: ClassTag[A]): Encoder[A] = Encoders.kryo[A](c)

  override def extract(df:Dataset[String]) : Dataset[LightMessage] =
    df.filter(x => !StringUtil.isEmpty(x)).map(x => extract(x)).filter(x => x != null).flatMap(x => x)

}
