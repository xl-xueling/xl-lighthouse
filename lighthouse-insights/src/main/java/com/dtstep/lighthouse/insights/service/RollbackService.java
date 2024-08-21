package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.common.modal.RollbackModal;
import com.dtstep.lighthouse.insights.dto.RollbackQueryParam;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

public interface RollbackService {

    ObjectNode put(RollbackModal rollbackModal) throws Exception;

    RollbackModal queryByVersion(RollbackQueryParam queryParam) throws Exception;

    List<RollbackModal> queryVersionList(RollbackQueryParam queryParam) throws Exception;
}
