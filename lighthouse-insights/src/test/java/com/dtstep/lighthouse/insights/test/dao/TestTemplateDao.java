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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
@TestExecutionListeners(listeners = SpringTestExecutionListener.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class TestTemplateDao {

    @Autowired
    private AlarmTemplateDao alarmTemplateDao;

    @Test
    public void testInsert() throws Exception {
        AlarmTemplate alarmTemplate = new AlarmTemplate();
        alarmTemplate.setCreateTime(LocalDateTime.now());
        alarmTemplate.setUpdateTime(LocalDateTime.now());
        alarmTemplate.setUserId(111);
        alarmTemplate.setConfig("SSSSADSASGSG");
        alarmTemplateDao.insert(alarmTemplate);
    }
}
