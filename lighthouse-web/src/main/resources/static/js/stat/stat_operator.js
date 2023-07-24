/**
 * -----------------------------------------
 *  Author:XueLing.雪灵
 *  Email:better_xueling@126.com
 * -----------------------------------------
 */
var STAT_OPERATOR = {
    disableStatConfirm:function(statId,title,groupId){
        MsgBox.Confirm(Strings.format($.i18n.prop('ldp_i18n_group_manage_1030'),title),function(){STAT_OPERATOR.disableStat(statId,groupId)});
    },
    disableStat:function(statId,groupId){
        $.ajax({
            type:"POST",
            url:Encrypt.encryptUrl("/stat/disable.shtml?id="+statId),
            success:function(data){
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_group_manage_1031'),function(){
                        let url = "/group/manage/index.shtml?id="+groupId;
                        MsgBox.subRedirect("#sub_nav",url);
                    });
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },
    enableStatConfirm:function(statId,title,groupId){
        MsgBox.Confirm(Strings.format($.i18n.prop('ldp_i18n_group_manage_1032'),title),function(){STAT_OPERATOR.enableStat(statId,groupId)});
    },
    enableStat:function(statId,groupId){
        $.ajax({
            type:"POST",
            url:Encrypt.encryptUrl("/stat/enable.shtml?id="+statId),
            success:function(data){
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_group_manage_1033'),function(){
                        let url = "/group/manage/index.shtml?id="+groupId;
                        MsgBox.subRedirect("#sub_nav",url);
                    });
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },
    showUpdateModal:function (statId){
        let obj = {};
        obj.id = statId;
        let encryptParams = Encrypt.encryptParams(obj);
        $.ajax({
            type:"POST",
            async: false,
            url:"/stat/detail.shtml",
            data:encryptParams,
            success:function(result){
                if(result.code === '0'){
                    let data = result.data;
                    $("#stat_id").val(data.id);
                    $("#group_id").val(data.groupId);
                    $("#project_id").val(data.projectId);
                    $("#stat_title").val(data.title);
                    $("#stat_template").val(data.template);
                    $("#stat_timeparam").val(data.timeParam).trigger("change");
                    if(data.dataExpire === '0'){
                        $("#stat_expire").val("259200").trigger("change","init");
                    }else{
                        $("#stat_expire").val(data.dataExpire.toString()).trigger("change","init");
                    }
                    $("#statUpdateModal").modal('show');
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },

    updateConfirm:function (){
        let statId = $("#stat_id").val();
        let groupId = $("#group_id").val();
        let projectId = $("#project_id").val();
        let title = $("#stat_title").val();
        let statExpire = $("#stat_expire").val();
        let statTemplate = $("#stat_template").val();
        if(Validate.isNull(title)){
            MsgBox.Alert($.i18n.prop('ldp_i18n_group_manage_1028'));
            return;
        }
        if(title.length < 4 || title.length > 20){
            MsgBox.Alert($.i18n.prop('ldp_i18n_group_manage_1029'));
            return;
        }
        let obj = {};
        obj.statId = statId;
        obj.groupId = groupId;
        obj.projectId = projectId;
        obj.title = title;
        obj.statExpire = statExpire;
        obj.template = statTemplate;
        obj.projectId = projectId;
        MsgBox.Confirm(Strings.format($.i18n.prop('ldp_i18n_group_manage_1026'),statId), function () { STAT_OPERATOR.updateSubmit(obj); });
    },

    updateSubmit:function (obj){
        let projectId = obj.projectId;
        let groupId = obj.groupId;
        let encryptParams = Encrypt.encryptParams(obj);
        $.ajax({
            type:"POST",
            async: false,
            url:"/stat/update/submit.shtml",
            data:encryptParams,
            success:function(data){
                COMMON.hiddenLoading();
                if(data.code === '0'){
                    let url = "/group/update/index.shtml?id="+groupId;
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_group_manage_1027'),function(){MsgBox.subRedirect("#sub_nav",url);});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },

    frozenStatConfirm:function(statId,title,groupId){
        MsgBox.Confirm(Strings.format($.i18n.prop('ldp_i18n_group_manage_1022'),title),function(){STAT_OPERATOR.frozenStat(statId,groupId)})
    },
    frozenStat:function(statId,groupId){
        $.ajax({
            type:"POST",
            url:Encrypt.encryptUrl("/stat/frozen.shtml?id="+statId),
            success:function(data){
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_group_manage_1023'),function(){
                        let url = "/group/manage/index.shtml?id="+groupId;
                        MsgBox.subRedirect("#sub_nav",url);
                    });
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },
    deleteStatConfirm:function(statId,title,groupId){
        MsgBox.Confirm(Strings.format($.i18n.prop('ldp_i18n_group_manage_1024'),title),function(){STAT_OPERATOR.deleteStat(statId,groupId)});
    },
    deleteStat:function(statId,groupId){
        $.ajax({
            type:"POST",
            url:Encrypt.encryptUrl("/stat/delete.shtml?id="+statId),
            success:function(data){
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_group_manage_1025'),function(){
                        let url = "/group/manage/index.shtml?isSub=1&id="+groupId;
                        MsgBox.subRedirect("#sub_nav",url);
                    });
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },
    search:function(){
        let projectId = $("#project_id").val();
        let departmentId = $("#department_id").val();
        let search = $("#search_param").val();
        let params = {
            projectId: projectId,
            departmentId: departmentId,
            search: search
        };
        let url = Link.CombineParams("/stat/list.shtml",params);
        MsgBox.Redirect(url);
    }
};


