/**
 * -----------------------------------------
 *  Author:XueLing.雪灵
 *  Email:better_xueling@126.com
 * -----------------------------------------
 */
var COMPONENTS_OPERATOR = {

    initList:function () {
        $(".filter_components").each(function() {
            let attrId = $(this).attr("id");
            let formOBJ = {};
            let id = attrId.split('_')[1];
            formOBJ.id = id;
            let encryptParams = Encrypt.encryptParams(formOBJ);
            $.ajax({
                type:"POST",
                url:"/components/data.shtml",
                data:encryptParams,
                success:function(data){
                    if(data.code === '0'){
                        let renderObj = data.data;
                        FILTER_OPERATOR.filterInit("filter_" + id,renderObj);
                    }else{
                        MsgBox.AlertRequestError(data);
                    }
                }
            });
        });
    },

    search:function(){
        const search = $("#components_search").val();
        const params = {
            search: search
        };
        const url = Link.CombineParams("/components/list.shtml",params);
        MsgBox.Redirect(url);
    },
    checkComponentsData:function () {
        let data = $("#components_data").val();
        let componentsType = $("#components_type").val();
        if(Validate.isNull(data)){
            MsgBox.Alert($.i18n.prop('ldp_i18n_components_create_1009'));
            return;
        }
        let formOBJ = {};
        formOBJ.componentsType = componentsType;
        let isJson = Validate.isJson(data);
        if(!isJson){
            MsgBox.Alert($.i18n.prop('ldp_i18n_components_create_1010'));
            return;
        }
        formOBJ.data = JSON.parse(data);
        let encryptParams = Encrypt.encryptParams(formOBJ);
        $.ajax({
            type:"POST",
            url:"/components/check.shtml",
            data:encryptParams,
            success:function(data){
                if(data.code === '0'){
                    $("#components_display").html("");
                    $(".components_display_group").show();
                    let renderObj = data.data;
                    FILTER_OPERATOR.filterInit("components_display",renderObj);
                    MsgBox.Alert($.i18n.prop('ldp_i18n_components_create_1011'));
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },


    registerComponents:function () {
        let title = $("#components_title").val();
        let data = $("#components_data").val();
        let componentsType = $("#components_type").val();
        let privateFlag = $("#privateFlag").is(":checked") === true? 1 : 0;
        if(Validate.isNull(title)){
            MsgBox.Alert($.i18n.prop('ldp_i18n_components_create_1012'));
            return;
        }
        if(Validate.isNull(data)){
            MsgBox.Alert($.i18n.prop('ldp_i18n_components_create_1013'));
            return;
        }
        let formOBJ = {};
        formOBJ.componentsType = componentsType;
        let isJson = Validate.isJson(data);
        if(!isJson){
            MsgBox.Alert($.i18n.prop('ldp_i18n_components_create_1014'));
            return;
        }
        formOBJ.data = JSON.parse(data);
        formOBJ.title = title;
        formOBJ.privateFlag = privateFlag;
        let encryptParams = Encrypt.encryptParams(formOBJ);
        $.ajax({
            type:"POST",
            url:"/components/create/submit.shtml",
            data:encryptParams,
            success:function(data){
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_components_create_1015'),function () {MsgBox.Redirect("/components/list.shtml")});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },
    updateComponents:function () {
        let id = $("#components_id").val();
        let title = $("#components_title").val();
        let data = $("#components_data").val();
        let componentsType = $("#components_type").val();
        let privateFlag = $("#privateFlag").is(":checked") === true? 1 : 0;
        if(Validate.isNull(title)){
            MsgBox.Alert($.i18n.prop('ldp_i18n_components_update_1010'));
            return;
        }
        if(Validate.isNull(data)){
            MsgBox.Alert($.i18n.prop('ldp_i18n_components_update_1011'));
            return;
        }
        let formOBJ = {};
        formOBJ.componentsType = componentsType;
        formOBJ.id = id;
        let isJson = Validate.isJson(data);
        if(!isJson){
            MsgBox.Alert($.i18n.prop('ldp_i18n_components_update_1012'));
            return;
        }
        formOBJ.data = JSON.parse(data);
        formOBJ.title = title;
        formOBJ.privateFlag = privateFlag;
        let encryptParams = Encrypt.encryptParams(formOBJ);
        $.ajax({
            type:"POST",
            url:"/components/update/submit.shtml",
            data:encryptParams,
            success:function(data){
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_components_update_1013'),function () {MsgBox.Redirect("/components/list.shtml")});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },
    privilegeApplyConfirm:function(title,id){
        MsgBox.Confirm(Strings.format($.i18n.prop('ldp_i18n_components_update_1014'),title), function () { COMPONENTS_OPERATOR.deleteComponents(id); });
    },
    deleteComponents:function (id) {
        let formOBJ = {};
        formOBJ.id = id;
        let encryptParams = Encrypt.encryptParams(formOBJ);
        $.ajax({
            type:"POST",
            url:"/components/delete/submit.shtml",
            data:encryptParams,
            success:function(data){
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_components_update_1015'),function () {MsgBox.Redirect("/components/list.shtml")});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    }
};