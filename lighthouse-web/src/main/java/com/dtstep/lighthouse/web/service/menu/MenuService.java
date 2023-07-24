package com.dtstep.lighthouse.web.service.menu;

import com.dtstep.lighthouse.common.entity.manage.MenuEntity;

import java.util.HashMap;
import java.util.List;

public interface MenuService {

    HashMap<String,List<MenuEntity>> queryMenuInfo(int userId) throws Exception;
}
