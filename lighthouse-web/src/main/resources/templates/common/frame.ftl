<#macro header>
<#setting classic_compatible=true>
<header class="main-header">
  <a href="/index.shtml" class="logo">
    <span class="logo-mini">LDP</span>
    <span class="logo-lg" style="font-size: 12px;"><b><samp class="i18n" i18n_code="ldp_i18n_header_1000"></samp></b></span>
  </a>
  <nav class="navbar navbar-static-top">
    <div class="navbar-custom-menu">
      <ul class="nav navbar-nav">
        <li class="dropdown user user-menu" id="userInfo">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown">
            <i class="fa fa-user"></i>
          </a>
          <ul class="dropdown-menu" id="userInfoPop">
            <li class="user-header">
              <img src="/static/dist/img/1.png" class="img-circle" alt="User Image">
              <p></p>
              <span style="color:#fff8f8"> ${Session["user"].userName}</span>
            </li>
            <li class="user-footer">
              <div class="pull-left">
                <a href="/user/update/index.shtml?id=${Session["user"].id}" class="btn btn-default btn-flat" style="font-size: 11px;"><samp class="i18n" i18n_code="ldp_i18n_header_1001"></samp></a>
              </div>
              <div class="pull-left" style="text-align:center;margin-left:35px;">
                <a href="/user/changepwd/index.shtml" class="btn btn-default btn-flat" style="font-size: 11px;"><samp class="i18n" i18n_code="ldp_i18n_header_1002"></samp></a>
              </div>
              <div class="pull-right">
                <a href="/login/signout.shtml" class="btn btn-default btn-flat" style="font-size: 11px;"><samp class="i18n" i18n_code="ldp_i18n_header_1003"></samp></a>
              </div>
            </li>
          </ul>
        </li>
        <li>
          <a href="#" data-toggle="control-sidebar"><i class="fa fa-gears"></i></a>
        </li>
      </ul>
    </div>
  </nav>
</header>
</#macro>

<#macro rightMenu>
<aside class="control-sidebar control-sidebar-dark" id="side-menu" style="min-height:500px;">
  <div class="tab-content" >
    <div class="tab-pane active" id="control-sidebar-tab_3">
      <h3 class="control-sidebar-heading"><samp class="i18n" i18n_code="ldp_i18n_right_menu_1000"></samp></h3>
      <ul class="control-sidebar-menu">
        <li>
          <a href="/project/list.shtml?owner=1">
            <i class="menu-icon fa fa-plus-square-o bg-green"></i>
            <div class="menu-info">
              <h4 class="control-sidebar-subheading"><samp class="i18n" i18n_code="ldp_i18n_right_menu_1001"></samp></h4>
            </div>
          </a>
        </li>
      </ul>
      <h3 class="control-sidebar-heading"><samp class="i18n" i18n_code="ldp_i18n_right_menu_1002"></samp></h3>
      <ul class="control-sidebar-menu">
        <li>
          <a href="/approve/list.shtml">
            <i class="menu-icon fa fa-plus-square-o bg-green"></i>
            <div class="menu-info">
              <h4 class="control-sidebar-subheading"><samp class="i18n" i18n_code="ldp_i18n_right_menu_1012"></samp></h4>
            </div>
          </a>
        </li>
        <li>
          <a href="/apply/list.shtml">
            <i class="menu-icon fa fa-plus-square-o bg-green"></i>
            <div class="menu-info">
              <h4 class="control-sidebar-subheading"><samp class="i18n" i18n_code="ldp_i18n_right_menu_1011"></samp></h4>
            </div>
          </a>
        </li>
      </ul>
      <h3 class="control-sidebar-heading"><samp class="i18n" i18n_code="ldp_i18n_right_menu_1004"></samp></h3>
      <ul class="control-sidebar-menu">
        <li>
          <a href="/components/list.shtml">
            <i class="menu-icon fa fa-plus-square-o bg-green"></i>
            <div class="menu-info">
              <h4 class="control-sidebar-subheading"><samp class="i18n" i18n_code="ldp_i18n_right_menu_1005"></samp></h4>
            </div>
          </a>
        </li>
        <li>
          <a href="/display/project.shtml?projectId=1">
            <i class="menu-icon fa fa-plus-square-o bg-green"></i>
            <div class="menu-info">
              <h4 class="control-sidebar-subheading"><samp class="i18n" i18n_code="ldp_i18n_right_menu_1013"></samp></h4>
            </div>
          </a>
        </li>
      </ul>
    </div>
    <#if Session["user"].userName == 'admin'>
      <h3 class="control-sidebar-heading"><samp class="i18n" i18n_code="ldp_i18n_right_menu_1006"></samp></h3>
      <ul class="control-sidebar-menu">
        <li>
          <a href="/department/list.shtml">
            <i class="menu-icon fa fa-plus-square-o bg-green"></i>
            <div class="menu-info">
              <h4 class="control-sidebar-subheading"><samp class="i18n" i18n_code="ldp_i18n_right_menu_1007"></samp></h4>
            </div>
          </a>
        </li>
        <li>
          <a href="/user/list.shtml">
            <i class="menu-icon fa fa-plus-square-o bg-green"></i>
            <div class="menu-info">
              <h4 class="control-sidebar-subheading"><samp class="i18n" i18n_code="ldp_i18n_right_menu_1008"></samp></h4>
            </div>
          </a>
        </li>
      </ul>
    </#if>
  </div>
