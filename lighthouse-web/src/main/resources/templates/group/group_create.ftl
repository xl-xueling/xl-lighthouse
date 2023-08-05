<html lang="UTF-8">
<head>
  <#import "../common/frame.ftl" as baseFrame>
  <title class="i18n" i18n_code="ldp_i18n_group_create_1000"></title>
</head>
<@baseFrame.sub_head />
<div class="row nested-sub-page">
  <div class="col-md-12">
    <div class="box">
      <section class="content-header">
        <h1 style="color:#57524d;font-size: 18px;">
          <samp class="i18n" i18n_code="ldp_i18n_group_create_1001"></samp>
        </h1>
      </section>
      <div class="register-box-body">
        <div class="form-group">
          <label for="token">Token:</label><span style="color: #9d9b32;font-size:13px;float:right;"><samp class="i18n" i18n_code="ldp_i18n_group_create_1002"></samp></span>
          <input type="text" class="form-control" autocomplete="off" id="token" placeholder="Token">
        </div>
        <div class="form-group">
          <label for=""><samp class="i18n" i18n_code="ldp_i18n_group_create_1003"></samp></label>
          <label for="projectId"></label>
          <select id="projectId" autocomplete="off" disabled class="form-control select2 select2-hidden-accessible" style="width: 100%;" tabindex="-1" aria-hidden="true">
            <option value="${projectEntity.id}" selected>${projectEntity.name}</option>
          </select>
        </div>
        <div class="form-group" style="display: none">
          <label for=""><samp class="i18n" i18n_code="ldp_i18n_group_create_1004"></samp></label>
          <label for="statType"></label>
          <select id="statType" disabled class="form-control select2 select2-hidden-accessible" style="width: 100%;" tabindex="-1" aria-hidden="true">
            <option value="1" selected class="i18n" i18n_code="ldp_i18n_group_create_1005"></option>
          </select>
        </div>
        <div class="form-group">
          <label for=""><samp class="i18n" i18n_code="ldp_i18n_group_update_1039"></samp>:</label>
          <label for="remark"></label>
          <textarea id="remark" style="width: 100%;border-color: #ccd2d8;" spellcheck="false" rows="2"></textarea>
        </div>
        <div class="form-group" id="dataColumnForm" style="display: block;">
          <label for=""><samp class="i18n" i18n_code="ldp_i18n_group_create_1006"></samp></label>
          <span onclick="GROUP_OPERATOR.addColumn();" style="float: right;cursor:pointer" class="badge bg-yellow"><samp class="i18n" i18n_code="ldp_i18n_group_create_1007"></samp></span>
          <div class="box-body">
            <table class="table table-bordered">
              <tbody id="column-content">
              <tr>
                <th style="width: 30%">ColumnName</th>
                <th style="width: 20%">Type</th>
                <th style="width: 30%">Comment</th>
                <th style="width: 20px">Operate</th>
              </tr>
              <tr>
                <td onclick="PAGE.tdclick(this);" class="column_name">ColumnName</td>
                <td style="padding: 1px;">
                  <select id="" autocomplete="off" class="column_type" style="width: 100px;" tabindex="-1" aria-hidden="true">
                    <option value="1" selected>String</option>
                    <option value="2">Numeric</option>
                  </select>
                </td>
                <td onclick="PAGE.tdclick(this);" class="column_comment">Comment</td>
                <td class="column_operate" onclick='GROUP_OPERATOR.deleteColumn(this.parentNode)'>
                  <a href="javascript:void(0);" style="color:#000000;"><span class="col-md-1 col-sm-2" title=""><i class="fa fa-fw fa-minus-circle"></i></span></a>
                </td>
              </tr>
              </tbody></table>
          </div>
        </div>

        <div class="form-group" id="rt_template" style="display: block;">
          <label for=""><samp class="i18n" i18n_code="ldp_i18n_group_create_1008"></samp></label>
          <span onclick="GROUP_OPERATOR.addRTTemplate();" style="float: right;cursor:pointer" class="badge bg-yellow"><samp class="i18n" i18n_code="ldp_i18n_group_create_1007"></samp></span>
          <div class="box-body">
            <table class="table table-bordered">
              <tbody id="rt_item-content">
              <tr>
                <th style="width: 75%">Template</th>
                <th style="width: 10%">TimeParam</th>
                <th style="width: 10%">Expire</th>
                <th style="width: 5%">Operate</th>
              </tr>
              <tr>
                <td onclick="" style="">
                  <pre id="stat_template_0" class="stat_template_origin" style="height: 60px;"></pre>
                </td>
                <td style=" vertical-align: middle;">
                  <label>
                    <select autocomplete="off" class="stat_time_param" style="width: 80px;" tabindex="-1" aria-hidden="true">
                      <option value="1-minute" class="i18n" i18n_code="ldp_i18n_group_create_1010"></option>
                      <option value="2-minute" class="i18n" i18n_code="ldp_i18n_group_create_1011"></option>
                      <option value="3-minute" class="i18n" i18n_code="ldp_i18n_group_create_1012"></option>
                      <option value="5-minute" class="i18n" i18n_code="ldp_i18n_group_create_1013"></option>
                      <option value="10-minute" class="i18n" i18n_code="ldp_i18n_group_create_1014"></option>
                      <option value="20-minute" class="i18n" i18n_code="ldp_i18n_group_create_1015"></option>
                      <option value="30-minute" class="i18n" i18n_code="ldp_i18n_group_create_1016"></option>
                      <option value="1-hour" selected class="i18n" i18n_code="ldp_i18n_group_create_1017"></option>
                      <option value="2-hour" class="i18n" i18n_code="ldp_i18n_group_create_1018"></option>
                      <option value="3-hour" class="i18n" i18n_code="ldp_i18n_group_create_1019"></option>
                      <option value="1-day" class="i18n" i18n_code="ldp_i18n_group_create_1020"></option>
                    </select>
                  </label>
                </td>
                <td style=" vertical-align: middle;">
                  <label>
                    <select autocomplete="off" class="stat_data_expire" style="width: 100px;" tabindex="-1" aria-hidden="true">
                      <option value="259200" class="i18n" i18n_code="ldp_i18n_group_create_1021"></option>
                      <option value="604800" class="i18n" i18n_code="ldp_i18n_group_create_1022"></option>
                      <option value="1209600" selected class="i18n" i18n_code="ldp_i18n_group_create_1023"></option>
                      <option value="2592000" class="i18n" i18n_code="ldp_i18n_group_create_1024"></option>
                      <option value="7776000" class="i18n" i18n_code="ldp_i18n_group_create_1025"></option>
                      <option value="15552000" class="i18n" i18n_code="ldp_i18n_group_create_1026"></option>
                      <option value="31104000" class="i18n" i18n_code="ldp_i18n_group_create_1027"></option>
                      <option value="62208000" class="i18n" i18n_code="ldp_i18n_group_create_1028"></option>
                    </select>
                  </label>
                </td>
                <td style=" vertical-align: middle; text-align: center; ">
                  <a href="javascript:void(0);" style="color:#ba7b7b;"><span class="col-md-1 col-sm-2" title=""><i class="fa fa-fw fa-minus-circle"></i></span></a>
                </td>
              </tr>
              </tbody></table>
          </div>
        </div>
        <div class="row">
          <div class="col-xs-12" style="text-align: center;vertical-align: middle;">
            <a href="javascript:GROUP_OPERATOR.addGroup();">
              <button  class="btn btn-primary" style="float: right;line-height: 1.42857;vertical-align: middle;"><samp class="i18n" i18n_code="ldp_i18n_group_create_1009"></samp></button>
            </a>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<@baseFrame.sub_tail />
