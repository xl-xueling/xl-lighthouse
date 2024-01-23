package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.dao.ComponentDao;
import com.dtstep.lighthouse.insights.dto_bak.TreeNode;
import com.dtstep.lighthouse.insights.vo.ComponentVO;
import com.dtstep.lighthouse.insights.dto.ComponentQueryParam;
import com.dtstep.lighthouse.insights.dto_bak.PermissionEnum;
import com.dtstep.lighthouse.insights.modal.Component;
import com.dtstep.lighthouse.insights.modal.User;
import com.dtstep.lighthouse.insights.service.BaseService;
import com.dtstep.lighthouse.insights.service.ComponentService;
import com.dtstep.lighthouse.insights.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ComponentServiceImpl implements ComponentService {

    private static final Logger logger = LoggerFactory.getLogger(ComponentServiceImpl.class);

    @Autowired
    private ComponentDao componentDao;

    @Autowired
    private BaseService baseService;

    @Autowired
    private UserService userService;

    @Override
    public ResultCode verify(String configuration) {
        JsonNode jsonNode;
        try{
            jsonNode = JsonUtil.readTree(configuration);
        }catch (Exception ex){
            return ResultCode.componentVerifyFormatError;
        }
        List<TreeNode> nodeList = JsonUtil.toJavaObjectList(configuration, TreeNode.class);
        return verifyConfiguration(nodeList,1,new ArrayList<String>());
    }

    private static final int MAX_DEPTH = 3;

    public static ResultCode verifyConfiguration(List<TreeNode> nodeList, int level,List<String> valueList){
        ResultCode resultCode = ResultCode.success;
        if(level > 3){
            return ResultCode.componentVerifyLevelLimit;
        }
        for(int i=0;i<nodeList.size();i++){
            TreeNode treeNode = nodeList.get(i);
            String label = treeNode.getLabel();
            Object value = treeNode.getValue();
            if(StringUtil.isEmpty(label) ){
                return ResultCode.getExtendResultCode(ResultCode.componentVerifyNotEmpty,new String[]{"label"});
            }
            if(value == null || StringUtil.isEmpty(value.toString())){
                return ResultCode.getExtendResultCode(ResultCode.componentVerifyNotEmpty,new String[]{"value"});
            }
            if(valueList.contains(value)){
                return ResultCode.getExtendResultCode(ResultCode.componentVerifyDuplicateValue,new String[]{value.toString()});
            }else{
                valueList.add(value.toString());
            }
            List<TreeNode> children = treeNode.getChildren();
            if(children != null && children.size() == 0){
                return ResultCode.getExtendResultCode(ResultCode.componentVerifyEmptyChildren,new String[]{label});
            }
            if(CollectionUtils.isNotEmpty(children)){
                resultCode = verifyConfiguration(children,level + 1,valueList);
                if(resultCode != ResultCode.success){
                    return resultCode;
                }
            }
        }
        return resultCode;
    }


    @Override
    public int create(Component component) {
        LocalDateTime localDateTime = LocalDateTime.now();
        component.setCreateTime(localDateTime);
        component.setUpdateTime(localDateTime);
        Integer id = componentDao.insert(component);
        return id;
    }

    @Override
    public int update(Component component) {
        LocalDateTime localDateTime = LocalDateTime.now();
        component.setUpdateTime(localDateTime);
        Integer id = componentDao.update(component);
        return id;
    }

    private ComponentVO translate(Component component){
        ComponentVO componentVO = new ComponentVO(component);
        int userId = component.getUserId();
        int currentUserId = baseService.getCurrentUserId();
        if(userId == currentUserId){
            componentVO.addPermission(PermissionEnum.ManageAble);
        }else{
            componentVO.addPermission(PermissionEnum.AccessAble);
        }
        User user = userService.cacheQueryById(userId);
        componentVO.setUser(user);
        return componentVO;
    }

    @Override
    public ListData<ComponentVO> queryList(ComponentQueryParam queryParam, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        ListData<ComponentVO> listData;
        PageInfo<Component> pageInfo = null;
        try{
            List<Component> components = componentDao.queryList(queryParam);
            pageInfo = new PageInfo<>(components);
        }finally {
            PageHelper.clearPage();
        }
        List<ComponentVO> dtoList = new ArrayList<>();
        for(Component component : pageInfo.getList()){
            try{
                ComponentVO componentVO = translate(component);
                dtoList.add(componentVO);
            }catch (Exception ex){
                logger.error("translate item info error,itemId:{}",component.getId(),ex);
            }
        }
        return ListData.newInstance(dtoList,pageInfo.getTotal(),pageNum,pageSize);
    }

    @Override
    public int delete(Component component) {
        return componentDao.deleteById(component.getId());
    }

    @Override
    public Component queryById(Integer id) {
        return componentDao.queryById(id);
    }
}
