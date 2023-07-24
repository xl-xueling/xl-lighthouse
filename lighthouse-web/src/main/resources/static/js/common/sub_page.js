$(document).ready(function (){
    I18N.refresh();
    $(".nested-sub-page .select2").not(".total-users").select2();
    $(".nested-sub-page .total-users").select2({
        minimumInputLength:1,
        minimumResultsForSearch: 15,
        maximumSelectionLength: 5,
        ajax:{
            url: Encrypt.encryptUrl('/user/termQuery.shtml'),
            dataType:'json',
            delay:300,
            cache:true,
            data:function(params){
                params.page = params.page || 1;
                return {
                    "term": params.term,
                    "page": params.page
                };
            },
            processResults:function(res,params){
                return {
                    results:res.data,
                    pagination: {
                        more: res.data.length > 0
                    }
                }
            }
        },
        escapeMarkup: function (markup) { return markup; },
    })
    $(".nested-sub-page").show();
    typeof _SubPageLoadCallBack === "function" ? _SubPageLoadCallBack() : false;
});
