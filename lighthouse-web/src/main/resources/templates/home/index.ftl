<html lang="UTF-8">
<head>
  <#import "../common/frame.ftl" as baseFrame>
  <title class="i18n">i18n(ldp_i18n_homepage_1000)</title>
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
        HomePage
      </h1>
    </section>
    <section class="content">
      <div class="row">
        <div class="col-md-9">
          <div class="box box-primary" style="border-top:2px solid #8e9dd1">
            <div class="box-body">
              <div class="ldp-stat-chart" statId="${fixStatEntity.id}" stateIndex="-1" style="width: 100%; height: 280px;"></div>
            </div>
          </div>
        </div>
        <div class="col-md-3">
          <div class="info-box">
            <span class="info-box-icon bg-aqua"><i class="fa fa-icon fa-star-o"></i></span>
            <div class="info-box-content">
              <span><samp class="i18n" i18n_code="ldp_i18n_homepage_1001"></samp></span>
              <span class="info-box-number">${yesterdayStatCount}/${totalStatCount}</span>
            </div>
          </div>
          <div class="info-box">
            <span class="info-box-icon bg-yellow"><i class="fa fa-icon fa-files-o"></i></span>
            <div class="info-box-content">
              <span><samp class="i18n" i18n_code="ldp_i18n_homepage_1003"></samp></span>
              <span class="info-box-number">${totalProjectCount}</span>
            </div>
          </div>
          <div class="info-box">
            <span class="info-box-icon bg-red" style="background-color: #e97465 !important;"><i class="fa fa-icon fa-user"></i></span>
            <div class="info-box-content">
              <span><samp class="i18n" i18n_code="ldp_i18n_homepage_1004"></samp></span>
              <span class="info-box-number">${totalUserCount}</span>
            </div>
          </div>
        </div>
      </div>
      <h2 class="page-header2">
        <samp class="i18n" i18n_code="ldp_i18n_homepage_1018"></samp></h2>
      <div class="row">
        <div class="col-md-3">
          <div class="box box-widget widget-user-2" style="padding: 10px;">
            <div class="widget-user-header bg-yellow" style="background-color: #bc6f15 !important">
              <h3 class="widget-user-username" style="margin-left:20px;"><samp class="i18n" i18n_code="ldp_i18n_homepage_1005"></samp></h3>
            </div>
            <div class="box-footer no-padding">
              <ul class="nav nav-stacked">
                <li><a href="/favorite/project/list.shtml"><samp class="i18n" i18n_code="ldp_i18n_homepage_1007"></samp></a></li>
                <li><a href="/favorite/stat/list.shtml"><samp class="i18n" i18n_code="ldp_i18n_homepage_1006"></samp></a></li>
                <li>&nbsp;</li>
              </ul>
            </div>
          </div>
        </div>
        <div class="col-md-3">
          <div class="box box-widget widget-user-2" style="padding: 10px;">
            <div class="widget-user-header bg-yellow" style="background-color: #bc6f15 !important">
              <h3 class="widget-user-username" style="margin-left:20px;"><samp class="i18n" i18n_code="ldp_i18n_homepage_1008"></samp></h3>
            </div>
            <div class="box-footer no-padding">
              <ul class="nav nav-stacked">
                <li><a href="/project/list.shtml?owner=1"><samp class="i18n" i18n_code="ldp_i18n_homepage_1009"></samp></a></li>
                <li><a href="/stat/list.shtml"><samp class="i18n" i18n_code="ldp_i18n_homepage_1010"></samp></a></li>
                <li>&nbsp;</li>
              </ul>
            </div>
          </div>
        </div>

        <div class="col-md-3">
          <div class="box box-widget widget-user-2" style="padding: 10px;">
            <div class="widget-user-header bg-yellow" style="background-color: #bc6f15 !important">
              <h3 class="widget-user-username" style="margin-left:20px;"><samp class="i18n" i18n_code="ldp_i18n_homepage_1011"></samp></h3>
            </div>
            <div class="box-footer no-padding">
              <ul class="nav nav-stacked">
                  <#if Session["user"].userName != 'admin'>
                    <li>
                      <a href="/approve/list.shtml"><samp class="i18n" i18n_code="ldp_i18n_homepage_1012"></samp>
                      <#if approveCount != 0>
                        <span class="pull-right badge bg-blue">${approveCount}</span>
                      </#if>
                      </a>
                    </li>
                    <li>
                      <a href="/apply/list.shtml"><samp class="i18n" i18n_code="ldp_i18n_homepage_1019"></samp></a>
                    </li>
                  <#else>
                    <li>
                      <a href="/approve/list.shtml"><samp class="i18n" i18n_code="ldp_i18n_homepage_1012"></samp>
                      <#if approveCount != 0>
                        <span class="pull-right badge bg-blue">${approveCount}</span>
                      </#if>
                      </a>
                    </li>
                    <li><a href="/user/list.shtml?state=0"><samp class="i18n" i18n_code="ldp_i18n_homepage_1013"></samp>
                      <#if pendUserCount != 0>
                        <span class="pull-right badge bg-blue">${pendUserCount}</span>
                      </#if>
                      </a>
                    </li>
                  </#if>
                <li>&nbsp;</li>
              </ul>
            </div>
          </div>
        </div>
        <div class="col-md-3">
          <div class="box box-widget widget-user-2" style="padding: 10px;">
            <div class="widget-user-header bg-yellow" style="background-color: #bc6f15 !important">
              <h3 class="widget-user-username" style="margin-left:20px;"><samp class="i18n" i18n_code="ldp_i18n_homepage_1014"></samp></h3>
            </div>
            <div class="box-footer no-padding">
              <ul class="nav nav-stacked">
                <li><a onclick="window.open('https://www.dtstep.com/archives/4231.html?fk=${_ldp_cluster}')"><samp class="i18n" i18n_code="ldp_i18n_homepage_1015"></samp></a></li>
                <li><a onclick="window.open('https://www.dtstep.com/community/ldp-issue?fk=${_ldp_cluster}')"><samp class="i18n" i18n_code="ldp_i18n_homepage_1016"></samp></a></li>
                <li><a onclick="window.open('https://www.dtstep.com?fk=${_ldp_cluster}')"><samp class="i18n" i18n_code="ldp_i18n_homepage_1017"></samp></a></li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
  <@baseFrame.footer />
</div>
<@baseFrame.tail />
</body>
</html>
<script src="/static/js/display/display_operator.js"></script>
<script type="text/javascript">
  function _PageLoadCallBack(){
    DISPLAY_OPERATOR.initStatView('${fixStatEntity.id}',DATE.format(new Date()),DATE.format(new Date()));
  }
</script>

