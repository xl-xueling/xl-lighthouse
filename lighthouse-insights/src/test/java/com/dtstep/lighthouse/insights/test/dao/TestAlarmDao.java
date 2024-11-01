package com.dtstep.lighthouse.insights.test.dao;

import com.dtstep.lighthouse.common.modal.Alarm;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dao.AlarmDao;
import com.dtstep.lighthouse.insights.test.listener.SpringTestExecutionListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
@TestExecutionListeners(listeners = SpringTestExecutionListener.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class TestAlarmDao {

    @Autowired
    private AlarmDao alarmDao;

    @Test
    public void testInsert() throws Exception {
        Alarm alarm = new Alarm();
        alarm.setDivide(true);
        alarmDao.insert(alarm);
    }
}
