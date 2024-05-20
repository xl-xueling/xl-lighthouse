package com.dtstep.lighthouse.core.tasks;

import com.dtstep.lighthouse.common.enums.RunningMode;
import com.dtstep.lighthouse.core.config.LDPConfig;
import com.dtstep.lighthouse.core.tasks.kafka.KafkaEventSender;
import com.dtstep.lighthouse.core.tasks.standalone.StandaloneEventSender;

public class EventSenderFactory {

    private static EventSender eventSender;

    public static EventSender getEventSender(){
        RunningMode runningMode = LDPConfig.getRunningMode();
        if(runningMode == RunningMode.CLUSTER){
            if(eventSender == null){
                eventSender = new KafkaEventSender();
            }

        }else if(runningMode == RunningMode.STANDALONE){
            if(eventSender == null){
                eventSender = new StandaloneEventSender();
            }
        }else{
            throw new RuntimeException("running mode not support!");
        }
        return eventSender;
    }
}
