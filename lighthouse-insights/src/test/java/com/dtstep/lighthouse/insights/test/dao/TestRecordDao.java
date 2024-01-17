package com.dtstep.lighthouse.insights.test.dao;

import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.LightHouseInsightsApplication;
import com.dtstep.lighthouse.insights.dao.RecordDao;
import com.dtstep.lighthouse.insights.dto_bak.LimitedRecord;
import com.dtstep.lighthouse.insights.dto_bak.RecordQueryParam;
import com.dtstep.lighthouse.insights.enums.RecordTypeEnum;
import com.dtstep.lighthouse.insights.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.insights.modal.Record;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LightHouseInsightsApplication.class,properties = {"spring.config.location=classpath:lighthouse-insights.yml"})
public class TestRecordDao {

    @Autowired
    private RecordDao recordDao;

    @Test
    public void testInsert() throws Exception {
        for(int i=0;i<300;i++){
            LimitedRecord limitedRecord = new LimitedRecord();
            limitedRecord.setStartTime(LocalDateTime.now());
            long t = DateUtil.getMinuteAfter(System.currentTimeMillis(),15);
            Instant instant = Instant.ofEpochMilli(t);
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            limitedRecord.setStartTime(LocalDateTime.now());
            limitedRecord.setEndTime(localDateTime);
            Record record = new Record();
            record.setRecordTime(LocalDateTime.now());
            record.setRecordType(RecordTypeEnum.GROUP_LIMITED);
            record.setResourceId(100215);
            record.setResourceType(ResourceTypeEnum.Group);
            record.setDesc(JsonUtil.toJSONString(record));
            recordDao.insert(record);
        }
    }

    @Test
    public void testQueryList() throws Exception {
        RecordQueryParam queryParam = new RecordQueryParam();
        queryParam.setRecordTypes(List.of(RecordTypeEnum.GROUP_LIMITED));
        List<Record> recordList = recordDao.queryList(queryParam);
        System.out.println("recordList:" + JsonUtil.toJSONString(recordList));
        System.out.println("size:" + recordList.size());
    }
}
