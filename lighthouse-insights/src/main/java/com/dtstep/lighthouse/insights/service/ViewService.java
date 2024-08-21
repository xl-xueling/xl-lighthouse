package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.insights.vo.ProjectVO;
import com.dtstep.lighthouse.insights.vo.ViewVO;

import java.util.List;

public interface ViewService {

    List<ViewVO> queryByIds(List<Integer> ids);
}
