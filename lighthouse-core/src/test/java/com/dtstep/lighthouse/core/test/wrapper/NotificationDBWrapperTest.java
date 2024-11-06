package com.dtstep.lighthouse.core.test.wrapper;

import com.dtstep.lighthouse.common.enums.NotificationStateEnum;
import com.dtstep.lighthouse.common.enums.NotificationTypeEnum;
import com.dtstep.lighthouse.common.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.common.modal.Notification;
import com.dtstep.lighthouse.core.test.CoreBaseTest;
import com.dtstep.lighthouse.core.wrapper.NotificationDBWrapper;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

public class NotificationDBWrapperTest extends CoreBaseTest {

    @Test
    public void testInsert() throws Exception {
        Notification notification = new Notification();
        notification.setContent("test");
        notification.setNotificationType(NotificationTypeEnum.StatAlarm);
        notification.setResourceType(ResourceTypeEnum.Stat);
        notification.setResourceId(1);
        notification.setCreateTime(LocalDateTime.now());
        notification.setUpdateTime(LocalDateTime.now());
        notification.setUserIds(List.of(1,2,3));
        notification.setState(NotificationStateEnum.Pend);
        NotificationDBWrapper.insert(notification);
    }
}
