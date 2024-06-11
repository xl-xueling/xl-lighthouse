package com.dtstep.lighthouse.insights.vo;

import java.io.Serializable;

public class ExportVO implements Serializable {

    private String filename;

    private String content;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
