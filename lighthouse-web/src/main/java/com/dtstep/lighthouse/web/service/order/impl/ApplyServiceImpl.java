package com.dtstep.lighthouse.web.service.order.impl;

import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.entity.list.ListViewDataObject;
import com.dtstep.lighthouse.common.entity.order.OrderEntity;
import com.dtstep.lighthouse.common.entity.order.OrderViewEntity;
import com.dtstep.lighthouse.common.entity.paging.PageEntity;
import com.dtstep.lighthouse.common.entity.user.UserEntity;
import com.dtstep.lighthouse.common.enums.order.OrderStateEnum;
import com.dtstep.lighthouse.common.enums.order.OrderTypeEnum;
import com.dtstep.lighthouse.core.dao.DaoHelper;
import com.dtstep.lighthouse.core.sql.SqlBinder;
import com.dtstep.lighthouse.web.manager.privilege.PrivilegeManager;
import com.dtstep.lighthouse.web.manager.order.OrderManager;
import com.dtstep.lighthouse.web.param.ParamWrapper;
import com.dtstep.lighthouse.web.service.order.ApplyService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ApplyServiceImpl implements ApplyService {

    private static final Logger logger = LoggerFactory.getLogger(ApplyServiceImpl.class);

    @Autowired
    private OrderManager orderManager;

    @Autowired
    private PrivilegeManager privilegeManager;

    @Override
    public void createOrder(OrderEntity orderEntity) throws Exception {
        orderManager.createOrder(orderEntity);
    }

    @Override
    public ListViewDataObject queryListByPage(UserEntity currentUser, int page, int state) throws Exception {
        int totalSize;
        try{
            totalSize = countApplyByParam(state);
        }catch (Exception ex){
            logger.error("count order approve list error!",ex);
            throw ex;
        }
        HashMap<String,Object> urlMap = new HashMap<>();
        urlMap.put("state",state);
        ListViewDataObject listObject = new ListViewDataObject();
        String baseUrl = ParamWrapper.getBaseLink("/approve/list.shtml",urlMap);
        PageEntity pageEntity = new PageEntity(baseUrl, page,totalSize, SysConst._LIST_PAGE_SIZE);
        listObject.setPageEntity(pageEntity);
        try{
            List<OrderViewEntity> viewEntityList = queryListByPage(state,pageEntity);
            listObject.setDataList(viewEntityList);
        }catch (Exception ex){
            logger.error("query order approve list error!",ex);
            throw ex;
        }
        return listObject;
    }

    public int countApplyByParam(int state) throws Exception {
        SqlBinder sqlBinder = new SqlBinder.Builder()
                .appendSegment("select count(1) from ldp_order a left join ldp_user b on a.user_id = b.id")
                .appendWhere("c.state",state)
                .create();
        return DaoHelper.sql.count(sqlBinder.toString());
    }


    protected List<OrderViewEntity> queryListByPage(int state, PageEntity pageEntity) throws Exception{
        SqlBinder sqlBinder = new SqlBinder.Builder()
                .appendSegment("select a.*,b.user_name from ldp_order a left join ldp_user b on a.user_id = b.id")
                .appendWhere("a.state",state)
                .appendSegment("order by a.create_time desc limit ?,?")
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
                orderViewEntity.setDesc(desc);
                viewEntityList.add(orderViewEntity);
            }catch (Exception ex){
                logger.error("combine apply view info error!",ex);
            }
        }
        return viewEntityList;
    }

    @Override
    public OrderEntity queryById(int orderId) throws Exception {
        return DaoHelper.sql.getItem(OrderEntity.class,"select * from ldp_order where id = ?" , orderId);
    }

    @Override
    public void retract(int orderId) throws Exception {
        orderManager.changeState(orderId,-1,OrderStateEnum.RETRACT);
    }
}
