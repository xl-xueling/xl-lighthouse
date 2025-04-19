package com.dtstep.lighthouse.common.modal;

import java.util.HashMap;
import java.util.Map;

public class AlarmSettings {

    private String activeChannel;

    private Map<String, AlarmChannel> channels = new HashMap<>();

    public String getActiveChannel() {
        return activeChannel;
    }

    public void setActiveChannel(String activeChannel) {
        this.activeChannel = activeChannel;
    }

    public Map<String, AlarmChannel> getChannels() {
        return channels;
    }

    public void setChannels(Map<String, AlarmChannel> channels) {
        this.channels = channels;
    }
}
