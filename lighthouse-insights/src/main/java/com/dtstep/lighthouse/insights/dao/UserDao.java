package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.insights.modal.User;

public interface UserDao {

    int insert(User user);

    User queryById(int id);

    User queryByUserName(String username);


}
