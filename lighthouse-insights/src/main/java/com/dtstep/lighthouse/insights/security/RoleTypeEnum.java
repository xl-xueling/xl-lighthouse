package com.dtstep.lighthouse.insights.security;

public enum RoleTypeEnum {

    ADMIN("admin"),

    USER("user");

    private final String roleName;

    RoleTypeEnum(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
