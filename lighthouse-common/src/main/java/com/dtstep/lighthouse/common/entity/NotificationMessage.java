package com.dtstep.lighthouse.common.entity;

import com.dtstep.lighthouse.common.modal.User;

import java.io.Serializable;
import java.util.List;

public class NotificationMessage implements Serializable {

    private String strategy;

    private String uniqueCode;

    private String dimensValue;

    private String time;

    private String current;

    private String threshold;

    private String priority;

    private String operator;

    private String comparator;

    private String message;

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

    public String getDimensValue() {
        return dimensValue;
    }

    public void setDimensValue(String dimensValue) {
        this.dimensValue = dimensValue;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getThreshold() {
        return threshold;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getComparator() {
        return comparator;
    }

    public void setComparator(String comparator) {
        this.comparator = comparator;
    }
}
