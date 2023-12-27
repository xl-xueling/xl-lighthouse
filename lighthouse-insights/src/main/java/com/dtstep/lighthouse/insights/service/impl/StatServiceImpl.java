package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.entity.stat.TemplateEntity;
import com.dtstep.lighthouse.common.enums.stat.StatStateEnum;
import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dao.GroupDao;
import com.dtstep.lighthouse.insights.dao.ProjectDao;
import com.dtstep.lighthouse.insights.dao.StatDao;
import com.dtstep.lighthouse.insights.dto.StatDto;
import com.dtstep.lighthouse.insights.dto.StatQueryParam;
import com.dtstep.lighthouse.insights.modal.Group;
import com.dtstep.lighthouse.insights.modal.Project;
import com.dtstep.lighthouse.insights.modal.Stat;
import com.dtstep.lighthouse.insights.service.StatService;
import com.dtstep.lighthouse.insights.template.TemplateContext;
import com.dtstep.lighthouse.insights.template.TemplateParser;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class StatServiceImpl implements StatService {

    @Autowired
    private StatDao statDao;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private ProjectDao projectDao;

    @Override
    public int create(Stat stat) {
        int groupId = stat.getGroupId();
        Group group = groupDao.queryById(groupId);
        String template = stat.getTemplate();
        String timeParam = stat.getTimeparam();
        try{
            TemplateEntity templateEntity = TemplateParser.parse(new TemplateContext(template,timeParam, group.getColumns()));
            System.out.println("templateEntity:" + templateEntity);
            stat.setTitle(templateEntity.getTitle());
        }catch (Exception ex){
            ex.printStackTrace();
        }
        LocalDateTime localDateTime = LocalDateTime.now();
        stat.setUpdateTime(localDateTime);
        stat.setCreateTime(localDateTime);
        stat.setState(StatStateEnum.FROZEN);
        stat.setRandomId(UUID.randomUUID().toString());
        return statDao.insert(stat);
    }

    @Override
    public ListData<StatDto> queryList(StatQueryParam queryParam, Integer pageNum, Integer pageSize) {
        List<Stat> list = statDao.queryList(queryParam,pageNum,pageSize);
        List<StatDto> dtoList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(list)){
            for(Stat stat : list){
                StatDto statDto = new StatDto(stat);
                Group group = groupDao.queryById(stat.getGroupId());
                Project project = projectDao.queryById(stat.getProjectId());
                statDto.setGroup(group);
                statDto.setProject(project);
                dtoList.add(statDto);
            }
        }
        ListData<StatDto> listData = new ListData<>();
        listData.setList(dtoList);
        return listData;
    }
}
