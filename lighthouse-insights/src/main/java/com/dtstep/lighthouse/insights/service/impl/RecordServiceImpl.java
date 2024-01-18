package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dao.RecordDao;
import com.dtstep.lighthouse.insights.dto.RecordQueryParam;
import com.dtstep.lighthouse.insights.modal.Record;
import com.dtstep.lighthouse.insights.service.BaseService;
import com.dtstep.lighthouse.insights.service.RecordService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordDao recordDao;

    @Autowired
    private BaseService baseService;

    @Override
    public int create(Record record) {
        return recordDao.insert(record);
    }

    @Override
    public ListData<Record> queryList(RecordQueryParam queryParam,Integer pageNum,Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        ListData<Record> result;
        try{
            List<Record> recordList = recordDao.queryList(queryParam);
            result = baseService.translateToListData(recordList);
        }finally {
            PageHelper.clearPage();
        }
        return result;
    }
}
