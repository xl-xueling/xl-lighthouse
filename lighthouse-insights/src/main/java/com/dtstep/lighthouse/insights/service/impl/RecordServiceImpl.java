package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.insights.dao.RecordDao;
import com.dtstep.lighthouse.insights.enums.RecordTypeEnum;
import com.dtstep.lighthouse.insights.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.insights.modal.Record;
import com.dtstep.lighthouse.insights.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordDao recordDao;

    @Override
    public int create(Record record) {
        return recordDao.insert(record);
    }

    @Override
    public List<Record> queryList(ResourceTypeEnum resourceType, Integer resourceId, RecordTypeEnum recordTypeEnum) {

        return null;
    }
}
