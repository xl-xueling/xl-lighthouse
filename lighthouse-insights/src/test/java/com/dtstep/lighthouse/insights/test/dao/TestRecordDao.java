package com.dtstep.lighthouse.insights.test.dao;

import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dao.RecordDao;
import com.dtstep.lighthouse.insights.enums.RecordTypeEnum;
import com.dtstep.lighthouse.insights.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.insights.modal.Record;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
public class TestRecordDao {

    @Autowired
    private RecordDao recordDao;

    @Test
    public void testInsert() throws Exception {
        Record record = new Record();
        record.setRecordType(RecordTypeEnum.CHANGE_STAT_STATE);
        record.setResourceType(ResourceTypeEnum.Stat);
        record.setResourceId(101);
        record.setRecordTime(LocalDateTime.now());
        int result = recordDao.insert(record);
        System.out.println("result:" + result);
    }
}
