<html lang="UTF-8">
<head>
  <#import "../common/frame.ftl" as baseFrame>
  <title class="i18n">i18n(ldp_i18n_components_create_1000)</title>
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
        <samp class="i18n" i18n_code="ldp_i18n_components_create_1001"></samp>
      </h1>
      <ol class="breadcrumb">
        <li><a href="/index.shtml"><i class="fa fa-dashboard"></i>Home</a></li>
        <li class="active"><samp class="i18n" i18n_code="ldp_i18n_components_create_1001"></samp></li>
      </ol>
    </section>
    <section class="content">
      <div class="row">
        <div class="col-md-8">
          <div class="box">
            <div class="register-box-body">
              <div class="form-group">
                <label for="components_title">Title:</label>
                <input type="text" class="form-control" id="components_title">
              </div>
              <div class="form-group">
                <label for="components_type">Type:</label>
                <select id="components_type" class="select2 select2-hidden-accessible" style="width: 100%;" tabindex="-1" aria-hidden="true">
                  <option value="1" class="i18n" i18n_code="ldp_i18n_components_common_1002"></option>
                  <option value="2" selected class="i18n" i18n_code="ldp_i18n_components_common_1003"></option>
                  <option value="3" class="i18n" i18n_code="ldp_i18n_components_common_1004"></option>
                  <option value="4" class="i18n" i18n_code="ldp_i18n_components_common_1005"></option>
                </select>
              </div>
              <div class="form-group">
                <label for="components_data">Configuration:</label><span onclick="COMPONENTS_OPERATOR.checkComponentsData();" style="float: right;cursor:pointer;" class="badge bg-yellow">
                <samp class="i18n" i18n_code="ldp_i18n_components_create_1006"></samp>
              </span>
                <form class="layui-form" method="post">
                  <div class="layui-form-item">
                    <textarea id="components_data" style="width: 100%;" rows="16" spellcheck="false"></textarea>
                  </div>
                </form>
              </div>
              <div class="form-group components_display_group" style="display: none;">
                <label for="components_display">Display:</label>
                <form class="layui-form" method="post">
                  <div class="layui-form-item">
                    <div class="filter_components" id="components_display"></div>
                  </div>
                </form>
              </div>
              <div class="form-group">
                <label for="privateFlag"><samp class="i18n" i18n_code="ldp_i18n_components_create_1007"></samp>:</label>
                <label>
                  <input type="checkbox" class="minimal" checked id="privateFlag">
                </label>
              </div>
              <div class="row">
                <div class="col-xs-12" style="text-align: center;vertical-align: middle;">
                  <a href="javascript:COMPONENTS_OPERATOR.registerComponents();">
                    <button  class="btn btn-primary" style="float: right;line-height: 1.42857;vertical-align: middle;">
                      <samp class="i18n" i18n_code="ldp_i18n_components_create_1008"></samp>
                    </button>
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
<script type="text/javascript" src="/static/js/render/render.js" charset="utf-8"></script>
<script type="text/javascript" src="/static/js/render/base_options.js" charset="utf-8"></script>
<script type="text/javascript" src="/static/js/render/table_options.js" charset="utf-8"></script>
<script type="text/javascript" src="/static/js/components/components_operator.js" charset="utf-8"></script>
<script type="text/javascript" src="/static/js/filter/filter_operator.js" charset="utf-8"></script>
</body>
</html>
