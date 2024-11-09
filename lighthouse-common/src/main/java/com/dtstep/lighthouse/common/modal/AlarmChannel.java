package com.dtstep.lighthouse.common.modal;

import com.dtstep.lighthouse.common.enums.AlarmChannelEnum;

import java.io.Serializable;

public class AlarmChannel implements Serializable {

    private AlarmChannelEnum channel;

    private RemoteServerAlarmChannel remoteServer;

    public AlarmChannelEnum getChannel() {
        return channel;
    }

    public void setChannel(AlarmChannelEnum channel) {
        this.channel = channel;
    }

    public RemoteServerAlarmChannel getRemoteServer() {
        return remoteServer;
    }

    public void setRemoteServer(RemoteServerAlarmChannel remoteServer) {
        this.remoteServer = remoteServer;
    }
}
