<html lang="UTF-8">
<head>
  <#import "../common/frame.ftl" as baseFrame>
  <title class="i18n" i18n_code="ldp_i18n_user_register_1000"></title>
  <@baseFrame.top />
</head>
<body class="hold-transition register-page" style="overflow-y: hidden;overflow-x: hidden;">
<div class="register-box">
  <div class="register-logo">
    <a href="/index.shtml">LightHouse</a>
  </div>
  <div class="register-box-body">
    <label>
      <input type="text" name="name" value="admin" style="position: absolute;z-index: -1;" disabled autocomplete = "off"/>
    </label>
    <label>
      <input type="password" name="pwd" value=" " style="position: absolute;z-index: -1;" disabled autocomplete = "off"/>
    </label>
    <p class="login-box-msg"><samp class="i18n" i18n_code="ldp_i18n_user_register_1001"></samp></p>
      <div class="form-group has-feedback">
        <label for="userName"></label>
        <input type="text" class="form-control i18n" autocomplete="off" readonly onfocus="this.removeAttribute('readonly');" onblur="this.setAttribute('readonly',true);" id="userName" name="name" placeholder="i18n(ldp_i18n_user_register_1002)" value="">
      </div>
    <div class="form-group has-feedback">
      <label for="password"></label>
      <input type="password" class="form-control i18n" autocomplete="off" readonly onfocus="this.removeAttribute('readonly');" onblur="this.setAttribute('readonly',true);" id="password" name ="pwd" placeholder="i18n(ldp_i18n_user_register_1003)" value="" >
    </div>
    <div class="form-group has-feedback">
      <label for="phone"></label>
      <input type="text" class="form-control i18n" autocomplete="off" readonly onfocus="this.removeAttribute('readonly');" onblur="this.setAttribute('readonly',true);" id="phone" placeholder="i18n(ldp_i18n_user_register_1004)">
    </div>
    <div class="form-group has-feedback">
      <label for="email"></label>
      <input type="email" class="form-control i18n" autocomplete="off" readonly onfocus="this.removeAttribute('readonly');" onblur="this.setAttribute('readonly',true);" id="email" placeholder="i18n(ldp_i18n_user_register_1005)">
    </div>
    <div class="form-group has-feedback">
      <label for="department_id"></label>
      <select class="form-control select2 select2-accessible" id="department_id" readonly onfocus="this.removeAttribute('readonly');" onblur="this.setAttribute('readonly',true);" style="width: 100%;" tabindex="-1" aria-hidden="true">
        <option selected="selected" value="-1" class="i18n" i18n_code="ldp_i18n_user_register_1006"></option>
        <#if departmentList??>
          <#list departmentList as department>
            <option value="${department.id}">${department.fullPathName}</option>
          </#list>
        </#if>
      </select>
    </div>
    <div class="row">
      <div class="col-xs-12" style="text-align: center;vertical-align: middle;">
      <a href="javascript:USER_OPERATOR.register();">
        <button  class="btn btn-primary" style="float: right;line-height: 1.42857;vertical-align: middle;"><samp class="i18n" i18n_code="ldp_i18n_user_register_1007"></samp></button>
      </a>
      </div>
    </div>
  </div>
</div>
<script src="/static/js/user/user_operator.js" charset="utf-8"></script>
<@baseFrame.tail />
</body>
</html>