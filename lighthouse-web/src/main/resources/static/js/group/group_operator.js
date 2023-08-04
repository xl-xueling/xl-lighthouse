/**
 * -----------------------------------------
 *  Author:XueLing.雪灵
 *  Email:better_xueling@126.com
 * -----------------------------------------
 */
var GROUP_OPERATOR = {
    addRTTemplate:function(){
        let randomStr = COMMON.getRandomString(8);
        let html = "<tr>\n" +
            "                    <td onclick=\"\" style=\"\">\n" +
            "                     <input type=\"hidden\" class=\"template_id\" value=\"0\"/>"+
            "                     <pre id=\"stat_template_"+randomStr+"\" class=\"stat_template_new\" style=\"height: 60px;\"></pre>" +
            "                    </td>\n" +
            "<td style=\" vertical-align: middle;\">\n" +
            "                      <select autocomplete=\"off\" class=\"stat_time_param\" style=\"width: 80px;\" tabindex=\"-1\" aria-hidden=\"true\">\n" +
            "                        <option value=\"1-minute\" selected>"+$.i18n.prop('ldp_i18n_group_create_1010')+"</option>\n" +
            "                        <option value=\"2-minute\">"+$.i18n.prop('ldp_i18n_group_create_1011')+"</option>\n" +
            "                        <option value=\"3-minute\">"+$.i18n.prop('ldp_i18n_group_create_1012')+"</option>\n" +
            "                        <option value=\"5-minute\">"+$.i18n.prop('ldp_i18n_group_create_1013')+"</option>\n" +
            "                        <option value=\"10-minute\">"+$.i18n.prop('ldp_i18n_group_create_1014')+"</option>\n" +
            "                        <option value=\"20-minute\">"+$.i18n.prop('ldp_i18n_group_create_1015')+"</option>\n" +
            "                        <option value=\"30-minute\">"+$.i18n.prop('ldp_i18n_group_create_1016')+"</option>\n" +
            "                        <option value=\"1-hour\" selected>"+$.i18n.prop('ldp_i18n_group_create_1017')+"</option>\n" +
            "                        <option value=\"2-hour\">"+$.i18n.prop('ldp_i18n_group_create_1018')+"</option>\n" +
            "                        <option value=\"3-hour\">"+$.i18n.prop('ldp_i18n_group_create_1019')+"</option>\n" +
            "                        <option value=\"1-day\">"+$.i18n.prop('ldp_i18n_group_create_1020')+"</option>\n" +
            "                      </select>\n" +
            "                    </td>" +
            "                    <td style=\" vertical-align: middle;\">\n" +
            "                      <select autocomplete=\"off\" class=\"stat_data_expire\" style=\"width: 100px;\" tabindex=\"-1\" aria-hidden=\"true\">\n" +
            "                        <option value=\"259200\">"+$.i18n.prop('ldp_i18n_group_create_1021')+"</option>\n" +
            "                        <option value=\"604800\">"+$.i18n.prop('ldp_i18n_group_create_1022')+"</option>\n" +
            "                        <option value=\"1209600\" selected>"+$.i18n.prop('ldp_i18n_group_create_1023')+"</option>\n" +
            "                        <option value=\"2592000\">"+$.i18n.prop('ldp_i18n_group_create_1024')+"</option>\n" +
            "                        <option value=\"7776000\">"+$.i18n.prop('ldp_i18n_group_create_1025')+"</option>\n" +
            "                        <option value=\"15552000\">"+$.i18n.prop('ldp_i18n_group_create_1026')+"</option>\n" +
            "                        <option value=\"31104000\">"+$.i18n.prop('ldp_i18n_group_create_1027')+"</option>\n" +
            "                        <option value=\"62208000\">"+$.i18n.prop('ldp_i18n_group_create_1028')+"</option>\n" +
            "                      </select>\n" +
            "                    </td>\n" +
            "                    <td style=\" vertical-align: middle; text-align: center; \" onclick='GROUP_OPERATOR.deleteTemplate(this.parentNode)'>\n" +
            "                      <a href=\"javascript:void(0);\" style=\"color:#000000;\"><span class=\"col-md-1 col-sm-2\" title=\"\"><i class=\"fa fa-fw fa-minus-circle\"></i></span></a>\n" +
            "                    </td>\n" +
            "                  </tr>";
        $("#rt_item-content").append(html);
        GROUP_OPERATOR.initEditor(".stat_template_new",false);
    },
    addRTGroup:function(){
        let obj;
        let formOBJ = {};
        let token = $("#token").val();
        if(Validate.isNull(token)){
            MsgBox.Alert($.i18n.prop('ldp_i18n_group_create_1031'));
            return;
        }
        token = token.trim();
        if(!Validate.isNumLetterOrLine(token)){
            MsgBox.Alert($.i18n.prop('ldp_i18n_group_create_1032'));
            return;
        }
        if(token.length < 5 || token.length > 28){
            MsgBox.Alert($.i18n.prop('ldp_i18n_group_create_1033'));
            return;
        }
        let projectId = $("#projectId").val();
        let statType = $("#statType").val();
        let column_trs = $("#column-content").children("tr");
        let tempColumnArray = [];
        let columnArray = [];
        for(let n=1; n<column_trs.length; n++){
            let column_tr = column_trs[n];
            let columnName = $(column_tr).children(".column_name").html();
            if(Validate.isNull(columnName) || columnName === 'ColumnName'){
                MsgBox.Alert($.i18n.prop('ldp_i18n_group_create_1034'));
                return;
            }
            columnName = columnName.trim();
            if(!Validate.isNumLetterOrLine(columnName)){
                MsgBox.Alert($.i18n.prop('ldp_i18n_group_create_1035'));
                return;
            }
            if(columnName.length < 2 || columnName.length > 20){
                MsgBox.Alert($.i18n.prop('ldp_i18n_group_create_1036'));
                return;
            }

            let columnType = $(column_tr).find(".column_type").val();
            let columnComment = $(column_tr).children(".column_comment").html();
            if(columnComment.length > 60){
                MsgBox.Alert($.i18n.prop('ldp_i18n_group_create_1037'));
                return;
            }

            obj = {};
            obj.columnName = columnName;
            obj.columnType = parseInt(columnType);
            obj.columnLength = -1;
            obj.columnComment = columnComment;
            if(tempColumnArray.indexOf(obj.columnName) > -1){
                MsgBox.Alert(Strings.format($.i18n.prop('ldp_i18n_group_create_1038'),obj.columnName));
                return;
            }
            tempColumnArray[n - 1] = columnName;
            columnArray[n - 1] = obj;
        }
        if(columnArray.length === 0){
            MsgBox.Alert($.i18n.prop('ldp_i18n_group_create_1039'));
            return;
        }
        formOBJ.columnArray = columnArray;
        let itemArray = [];
        let item_trs = $("#rt_item-content").children("tr");
        for(let i=1; i<item_trs.length; i++) {
            let item_tr = item_trs[i];
            let editorId = $(item_tr).find("[class^='stat_template_']").attr("id");
            let editor = ace.edit(editorId);
            let stat_template = editor.getValue();
            if(Validate.isNull(stat_template)){
                MsgBox.Alert($.i18n.prop('ldp_i18n_group_create_1040'));
                return;
            }
            stat_template = stat_template.trim();
            let stat_data_expire = $(item_tr).find(".stat_data_expire").val();
            let time_param = $(item_tr).find(".stat_time_param").val();
            obj = {};
            obj.template = stat_template;
            obj.dataExpire = stat_data_expire;
            obj.timeParam = time_param;
            itemArray[i - 1] = obj;
        }
        if(itemArray.length === 0){
            MsgBox.Alert($.i18n.prop('ldp_i18n_group_create_1041'));
            return;
        }

        formOBJ.itemArray = itemArray;
        formOBJ.token = token.toLowerCase();
        formOBJ.statType = statType;
        formOBJ.projectId = projectId;
        let encryptParams = Encrypt.encryptParams(formOBJ);
        $.ajax({
            type:"POST",
            url:"/group/create/submit.shtml",
            data:encryptParams,
            beforeSend:function(){
                COMMON.showLoading();
            },
            success:function(data){
                COMMON.hiddenLoading();
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_group_create_1042'),function () {MsgBox.Redirect("/project/manage/index.shtml?projectId="+projectId)});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },
    deleteGroupConfirm:function(groupName,groupId,projectId){
        MsgBox.Confirm(Strings.format($.i18n.prop('ldp_i18n_project_manage_1006'),groupName),function(){GROUP_OPERATOR.deleteGroup(groupId,projectId)});
    },
    deleteGroup:function(groupId,projectId){
        $.ajax({
            type:"POST",
            async: false,
            url:Encrypt.encryptUrl("/group/delete/submit.shtml?groupId="+groupId+"&projectId="+projectId),
            success:function(data){
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_project_manage_1007'),function(){MsgBox.Redirect("/project/manage/index.shtml?projectId="+projectId);});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },
    addColumn:function(){
        let html = "<tr>\n" +
            "                      <td onclick=\"PAGE.tdclick(this);\" class=\"column_name\">ColumnName</td>\n" +
            "                      <td style='padding:1;'>\n" +
            "                        <select id=\"\" autocomplete=\"off\" class=\"column_type\" style=\"width: 100px;\" tabindex=\"-1\" aria-hidden=\"true\">\n" +
            "                          <option value=\"1\" selected>String</option>\n" +
            "                          <option value=\"2\">Numeric</option>\n" +
            "                        </select>\n" +
            "                      </td>\n" +
            "                      <td onclick=\"PAGE.tdclick(this);\" class=\"column_comment\">Comment</td>\n" +
            "                      <td class=\"column_operate\" onclick='GROUP_OPERATOR.deleteColumn(this.parentNode)'>\n" +
            "                          <a href=\"javacript:void(0);\" style=\"color:#000000;\"><span class=\"col-md-1 col-sm-2\" title=\"\"><i class=\"fa fa-fw fa-minus-circle\"></i></span></a>\n" +
            "                      </td>\n" +
            "                    </tr>";
        $("#column-content").append(html);
    },
    updateGroup:function(){
        let statType = $("#statType").val();
        if(statType === '1'){
            GROUP_OPERATOR.updateRTGroup();
        }
    },
    updateRTGroup:function(){
        let formOBJ = {};
        let groupId = $("#groupId").val();
        let groupToken = $("#groupToken").val();
        if(Validate.isNull(groupToken)){
            MsgBox.Alert($.i18n.prop('ldp_i18n_group_update_1027'));
            return;
        }
        groupToken = groupToken.toLowerCase();
        if(!Validate.isNumLetterOrLine(groupToken)){
            MsgBox.Alert($.i18n.prop('ldp_i18n_group_update_1028'));
            return;
        }
        if(groupToken.length < 5 || groupToken.length > 28){
            MsgBox.Alert($.i18n.prop('ldp_i18n_group_update_1029'));
            return;
        }
        let projectId = $("#projectId").val();
        let statType = $("#statType").val();
        let column_trs = $("#column-content").children("tr");
        let columnArray = [];
        let tempColumnArray = [];
        for(var i=1;i<column_trs.length;i++){
            let column_tr = column_trs[i];
            let columnName = $(column_tr).children(".column_name").html();
            if(Validate.isNull(columnName) || columnName === 'ColumnName'){
                MsgBox.Alert($.i18n.prop('ldp_i18n_group_update_1030'));
                return;
            }
            columnName = columnName.trim();
            if(!Validate.isNumLetterOrLine(columnName)){
                MsgBox.Alert($.i18n.prop('ldp_i18n_group_update_1031'));
                return;
            }
            if(columnName.length < 2 || columnName.length > 20){
                MsgBox.Alert($.i18n.prop('ldp_i18n_group_update_1032'));
                return;
            }
            let columnType = $(column_tr).find(".column_type").val();
            let columnComment = $(column_tr).children(".column_comment").html();
            if(columnComment.length > 60){
                MsgBox.Alert($.i18n.prop('ldp_i18n_group_update_1033'));
                return;
            }
            let obj = {};
            obj.columnName = columnName;
            obj.columnType = parseInt(columnType);
            obj.columnLength = -1;
            obj.columnComment = columnComment;
            if(tempColumnArray.indexOf(obj.columnName) > -1){
                MsgBox.Alert(Strings.format($.i18n.prop('ldp_i18n_group_update_1033'),obj.columnName));
                return;
            }
            tempColumnArray[i - 1] = columnName;
            columnArray[i - 1] = obj;
        }
        formOBJ.columnArray = columnArray;
        let itemArray = [];
        let item_trs = $("#rt_item-content").children("tr");
        for(let i=1;i<item_trs.length;i++) {
            let item_tr = item_trs[i];
            let editorId = $(item_tr).find("[class^='stat_template_']").attr("id");
            let editor = ace.edit(editorId);
            let stat_template = editor.getValue();
            if(Validate.isNull(stat_template)){
                MsgBox.Alert($.i18n.prop('ldp_i18n_group_update_1035'));
                return;
            }
            let stat_data_expire = $(item_tr).find(".stat_data_expire").val();
            let itemId = $(item_tr).find(".template_id").val();
            let time_param = $(item_tr).find(".stat_time_param").val();
            let obj = {};
            obj.template = stat_template;
            obj.dataExpire = stat_data_expire;
            obj.itemId = itemId;
            obj.timeParam = time_param;
            itemArray[i - 1] = obj;
        }
        if(itemArray.length === 0){
            MsgBox.Alert($.i18n.prop('ldp_i18n_group_update_1036'));
            return;
        }
        formOBJ.groupId = groupId;
        formOBJ.itemArray = itemArray;
        formOBJ.groupToken = groupToken;
        formOBJ.projectId = projectId;
        formOBJ.statType = statType;
        let encryptParams = Encrypt.encryptParams(formOBJ);
        $.ajax({
            type:"POST",
            url:"/group/update/submit.shtml",
            data:encryptParams,
            beforeSend:function(){
                COMMON.showLoading();
            },
            success:function(data){
                COMMON.hiddenLoading();
                if(data.code === '0'){
                    let url = "/group/manage/index.shtml?id="+groupId;
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_group_update_1037'),function(){MsgBox.subRedirect("#sub_nav",url);});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },
    addGroup:function(){
        GROUP_OPERATOR.addRTGroup();
    },
    deleteColumn:function(obj){
        $(obj).remove();
    },
    deleteTemplate:function(obj){
        $(obj).remove();
    },

    initEditor:function(classFlag,readOnly) {
        let templateArray = $(".table-bordered").find(classFlag);
        for (let i = 0; i < templateArray.length; i++) {
            let templateEntity = templateArray[i];
            let id = $(templateEntity).attr("id");
            let editor = ace.edit($(templateEntity).attr("id"));
            editor.setOptions({
                enableBasicAutocompletion: false,
                enableLiveAutocompletion: true
            });
            editor.setReadOnly(readOnly);
            editor.setTheme("ace/theme/textmate");
            editor.getSession().setMode("ace/mode/xml");
            editor.setOption("showPrintMargin", false);
            editor.clearSelection();
            if(readOnly === true){
                let row = editor.session.getLength() - 1
                let column = editor.session.getLine(row).length
                editor.gotoLine(row + 1, column)
                editor.renderer.$cursorLayer.element.style.display = "none"
            }
        }
    }
}

$("#statType").bind("change",function(){
    var vs = $('#statType  option:selected').val();
    if(vs === '1'){
        $("#dataSetForm").hide();
        $("#dataColumnForm").show();
        $("#rt_template").show();
        $("#lt_template").hide();
    }else if(vs === '2'){
        //$("#dataSetForm").show();
        //$("#dataColumnForm").hide();
        //$("#lt_template").show();
        //$("#rt_template").hide();
    }else{
        $("#dataSetForm").hide();
        $("#dataColumnForm").hide();
    }
});

$(document).on('change', ".stat_data_expire", function (event,args1) {
    if(args1 === undefined){
        let vs = $(this).val();
        if(vs >= 15552000){
            MsgBox.Alert($.i18n.prop('ldp_i18n_group_update_1038'));
        }
    }
});