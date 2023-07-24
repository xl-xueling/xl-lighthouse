
jQuery("#userInfo").on('click',function(){
    $(this).toggleClass("open");
});

jQuery(document).mouseup(function(e){
    let child = $("#userInfo");
    if(!child.is(e.target) && child.has(e.target).length === 0){
        $("#userInfoPop").hide();
        $(child).removeClass("open");
    }else{
        $("#userInfoPop").show();
    }

    let leftMenu = $("#side-menu");
    if(!leftMenu.is(e.target) && leftMenu.has(e.target).length === 0){
        $(leftMenu).removeClass("control-sidebar-open");
    }else{
        $(leftMenu).addClass("control-sidebar-open");
    }

    let rightMenu = $(".main-sidebar");
    let rightMenuButton = $(".sidebar-toggle");
    if(!rightMenu.is(e.target) && rightMenu.has(e.target).length === 0 &&
        !rightMenuButton.is(e.target) && rightMenuButton.has(e.target).length === 0){
        $("body").addClass("sidebar-collapse");
    }
});