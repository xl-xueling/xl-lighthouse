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
import {LimitData, LineChartData, RenderValue, Stat, StatData} from "@/types/insights-web";
import {ResultData} from "@/types/insights-common";

export const handlerFetchStatData = async (statInfo:Stat,search:any):Promise<ResultData<Array<StatData>>> => {
    const combineParam:any = {}
    combineParam.statId = statInfo?.id;
    if(search == null || search.date == null || !Array.isArray(search.date)){
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
    }else {
        const date = search.date;
        combineParam.startTime = getDayStartTimestamp(convertDateToTimestamp(date[0],DateFormat));
        combineParam.endTime = getDayEndTimestamp(convertDateToTimestamp(date[1],DateFormat));
    }
    if(search != null){
        const dimensParams = {};
        for (const [key, value] of Object.entries(search)) {
            if(key == 'date' || key == 't'){
                continue;
            }
            dimensParams[key] = value;
        }
        combineParam.dimensParams = dimensParams;
    }
    return await requestStatData(combineParam);
}

export const translateStatDataToSequenceData = (statData:Array<StatData>,functionIndex:number = -1):Array<RenderValue> => {
    if(!statData){
        return ;
    }
    const renderValues:Array<RenderValue> = [];
    for(let i=0;i<statData.length;i++){
        const displayDimensValue = statData[i].displayDimensValue;
        const dimensData = statData[i].valuesList;
        const batchList = statData[i].valuesList.map(z => z.batchTime);
        const displayBatchList = statData[i].valuesList.map(z => z.displayBatchTime);
        let values;
        if(functionIndex == -1){
            values = dimensData.map(z => z.value)
        }else{
            values = dimensData.map(z => z.statesValue[functionIndex]);
        }
        for(let n=0;n<values.length;n++){
            const renderValue:RenderValue = {};
            renderValue.value = values[n];
            renderValue.batchTime = batchList[n];
            renderValue.displayBatchTime = displayBatchList[n];
            renderValue.category = displayDimensValue;
            renderValues.push(renderValue);
        }
    }
    return renderValues;
}


export const handlerFetchLimitData = async (statId):Promise<ResultData<Array<LimitData>>> => {
    const combineParam = {
        statId:statId,
    }
    return await requestLimitData(combineParam);
}

const getBasicLineSeries = (chartData) => {
    const seriesArray = new Array<any>();
    for(let i=0;i<chartData.length;i++){
        const dimensValue = chartData[i].dimensValue;
        const values = chartData[i].valuesList.map(z => z.value);
        const seriesObj =  {
            name: dimensValue,
            type: 'line',
            data: values,
            animation: true,
            animationEasing: 'quadraticInOut',
            animationDurationUpdate:3000,
        };
        seriesArray.push(seriesObj);
    }
    return seriesArray;
}

export const getStatBasicLineOption = (statData:Array<StatData>,stateIndex:number,errorMessage:string):any => {
    if(!statData){
        return {};
    }
    let dimensList = statData.map(z => z.displayDimensValue);
    dimensList = dimensList.length == 1 && dimensList[0] == null ? [] : dimensList
    const batchList = statData[0].valuesList.map(z => z.displayBatchTime);
    const seriesArray = getBasicLineSeries(statData);
    return  {
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
            data: dimensList,
            icon:'circle',
            itemHeight:'10',
        },
        grid: {
            top:'25px',
            left: '25px',
            right: '25px',
            bottom: '10px',
            containLabel: true
        },
        xAxis: [
            {
                type: 'category',
                boundaryGap: false,
                data: batchList,
                axisLabel: {
                    animation: true
                }
            }
        ],
        yAxis: [
            {
                type: 'value',
                axisLabel: {
                    animation: true
                },
            }
        ],
        series: seriesArray,
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