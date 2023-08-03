<html lang="UTF-8">
<head>
  <#import "../common/frame.ftl" as baseFrame>
  <#import "../common/pop.ftl" as popFrame>
  <title class="i18n">i18n(ldp_i18n_display_1000)</title>
  <@baseFrame.top />
</head>
<body class="skin-blue sidebar-mini sidebar-collapse">
<div class="wrapper" style="overflow: hidden;">
  <@baseFrame.header />
  <@baseFrame.leftMenu />
  <@baseFrame.rightMenu />
  <div class="content-wrapper">
    <section class="content-header" style="padding: 5px 5px 0 5px;">
    </section>
    <section class="invoice" style="padding: 5px 10px;margin: 10px 10px;">
      <div class="row">
        <div class="col-xs-12">
          <h2 class="page-header" style="border-bottom:none;margin: 10px 0 5px 0;padding-bottom:3px;font-size:18px;">
            <i class="fa fa-globe"></i>&nbsp;
            <#if statEntity.state == '1'>
              <span>${statEntity.title}</span>
            <#else>
              <span style="color:#d2691e; ">${statEntity.title}</span>
            </#if>
            <span style="color:#d2691e;font-size: 16px;" class="i18n">
              【${statEntity.statStateEnum.desc}】
            </span>
            <#if statEntity.templateEntity.limitFlag?string("true","false") == "true">
              <span style="font-size: 14px;margin-left: 10px;">
                <label>
                  <input type="checkbox" name="limitType" class="minimal" onclick="DISPLAY_OPERATOR.changeLimitType('${statEntity.id}','${startDate}','${endDate}','1','false')">
                </label>
                Limit
              </span>
            </#if>
            <small class="pull-right">
              <div style="float: left;margin-top: 3px;">
                <label>
                  <input type="radio" name="dateType" value="1" class="minimal" <#if startDate == endDate> checked</#if>/>
                </label>
                <span style="font-size: 14px;"><samp class="i18n" i18n_code="ldp_i18n_display_1010"></samp></span>
                <label>
                  <input type="radio" name="dateType" value="2" class="minimal" <#if startDate != endDate> checked</#if>>
                </label>
                <span style="font-size: 14px;"><samp class="i18n" i18n_code="ldp_i18n_display_1011"></samp></span>
                &nbsp;&nbsp;
              </div>
              <div style="float: right; display: block;width: 200px;">
                <#if startDate == endDate>
                  <input id="temp_date1" style="float: left;height: 25px;display: block;" type="text" autocomplete="off" value="${startDate}" class="form-control pull-right" placeholder="yyyy-MM-dd" lay-key="6">
                  <input id="temp_date2" style="float: left;height: 25px;display: none;" type="text" autocomplete="off" value="${startDate} - ${endDate}" class="form-control pull-right" placeholder=" - " lay-key="9">
                <#else>
                  <input id="temp_date1" style="float: left;height: 25px;display: none;" type="text" autocomplete="off" value="${startDate}" class="form-control pull-right" placeholder="yyyy-MM-dd" lay-key="6">
                  <input id="temp_date2" style="float: left;height: 25px;display: block;" type="text" autocomplete="off" value="${startDate} - ${endDate}" class="form-control pull-right" placeholder=" - " lay-key="9">
                </#if>
              </div>
            </small>
          </h2>
        </div>
      </div>
      <div class="row">
        <div class="col-md-12" style="padding-left:0;padding-right: 0;">
          <input type="hidden" id="displayType" value="${displayTypeEnum.chartName}"/>
          <div class="box-body">
            <div class="box box-primary" style="border-top:2px solid #8e9dd1">
              <div class="layui-form box-header">
                <div class="layui-form-item filter_default_components"></div>
                <#if projectEntity.privilegeIds?seq_contains(5) >
                  <div class="box-tools pull-right">
                  <button type="button" class="btn btn-box-tool btn-open" aria-expanded="false">
                    <i class="fa fa-cog"></i></button>
                  <ul class="dropdown-menu btn-open-ul" role="menu" style="min-width: 150px;right: 45px;text-align: center;">
                    <li>
                          <span>
                            <a style="width: 100px;" href="javascript:DISPLAY_OPERATOR.showFilterConfPage();"><samp class="i18n" i18n_code="ldp_i18n_display_1012"></samp></a>
                          </span>
                    </li>
                  </ul>
                  <#if statEntity.state == '1'>
                    <button type="button" onclick="MsgBox.Open('/track/stat.shtml?statId=${statEntity.id}')" class="btn btn-box-tool"><i class="fa  fa-flash (alias)"></i></button>
                  </#if>
                  <div class="btn-group">
                    <button type="button" class="btn btn-box-tool btn-open" aria-expanded="false">
                      <i class="fa fa-ellipsis-v"></i></button>
                    <ul class="dropdown-menu btn-open-ul" role="menu" style="min-width: 100px;">
                      <#if displayTypeList??>
                          <#list displayTypeList as displayTypeEnum>
                            <li>
                                <span>
                                  <a style="float: left;width: 70px;" href="javascript:DISPLAY_OPERATOR.changeDisplayType('${statEntity.id}','${startDate}','${endDate}','${displayTypeEnum.chartName}')">
                                      ${displayTypeEnum.chartName}
                                  </a>|
                                  <#if statEntity.displayType == displayTypeEnum.displayType>
                                    <a href="javascript:DISPLAY_OPERATOR.updateDisplayType('${statEntity.id}','${displayTypeEnum.displayType}','${displayTypeEnum.chartName}','false')"><i class="fa fa-fw fa-dot-circle-o"></i></a>
                                  <#else>
                                    <a href="javascript:DISPLAY_OPERATOR.updateDisplayType('${statEntity.id}','${displayTypeEnum.displayType}','${displayTypeEnum.chartName}','false')"><i class="fa fa-fw fa-circle-o"></i></a>
                                  </#if>
                                </span>
                              <span style="clear: both;"></span>
                            </li>
                          </#list>
                      </#if>
                    </ul>
                  </div>
                </div>
                </#if>
              </div>
              <div class="box-body">
                <div class="ldp-stat-chart" statId="${statEntity.id}" stateIndex="-1" style="height: 350px;padding: 10px;"></div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <#if (statEntity.templateEntity.statStateList?size == 2)>
        <div class="row">
          <div class="box box-primary" style="border-top:none;">
            <div class="col-md-6">
              <div class="box-body chart-responsive">
                <div class="ldp-stat-chart" statId="${statEntity.id}" stateIndex="0" style="height: 180px;padding: 10px;"></div>
              </div>
            </div>
            <div class="col-md-6">
              <div class="box-body chart-responsive">
                <div class="ldp-stat-chart" statId="${statEntity.id}" stateIndex="1" style="height: 180px;padding: 10px;"></div>
              </div>
            </div>
          </div>
        </div>
      </#if>
      <#if (statEntity.templateEntity.statStateList?size == 3)>
        <div class="row">
          <div class="box box-primary" style="border-top:none;">
            <div class="col-md-4">
              <div class="box-body chart-responsive">
                <div class="ldp-stat-chart" statId="${statEntity.id}" stateIndex="0" style="height: 180px;padding: 10px;"></div>
              </div>
            </div>
            <div class="col-md-4">
              <div class="box-body chart-responsive">
                <div class="ldp-stat-chart" statId="${statEntity.id}" stateIndex="1" style="height: 180px;padding: 10px;"></div>
              </div>
            </div>
            <div class="col-md-4">
              <div class="box-body chart-responsive">
                <div class="ldp-stat-chart" statId="${statEntity.id}" stateIndex="2" style="height: 180px;padding: 10px;"></div>
              </div>
            </div>
          </div>
        </div>
      </#if>
      <div class="row" style="padding: 5px;">
        <div class="col-xs-7">
          <p class="lead"><samp class="i18n" i18n_code="ldp_i18n_display_1013"></samp></p>
          <p class="text-muted well well-sm no-shadow" style="margin-top: 10px;font-size:12px;">
            ${statEntity.templateOfHtml}
          </p>
          <div id="match_stat_div" style="display: none;">
            <p class="lead"><samp class="i18n" i18n_code="ldp_i18n_display_1014"></samp></p>
            <p class="text-muted well well-sm no-shadow" style="margin-top: 10px;font-size:12px;">
            </p>
          </div>
          <p class="lead"><samp class="i18n" i18n_code="ldp_i18n_display_1015"></samp></p>
          <p class="text-muted well well-sm no-shadow" style="margin-top: 10px;font-size:13px;">
            ${statEntity.timeParam}
          </p>
        </div>
        <div class="col-xs-5">
          <p class="lead"><samp class="i18n" i18n_code="ldp_i18n_display_1016"></samp></p>
          <div class="table-responsive">
            <table class="table">
              <tr>
                <th style="width:50%">ID:</th>
                <td>${statEntity.id}</td>
              </tr>
              <tr id="match_stat_id" style="display: none;">
                <th style="width:50%"><samp class="i18n" i18n_code="ldp_i18n_display_1017"></samp>:</th>
                <td> -- </td>
              </tr>
              <tr>
                <th style="width:50%"><samp class="i18n" i18n_code="ldp_i18n_display_1018"></samp>:</th>
                <td>
                  ${projectEntity.name}
                  <#if projectEntity.privilegeIds?seq_contains(5) >
                    <a onclick="window.open(Encrypt.encryptUrl('/project/manage/index.shtml?projectId=${statEntity.projectId}'),'_blank');">[<i class="fa fa-fw fa-gears"></i>]</a>
                  </#if>
                </td>
              </tr>
              <tr>
                <th style="width:50%"><samp class="i18n" i18n_code="ldp_i18n_display_1020"></samp>:</th>
                <td>${departmentEntity.name}</td>
              </tr>
              <tr>
                <th><samp class="i18n" i18n_code="ldp_i18n_display_1021"></samp>(Token):</th>
                <td>${groupEntity.token}</td>
              </tr>
              <tr>
                <th><samp class="i18n" i18n_code="ldp_i18n_display_1022"></samp>:</th>
                <td>
                  <#if projectEntity.admins??>
                    <#list projectEntity.admins as admin>
                      <a href="javascript:MsgBox.Alert('User：${admin.userName};<br>Phone：${admin.phone};<br>Email：${admin.email};');">${admin.userName};</a>
                    </#list>
                  </#if>
                </td>
              </tr>
              <tr>
                <th><samp class="i18n" i18n_code="ldp_i18n_display_1023"></samp>:</th>
                <td>${statEntity.createTime?string('yyyy-MM-dd HH:mm:ss')}</td>
              </tr>
              <tr>
                <th><samp class="i18n" i18n_code="ldp_i18n_display_1024"></samp>:</th>
                <td>&nbsp;<a href="javascript:DISPLAY_OPERATOR.showStatLimitedRecord('${statEntity.id}')";>[<i class="fa fa-fw fa-tasks"></i>]</a></td>
              </tr>
            </table>
          </div>
        </div>
      </div>
    </section>
    <div class="clearfix"></div>
  </div>
  <@baseFrame.footer />
</div>
<@baseFrame.tail />
<script src="/static/js/filter/filter_operator.js" charset="utf-8" type="text/javascript" ></script>
<script src="/static/js/display/display_operator.js" charset="utf-8" type="text/javascript" ></script>
<script src="/static/js/render/render.js" charset="utf-8" type="text/javascript"></script>
<script src="/static/js/render/base_options.js" charset="utf-8" type="text/javascript"></script>
<script src="/static/js/render/table_options.js" charset="utf-8" type="text/javascript"></script>
<@popFrame.limitedRecordModal "2"/>
<@popFrame.filterConfig/>
<script>
  let statId = '${statEntity.id}';
  let startDate = '${startDate}';
  let endDate = '${endDate}';
  let filterParams = JSON.stringify(${filterParams});
  let isSub = 'false';
  function _PageLoadCallBack(){
    FILTER_OPERATOR.initFilter(filterParams,'false');
    DISPLAY_OPERATOR.initStatView(statId,startDate, endDate, null,'${displayTypeEnum.chartName}');
    DISPLAY_OPERATOR.dateSwitch(statId,'false');
  }
</script>
</body>
</html>
