import {
    convertDateToTimestamp,
    DateFormat,
    getDailyEndTimestamp,
    getDailyStartTimestamp,
    getDayBefore,
    getDayEndTimestamp,
    getDayStartTimestamp
} from "@/utils/date";
import {requestLimitData, requestStatData} from "@/api/data";
import {LimitData, LineChartData, Stat, StatData} from "@/types/insights-web";
import {ResultData} from "@/types/insights-common";

export const handlerFetchStatData = async (statInfo:Stat,search:any):Promise<ResultData<Array<StatData>>> => {
    const combineParam:any = {}
    combineParam.statId = statInfo?.id;
    if(search != null){
        const date = search.date;
        if(date && Array.isArray(date)){
            combineParam.startTime = getDayStartTimestamp(convertDateToTimestamp(date[0],DateFormat));
            combineParam.endTime = getDayEndTimestamp(convertDateToTimestamp(date[1],DateFormat));
        }
        const dimensParams = {};
        for (const [key, value] of Object.entries(search)) {
            if(key == 'date' || key == 't'){
                continue;
            }
            dimensParams[key] = value;
        }
        combineParam.dimensParams = dimensParams;
    }else{
        const timeParam = statInfo?.timeparam;
        if(timeParam.endsWith('minute') || timeParam.endsWith('second') || timeParam.endsWith('hour')){
            combineParam.startTime = getDailyStartTimestamp();
            combineParam.endTime = getDailyEndTimestamp();
        }else if(timeParam.endsWith('day')){
            combineParam.startTime = getDayBefore(getDailyStartTimestamp(),13);
            combineParam.endTime = getDailyEndTimestamp();
        }else if(timeParam.endsWith('month')){
            combineParam.startTime = getDayBefore(getDailyStartTimestamp(),365);
            combineParam.endTime = getDailyEndTimestamp();
        }
    }
    return await requestStatData(combineParam);
}

export const handlerFetchLimitData = async ():Promise<ResultData<Array<LimitData>>> => {
    const statId = 1100522;
    const combineParam = {
        statId:statId,
    }
    return await requestLimitData(combineParam);
}

export const translateResponseDataToLineChartData = (statData:Array<StatData>,stateIndex:number):LineChartData => {
    if(!statData){
        return null;
    }
    const dataMap = new Map();
    statData?.forEach(z => {
        let values
        if(stateIndex >= 0){
            values = z.valuesList.map(z => z.statesValue[stateIndex]);
        }else{
            values = z.valuesList.map(z => z.value);
        }
        dataMap.set(z.dimensValue,values);
    })
    const batchList = statData[0].valuesList.map(z => z.displayBatchTime);
    return {
        xAxis: batchList,
        dataMap: dataMap,
    };
}

export const getTimeLineBarOption = (data:Array<LimitData>,errorMessage:string) => {
    console.log("----data is:" + data);
    if(!data){
        return {};
    }
    const batchList = data.map(z => z.batchTime);
    console.log("batchList is:" + JSON.stringify(batchList));
    const option = {
        baseOption: {
            timeline: {
                axisType: 'category',
                autoPlay: false,
                playInterval: 1000,
                data: batchList,
                currentIndex : 1,
                label: {
                    formatter: '{value}'
                }
            },
            xAxis: {
                type: 'category',
                data: ['Category1', 'Category2', 'Category3']
            },
            yAxis: {
                type: 'value'
            },
            series: [
                {
                    name: 'Series',
                    type: 'bar',
                    data: [100, 200, 300]
                }
            ]
        },
        options: [
            {
                series: [
                    {
                        name: 'Series',
                        type: 'bar',
                        data: [100, 200, 300]
                    }
                ]
            },
            {
                series: [
                    {
                        name: 'Series',
                        type: 'bar',
                        data: [150, 250, 350]
                    }
                ]
            },
            {
                series: [
                    {
                        name: 'Series',
                        type: 'bar',
                        data: [200, 300, 400]
                    }
                ]
            }
        ]
    };
    return option;
}


export const getLineOption = (lineChartData:LineChartData,errorMessage:string) => {
    if(!lineChartData){
        return {};
    }
    const data = Array.from(lineChartData.dataMap).map(([key,value]) => {
        return {
            name:key,
            type:'line',
            data:value,
            animation: true,
            animationEasing: 'quadraticInOut',
            animationDurationUpdate:30,
        }
    })
    return {
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'line',
                label: {
                    backgroundColor: '#6a7985'
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
        legend: {
            data: lineChartData.dataMap.keys(),
            icon:'circle',
            itemHeight:'10',
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: [
            {
                type: 'category',
                boundaryGap: false,
                data: errorMessage ? [] : lineChartData.xAxis,
                axisLabel: {
                    animation: false
                }
            }
        ],
        yAxis: [
            {
                type: 'value',
                axisLabel: {
                    animation: false
                },
            }
        ],

        series: errorMessage ? [] : data,
        graphic: errorMessage && [{
            type: 'text',
            left: 'center',
            top: 'middle',
            style: {
                fill: '#000',
                text: errorMessage,
                fontSize: 12,
            }
        }]
    };
}