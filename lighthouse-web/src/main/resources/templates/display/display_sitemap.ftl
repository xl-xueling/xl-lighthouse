<html lang="UTF-8">
<head>
    <#import "../common/frame.ftl" as baseFrame>
    <title class="i18n">i18n(ldp_i18n_display_sitemap_1000,${siteMapEntity.name})</title>
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
                <samp class="i18n" i18n_code="ldp_i18n_display_sitemap_1001"></samp>&nbsp;${siteMapEntity.name}
            </h1>
            <ol class="breadcrumb">
                <li><a href="/index.shtml"><i class="fa fa-dashboard"></i>Home</a></li>
                <li class="active"><samp class="i18n" i18n_code="ldp_i18n_display_sitemap_1002"></samp></li>
            </ol>
        </section>
        <section class="content">
            <div class="row">
                <div class="col-md-3" style="width: 20%;padding-left:10px;padding-right: 8px;">
                    <a href="#" class="btn btn-primary btn-block margin-bottom"><samp class="i18n" i18n_code="ldp_i18n_display_sitemap_1003"></samp></a>
                    <div class="box box-solid">
                        <div class="zTreeDemoBackground left">
                            <ul id="departmentTree" class="ztree" style="min-height: 420px;"></ul>
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
<script type="text/javascript" src="/static/js/sitemap/sitemap_operator.js"></script>
<SCRIPT type="text/javascript">
    let siteId = '${siteMapEntity.id}';
    var setting = {
        data: {
            simpleData: {
                enable: true
            }
        },
        callback:{
            onClick:SITEMAP_OPERATOR.itemClick
        }
    };
    let zNodes = ${zNodeData};
    let zNodesI18N = I18N.translate(JSON.stringify(zNodes));
    jQuery(document).ready(function(){
        $.fn.zTree.init(jQuery("#departmentTree"), setting, JSON.parse(zNodesI18N));
    });
</SCRIPT>
</body>
</html>
