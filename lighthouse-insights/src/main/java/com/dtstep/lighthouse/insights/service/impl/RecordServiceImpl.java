package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.entity.ListData;
import com.dtstep.lighthouse.common.modal.Stat;
import com.dtstep.lighthouse.insights.dao.RecordDao;
import com.dtstep.lighthouse.insights.dao.StatDao;
import com.dtstep.lighthouse.insights.dto.RecordQueryParam;
import com.dtstep.lighthouse.common.modal.Record;
import com.dtstep.lighthouse.insights.service.BaseService;
import com.dtstep.lighthouse.insights.service.RecordService;
import com.dtstep.lighthouse.insights.service.StatService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordDao recordDao;

    @Autowired
    private StatDao statDao;

    @Override
    public int create(Record record) {
        return recordDao.insert(record);
    }

    @Override
    public ListData<Record> queryList(RecordQueryParam queryParam,Integer pageNum,Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        PageInfo<Record> pageInfo;
        try{
            List<Record> recordList = recordDao.queryList(queryParam);
            pageInfo = new PageInfo<>(recordList);
        }finally {
            PageHelper.clearPage();
        }
        return ListData.newInstance(pageInfo.getList(),pageInfo.getTotal(),pageNum,pageSize);
    }

    @Override
    public ListData<Record> queryStatLimitList(Integer statId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        Stat stat = statDao.queryById(statId);
        Validate.notNull(stat);
        PageInfo<Record> pageInfo;
        try{
            List<Record> recordList = recordDao.queryStatLimit(stat.getId(),stat.getGroupId());
            pageInfo = new PageInfo<>(recordList);
        }finally {
            PageHelper.clearPage();
        }
        return ListData.newInstance(pageInfo.getList(),pageInfo.getTotal(),pageNum,pageSize);
    }
}
