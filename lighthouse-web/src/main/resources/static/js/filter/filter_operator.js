/**
 * -----------------------------------------
 *  Author:XueLing.雪灵
 *  Email:better_xueling@126.com
 * -----------------------------------------
 */
var selectorMap = {};
var renderMap = {};
var updateRenderMap = {};
if (typeof cascadeSelector == 'undefined' || typeof layui.modules.selectN === 'undefined'){
    var cascadeSelector = layui.config({
        base : './'
    }).extend({
        selectN: '../../static/layui/extend/selectN',
        selectM: '../../static/layui/extend/selectM',
    });
}

var FILTER_OPERATOR = {
    renderCascadeSelector:function(elemId,renderObj){
        let cascadeData = typeof renderObj.data == "string" ? JSON.parse(renderObj.data):renderObj.data;
        let level = renderObj.level;
        let width = 300;
        if(level === 1){
            width = 300;
        }else if(level === 2){
            width = 170;
        }else if(level === 3){
            width = 110;
        }
        cascadeSelector.use(['layer','form','jquery','selectN','selectM'],function(){
            $ = layui.jquery;
            let form = layui.form
                , selectN = layui.selectN
                , selectM = layui.selectM;
            let obj =  selectN({
                elem: "#" + elemId
                , data: cascadeData
                , width: width
                , selected: [6, 10, 11]
                , last: true
                , tips: 'Select'
                , filter: ''
                , name: 'cat2'
                , field: {idName: 'id', titleName: 'name', childName: 'children'}
                , formFilter: null
            }, level);
            obj.renderObj = renderObj;
            selectorMap[elemId] = obj;
        });
    },

    combineMultiData:function (parentnode,result,idPrefix,namePrefix){
        for(let i = 0, len = parentnode.length; i < len; i++){
            let node = parentnode[i];
            if(!Validate.isNull(idPrefix)){
                node.aliasId = idPrefix + ";" + node.id;
            }else{
                node.aliasId = node.id;
            }
            if(!Validate.isNull(namePrefix)){
                node.aliasName = namePrefix + ";" + node.name;
            }else{
                node.aliasName = node.name;
            }
            if("children" in node){
                node['children'] = this.combineMultiData(node['children'], [], node.aliasId,node.aliasName);
            }
            result[i] = node;
        }
        return result;
    },

    renderMultiCascadeSelector:function(elemId,renderObj){
        let catData = typeof renderObj.data == "string" ? JSON.parse(renderObj.data):renderObj.data;
        let rawData = this.combineMultiData(catData,[],"","");
        let obj  =  xmSelect.render({
            el: "#" + elemId,
            cascader: {
                show: true,
                indent: 200,
            }
            , data: rawData
        });
        obj.renderObj = renderObj;
        selectorMap[elemId] = obj;
    },

    renderSingleSelector:function(elemId,renderObj){
        let catData = typeof renderObj.data == "string" ? JSON.parse(renderObj.data):renderObj.data;
        let obj  =  xmSelect.render({
            el: "#" + elemId,
            filterable: true,
            toolbar: {
                show: true,
                list: ['ALL', 'CLEAR']
            },
            model: {
                icon: 'hidden',
                label: { type: 'text' } },
            showFolderIcon: true,
            clickClose: true,
            tree: {
                show: true,
                simple: true,
                strict: false,
                expandedKeys: [ -1 ],
            },
            on: function(data){
                if(data.isAdd){
                    return data.change.slice(0, 1)
                }
            },
            height: '500px',
            data: catData
        });
        obj.renderObj = renderObj;
        selectorMap[elemId] = obj;
    },

    renderInput:function(elemId,renderObj){
        $("#"+elemId).html("<input autocomplete=\"off\" class=\"layui-input\" type='text'/>");
        let obj = {};
        obj.renderObj = renderObj;
        selectorMap[elemId] = obj;
    },
    renderMultiSelector:function(elemId,renderObj){
        let catData = typeof renderObj.data == "string" ? JSON.parse(renderObj.data):renderObj.data;
        let level = renderObj.level;
        let label = renderObj.label;
        let filter = renderObj.filter;
        let width = 300;
        let obj = xmSelect.render({
            el: "#" + elemId,
            toolbar: {
                show: true,
                list: ['ALL', 'CLEAR']
            },
            max: 10,
            maxMethod(seles, item) {
                MsgBox.Alert($.i18n.prop('ldp_i18n_display_1056'));
            },
            filterable: true,
            height: '500px',
            data: catData,
            width: width
        });
        obj.renderObj = renderObj;
        selectorMap[elemId] = obj;
    },
    getFilterParams:function (classId) {
        let paramArr = [];
        $("." + classId + " .filter_unit").each(function() {
            let id = $(this).attr("id");
            let selector = selectorMap[id];
            let renderObj = selector.renderObj;
            let filterKey = renderObj.filter;
            let value = [];
            let name = [];
            if(renderObj.type === 1){
                value = selector.getValue('value');
                name = selector.getValue('name');
            }else if(renderObj.type === 2){
                value = selector.getValue('value');
                name = selector.getValue('name');
            }else if(renderObj.type === 3){
                let tempValue = selector.values.filter(x => !Validate.isNull(x));
                let tempName = selector.names.filter(x => !Validate.isNull(x) && x !== '请选择');
                filterKey = filterKey.split(";").slice(0,tempValue.length).join(";");
                value = Array(tempValue.join(";"));
                name = Array(tempName.join(";"));
            }else if(renderObj.type === 4){
                let valueObj = selector.getValue();
                for(let n = 0;n<valueObj.length;n++){
                    let obj = valueObj[n];
                    value[n] = obj.aliasId;
                    name[n] = obj.aliasName;
                }
            }else if(renderObj.type === 5){
                value = Array($("#" + id).children("input:first-child").val());
                name = value;
            }
            let valueList = FILTER_OPERATOR.valuesMapCombine(value,name);
            if(!Validate.isNull(value) && !Validate.isNull(filterKey)){
                let obj = {};
                obj.filterKey = filterKey;
                obj.valueList = valueList;
                paramArr.push(obj);
            }
        });
        return paramArr;
    },

    valuesMapCombine:function(arr1, arr2) {
        let dataArr = [];
        if (arr2.length === 0) {
            return arr1;
        } else {
            arr1.map(function(value, index) {
                if(!Validate.isNull(value)){
                    let obj = {};
                    obj.value = value;
                    obj.aliasName = arr2[index];
                    dataArr[index] = obj;
                }
            })
        }
        return dataArr;
    },

    addCustomComponents:function(curPage){
        if(typeof curPage === 'undefined'){
            curPage = $("#custom_page_index").val();
        }
        let filterObj = $(".filter_custom_components");
        let formOBJ = {};
        formOBJ.page = curPage;
        let encryptParams = Encrypt.encryptParams(formOBJ);
        $.ajax({
            type:"POST",
            url:"/components/custom.shtml",
            data:encryptParams,
            success:function(data){
                COMMON.hiddenLoading();
                if(data.code === '0'){
                    filterObj.html("");
                    let dataObj = data.data;
                    if(dataObj.length > 0){
                        for(let n = 0;n<dataObj.length;n++){
                            filterObj.append("<div style=\"clear:both\"></div>");
                            let renderObj = dataObj[n];
                            let filterId = COMMON.getRandomString(8);
                            let elemObj = "filter_custom_components"  + "_" + filterId;
                            let componentsId = renderObj.id;
                            let operate = 'FILTER_OPERATOR.selectComponents(\''+componentsId+'\')';
                            renderMap[componentsId] = renderObj;
                            let html = "<tr>" +
                                "<td>"+I18N.translate(renderObj.title) +"</td>" +
                                "<td id=\""+elemObj+"\"></td>" +
                                "<td onclick=\""+operate+"\" title='"+$.i18n.prop('ldp_i18n_display_1063')+"'><i class=\"fa fa-fw fa-plus\"></i></td>" +
                                "</td>";
                            filterObj.append(html);
                            FILTER_OPERATOR.filterInit(elemObj,renderObj);
                        }
                    }else{
                        filterObj.html("<div style=\"text-align:center;width:100%;margin-top:20px;\">"+$.i18n.prop('ldp_i18n_display_1062')+"</div>");
                    }
                }
            }
        });
        $('#custom_components').modal('show');
    },

    addSystemComponents:function(curPage){
        let filterObj = $(".filter_system_components");
        let formOBJ = {};
        formOBJ.page = curPage;
        let encryptParams = Encrypt.encryptParams(formOBJ);
        $.ajax({
            type:"POST",
            url:"/components/system.shtml",
            data:encryptParams,
            success:function(data){
                COMMON.hiddenLoading();
                if(data.code === '0'){
                    filterObj.html("");
                    let dataObj = data.data;
                    for(let n = 0;n<dataObj.length;n++){
                        filterObj.append("<div style=\"clear:both\"></div>");
                        let renderObj = dataObj[n];
                        let filterId = COMMON.getRandomString(8);
                        let elemObj = "filter_system_components"  + "_" + filterId;
                        let componentsId = renderObj.id;
                        let operate = 'FILTER_OPERATOR.selectComponents(\''+componentsId+'\')';
                        renderMap[componentsId] = renderObj;
                        let html = "<tr>" +
                            "<td>"+I18N.translate(renderObj.title) +"</td>" +
                            "<td id=\""+elemObj+"\"></td>" +
                            "<td onclick=\""+operate+"\" title='"+$.i18n.prop('ldp_i18n_display_1063')+"'><i class=\"fa fa-fw fa-plus\"></i></td>" +
                            "</td>";
                        filterObj.append(html);
                        FILTER_OPERATOR.filterInit(elemObj,renderObj);
                    }
                }
            }
        });
        $('#system_components').modal('show');
    },

    selectComponents:function(componentsId){
        if(Object.keys(updateRenderMap).length >= 5){
            MsgBox.Alert($.i18n.prop('ldp_i18n_display_1065'));
            return;
        }
        let filterObj = $(".filter_change_components");
        let renderObj = renderMap[componentsId];
        let filterId = COMMON.getRandomString(8);
        let addElemId = "filter_change_components"  + "_" + filterId;
        updateRenderMap[filterId] = renderObj;
        filterObj.append(
            "<tr id='"+filterId+"'>" +
            "<input type='hidden' class='componentsId' value='"+componentsId+"'>" +
            "<input type='hidden' class='componentsType' value='"+renderObj.type+"'>" +
            "<input type='hidden' class='maxLevel' value='"+renderObj.level+"'>" +
            "<td class='filterLabel' onclick=\"PAGE.tdclick2(this);\">--</td>" +
            "<td class='filterParam' onclick=\"PAGE.tdclick2(this);\">--</td>" +
            "<td id=\""+addElemId+"\"></td>" +
            "<td onclick='FILTER_OPERATOR.removeFilter(this);' title='"+$.i18n.prop('ldp_i18n_display_1064')+"'><i class=\"fa fa-fw fa-remove\"></i></td>" +
            "</td>"
        );
        FILTER_OPERATOR.filterInit(addElemId,renderObj);
        $('.change_components').modal('hide');
    },
    filterInit:function (div,renderObj) {
        let type = renderObj.type;
        if(type === 1){
            FILTER_OPERATOR.renderSingleSelector(div,renderObj);
        }else if(type === 2){
            FILTER_OPERATOR.renderMultiSelector(div,renderObj);
        }else if(type === 3){
            FILTER_OPERATOR.renderCascadeSelector(div,renderObj);
        }else if(type === 4){
            FILTER_OPERATOR.renderMultiCascadeSelector(div,renderObj);
        }else if(type === 5){
            FILTER_OPERATOR.renderInput(div,renderObj);
        }
    },

    initFilterOfExport:function (filterParams){
        let filterObj = $(".filter_export_components");
        if(Validate.isNull(filterParams)){
            return null;
        }
        let filterParamsObj = JSON.parse(filterParams);
        let html;
        for(let n = 0;n<filterParamsObj.length;n++){
            let renderObj = filterParamsObj[n];
            let filterId = COMMON.getRandomString(8);
            let elemId = "filter_export_components" + "_" + filterId;
            filterObj.append("<tr>\n" +
                "                            <td>"+renderObj.label+"</td>\n" +
                "                            <td>\n" +
                "                                <span class=\"filter_unit\" style=\"width: 350px;margin-bottom: 5px;float: left;\" id=\""+elemId+"\"></span>\n" +
                "                            </td>\n" +
                "                        </tr>");
            FILTER_OPERATOR.filterInit(elemId,renderObj);
        }
    },


    initFilter:function (filterParams,isSub) {
        let filterDefaultObj = $(".filter_default_components");
        let filterChangeObj = $(".filter_change_components");
        let lineSize = 3;
        if(isSub === 'true'){
            lineSize = 2;
        }
        if(!Validate.isNull(filterParams)){
            $(filterDefaultObj).html("");
            $(filterChangeObj).html("");
            let filterParamsObj = JSON.parse(filterParams);
            for(let n = 0;n<filterParamsObj.length;n++){
                if(n !== 0 && n % lineSize === 0){
                    filterDefaultObj.append("<div style=\"clear:both\"></div>");
                }
                let renderObj = filterParamsObj[n];
                let filterId = COMMON.getRandomString(8);
                let elemId = "filter_default_components" + "_" + filterId;
                filterDefaultObj.append(
                    "<label class=\"layui-form-label\" style=\"float: left;font-weight:420;font-size:15px;\">"+renderObj.label +"：</label>" +
                    "<span class=\"filter_unit\" style=\"width: 350px;margin-bottom: 5px;float: left;\" id=\""+elemId+"\"></span>"
                );
                FILTER_OPERATOR.filterInit(elemId,renderObj);
            }
            for(let n = 0;n<filterParamsObj.length;n++){
                let renderObj = filterParamsObj[n];
                let filter = renderObj.filter;
                let filterId = COMMON.getRandomString(8);
                let elemId = "filter_change_components" + "_" + filterId;
                updateRenderMap[filterId] = renderObj;
                filterChangeObj.append(
                    "<tr id='"+filterId+"'>" +
                    "<input type='hidden' class='componentsId' value='"+renderObj.componentsId+"'>" +
                    "<input type='hidden' class='componentsType' value='"+renderObj.type+"'>" +
                    "<input type='hidden' class='maxLevel' value='"+renderObj.level+"'>" +
                    "<td class='filterLabel' onclick=\"PAGE.tdclick2(this);\">"+renderObj.label +"</td>" +
                    "<td class='filterParam' onclick=\"PAGE.tdclick2(this);\">"+renderObj.filter +"</td>" +
                    "<td id=\""+elemId+"\"></td>" +
                    "<td onclick='FILTER_OPERATOR.removeFilter(this)' title='"+$.i18n.prop('ldp_i18n_display_1048')+"'><i class=\"fa fa-fw fa-remove\"></i></td>" +
                    "</td>"
                );
                FILTER_OPERATOR.filterInit(elemId,renderObj);
            }
            if(filterParamsObj.length > 0){
                filterDefaultObj.append("<div class=\"btn-group\" style=\"margin-top: 5px;margin-left: 20px;width:98px;\">\n" +
                    "       <a href=\"javascript:void(0);\" title=\""+$.i18n.prop('ldp_i18n_display_1045')+"\" style='float: left'>\n" +
                    "                      <button type=\"button\" id=\"clear_button_filter1\" class=\"btn btn-block btn-danger btn-flat\">"+$.i18n.prop('ldp_i18n_display_1045')+"</button>\n" +
                    "                    </a>\n" +
                    "                    <a href=\"javascript:void(0);\" title=\""+$.i18n.prop('ldp_i18n_display_1046')+"\" style='float: right;'>\n" +
                    "                      <button type=\"button\" id=\"filter_button_filter1\" class=\"btn btn-block btn-danger btn-flat\">"+$.i18n.prop('ldp_i18n_display_1046')+"</button>\n" +
                    "                    </a>\n" +
                    "                  </div>");

                document.getElementById('filter_button_filter1').onclick = function () {
                    DISPLAY_OPERATOR.changeDimensParams();
                };

                document.getElementById('clear_button_filter1').onclick = function () {
                    $(".filter_unit").each(function() {
                        let id = $(this).attr("id");
                        let selector = selectorMap[id];
                        let type = selector.renderObj.type;
                        if(type === 1){
                            selector.setValue([]);
                        }else if(type === 2){
                            selector.setValue([]);
                        }else if(type === 3){
                            selector.set();
                        }else if(type === 4){
                            selector.setValue([]);
                        }else if(type === 5){
                            $("#" + id).children("input:first-child").val("");
                        }
                    });
                };
            }
        }
    },
    removeFilter:function(obj){
        let tr = $(obj).parent();
        let id = tr.attr("id");
        delete updateRenderMap[id];
        tr.remove();
    },

    change:function (statId) {
        if(Object.keys(updateRenderMap).length === 0){
            MsgBox.Alert($.i18n.prop('ldp_i18n_display_1049'));
            return;
        }
        let trArr = $(".filter_change_components tr");
        let componentsArray=[];
        for(let n = 0;n<trArr.length;n++){
            let obj = trArr[n];
            let filterLabel = $(obj).find(".filterLabel").html();
            let filterParam = $(obj).find(".filterParam").html();
            let componentsId = $(obj).find(".componentsId").val();
            let componentsType = $(obj).find(".componentsType").val();
            let maxLevel = parseInt($(obj).find(".maxLevel").val());
            if(Validate.isNull(filterLabel) || filterLabel === '--'){
                MsgBox.Alert($.i18n.prop('ldp_i18n_display_1050'));
                return;
            }
            if(filterLabel.length > 30){
                MsgBox.Alert($.i18n.prop('ldp_i18n_display_1051'));
                return;
            }
            if(Validate.isNull(filterParam) || filterParam === '--'){
                MsgBox.Alert($.i18n.prop('ldp_i18n_display_1052'));
                return;
            }
            let filterParamsLength = filterParam.split(";").filter(x => !Validate.isNull(x)).length;
            if(filterParamsLength !== maxLevel){
                MsgBox.Alert(Strings.format($.i18n.prop('ldp_i18n_display_1053'),filterLabel,maxLevel));
                return;
            }
            let components = {};
            components.label = filterLabel;
            components.filter = filterParam;
            components.componentsId = componentsId;
            components.type = componentsType;
            componentsArray[n] = components;
        }
        let formOBJ = {};
        formOBJ.data = componentsArray;
        formOBJ.statId = statId;
        let encryptParams = Encrypt.encryptParams(formOBJ);
        $.ajax({
            type:"POST",
            url:"/display/changeFilterConfig.shtml",
            data:encryptParams,
            success:function(data){
                COMMON.hiddenLoading();
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_display_1054'),function () {
                        let paramObj = {
                            statId:statId,
                            isSub:isSub === 'true' ? 1 : 0
                        };
                        let url = Link.CombineParams("/display/stat.shtml",paramObj);
                        if(isSub === 'true'){
                            MsgBox.subRedirect("#sub_nav",url);
                        }else{
                            MsgBox.Redirect(url);
                        }
                    });
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },
    reset:function (statId) {
        let formOBJ = {};
        formOBJ.statId = statId;
        let encryptParams = Encrypt.encryptParams(formOBJ);
        $.ajax({
            type:"POST",
            url:"/display/resetFilterConfig.shtml",
            data:encryptParams,
            success:function(data){
                COMMON.hiddenLoading();
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_display_1054'),function () {
                        let paramObj = {
                            statId:statId,
                            isSub:isSub === 'true' ? 1 : 0
                        };
                        let url = Link.CombineParams("/display/stat.shtml",paramObj);
                        if(isSub === 'true'){
                            MsgBox.subRedirect("#sub_nav",url);
                        }else{
                            MsgBox.Redirect(url);
                        }
                    });
                }else{
                    MsgBox.AlertWithCallBack(I18N.translate(data.msg),function () {window.location.reload();});
                }
            }
        });
    }
};

