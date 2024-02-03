package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.common.enums.PrivateTypeEnum;
import com.dtstep.lighthouse.common.modal.Project;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

public class ProjectCreateParam {

    @NotEmpty
    @Pattern(regexp = "^[\\u3010\\u3011\\uFF08\\uFF09\\u4E00-\\u9FA5a-zA-Z0-9_\\-()\\[\\]#\\s]+$")
    private String title;

    @NotNull
    private Integer departmentId;

    @NotNull
    private PrivateTypeEnum privateType;

    @NotEmpty
    private String desc;

    private List<Integer> usersPermission;

    private List<Integer> departmentsPermission;

    public List<Integer> getUsersPermission() {
        return usersPermission;
    }

    public void setUsersPermission(List<Integer> usersPermission) {
        this.usersPermission = usersPermission;
    }

    public List<Integer> getDepartmentsPermission() {
        return departmentsPermission;
    }

    public void setDepartmentsPermission(List<Integer> departmentsPermission) {
        this.departmentsPermission = departmentsPermission;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public PrivateTypeEnum getPrivateType() {
        return privateType;
    }

    public void setPrivateType(PrivateTypeEnum privateType) {
        this.privateType = privateType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
