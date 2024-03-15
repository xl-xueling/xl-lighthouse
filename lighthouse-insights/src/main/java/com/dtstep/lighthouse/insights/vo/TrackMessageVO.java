package com.dtstep.lighthouse.insights.vo;

import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.Serializable;
import java.util.List;

public class TrackMessageVO implements Serializable {

    private List<String> columns;

    private ArrayNode data;

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public ArrayNode getData() {
        return data;
    }

    public void setData(ArrayNode data) {
        this.data = data;
    }
}
