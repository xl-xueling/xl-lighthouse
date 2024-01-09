package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dto.RecordQueryParam;
import com.dtstep.lighthouse.insights.enums.RecordTypeEnum;
import com.dtstep.lighthouse.insights.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.insights.modal.Record;

import java.util.List;

public interface RecordService {

    int create(Record record);

    ListData<Record> queryList(RecordQueryParam queryParam,Integer pageNum,Integer pageSize);
}
