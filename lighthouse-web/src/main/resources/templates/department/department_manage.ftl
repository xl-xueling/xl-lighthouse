<html lang="UTF-8">
<head>
  <#import "../common/frame.ftl" as baseFrame>
  <title class="i18n">i18n(ldp_i18n_department_manage_1000)</title>
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
        <samp class="i18n" i18n_code="ldp_i18n_department_manage_1002"></samp>
      </h1>
      <ol class="breadcrumb">
        <li><a href="/index.shtml"><i class="fa fa-dashboard"></i>Home</a></li>
        <li><a href="/department/list.shtml"><samp class="i18n" i18n_code="ldp_i18n_department_manage_1001"></samp></a></li>
        <li class="active"><samp class="i18n" i18n_code="ldp_i18n_department_manage_1002"></samp></li>
      </ol>
    </section>
    <section class="content">
      <div class="row">
        <div class="col-md-3" style="width: 20%;">
          <a href="#" class="btn btn-primary btn-block margin-bottom"><samp class="i18n" style="font-size: 10px;" i18n_code="ldp_i18n_department_manage_1003"></samp></a>
          <div class="box box-solid">
            <div class="zTreeDemoBackground left">
              <ul id="departmentTree" class="ztree"></ul>
            </div>
          </div>
        </div>
        <div class="col-md-9" style="width: 80%;">
          <div class="nav-tabs-custom">
          </div>
        </div>
      </div>
    </section>
  </div>
  <@baseFrame.footer />
</div>
<@baseFrame.tail />
<script type="text/javascript" src="/static/js/department/department_operator.js" charset="utf-8"></script>
<SCRIPT type="text/javascript">
  function _PageLoadCallBack(){
    let setting = {
      data: {
        simpleData: {
          enable: true
        }
      },
      callback:{
        onClick:DEPARTMENT_OPERATOR.treeClick
      }
    };
    let zNodes = I18N.translate(JSON.stringify(${zNodeData}));
    $.fn.zTree.init(jQuery("#departmentTree"), setting, JSON.parse(zNodes));
  }
</SCRIPT>
</body>
</html>
