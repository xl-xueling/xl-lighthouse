package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.enums.RollbackStateEnum;
import com.dtstep.lighthouse.common.enums.RollbackTypeEnum;
import com.dtstep.lighthouse.common.modal.RollbackModal;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.dao.RollbackDao;
import com.dtstep.lighthouse.insights.dto.RollbackQueryParam;
import com.dtstep.lighthouse.insights.service.BaseService;
import com.dtstep.lighthouse.insights.service.RollbackService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RollbackServiceImpl implements RollbackService {

    @Autowired
    private RollbackDao rollbackDao;

    @Autowired
    private BaseService baseService;

    @Transactional
    @Override
    public ObjectNode put(RollbackModal rollbackModal) throws Exception {
        Integer resourceId = rollbackModal.getResourceId();
        RollbackTypeEnum rollbackTypeEnum = rollbackModal.getDataType();
        Integer dbVersion = rollbackDao.getLatestVersion(resourceId,rollbackTypeEnum);
        int version = 1;
        if(dbVersion != null){
            version = dbVersion + 1;
        }
        LocalDateTime localDateTime = LocalDateTime.now();
        int userId = baseService.getCurrentUserId();
        rollbackModal.setVersion(version);
        rollbackModal.setCreateTime(localDateTime);
        rollbackModal.setStateEnum(RollbackStateEnum.UNPUBLISHED);
        rollbackModal.setUserId(userId);
        rollbackDao.insert(rollbackModal);
        ObjectNode objectNode = JsonUtil.createObjectNode();
        objectNode.put("version",version);
        objectNode.put("state",RollbackStateEnum.UNPUBLISHED.getState());
        objectNode.put("dataType",rollbackTypeEnum.getType());
        objectNode.put("createTime", DateUtil.translateToTimeStamp(localDateTime));
        return objectNode;
    }

    @Override
    public RollbackModal queryByVersion(RollbackQueryParam queryParam) throws Exception {
        return rollbackDao.queryByVersion(queryParam);
    }

    @Override
    public List<RollbackModal> queryVersionList(RollbackQueryParam queryParam) throws Exception {
        return rollbackDao.queryVersionList(queryParam);
    }
}
