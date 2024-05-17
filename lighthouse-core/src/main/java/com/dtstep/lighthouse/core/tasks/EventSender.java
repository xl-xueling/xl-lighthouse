package com.dtstep.lighthouse.core.tasks;

public interface EventSender {

    void send(String text) throws Exception;

    void syncSend(String text) throws Exception;
}
