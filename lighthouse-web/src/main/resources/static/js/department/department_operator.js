/**
 * -----------------------------------------
 *  Author:XueLing.雪灵
 *  Email:better_xueling@126.com
 * -----------------------------------------
 */
var IDMark_Switch = "_switch", IDMark_A = "_a"
var DEPARTMENT_OPERATOR = {
    addDepartment:function(){
        const name = $("#name").val();
        if(!name || name === ''){
            MsgBox.Alert($.i18n.prop('ldp_i18n_department_create_1005'));
            return;
        }
        if(Validate.isExistSpecialChar(name)){
            MsgBox.Alert($.i18n.prop('ldp_i18n_department_create_1006'));
            return;
        }
        if(name.length < 3 || name.length > 30){
            MsgBox.Alert($.i18n.prop('ldp_i18n_department_create_1007'));
            return;
        }
        const pid = $("#pid").val();
        let obj = {};
        obj.name = name;
        obj.pid = pid;
        const encryptParams = Encrypt.encryptParams(obj);
        $.ajax({
            type:"POST",
            async: false,
            url:"/department/create/submit.shtml",
            data:encryptParams,
            success:function(data){
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_department_create_1008'),function () {MsgBox.Redirect("/department/manage/index.shtml?pid="+pid)});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },
    updateDepartment:function(){
        const name = $("#name").val();
        const id = $("#id").val();
        const pid = $("#pid").val();
        if(Validate.isNull(id)){
            MsgBox.Alert($.i18n.prop('ldp_i18n_department_update_1006'));
            return;
        }
        if(Validate.isNull(name)){
            MsgBox.Alert($.i18n.prop('ldp_i18n_department_update_1007'));
            return;
        }
        if(name.length < 3 || name.length > 30){
            MsgBox.Alert($.i18n.prop('ldp_i18n_department_update_1008'));
            return;
        }
        let obj = {};
        obj.id = id;
        obj.name = name;
        obj.admins = "-1";
        obj.pid = pid;
        let encryptParams = Encrypt.encryptParams(obj);
        $.ajax({
            type:"POST",
            async: false,
            url:"/department/update/submit.shtml",
            data:encryptParams,
            success:function(data){
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_department_update_1009'),function () {MsgBox.Redirect("/department/manage/index.shtml?pid="+id)});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },

    deleteConfirm:function(id,name){
        if(id === 0){
            MsgBox.Alert($.i18n.prop('ldp_i18n_department_delete_1001'));
            return;
        }
        MsgBox.Confirm(Strings.format($.i18n.prop('ldp_i18n_department_delete_1002'),name), function () { DEPARTMENT_OPERATOR.delete(id); });
    },
    search:function(){
        const search = $("#search_param").val();
        let level = $("#level").val();
        const params = {
            level: level,
            search: search
        };
        const url = Link.CombineParams("/department/list.shtml",params);
        MsgBox.Redirect(url);
    },
    delete:function(id){
        $.ajax({
            type:"POST",
            url:Encrypt.encryptUrl("/department/delete/submit.shtml?id="+id),
            success:function(data){
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_department_delete_1003'),function () {MsgBox.Redirect("/department/manage/index.shtml")});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },

    treeClick:function(e,treeId, treeNode) {
        let addHtml;
        $(".treeIcon").remove();
        if (treeNode.parentNode && treeNode.parentNode.id!==1) return;
        let aObj = $("#" + treeNode.tId + IDMark_A);
        if(treeNode.id === 0 || treeNode.id === '0'){
            addHtml = "<span class='treeIcon' id='diyBtn_add_" + treeNode.id + "' title='"+$.i18n.prop('ldp_i18n_department_manage_1004')+"' onfocus='this.blur();'><span class='button icon_add'></span></span>";
            aObj.append(addHtml);
            let addBtn = $("#diyBtn_add_" + treeNode.id);
            if (addBtn) addBtn.bind("click", function(event){
                let url = "/department/create/index.shtml?pid="+treeNode.id;
                MsgBox.subRedirect(".nav-tabs-custom",url);
                event.stopPropagation();
            });
        }else{
            addHtml = "<span class='treeIcon' id='diyBtn_add_" +treeNode.id+ "' title='"+$.i18n.prop('ldp_i18n_department_manage_1004')+"' onfocus='this.blur();'><span class='button icon_add'></span></span>";
            aObj.append(addHtml);
            let updateHtml = "<span class='treeIcon' id='diyBtn_update_" +treeNode.id+ "' title='"+$.i18n.prop('ldp_i18n_department_manage_1005')+"' onfocus='this.blur();'><span class='button icon_update'></span></span>";
            aObj.append(updateHtml);
            let deleteHtml = "<span class='treeIcon' id='diyBtn_delete_" +treeNode.id+ "' title='"+$.i18n.prop('ldp_i18n_department_manage_1006')+"' onfocus='this.blur();'><span class='button icon_delete'></span></span>";
            aObj.append(deleteHtml);
            let addBtn = $("#diyBtn_add_"+treeNode.id);
            if (addBtn) addBtn.bind("click", function(event){
                let url = "/department/create/index.shtml?pid="+treeNode.id;
                MsgBox.subRedirect(".nav-tabs-custom",url);
                event.stopPropagation();
            });
            const delBtn = $("#diyBtn_delete_" + treeNode.id);
            if (delBtn) delBtn.bind("click", function(){DEPARTMENT_OPERATOR.deleteConfirm(treeNode.id,treeNode.name);});
            const updBtn = $("#diyBtn_update_" + treeNode.id);
            if (updBtn) updBtn.bind("click", function(event){
                let url = "/department/update/index.shtml?id="+treeNode.id;
                MsgBox.subRedirect(".nav-tabs-custom",url);
                event.stopPropagation();
            });
        }
    }
};



