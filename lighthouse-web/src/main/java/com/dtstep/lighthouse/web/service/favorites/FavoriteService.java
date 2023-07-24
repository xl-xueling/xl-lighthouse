package com.dtstep.lighthouse.web.service.favorites;

import com.dtstep.lighthouse.common.entity.tree.ZTreeViewNode;

import java.util.List;

public interface FavoriteService {

    List<ZTreeViewNode> queryProjectZTreeInfo(int userId) throws Exception;

    List<ZTreeViewNode> queryStatZTreeInfo(int userId) throws Exception;
}
