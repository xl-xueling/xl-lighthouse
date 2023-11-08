package com.dtstep.lighthouse.insights.dao.project;

import com.dtstep.lighthouse.common.entity.project.ProjectEntity;
import com.dtstep.lighthouse.core.builtin.BuiltinLoader;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectDao {

    private static final Logger logger = LoggerFactory.getLogger(ProjectDao.class);

    @Cacheable(value = "PROJECT",key = "'queryById' + '_' + #projectId",cacheManager = "redisCacheManager",unless = "#result == null")
    public ProjectEntity queryById(int projectId) {
        if(BuiltinLoader.isBuiltinProject(projectId)){
            return BuiltinLoader.getBuiltinProject();
        }
        ProjectEntity projectEntity = null;
        try{
            projectEntity = DaoHelper.sql.getItem(ProjectEntity.class, "select * from ldp_stat_project where id = ?", projectId);
        }catch (Exception ex){
            logger.error("query project info error!",ex);
        }
        return projectEntity;
    }
}
