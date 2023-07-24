<html lang="UTF-8">
<head>
  <#import "../common/frame.ftl" as baseFrame>
  <title class="i18n" i18n_code="ldp_i18n_change_password_1000"></title>
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
        <samp class="i18n" i18n_code="ldp_i18n_change_password_1001"></samp>
      </h1>
      <ol class="breadcrumb">
        <li><a href="/index.shtml"><i class="fa fa-dashboard"></i>Home</a></li>
        <li class="active"><samp class="i18n" i18n_code="ldp_i18n_change_password_1001"></samp></li>
      </ol>
    </section>
    <section class="content">
      <div class="row">
        <div class="col-md-6">
          <div class="box">
          <div class="register-box-body">
            <div class="form-group">
              <label for="password"><samp class="i18n" i18n_code="ldp_i18n_change_password_1002"></samp>：</label>
              <input type="hidden" class="form-control" id="id" value="${Session["user"].id}">
              <input type="password" class="form-control" id="password" autocomplete = "off">
            </div>
            <div class="form-group">
              <label for="new_password"><samp class="i18n" i18n_code="ldp_i18n_change_password_1003"></samp>：</label>
              <input type="password" class="form-control" id="new_password" autocomplete = "off">
            </div>
            <div class="form-group">
              <label for="repeat_password"><samp class="i18n" i18n_code="ldp_i18n_change_password_1004"></samp>：</label>
              <input type="password" class="form-control" id="repeat_password" autocomplete = "off">
            </div>
            <div class="row">
              <div class="col-xs-12" style="text-align: center;vertical-align: middle;">
                <a href="javascript:USER_OPERATOR.changePassword();">
                  <button  class="btn btn-primary" style="float: right;line-height: 1.42857;vertical-align: middle;"><samp class="i18n" i18n_code="ldp_i18n_change_password_1005"></samp></button>
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
