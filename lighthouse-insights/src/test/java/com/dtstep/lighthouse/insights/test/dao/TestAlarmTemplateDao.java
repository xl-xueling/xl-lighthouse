package com.dtstep.lighthouse.insights.test.dao;

import com.dtstep.lighthouse.common.modal.AlarmTemplate;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dao.AlarmTemplateDao;
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
public class TestAlarmTemplateDao {

    @Autowired
    private AlarmTemplateDao alarmTemplateDao;

    @Test
    public void testInsert() throws Exception {
        AlarmTemplate alarmTemplate = new AlarmTemplate();
        alarmTemplate.setCreateTime(LocalDateTime.now());
        alarmTemplate.setUpdateTime(LocalDateTime.now());
        alarmTemplate.setCreateUser(111);
        alarmTemplate.setDesc("desc....");
        alarmTemplate.setTitle("title.");
        alarmTemplate.setUserIds(List.of(1,2,3));
        alarmTemplate.setDepartmentIds(List.of(4,5,6));
        alarmTemplate.setConfig("SSSSADSASGSG");
        alarmTemplateDao.insert(alarmTemplate);
    }

    @Test
    public void testUpdate() throws Exception {
        AlarmTemplate alarmTemplate = new AlarmTemplate();
        alarmTemplate.setUserIds(List.of());
        alarmTemplate.setId(17);
        alarmTemplateDao.update(alarmTemplate);
    }

    @Test
    public void testQueryByUserId() throws Exception {

    }
}
