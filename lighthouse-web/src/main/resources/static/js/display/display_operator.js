/**
 * -----------------------------------------
 *  Author:XueLing.雪灵
 *  Email:better_xueling@126.com
 * -----------------------------------------
 */
var $ = jQuery.noConflict();
var DISPLAY_OPERATOR = {
    initStatView:function(statId, startDate, endDate, params){
        let displayType=$("#displayType").val();
        if(typeof displayType === 'undefined'){
            displayType = 'LineChart';
        }
        let btnGroup = $(".btn-group");
        if($(btnGroup).hasClass("open")){
            $(btnGroup).removeClass("open");
        }
        if(displayType === 'LineChart'){
            Render.renderBaseChartOfStat(statId,startDate,endDate,params,'line');
        }else if(displayType === 'MultiLineChart'){
            Render.renderBaseChartOfStat(statId,startDate,endDate,params,'line');
        }else if(displayType === 'BarChart'){
            Render.renderBaseChartOfStat(statId,startDate,endDate,params,'bar');
        }else if(displayType === 'MultiBarChart'){
            Render.renderBaseChartOfStat(statId,startDate,endDate,params,'bar');
        }else if(displayType === 'AreaChart'){
            Render.renderBaseChartOfStat(statId,startDate,endDate,params,'area');
        }else if(displayType === 'MultiAreaChart'){
            Render.renderBaseChartOfStat(statId,startDate,endDate,params,'area');
        }
    },
    initLimitView:function(statId, startDate, endDate,curBatchIndex){
        let obj = $("#"+statId);
        let element = $(obj)[0];
        let chart = echarts.init(element,'shine');
        Render.renderLimitChartWithTimeLine(chart,statId,startDate,endDate,curBatchIndex,'bar');
        let chartElement = echarts.init(element,'shine');
        chartElement.on('timelinechanged', function (obj) {
            chartElement.showLoading();
            Render.renderLimitChartWithTimeLine(chart,statId,startDate,endDate,obj.currentIndex,'bar');
        });
    },
    clearDimensSelected:function(statId,startDate,endDate){
        $(".dimens_a_list:checked").each(function (e) {
            $(this).attr("checked", false);
            $(this).parent().parent("li").css('background-color', 'white');
        });
        $("#filter_dimens").val("");
        DISPLAY_OPERATOR.initStatView(statId,startDate,endDate,null);
    },
    updateDisplayType:function(statId, displayType,desc,isSub){
        let btnGroup = $(".btn-group");
        if($(btnGroup).hasClass("open")){
            $(btnGroup).removeClass("open");
        }
        let obj = {
            id: statId,
            displayType: displayType
        };
        let encryptParams = Encrypt.encryptParams(obj);
        $.ajax({
            type:"POST",
            url:"/stat/change_display.shtml",
            data:encryptParams,
            success:function(data){
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack(Strings.format($.i18n.prop('ldp_i18n_display_1029'),desc),function () {
                        let paramObj = {
                          statId:statId,
                          isSub:isSub === 'true' ? 1 : 0
                        };
                        let url = Link.CombineParams("/display/stat.shtml",paramObj);
                        if(isSub === 'true'){
                            MsgBox.subRedirect("#sub_nav",url);
                        }else{
                            MsgBox.Redirect(url);
                        }
                    });
                }else{
                    MsgBox.Alert($.i18n.prop('ldp_i18n_display_1030'));
                }
            }
        });
    },
    changeLimitType:function(statId,startDate,endDate,limitType,isSub){
        let obj = {
            statId:statId,
            startDate:startDate,
            endDate:endDate,
            isSub:isSub === 'true'? 1 : 0
        };
        let url;
        if(limitType === '1'){
            url = Link.CombineParams("/display/limit.shtml",obj);
        }else{
            url = Link.CombineParams("/display/stat.shtml",obj);
        }
        if(isSub === 'true'){
            MsgBox.subRedirect("#sub_nav",url);
        }else{
            MsgBox.Redirect(url);
        }
    },
    dateSwitch:function (statId, isSub) {
        $("#temp_date1").removeAttr("lay-key");
        $("#temp_date2").removeAttr("lay-key");
        $("#temp_date3").removeAttr("lay-key");
        laydate.render({
            elem: '#temp_date1'
            ,format: 'yyyy-MM-dd'
            ,trigger:'click'
            ,max: new Date().format("yyyy-MM-dd")
            ,done: function(value){
                let tempStartDate = value;
                let tempEndDate = value;
                if(isSub === 'true'){
                    let url = Encrypt.encryptUrl("/display/stat.shtml?statId="+statId+"&startDate="+tempStartDate+"&endDate="+tempEndDate+"&isSub=1");
                    COMMON.loadURL("#sub_nav",url);
                }else{
                    MsgBox.Redirect("/display/stat.shtml?statId="+statId+"&startDate="+tempStartDate+"&endDate="+tempEndDate);
                }
            }
        });
        laydate.render({
            elem: '#temp_date2'
            ,trigger:'click'
            ,max: new Date().format("yyyy-MM-dd")
            ,range: true
            ,done: function(value){
                let tempStartDate = value.split(" - ")[0];
                let tempEndDate = value.split(" - ")[1];
                if(tempStartDate > tempEndDate){
                    let replace = tempStartDate;
                    tempStartDate = tempEndDate;
                    tempEndDate = replace;
                }
                let diffValue = (new Date(tempEndDate) - new Date(tempStartDate)) / (1000 * 60 * 60 * 24);
                if(diffValue > 14){
                    MsgBox.Alert($.i18n.prop('ldp_i18n_display_1031'));
                }else{
                    if(isSub === 'true'){
                        let url = Encrypt.encryptUrl("/display/stat.shtml?statId="+statId+"&startDate="+tempStartDate+"&endDate="+tempEndDate+"&isSub=1");
                        COMMON.loadURL("#sub_nav",url);
                    }else{
                        MsgBox.Redirect("/display/stat.shtml?statId="+statId+"&startDate="+tempStartDate+"&endDate="+tempEndDate);
                    }
                }
            }
        });
        laydate.render({
            elem: '#temp_date3'
            ,trigger:'click'
            ,max: new Date().format("yyyy-MM-dd")
            ,range: true
            ,done: function(value){}
        });
    },
    dateSwitchLimit:function (statId, isSub) {
        $("#temp_date1").removeAttr("lay-key");
        $("#temp_date2").removeAttr("lay-key");
        $("#temp_date3").removeAttr("lay-key");
        laydate.render({
            elem: '#temp_date1'
            ,format: 'yyyy-MM-dd'
            ,trigger:'click'
            ,max: new Date().format("yyyy-MM-dd")
            ,done: function(value){
                let tempStartDate = value;
                let tempEndDate = value;
                if(isSub === 'true'){
                    let url = Encrypt.encryptUrl("/display/limit.shtml?statId="+statId+"&startDate="+tempStartDate+"&endDate="+tempEndDate+"&isSub=1");
                    COMMON.loadURL("#sub_nav",url);
                }else{
                    MsgBox.Redirect("/display/limit.shtml?statId="+statId+"&startDate="+tempStartDate+"&endDate="+tempEndDate);
                }
            }
        });
        laydate.render({
            elem: '#temp_date2'
            ,trigger:'click'
            ,max: new Date().format("yyyy-MM-dd")
            ,range: true
            ,done: function(value){
                let tempStartDate = value.split(" - ")[0];
                let tempEndDate = value.split(" - ")[1];
                if(tempStartDate > tempEndDate){
                    let replace = tempStartDate;
                    tempStartDate = tempEndDate;
                    tempEndDate = replace;
                }
                let diffValue = (new Date(tempEndDate) - new Date(tempStartDate)) / (1000 * 60 * 60 * 24);
                if(diffValue > 14){
                    MsgBox.Alert($.i18n.prop('ldp_i18n_display_1031'));
                }else{
                    if(isSub === 'true'){
                        let url = Encrypt.encryptUrl("/display/limit.shtml?statId="+statId+"&startDate="+tempStartDate+"&endDate="+tempEndDate+"&isSub=1");
                        COMMON.loadURL("#sub_nav",url);
                    }else{
                        MsgBox.Redirect("/display/limit.shtml?statId="+statId+"&startDate="+tempStartDate+"&endDate="+tempEndDate);
                    }
                }
            }
        });
        laydate.render({
            elem: '#temp_date3'
            ,trigger:'click'
            ,max: new Date().format("yyyy-MM-dd")
            ,range: true
            ,done: function(value){}
        });
    },

    showStatLimitedRecord:function (statId) {
        $('#limitedRecordModal').modal('show');
        if(limitedPage === 1) {
            DISPLAY_OPERATOR.loadStatLimitedRecord(statId, false);
        }
    },
    changeDimensParams:function (){
        let paramArr = FILTER_OPERATOR.getFilterParams("filter_default_components");
        if(paramArr === null || paramArr.length === 0){
            MsgBox.Alert($.i18n.prop('ldp_i18n_display_1047'));
        }else{
            DISPLAY_OPERATOR.initStatView(statId, startDate, endDate, JSON.stringify(paramArr));
        }
    },
    changeDisplayType:function (statId,startDate,endDate,displayType){
        let paramArr = FILTER_OPERATOR.getFilterParams("filter_default_components");
        $("#displayType").val(displayType);
        DISPLAY_OPERATOR.initStatView(statId, startDate, endDate, JSON.stringify(paramArr));
    },

    showGroupLimitedRecord:function (groupId) {
        $('#limitedRecordModal').modal('show');
        if(limitedPage === 1){
            DISPLAY_OPERATOR.loadGroupLimitedRecord(groupId,false);
        }
    },

    showFilterConfPage:function(){
        $('#popup_filter_config').modal('show');
    },

    showExportModal:function () {
        $('#exportModal').modal('show');
        $("#data_export_modal_body").html("");
    },
    loadStatLimitedRecord:function (statId,moreFlag) {
        $.ajax({
            type:"POST",
            url:Encrypt.encryptUrl("/limited/stat/list.shtml?statId="+statId+"&page=" + limitedPage),
            success:function(data){
                COMMON.hiddenLoading();
                if(data.code === '0'){
                    let dataSize = data.data.length;
                    if(moreFlag && dataSize === 0){
                        MsgBox.Alert($.i18n.prop('ldp_i18n_display_1032'));
                        return;
                    }
                    let html;
                    for(let i = 0, len = dataSize; i < len; i++) {
                        let limitedObject = data.data[i];
                        html += "<tr>\n" +
                            "                                    <td>"+limitedObject.id+"</td>\n" +
                            "                                    <td>"+limitedObject.strategy+"</td>\n" +
                            "                                    <td>"+DATE.formatUnixtimestamp(limitedObject.startTime)+"</td>\n" +
                            "                                    <td>"+DATE.formatUnixtimestamp(limitedObject.endTime)+"</td>\n" +
                            "                                    <td>15-minutes</td>\n" +
                            "                                </tr>";
                    }
                    $("#limited_tbody").append(html);
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
        limitedPage = limitedPage + 1;
    },

    loadGroupLimitedRecord:function (groupId,moreFlag) {
        $.ajax({
            type:"POST",
            url:Encrypt.encryptUrl("/limited/group/list.shtml?groupId="+groupId+"&page=" + limitedPage),
            success:function(data){
                COMMON.hiddenLoading();
                if(data.code === '0'){
                    let dataSize = data.data.length;
                    if(moreFlag && dataSize === 0){
                        MsgBox.Alert($.i18n.prop('ldp_i18n_display_1032'));
                        return;
                    }
                    let html;
                    for(let i = 0, len = dataSize; i < len; i++) {
                        let limitedObject = data.data[i];
                        html += "<tr>\n" +
                            "                                    <td>"+limitedObject.id+"</td>\n" +
                            "                                    <td>"+limitedObject.strategy+"</td>\n" +
                            "                                    <td>"+DATE.formatUnixtimestamp(limitedObject.startTime)+"</td>\n" +
                            "                                    <td>"+DATE.formatUnixtimestamp(limitedObject.endTime)+"</td>\n" +
                            "                                    <td>15-minutes</td>\n" +
                            "                                </tr>";
                    }
                    $("#limited_tbody").append(html);
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
        limitedPage = limitedPage + 1;
    },
    showUpdateThresholdModal:function(){
        $('#updateGroupThreshold').modal('show');
    },
    updateThreshold:function (groupId) {
        let strategy;
        let select = $("#strategyType").val();
        if(select === '2'){
            strategy = 'STAT_RESULT_SIZE_LIMIT';
        }else{
            strategy = 'GROUP_MESSAGE_SIZE_LIMIT';
        }
        let originValue = $("#originValue").val();
        let updateValue = $("#updateValue").val();
        if(Validate.isNull(updateValue)){
            MsgBox.Alert($.i18n.prop('ldp_i18n_display_1033'));
            return;
        }
        if(!Validate.isNum(updateValue)){
            MsgBox.Alert($.i18n.prop('ldp_i18n_display_1034'));
            return;
        }
        let newValue = parseInt(updateValue);
        if(newValue === parseInt(originValue)){
            MsgBox.Alert($.i18n.prop('ldp_i18n_display_1035'));
        }else if(newValue > 100000){
            MsgBox.Confirm(Strings.format($.i18n.prop('ldp_i18n_display_1036'),newValue),function () { DISPLAY_OPERATOR.updateThresholdSubmit(groupId,strategy,newValue); });
        }else{
            MsgBox.Confirm(Strings.format($.i18n.prop('ldp_i18n_display_1037'),strategy,newValue),function () { DISPLAY_OPERATOR.updateThresholdSubmit(groupId,strategy,newValue); });
        }
    },
    changeStrategy:function (){
        let select = $("#strategyType").val();
        if(select === '2'){
            $("#originValue").val(statResultSizeLimit);
        }else{
            $("#originValue").val(groupMessageSizeLimit);
        }
    },

    updateThresholdSubmit:function (groupId,strategy,newValue) {
        let params = {};
        params.groupId = groupId;
        params.strategy = strategy;
        params.newValue = newValue;
        let formOBJ = {};
        formOBJ.orderType = '4';
        formOBJ.params = params;
        let encryptParams = Encrypt.encryptParams(formOBJ);
        $.ajax({
            type:"POST",
            url:"/order/submit.shtml",
            data:encryptParams,
            beforeSend:function(){
                COMMON.showLoading();
            },
            success:function(data){
                COMMON.hiddenLoading();
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_display_1038'),function () {
                        COMMON.hideAllPop();
                    });
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },
    dataExport:function (){
        let limitFlag=$("#limitFlag").val();
        let url;
        if(!Validate.isNull(limitFlag) && limitFlag === '1'){
            url="/chart/limitExport.shtml";
        }else{
            url="/chart/export.shtml";
        }
        $("#data_export_modal_body").html("");
        let filterObj = $(".filter_export_components");
        let value = $("#temp_date3").val();
        let tempStartDate = value.split(" - ")[0];
        let tempEndDate = value.split(" - ")[1];
        if(tempStartDate > tempEndDate){
            let replace = tempStartDate;
            tempStartDate = tempEndDate;
            tempEndDate = replace;
        }
        let paramMap = FILTER_OPERATOR.getFilterParams("filter_export_components");
        let obj = {};
        obj.dimens = JSON.stringify(paramMap);
        obj.startDate = tempStartDate;
        obj.endDate = tempEndDate;
        obj.statId = statId;
        obj.siteId = typeof siteId === "undefined" ? '-1' : siteId;
        let encryptParams = Encrypt.encryptParams(obj);
        $.ajax({
            type:"POST",
            async: true,
            data:encryptParams,
            beforeSend:function(){
                if(filterObj.find("#export_progress_bar").length === 0){
                    $("#data_export_modal_body").append(
                        "<div id='export_progress_bar' class=\"box-body no-padding\" style=\"margin-top:120px;\">\n" +
                        "                        <div class=\"form-group\">\n" +
                        "                            <div class=\"progress progress-sm active\" style=\"margin: auto; width: 90%;\">\n" +
                        "                                <div class=\"progress-bar progress-bar-primary progress-bar-striped\"\n" +
                        "                                     role=\"progressbar\" aria-valuenow=\"20\" aria-valuemin=\"0\" aria-valuemax=\"100\" style=\"width: 100%\">\n" +
                        "                                    <span class=\"sr-only\">20% Complete</span>\n" +
                        "                                </div>\n" +
                        "                            </div>\n" +
                        "                        </div>\n" +
                        "                    </div>"
                    );
                }
            },
            url:url,
            success:function(data){
                $("#export_progress_bar").remove();
                if(data.code === '0'){
                    $("#data_export_modal_body").append(
                      "<div class=\"box-body no-padding\" style=\"margin-top:80px;text-align:center;\">\n" +
                        "                        <div class=\"form-group\" style=\"font-size:16px;\">\n" +
                        $.i18n.prop('ldp_i18n_display_1039') + "                            <a style=\"text-decoration: underline #587ec1;color: #587ec1;\" target='_blank' href='"+data.link+"'>"+data.fileName+"</a>\n" +
                        "                        </div>\n" +
                        "                    </div>"
                    );
                }else{
                    MsgBox.AlertRequestError(data);
                }

            }
        });
    },

    DisplayProjectTreeClick:function(e, treeId, treeNode) {
        $(".treeIcon").remove();
        if(treeNode.type === 3){
            let url = Encrypt.encryptUrl("/display/stat.shtml?statId="+treeNode.id+"&isSub=1");
            COMMON.loadURL("#sub_nav",url);
        }
    }
};

$('input:radio[name="dateType"]').bind("change",function(){
    let value =$("input[type='radio']:checked").val();
    let date1 = $("#temp_date1");
    let date2 = $("#temp_date2");
    if(value === '1'){
        date1.show();
        date2.hide();
        date1.attr("placeholder","yyyy-MM-dd");
        date2.attr("placeholder"," - ");
    }else if(value === '2'){
        date1.hide();
        date2.show();
        date1.attr("placeholder","yyyy-MM-dd");
        date2.attr("placeholder"," - ");
    }
});


$(".btn-open").click(function(){
    $(".btn-open-ul").each(function () {
        $(this).parent().removeClass("open");
    });
    $(this).parent().addClass("open");
});


$(".content-wrapper").click(function(){
    $(".btn-open-ul").parent().removeClass("open");
});

$(".btn-open").click(function()
{
    return false;
});

