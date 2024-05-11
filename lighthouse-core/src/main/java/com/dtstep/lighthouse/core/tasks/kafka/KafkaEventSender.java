package com.dtstep.lighthouse.core.tasks.kafka;

import com.dtstep.lighthouse.common.random.RandomID;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.kafka.KafkaProducerPool;
import com.dtstep.lighthouse.core.tasks.EventSender;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class KafkaEventSender implements EventSender {

    private static final Logger logger = LoggerFactory.getLogger(KafkaEventSender.class);

    private static final KafkaProducerPool producerPool;

    private static final String KAFKA_TOPIC_NAME;

    static {
        producerPool = new KafkaProducerPool();
        KAFKA_TOPIC_NAME = LDPConfig.getVal(LDPConfig.KEY_KAFKA_TOPIC_NAME);
    }

    @Override
    public void send(String text) throws Exception {
        if(StringUtil.isEmpty(text)){
            return;
        }
        KafkaProducer<byte[],byte[]> producer = producerPool.getInstance();
        try{
            producer.send(new ProducerRecord<>(KAFKA_TOPIC_NAME, RandomID.id(15).getBytes(StandardCharsets.UTF_8), text.getBytes(StandardCharsets.UTF_8)));
        }catch (Exception ex){
            logger.error("send message failed!",ex);
        }finally {
            producerPool.release(producer);
        }
    }

    @Deprecated
    @Override
    public void syncSend(String text) throws Exception{
        KafkaProducer<byte[],byte[]> producer = producerPool.getInstance();
        producer.send(new ProducerRecord<>(KAFKA_TOPIC_NAME, RandomID.id(15).getBytes(StandardCharsets.UTF_8),text.getBytes(StandardCharsets.UTF_8)),(recordMetadata,ex) ->  {
            if (ex != null) {
                logger.error("send message failed!",ex);
            }
        }).get();
        producerPool.release(producer);
    }
}
