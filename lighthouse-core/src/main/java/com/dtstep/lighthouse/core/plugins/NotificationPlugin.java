package com.dtstep.lighthouse.core.plugins;


import com.dtstep.lighthouse.common.entity.AlarmNotification;

import java.util.List;

public interface NotificationPlugin extends Plugin {

    void send(List<AlarmNotification> notifications);
}
