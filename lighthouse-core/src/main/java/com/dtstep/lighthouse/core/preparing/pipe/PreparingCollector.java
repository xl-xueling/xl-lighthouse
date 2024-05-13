package com.dtstep.lighthouse.core.preparing.pipe;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class PreparingCollector<OUT> {

    private List<OUT> data = new ArrayList<>();

    public void add(OUT item){
        if(item != null){
            data.add(item);
        }
    }

    public void addAll(List<OUT> itemList){
        if(CollectionUtils.isNotEmpty(itemList)){
            data.addAll(itemList);
        }
    }

    public List<OUT> getData() {
        return data;
    }

    public void setData(List<OUT> data) {
        this.data = data;
    }
}
