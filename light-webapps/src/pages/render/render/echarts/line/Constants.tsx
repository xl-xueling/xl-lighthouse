import React from 'react';
import {RenderValue} from "@/types/insights-web";

export const getOptions = (renderValues:Array<RenderValue>) => {
    if(!renderValues){
        return ;
    }

    const getDimensList = () => {
        const dimensList = renderValues.map(z => z.category);
        const uniqueDimensList = [...new Set(dimensList)];
        return uniqueDimensList.length == 1 && uniqueDimensList[0] == null ? [] : uniqueDimensList;
    }

    const getBatchList = () => {
        const displayBatchTimeArray = renderValues.map(z => z.displayBatchTime);
        const uniqueArray = [...new Set(displayBatchTimeArray)];
        return uniqueArray.sort((a, b) => a - b);
    }

    const getSeries = () => {
        interface CategoryData {
            [key: string]: RenderValue[];
        }
        const groupByCategory = ():CategoryData => {
            return renderValues.reduce((acc:CategoryData, cur) => {
                if (!acc[cur.category]) {
                    acc[cur.category] = [];
                }
                acc[cur.category].push({ batchTime: cur.batchTime,displayBatchTime:cur.displayBatchTime, value: cur.value });
                return acc;
            }, {});
        };

        const categoryData = groupByCategory();
        const seriesArray = new Array<any>();
        for (const category in categoryData) {
            if (categoryData.hasOwnProperty(category)) {
                const dimensValue = category;
                const items = categoryData[category];
                const values = items.sort((a,b) => a.displayBatchTime - b.displayBatchTime).map(z => z.value);
                const seriesObj =  {
                    name: !dimensValue || dimensValue === 'undefined' ? "Value" : dimensValue,
                    type: 'line',
                    data: values,
                    label: {
                        show: false,
                    },
                    symbolSize: 3,
                    animation: true,
                    animationEasing: 'quadraticInOut',
                    animationDurationUpdate:1000,
                };
                seriesArray.push(seriesObj);
            }
        }
        return seriesArray;
    }

    const dimensList = getDimensList();
    return {
        tooltip: {
            trigger: 'axis',
            formatter: function (params) {
                if(!params){
                    return;
                }
                const newParams = [];
                const paramData = params.sort(function (a, b) {
                    return b.value - a.value;
                });
                for (let i = 0, len = paramData.length; i < len; i++) {
                    const v = paramData[i];
                    const dimens = v.seriesName.startsWith('series')?"value":v.seriesName;
                    const s = v.marker + ' ' + dimens + ' : ' + v.value;
                    newParams.push(s)
                }
                return params[0].axisValue + "<br>" + newParams.join('<br>');
            },
            confine: true
        },
        dataZoom: [
            {
                type: 'inside',
                start: 0,
                end: 100
            }
        ],
        legend: {
            data: dimensList,
            icon:'circle',
            itemHeight:'10',
        },
        grid: {
            top: dimensList.length> 0 ? '40px':'25px',
            left: '10px',
            right: '10px',
            bottom: '0px',
            containLabel: true
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        xAxis: [
            {
                type: 'category',
                boundaryGap: false,
                data: getBatchList(),
                axisLabel: {
                    animation: true
                }
            }
        ],
        yAxis: [
            {
                type: 'value',
                axisLabel: {
                    animation: true,
                    formatter: function (value, index) {
                        if (value >= 1000 && value < 1000000) {
                            value = value / 1000 + "K";
                        } else if (value >= 1000000 && value < 1000000000) {
                            value = value / 1000000 + "M";
                        } else if (value >= 1000000000 && value < 1000000000000) {
                            value = value / 1000000000 + "B";
                        } else if (value >= 1000000000000) {
                            value = value / 1000000000000 + "T";
                        }
                        return value;
                    }
                },
            }
        ],
        series: getSeries(),
    };
}