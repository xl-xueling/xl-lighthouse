<html lang="UTF-8">
<head>
  <#import "../common/frame.ftl" as baseFrame>
  <#import "../common/pop.ftl" as popFrame>
  <title class="i18n">i18n(ldp_i18n_display_1000)</title>
  <@baseFrame.top />
</head>
<style type="text/css">
  .xm-select * {
    font-size:10px !important;
  }
</style>
<body class="skin-blue sidebar-mini sidebar-collapse">
<div class="wrapper">
  <@baseFrame.header />
  <@baseFrame.leftMenu />
  <@baseFrame.rightMenu />
  <div class="content-wrapper">
    <section class="content-header" style="padding: 5px 5px 0 5px;">
    </section>
    <section class="invoice" style="padding: 5px 10px;margin: 10px 10px;">
      <input type="hidden" id="limitFlag" value="1">
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
                  <input type="checkbox" name="limitType" checked class="minimal" onclick="DISPLAY_OPERATOR.changeLimitType('${statEntity.id}','${startDate}','${endDate}','2','false')">
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
              <div class="box-body">
                <div id="${statEntity.id}" style="height: 410px;padding: 10px;"></div>
              </div>
            </div>
          </div>
        </div>
      </div>
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
  let filterParams = '${filterParams}';
  let batchIndex = '${curBatchIndex}';
  let isSub = 'false';
  function _PageLoadCallBack(){
    FILTER_OPERATOR.initFilter(filterParams,'false');
    DISPLAY_OPERATOR.initLimitView(statId, startDate, endDate,batchIndex);
    DISPLAY_OPERATOR.dateSwitchLimit(statId,'false');
    FILTER_OPERATOR.initFilterOfExport(filterParams);
  }
</script>
</body>
</html>
