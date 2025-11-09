package com.dtstep.lighthouse.common.entity;

public class LabelValue {

    private String label;

    private Object value;

    private Integer state = 0;

    public LabelValue(){}

    public LabelValue(String label,Object value){
        this.label = label;
        this.value = value;
    }

    public LabelValue(String label,Object value,Integer state){
        this.label = label;
        this.value = value;
        this.state = state;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
