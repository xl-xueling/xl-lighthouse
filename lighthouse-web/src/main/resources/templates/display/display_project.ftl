<html lang="UTF-8">
<head>
    <#import "../common/frame.ftl" as baseFrame>
    <title class="i18n">i18n(ldp_i18n_display_project_1000,${projectEntity.name})</title>
    <@baseFrame.top />
</head>
<body class="skin-blue sidebar-mini sidebar-collapse">
<div class="wrapper">
    <@baseFrame.header />
    <@baseFrame.leftMenu />
    <@baseFrame.rightMenu />
    <div class="content-wrapper">
        <section class="content-header">
            <h1 style="font-size: 14px;">
                <samp class="i18n" i18n_code="ldp_i18n_display_project_1001"></samp>&nbsp;${projectEntity.name}
            </h1>
            <ol class="breadcrumb">
                <li><a href="/index.shtml"><i class="fa fa-dashboard"></i>Home</a></li>
                <li class="active"><samp class="i18n" i18n_code="ldp_i18n_display_project_1002"></samp></li>
            </ol>
        </section>
        <section class="content">
            <div class="row">
                <div class="col-md-3" style="width: 20%;padding-left:10px;padding-right: 8px;">
                    <a href="#" class="btn btn-primary btn-block margin-bottom"><samp class="i18n" i18n_code="ldp_i18n_display_project_1003"></samp> </a>
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
<script type="text/javascript" src="/static/js/render/render.js" charset="utf-8"></script>
<script type="text/javascript" src="/static/js/render/base_options.js" charset="utf-8"></script>
<script type="text/javascript" src="/static/js/render/table_options.js" charset="utf-8"></script>
<script type="text/javascript" src="/static/js/display/display_operator.js" charset="utf-8"></script>
<SCRIPT type="text/javascript">
    function _PageLoadCallBack(){
        let zNodes = ${zNodeData};
        let zNodesI18N = I18N.translate(JSON.stringify(zNodes));
        let setting = {
            data: {
                simpleData: {
                    enable: true
                }
            },
            callback:{
                onClick:DISPLAY_OPERATOR.DisplayProjectTreeClick
            }
        };
        jQuery(document).ready(function(){
            $.fn.zTree.init(jQuery("#projectTree"), setting, JSON.parse(zNodesI18N));
        });
    }
</SCRIPT>
</body>
</html>
