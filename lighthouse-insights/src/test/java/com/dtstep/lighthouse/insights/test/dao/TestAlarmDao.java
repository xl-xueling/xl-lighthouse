package com.dtstep.lighthouse.insights.test.dao;

import com.dtstep.lighthouse.common.enums.AlarmStateEnum;
import com.dtstep.lighthouse.common.enums.NumberCompareType;
import com.dtstep.lighthouse.common.modal.Alarm;
import com.dtstep.lighthouse.common.modal.AlarmCondition;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dao.AlarmDao;
import com.dtstep.lighthouse.insights.test.listener.SpringTestExecutionListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
@TestExecutionListeners(listeners = SpringTestExecutionListener.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class TestAlarmDao {

    @Autowired
    private AlarmDao alarmDao;

    @Test
    public void testInsert() throws Exception {
        Alarm alarm = new Alarm();
        alarm.setDivide(false);
        alarm.setDelay(23);
        AlarmCondition alarmCondition = new AlarmCondition();
        alarmCondition.setCompare(NumberCompareType.EQ);
        alarmCondition.setIndicator(1);
        AlarmCondition.ThresholdConfig thresholdConfig = new AlarmCondition.ThresholdConfig();
        thresholdConfig.setThreshold(1.0d);
        thresholdConfig.setSilent(23);
        thresholdConfig.setState(true);
        alarmCondition.setOverall(thresholdConfig);
        List<AlarmCondition> conditionList = new ArrayList<>();
        conditionList.add(alarmCondition);
        alarm.setConditions(conditionList);
        alarm.setCreateTime(LocalDateTime.now());
        alarm.setTitle("tttssss");
        alarm.setDesc("ssswewe");
        alarm.setTemplateId(111);
        alarm.setState(AlarmStateEnum.DISABLE);
        alarmDao.insert(alarm);
    }

    @Test
    public void queryById() throws Exception {
        int id = 3;
        Alarm alarm = alarmDao.queryById(id);
        System.out.println("alarm is:" + JsonUtil.toJSONString(alarm));
    }
}
