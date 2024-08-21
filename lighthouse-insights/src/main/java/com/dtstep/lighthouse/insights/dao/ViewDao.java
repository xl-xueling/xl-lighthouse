package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.common.modal.View;
import com.dtstep.lighthouse.common.modal.ViewQueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViewDao {

    int insert(View view);

    int update(View view);

    int deleteById(Integer id);

    View queryById(Integer id);

    List<View> queryList(@Param("queryParam") ViewQueryParam queryParam);
}
