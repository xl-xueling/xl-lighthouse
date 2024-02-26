package com.dtstep.lighthouse.common.enums;

public enum AuthRoleTypeEnum {

    ADMIN("admin"),

    USER("user");

    private final String roleName;

    AuthRoleTypeEnum(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
