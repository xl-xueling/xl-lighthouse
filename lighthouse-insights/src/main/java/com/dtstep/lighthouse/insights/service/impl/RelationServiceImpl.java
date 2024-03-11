package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.util.Md5Util;
import com.dtstep.lighthouse.common.entity.ListData;
import com.dtstep.lighthouse.common.entity.ResultCode;
import com.dtstep.lighthouse.insights.dao.RelationDao;
import com.dtstep.lighthouse.insights.dto.RelationDeleteParam;
import com.dtstep.lighthouse.insights.dto.RelationQueryParam;
import com.dtstep.lighthouse.common.enums.RelationTypeEnum;
import com.dtstep.lighthouse.insights.vo.ProjectVO;
import com.dtstep.lighthouse.insights.vo.RelationVO;
import com.dtstep.lighthouse.insights.vo.StatVO;
import com.dtstep.lighthouse.common.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.common.modal.Relation;
import com.dtstep.lighthouse.insights.service.BaseService;
import com.dtstep.lighthouse.insights.service.ProjectService;
import com.dtstep.lighthouse.insights.service.RelationService;
import com.dtstep.lighthouse.insights.service.StatService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RelationServiceImpl implements RelationService {

    private static final Logger logger = LoggerFactory.getLogger(RelationServiceImpl.class);

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
    public ResultCode create(Relation relation) {
        String message = relation.getSubjectId() + "_" + relation.getRelationType().getRelationType()
                + "_" + relation.getResourceType().getResourceType() + "_" + relation.getResourceId();
        String hash = Md5Util.getMD5(message);
        relation.setHash(hash);
        LocalDateTime localDateTime = LocalDateTime.now();
        int result;
        if(isExist(hash)){
            relation.setUpdateTime(localDateTime);
            result = relationDao.update(relation);
        }else{
            relation.setHash(hash);
            relation.setCreateTime(localDateTime);
            relation.setUpdateTime(localDateTime);
            result = relationDao.insert(relation);
        }
        if(result > 0){
            return ResultCode.success;
        }else{
            return ResultCode.systemError;
        }
    }

    @Override
    public int delete(RelationDeleteParam deleteParam){
        return relationDao.delete(deleteParam);
    }

    @Override
    public boolean isExist(String hash) {
        return relationDao.isExist(hash);
    }

    private RelationVO translate(Relation relation) throws Exception{
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
    public List<RelationVO> queryList(Integer relationId, RelationTypeEnum relationTypeEnum) throws Exception{
        List<Relation> relationList = relationDao.queryList(relationId,relationTypeEnum);
        List<Integer> statIdList = new ArrayList<>();
        List<Integer> projectIdList = new ArrayList<>();
        for(Relation relation : relationList){
            if(relation.getResourceType() == ResourceTypeEnum.Project){
                projectIdList.add(relation.getResourceId());
            }else if(relation.getResourceType() == ResourceTypeEnum.Stat){
                statIdList.add(relation.getResourceId());
            }
        }
        List<StatVO> statList = statService.queryByIds(statIdList);
        List<ProjectVO> projectList = projectService.queryByIds(projectIdList);
        List<RelationVO> voList = new ArrayList<>();
        for(Relation relation : relationList){
            RelationVO relationVO = new RelationVO(relation);
            if(relation.getResourceType() == ResourceTypeEnum.Project){
                ProjectVO project = projectList.stream().filter(x -> x.getId().intValue() == relation.getResourceId().intValue()).findFirst().orElse(null);
                relationVO.setExtend(project);
            }else if(relation.getResourceType() == ResourceTypeEnum.Stat){
                StatVO statVO = statList.stream().filter(x -> x.getId().intValue() == relation.getResourceId().intValue()).findFirst().orElse(null);
                relationVO.setExtend(statVO);
            }
            voList.add(relationVO);
        }
        return voList;
    }

    @Override
    public ListData<RelationVO> queryList(RelationQueryParam queryParam, Integer pageNum, Integer pageSize) throws Exception{
        PageHelper.startPage(pageNum,pageSize);
        PageInfo<Relation> pageInfo = null;
        try{
            List<Relation> relationList = relationDao.queryListByPage(queryParam);
            pageInfo = new PageInfo<>(relationList);
        }finally {
            PageHelper.clearPage();
        }
        List<RelationVO> voList = new ArrayList<>();
        List<Integer> statIdList = new ArrayList<>();
        List<Integer> projectIdList = new ArrayList<>();
        for(Relation relation : pageInfo.getList()){
            if(relation.getResourceType() == ResourceTypeEnum.Project){
                projectIdList.add(relation.getResourceId());
            }else if(relation.getResourceType() == ResourceTypeEnum.Stat){
                statIdList.add(relation.getResourceId());
            }
        }
        List<StatVO> statList = statService.queryByIds(statIdList);
        List<ProjectVO> projectList = projectService.queryByIds(projectIdList);
        for(Relation relation : pageInfo.getList()){
            RelationVO relationVO = new RelationVO(relation);
            if(relation.getResourceType() == ResourceTypeEnum.Project){
                ProjectVO project = projectList.stream().filter(x -> x.getId().intValue() == relation.getResourceId().intValue()).findFirst().orElse(null);
                relationVO.setExtend(project);
            }else if(relation.getResourceType() == ResourceTypeEnum.Stat){
                StatVO statVO = statList.stream().filter(x -> x.getId().intValue() == relation.getResourceId().intValue()).findFirst().orElse(null);
                relationVO.setExtend(statVO);
            }
            voList.add(relationVO);
        }
        return ListData.newInstance(voList,pageInfo.getTotal(),pageNum,pageSize);
    }

    @Override
    public int count(RelationQueryParam queryParam) {
        return relationDao.count(queryParam);
    }
}
