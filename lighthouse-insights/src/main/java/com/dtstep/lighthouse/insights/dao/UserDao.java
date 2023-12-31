package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.common.enums.user.UserStateEnum;
import com.dtstep.lighthouse.insights.dto.ChangePasswordParam;
import com.dtstep.lighthouse.insights.dto.UserQueryParam;
import com.dtstep.lighthouse.insights.dto.UserUpdateParam;
import com.dtstep.lighthouse.insights.modal.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {

    boolean isUserNameExist(String username);

    int insert(User user);
    
    User queryBasicInfoById(int id);

    int deleteById(int id);

    User queryAllInfoById(int id);

    User queryByUserName(String username);

    List<User> termQuery(String search);

    List<User> queryList(@Param("queryParam")UserQueryParam queryParam);

    int count(@Param("queryParam")UserQueryParam queryParam);

    int update(User user);

}
