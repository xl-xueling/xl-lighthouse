package com.dtstep.lighthouse.core.dao;

import com.dtstep.lighthouse.common.exception.InitializationException;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RDBMSConfiguration implements Serializable {

    private String database;

    private String driverClassName;

    private String connectionURL;

    private String connectionUserName;

    private String connectionPassword;

    public RDBMSConfiguration(String driverClassName, String connectionURL, String connectionUserName, String connectionPassword){
        this.driverClassName = driverClassName;
        this.connectionURL = connectionURL;
        Pattern pattern = Pattern.compile("jdbc:mysql://(.*):(\\d+)/(.+)\\?.*");
        Matcher matcher = pattern.matcher(connectionURL);
        if (matcher.find()) {
            this.database = matcher.group(3);
        } else {
            throw new InitializationException("Failed to parse rdbms configuration!");
        }
        this.connectionUserName = connectionUserName;
        this.connectionPassword = connectionPassword;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getConnectionURL() {
        return connectionURL;
    }

    public void setConnectionURL(String connectionURL) {
        this.connectionURL = connectionURL;
    }

    public String getConnectionUserName() {
        return connectionUserName;
    }

    public void setConnectionUserName(String connectionUserName) {
        this.connectionUserName = connectionUserName;
    }

    public String getConnectionPassword() {
        return connectionPassword;
    }

    public void setConnectionPassword(String connectionPassword) {
        this.connectionPassword = connectionPassword;
    }
}
