package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.common.enums.user.UserStateEnum;
import com.dtstep.lighthouse.insights.dto.UserQueryParam;
import com.dtstep.lighthouse.insights.modal.User;

import java.util.List;

public interface UserDao {

    int insert(User user);

    User queryById(int id);

    User queryByUserName(String username);

    List<User> queryList(UserQueryParam queryParam, Integer pageNum,Integer pageSize);

    Integer count(UserQueryParam queryParam);

    int update(User user);

    int changePasswd(Integer id,String password);

    int changeState(Integer id, UserStateEnum userStateEnum);
}
