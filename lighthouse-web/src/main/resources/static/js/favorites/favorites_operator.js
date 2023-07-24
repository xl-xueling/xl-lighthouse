/**
 * -----------------------------------------
 *  Author:XueLing.雪灵
 *  Email:better_xueling@126.com
 * -----------------------------------------
 */
var $ = jQuery.noConflict();
var IDMark_Switch = "_switch", IDMark_A = "_a"
var FAVORITES_OPERATOR = {
    favoritesConfirm:function(relationB,relationType,desc,redirectURL){
        MsgBox.Confirm(Strings.format($.i18n.prop('ldp_i18n_favorite_manage_1001'),desc), function () { FAVORITES_OPERATOR.favorites(relationB,relationType,redirectURL); });
    },
    removeFavoritesConfirm:function(relationB,relationType,desc,redirectURL){
        MsgBox.Confirm(Strings.format($.i18n.prop('ldp_i18n_favorite_manage_1002'),desc), function () { FAVORITES_OPERATOR.removeFavorites(relationB,relationType,redirectURL); });
    },
    favorites:function(relationB,relationType,redirectURL){
        if(Validate.isNull(relationB)){
            MsgBox.Alert($.i18n.prop('ldp_i18n_favorite_manage_1003'));
            return;
        }
        if(Validate.isNull(relationType) || relationType === ''){
            MsgBox.Alert($.i18n.prop('ldp_i18n_favorite_manage_1003'));
            return;
        }
        let formOBJ = {};
        formOBJ.relationB = relationB;
        formOBJ.relationType = relationType;
        let encryptParams = Encrypt.encryptParams(formOBJ);
        $.ajax({
            type:"POST",
            url:"/relations/create/submit.shtml",
            data:encryptParams,
            beforeSend:function(){
                COMMON.showLoading();
            },
            success:function(data){
                COMMON.hiddenLoading();
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_favorite_manage_1004'),function () {MsgBox.Redirect(redirectURL)});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },
    removeFavorites:function(relationB,relationType,redirectURL){
        if(Validate.isNull(relationB)){
            MsgBox.Alert($.i18n.prop('ldp_i18n_favorite_manage_1003'));
            return;
        }
        if(Validate.isNull(relationType) || relationType === ''){
            MsgBox.Alert($.i18n.prop('ldp_i18n_favorite_manage_1003'));
            return;
        }
        let formOBJ = {};
        formOBJ.relationB = relationB;
        formOBJ.relationType = relationType;
        let encryptParams = Encrypt.encryptParams(formOBJ);
        $.ajax({
            type:"POST",
            url:"/relations/remove/submit.shtml",
            data:encryptParams,
            beforeSend:function(){
                COMMON.showLoading();
            },
            success:function(data){
                COMMON.hiddenLoading();
                if(data.code === '0'){
                    MsgBox.AlertWithCallBack($.i18n.prop('ldp_i18n_favorite_manage_1005'),function () {MsgBox.Redirect(redirectURL)});
                }else{
                    MsgBox.AlertRequestError(data);
                }
            }
        });
    },
    itemClick:function(e, treeId, treeNode) {
        $(".treeIcon").remove();
        if (treeNode.parentNode && treeNode.parentNode.id!==1) return;
        let aObj = $("#" + treeNode.tId + IDMark_A);
        let type = treeNode.type;
        if(type === 2){
            let url = Encrypt.encryptUrl("/display/stat.shtml?statId="+treeNode.id+"&isSub=1");
            COMMON.loadURL("#sub_nav",url);
            let deleteHtml = "<span class='treeIcon' id='diyBtn_delete_" +treeNode.id+ "' title='"+$.i18n.prop('ldp_i18n_favorite_manage_1006')+"' onfocus='this.blur();'><span class='button icon_delete'></span></span>";
            aObj.append(deleteHtml);
            let btn2 = $("#diyBtn_delete_"+treeNode.id);
            if (btn2) btn2.bind("click", function(event){
                FAVORITES_OPERATOR.removeFavoritesConfirm(treeNode.id,'1',treeNode.name,"/favorite/stat/list.shtml");
                event.stopPropagation();
            })
        }
    },
    projectClick:function(e, treeId, treeNode) {
        $(".treeIcon").remove();
        if (treeNode.parentNode && treeNode.parentNode.id!==1) return;
        let aObj = $("#" + treeNode.tId + IDMark_A);
        let type = treeNode.type;
        if(type === 1){
            let deleteHtml = "<span class='treeIcon' id='diyBtn_delete_" +treeNode.id+ "' title='"+$.i18n.prop('ldp_i18n_favorite_manage_1006')+"' onfocus='this.blur();'><span class='button icon_delete'></span></span>";
            aObj.append(deleteHtml);
            let btn2 = $("#diyBtn_delete_"+treeNode.id);
            if (btn2) btn2.bind("click", function(event){
                FAVORITES_OPERATOR.removeFavoritesConfirm(treeNode.id,'2',treeNode.name,"/favorite/project/list.shtml");
                event.stopPropagation();
            })
        }else if(type === 3){
            let url = Encrypt.encryptUrl("/display/stat.shtml?statId="+treeNode.id+"&isSub=1");
            COMMON.loadURL("#sub_nav",url);
        }
    }
};

