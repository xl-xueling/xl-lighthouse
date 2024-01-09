package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.insights.enums.RecordTypeEnum;
import com.dtstep.lighthouse.insights.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.insights.modal.Record;

import java.util.List;

public interface RecordService {

    List<Record> queryList(ResourceTypeEnum resourceType, Integer resourceId, RecordTypeEnum recordTypeEnum);
}
