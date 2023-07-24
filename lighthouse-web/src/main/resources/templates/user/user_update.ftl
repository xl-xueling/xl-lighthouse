<html lang="UTF-8">
<head>
  <#import "../common/frame.ftl" as baseFrame>
  <title class="i18n" i18n_code="ldp_i18n_user_update_1000"></title>
  <@baseFrame.top />
</head>
<body class="skin-blue sidebar-mini sidebar-collapse">
<div class="wrapper">
  <@baseFrame.header />
  <@baseFrame.leftMenu />
  <@baseFrame.rightMenu />
  <div class="content-wrapper">
    <section class="content-header">
      <h1>
        <samp class="i18n" i18n_code="ldp_i18n_user_update_1001"></samp>
      </h1>
      <ol class="breadcrumb">
        <li><a href="/index.shtml"><i class="fa fa-dashboard"></i>Home</a></li>
        <li class="active"><samp class="i18n" i18n_code="ldp_i18n_user_update_1001"></samp></li>
      </ol>
    </section>
    <section class="content">
      <div class="row">
        <div class="col-md-6">
          <div class="box">
          <div class="register-box-body">
            <div class="form-group">
              <label for="userName"><samp class="i18n" i18n_code="ldp_i18n_user_update_1002"></samp>：</label>
              <input type="hidden" class="form-control" id="id" value="${userEntity.id}">
                <#if Session["user"].userName == 'admin'>
                  <input type="text" class="form-control" id="userName"  disabled autocomplete="off" value="${userEntity.userName}">
                <#else>
                  <input type="text" class="form-control" id="userName" autocomplete="off" value="${userEntity.userName}">
                </#if>
            </div>
            <div class="form-group">
              <label for="phone"><samp class="i18n" i18n_code="ldp_i18n_user_update_1003"></samp>：</label>
              <input type="email" class="form-control" id="phone" autocomplete="off" value="${userEntity.phone}">
            </div>
            <div class="form-group">
              <label for="email"><samp class="i18n" i18n_code="ldp_i18n_user_update_1004"></samp>：</label>
              <input type="email" class="form-control" id="email" autocomplete="off" value="${userEntity.email}">
            </div>
            <#if Session["user"].userName != 'admin'>
                <div class="form-group">
                  <label for="department_id"><samp class="i18n" i18n_code="ldp_i18n_user_update_1005"></samp>：</label>
                  <select class="form-control select2 select2-hidden-accessible" id="department_id" style="width: 100%;" tabindex="-1" aria-hidden="true">
                    <option selected="selected" value="-1" class="i18n" i18n_code="ldp_i18n_user_update_1006"></option>
                    <#if departmentList??>
                      <#list departmentList as department>
                          <#if userEntity.departmentId == department.id>
                            <option value="${department.id}" selected>${department.fullPathName}</option>
                          <#else>
                            <option value="${department.id}">${department.fullPathName}</option>
                          </#if>
                      </#list>
                    </#if>
                  </select>
                </div>
              </#if>
            <div class="row">
              <div class="col-xs-12" style="text-align: center;vertical-align: middle;">
                <a href="javascript:USER_OPERATOR.updateUser();">
                  <button  class="btn btn-primary" style="float: right;line-height: 1.42857;vertical-align: middle;"><samp class="i18n" i18n_code="ldp_i18n_user_update_1007"></samp></button>
                </a>
              </div>
            </div>
          </div>
          </div>
        </div>
      </div>
    </section>
  </div>
  <@baseFrame.footer />
</div>
<@baseFrame.tail />
<script src="/static/js/user/user_operator.js" charset="utf-8"></script>
</body>
</html>
