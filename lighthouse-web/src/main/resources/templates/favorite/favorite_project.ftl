<html lang="UTF-8">
<head>
  <#import "../common/frame.ftl" as baseFrame>
  <title class="i18n">i18n(ldp_i18n_favorite_project_1000)</title>
  <@baseFrame.top />
</head>
<body class="skin-blue sidebar-mini sidebar-collapse">
<div class="wrapper">
  <@baseFrame.header />
  <@baseFrame.leftMenu />
  <@baseFrame.rightMenu />
    <#if isEmpty?? && isEmpty?string("true","false") == "true">
      <div class="content-wrapper" style="padding-top:30px;">
        <section class="content">
          <div class="error-page">
            <div class="">
              <h5>&nbsp;&nbsp;
                <samp class="i18n" i18n_code="ldp_i18n_favorite_project_1001"></samp>
                <a href="${link}"><span style="color: indianred"><i class="fa fa-fw fa-plus-circle"></i></span></a>
              </h5>
            </div>
          </div>
        </section>
      </div>
    <#else>
      <div class="content-wrapper">
        <section class="content-header">
          <h1 style="font-size: 14px;">
            <samp class="i18n" i18n_code="ldp_i18n_favorite_project_1002"></samp>
          </h1>
          <ol class="breadcrumb">
            <li><a href="/index.shtml"><i class="fa fa-dashboard"></i>Home</a></li>
            <li class="active"><samp class="i18n" i18n_code="ldp_i18n_favorite_project_1002"></samp></li>
          </ol>
        </section>
        <section class="content">
          <div class="row">
            <div class="col-md-3" style="width: 20%;padding-left:10px;padding-right: 8px;">
              <a href="#" class="btn btn-primary btn-block margin-bottom">
                <samp class="i18n" i18n_code="ldp_i18n_favorite_project_1003"></samp>
              </a>
              <div class="box box-solid">
                <div class="zTreeDemoBackground left">
                  <ul id="favoriteTree" class="ztree" style="min-height: 420px;"></ul>
                </div>
              </div>
            </div>
            <div class="col-md-9" style="width: 80%;padding-left:8px;padding-right: 15px;">
              <div id="sub_nav">
              </div>
            </div>
          </div>
        </section>
      </div>
    </#if>
  <@baseFrame.footer />
</div>
<@baseFrame.tail />
<script src="/static/js/favorites/favorites_operator.js" charset="utf-8"></script>
<SCRIPT type="text/javascript">
  function _PageLoadCallBack(){
    let setting = {
      data: {
        simpleData: {
          enable: true
        }
      },
      callback:{
        onClick:FAVORITES_OPERATOR.projectClick
      }
    };
    let zNodes = I18N.translate(JSON.stringify(${zNodeData}));
    jQuery(document).ready(function(){
      $.fn.zTree.init(jQuery("#favoriteTree"), setting, JSON.parse(zNodes));
    });
  }
</SCRIPT>
</body>
</html>
