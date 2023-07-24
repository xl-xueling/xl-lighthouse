<html lang="UTF-8">
<head>
  <#import "../common/frame.ftl" as baseFrame>
  <title class="i18n">i18n(ldp_i18n_project_manage_1000)</title>
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
        <samp class="i18n" i18n_code="ldp_i18n_project_manage_1001"></samp>
      </h1>
      <ol class="breadcrumb">
        <li><a href="/index.shtml"><i class="fa fa-dashboard"></i>Home</a></li>
        <li class="active"><samp class="i18n" i18n_code="ldp_i18n_project_manage_1001"></samp></li>
      </ol>
    </section>
    <section class="content">
      <div class="row">
        <div class="col-md-3" style="width: 20%;padding-left:10px;padding-right: 8px;">
          <a href="#" class="btn btn-primary btn-block margin-bottom"><samp class="i18n" i18n_code="ldp_i18n_project_manage_1002" style="font-size: 9px;"></samp></a>
          <div class="box box-solid">
            <div class="zTreeDemoBackground left">
              <ul id="projectTree" class="ztree i18n" style="min-height: 420px;"></ul>
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
  <@baseFrame.footer />
</div>
<@baseFrame.tail />
<script type="text/javascript" src="/static/js/group/group_operator.js"></script>
<script type="text/javascript" src="/static/js/project/project_operator.js"></script>
<script type="text/javascript" src="/static/js/display/display_operator.js"></script>
<SCRIPT type="text/javascript">
  function _PageLoadCallBack(){
    let setting = {
      data: {
        simpleData: {
          enable: true
        }
      },
      callback:{
        onClick:PROJECT_OPERATOR.treeClick
      }
    };
    let zNodesI18N = I18N.translate(JSON.stringify(${zNodeData}));
    let zTree = $.fn.zTree.init(jQuery("#projectTree"), setting, JSON.parse(zNodesI18N));
    let groupId = ${groupId};
    if(groupId !== -1){
      let treeNode=zTree.getNodeByParam("id", ${groupId})
      zTree.selectNode(treeNode);
      let url = "/group/manage/index.shtml?isSub=1&id="+${groupId};
      MsgBox.subRedirect("#sub_nav",url);
    }
  }
</SCRIPT>
</body>
</html>
