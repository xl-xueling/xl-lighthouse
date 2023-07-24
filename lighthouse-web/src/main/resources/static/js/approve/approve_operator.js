/**
 * -----------------------------------------
 *  Author:XueLing.雪灵
 *  Email:better_xueling@126.com
 * -----------------------------------------
 */
var APPROVE_OPERATOR = {
    agreeConfirm:function(orderId,userName,desc){
        MsgBox.Confirm(Strings.format($.i18n.prop('ldp_i18n_order_apply_1019'),userName,desc), function () { APPROVE_OPERATOR.agree(orderId); });
    },
    agree:function(orderId){
        $.ajax({
            type:"POST",
            url:Encrypt.encryptUrl("/approve/agree.shtml?orderId="+orderId),
            beforeSend:function(){
                COMMON.showLoading();
            },
            success:function(data){
                COMMON.hiddenLoading();
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_order_apply_1018'),function () {MsgBox.Redirect("/approve/list.shtml")});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },
    refuseConfirm:function(orderId,userName,desc){
        MsgBox.Confirm(Strings.format($.i18n.prop('ldp_i18n_order_apply_1020'),userName,desc), function () { APPROVE_OPERATOR.refuse(orderId); });
    },
    refuse:function(orderId){
        $.ajax({
            type:"POST",
            url:Encrypt.encryptUrl("/approve/refuse.shtml?orderId="+orderId),
            beforeSend:function(){
                COMMON.showLoading();
            },
            success:function(data){
                COMMON.hiddenLoading();
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_order_apply_1021'),function () {MsgBox.Redirect("/approve/list.shtml")});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },
    searchApprove:function(){
        let state = $("#approve_state").val();
        let search = $("#approve_search").val();
        const params = {
            state: state,
            search: search
        };
        const url = Link.CombineParams("/approve/list.shtml",params);
        MsgBox.Redirect(url);
    },
}