</aside>
</#macro>

<#macro leftMenu>
<aside class="main-sidebar">
  <section class="sidebar">
    <ul class="sidebar-menu">
      <li class="treeview">
        <a href="#">
          <i class="fa fa-cubes" style="font-size: 16px;"></i>
          <span><samp class="i18n" i18n_code="ldp_i18n_left_menu_1003"></samp></span>
        </a>
        <ul class="treeview-menu" style="margin-top: -4px;">
          <li><a href="/favorite/project/list.shtml"><i class="fa fa-circle-o"></i><samp class="i18n" i18n_code="ldp_i18n_left_menu_1004"></samp></a></li>
          <li><a href="/favorite/stat/list.shtml"><i class="fa fa-circle-o"></i><samp class="i18n" i18n_code="ldp_i18n_left_menu_1005"></samp></a></li>
        </ul>
      </li>

      <li class="treeview">
        <a href="#">
          <i class="fa fa-th" style="font-size: 16px;"></i>
          <span><samp class="i18n" i18n_code="ldp_i18n_left_menu_1006"></samp></span>
        </a>
        <ul class="treeview-menu" style="margin-top: -4px;">
          <li><a href="/project/list.shtml"><i class="fa fa-circle-o"></i><samp class="i18n" i18n_code="ldp_i18n_left_menu_1007"></samp></a></li>
          <li><a href="/stat/list.shtml"><i class="fa fa-circle-o"></i><samp class="i18n" i18n_code="ldp_i18n_left_menu_1008"></samp></a></li>
        </ul>
      </li>
    </ul>
  </section>
</aside>
  <script type="text/javascript">
    $.ajax({
      type: "POST",
      url: Encrypt.encryptUrl("/menu/custominfo.shtml"),
      success: function (data) {
        if (data.code === '0') {
          let siteMapMenuData = data.data['sitemap'];
          if(!Validate.isNull(siteMapMenuData)){
            for(let n=0; n<siteMapMenuData.length; n++){
              let html = "<li><a href=\""+siteMapMenuData[n].link+"\"><i class=\"fa fa-circle-o\"></i><samp class='i18n'>"+siteMapMenuData[n].name+"</samp></a></li>"
              $(".sitemap-menu").append(html);
            }
          }
        }
      }
    });
  </script>
</#macro>

<#macro footer>
<footer class="main-footer">
  <div class="pull-right hidden-xs">
    <span style="font-size: 12px;text-align: right;">Version：${_ldp_version}</span>
  </div>
  <strong style="font-size: 12px;">Copyright &copy; 2022-${.now?string("yyyy")} XueLing. &nbsp;&nbsp;
    <a style="color: #333;" target="_self" href="/license.shtml">XL-LightHouse&nbsp;&nbsp;(<samp class="i18n" i18n_code="ldp_i18n_footer_1001"></samp>)</a></strong>
</footer>
</#macro>


