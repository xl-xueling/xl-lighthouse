package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.common.enums.UserStateEnum;
import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import com.dtstep.lighthouse.common.util.Md5Util;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.commonv2.constant.SystemConstant;
import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.controller.annotation.AuthPermission;
import com.dtstep.lighthouse.insights.dto.*;
import com.dtstep.lighthouse.insights.dto_bak.ResultData;
import com.dtstep.lighthouse.insights.config.SeedAuthenticationToken;
import com.dtstep.lighthouse.insights.dto_bak.*;
import com.dtstep.lighthouse.insights.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.*;
import com.dtstep.lighthouse.insights.service.*;
import com.dtstep.lighthouse.insights.vo.UserVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@ControllerAdvice
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private BaseService baseService;

    @RequestMapping("/user/register")
    public ResultData<Integer> register(@Validated @RequestBody UserCreateParam createParam) throws Exception{
        String userName = createParam.getUsername();
        boolean isExist = userService.isUserNameExist(userName);
        if(isExist){
            return ResultData.result(ResultCode.registerUserNameExist);
        }
        User user = new User();
        BeanCopyUtil.copy(createParam,user);
        int result = userService.create(user,SystemConstant.REGISTER_NEED_APPROVE);
        if(result > 0){
            return ResultData.success(result);
        }else{
            return ResultData.result(ResultCode.systemError);
        }
    }

    @RequestMapping("/user/fetchUserInfo")
    public ResultData<UserVO> fetchUserInfo(){
        SeedAuthenticationToken authentication = (SeedAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Integer userId = authentication.getUserId();
        User user = userService.queryById(userId);
        Validate.notNull(user);
        UserVO userVO = new UserVO(user);
        Set<PermissionEnum> permissionEnumSets = userService.getUserPermissions(userId);
        userVO.setPermissions(permissionEnumSets);
        return ResultData.success(userVO);
    }

    @RequestMapping("/user/updateById")
    public ResultData<Integer> updateById(@Validated @RequestBody UserUpdateParam updateParam) {
        Integer userId = updateParam.getId();
        SeedAuthenticationToken authentication = (SeedAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Integer currentUserId = authentication.getUserId();
        if(currentUserId.intValue() != userId.intValue()){
            return ResultData.result(ResultCode.systemError);
        }
        User userInfo = new User();
        userInfo.setId(userId);
        userInfo.setEmail(updateParam.getEmail());
        userInfo.setPhone(updateParam.getPhone());
        userInfo.setDepartmentId(updateParam.getDepartmentId());
        int id = userService.update(userInfo);
        if(id > 0){
            return ResultData.success(id);
        }else{
            return ResultData.result(ResultCode.systemError);
        }
    }

    @RequestMapping("/user/changePassword")
    public ResultData<Integer> changePassword(@Validated @RequestBody ChangePasswordParam updateParam) {
        Integer userId = updateParam.getId();
        SeedAuthenticationToken authentication = (SeedAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Integer currentUserId = authentication.getUserId();
        if(currentUserId.intValue() != userId.intValue()){
            return ResultData.result(ResultCode.paramValidateFailed);
        }
        String username = updateParam.getUsername();;
        User dbUser = userService.queryAllInfoByUserName(username);
        String originPassword = updateParam.getOriginPassword();
        if(dbUser == null
                || dbUser.getState() != UserStateEnum.USER_NORMAL
                || dbUser.getId().intValue() != userId.intValue()){
            return ResultData.result(ResultCode.unauthorized);
        }
        if(!passwordEncoder.matches(originPassword,dbUser.getPassword())){
            return ResultData.result(ResultCode.userChangePasswordWrong);
        }
        User userInfo = new User();
        userInfo.setId(userId);
        userInfo.setPassword(passwordEncoder.encode(updateParam.getPassword()));
        int id = userService.update(userInfo);
        if(id > 0){
            return ResultData.success(id);
        }else{
            return ResultData.result(ResultCode.systemError);
        }
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.OPT_MANAGE_PERMISSION)
    @RequestMapping("/user/changeState")
    public ResultData<Integer> changeState(@Validated @RequestBody ChangeUserStateParam changeParam) {
        Integer id = changeParam.getId();
        UserStateEnum userStateEnum = changeParam.getState();
        Validate.notNull(id);
        Validate.notNull(userStateEnum);
        User dbUser = userService.queryById(id);
        Validate.notNull(dbUser);
        User user = new User();
        user.setState(userStateEnum);
        user.setId(id);
        int result = userService.update(user);
        if(id > 0){
            return ResultData.success(id);
        }else{
            return ResultData.result(ResultCode.systemError);
        }
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.OPT_MANAGE_PERMISSION)
    @RequestMapping("/user/resetPasswd")
    public ResultData<Integer> resetPasswd(@Validated @RequestBody IDParam idParam) {
        Integer id = idParam.getId();
        User dbUser = userService.queryById(id);
        Validate.notNull(dbUser);
        User userInfo = new User();
        userInfo.setId(id);
        userInfo.setPassword(passwordEncoder.encode(Md5Util.getMD5(SystemConstant.DEFAULT_PASSWORD)));
        int result = userService.update(userInfo);
        if(result > 0){
            return ResultData.success(id);
        }else{
            return ResultData.result(ResultCode.systemError);
        }
    }

    @AuthPermission(roleTypeEnum = RoleTypeEnum.OPT_MANAGE_PERMISSION)
    @RequestMapping("/user/deleteById")
    public ResultData<Integer> delete(@Validated @RequestBody IDParam idParam) {
        Integer id = idParam.getId();
        int currentUserId = baseService.getCurrentUserId();
        if(id.intValue() == currentUserId){
            return ResultData.result(ResultCode.userDelErrorCannotDelCurrentUser);
        }
        List<Permission> permissions = permissionService.queryUserManagePermission(id,1);
        String message;
        if(CollectionUtils.isNotEmpty(permissions)){
            Integer roleId = permissions.get(0).getRoleId();
            ResourceDto resource = resourceService.queryByRoleId(roleId);
            if(resource != null){
                ResourceTypeEnum resourceTypeEnum = resource.getResourceType();
                if(resourceTypeEnum == ResourceTypeEnum.Department){
                    return ResultData.result(ResultCode.userDelErrorExistDepartPermission,((Department)resource.getData()).getName());
                }else if(resourceTypeEnum == ResourceTypeEnum.Project){
                    return ResultData.result(ResultCode.userDelErrorExistProjectPermission,((Project)resource.getData()).getTitle());
                }else if(resourceTypeEnum == ResourceTypeEnum.Group){
                    return ResultData.result(ResultCode.userDelErrorExistGroupPermission,((Group)resource.getData()).getToken());
                }else if(resourceTypeEnum == ResourceTypeEnum.Stat){
                    return ResultData.result(ResultCode.userDelErrorExistStatPermission,((Stat)resource.getData()).getTitle());
                }
            }
        }
        int result = userService.deleteById(id);
        if(result > 0){
            return ResultData.success();
        }else{
            return ResultData.result(ResultCode.systemError);
        }
    }


    @AuthPermission(roleTypeEnum = RoleTypeEnum.OPT_MANAGE_PERMISSION)
    @PostMapping("/user/list")
    public ResultData<ListData<UserVO>> list(
            @Validated @RequestBody ListSearchObject<UserQueryParam> searchObject) {
        Pagination pagination = searchObject.getPagination();
        ListData<UserVO> listData = userService.queryList(searchObject.getQueryParams(),pagination.getPageNum(),pagination.getPageSize());
        return ResultData.success(listData);
    }


    @PostMapping("/user/termList")
    public ResultData<List<User>> termList(@RequestBody TextParam text){
        String search = text.getText();
        if(!StringUtil.isLetterNumOrUnderLine(search)){
            return ResultData.result(ResultCode.paramValidateFailed);
        }
        List<User> users = userService.termQuery(search);
        return ResultData.success(users);
    }

}
