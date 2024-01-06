package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dto.*;
import com.dtstep.lighthouse.insights.modal.User;

import java.util.List;

public interface UserService {

    int create(User user,boolean needApprove);

    int update(User user);

    List<User> termQuery(String search);

    boolean isUserNameExist(String username);

    User queryBasicInfoById(int id);

    User cacheQueryById(int id);

    User queryAllInfoById(int id);

    User queryByUserName(String userName);

    int deleteById(int userId);

    ListData<UserDto> queryList(UserQueryParam queryParam, Integer pageNum, Integer pageSize);

    int count(UserQueryParam queryParam);
}
