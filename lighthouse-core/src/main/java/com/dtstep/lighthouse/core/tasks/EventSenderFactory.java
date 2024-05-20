package com.dtstep.lighthouse.core.tasks;

import com.dtstep.lighthouse.common.enums.RunningMode;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.tasks.kafka.KafkaEventSender;
import com.dtstep.lighthouse.core.tasks.standalone.StandaloneEventSender;

public class EventSenderFactory {

    private static EventSender kafkaEventSender;

    private static EventSender standEventSender;

    public static EventSender getEventSender(){
        RunningMode runningMode = LDPConfig.getRunningMode();
        if(runningMode == RunningMode.CLUSTER){
            if(kafkaEventSender == null){
                kafkaEventSender = new KafkaEventSender();
            }
            return kafkaEventSender;
        }else if(runningMode == RunningMode.STANDALONE){
            if(standEventSender == null){
                standEventSender = new StandaloneEventSender();
            }
            return standEventSender;
        }else{
            throw new RuntimeException("running mode not support!");
        }
    }
}
