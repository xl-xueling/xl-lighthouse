package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.insights.enums.RecordTypeEnum;
import com.dtstep.lighthouse.insights.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.insights.modal.Record;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordDao {

    int insert(Record record);

    List<Record> queryList(ResourceTypeEnum resourceType, Integer resourceId, RecordTypeEnum recordTypeEnum);
}
