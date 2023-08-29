<html lang="UTF-8">
<head>
  <#import "../common/frame.ftl" as baseFrame>
  <title class="i18n">i18n(ldp_i18n_track_1000)</title>
  <@baseFrame.top />
</head>
<body class="skin-blue sidebar-mini sidebar-collapse">
<div class="wrapper">
  <@baseFrame.header />
  <@baseFrame.leftMenu />
  <@baseFrame.rightMenu />
  <div class="content-wrapper">
    <section class="content-header">
      <h1 style="font-size: 20px;">
        <samp class="i18n" i18n_code="ldp_i18n_track_1001"></samp><span style="font-size: 15px;">${statEntity.title}</span>
        <label for="track-switch"></label>
        <input type="checkbox" name="track-switch" id="track-switch" data-on-text="ON" data-off-text="OFF"/>
      </h1>
      <ol class="breadcrumb">
        <li><a href="/index.shtml"><i class="fa fa-dashboard"></i> Home</a></li>
        <li><a href="/stat/list.shtml"><samp class="i18n" i18n_code="ldp_i18n_track_1002"></samp></a></li>
        <li class="active"><samp class="i18n" i18n_code="ldp_i18n_track_1010"></samp></li>
      </ol>
    </section>
    <section class="content" style="padding-top: 15px;">
      <input type="hidden" id="debugStartTime" />
      <input type="hidden" id="debugEndTime" />
      <div class="row">
        <div class="col-md-7">
          <div class="box box-primary" style="border-top:2px solid #8e9dd1">
            <div class="box-header">
              <div class="box-tools pull-right">
              </div>
            </div>
            <div class="box-body">
              <div class="ldp-stat-chart" statId="${fixStatEntity.id}" stateIndex="-1" style="width: 100%; height: 350px;"></div>
            </div>
          </div>
        </div>
        <div class="col-md-5">
           <pre class="prettyprint" id="track_shell"></pre>
        </div>
      </div>
      <div class="row">
        <div class="col-md-12" id="track_record">
          <div class="box">
            <div class="box-body" style="padding-top:0;">
              <div style="float: right;">
                <div class="checkbox">
                  <label>
                    <input type="checkbox" name="refresh_button" checked/>
                    <font size="2px;"><samp class="i18n" i18n_code="ldp_i18n_track_1003"></samp></font>
                  </label>
                </div>
              </div>
              <table class="table table-bordered table-hover">
                <tr></tr>
                <tbody id="track_record_table">
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
  <@baseFrame.footer />
</div>
<@baseFrame.tail />
<script src="/static/js/display/display_operator.js"></script>
<script src="/static/js/render/render.js" charset="utf-8" type="text/javascript"></script>
<script src="/static/js/render/base_options.js" charset="utf-8" type="text/javascript"></script>
<script src="/static/js/render/table_options.js" charset="utf-8" type="text/javascript"></script>
<script src="/static/js/track/track_operator.js"></script>
<script>
  let refreshId;
  const fixStatId = '${fixStatEntity.id}';
  const statId = '${statEntity.id}';
  const groupId = '${groupEntity.id}';
  const debugParams = '${groupEntity.debugParams}';
  const token = '${groupEntity.token}'
  const title = '${statEntity.title}';
  function _PageLoadCallBack(){
    let paramArr = [];
    let groupObj = {
      filterKey:"groupId",
      valueList:[{
        value:groupId,
        aliasName:"${groupEntity.token}"
      }]
    }
    let captchaObj = {
      filterKey:"captcha",
      valueList:[
        {
          value:0,
          aliasName:"SUCCESS"
        },
        {
          value:1,
          aliasName:"PARAM_CHECK_FAILED"
        }
      ]
    }
    paramArr.push(groupObj);
    paramArr.push(captchaObj);
    let initFlag = false;
    DISPLAY_OPERATOR.initStatView(fixStatId,DATE.format(new Date()),DATE.format(new Date()),JSON.stringify(paramArr));
    $("[name='track-switch']").bootstrapSwitch({
      'onSwitchChange': function(event, state){
        if(initFlag){
          TRACK_OPERATOR.trackConfirm(statId,state);
          return false;
        }
      },
    }).bootstrapSwitch("size","mini");
    let debugMode = '${groupEntity.debugMode}';
    TRACK_OPERATOR.trackInit(statId,debugMode,debugParams);
    setTimeout(function (){
      initFlag = true;
    }, 100)
    $(document).ready(function() {
      $('input[type=checkbox][name=refresh_button]').change(function() {
        if ($(this).is(':checked')) {
          TRACK_OPERATOR.startAutoRefresh();
        }
        else {
          window.clearInterval(refreshId);
        }
      });

      if(debugMode === '1'){
        TRACK_OPERATOR.startAutoRefresh();
      }
    });
  }
</script>
</body>
</html>
