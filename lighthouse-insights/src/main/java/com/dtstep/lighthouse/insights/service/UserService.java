package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dto.UserCreateParam;
import com.dtstep.lighthouse.insights.dto_bak.*;
import com.dtstep.lighthouse.insights.modal.User;

import java.util.List;

public interface UserService {

    int create(User user, boolean needApprove) throws Exception;

    int update(User user);

    List<User> termQuery(String search);

    boolean isUserNameExist(String username);

    User cacheQueryById(int id);

    User queryById(int id);

    User queryAllInfoByUserName(String userName);

    String queryUserPassword(Integer id);

    int deleteById(int userId);

    ListData<UserDto> queryList(UserQueryParam queryParam, Integer pageNum, Integer pageSize);

    int count(UserQueryParam queryParam);
}