<#macro paging pageEntity>
  <#assign currentPage="${pageEntity.currentPage}"/>
  <#assign totalPage="${pageEntity.totalPage}"/>
  <#assign baseUrl="${pageEntity.baseUrl}"/>
  <#assign totalSize = "${pageEntity.totalSize}"/>
  <div class="box-footer clearfix">
    <div class="pull-left">
      Total:&nbsp;${pageEntity.totalSize}
    </div>
    <#if (totalSize?number > 1)>
      <ul class="pagination pagination-sm no-margin pull-right">
        <#if (currentPage?number == 1)>
          <li><a href="#">&laquo;</a></li>
        <#else>
          <li><a href="${baseUrl?replace('#page', (currentPage?number-1)?string)}">&laquo;</a></li>
        </#if>
        <#if (totalPage?number <= 6)>
          <#list 1..totalPage?number as item>
            <li  <#if (currentPage?number == item)>class="active"</#if>><a href="${baseUrl?replace('#page', item?string)}">${item}</a></li>
          </#list>
        <#else>
          <#if (currentPage?number < 5)>
            <#list 1..currentPage?number + 2 as item>
              <#if item <= totalPage?number>
                <li <#if currentPage?number == item>class="active"</#if>><a href="${baseUrl?replace('#page', item?string)}">${item}</a></li>
              </#if>
            </#list>
            <#if (totalPage?number > currentPage?number + 2)>
              <li><span>....</span></li>
            </#if>
            <#if (totalPage?number > currentPage?number + 1)>
              <li <#if (currentPage?number == totalPage?number)>class="active"</#if>><a href="${baseUrl?replace('#page', totalPage?string)}">${totalPage}</a></li>
            </#if>
          <#else>
            <li <#if (currentPage?number == 1)>class="active"</#if>><a href="${baseUrl?replace('#page', '1')}">1</a></li>
            <li><span>....</span></li>
            <#list currentPage?number - 2..currentPage?number + 2 as item>
              <#if (item?number <= totalPage?number)>
                <li <#if (currentPage?number == item)>class="active"</#if>><a href="${baseUrl?replace('#page', item?string)}">${item}</a></li>
              </#if>
            </#list>
            <#if (totalPage?number > currentPage?number + 2)>
              <li><span>....</span></li>
            </#if>
            <#if (totalPage?number > currentPage?number + 2)>
              <li <#if (currentPage?number == totalPage?number)>class="active"</#if>><a href="${baseUrl?replace('#page', totalPage?string)}">${totalPage}</a></li>
            </#if>
          </#if>
        </#if>
          <#if (currentPage?number == totalPage?number)>
            <li><a href="${baseUrl?replace('#page', totalPage?string)}">&raquo;</a></li>
          <#else>
            <li><a href="${baseUrl?replace('#page', (currentPage?number+1)?string)}">&raquo;</a></li>
          </#if>
      </ul>
    </#if>
  </div>
</#macro>

<#macro top>
  <#setting classic_compatible=true>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <link rel="shortcut icon" href="/static/extend/ico/fav.png"/>
  <link rel="stylesheet" href="/static/plugins/daterangepicker/daterangepicker.css">
  <link rel="stylesheet" href="/static/plugins/datepicker/datepicker3.css">
  <link rel="stylesheet" href="/static/plugins/iCheck/all.css">
  <link rel="stylesheet" href="/static/plugins/colorpicker/bootstrap-colorpicker.min.css">
  <link rel="stylesheet" href="/static/plugins/timepicker/bootstrap-timepicker.min.css">
  <link rel="stylesheet" href="/static/plugins/select2/select2.min.css">
  <link rel="stylesheet" href="/static/bootstrap/css/bootstrap.min.css">
  <link rel="stylesheet" href="/static/bootstrap/css/bootstrap-switch3.min.css">
  <link rel="stylesheet" href="/static/extend/css/font-awesome.min.css">
  <link rel="stylesheet" href="/static/extend/css/ionicons.min.css">
  <link rel="stylesheet" href="/static/extend/css/common.css">
  <link rel="stylesheet" href="/static/plugins/select2/select2.min.css">
  <link rel="stylesheet" href="/static/dist/css/AdminLTE.min.css">
  <link rel="stylesheet" href="/static/dist/css/skins/_all-skins.min.css">
  <link rel="stylesheet" href="/static/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
  <link rel="stylesheet" href="/static/plugins/daterangepicker/daterangepicker.css">
  <link rel="stylesheet" href="/static/plugins/iCheck/all.css">
  <script type="text/javascript" src="/static/js/jquery-3.6.0.min.js"></script>
  <script type="text/javascript" src="/static/js/i18n/jquery.i18n.properties.min.js"></script>
  <script type="text/javascript" src="/static/js/common/aes.js"></script>
  <script type="text/javascript" src="/static/js/common/pbkdf2.js"></script>
  <script type="text/javascript" src="/static/js/common/AesUtil.js"></script>
  <script type="text/javascript" src="/static/js/common/md5.js"></script>
  <script type="text/javascript" src="/static/js/common/common.js" charset="utf-8"></script>
  <script type="text/javascript" src="/static/ztree/js/jquery.ztree.all.min.js"></script>
  <script type="text/javascript" src="/static/extend/rater/js/star-rating.min.js"></script>
  <link type="text/css" rel="stylesheet" href="/static/extend/rater/css/star-rating.min.css">
  <script type="text/javascript" src="/static/laydate/laydate.js"></script>
  <script type="text/javascript" src="/static/layui/layui.js"></script>
  <script type="text/javascript" src="/static/layui/layui.all.js"></script>
  <script type="text/javascript" src="/static/layui/xm-select.js"></script>
  <link rel="stylesheet" href="/static/dist/css/skins/_all-skins.min.css">
  <link rel="stylesheet" href="/static/layui/css/layui.css">
  <script type="text/javascript" src="/static/js/charts/dist/echarts.min.js"></script>
  <script type="text/javascript" src="/static/js/charts/themes/shine.js"></script>
  <div id="loading" style="display: none;user-select: none;"><img src="/static/dist/img/loading6.gif" alt="loading.."></div>
  <div id="popLayer" class="pop_layer" style="position:fixed;width: 100%;height: 100%;display: none;user-select: none;"></div>
