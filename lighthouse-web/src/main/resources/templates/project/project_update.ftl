<html lang="UTF-8">
<head>
  <#import "../common/frame.ftl" as baseFrame>
  <title class="i18n">i18n(ldp_i18n_project_update_1000)</title>
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
        <samp class="i18n" i18n_code="ldp_i18n_project_update_1001"></samp>
      </h1>
      <ol class="breadcrumb">
        <li><a href="/index.shtml"><i class="fa fa-dashboard"></i>Home</a></li>
        <li><a href="/project/list.shtml"><samp class="i18n" i18n_code="ldp_i18n_project_update_1002"></samp></a></li>
        <li class="active"><samp class="i18n" i18n_code="ldp_i18n_project_update_1001"></samp></li>
      </ol>
    </section>
    <section class="content">
      <div class="row">
        <div class="col-md-6">
          <div class="box">
            <div class="register-box-body">
              <div class="form-group">
                <label for="name"><samp class="i18n" i18n_code="ldp_i18n_project_update_1003"></samp></label>
                <input type="hidden" class="form-control" value="${projectEntity.id}" id="id"/>
                <input type="text" class="form-control" value="${projectEntity.name}" id="name" placeholder="ProjectName"/>
              </div>
              <div class="form-group">
                <label for="departmentId"><samp class="i18n" i18n_code="ldp_i18n_project_update_1005"></samp></label>
                <select id="departmentId" autocomplete="off" class="form-control select2 select2-hidden-accessible" style="width: 100%;" tabindex="-1" aria-hidden="true">
                  <#if departmentList??>
                    <#list departmentList as department>
                      <#if projectEntity.departmentId == department.id>
                        <option value="${department.id}" selected>${department.fullPathName}</option>
                      <#else>
                        <option value="${department.id}">${department.fullPathName}</option>
                      </#if>
                    </#list>
                  </#if>
                </select>
              </div>
              <div class="form-group">
                <label for="admins"><samp class="i18n" i18n_code="ldp_i18n_project_update_1006"></samp></label>
                <select class="form-control select2 total-users" multiple="multiple" id="admins" data-placeholder="Select a State" style="width: 100%;">
                  <#if adminsList??>
                    <#list adminsList as admin>
                      <option value="${admin.id}" selected>${admin.userName}</option>
                    </#list>
                  </#if>
                </select>
              </div>
              <div class="form-group">
                <label for="desc"><samp class="i18n" i18n_code="ldp_i18n_project_update_1008"></samp></label>
                <textarea style="width: 100%;" rows="3" spellcheck="false" id="desc">${projectEntity.desc}</textarea>
              </div>
              <div class="row">
                <div class="col-xs-12" style="text-align: left;vertical-align: middle;">
                  <label for="privateFlag"><samp class="i18n" i18n_code="ldp_i18n_project_update_1009"></samp></label>
                  <label>
                    <input type="checkbox" class="minimal" <#if projectEntity.privateFlag == 1>checked</#if> id="privateFlag">
                  </label>
                  <a href="javascript:PROJECT_OPERATOR.updateProject();">
                    <button  class="btn btn-primary" style="float: right;line-height: 1.42857;vertical-align: middle;"><samp class="i18n" i18n_code="ldp_i18n_project_update_1010"></samp></button>
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
