/**
 * -----------------------------------------
 *  Author:XueLing.雪灵
 *  Email:better_xueling@126.com
 * -----------------------------------------
 */
var AUTHORIZE_OPERATE = {

    authorize:function(){
        let licenceCode = $("#licence_code").val();
        if(Validate.isNull(licenceCode)){
            MsgBox.Alert($.i18n.prop('ldp_i18n_authorize_1010'));
            return;
        }
        let obj = {};
        licenceCode = btoa(licenceCode);
        obj.licenceCode = licenceCode;
        const encryptParams = Encrypt.encryptParams(obj);
        $.ajax({
            type:"POST",
            url:"/authorize/submit.shtml",
            data:encryptParams,
            beforeSend :function(xmlHttp){
                COMMON.showLoading();
                xmlHttp.setRequestHeader("If-Modified-Since","0");
                xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
            },
            success:function(data){
                COMMON.hiddenLoading();
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_authorize_1011'),function () {MsgBox.Redirect("/authorize/index.shtml")});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    }
}