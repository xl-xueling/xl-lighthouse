<html lang="UTF-8">
<head>
  <#import "../common/frame.ftl" as baseFrame>
  <title class="i18n">i18n(ldp_i18n_components_update_1000)</title>
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
        <samp class="i18n" i18n_code="ldp_i18n_components_update_1001"></samp>
      </h1>
      <ol class="breadcrumb">
        <li><a href="/index.shtml"><i class="fa fa-dashboard"></i>Home</a></li>
        <li class="active"><samp class="i18n" i18n_code="ldp_i18n_components_update_1001"></samp></li>
      </ol>
    </section>
    <section class="content">
      <div class="row">
        <div class="col-md-8">
          <div class="box">
          <div class="register-box-body">
            <div class="form-group">
              <label for="components_title">Title:</label>
              <input type="hidden" id="components_id" value="${componentsEntity.id}"/>
              <input type="text" class="form-control" id="components_title" value="${componentsEntity.title}">
            </div>
            <div class="form-group">
              <label for="components_type">Type:</label>
              <select id="components_type" class="select2 select2-hidden-accessible" style="width: 100%;" tabindex="-1" aria-hidden="true">
                <option value="1" class="i18n" i18n_code="ldp_i18n_components_update_1003" <#if componentsEntity.type == 1>selected</#if>></option>
                <option value="2" class="i18n" i18n_code="ldp_i18n_components_update_1004" <#if componentsEntity.type == 2>selected</#if>></option>
                <option value="3" class="i18n" i18n_code="ldp_i18n_components_update_1005" <#if componentsEntity.type == 3>selected</#if>></option>
                <option value="4" class="i18n" i18n_code="ldp_i18n_components_update_1006" <#if componentsEntity.type == 4>selected</#if>></option>
              </select>
            </div>
            <div class="form-group">
              <label for="components_data">Data：</label><span onclick="COMPONENTS_OPERATOR.checkComponentsData();" style="float: right;cursor:pointer;" class="badge bg-yellow">
              <samp class="i18n" i18n_code="ldp_i18n_components_update_1007"></samp>
            </span>
              <form class="layui-form" method="post">
                <div class="layui-form-item">
                  <textarea id="components_data" style="width: 100%;" rows="16" spellcheck="false">${componentsEntity.data}</textarea>
                </div>
              </form>
            </div>
            <div class="form-group components_display_group" style="display: none;">
              <label for="filter_${componentsEntity.id}">Display:</label>
              <form class="layui-form" method="post">
                <div class="layui-form-item">
                  <div class="filter_components" id="components_display"></div>
                </div>
              </form>
            </div>
            <div class="form-group">
              <label for="privateFlag"><samp class="i18n" i18n_code="ldp_i18n_components_update_1008"></samp>:</label>
              <label>
                <input type="checkbox" class="minimal" <#if componentsEntity.privateFlag == 1>checked</#if> id="privateFlag">
              </label>
            </div>
            <div class="row">
              <div class="col-xs-12" style="text-align: center;vertical-align: middle;">
                <a href="javascript:COMPONENTS_OPERATOR.updateComponents();">
                  <button  class="btn btn-primary" style="float: right;line-height: 1.42857;vertical-align: middle;"><samp class="i18n" i18n_code="ldp_i18n_components_update_1009"></samp></button>
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
<script type="text/javascript" src="/static/js/components/components_operator.js" charset="utf-8"></script>
<script type="text/javascript" src="/static/js/filter/filter_operator.js"></script>
</body>
</html>
