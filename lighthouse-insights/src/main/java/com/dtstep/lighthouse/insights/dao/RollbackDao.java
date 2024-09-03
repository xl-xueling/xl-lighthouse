package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.common.enums.RollbackTypeEnum;
import com.dtstep.lighthouse.common.modal.RollbackModal;
import com.dtstep.lighthouse.insights.dto.RollbackQueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RollbackDao {

    int insert(RollbackModal rollbackModal);

    Integer getLatestVersion(Integer resourceId, RollbackTypeEnum rollbackTypeEnum) throws Exception;

    RollbackModal queryByVersion(@Param("queryParam")RollbackQueryParam queryParam) throws Exception;

    List<RollbackModal> queryVersionList(@Param("queryParam")RollbackQueryParam queryParam) throws Exception;

    Integer update(RollbackModal rollbackModal);
}
