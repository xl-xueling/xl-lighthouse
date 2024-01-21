package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.insights.vo.RelationVO;
import com.dtstep.lighthouse.insights.enums.RelationTypeEnum;
import com.dtstep.lighthouse.insights.modal.Relation;

import java.util.List;

public interface RelationService {

    int batchCreate(List<Relation> relationList);

    boolean isExist(String hash);

    List<RelationVO> queryList(Integer relationId, RelationTypeEnum relationTypeEnum);
}
