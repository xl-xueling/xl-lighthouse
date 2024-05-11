package com.dtstep.lighthouse.core.tasks;

public interface EventSender {

    void send(String event) throws Exception;

    void syncSend(String text) throws Exception;
}
