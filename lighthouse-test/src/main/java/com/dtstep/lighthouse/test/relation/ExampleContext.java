package com.dtstep.lighthouse.test.relation;

import com.dtstep.lighthouse.common.modal.Group;
import com.dtstep.lighthouse.common.modal.Project;

public class ExampleContext {

    private Integer adminId;

    private Integer departmentId;

    private Integer departManageRoleId;

    private Integer departAccessRoleId;

    private Project project;

    private Group group;

    private String token;

    private Integer projectManageRoleId;

    private Integer projectAccessRoleId;

    private Integer groupManageRoleId;

    private Integer groupAccessRoleId;

    private Integer metaId;

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getDepartManageRoleId() {
        return departManageRoleId;
    }

    public void setDepartManageRoleId(Integer departManageRoleId) {
        this.departManageRoleId = departManageRoleId;
    }

    public Integer getDepartAccessRoleId() {
        return departAccessRoleId;
    }

    public void setDepartAccessRoleId(Integer departAccessRoleId) {
        this.departAccessRoleId = departAccessRoleId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getProjectManageRoleId() {
        return projectManageRoleId;
    }

    public void setProjectManageRoleId(Integer projectManageRoleId) {
        this.projectManageRoleId = projectManageRoleId;
    }

    public Integer getProjectAccessRoleId() {
        return projectAccessRoleId;
    }

    public void setProjectAccessRoleId(Integer projectAccessRoleId) {
        this.projectAccessRoleId = projectAccessRoleId;
    }

    public Integer getGroupManageRoleId() {
        return groupManageRoleId;
    }

    public void setGroupManageRoleId(Integer groupManageRoleId) {
        this.groupManageRoleId = groupManageRoleId;
    }

    public Integer getGroupAccessRoleId() {
        return groupAccessRoleId;
    }

    public void setGroupAccessRoleId(Integer groupAccessRoleId) {
        this.groupAccessRoleId = groupAccessRoleId;
    }

    public Integer getMetaId() {
        return metaId;
    }

    public void setMetaId(Integer metaId) {
        this.metaId = metaId;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
