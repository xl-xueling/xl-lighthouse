/**
 * -----------------------------------------
 *  Author:XueLing.雪灵
 *  Email:better_xueling@126.com
 * -----------------------------------------
 */
var Render = {
    renderBaseChartOfStat:function(statId, startDate, endDate, dimens, style){
        let chartWrapperArray=[];
        let groupName = 'ldp-stat-chart-'+statId;
        $(".ldp-stat-chart").each(function (){
            let obj = $(this);
            let attrStatId = $(obj).attr("statId");
            if(attrStatId === statId){
                let element = $(obj)[0];
                let echartAttr = $(obj).attr('_echarts_instance_');
                if (typeof echartAttr !== typeof undefined && echartAttr !== false) {
                    $(obj).removeAttr("_echarts_instance_");
                }
                let chart = echarts.init(element,'shine');
                chart.group = groupName;
                chart.showLoading();
                let chartWrapper={};
                chartWrapper.stateIndex = $(this).attr("stateIndex");
                chartWrapper.chart = chart;
                chartWrapperArray.push(chartWrapper);
            }
        });
        echarts.connect(groupName);
        $("#match_stat_div").hide();
        $("#match_stat_id").hide();
        let obj = {
            siteId:typeof siteId === 'undefined' ? '-1' : siteId,
            statId: statId,
            startDate: startDate,
            endDate: endDate,
            dimens: dimens
        };
        let encryptParams = Encrypt.encryptParams(obj);
        $.ajax({
            type:"POST",
            url:"/data/stat.shtml",
            data:encryptParams,
            success:function(data){
                for(let i = 0;i<chartWrapperArray.length;i++){
                    let chartWrapper = chartWrapperArray[i];
                    let stateIndex = chartWrapper.stateIndex;
                    let chart = chartWrapper.chart;
                    chart.clear();
                    chart.hideLoading();
                    if(data.code === '0'){
                        let option = BaseOptions.getMultiBaseOption(data.data,stateIndex,style);
                        chart.setOption(option,true);
                        let template = data.template;
                        if(!Validate.isNull(template)){
                            $("#match_stat_div").show();
                            $("#match_stat_div .text-muted").html(template);
                            $("#match_stat_id").show();
                            $("#match_stat_id td").html(data.relationId);
                        }
                    }else{
                        chart.showLoading({
                            text: I18N.translate(data.msg),
                            effect : 'whirling',
                            color: '#fff',
                            textColor: '#000',
                            x: 'center',
                            y: 'center',
                            fontSize: 14,
                            maskColor: 'rgba(255, 255, 255, 0.2)',
                            textStyle: {
                                textColor: '#000',
                                opacity:1
                            }
                        });
                    }
                }
            }
        });
    },
    renderLimitChartWithTimeLine:function(chart, statId, startDate, endDate, currentIndex, style){
        chart.showLoading();
        let obj = {
            siteId:typeof siteId === "undefined" ? '-1' : siteId,
            statId:statId,
            startDate:startDate,
            endDate:endDate,
            currentIndex:currentIndex
        };
        var encryptParams = Encrypt.encryptParams(obj);
        $.ajax({
            type:"POST",
            url:"/data/limit.shtml",
            data:encryptParams,
            success:function(data){
                chart.clear();
                chart.hideLoading();
                if(data.code === '0'){
                    let option = BaseOptions.getLimitChartWithTimeLineOption(data.data,style);
                    chart.setOption(option,true);
                }else{
                    chart.showLoading({
                        text: data.msg,
                        effect : 'whirling',
                        color: '#fff',
                        textColor: '#000',
                        x: 'center',
                        y: 'center',
                        fontSize: 14,
                        maskColor: 'rgba(255, 255, 255, 0.2)',
                        textStyle: {
                            textColor: '#000',
                            opacity:1
                        }
                    });
                }
            }
        });
    },
};