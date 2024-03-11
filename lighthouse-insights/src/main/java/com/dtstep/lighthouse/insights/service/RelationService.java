package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.common.entity.ListData;
import com.dtstep.lighthouse.common.entity.ResultCode;
import com.dtstep.lighthouse.insights.dto.RelationDeleteParam;
import com.dtstep.lighthouse.insights.dto.RelationQueryParam;
import com.dtstep.lighthouse.insights.vo.RelationVO;
import com.dtstep.lighthouse.common.enums.RelationTypeEnum;
import com.dtstep.lighthouse.common.modal.Relation;

import java.util.List;

public interface RelationService {

    int batchCreate(List<Relation> relationList);

    int delete(RelationDeleteParam deleteParam);

    ResultCode create(Relation relation);

    boolean isExist(String hash);

    List<RelationVO> queryList(Integer relationId, RelationTypeEnum relationTypeEnum) throws Exception;

    ListData<RelationVO> queryList(RelationQueryParam queryParam, Integer pageNum, Integer pageSize) throws Exception;

    int count(RelationQueryParam queryParam);
}
