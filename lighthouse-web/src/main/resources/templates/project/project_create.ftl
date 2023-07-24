<html lang="UTF-8">
<head>
  <#import "../common/frame.ftl" as baseFrame>
  <title class="i18n">i18n(ldp_i18n_project_create_1000)</title>
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
        <samp class="i18n" i18n_code="ldp_i18n_project_create_1001"></samp>
      </h1>
      <ol class="breadcrumb">
        <li><a href="/index.shtml"><i class="fa fa-dashboard"></i>Home</a></li>
        <li><a href="/project/list.shtml"><samp class="i18n" i18n_code="ldp_i18n_project_create_1002"></samp></a></li>
        <li class="active"><samp class="i18n" i18n_code="ldp_i18n_project_create_1001"></samp></li>
      </ol>
    </section>
    <section class="content">
      <div class="row">
        <div class="col-md-8">
          <div class="box">
            <div class="register-box-body">
              <div class="form-group">
                <label for="name"><samp class="i18n" i18n_code="ldp_i18n_project_create_1003"></samp></label>
                <input type="text" class="form-control" autocomplete="off" id="name" placeholder="Project Name">
              </div>
              <div class="form-group">
                <label for="departmentId"><samp class="i18n" i18n_code="ldp_i18n_project_create_1005"></samp></label>
                <select id="departmentId" autocomplete="off" class="form-control select2 select2-hidden-accessible" style="width: 100%;" tabindex="-1" aria-hidden="true">
                  <#if departmentList??>
                    <#list departmentList as department>
                      <#if department.id == departmentId>
                        <option selected value="${department.id}">${department.fullPathName}</option>
                      <#else>
                        <option value="${department.id}">${department.fullPathName}</option>
                      </#if>
                    </#list>
                  </#if>
                </select>

              </div>
              <div class="form-group">
                <label for="admins"><samp class="i18n" i18n_code="ldp_i18n_project_create_1006"></samp></label>
                <select class="form-control select2 total-users" multiple="multiple" id="admins" data-placeholder="Select User" style="width: 100%;">
                </select>
              </div>

              <div class="form-group">
                <label for="name"><samp class="i18n" i18n_code="ldp_i18n_project_create_1008"></samp></label>
                <label for="desc"></label>
                <textarea style="width: 100%;" spellcheck="false" rows="3" id="desc"></textarea>
              </div>
              <div class="form-group">
              </div>
              <div class="row">
                <div class="col-xs-12" style="text-align: left;vertical-align: middle;">
                  <label for="privateFlag"><samp class="i18n" i18n_code="ldp_i18n_project_create_1009"></samp></label>
                  <label>
                    <input type="checkbox" class="minimal" id="privateFlag">
                  </label>
                  <a href="javascript:PROJECT_OPERATOR.createProject();">
                    <button  class="btn btn-primary" style="float: right;line-height: 1.42857;vertical-align: middle;"><samp class="i18n" i18n_code="ldp_i18n_project_create_1010"></samp></button>
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
<script src="/static/js/project/project_operator.js" charset="utf-8"></script>
</body>
</html>
