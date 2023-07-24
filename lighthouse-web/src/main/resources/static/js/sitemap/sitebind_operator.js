/**
 * -----------------------------------------
 *  Author:XueLing.雪灵
 *  Email:better_xueling@126.com
 * -----------------------------------------
 */
var SITEBIND_OPERATOR = {
    offlineConfirm:function (siteId,bindId,name){
        MsgBox.Confirm(Strings.format($.i18n.prop('ldp_i18n_sitebind_update_1008'),name), function () { SITEBIND_OPERATOR.offline(siteId,bindId); });
    },

    offline:function(siteId,bindId){
        let obj = {};
        obj.id = bindId;
        obj.state = 0;
        var encryptParams = Encrypt.encryptParams(obj);
        $.ajax({
            type:"POST",
            async: false,
            url:"/sitebind/state/submit.shtml",
            data:encryptParams,
            success:function(data){
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_sitebind_update_1009'),function () {MsgBox.Redirect("/sitemap/manage/index.shtml?siteId="+siteId)});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },

    onlineConfirm:function (siteId,bindId,name){
        MsgBox.Confirm(Strings.format($.i18n.prop('ldp_i18n_sitebind_update_1010'),name), function () { SITEBIND_OPERATOR.online(siteId,bindId); });
    },

    online:function(siteId,bindId){
        let obj = {};
        obj.id = bindId;
        obj.state = 1;
        let encryptParams = Encrypt.encryptParams(obj);
        $.ajax({
            type:"POST",
            async: false,
            url:"/sitebind/state/submit.shtml",
            data:encryptParams,
            success:function(data){
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_sitebind_update_1011'),function () {MsgBox.Redirect("/sitemap/manage/index.shtml?siteId="+siteId)});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },

    showUpdateSiteBindModal:function (siteId,bindId){
        $("#bind_siteId").val(siteId);
        $("#bind_id").val(bindId);
        $('#siteBindUpdateModal').modal('show');
        let obj = {};
        obj.siteId = siteId;
        obj.bindId = bindId;
        obj.format = 1;
        let encryptParams = Encrypt.encryptParams(obj);
        $.ajax({
            type:"POST",
            async: false,
            url:"/sitebind/detail.shtml",
            data:encryptParams,
            success:function(data){
                if(data.code === '0'){
                    let result = data.data;
                    $("#bind_name").val(result.name);
                    $("#element_name").val(result.elementName);
                    $("#element_link").attr({"href":result.elementLink,"target":"_blank"});
                    let html = "";
                    let siteNodes = JSON.parse(result.siteConfig);
                    for(let i = 0, len = siteNodes.length; i < len; i++){
                        let nodeId = siteNodes[i].id;
                        if(parseInt(nodeId) === parseInt(result.nodeId)){
                            html += "<option selected value=\""+siteNodes[i].id+"\">"+siteNodes[i].fullPathName+"</option>"
                        }else{
                            html += "<option value=\""+siteNodes[i].id+"\">"+siteNodes[i].fullPathName+"</option>"
                        }
                    }
                    $("#sitebind_path").html(html);
                }
            }
        });
    },

    updateSubmitConfirm:function (){
        let bindId = $("#bind_id").val();
        let nodeId = $("#sitebind_path").val();
        let name = $("#bind_name").val();
        if(nodeId === "-1"){
            MsgBox.Alert($.i18n.prop('ldp_i18n_sitebind_update_1012'));
            return;
        }
        if(Validate.isNull(name)){
            MsgBox.Alert($.i18n.prop('ldp_i18n_sitebind_update_1013'));
            return;
        }
        if(name.length < 4 || name.length > 20){
            MsgBox.Alert($.i18n.prop('ldp_i18n_sitebind_update_1014'));
            return;
        }
        let siteId = $("#bind_siteId").val();
        MsgBox.Confirm($.i18n.prop('ldp_i18n_sitebind_update_1015'), function () { SITEBIND_OPERATOR.updateSubmit(siteId,bindId,nodeId,name); });
    },

    updateSubmit:function (siteId,bindId,nodeId,name){
        let obj = {};
        obj.siteId = siteId;
        obj.bindId = bindId;
        obj.nodeId = nodeId;
        obj.name = name;
        let encryptParams = Encrypt.encryptParams(obj);
        $.ajax({
            type:"POST",
            async: false,
            url:"/sitebind/update/submit.shtml",
            data:encryptParams,
            success:function(data){
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_sitebind_update_1016'),function () {MsgBox.Redirect("/sitemap/manage/index.shtml?siteId="+siteId)});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },

    removeConfirm:function (siteId,bindId,name){
        MsgBox.Confirm(Strings.format($.i18n.prop('ldp_i18n_sitebind_update_1017'),name), function () { SITEBIND_OPERATOR.remove(siteId,bindId); });
    },

    remove:function(siteId,bindId){
        let obj = {};
        obj.id = bindId;
        let encryptParams = Encrypt.encryptParams(obj);
        $.ajax({
            type:"POST",
            async: false,
            url:"/sitebind/remove/submit.shtml",
            data:encryptParams,
            success:function(data){
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_sitebind_update_1018'),function () {MsgBox.Redirect("/sitemap/manage/index.shtml?siteId="+siteId)});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },
}