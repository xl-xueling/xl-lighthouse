package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.ListData;
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.enums.ComponentTypeEnum;
import com.dtstep.lighthouse.common.modal.RenderFilterConfig;
import com.dtstep.lighthouse.common.modal.TreeNode;
import com.dtstep.lighthouse.core.storage.dimens.DimensStorageSelector;
import com.dtstep.lighthouse.core.wrapper.GroupDBWrapper;
import com.dtstep.lighthouse.insights.dto.ComponentQueryParam;
import com.dtstep.lighthouse.insights.dto.FilterQueryParam;
import com.dtstep.lighthouse.insights.dto.StatQueryParam;
import com.dtstep.lighthouse.insights.service.ComponentService;
import com.dtstep.lighthouse.insights.service.FilterService;
import com.dtstep.lighthouse.insights.service.StatService;
import com.dtstep.lighthouse.insights.util.TreeUtil;
import com.dtstep.lighthouse.insights.vo.ComponentVO;
import com.dtstep.lighthouse.insights.vo.StatVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.stream.Collectors.toList;

@Service
public class FilterServiceImpl implements FilterService {

    @Autowired
    private StatService statService;

    @Autowired
    private ComponentService componentService;

    @Override
    public ListData<RenderFilterConfig> queryDefaultList(String token, Integer pageNum, Integer pageSize) throws Exception {
        GroupExtEntity groupExtEntity = GroupDBWrapper.queryByToken(token);
        Validate.notNull(groupExtEntity);
        StatQueryParam statQueryParam = new StatQueryParam();
        statQueryParam.setGroupIds(List.of(groupExtEntity.getId()));
        ListData<StatVO> statList = statService.queryList(statQueryParam,1,500);
        Set<String> dimensSet = new HashSet<>();
        for(StatVO statVO : statList.getList()){
            String [] dimensArray = statVO.getTemplateEntity().getDimensArray();
            if(ArrayUtils.isNotEmpty(dimensArray)){
                List<String> list = Arrays.asList(dimensArray);
                dimensSet.addAll(list);
            }
        }
        List<RenderFilterConfig> resultList = new ArrayList<>();
        int startRow = (pageNum - 1) * pageSize;
        int endRow = Math.min(pageNum * pageSize, dimensSet.size());
        List<String> dimensList = new ArrayList<>(dimensSet);
        for(int i=startRow;i < endRow;i++){
            String dimens = dimensList.get(i);
            List<String> dimensValueList = DimensStorageSelector.query(groupExtEntity,dimens, null, StatConst.DIMENS_THRESHOLD_LIMIT_COUNT);
            List<TreeNode> treeNodes = dimensValueList.stream().map(z -> new TreeNode(z,z)).collect(toList());
            RenderFilterConfig renderFilterConfig = new RenderFilterConfig();
            renderFilterConfig.setComponentType(ComponentTypeEnum.FILTER_SELECT);
            renderFilterConfig.setComponentId(0);
            renderFilterConfig.setLabel(dimens);
            renderFilterConfig.setToken(token);
            renderFilterConfig.setDimens(dimens);
            renderFilterConfig.setConfigData(treeNodes);
            resultList.add(renderFilterConfig);
        }
        return ListData.newInstance(resultList, dimensList.size(), pageNum,pageSize);
    }

    @Override
    public ListData<RenderFilterConfig> queryCustomList(FilterQueryParam queryParam, Integer pageNum, Integer pageSize) throws Exception {
        ComponentQueryParam componentQueryParam = new ComponentQueryParam();
        componentQueryParam.setSearch(queryParam.getSearch());
        ListData<ComponentVO> listData = componentService.queryList(componentQueryParam,pageNum,pageSize);
        if(listData == null){
            return null;
        }
        List<RenderFilterConfig> resultList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(listData.getList())){
            for(ComponentVO componentVO : listData.getList()){
                RenderFilterConfig renderFilterConfig = new RenderFilterConfig();
                renderFilterConfig.setComponentType(ComponentTypeEnum.FILTER_SELECT);
                int level = TreeUtil.getMaxLevel(componentVO.getConfiguration());
                renderFilterConfig.setMaxLevel(level);
                renderFilterConfig.setComponentId(componentVO.getId());
                renderFilterConfig.setTitle(componentVO.getTitle());
                renderFilterConfig.setConfigData(componentVO.getConfiguration());
                resultList.add(renderFilterConfig);
            }
        }
        return ListData.newInstance(resultList, listData.getTotal(), pageNum,pageSize);
    }
}
