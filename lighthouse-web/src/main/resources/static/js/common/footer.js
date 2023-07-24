$(document).ready(function (){
    if(typeof i18n_init_flag === 'undefined'){
        var i18n_init_flag = 'true';
        I18N.refresh();
    }
    $(".select2").not(".total-users").select2();
    $(".total-users").select2({
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

    // $('input[type="checkbox"].minimal, input[type="radio"].minimal').iCheck({
    //     checkboxClass: 'icheckbox_minimal-blue',
    //     radioClass: 'iradio_minimal-blue'
    // });

    $(".wrapper").show();
    typeof _PageLoadCallBack === "function" ? _PageLoadCallBack() : false;
});
