package com.dtstep.lighthouse.web.service.menu.impl;

import com.dtstep.lighthouse.common.entity.manage.MenuEntity;
import com.dtstep.lighthouse.web.service.menu.MenuService;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    @Override
    public HashMap<String,List<MenuEntity>> queryMenuInfo(int userId) throws Exception {
        return new HashMap<>();
    }
}
