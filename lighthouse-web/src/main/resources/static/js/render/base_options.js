/**
 * -----------------------------------------
 *  Author:XueLing.雪灵
 *  Email:better_xueling@126.com
 * -----------------------------------------
 */
var BaseOptions = {
    getStyleStateSeries:function(name,data,stateIndex,smooth,symbolSize,style){
        let seriesArray=[];
        for (let i = 0; i < data.length; i++) {
            if(stateIndex === '-1'){
                seriesArray.push(data[i]);
            }else {
                let obj = data[i];
                let value = data[i].statesValue[stateIndex];
                if(value === undefined){
                    obj.value= '0.0';
                }else{
                    obj.value= value;
                }
                seriesArray.push(obj);
            }
        }
        let seriesValue = {};
        seriesValue.itemStyle = {
            normal: {
                lineStyle: {
                    width: 1.8
                }
            }
        };
        seriesValue.emphasis = {
            lineStyle: {
                width: 1.8,
            },
        };
        seriesValue.showAllSymbol = false;
        seriesValue.smooth = 0.2;
        if(style === 'line'){
            seriesValue.name = name;
            seriesValue.data = seriesArray;
            seriesValue.type = 'line';
            seriesValue.symbolSize = symbolSize;
        }else if(style === 'bar'){
            seriesValue.name = name;
            seriesValue.data = seriesArray;
            seriesValue.type = 'bar';
            seriesValue.symbolSize = symbolSize;
        }else if(style === 'area'){
            seriesValue.name = name;
            seriesValue.data = seriesArray;
            seriesValue.type = 'line';
            seriesValue.smooth = smooth;
            seriesValue.symbolSize = symbolSize;
            seriesValue.areaStyle = '{}';
        }
        return seriesValue;
    },

    getMultiBaseOption:function(data,stateIndex,style){
        let seriesArray = [];
        let dataMap = data.dataMap;
        let dimensMapperInfo = data.dimensMapperInfo;
        let batchList = data.batchList;
        let dimensArray = [];
        let xAxisFormat = data.xAxisFormat;
        for(let key in dataMap){
            let mapperKey = key;
            if(dimensMapperInfo != null && !Validate.isNull(dimensMapperInfo[key])){
                mapperKey = dimensMapperInfo[key];
            }
            let seriesValue = BaseOptions.getStyleStateSeries(mapperKey,dataMap[key],stateIndex,'true','1',style);
            dimensArray.push(mapperKey);
            seriesArray.push(seriesValue);
        }
        let option = {
            legend: {
                icon: 'roundRect',
                data: dimensArray,
                top: '5px',
                right: '0px',
                orient: 'vertical',
                itemHeight: 10,
                itemWidth: 15
            },
            xAxis: {
                type: 'category',
                axisTick: {
                    show: true
                },
                axisLabel: {
                    formatter: function (value) {
                        return new Date(Number(value.toString())).format(xAxisFormat);
                    }
                },
                data: batchList
            },
            tooltip: {
                trigger: 'axis',
                formatter: function (params) {
                    let newParams = [];
                    let paramData = params.sort(function (a, b) {
                        return b.value - a.value;
                    });
                    for (let i = 0, len = paramData.length; i < len; i++) {
                        let v = paramData[i];
                        let s = v.marker + ' ' + v.seriesName + ' : ' + v.value.toString().replace(/(\d{1,3})(?=(\d{3})+(?:$|\.))/g, '$1,');
                        newParams.push(s)
                    }
                    return new Date(Number(params[0].name)).format("yyyy-MM-dd hh:mm:ss") + "<br>" + newParams.join('<br>');
                },
                textStyle: {
                    color: '#fff',
                    fontSize: 14,
                    fontWeight: 'normal'
                },
                confine: true
            },
            yAxis: {
                type: 'value',
                axisTick: {
                    show: true
                },
                axisLabel: {
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
                }
            },
            dataZoom: [
                {
                    type: 'inside',
                    start: 0,
                    end: 100
                }
            ],
            grid: {
                left: '50px;',
                right: '5px;',
                bottom: '25px;',
                top: '10px;'
            },
            series: seriesArray
        };
        if(seriesArray.length === 0){
            option.graphic = [
                {
                    type: "text",
                    left: "center",
                    top: "center",
                    style: {
                        text: "No data available!"
                    }
                }
            ];
        }
        return option;
    },

    getLimitChartWithTimeLineOption:function(data, style){
        let batchList = data.batchList;
        let valueList = data.valueList;
        let currentIndex = data.timeLine.currentIndex;
        let timeLineDataArray = data.timeLine.timeList;
        let curDate = timeLineDataArray[currentIndex];
        let xAxisFormat = data.xAxisFormat;
        let xAxisDataArray = [];
        let seriesObj = {};
        seriesObj.type = 'bar';
        let dataArray = [];
        let dimensArray = [];
        for(let i = 0, len = valueList.length; i < len; i++){
            dimensArray.push(valueList[i].dimens);
            dataArray.push(valueList[i]);
        }
        let intervals = Math.ceil(timeLineDataArray.length / 8);
        seriesObj.data = dataArray;
        let option = {
            baseOption: {
                timeline: {
                    axisType: 'category',
                    show: true,
                    autoPlay: false,
                    left: '100px',
                    right:'100px',
                    bottom:'25px',
                    top:'385px',
                    currentIndex:currentIndex,
                    controlStyle:{
                        showPlayBtn:false,
                        itemSize:12
                    },
                    symbolSize: 5,
                    lineStyle: {
                        width: 2,
                    },
                    data: timeLineDataArray,
                    tooltip: {
                        formatter: []
                    },
                    label: {
                        interval: intervals,
                        color: '#1781bf'
                    },
                },
                grid: {
                    containLabel: true,
                    left: '50px;',
                    right: '5px;'
                },
                xAxis: [{
                    type: 'category'}
                ],
                yAxis: {
                    type: 'value',
                    axisLabel: {
                        formatter: function (value, index) {
                            if (value >= 1000 && value < 1000000) {
                                value = value / 1000 + "K";
                            }else if (value >= 1000000 && value < 1000000000) {
                                value = value / 1000000 + "M";
                            } else if (value >= 1000000000 && value < 1000000000000) {
                                value = value / 1000000000 + "B";
                            } else if (value >= 1000000000000) {
                                value = value / 1000000000000 + "T";
                            }
                            return value;
                        }
                    }
                },
                series: [
                    {
                        type: 'line',
                    },
                ],

            },
            options:[]
        };

        let subOption = {
            legend: {
                data: dimensArray,
                top:'5px',
                right:'0px',
                orient:'vertical'
            },
            xAxis: [{
                type: 'category',
                axisTick: {
                    show: true
                },
                data: dimensArray
            }],
            yAxis: {
                type: 'value',
                axisTick: {
                    show: true
                }
            },
            tooltip:{
                trigger: 'axis',
                formatter:function(params){
                    if(params == null || Validate.isNull(params[0])){
                        return ;
                    }
                    let newParams = [];
                    let paramData = params;
                    for(let i = 0, len = paramData.length; i < len; i++){
                        let v = paramData[i];
                        let s =  v.name + ' : ' + v.value.toString().replace(/(\d{1,3})(?=(\d{3})+(?:$|\.))/g,'$1,');
                        newParams.push(s)
                    }
                    return curDate+"<br>"+newParams.join('<br>');
                },
                textStyle : {
                    color: '#fff',
                    fontSize: 14,
                    fontWeight: 'normal'
                },
                confine:true
            },
            dataZoom: [
                {
                    type: 'inside',
                    start: 0,
                    end: 100
                }
            ],
            grid: {
                left: '50px;',
                right: '5px;',
                bottom: '45px;',
                top: '10px;'
            },
            series: [seriesObj]
        };
        for(let i = 0, len = timeLineDataArray.length; i < len; i++){
            option.options[i] = subOption;
        }
        return option;
    }
};