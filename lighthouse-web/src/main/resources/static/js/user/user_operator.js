/**
 * -----------------------------------------
 *  Author:XueLing.雪灵
 *  Email:better_xueling@126.com
 * -----------------------------------------
 */
var $ = jQuery.noConflict();
var USER_OPERATOR = {
    login:function(){
        let userName = $("#userName").val();
        let password = $("#password").val();
        if(Validate.isNull(userName) || Validate.isNull(password)){
            MsgBox.Alert($.i18n.prop('ldp_i18n_user_login_1008'));
            return;
        }
        if(!$("#agreement").is(':checked')){
            MsgBox.Alert($.i18n.prop('ldp_i18n_user_login_1009'));
            return;
        }
        let obj = {};
        obj.userName = userName.trim();
        obj.password = hex_md5(password.trim());
        let encryptParams = Encrypt.encryptParams(obj);
        $.ajax({
            type:"POST",
            url:"/login/submit.shtml",
            data:encryptParams,
            beforeSend :function(xmlHttp){
                COMMON.showLoading();
                xmlHttp.setRequestHeader("If-Modified-Since","0");
                xmlHttp.setRequestHeader("Cache-Control","no-cache");
            },
            success:function(data){
                COMMON.hiddenLoading();
                if(data.code === '0'){
                    MsgBox.Redirect("/index.shtml")
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },
    register:function(){
        let userName = $("#userName").val();
        let email = $("#email").val();
        let phone = $("#phone").val();
        let password = $("#password").val();
        let departmentId = $("#department_id").val();
        if(Validate.isNull(userName) || Validate.isNull(email) || Validate.isNull(password) || Validate.isNull(departmentId) || departmentId === '-1'){
            MsgBox.Alert($.i18n.prop('ldp_i18n_user_register_1008'));
            return;
        }
        if(!Validate.isNumLetterOrLine(userName) || userName.length < 4 || userName.length > 20){
            MsgBox.Alert($.i18n.prop('ldp_i18n_user_register_1009'));
            return;
        }
        if(password.length < 4 || password.length > 20){
            MsgBox.Alert($.i18n.prop('ldp_i18n_user_register_1010'));
            return;
        }
        // if(!Validate.isPhone(phone) || phone.length !== 11){
        //     MsgBox.Alert($.i18n.prop('ldp_i18n_user_register_1011'));
        //     return;
        // }
        if(!Validate.isNull(phone) && Validate.isExistSpecialChar(phone)){
            MsgBox.Alert($.i18n.prop('ldp_i18n_user_register_1011'));
            return;
        }
        if(!Validate.isNull(phone) && phone.length < 4){
            MsgBox.Alert($.i18n.prop('ldp_i18n_user_register_1011'));
            return;
        }
        if(!Validate.isNull(phone) && phone.length > 20){
            MsgBox.Alert($.i18n.prop('ldp_i18n_user_register_1011'));
            return;
        }
        if(!Validate.isEmail(email)){
            MsgBox.Alert($.i18n.prop('ldp_i18n_user_register_1012'));
            return;
        }
        if(Validate.isNull(departmentId) || departmentId === '-1'){
            MsgBox.Alert($.i18n.prop('ldp_i18n_user_register_1013'));
            return;
        }
        let obj = {};
        obj.userName = userName.toLowerCase();
        obj.email = email;
        obj.phone = phone;
        obj.departmentId = departmentId;
        obj.password = hex_md5(password);
        let encryptParams = Encrypt.encryptParams(obj);
        $.ajax({
            type:"POST",
            url:"/user/register/submit.shtml",
            data:encryptParams,
            beforeSend :function(xmlHttp){
                COMMON.showLoading();
                xmlHttp.setRequestHeader("If-Modified-Since","0");
                xmlHttp.setRequestHeader("Cache-Control","no-cache");
            },
            success:function(data){
                COMMON.hiddenLoading();
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_user_register_1014'),function () {MsgBox.Redirect("/login/index.shtml")});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },
    deleteConfirm:function(id,name){
        if(name === "admin"){
            MsgBox.Alert($.i18n.prop('ldp_i18n_user_list_1025'));
        }else{
            MsgBox.Confirm(Strings.format($.i18n.prop('ldp_i18n_user_list_1026'),name), function () { USER_OPERATOR.delete(id); });
        }
    },
    delete:function(id){
        $.ajax({
            type:"POST",
            async: false,
            url:Encrypt.encryptUrl("/user/delete/submit.shtml?id="+id),
            beforeSend :function(xmlHttp){
                COMMON.showLoading();
                xmlHttp.setRequestHeader("If-Modified-Since","0");
                xmlHttp.setRequestHeader("Cache-Control","no-cache");
            },
            success:function(data){
                COMMON.hiddenLoading();
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_user_list_1027'),function () {MsgBox.Redirect("/user/list.shtml")});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },
    resetPasswordConfirm:function(id,name){
        MsgBox.Confirm(Strings.format($.i18n.prop('ldp_i18n_user_list_1021'),name), function () { USER_OPERATOR.resetPassword(id); });
    },
    resetPassword:function(id){
        $.ajax({
            type:"POST",
            async: false,
            url:Encrypt.encryptUrl("/user/resetPwd/submit.shtml?id="+id),
            beforeSend :function(xmlHttp){
                COMMON.showLoading();
                xmlHttp.setRequestHeader("If-Modified-Since","0");
                xmlHttp.setRequestHeader("Cache-Control","no-cache");
            },
            success:function(data){
                COMMON.hiddenLoading();
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_user_list_1022'),function () {MsgBox.Redirect("/user/list.shtml")});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },
    freezeUserConfirm:function(id,name){
        if(name === "admin"){
            MsgBox.Alert($.i18n.prop('ldp_i18n_user_list_1016'));
        }else{
            MsgBox.Confirm(Strings.format($.i18n.prop('ldp_i18n_user_list_1017'),name), function () { USER_OPERATOR.freezeUser(id); });
        }
    },
    freezeUser:function(id){
        $.ajax({
            type:"POST",
            async: false,
            url:Encrypt.encryptUrl("/user/freeze/submit.shtml?id="+id),
            beforeSend :function(xmlHttp){
                COMMON.showLoading();
                xmlHttp.setRequestHeader("If-Modified-Since","0");
                xmlHttp.setRequestHeader("Cache-Control","no-cache");
            },
            success:function(data){
                COMMON.hiddenLoading();
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_user_list_1018'),function () {MsgBox.Redirect("/user/list.shtml")});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },
    activateUserConfirm:function(id,name){
        MsgBox.Confirm(Strings.format($.i18n.prop('ldp_i18n_user_list_1019'),name), function () { USER_OPERATOR.activateUser(id); });
    },
    activateUser:function(id){
        $.ajax({
            type:"POST",
            async: false,
            url:Encrypt.encryptUrl("/user/activate/submit.shtml?id="+id),
            beforeSend :function(xmlHttp){
                COMMON.showLoading();
                xmlHttp.setRequestHeader("If-Modified-Since","0");
                xmlHttp.setRequestHeader("Cache-Control","no-cache");
            },
            success:function(data){
                COMMON.hiddenLoading();
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_user_list_1020'),function () {MsgBox.Redirect("/user/list.shtml")});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },
    refuseUserConfirm:function(id,name){
        MsgBox.Confirm(Strings.format($.i18n.prop('ldp_i18n_user_list_1023'),name), function () { USER_OPERATOR.refuseUser(id); });
    },
    refuseUser:function(id){
        $.ajax({
            type:"POST",
            async: false,
            url:Encrypt.encryptUrl("/user/delete/submit.shtml?id="+id),
            beforeSend :function(xmlHttp){
                COMMON.showLoading();
                xmlHttp.setRequestHeader("If-Modified-Since","0");
                xmlHttp.setRequestHeader("Cache-Control","no-cache");
            },
            success:function(data){
                COMMON.hiddenLoading();
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_user_list_1024'),function () {MsgBox.Redirect("/user/list.shtml")});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },
    searchUser:function(){
        const departmentId = $("#department_id").val();
        const search = $("#user_search").val();
        const state = $("#state").val();
        const params = {
            departmentId: departmentId,
            state: state,
            search: search
        };
        const url = Link.CombineParams("/user/list.shtml",params);
        MsgBox.Redirect(url);
    },
    updateUser:function(){
        const userName = $("#userName").val();
        const phone = $("#phone").val();
        const email = $("#email").val();
        const id = $("#id").val();
        const departmentId = $("#department_id").val();
        if(Validate.isNull(userName) || Validate.isNull(email)){
            MsgBox.Alert($.i18n.prop('ldp_i18n_user_update_1008'));
            return;
        }
        if(!Validate.isNumOrLetter(userName) || userName.length < 4 || userName.length > 20){
            MsgBox.Alert($.i18n.prop('ldp_i18n_user_update_1009'));
            return;
        }
        // if(!Validate.isPhone(phone) || phone.length !== 11){
        //     MsgBox.Alert($.i18n.prop('ldp_i18n_user_update_1010'));
        //     return;
        // }
        if(!Validate.isNull(phone) && Validate.isExistSpecialChar(phone)){
            MsgBox.Alert($.i18n.prop('ldp_i18n_user_register_1011'));
            return;
        }
        if(!Validate.isNull(phone) && phone.length < 4){
            MsgBox.Alert($.i18n.prop('ldp_i18n_user_register_1011'));
            return;
        }
        if(!Validate.isNull(phone) && phone.length > 20){
            MsgBox.Alert($.i18n.prop('ldp_i18n_user_register_1011'));
            return;
        }
        if(!Validate.isEmail(email)){
            MsgBox.Alert($.i18n.prop('ldp_i18n_user_update_1011'));
            return;
        }
        if(userName !== 'admin' && (Validate.isNull(departmentId) || departmentId === '-1')){
            MsgBox.Alert($.i18n.prop('ldp_i18n_user_update_1012'));
            return;
        }
        let obj = {};
        obj.userName = userName;
        obj.email = email;
        obj.id = id;
        obj.departmentId = departmentId;
        obj.phone = phone;
        let encryptParams = Encrypt.encryptParams(obj);
        $.ajax({
            type:"POST",
            url:"/user/update/submit.shtml",
            data:encryptParams,
            beforeSend :function(xmlHttp){
                xmlHttp.setRequestHeader("If-Modified-Since","0");
                xmlHttp.setRequestHeader("Cache-Control","no-cache");
            },
            success:function(data){
                COMMON.hiddenLoading();
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_user_update_1013'),function(){MsgBox.Redirect("/index.shtml")});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },
    changePassword:function(){
        let password = $("#password").val();
        let new_password = $("#new_password").val();
        let repeat_password = $("#repeat_password").val();
        let id = $("#id").val().trim();
        if(Validate.isNull(password) || Validate.isNull(new_password)){
            MsgBox.Alert($.i18n.prop('ldp_i18n_change_password_1006'));
            return;
        }
        if(password === new_password){
            MsgBox.Alert($.i18n.prop('ldp_i18n_change_password_1007'));
            return;
        }

        if(new_password.length < 4 || new_password.length > 20){
            MsgBox.Alert($.i18n.prop('ldp_i18n_change_password_1008'));
            return;
        }

        if(new_password !== repeat_password){
            MsgBox.Alert($.i18n.prop('ldp_i18n_change_password_1009'));
            return;
        }
        let obj = {};
        obj.password = hex_md5(password);;
        obj.new_password = hex_md5(new_password);
        obj.id = id;
        let encryptParams = Encrypt.encryptParams(obj);
        $.ajax({
            type:"POST",
            url:"/user/changepwd/submit.shtml",
            data:encryptParams,
            beforeSend :function(xmlHttp){
                COMMON.showLoading();
                xmlHttp.setRequestHeader("If-Modified-Since","0");
                xmlHttp.setRequestHeader("Cache-Control","no-cache");
            },
            success:function(data){
                COMMON.hiddenLoading();
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_change_password_1010'),function(){MsgBox.Redirect("/login/index.shtml")});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    }
};



