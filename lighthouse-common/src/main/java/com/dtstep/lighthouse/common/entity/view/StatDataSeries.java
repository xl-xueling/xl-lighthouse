package com.dtstep.lighthouse.common.entity.view;

import java.io.Serializable;
import java.util.List;

public class StatDataSeries implements Serializable {

    private Integer statId;

    private Integer indicatorIndex;

    private String indicatorAlias;

    private String dimens;

    private String displayDimens;

    private List<com.dtstep.lighthouse.common.entity.view.OperatorValue> valueList;

    public Integer getStatId() {
        return statId;
    }

    public void setStatId(Integer statId) {
        this.statId = statId;
    }

    public Integer getIndicatorIndex() {
        return indicatorIndex;
    }

    public void setIndicatorIndex(Integer indicatorIndex) {
        this.indicatorIndex = indicatorIndex;
    }

    public String getDimens() {
        return dimens;
    }

    public void setDimens(String dimens) {
        this.dimens = dimens;
    }

    public String getDisplayDimens() {
        return displayDimens;
    }

    public void setDisplayDimens(String displayDimens) {
        this.displayDimens = displayDimens;
    }

    public List<com.dtstep.lighthouse.common.entity.view.OperatorValue> getValueList() {
        return valueList;
    }

    public void setValueList(List<com.dtstep.lighthouse.common.entity.view.OperatorValue> valueList) {
        this.valueList = valueList;
    }

    public String getIndicatorAlias() {
        return indicatorAlias;
    }

    public void setIndicatorAlias(String indicatorAlias) {
        this.indicatorAlias = indicatorAlias;
    }
}
