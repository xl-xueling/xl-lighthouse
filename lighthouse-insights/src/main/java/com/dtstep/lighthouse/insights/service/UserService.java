package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.insights.modal.User;

public interface UserService {

    int create(User user);

    User queryById(int id);

    User queryByUserName(String userName);
}
