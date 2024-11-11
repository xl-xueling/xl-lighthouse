package com.dtstep.lighthouse.common.entity;

import com.dtstep.lighthouse.common.enums.NotificationTypeEnum;
import com.dtstep.lighthouse.common.modal.User;

import java.io.Serializable;
import java.util.List;

public class NotificationMessage implements Serializable {

    private NotificationTypeEnum type;

    private String message;

    private String uniqueCode;

    private List<User> users;

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public NotificationTypeEnum getType() {
        return type;
    }

    public void setType(NotificationTypeEnum type) {
        this.type = type;
    }
}
