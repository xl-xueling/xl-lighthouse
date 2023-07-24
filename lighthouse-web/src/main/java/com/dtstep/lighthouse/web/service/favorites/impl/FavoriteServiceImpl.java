package com.dtstep.lighthouse.web.service.favorites.impl;

import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.entity.project.ProjectEntity;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.entity.tree.ZTreeViewNode;
import com.dtstep.lighthouse.common.enums.relations.RelationTypeEnum;
import com.dtstep.lighthouse.common.enums.stat.StatStateEnum;
import com.dtstep.lighthouse.web.manager.group.GroupManager;
import com.dtstep.lighthouse.web.manager.project.ProjectManager;
import com.dtstep.lighthouse.web.manager.relations.RelationManager;
import com.dtstep.lighthouse.web.manager.stat.StatManager;
import com.dtstep.lighthouse.web.service.favorites.FavoriteService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private RelationManager relationManager;

    @Autowired
    private ProjectManager projectManager;

    @Autowired
    private GroupManager groupManager;

    @Autowired
    private StatManager statManager;

    @Override
    public List<ZTreeViewNode> queryProjectZTreeInfo(int userId) throws Exception {
        List<Integer> list = relationManager.queryListByUserId(userId, RelationTypeEnum.FAVORITE_PROJECT);
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        List<ZTreeViewNode> viewNodeList = new ArrayList<>();
        for(Integer id : list){
            ProjectEntity projectEntity = projectManager.queryById(id);
            if(projectEntity == null){
                continue;
            }
            ZTreeViewNode projectNode = new ZTreeViewNode();
            projectNode.setId(String.valueOf(projectEntity.getId()));
            projectNode.setpId(SysConst.TREE_ROOT_NODE_NAME);
            projectNode.setType(1);
            projectNode.setOpen(true);
            projectNode.setName("i18n(ldp_i18n_favorite_project_1004)"+projectEntity.getName());
            projectNode.setIcon("/static/extend/png/depart.png");
            viewNodeList.add(projectNode);
            List<GroupExtEntity> groupList = groupManager.queryListByProjectId(id);
            if(CollectionUtils.isEmpty(groupList)){
                continue;
            }
            for (GroupExtEntity groupExtEntity : groupList) {
                ZTreeViewNode groupNode = new ZTreeViewNode();
                groupNode.setName("i18n(ldp_i18n_favorite_project_1005)" + groupExtEntity.getToken());
                groupNode.setIcon("/static/extend/png/plugin.png");
                groupNode.setType(2);
                groupNode.setId(String.valueOf(groupExtEntity.getId()));
                groupNode.setpId(String.valueOf(projectEntity.getId()));
                viewNodeList.add(groupNode);
                List<StatExtEntity> itemList = statManager.queryListByGroupId(groupExtEntity.getId());
                if(CollectionUtils.isEmpty(itemList)){
                    continue;
                }
                for (StatExtEntity statExtEntity : itemList) {
                    ZTreeViewNode statNode = new ZTreeViewNode();
                    statNode.setId(String.valueOf(statExtEntity.getId()));
                    statNode.setpId(String.valueOf(groupExtEntity.getId()));
                    statNode.setName(statExtEntity.getTitle());
                    if(statExtEntity.getStatStateEnum() == StatStateEnum.RUNNING){
                        statNode.setIcon("/static/extend/png/running.png");
                    }else if(statExtEntity.getStatStateEnum() == StatStateEnum.LIMITING){
                        statNode.setIcon("/static/extend/png/pause.png");
                    }else{
                        statNode.setIcon("/static/extend/png/stop.png");
                    }
                    statNode.setType(3);
                    viewNodeList.add(statNode);
                }
            }
        }
        return viewNodeList;
    }


    @Override
    public List<ZTreeViewNode> queryStatZTreeInfo(int userId) throws Exception {
        List<Integer> ids = relationManager.queryListByUserId(userId, RelationTypeEnum.FAVORITE_ITEM);
        if(CollectionUtils.isEmpty(ids)){
            return null;
        }
        List<ZTreeViewNode> viewNodeList = new ArrayList<>();
        ZTreeViewNode rootNode = new ZTreeViewNode();
        rootNode.setId("0");
        rootNode.setpId(SysConst.TREE_ROOT_NODE_NAME);
        rootNode.setName("i18n(ldp_i18n_favorite_stat_1005)");
        rootNode.setIcon("/static/extend/png/root.png");
        rootNode.setOpen(true);
        rootNode.setType(1);
        viewNodeList.add(rootNode);
        for(Integer id : ids){
            StatExtEntity statExtEntity = statManager.queryById(id);
            if(statExtEntity == null){
                continue;
            }
            ZTreeViewNode projectNode = new ZTreeViewNode();
            projectNode.setId(String.valueOf(statExtEntity.getId()));
            projectNode.setpId("0");
            projectNode.setType(2);
            projectNode.setOpen(true);
            projectNode.setName(statExtEntity.getTitle());
            if(statExtEntity.getStatStateEnum() == StatStateEnum.RUNNING){
                projectNode.setIcon("/static/extend/png/running.png");
            }else if(statExtEntity.getStatStateEnum() == StatStateEnum.LIMITING){
                projectNode.setIcon("/static/extend/png/pause.png");
            }else{
                projectNode.setIcon("/static/extend/png/stop.png");
            }
            viewNodeList.add(projectNode);
        }
        return viewNodeList;
    }
}
