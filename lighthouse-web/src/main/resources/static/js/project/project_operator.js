/**
 * -----------------------------------------
 *  Author:XueLing.雪灵
 *  Email:better_xueling@126.com
 * -----------------------------------------
 */
var IDMark_Switch = "_switch", IDMark_A = "_a"
var PROJECT_OPERATOR = {
    createProject:function(){
        let name = $("#name").val();
        if(Validate.isNull(name)){
            MsgBox.Alert($.i18n.prop('ldp_i18n_project_create_1011'));
            return;
        }
        if(Validate.isExistSpecialChar(name)){
            MsgBox.Alert($.i18n.prop('ldp_i18n_project_create_1012'));
            return;
        }
        if(name.length < 4){
            MsgBox.Alert($.i18n.prop('ldp_i18n_project_create_1014'));
            return;
        }
        if(name.length > 40){
            MsgBox.Alert($.i18n.prop('ldp_i18n_project_create_1015'));
            return;
        }
        let departmentId = $("#departmentId").val();
        if(Validate.isNull(departmentId) || departmentId === '-1'){
            MsgBox.Alert($.i18n.prop('ldp_i18n_project_create_1016'));
            return;
        }
        let admins = $("#admins").val();
        if(!admins){
            MsgBox.Alert($.i18n.prop('ldp_i18n_project_create_1017'));
            return;
        }
        if(admins.length < 1){
            MsgBox.Alert($.i18n.prop('ldp_i18n_project_create_1017'));
            return;
        }

        if(admins.length > 3){
            MsgBox.Alert($.i18n.prop('ldp_i18n_project_create_1018'));
            return;
        }

        let desc = $("#desc").val();
        if(Validate.isNull(desc)){
            MsgBox.Alert($.i18n.prop('ldp_i18n_project_create_1019'));
            return;
        }
        if(desc.length < 5){
            MsgBox.Alert($.i18n.prop('ldp_i18n_project_create_1020'));
            return;
        }
        if(desc.length > 50){
            MsgBox.Alert($.i18n.prop('ldp_i18n_project_create_1021'));
            return;
        }
        let privateFlag = $("#privateFlag").is(":checked") === true? 1 : 0;
        desc = desc.trim();
        name = name.trim();
        let obj = {};
        obj.name = name;
        obj.desc = desc;
        obj.departmentId = departmentId;
        obj.admins = admins;
        obj.privateFlag = privateFlag;
        let encryptParams = Encrypt.encryptParams(obj);
        $.ajax({
            type:"POST",
            url:"/project/create/submit.shtml",
            data:encryptParams,
            beforeSend:function(){
                COMMON.showLoading();
            },
            success:function(data){
                COMMON.hiddenLoading();
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_project_create_1022'),function(){MsgBox.Redirect("/project/list.shtml")});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },
    search:function(){
        const search = $("#search_param").val();
        const departmentId = $("#department_id").val();
        const owner = $("#owner").is(":checked")? 1 : 0;
        const params = {
            departmentId: departmentId,
            owner: owner,
            search: search
        };
        const url = Link.CombineParams("/project/list.shtml",params);
        MsgBox.Redirect(url);
    },
    updateProject:function(){
        var name = $("#name").val();
        if(Validate.isNull(name)){
            MsgBox.Alert($.i18n.prop('ldp_i18n_project_update_1011'));
            return;
        }
        if(Validate.isExistSpecialChar(name)){
            MsgBox.Alert($.i18n.prop('ldp_i18n_project_update_1012'));
            return;
        }
        if(name.length < 4){
            MsgBox.Alert($.i18n.prop('ldp_i18n_project_update_1014'));
            return;
        }
        if(name.length > 40){
            MsgBox.Alert($.i18n.prop('ldp_i18n_project_update_1015'));
            return;
        }
        var departmentId = $("#departmentId").val();
        if(Validate.isNull(departmentId) || departmentId === '-1'){
            MsgBox.Alert($.i18n.prop('ldp_i18n_project_update_1016'));
            return;
        }
        var desc = $("#desc").val();
        if(Validate.isNull(desc)){
            MsgBox.Alert($.i18n.prop('ldp_i18n_project_update_1017'));
            return;
        }
        if(desc.length < 5){
            MsgBox.Alert($.i18n.prop('ldp_i18n_project_update_1018'));
            return;
        }
        if(desc.length > 50){
            MsgBox.Alert($.i18n.prop('ldp_i18n_project_update_1019'));
            return;
        }
        let admins = $("#admins").val();
        if(!admins){
            MsgBox.Alert($.i18n.prop('ldp_i18n_project_update_1020'));
            return;
        }

        if(admins.length < 1){
            MsgBox.Alert($.i18n.prop('ldp_i18n_project_update_1020'));
            return;
        }
        if(admins.length > 3){
            MsgBox.Alert($.i18n.prop('ldp_i18n_project_update_1021'));
            return;
        }
        let privateFlag = $("#privateFlag").is(":checked") === true? 1 : 0;
        let id = $("#id").val();
        let obj = {};
        obj.name = name.trim();
        obj.desc = desc.trim();
        obj.id = id;
        obj.departmentId = departmentId;
        obj.admins = admins;
        obj.privateFlag = privateFlag;
        var encryptParams = Encrypt.encryptParams(obj);
        $.ajax({
            type:"POST",
            url:"/project/update/submit.shtml",
            data:encryptParams,
            beforeSend:function(){
                COMMON.showLoading();
            },
            success:function(data){
                COMMON.hiddenLoading();
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_project_update_1022'),function(){MsgBox.Redirect("/project/list.shtml")});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },
    projectView:function(projectId){
        $.ajax({
            type:"POST",
            url:Encrypt.encryptUrl("/project/hasItem.shtml?projectId="+projectId),
            beforeSend:function(){
                COMMON.showLoading();
            },
            success:function(data){
                COMMON.hiddenLoading();
                if(data.code === '0'){
                    MsgBox.Open("/display/project.shtml?projectId="+projectId)
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },
    deleteConfirm:function(id,name){
        MsgBox.Confirm(Strings.format($.i18n.prop('ldp_i18n_project_list_1015'),name), function () { PROJECT_OPERATOR.delete(id); });
    },
    delete:function(id){
        $.ajax({
            type:"POST",
            async: false,
            url:Encrypt.encryptUrl("/project/delete/submit.shtml?id="+id),
            success:function(data){
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_project_list_1016'),function () {MsgBox.Redirect("/project/list.shtml")});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },
    treeClick:function(e,treeId, treeNode) {
        $(".treeIcon").remove();
        if (treeNode.parentNode && treeNode.parentNode.id!==1) return;
        let aObj = $("#" + treeNode.tId + IDMark_A);
        let type = treeNode.type;
        if(type === 1){
            let addHtml = "<span class='treeIcon i18n' id='diyBtn_add_" +treeNode.id+ "' title='"+$.i18n.prop('ldp_i18n_project_manage_1003')+"' onfocus='this.blur();'><span class='button icon_add'></span></span>";
            aObj.append(addHtml);
            let addBtn = $("#diyBtn_add_"+treeNode.id);
            if (addBtn) addBtn.bind("click", function(event){
                let url = "/group/create/index.shtml?projectId="+treeNode.id;
                MsgBox.subRedirect("#sub_nav",url);
                event.stopPropagation();
            });
        }else if(type === 2){
            let url = "/group/manage/index.shtml?id="+treeNode.id;
            MsgBox.subRedirect("#sub_nav",url);
            let updateHtml = "<span class='treeIcon i18n' id='diyBtn_update_" +treeNode.id+ "' title='"+$.i18n.prop('ldp_i18n_project_manage_1004')+"' onfocus='this.blur();'><span class='button icon_update'></span></span>";
            aObj.append(updateHtml);
            let btn1 = $("#diyBtn_update_"+treeNode.id);
            if (btn1) btn1.bind("click", function(event){
                var url = "/group/update/index.shtml?id="+treeNode.id;
                MsgBox.subRedirect("#sub_nav",url);
                event.stopPropagation();}
            );
            let deleteHtml = "<span class='treeIcon i18n' id='diyBtn_delete_" +treeNode.id+ "' title='"+$.i18n.prop('ldp_i18n_project_manage_1005')+"' onfocus='this.blur();'><span class='button icon_delete'></span></span>";
            aObj.append(deleteHtml);
            let btn2 = $("#diyBtn_delete_"+treeNode.id);
            if (btn2) btn2.bind("click", function(event){
                GROUP_OPERATOR.deleteGroupConfirm(treeNode.name,treeNode.id,treeNode.pId);
                event.stopPropagation();
            })
        }
    }
}

$("#owner").on('click',function(){
    var search = $("#search_param").val();
    var departmentId = $("#department_id").val();
    var owner = $("#owner").is(":checked") === true? 1 : 0;
    if(Validate.isNull(search)){
        MsgBox.Redirect("/project/list.shtml?owner="+owner+"&departmentId="+departmentId);
    }else{
        MsgBox.Redirect("/project/list.shtml?search="+search+"&owner="+owner+"&departmentId="+departmentId);
    }
});

