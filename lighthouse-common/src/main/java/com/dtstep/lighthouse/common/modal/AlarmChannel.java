package com.dtstep.lighthouse.common.modal;

import com.dtstep.lighthouse.common.enums.LanguageEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.validation.constraints.NotBlank;
import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CustomAlarmChannel.class, name = "custom"),
        @JsonSubTypes.Type(value = AliyunAlarmChannel.class, name = "aliyun"),
})
public class AlarmChannel {

    @JsonIgnore
    private String type;

    @NotBlank
    private String endpoint;

    @NotBlank
    private List<String> params;

    private LanguageEnum lang = LanguageEnum.CHINESE;

    protected Integer timeout = 5000;

    private boolean enabled;

    public AlarmChannel(String type) {
        this.type = type;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public LanguageEnum getLang() {
        return lang;
    }

    public void setLang(LanguageEnum lang) {
        this.lang = lang;
    }
}
