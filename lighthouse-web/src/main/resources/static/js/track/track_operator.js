/**
 * -----------------------------------------
 *  Author:XueLing.雪灵
 *  Email:better_xueling@126.com
 * -----------------------------------------
 */
var TRACK_OPERATOR = {
    trackConfirm(statId,state){
        if(state === true){
            MsgBox.Confirm($.i18n.prop('ldp_i18n_track_1004'),function (){TRACK_OPERATOR.startDebugMode(statId)});
        }else{
            MsgBox.Confirm($.i18n.prop('ldp_i18n_track_1005'),function (){TRACK_OPERATOR.stopDebugMode(statId)});
        }
    },

    trackInit(statId,debugMode,debugParams){
        if(debugMode === '1'){
            let paramsObj = JSON.parse(debugParams);
            let startTime = paramsObj.startTime;
            let endTime = paramsObj.endTime;
            TRACK_OPERATOR.record("Current Statistic("+title+")" + ",Debug Mode: ON!");
            TRACK_OPERATOR.record('The debug mode period is: ' + DATE.formatUnixtimestamp(startTime) + " - " + DATE.formatUnixtimestamp(endTime));
            TRACK_OPERATOR.loadTrackRecords(statId,startTime,endTime);
            $("#debugStartTime").val(startTime);
            $("#debugEndTime").val(endTime);
            $("[name='track-switch']").bootstrapSwitch('state', true, true);
            $("#track_record").show();
        }else{
            TRACK_OPERATOR.record("Current Statistic("+title+")" + ",Debug Mode: OFF!");
            $("[name='track-switch']").bootstrapSwitch('state', false,true);
            $("#track_record").hide();
        }
    },

    startDebugMode(statId){
        let obj = {};
        obj.statId = statId;
        obj.state = '1';
        let encryptParams = Encrypt.encryptParams(obj);
        $.ajax({
            type:"POST",
            url:"/track/stateSubmit.shtml",
            data:encryptParams,
            beforeSend :function(xmlHttp){
                COMMON.showLoading();
                xmlHttp.setRequestHeader("If-Modified-Since","0");
                xmlHttp.setRequestHeader("Cache-Control","no-cache");
            },
            success:function(data){
                COMMON.hiddenLoading();
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_track_1008'),function () {MsgBox.Redirect("/track/stat.shtml?statId="+statId)});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },

    stopDebugMode(statId){
        window.clearInterval(refreshId);
        let obj = {};
        obj.statId = statId;
        obj.state = '0';
        let encryptParams = Encrypt.encryptParams(obj);
        $.ajax({
            type:"POST",
            url:"/track/stateSubmit.shtml",
            data:encryptParams,
            beforeSend :function(xmlHttp){
                COMMON.showLoading();
                xmlHttp.setRequestHeader("If-Modified-Since","0");
                xmlHttp.setRequestHeader("Cache-Control","no-cache");
            },
            success:function(data){
                COMMON.hiddenLoading();
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_track_1009'),function () {MsgBox.Redirect("/track/stat.shtml?statId="+statId)});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },

    record(message){
        $("#track_shell").append(DATE.formatUnixtimestamp(new Date()) + "&nbsp;&nbsp;" + message + "<br>");
    },

    loadTrackRecords(statId,startTime,endTime){
        $("#track_record").show();
        let obj = {};
        obj.statId = statId;
        obj.startTime = startTime;
        obj.endTime = endTime;
        let encryptParams = Encrypt.encryptParams(obj);
        $.ajax({
            type:"POST",
            url:"/track/records.shtml",
            data:encryptParams,
            beforeSend :function(xmlHttp){
                xmlHttp.setRequestHeader("If-Modified-Since","0");
                xmlHttp.setRequestHeader("Cache-Control","no-cache");
            },
            success:function(result){
                if(result.code === '0'){
                    TRACK_OPERATOR.appendHTML(result.columns,result.data);
                }
            }
        });
    },

    appendHTML(columns,data){
        let element = $("#track_record_table");
        if(columns === undefined || data === undefined || data.length === '0'){
            element.html("<span style=\"width: 100%;text-align: center;float:right;\">"+$.i18n.prop('ldp_i18n_track_1007')+"</span>");
            return;
        }
        element.html("");
        let columnSize = columns.length;
        let tr = "";
        tr += "<tr>";
        tr += "<th style='width: 50px;'>No.</th>"
        for(let j=0;j < columnSize;j++){
            let columnName = columns[j];
            if(columnName === 'detail'){
                tr += "<th style='max-width: 40%;min-width: 10%;'>" + columnName + "</th>";
            }else if(columnName === 'repeat'){
                tr += "<th style='width: 20px;'>" + columnName + "</th>";
            }else if(columnName === 'param_check'){
                tr += "<th style='width: 50px;'>" + columnName + "</th>";
            }else if(columnName === 'batch_time'){
                tr += "<th style='width: 150px;'>" + columnName + "</th>";
            }else if(columnName === 'system_time'){
                tr += "<th style='width: 150px;'>" + columnName + "</th>";
            }else if(columnName === 'result'){
                tr += "<th style='width: 50px;'>" + columnName + "</th>";
            }else{
                tr += "<th>" + columnName + "</th>";
            }
        }
        tr += "</tr>";
        element.append(tr);
        let resultSize = data.length;
        for(let i = 0;i < resultSize; i++) {
            let obj = data[i];
            let tr = "";
            if(obj['result'] === "Invalid"){
                tr += "<tr style='background: #e9d5d5;'>";
            }else{
                tr += "<tr>";
            }
            tr += "<td>" + i + "</td>";
            for(let j=0;j < columnSize;j++){
                let column = columns[j];
                tr += "<td>"+obj[column]+"</td>";
            }
            tr += "</tr>";
            element.append(tr);
        }
    },

    startAutoRefresh(){
        window.clearInterval(refreshId);
        refreshId = setInterval(function (){
            let debugStartTime = $("#debugStartTime").val();
            let debugEndTime = $("#debugEndTime").val();
            if(!Validate.isNull(debugStartTime)){
                TRACK_OPERATOR.loadTrackRecords(statId,debugStartTime,debugEndTime);
            }
        }, 5000)
    }

};


