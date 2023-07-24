package com.dtstep.lighthouse.web.service.login;

import com.dtstep.lighthouse.common.enums.result.RequestCodeEnum;

import javax.servlet.http.HttpServletRequest;

public interface LoginService {

    RequestCodeEnum login(HttpServletRequest request, String userName, String password) throws Exception;

    void signout(HttpServletRequest request) throws Exception;

}
