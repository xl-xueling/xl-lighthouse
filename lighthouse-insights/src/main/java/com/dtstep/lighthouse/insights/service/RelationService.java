package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.dto.RelationQueryParam;
import com.dtstep.lighthouse.insights.vo.RelationVO;
import com.dtstep.lighthouse.insights.enums.RelationTypeEnum;
import com.dtstep.lighthouse.insights.modal.Relation;

import java.util.List;

public interface RelationService {

    int batchCreate(List<Relation> relationList);

    int delete(Relation relation);

    ResultCode create(Relation relation);

    boolean isExist(String hash);

    List<RelationVO> queryList(Integer relationId, RelationTypeEnum relationTypeEnum);

    ListData<RelationVO> queryList(RelationQueryParam queryParam, Integer pageNum, Integer pageSize);
}
