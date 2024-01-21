package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.insights.dao.RelationDao;
import com.dtstep.lighthouse.insights.vo.ProjectVO;
import com.dtstep.lighthouse.insights.vo.RelationVO;
import com.dtstep.lighthouse.insights.vo.StatVO;
import com.dtstep.lighthouse.insights.enums.RelationTypeEnum;
import com.dtstep.lighthouse.insights.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.insights.modal.Relation;
import com.dtstep.lighthouse.insights.service.BaseService;
import com.dtstep.lighthouse.insights.service.ProjectService;
import com.dtstep.lighthouse.insights.service.RelationService;
import com.dtstep.lighthouse.insights.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RelationServiceImpl implements RelationService {

    @Autowired
    private RelationDao relationDao;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private StatService statService;

    @Autowired
    private BaseService baseService;

    @Override
    public int batchCreate(List<Relation> relationList) {
        return relationDao.batchInsert(relationList);
    }

    @Override
    public boolean isExist(String hash) {
        return relationDao.isExist(hash);
    }

    private RelationVO translate(Relation relation){
        RelationVO relationVO = new RelationVO(relation);
        if(relation.getResourceType() == ResourceTypeEnum.Project){
            ProjectVO projectVO = projectService.queryById(relation.getResourceId());
            relationVO.setExtend(projectVO);
        }else if(relation.getResourceType() == ResourceTypeEnum.Stat){
            StatVO statVO = statService.queryById(relation.getResourceId());
            relationVO.setExtend(statVO);
        }
        return relationVO;
    }

    @Override
    public List<RelationVO> queryList(Integer relationId, RelationTypeEnum relationTypeEnum) {
        List<RelationVO> dtoList = new ArrayList<>();
        List<Relation> relationList = relationDao.queryList(relationId,relationTypeEnum);
        for(Relation relation : relationList){
            RelationVO dto = translate(relation);
            dtoList.add(dto);
        }
        return dtoList;
    }
}
