package com.dtstep.lighthouse.common.modal;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Domain implements Serializable {

    private Integer id;

    private String name;

    private String defaultTokenPrefix;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultTokenPrefix() {
        return defaultTokenPrefix;
    }

    public void setDefaultTokenPrefix(String defaultTokenPrefix) {
        this.defaultTokenPrefix = defaultTokenPrefix;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }


}
