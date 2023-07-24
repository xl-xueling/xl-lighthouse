package com.dtstep.lighthouse.web.service.order.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Sets;
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.entity.list.ListViewDataObject;
import com.dtstep.lighthouse.common.entity.order.OrderEntity;
import com.dtstep.lighthouse.common.entity.order.OrderViewEntity;
import com.dtstep.lighthouse.common.entity.paging.PageEntity;
import com.dtstep.lighthouse.common.entity.sitemap.SiteMapBindEntity;
import com.dtstep.lighthouse.common.entity.stat.StatEntity;
import com.dtstep.lighthouse.common.entity.user.UserEntity;
import com.dtstep.lighthouse.common.enums.order.OrderStateEnum;
import com.dtstep.lighthouse.common.enums.order.OrderTypeEnum;
import com.dtstep.lighthouse.common.enums.role.PrivilegeTypeEnum;
import com.dtstep.lighthouse.common.enums.sitemap.SiteBindElementTypeEnum;
import com.dtstep.lighthouse.common.enums.sitemap.SiteBindStateEnum;
import com.dtstep.lighthouse.common.enums.stat.StatStateEnum;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.dao.ConnectionManager;
import com.dtstep.lighthouse.core.dao.DBConnection;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import com.dtstep.lighthouse.core.sql.SqlBinder;
import com.dtstep.lighthouse.web.manager.group.GroupManager;
import com.dtstep.lighthouse.web.manager.order.OrderManager;
import com.dtstep.lighthouse.web.manager.privilege.PrivilegeManager;
import com.dtstep.lighthouse.web.manager.project.ProjectManager;
import com.dtstep.lighthouse.web.manager.stat.StatManager;
import com.dtstep.lighthouse.web.manager.user.UserManager;
import com.dtstep.lighthouse.web.param.ParamWrapper;
import com.dtstep.lighthouse.web.service.order.ApproveService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ApproveServiceImpl implements ApproveService {

    private static final Logger logger = LoggerFactory.getLogger(ApproveServiceImpl.class);

    @Autowired
    private PrivilegeManager privilegeManager;

    @Autowired
    private ProjectManager projectManager;

    @Autowired
    private GroupManager groupManager;

    @Autowired
    private StatManager statManager;

    @Autowired
    private OrderManager orderManager;

    @Autowired
    private UserManager userManager;

    @Override
    public ListViewDataObject queryListByPage(UserEntity currentUser, int page, int state, String search) throws Exception {
        int totalSize = countApproveByParam(currentUser,state,search);
        ListViewDataObject listObject = new ListViewDataObject();
        HashMap<String,Object> urlMap = new HashMap<>();
        urlMap.put("state",state);
        urlMap.put("search",search);
        String baseUrl = ParamWrapper.getBaseLink("/approve/list.shtml",urlMap);
        PageEntity pageEntity = new PageEntity(baseUrl, page,totalSize, SysConst._LIST_PAGE_SIZE);
        listObject.setPageEntity(pageEntity);
        List<OrderViewEntity> viewEntityList = queryListByPage(currentUser,state,search,pageEntity);
        listObject.setDataList(viewEntityList);
        return listObject;
    }

    @Override
    public int countApproveByParam(UserEntity currentUser, int state, String search) throws Exception {
        SqlBinder sqlBinder = new SqlBinder.Builder()
                .appendSegment("select count(1) from (select a.* from ldp_order a inner join (select * from ldp_privilege where relation_a = '"+currentUser.getId()+"') b \n" +
                        "on a.privilege_kid = b.relation_b and a.privilege_type = b.privilege_type) c left join ldp_user d on c.user_id = d.id")
                .appendWhere("c.state",state)
                .appendInExceptNull("c.state", Sets.newHashSet(OrderStateEnum.PEND.getState(),OrderStateEnum.APPROVED.getState(),OrderStateEnum.REJECTED.getState()))
                .appendLike("d.user_name",search)
                .create();
        return DaoHelper.sql.count(sqlBinder.toString());
    }


    protected List<OrderViewEntity> queryListByPage(UserEntity currentUser, int state, String search, PageEntity pageEntity) throws Exception{
        SqlBinder sqlBinder = new SqlBinder.Builder()
                .appendSegment("select c.*,d.user_name from (select a.* from ldp_order a inner join (select * from ldp_privilege where relation_a = '"+currentUser.getId()+"') b \n" +
                        "on a.privilege_kid = b.relation_b and a.privilege_type = b.privilege_type) c left join ldp_user d on c.user_id = d.id")
                .appendWhere("c.state",state)
                .appendInExceptNull("c.state", Sets.newHashSet(OrderStateEnum.PEND.getState(),OrderStateEnum.APPROVED.getState(),OrderStateEnum.REJECTED.getState()))
                .appendLike("d.user_name",search)
                .appendSegment("order by c.create_time desc limit ?,?")
                .create();
        List<OrderEntity> orderEntities = DaoHelper.sql.getList(OrderEntity.class,sqlBinder.toString(),pageEntity.getStartIndex() - 1, pageEntity.getPageSize());
        if(CollectionUtils.isEmpty(orderEntities)){
            return null;
        }
        List<OrderViewEntity> viewEntityList = new ArrayList<>();
        for (OrderEntity orderEntity : orderEntities) {
            try{
                OrderViewEntity orderViewEntity = new OrderViewEntity(orderEntity);
                OrderTypeEnum orderTypeEnum = OrderTypeEnum.getOrderType(orderEntity.getOrderType());
                if(orderTypeEnum != null){
                    orderViewEntity.setOrderTypeDesc(orderTypeEnum.getDesc());
                }
                orderViewEntity.setOrderStateDesc(Objects.requireNonNull(OrderStateEnum.getStateEnum(orderViewEntity.getState())).getDesc());
                List<UserEntity> admins = privilegeManager.queryAdmins(orderEntity.getPrivilegeKId(), orderEntity.getPrivilegeType());
                orderViewEntity.setAdmins(admins);
                String desc = orderManager.getDescription(orderEntity);
                UserEntity approveUser = userManager.queryById(orderEntity.getApproveUserId());
                if(approveUser != null){
                    orderViewEntity.setApproveUserEntity(approveUser);
                }
                orderViewEntity.setDesc(desc);
                viewEntityList.add(orderViewEntity);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return viewEntityList;
    }


    public void approve(UserEntity currentUser,OrderEntity orderEntity) throws Exception {
        DBConnection dbConnection = ConnectionManager.getConnection();
        ConnectionManager.beginTransaction(dbConnection);
        try{
            orderManager.changeState(orderEntity.getId(),currentUser.getId(),OrderStateEnum.APPROVED);
            approveCallBack(orderEntity);
            ConnectionManager.commitTransaction(dbConnection);
        }catch (Exception ex){
            ConnectionManager.rollbackTransaction(dbConnection);
            throw ex;
        }finally {
            ConnectionManager.close(dbConnection);
        }
    }

    @Override
    public void reject(UserEntity currentUser, OrderEntity orderEntity) throws Exception {
        DBConnection dbConnection = ConnectionManager.getConnection();
        ConnectionManager.beginTransaction(dbConnection);
        try{
            orderManager.changeState(orderEntity.getId(),currentUser.getId(),OrderStateEnum.REJECTED);
            rejectCallBack(orderEntity);
            ConnectionManager.commitTransaction(dbConnection);
        }catch (Exception ex){
            ConnectionManager.rollbackTransaction(dbConnection);
            throw ex;
        }finally {
            ConnectionManager.close(dbConnection);
        }
    }

    protected void approveCallBack(OrderEntity orderEntity) throws Exception {
        OrderTypeEnum orderTypeEnum = OrderTypeEnum.getOrderType(orderEntity.getOrderType());
        JsonNode paramNode = JsonUtil.readTree(orderEntity.getParams());
        if(orderTypeEnum == OrderTypeEnum.PROJECT_ACCESS){
            int projectId = paramNode.get("projectId").asInt();
            if(!projectManager.isExist(projectId)){
                return;
            }
            privilegeManager.createPrivilege(PrivilegeTypeEnum.STAT_PROJECT_USER,projectId,orderEntity.getUserId());
        }else if(orderTypeEnum == OrderTypeEnum.STAT_ACCESS){
            int statId = paramNode.get("statId").asInt();
            if(!statManager.isExist(statId)){
                return;
            }
            privilegeManager.createPrivilege(PrivilegeTypeEnum.STAT_ITEM_USER,statId,orderEntity.getUserId());
        }else if(orderTypeEnum == OrderTypeEnum.GROUP_THRESHOLD_ADJUST){
            int groupId = paramNode.get("groupId").asInt();
            if(!groupManager.isExist(groupId)){
                return;
            }
            String strategy = paramNode.get("strategy").asText();
            String value = paramNode.get("newValue").asText();
            ObjectNode thresholdNode = JsonUtil.createObjectNode();
            thresholdNode.put(strategy,value);
            groupManager.updateThreshold(groupId,thresholdNode);
        }else if(orderTypeEnum == OrderTypeEnum.STAT_ITEM_APPROVE){
            int statId = paramNode.get("statId").asInt();
            if(!statManager.isExist(statId)){
                return;
            }
            statManager.changeState(statId, StatStateEnum.RUNNING);
        }
    }

    protected void rejectCallBack(OrderEntity orderEntity) throws Exception {
        OrderTypeEnum orderTypeEnum = OrderTypeEnum.getOrderType(orderEntity.getOrderType());
        JsonNode paramNode = JsonUtil.readTree(orderEntity.getParams());
        if(orderTypeEnum == OrderTypeEnum.STAT_ITEM_APPROVE){
            int statId = paramNode.get("statId").asInt();
            if(!statManager.isExist(statId)){
                return;
            }
            statManager.changeState(statId, StatStateEnum.REJECTED);
        }
    }

}