<script>
  function _SubPageLoadCallBack(){
    let data = [
      {meta: "  Element", caption: "stat-item", value: "stat-item  />", score:1},
      {meta: "  KeyWord", caption: "title", value: "title=\"\"", score:1},
      {meta: "  KeyWord", caption: "stat", value: "stat=\"\"", score:1},
      {meta: "  KeyWord", caption: "dimens", value: "dimens=\"\"", score:1},
      {meta: "  KeyWord", caption: "limit", value: "limit=\"\"", score:1},
      {meta: "  FunctionName", caption: "count", value: "count()", score:1},
      {meta: "  FunctionName", caption: "max", value: "max()", score:1},
      {meta: "  FunctionName", caption: "min", value: "min()", score:1},
      {meta: "  FunctionName", caption: "avg", value: "avg()", score:1},
      {meta: "  FunctionName", caption: "sum", value: "sum()", score:1},
      {meta: "  FunctionName", caption: "seq", value: "seq()", score:1},
      {meta: "  FunctionName", caption: "bitcount", value: "bitcount()", score:1},
    ];
    let langTools = ace.require("ace/ext/language_tools");
    langTools.addCompleter({
      getCompletions: function(editor, session, pos, prefix, callback) {
        if (prefix.length === 0) {
          return callback(null, []);
        } else {
          return callback(null, data);
        }
      }
    });
    GROUP_OPERATOR.initEditor(".stat_template_origin",false);
  }
</script>
</html>