</#macro>

<#macro tail>
  <#setting classic_compatible=true>
  <link rel="stylesheet" href="/static/extend/css/list.css">
  <script type="text/javascript" src="/static/bootstrap/js/bootstrap.min.js" charset="utf-8"></script>
  <script type="text/javascript" src="/static/bootstrap/js/bootstrap-switch.min.js" charset="utf-8"></script>
  <script type="text/javascript" src="/static/plugins/fastclick/fastclick.js" charset="utf-8"></script>
  <script type="text/javascript" src="/static/plugins/iCheck/icheck.min.js" charset="utf-8"></script>
  <script type="text/javascript" src="/static/plugins/slimScroll/jquery.slimscroll.min.js" charset="utf-8"></script>
  <script type="text/javascript" src="/static/plugins/input-mask/jquery.inputmask.js" charset="utf-8"></script>
  <script type="text/javascript" src="/static/plugins/input-mask/jquery.inputmask.date.extensions.js" charset="utf-8"></script>
  <script type="text/javascript" src="/static/plugins/input-mask/jquery.inputmask.extensions.js" charset="utf-8"></script>
  <script type="text/javascript" src="/static/plugins/slimScroll/jquery.slimscroll.min.js" charset="utf-8"></script>
  <script type="text/javascript" src="/static/js/render/render.js" charset="utf-8"></script>
  <script type="text/javascript" src="/static/js/render/base_options.js" charset="utf-8"></script>
  <script type="text/javascript" src="/static/js/render/table_options.js" charset="utf-8"></script>
  <script type="text/javascript" src="/static/plugins/select2/select2.full.min.js"></script>
  <script type="text/javascript" src="/static/js/common/p_event.js"></script>
  <script type="text/javascript" src="/static/js/common/footer.js"></script>
  <script type="text/javascript" src="/static/dist/js/app.js"></script>
</#macro>

<#macro sub_head>
  <#setting classic_compatible=true>
</#macro>
<#macro sub_tail>
  <#setting classic_compatible=true>
<script type="text/javascript" src="/static/bootstrap/js/bootstrap.min.js" charset="utf-8"></script>
<script type="text/javascript" src="/static/plugins/iCheck/icheck.min.js" charset="utf-8" ></script>
<script type="text/javascript" src="/static/plugins/input-mask/jquery.inputmask.js" charset="utf-8" ></script>
<script type="text/javascript" src="/static/plugins/input-mask/jquery.inputmask.date.extensions.js" charset="utf-8" ></script>
<script type="text/javascript" src="/static/plugins/input-mask/jquery.inputmask.extensions.js" charset="utf-8" ></script>
<script type="text/javascript" src="/static/plugins/slimScroll/jquery.slimscroll.min.js" charset="utf-8" ></script>
<script type="text/javascript" src="/static/plugins/select2/select2.full.min.js" charset="utf-8" ></script>
<script type="text/javascript" src="/static/plugins/fastclick/fastclick.js" charset="utf-8" ></script>
<script type="text/javascript" src="/static/js/stat/stat_operator.js" charset="utf-8" ></script>
<script type="text/javascript" src="/static/js/group/group_operator.js" charset="utf-8" ></script>
<script type="text/javascript" src="/static/ace/ace-noconflict/ace.js" charset="utf-8" ></script>
<script type="text/javascript" src="/static/ace/ace-noconflict/ext-language_tools.js" charset="utf-8" ></script>
<script type="text/javascript" src="/static/js/sitemap/sitemap_operator.js" charset="utf-8" ></script>
<script type="text/javascript" src="/static/js/sitemap/sitebind_operator.js" charset="utf-8" ></script>
<script type="text/javascript" src="/static/js/filter/filter_operator.js" charset="utf-8"></script>
<script type="text/javascript" src="/static/js/display/display_operator.js" charset="utf-8"></script>
<script type="text/javascript" src="/static/js/render/render.js" charset="utf-8"></script>
<script type="text/javascript" src="/static/js/render/base_options.js" charset="utf-8"></script>
<script type="text/javascript" src="/static/js/render/table_options.js" charset="utf-8"></script>
<script type="text/javascript" src="/static/js/common/sub_page.js" charset="utf-8" ></script>
</#macro>
