package com.dtstep.lighthouse.core.dao;

import com.dtstep.lighthouse.common.exception.InitializationException;
import com.dtstep.lighthouse.common.util.Md5Util;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RDBMSConfiguration implements Serializable {

    private String dbInstance;

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
            String ip = matcher.group(1);
            String port = matcher.group(2);
            String dbName = matcher.group(3);
            this.dbInstance = Md5Util.get16MD5(ip + "_" + port + "_" + dbName);
        } else {
            throw new InitializationException("Failed to parse MySQL connection!");
        }
        this.connectionUserName = connectionUserName;
        this.connectionPassword = connectionPassword;
    }

    public String getDbInstance() {
        return dbInstance;
    }

    public void setDbInstance(String dbInstance) {
        this.dbInstance = dbInstance;
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
