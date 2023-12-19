package com.dtstep.lighthouse.insights.service.user;

import com.dtstep.lighthouse.commonv2.entity.user.User;

public interface UserService {

    User queryById(int id);

    User queryByUserName(String userName);
}
