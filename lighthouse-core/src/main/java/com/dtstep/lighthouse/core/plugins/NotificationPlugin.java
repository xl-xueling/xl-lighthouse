package com.dtstep.lighthouse.core.plugins;


import com.dtstep.lighthouse.common.entity.LdpNotification;

import java.util.List;

public interface NotificationPlugin extends Plugin {

    void send(List<LdpNotification> notifications);
}
