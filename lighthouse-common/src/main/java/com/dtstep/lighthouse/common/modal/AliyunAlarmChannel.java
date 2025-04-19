package com.dtstep.lighthouse.common.modal;

import javax.validation.constraints.NotBlank;

public class AliyunAlarmChannel extends AlarmChannel {

    public AliyunAlarmChannel() {
        super("aliyun");
    }

    @NotBlank
    private String accessKeyId;

    @NotBlank
    private String accessKeySecret;

    @NotBlank
    private String signName;

    @NotBlank
    private String templateCode;

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }
}
