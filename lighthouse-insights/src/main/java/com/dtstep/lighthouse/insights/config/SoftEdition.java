package com.dtstep.lighthouse.insights.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SoftEdition {

    @Value("${project.edition:opensource}")
    private String edition;

    public boolean isPro() {
        return "pro".equalsIgnoreCase(edition);
    }

    public boolean isOpenSource() {
        return "opensource".equalsIgnoreCase(edition);
    }

    public String getEdition() {
        return edition;
    }
}
