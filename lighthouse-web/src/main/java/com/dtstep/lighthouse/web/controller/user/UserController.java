package com.dtstep.lighthouse.web.controller.user;
/*
 * Copyright (C) 2022-2023 XueLing.雪灵
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import com.dtstep.lighthouse.web.controller.annotation.AuthorityPermission;
import com.dtstep.lighthouse.web.manager.department.DepartmentManager;
import com.dtstep.lighthouse.web.manager.login.LoginManager;
import com.dtstep.lighthouse.web.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.entity.authorize.LicenceViewEntity;
import com.dtstep.lighthouse.common.entity.department.DepartmentViewEntity;
import com.dtstep.lighthouse.common.entity.list.ListViewDataObject;
import com.dtstep.lighthouse.common.entity.user.UserEntity;
import com.dtstep.lighthouse.common.enums.result.RequestCodeEnum;
import com.dtstep.lighthouse.common.enums.role.PrivilegeTypeEnum;
import com.dtstep.lighthouse.common.enums.user.UserStateEnum;
import com.dtstep.lighthouse.common.exception.ProcessException;
import com.dtstep.lighthouse.common.util.Md5Util;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.web.param.ParamWrapper;
import com.dtstep.lighthouse.web.controller.base.BaseController;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@ControllerAdvice
public class UserController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private DepartmentManager departmentManager;

    @Autowired
    private LoginManager loginManager;

    @RequestMapping("/user/register/index.shtml")
    public ModelAndView register(ModelMap model) throws Exception {
        List<DepartmentViewEntity> departmentList = departmentManager.queryAllViewInfo();
        model.addAttribute("departmentList",departmentList);
        return new ModelAndView("/user/user_register",model);
    }

    @RequestMapping("/user/register/submit.shtml")
    public @ResponseBody
    ObjectNode registerSubmit(HttpServletRequest request) throws Exception{
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String departmentId  = request.getParameter("departmentId");
        String phone = request.getParameter("phone");
        ParamWrapper.valid(UserEntity.class,"email",email);
        ParamWrapper.valid(UserEntity.class,"userName",userName);
        ParamWrapper.valid(UserEntity.class,"password",password);
        ParamWrapper.valid(UserEntity.class,"departmentId",departmentId);
        if(userName.equals(SysConst._ADMIN_USER_NAME)){
            logger.info("register submit,user name conflicts with administrator account,userName:{}",userName);
            return RequestCodeEnum.toJSON(RequestCodeEnum.USER_REGISTER_USERNAME_EXIST);
        }
        if(userService.countUserName(userName) > 0){
            return RequestCodeEnum.toJSON(RequestCodeEnum.USER_REGISTER_USERNAME_EXIST);
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(userName);
        userEntity.setDepartmentId(Integer.parseInt(departmentId));
        userEntity.setEmail(email);
        userEntity.setPhone(phone);
        userEntity.setState(UserStateEnum.USER_PEND.getState());
        userEntity.setPassword(password);
        try{
            userService.save(userEntity);
        }catch (Exception ex){
            logger.error("register submit,save user info error!",ex);
            return RequestCodeEnum.toJSON(RequestCodeEnum.SYSTEM_ERROR);
        }
        return RequestCodeEnum.toJSON(RequestCodeEnum.SUCCESS);
    }

    @AuthorityPermission(roleTypeEnum = PrivilegeTypeEnum.ADMIN)
    @RequestMapping("/user/list.shtml")
    public ModelAndView list(HttpServletRequest request, ModelMap model) throws Exception{
        int page = ParamWrapper.getIntValueOrElse(request, "page", 1);
        int state = ParamWrapper.getIntValueOrElse(request,"state",-1);
        int departmentId = ParamWrapper.getIntValueOrElse(request,"departmentId",-1);
        String search = request.getParameter("search");
        try{
            ListViewDataObject listObject = userService.queryListByPage(page,state,departmentId,search);
            model.addAttribute("listObject",listObject);
        }catch (Exception ex){
            logger.error("query user list error,params:[search:{},state:{}]!",search,state,ex);
            throw new ProcessException();
        }
        List<DepartmentViewEntity> departmentList = departmentManager.queryAllViewInfo();
        model.addAttribute("departmentList",departmentList);
        model.addAttribute("search",search);
        model.addAttribute("state",state);
        model.addAttribute("departmentId",departmentId);
        return new ModelAndView("/user/user_list",model);
    }

    @AuthorityPermission(roleTypeEnum = PrivilegeTypeEnum.ADMIN)
    @RequestMapping("/user/delete/submit.shtml")
    public @ResponseBody
    ObjectNode delete(HttpServletRequest request) {
        int id = ParamWrapper.getIntValue(request,"id");
        try{
            userService.deleteById(id);
        }catch (Exception ex){
            logger.error("delete user[id:{}] error!",id,ex);
            RequestCodeEnum.toJSON(RequestCodeEnum.SYSTEM_ERROR);
        }
        return RequestCodeEnum.toJSON(RequestCodeEnum.SUCCESS);
    }

    @AuthorityPermission(roleTypeEnum = PrivilegeTypeEnum.ADMIN)
    @RequestMapping("/user/resetPwd/submit.shtml")
    public @ResponseBody
    ObjectNode resetPassword(HttpServletRequest request) {
        int id = ParamWrapper.getIntValue(request, "id");
        try{
            String password = Md5Util.getMD5(SysConst._SYS_INIT_PASSWORD);
            userService.changePassword(id, password);
        }catch (Exception ex){
            logger.error("reset user[id:{}] password error!", id,ex);
            RequestCodeEnum.toJSON(RequestCodeEnum.SYSTEM_ERROR);
        }
        return RequestCodeEnum.toJSON(RequestCodeEnum.SUCCESS);
    }

    @AuthorityPermission(roleTypeEnum = PrivilegeTypeEnum.ADMIN)
    @RequestMapping("/user/activate/submit.shtml")
    public @ResponseBody
    ObjectNode activate(HttpServletRequest request) throws Exception {
        int id = ParamWrapper.getIntValue(request,"id");
        try{
            userService.changeState(id, UserStateEnum.USR_NORMAL);
        }catch (Exception ex){
            logger.error("activate user[id:{}] state error!",id ,ex);
            RequestCodeEnum.toJSON(RequestCodeEnum.SYSTEM_ERROR);
        }
        logger.info("activate user[id:{}] success!",id);
        return RequestCodeEnum.toJSON(RequestCodeEnum.SUCCESS);
    }


    @AuthorityPermission(roleTypeEnum = PrivilegeTypeEnum.ADMIN)
    @RequestMapping("/user/freeze/submit.shtml")
    public @ResponseBody
    ObjectNode freeze(HttpServletRequest request){
        int id = ParamWrapper.getIntValue(request,"id");
        try{
            userService.changeState(id, UserStateEnum.USER_FREEZE);
        }catch (Exception ex){
            logger.error("freeze user[id:{}] state error!",id, ex);
            RequestCodeEnum.toJSON(RequestCodeEnum.SYSTEM_ERROR);
        }
        logger.info("freeze user[id:{}] success!",id);
        return RequestCodeEnum.toJSON(RequestCodeEnum.SUCCESS);
    }

    @RequestMapping("/user/update/index.shtml")
    public ModelAndView update(HttpServletRequest request, ModelMap model) throws Exception {
        int id = ParamWrapper.getIntValue(request,"id");
        List<DepartmentViewEntity> departmentEntityList = departmentManager.queryAllViewInfo();
        model.addAttribute("departmentList",departmentEntityList);
        UserEntity userEntity = userService.queryById(id);
        Validate.notNull(userEntity);
        model.addAttribute("userEntity",userEntity);
        return new ModelAndView("/user/user_update",model);
    }

    @RequestMapping("/user/update/submit.shtml")
    public @ResponseBody
    ObjectNode updateSubmit(HttpServletRequest request) throws Exception{
        int id = ParamWrapper.getIntValue(request,"id");
        String userName = request.getParameter("userName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        ParamWrapper.valid(UserEntity.class,"userName",userName);
        ParamWrapper.valid(UserEntity.class,"email",email);
        ParamWrapper.valid(UserEntity.class,"phone",phone);
        int departmentId = ParamWrapper.getIntValueOrElse(request,"departmentId",-1);
        if(!userName.equals(SysConst._ADMIN_USER_NAME)){
            ParamWrapper.valid(UserEntity.class,"departmentId",departmentId);
        }
        UserEntity currentUser = ParamWrapper.getCurrentUser(request);
        if(currentUser.getId() != id){
            logger.info("update user submit,authority limit,id:{}",id);
            return RequestCodeEnum.toJSON(RequestCodeEnum.AUTHORITY_LIMIT);
        }
        UserEntity dbUser = userService.queryById(id);
        if(!dbUser.getUserName().equals(userName)){
            if(dbUser.getUserName().equals(SysConst._ADMIN_USER_NAME)){
                return RequestCodeEnum.toJSON(RequestCodeEnum.USER_ADMIN_USERNAME_UPDATE_ERROR);
            }
            if(userService.countUserName(userName) > 0){
                return RequestCodeEnum.toJSON(RequestCodeEnum.USER_REGISTER_USERNAME_EXIST);
            }
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setEmail(email);
        userEntity.setPhone(phone);
        userEntity.setUserName(userName);
        userEntity.setDepartmentId(departmentId);
        try{
            userService.update(userEntity);
            loginManager.refreshSession(request,userEntity);
        }catch (Exception ex){
            logger.error("update user submit,system error",ex);
            return RequestCodeEnum.toJSON(RequestCodeEnum.SYSTEM_ERROR);
        }
        return RequestCodeEnum.toJSON(RequestCodeEnum.SUCCESS);
    }

    @RequestMapping("/user/changepwd/index.shtml")
    public ModelAndView changePwd(){
        return new ModelAndView("/user/pwd_update");
    }

    @RequestMapping("/user/changepwd/submit.shtml")
    public @ResponseBody
    ObjectNode changePwdSubmit(HttpServletRequest request) throws Exception{
        int id = ParamWrapper.getIntValue(request, "id");
        String password = request.getParameter("password");
        String newPassword = request.getParameter("new_password");
        ParamWrapper.valid(UserEntity.class,"password",password);
        ParamWrapper.valid(UserEntity.class,"password",newPassword);
        UserEntity dbEntity = userService.queryById(id);
        Validate.notNull(dbEntity);
        if(!dbEntity.getPassword().equals(password)){
            logger.info("change password,origin password not right.password:{}",password);
            return RequestCodeEnum.toJSON(RequestCodeEnum.USER_PASSWORD_NOT_RIGHT);
        }
        try{
            userService.changePassword(id,newPassword);
            loginManager.signout(request);
        }catch (Exception ex){
            logger.error("change password,update user password error,userId:{},newPassword:{}",id,newPassword,ex);
            return RequestCodeEnum.toJSON(RequestCodeEnum.SYSTEM_ERROR);
        }
        return RequestCodeEnum.toJSON(RequestCodeEnum.SUCCESS);
    }

    @RequestMapping("/user/termQuery.shtml")
    public @ResponseBody
    ObjectNode termQuery(HttpServletRequest request){
        int page = ParamWrapper.getIntValueOrElse(request,"page",-1);
        String term = ParamWrapper.getValue(request,"term");
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode resultObj = objectMapper.createObjectNode();
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode dataArray = mapper.createArrayNode();
        resultObj.putPOJO("data", dataArray);
        resultObj.put("status", "0");
        if(page == -1 || StringUtil.isEmpty(term)){
            return resultObj;
        }
        try{
            List<UserEntity> userList = userService.termQuery(term,page);
            if(userList != null && !userList.isEmpty()){
                for(UserEntity userEntity:userList){
                    ObjectNode userObj = objectMapper.createObjectNode();
                    userObj.put("id",userEntity.getId());
                    userObj.put("text",userEntity.getUserName());
                    dataArray.add(userObj);
                }
            }
        }catch (Exception ex){
            logger.error("user term query load error!",ex);
            resultObj.put("status", "-1");
        }
        return resultObj;
    }
}
