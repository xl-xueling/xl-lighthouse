package com.dtstep.lighthouse.insights.test.dao;

import com.dtstep.lighthouse.common.enums.NotificationStateEnum;
import com.dtstep.lighthouse.common.enums.NotificationTypeEnum;
import com.dtstep.lighthouse.common.modal.Notification;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dao.NotificationDao;
import com.dtstep.lighthouse.insights.dto.NotificationQueryParam;
import com.dtstep.lighthouse.insights.test.listener.SpringTestExecutionListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
@TestExecutionListeners(listeners = SpringTestExecutionListener.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class TestNotificationDao {

    @Autowired
    private NotificationDao notificationDao;

    @Test
    public void testInsert() throws Exception {
        Notification notification = new Notification();
        notification.setNotificationType(NotificationTypeEnum.StatAlarm);
        notification.setContent("test");
        LocalDateTime localDateTime = LocalDateTime.now();
        notification.setUpdateTime(localDateTime);
        notification.setCreateTime(localDateTime);
        notification.setP1("test1");
        notification.setP2("test2");
        notification.setP3("test3");
        notification.setState(NotificationStateEnum.Pend);
        notification.setResourceId(22);
        notification.setUserIds(List.of(1,2));
        notification.setDepartmentIds(List.of(2,10252));
        notificationDao.insert(notification);
    }

    @Test
    public void testQueryList() throws Exception {
        NotificationQueryParam queryParam = new NotificationQueryParam();
        queryParam.setUserId(110240);
        List<Notification> list = notificationDao.queryList(queryParam);
        for(Notification notification : list){
            System.out.println("notification:" + JsonUtil.toJSONString(notification));
        }
    }
}

