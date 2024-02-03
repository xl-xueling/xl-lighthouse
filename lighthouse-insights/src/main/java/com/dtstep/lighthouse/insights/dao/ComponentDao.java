package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.insights.dto.ComponentQueryParam;
import com.dtstep.lighthouse.common.modal.Component;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComponentDao {

    Integer insert(Component component);

    Integer update(Component component);

    int deleteById(Integer id);

    Component queryById(Integer id);

    List<Component> queryList(@Param("queryParam")ComponentQueryParam queryParam);
}
