<html lang="UTF-8">
<head>
  <#import "../common/frame.ftl" as baseFrame>
  <title class="i18n" i18n_code="ldp_i18n_user_login_1000"></title>
  <@baseFrame.top />
</head>
<body class="hold-transition register-page" style="overflow-y: hidden;overflow-x: hidden;">
<div class="nav-tabs-custom" style="border:none;">
  <ul class="nav nav-tabs" style="border: none;float: right;">
    <li class="dropdown language-div">
      <a class="language-dropdown" href="#"><span id="language-div">Language</span><span class="caret"></span>
      </a>
      <ul class="dropdown-menu language-menu">
        <li role="presentation"><a role="menuitem"  href="javascript:changeLanguage('zh')">简体中文</a></li>
        <li role="presentation"><a role="menuitem"  href="javascript:changeLanguage('en')">English</a></li>
      </ul>
    </li>
  </ul>
</div>
<p style="clear: both;"></p>
<div id="popLayer" class="pop_layer" style="position:fixed;width: 100%;height: 100%;display: none;"></div>
<div class="register-box wrapper">
  <div class="register-logo">
    <a href="index.shtml">LightHouse</a>
  </div>
  <div class="register-box-body">
    <p class="login-box-msg"><samp class="i18n" i18n_code="ldp_i18n_user_login_1001"></samp></p>
      <div class="form-group has-feedback">
        <label for="userName"></label>
        <input type="text" class="form-control i18n" autocomplete="off" readonly onfocus="this.removeAttribute('readonly');" onblur="this.setAttribute('readonly',true);" id="userName" placeholder="i18n(ldp_i18n_user_login_1002)">
      </div>
    <div class="form-group has-feedback">
      <label for="password"></label>
      <input type="password" class="form-control i18n" autocomplete="off" readonly onfocus="this.removeAttribute('readonly');" onblur="this.setAttribute('readonly',true);" id="password" placeholder="i18n(ldp_i18n_user_login_1003)">
    </div>
    <div class="row">
        <div class="col-xs-8" style="width: 90%;">
          <div class="checkbox">
            <label>
              <input type="checkbox" checked id="agreement">
              <samp class="i18n" style="font-size: 12px;font-family: 'Source Sans Pro','Helvetica Neue',Helvetica,Arial,sans-serif;" i18n_code="ldp_i18n_user_login_1005"></samp>
            </label>
          </div>
        </div>
        <div class="col-xs-12" style="margin-top: 5px;">
          <a href="/user/register/index.shtml"><samp class="i18n" i18n_code="ldp_i18n_user_login_1006"></samp></a>
          <a href="javascript:USER_OPERATOR.login();">
            <button class="btn btn-primary" style="float: right;line-height: 1.42857;vertical-align: middle;"><samp class="i18n" i18n_code="ldp_i18n_user_login_1007"></samp></button>
          </a>
        </div>
      </div>
  </div>
</div>
<script src="/static/js/user/user_operator.js" charset="utf-8"></script>
<@baseFrame.tail />
<script type="text/javascript">
  function _PageLoadCallBack(){
    document.onkeydown=function(event){
      let e = event || window.event || arguments.callee.caller.arguments[0];
      if($('#popLayer').is(':hidden') && e && e.keyCode === 13){
        USER_OPERATOR.login();
      }
    };
  }

  $(".language-dropdown").click(function () {
    let dc = $(".language-menu");
    if(dc.css('display') === 'none'){
      dc.show();
    }else{
      dc.hide();
    }
  });

  $('body').click(function (e){
    let dc = $(".language-div");
    if (!dc.is(e.target) && dc.has(e.target).length === 0) {
      $(".language-menu").hide();
    }
  })

  let lang = getLanguage();
  if(lang === 'en'){
    $("#language-div").html("English");
  }else{
    $("#language-div").html("简体中文");
  }
  function changeLanguage(lang){
    COOKIE.setCookie("ldp_language",lang,90);
    window.location.reload();
  }
</script>
</body>
</html>