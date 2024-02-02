import {getDataWithLocalCache} from "@/utils/localCache";
import {MetricSet} from "@/types/insights-web";


export function getMetricPreviewHistory(){
    const cacheData = localStorage.getItem('history_preview_metrics')
    let listData:Array<MetricSet>;
    if(cacheData){
        listData = JSON.parse(cacheData);
    }else{
        listData = [];
    }
    return listData;
}

export function addMetricPreviewHistory(item:MetricSet){
    const cacheData = localStorage.getItem('history_preview_metrics')
    let listData:Array<MetricSet> = null;
    if(cacheData){
        listData = JSON.parse(cacheData);
    }else{
        listData = [];
    }
    const filterList = listData.filter(x => x.id != item.id);
    filterList.unshift(item);
    filterList.slice(0,15);
    localStorage.setItem('history_preview_metrics',JSON.stringify(filterList));
}