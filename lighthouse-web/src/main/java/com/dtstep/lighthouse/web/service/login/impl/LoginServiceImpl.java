package com.dtstep.lighthouse.web.service.login.impl;

import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.entity.user.UserEntity;
import com.dtstep.lighthouse.common.enums.authorize.AuthorizeStateEnum;
import com.dtstep.lighthouse.common.enums.result.RequestCodeEnum;
import com.dtstep.lighthouse.common.enums.user.UserStateEnum;
import com.dtstep.lighthouse.common.util.Md5Util;
import com.dtstep.lighthouse.web.manager.login.LoginManager;
import com.dtstep.lighthouse.web.manager.user.UserManager;
import com.dtstep.lighthouse.web.service.login.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class LoginServiceImpl implements LoginService  {

    private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private UserManager userManager;

    @Autowired
    private LoginManager loginManager;

    @Override
    public RequestCodeEnum login(HttpServletRequest request,String userName, String password) throws Exception {
        if(SysConst._ADMIN_USER_NAME.equals(userName)){
            UserEntity dbUser = userManager.queryByUserName(userName);
            String initialPwd = Md5Util.getMD5(SysConst._SYS_INIT_PASSWORD);
            if(dbUser != null && password.equals(dbUser.getPassword())){
                userManager.refreshLoginTime(dbUser.getId());
                loginManager.refreshSession(request,dbUser);
                logger.info("user[{}] login success.",userName);
                return RequestCodeEnum.SUCCESS;
            }else if(dbUser == null && password.equals(initialPwd)){
                UserEntity adminUser = new UserEntity();
                adminUser.setDepartmentId(-1);
                Date date = new Date();
                adminUser.setCreateTime(date);
                adminUser.setLastTime(date);
                adminUser.setUserName(SysConst._ADMIN_USER_NAME);
                adminUser.setState(UserStateEnum.USR_NORMAL.getState());
                adminUser.setPassword(initialPwd);
                int userId = userManager.createAdmin(adminUser);
                adminUser.setId(userId);
                loginManager.refreshSession(request,adminUser);
                logger.info("user[{}] login success.",userName);
                return RequestCodeEnum.SUCCESS;
            }else{
                logger.info("login submit failure,userName or password is not correct.userName:{},password:{}.",userName,password);
                return RequestCodeEnum.USER_LOGIN_FAILURE;
            }
        }else{
            if(userManager.check(userName, password)){
                UserEntity userEntity = userManager.queryByUserName(userName);
                if(userEntity.getState() != UserStateEnum.USR_NORMAL.getState()){
                    logger.info("login submit failure,user status not available.userName:{}",userName);
                    return RequestCodeEnum.USER_STATUS_NOT_AVAILABLE;
                }else{
                    userManager.refreshLoginTime(userEntity.getId());
                    loginManager.refreshSession(request,userEntity);
                    logger.info("user[{}] login success.",userName);
                    return RequestCodeEnum.SUCCESS;
                }
            }else{
                logger.info("login submit failure,userName or password is not correct.userName:{},password:{}",userName,password);
                return RequestCodeEnum.USER_LOGIN_FAILURE;
            }
        }
    }

    @Override
    public void signout(HttpServletRequest request) throws Exception {
        loginManager.signout(request);
    }
}
