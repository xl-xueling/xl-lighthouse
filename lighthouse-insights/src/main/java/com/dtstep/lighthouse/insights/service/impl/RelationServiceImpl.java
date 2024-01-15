package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dao.RelationDao;
import com.dtstep.lighthouse.insights.dto.ProjectDto;
import com.dtstep.lighthouse.insights.dto.RelationDto;
import com.dtstep.lighthouse.insights.dto.StatDto;
import com.dtstep.lighthouse.insights.enums.RelationTypeEnum;
import com.dtstep.lighthouse.insights.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.insights.modal.Relation;
import com.dtstep.lighthouse.insights.service.BaseService;
import com.dtstep.lighthouse.insights.service.ProjectService;
import com.dtstep.lighthouse.insights.service.RelationService;
import com.dtstep.lighthouse.insights.service.StatService;
import com.github.pagehelper.PageHelper;
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

    private RelationDto translate(Relation relation){
        RelationDto relationDto = new RelationDto(relation);
        if(relation.getResourceType() == ResourceTypeEnum.Project){
            ProjectDto projectDto = projectService.queryById(relation.getResourceId());
            relationDto.setExtend(projectDto);
        }else if(relation.getResourceType() == ResourceTypeEnum.Stat){
            StatDto statDto = statService.queryById(relation.getResourceId());
            relationDto.setExtend(statDto);
        }
        return relationDto;
    }

    @Override
    public List<RelationDto> queryList(Integer relationId, RelationTypeEnum relationTypeEnum) {
        List<RelationDto> dtoList = new ArrayList<>();
        List<Relation> relationList = relationDao.queryList(relationId,relationTypeEnum);
        for(Relation relation : relationList){
            RelationDto dto = translate(relation);
            dtoList.add(dto);
        }
        return dtoList;
    }
}
