package com.dtstep.lighthouse.insights.dao;

import com.dtstep.lighthouse.insights.dto_bak.UserQueryParam;
import com.dtstep.lighthouse.insights.modal.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {

    boolean isUserNameExist(String username);

    int insert(User user);

    String getUserPassword(String username);

    int update(User user);
    
    User queryById(Integer id);

    User queryAllInfoByUserName(String username);

    int deleteById(Integer id);

    List<User> termQuery(String search);

    List<User> queryList(@Param("queryParam")UserQueryParam queryParam);

    int count(@Param("queryParam")UserQueryParam queryParam);

    String queryUserPassword(Integer id);

}
