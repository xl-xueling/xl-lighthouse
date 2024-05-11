package com.dtstep.lighthouse.core.tasks;

import com.dtstep.lighthouse.common.enums.RunningMode;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.tasks.kafka.KafkaEventSender;
import com.dtstep.lighthouse.core.tasks.standalone.StandaloneEventSender;

public class EventSenderFactory {

    private static final EventSender kafkaEventSender = new KafkaEventSender();

    private static final EventSender standEventSender = new StandaloneEventSender();

    public static EventSender getEventSender(){
        RunningMode runningMode = LDPConfig.getRunningMode();
        if(runningMode == RunningMode.CLUSTER){
            return kafkaEventSender;
        }else if(runningMode == RunningMode.STANDALONE){
            return standEventSender;
        }else{
            throw new RuntimeException("running mode not support!");
        }
    }
}
