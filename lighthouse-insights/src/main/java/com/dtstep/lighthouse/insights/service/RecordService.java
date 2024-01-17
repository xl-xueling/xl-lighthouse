package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dto_bak.RecordQueryParam;
import com.dtstep.lighthouse.insights.modal.Record;

public interface RecordService {

    int create(Record record);

    ListData<Record> queryList(RecordQueryParam queryParam,Integer pageNum,Integer pageSize);
}
