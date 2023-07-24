/**
 * -----------------------------------------
 *  Author:XueLing.雪灵
 *  Email:better_xueling@126.com
 * -----------------------------------------
 */
var APPLY_OPERATOR = {
    searchApply:function(){
        let state = $("#apply_state").val();
        const params = {
            state: state
        };
        const url = Link.CombineParams("/apply/list.shtml",params);
        MsgBox.Redirect(url);
    },
    agreeConfirm:function(applyId,userName,desc){
        MsgBox.Confirm(Strings.format($.i18n.prop('ldp_i18n_privilege_apply_1026'),userName,desc), function () { APPLY_OPERATOR.agree(applyId); });
    },
    agree:function(applyId){
        $.ajax({
            type:"POST",
            url:Encrypt.encryptUrl("/privilege/approve/agree.shtml?applyId="+applyId),
            success:function(data){
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_privilege_apply_1027'),function () {MsgBox.Redirect("/privilege/approve/list.shtml")});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },
    refuseConfirm:function(applyId,userName,desc){
        MsgBox.Confirm(Strings.format($.i18n.prop('ldp_i18n_privilege_apply_1028'),userName,desc), function () { APPLY_OPERATOR.refuse(applyId); });
    },
    refuse:function(applyId){
        $.ajax({
            type:"POST",
            url:Encrypt.encryptUrl("/privilege/approve/refuse.shtml?applyId="+applyId),
            success:function(data){
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_privilege_apply_1029'),function () {MsgBox.Redirect("/privilege/approve/list.shtml")});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },

    privilegeApplyConfirm:function(relationId,orderType,desc,redirectURL){
        MsgBox.Confirm(Strings.format($.i18n.prop('ldp_i18n_privilege_apply_1030'),desc), function () { APPLY_OPERATOR.privilegeApply(relationId,orderType,redirectURL); });
    },

    privilegeApply:function(relationId,orderType,redirectURL){
        let params = {}
        if(orderType === '1'){
            params.projectId = relationId;
        }else if(orderType === '2'){
            params.statId = relationId;
        }else if(orderType === '3'){
            params.siteId = relationId;
        }else{
            MsgBox.Alert("System Error!")
            return;
        }
        let formOBJ = {};
        formOBJ.orderType = orderType;
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
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_privilege_apply_1033'),function () {MsgBox.Redirect(redirectURL)});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },


    retractApplyConfirm:function(applyId,desc,redirectURL){
        MsgBox.Confirm(Strings.format($.i18n.prop('ldp_i18n_order_apply_1016'),desc), function () { APPLY_OPERATOR.retract(applyId,redirectURL); });
    },

    retract:function(orderId,redirectURL){
        $.ajax({
            type:"POST",
            url:Encrypt.encryptUrl("/apply/retract.shtml?id="+orderId),
            beforeSend:function(){
                COMMON.showLoading();
            },
            success:function(data){
                COMMON.hiddenLoading();
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_order_apply_1017'),function () {MsgBox.Redirect(redirectURL)});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },
};

