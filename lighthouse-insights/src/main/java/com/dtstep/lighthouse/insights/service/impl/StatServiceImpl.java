package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.entity.stat.TemplateEntity;
import com.dtstep.lighthouse.insights.dao.GroupDao;
import com.dtstep.lighthouse.insights.dao.StatDao;
import com.dtstep.lighthouse.insights.modal.Group;
import com.dtstep.lighthouse.insights.modal.Stat;
import com.dtstep.lighthouse.insights.service.StatService;
import com.dtstep.lighthouse.insights.template.TemplateContext;
import com.dtstep.lighthouse.insights.template.TemplateParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StatServiceImpl implements StatService {

    @Autowired
    private StatDao statDao;

    @Autowired
    private GroupDao groupDao;

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
        stat.setRandomId(UUID.randomUUID().toString());
        return statDao.insert(stat);
    }
}
