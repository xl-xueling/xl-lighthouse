package com.dtstep.lighthouse.tasks.executive

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
import com.dtstep.lighthouse.common.entity.calculate.MicroBucket
import com.dtstep.lighthouse.common.entity.message.LightMessage
import com.dtstep.lighthouse.common.util.FileUtil
import com.dtstep.lighthouse.core.config.LDPConfig
import com.dtstep.lighthouse.core.consumer.RealTimeProcessor
import com.dtstep.lighthouse.core.message.RetrenchMessage
import com.dtstep.lighthouse.tasks.listener.ListenerTrigger
import com.dtstep.lighthouse.tasks.transform.PreTransform

import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit
import com.dtstep.lighthouse.common.util.SnappyUtil
import com.dtstep.lighthouse.core.consumer.AggregateEvent
import com.dtstep.lighthouse.tasks.stream.NormalStream
import org.apache.commons.lang3.tuple.{Pair, Triple}
import org.apache.spark.SparkConf
import org.apache.spark.internal.Logging
import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.streaming.{OutputMode, Trigger}



object LightHouseEntrance extends Logging{

  def main(args: Array[String]){
    if(args.isEmpty){
      logError("Please enter the configuration file(ldp-site.xml) path,system exit!")
      System.exit(1);
    }
    val Array(sitePath) = args;
    if(!FileUtil.isFileExist(sitePath)){
      logError("The configuration file(ldp-site.xml) does not exist,system exit!")
      System.exit(1);
    }
    try LDPConfig.init(sitePath) catch {
      case ex:Exception =>
        logError("Failed to load configuration file(ldp-site.xml),system exit!",ex)
        System.exit(1);
    }
    val kafkaServers = LDPConfig.getVal(LDPConfig.KEY_KAFKA_BOOTSTRAP_SERVERS);
    val topic = LDPConfig.getVal(LDPConfig.KEY_KAFKA_TOPIC_NAME);
    val Array(bootstrapServers, topics, _*) = Array(kafkaServers,topic)
    lazy val sparkConf = new SparkConf().setAppName("LightHouseEntrance")
      .set("spark.kryoserializer.buffer.max","128m")
      .set("spark.serializer","org.apache.spark.serializer.KryoSerializer")
      .set("spark.shuffle.consolidateFiles", "true")
      .set("spark.network.timeout","600000")
      .set("spark.ui.killEnabled","false")
      .set("spark.default.parallelism","500")
      .set("spark.sql.shuffle.partitions","500")
      .set("spark.scheduler.listenerbus.eventqueue.capacity","20000")
      .set("spark.kryo.registrationRequired","false")
      .set("spark.shuffle.file.buffer","32k")
      .set("spark.kafka.consumer.cache.capacity","256")
      .set("spark.kafka.consumer.cache.timeout","1m")
      .set("spark.reducer.maxSizeInFlight","96m")
      .set("spark.io.compression.lz4.blockSize","16k")
      .registerKryoClasses(Array(
        classOf[LightMessage]
        ,classOf[RetrenchMessage]
        ,classOf[AggregateEvent]
        ,classOf[Pair[String, Integer]]
        ,classOf[Triple[Integer, String, Long]]
        ,classOf[MicroBucket]))
    val spark = SparkSession.builder.config(sparkConf).getOrCreate()
    spark.sparkContext.addSparkListener(new ListenerTrigger);
    import spark.implicits._
    spark.udf.register("deserialize", (bytes: Array[Byte]) =>
      if (SnappyUtil.isCompress(bytes)) SnappyUtil.uncompressByte(bytes) else new String(bytes, StandardCharsets.UTF_8))
    spark.sparkContext.addFile(sitePath)
    val kafkaDataSets = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", bootstrapServers)
      .option("enable.auto.commit","false")
      .option("subscribe", topics)
      .option("kafka.session.timeout.ms", "120000")
      .option("kafka.request.timeout.ms","90000")
      .option("kafka.allow.auto.create.topics","false")
      .option("kafka.max.poll.interval.ms","1500000")
      .option("kafka.fetch.max.wait.ms","1000")
      .option("kafka.fetch.max.bytes","2097152")
      .option("kafka.connections.max.idle.ms","1080000")
      .option("kafka.max.partition.fetch.bytes","2097152")
      .option("kafka.max.poll.records","300")
      .option("kafkaConsumer.pollTimeoutMs", "180000")
      .option("kafka.partition.assignment.strategy", "org.apache.kafka.clients.consumer.StickyAssignor")
      .option("startingOffsets", "latest")
      .option("failOnDataLoss","false")
      .load().selectExpr(s"""deserialize(value) AS message""").as[String]
    val transform = new PreTransform(spark)
    val flowData = transform.process(kafkaDataSets)
    processNormalRequest(flowData)
    spark.streams.awaitAnyTermination()
    spark.stop()
  }

  private def processNormalRequest(flowData:Dataset[(Int,LightMessage)]): Unit ={
    val spark = flowData.sparkSession;
    val normalStream = new NormalStream(spark);
    val normalDataSet = normalStream.part(flowData);
    normalDataSet.writeStream.queryName("NormalStream")
      .option("checkpointLocation","/lighthouse/checkpoint/normal")
      .foreach(new ItemForeachWriter())
      .outputMode(OutputMode.Append()).trigger(Trigger.ProcessingTime(TimeUnit.SECONDS.toMillis(StatConst.NORMAL_BATCH_INTERVAL))).start()
  }

  private class ItemForeachWriter() extends ForeachWriter[(Int,String,Int)] with Serializable {
    override def open(partitionId: Long, version: Long): Boolean = {true};

    @Override
    override def process(value: (Int,String,Int)): Unit = {
      RealTimeProcessor.onEvent(value._1,value._2,value._3);
    }

    override def close(errorOrNull: Throwable): Unit = {}
  }

}
