/**
 * -----------------------------------------
 *  Author:XueLing.雪灵
 *  Email:better_xueling@126.com
 * -----------------------------------------
 */
var SITEMAP_OPERATOR = {

    itemClick:function(e, treeId, treeNode) {
        let statId = (treeNode.id).split("_")[1];
        $(".treeIcon").remove();
        let type = treeNode.type;
        if(type === 2){
            let url = Encrypt.encryptUrl("/display/stat.shtml?siteId="+siteId+"&statId="+statId+"&isSub=1");
            COMMON.loadURL("#sub_nav",url);
        }
    },
    showPreview:function (siteId){
        $('#siteMapPreviewModal').modal('show');
        let chart = echarts.init(document.getElementById('mainChart'));
        let obj = {};
        obj.siteId = siteId;
        let encryptParams = Encrypt.encryptParams(obj);
        $.ajax({
            type:"POST",
            url:"/sitemap/struct.shtml",
            data:encryptParams,
            success:function(value){
                chart.setOption(getOption(value));
            }
        });

        function getOption(data){
            return {
                tooltip: {
                    trigger: 'item',
                    triggerOn: 'mousemove'
                },
                series: [
                    {
                        type: 'tree',
                        data: [data],
                        top: '1%',
                        left: '20%',
                        bottom: '1%',
                        right: '30%',
                        symbolSize: 7,
                        label: {
                            position: 'left',
                            verticalAlign: 'middle',
                            align: 'right',
                            fontSize: 12
                        },
                        leaves: {
                            label: {
                                position: 'right',
                                verticalAlign: 'middle',
                                align: 'left'
                            }
                        },
                        emphasis: {
                            focus: 'descendant'
                        },
                        expandAndCollapse: true,
                        animationDuration: 550,
                        animationDurationUpdate: 750
                    }
                ]
            };
        }
    },

    createSiteMap:function (){
        let treeArr = [];
        let treeObj = $.fn.zTree.getZTreeObj("siteMapTree");
        let nodes = treeObj.transformToArray(treeObj.getNodes());
        if(nodes.length >= 50){
            MsgBox.Alert($.i18n.prop('ldp_i18n_sitemap_create_1009'));
            return;
        }
        let treeLev = 0;
        let siteName = "";
        for (let i = 0, length = nodes.length; i < length; i++) {
            let temp = nodes[i];
            let nodeObj = {};
            if(temp.level > treeLev){
                treeLev = temp.level;
            }
            if(temp.id === "0"){
                siteName = temp.name;
            }
            nodeObj.id = temp.id;
            nodeObj.pId = temp.pId;
            if(Validate.isNull(nodeObj.pId)){
                nodeObj.pId = "-1";
            }
            nodeObj.fullPath = SITEMAP_OPERATOR.getFullPath(temp);
            nodeObj.fullPathName = SITEMAP_OPERATOR.getFullPathName(temp);
            nodeObj.name = temp.name;
            nodeObj.open = 'true';
            treeArr[i] = nodeObj;
        }
        if(Validate.isNull(siteName)){
            MsgBox.Alert($.i18n.prop('ldp_i18n_sitemap_manage_1030'))
            return;
        }
        if(treeLev >= 3){
            MsgBox.Alert($.i18n.prop('ldp_i18n_sitemap_create_1010'));
            return;
        }
        let mapStar = $("#map_star").rating().val();
        let admins = $("#admins").val();
        if(!admins){
            MsgBox.Alert($.i18n.prop('ldp_i18n_sitemap_create_1011'));
            return;
        }
        if(admins.length < 1){
            MsgBox.Alert($.i18n.prop('ldp_i18n_sitemap_create_1011'));
            return;
        }
        if(admins.length > 4){
            MsgBox.Alert($.i18n.prop('ldp_i18n_sitemap_create_1012'));
            return;
        }

        let desc = $("#desc").val();
        if(Validate.isNull(desc)){
            MsgBox.Alert($.i18n.prop('ldp_i18n_sitemap_create_1013'));
            return;
        }
        if(desc.length < 5){
            MsgBox.Alert($.i18n.prop('ldp_i18n_sitemap_create_1014'));
            return;
        }
        if(desc.length > 50){
            MsgBox.Alert($.i18n.prop('ldp_i18n_sitemap_create_1015'));
            return;
        }
        let obj = {};
        obj.siteName = siteName;
        obj.desc = desc.trim();
        obj.star = mapStar
        obj.admins = admins;
        obj.config = treeArr;
        let encryptParams = Encrypt.encryptParams(obj);
        $.ajax({
            type:"POST",
            url:"/sitemap/create/submit.shtml",
            data:encryptParams,
            beforeSend:function(){
                COMMON.showLoading();
            },
            success:function(data){
                COMMON.hiddenLoading();
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_sitemap_create_1016'),function(){MsgBox.Redirect("/sitemap/list.shtml")});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },

    getFullPathName:function(node){
        let fullPathName = node.name;
        while(node=node.getParentNode()) {
            fullPathName=node.name+'/'+fullPathName;
        }
        return fullPathName;
    },

    getFullPath:function(node){
        let fullPath = node.id;
        while(node=node.getParentNode()) {
            fullPath=node.id+','+fullPath;
        }
        return fullPath;
    },

    updateSiteMap:function (){
        let siteId = $("#siteId").val();
        let treeArr = [];
        let treeObj = $.fn.zTree.getZTreeObj("siteMapTree");
        let nodes = treeObj.transformToArray(treeObj.getNodes());
        if(nodes.length >= 50){
            MsgBox.Alert($.i18n.prop('ldp_i18n_sitemap_manage_1011'));
            return;
        }
        let treeLev = 0;
        let siteName = "";
        for (let i = 0, length = nodes.length; i < length; i++) {
            let temp = nodes[i];
            let nodeObj = {};
            if(temp.level > treeLev){
                treeLev = temp.level;
            }
            nodeObj.id = temp.id;
            nodeObj.pId = temp.pId;
            if(temp.id === "0"){
                siteName = temp.name;
            }
            nodeObj.fullPath = SITEMAP_OPERATOR.getFullPath(temp);
            nodeObj.fullPathName = SITEMAP_OPERATOR.getFullPathName(temp);
            if(Validate.isNull(temp.name)){
                MsgBox.Alert($.i18n.prop('ldp_i18n_sitemap_manage_1012'));
                return;
            }
            if(temp.name.length > 16){
                MsgBox.Alert(Strings.format($.i18n.prop('ldp_i18n_sitemap_manage_1013'),temp.name));
                return;
            }
            if(Validate.isNull(nodeObj.pId)){
                nodeObj.pId = "-1";
            }
            nodeObj.name = temp.name;
            nodeObj.open = 'true';
            treeArr[i] = nodeObj;
        }
        if(Validate.isNull(siteName)){
            MsgBox.Alert($.i18n.prop('ldp_i18n_sitemap_manage_1030'))
            return;
        }
        if(treeLev >= 3){
            MsgBox.Alert($.i18n.prop('ldp_i18n_sitemap_manage_1014'));
            return;
        }
        let mapStar = $("#map_star").rating().val();
        let admins = $("#admins").val();
        if(!admins){
            MsgBox.Alert($.i18n.prop('ldp_i18n_sitemap_manage_1015'));
            return;
        }
        if(admins.length < 1){
            MsgBox.Alert($.i18n.prop('ldp_i18n_sitemap_manage_1015'));
            return;
        }
        if(admins.length > 4){
            MsgBox.Alert($.i18n.prop('ldp_i18n_sitemap_manage_1016'));
            return;
        }
        let desc = $("#desc").val();
        if(Validate.isNull(desc)){
            MsgBox.Alert($.i18n.prop('ldp_i18n_sitemap_manage_1017'));
            return;
        }
        if(desc.length < 5){
            MsgBox.Alert($.i18n.prop('ldp_i18n_sitemap_manage_1018'));
            return;
        }
        if(desc.length > 50){
            MsgBox.Alert($.i18n.prop('ldp_i18n_sitemap_manage_1019'));
            return;
        }
        let obj = {};
        obj.siteName = siteName;
        obj.siteId = siteId;
        obj.config = treeArr;
        obj.star = mapStar;
        obj.desc = desc.trim();
        obj.admins = admins;
        let encryptParams = Encrypt.encryptParams(obj);
        $.ajax({
            type:"POST",
            url:"/sitemap/manage/submit.shtml",
            data:encryptParams,
            beforeSend:function(){
                COMMON.showLoading();
            },
            success:function(data){
                COMMON.hiddenLoading();
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_sitemap_manage_1020'),function(){MsgBox.Redirect("/sitemap/list.shtml")});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },

    search:function(){
        const search = $("#search_param").val();
        const owner = $("#owner").is(":checked")? 1 : 0;
        const params = {
            owner: owner,
            search: search
        };
        const url = Link.CombineParams("/sitemap/list.shtml",params);
        MsgBox.Redirect(url);
    },

    deleteConfirm:function(id,name){
        MsgBox.Confirm(Strings.format($.i18n.prop('ldp_i18n_sitemap_manage_1021'),name), function () { SITEMAP_OPERATOR.delete(id); });
    },
    delete:function(id){
        $.ajax({
            type:"POST",
            async: false,
            url:Encrypt.encryptUrl("/sitemap/delete/submit.shtml?id="+id),
            success:function(data){
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_sitemap_manage_1022'),function () {MsgBox.Redirect("/sitemap/list.shtml")});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },

    showPublishToSiteMapModal:function (projectId,groupId,statId) {
        $('#publishToSiteMap').modal('show');
        $('#quote_projectId').val(projectId);
        $('#quote_groupId').val(groupId);
        $('#quote_statId').val(statId);
    },

    publishToSiteMapConfirm:function() {
        let statId = $('#quote_statId').val();
        let siteId = $('#quote_siteId').val();
        let siteName = $('#quote_siteId').find("option:selected").text();
        if(siteId === '-1'){
            MsgBox.Alert($.i18n.prop('ldp_i18n_sitemap_manage_1024'));
            return;
        }
        MsgBox.Confirm(Strings.format($.i18n.prop('ldp_i18n_sitemap_manage_1023'),statId), function () { SITEMAP_OPERATOR.publishToSiteMapSubmit(siteId,statId);});
    },

    publishToSiteMapSubmit:function (siteId,statId){
        let params = {};
        params.siteId = siteId;
        params.statId = statId;
        let formOBJ = {};
        formOBJ.orderType = '6';
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
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_sitemap_manage_1025'),function () {
                        COMMON.hideAllPop();
                    });
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },

    treeClick:function (e,treeId, treeNode){
        let siteId = $("#siteId").val();
        let nodeId = treeNode.id;
        let url = "/sitebind/list.shtml?siteId="+siteId+"&nodeId="+nodeId;
        MsgBox.subRedirect("#sub_nav",url);
    },

    focusConfirm:function(siteId,siteName){
        MsgBox.Confirm(Strings.format($.i18n.prop('ldp_i18n_sitemap_manage_1026'),siteName), function () { SITEMAP_OPERATOR.focus(siteId); });
    },
    focus:function(siteId){
        let formOBJ = {};
        formOBJ.relationB = siteId;
        formOBJ.relationType = '3';
        let encryptParams = Encrypt.encryptParams(formOBJ);
        $.ajax({
            type:"POST",
            url:"/relations/create/submit.shtml",
            data:encryptParams,
            success:function(data){
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_sitemap_manage_1027'),function () {MsgBox.Redirect("/sitemap/list.shtml?owner=1")});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },
    unFocusConfirm:function(siteId,siteName){
        MsgBox.Confirm(Strings.format($.i18n.prop('ldp_i18n_sitemap_manage_1028'),siteName), function () { SITEMAP_OPERATOR.unFocus(siteId); });
    },
    unFocus:function(siteId){
        let formOBJ = {};
        formOBJ.relationB = siteId;
        formOBJ.relationType = '3';
        let encryptParams = Encrypt.encryptParams(formOBJ);
        $.ajax({
            type:"POST",
            url:"/relations/remove/submit.shtml",
            data:encryptParams,
            success:function(data){
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_sitemap_manage_1029'),function () {MsgBox.Redirect("/sitemap/list.shtml?owner=1")});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },

    beforeEditName:function(treeId, treeNode) {
        ztreeObject.selectNode(treeNode);
        setTimeout(function() {
            setTimeout(function() {
                ztreeObject.editName(treeNode);
            }, 0);
        }, 0);
        return false;
    },

    beforeRemove:function(treeId, treeNode) {
        ztreeObject.selectNode(treeNode);
        if(treeNode.id === '0'){
            MsgBox.Alert("Root node cannot be deleted!");
            return false;
        }
        MsgBox.Confirm("Confirm to delete the node【"+treeNode.name+"】？",function (){
            if(treeNode.isParent){
                MsgBox.Alert("The current node has child nodes that cannot be deleted!");
            }else{
                ztreeObject.removeNode(treeNode)
            }
        })
        return false;
    },
    beforeRename:function (treeId, treeNode, newName, isCancel) {
        if (newName === 'undefined' || newName.length === 0) {
            setTimeout(function() {
                ztreeObject.cancelEditName();
                alert("Node name cannot be empty!");
            }, 0);
            return false;
        }
        return true;
    },
    addHoverDom:function (treeId, treeNode) {
        let sObj = $("#" + treeNode.tId + "_span");
        let addBtn = $("#addBtn_"+treeNode.tId);
        if (treeNode.editNameFlag || $(addBtn).length>0) return;
        let addStr = "<span class='button add' id='addBtn_" + treeNode.tId
            + "' title='add node' onfocus='this.blur();'></span>";
        sObj.after(addStr);
        addBtn = $("#addBtn_"+treeNode.tId);
        if (addBtn) addBtn.bind("click", function(){
            maxId++;
            if(treeNode.level >= '2'){
                MsgBox.Alert("Tree structure exceeds max level limit!");
                return false;
            }
            ztreeObject.addNodes(treeNode, {id:(maxId), pId:treeNode.id, name:"New Node" + (maxId)});
            return false;
        });
    },
    removeHoverDom:function (treeId, treeNode) {
        $("#addBtn_"+treeNode.tId).unbind().remove();
    }
};

$("#owner").on('click',function(){
    var search = $("#search_param").val();
    var departmentId = $("#department_id").val();
    var owner = $("#owner").is(":checked") === true? 1 : 0;
    if(Validate.isNull(search)){
        MsgBox.Redirect("/sitemap/list.shtml?owner="+owner);
    }else{
        MsgBox.Redirect("/sitemap/list.shtml?search="+search+"&owner="+owner);
    }
});