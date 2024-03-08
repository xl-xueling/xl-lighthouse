package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.enums.MetaTableTypeEnum;
import com.dtstep.lighthouse.common.modal.MetaTable;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.core.storage.engine.StorageEngineProxy;
import com.dtstep.lighthouse.core.wrapper.MetaTableWrapper;
import com.dtstep.lighthouse.insights.dao.MetaTableDao;
import com.dtstep.lighthouse.insights.dto.MetaTableQueryParam;
import com.dtstep.lighthouse.insights.service.MetaTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class MetaTableServiceImpl implements MetaTableService {

    @Autowired
    private MetaTableDao metaTableDao;

    @Transactional
    @Override
    public int getCurrentStatResultTable() throws Exception {
        MetaTableQueryParam metaTableQueryParam = new MetaTableQueryParam();
        long maxRecordSize = StorageEngineProxy.getInstance().getMaxRecordSize();
        long maxContentSize = StorageEngineProxy.getInstance().getMaxContentSize();
        long maxTimeInterval = StorageEngineProxy.getInstance().getMaxTimeInterval();
        metaTableQueryParam.setMaxRecordSize(maxRecordSize);
        metaTableQueryParam.setMaxContentSize(maxContentSize);
        long timestamp = DateUtil.getSecondBefore(System.currentTimeMillis(),maxTimeInterval);
        LocalDateTime startDate = DateUtil.timestampToLocalDateTime(timestamp);
        metaTableQueryParam.setStartDate(startDate);
        metaTableQueryParam.setMetaTableTypeEnum(MetaTableTypeEnum.STAT_RESULT_TABLE);
        MetaTable metaTable = metaTableDao.getCurrentStorageTable(metaTableQueryParam);
        int metaId;
        if(metaTable == null){
            metaId = MetaTableWrapper.createStatResultMetaTable();
        }else{
            metaId = metaTable.getId();
        }
        return metaId;
    }


    @Transactional
    @Override
    public int getCurrentSeqResultTable() throws Exception {
        MetaTableQueryParam metaTableQueryParam = new MetaTableQueryParam();
        long maxRecordSize = StorageEngineProxy.getInstance().getMaxRecordSize();
        long maxContentSize = StorageEngineProxy.getInstance().getMaxContentSize();
        long maxTimeInterval = StorageEngineProxy.getInstance().getMaxTimeInterval();
        metaTableQueryParam.setMaxRecordSize(maxRecordSize);
        metaTableQueryParam.setMaxContentSize(maxContentSize);
        long timestamp = DateUtil.getSecondBefore(System.currentTimeMillis(),maxTimeInterval);
        LocalDateTime startDate = DateUtil.timestampToLocalDateTime(timestamp);
        metaTableQueryParam.setStartDate(startDate);
        metaTableQueryParam.setMetaTableTypeEnum(MetaTableTypeEnum.SEQ_RESULT_TABLE);
        MetaTable metaTable = metaTableDao.getCurrentStorageTable(metaTableQueryParam);
        int metaId;
        if(metaTable == null){
            metaId = MetaTableWrapper.createSeqResultMetaTable();
        }else{
            metaId = metaTable.getId();
        }
        return metaId;
    }
}
