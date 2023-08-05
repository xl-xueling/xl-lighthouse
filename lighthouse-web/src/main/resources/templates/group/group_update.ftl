<#import "../common/frame.ftl" as baseFrame>
<#import "../common/pop.ftl" as popFrame>
<@baseFrame.sub_head />
<div class="row nested-sub-page">
  <div class="col-md-12">
    <div class="box">
      <section class="content-header">
        <h1 style="color:#57524d;font-size: 18px;">
          <samp class="i18n" i18n_code="ldp_i18n_group_update_1001"></samp>
        </h1>
      </section>
    <div class="register-box-body">
      <div class="form-group">
        <label for="groupToken">Token:</label>
        <input type="hidden" id="groupId" value="${groupEntity.id}"/>
        <input type="text" class="form-control" disabled id="groupToken" placeholder="GroupToken" value="${groupEntity.token}">
      </div>
      <div class="form-group">
        <label for=""><samp class="i18n" i18n_code="ldp_i18n_group_update_1002"></samp>:</label>
        <label for="projectId"></label>
        <select id="projectId" autocomplete="off" disabled class="form-control select2 select2-hidden-accessible" style="width: 100%;" tabindex="-1" aria-hidden="true">
          <option value="${projectEntity.id}" selected>${projectEntity.name}</option>
        </select>
      </div>
      <div class="form-group" style="display: none">
        <label for=""><samp class="i18n" i18n_code="ldp_i18n_group_update_1003"></samp>:</label>
        <label for="statType"></label>
        <select id="statType" disabled autocomplete="off" class="form-control select2 select2-hidden-accessible" style="width: 100%;" tabindex="-1" aria-hidden="true">
          <option value="1" selected class="i18n" i18n_code="ldp_i18n_group_update_1004"></option>
        </select>
      </div>
      <div class="form-group">
        <label for=""><samp class="i18n" i18n_code="ldp_i18n_group_update_1039"></samp>:</label>
        <label for="remark"></label>
        <textarea id="remark" style="width: 100%;border-color: #ccd2d8;" spellcheck="false" rows="2">${groupEntity.remark}</textarea>
      </div>
      <div class="form-group">
        <label for=""><samp class="i18n" i18n_code="ldp_i18n_group_update_1006"></samp>:</label>
        <span onclick="GROUP_OPERATOR.addColumn();" style="float: right;cursor:pointer;" class="badge bg-yellow"><samp class="i18n" i18n_code="ldp_i18n_group_update_1007"></samp></span>
        <div class="box-body">
          <table class="table table-bordered">
            <tbody id="column-content">
            <tr>
              <th style="width: 35%">Column</th>
              <th style="width: 20%">Type</th>
              <th style="width: 30%">Comment</th>
              <th style="width: 20px">Operate</th>
            </tr>
            <#if groupEntity.columnList??>
              <#list groupEntity.columnList as metaColumn>
                <tr>
                  <td style="color:#957373;" class="column_name">${metaColumn.columnName}</td>
                  <td style="padding: 0;">
                    <select disabled id="" autocomplete="off" class="column_type" style="width: 100px;color: #b8b0b0;" tabindex="-1" aria-hidden="true">
                      <option value="1" <#if metaColumn.columnType == 1>selected</#if>>String</option>
                      <option value="2" <#if metaColumn.columnType == 2>selected</#if>>Numeric</option>
                    </select>
                  </td>
                  <td onclick="PAGE.tdclick(this);" class="column_comment">${metaColumn.columnComment}</td>
                  <td class="column_operate">
                    <a href="javascript:void(0);" style="color:#ba7b7b;"><span class="col-md-1 col-sm-2" title=""><i class="fa fa-fw fa-minus-circle"></i></span></a>
                  </td>
                </tr>
              </#list>
            </#if>
            </tbody></table>
        </div>
      </div>
      <div class="form-group">
        <label for=""><samp class="i18n" i18n_code="ldp_i18n_group_update_1008"></samp>：</label>
        <span onclick="GROUP_OPERATOR.addRTTemplate();" style="float: right;cursor:pointer;" class="badge bg-yellow"><samp class="i18n" i18n_code="ldp_i18n_group_update_1007"></samp></span>
        <div class="box-body">
          <table class="table table-bordered">
            <tbody id="rt_item-content">
            <tr>
              <th style="width: 75%">Template</th>
              <th style="width: 10%">TimeParam</th>
              <th style="width: 10%">Expire</th>
              <th style="width: 5%">Operate</th>
            </tr>
            <#if statList??>
              <#list statList as entity>
                <#assign index=entity_index/>
                <tr>
                  <td>
                    <input type="hidden" class="template_id" value="${entity.id}"/>
                    <pre id="stat_template_${index}" class="stat_template_origin" style="height: 50px;color: #0000cc;">${entity.templateOfHtml}</pre>
                  </td>
                  <td style=" vertical-align: middle;">
                    <label>
                      <select autocomplete="off" class="stat_time_param" disabled style="width: 80px;color: #b8b0b0;" tabindex="-1" aria-hidden="true">
                        <option value="1-minute" class="i18n" i18n_code="ldp_i18n_group_create_1010" <#if entity.timeParam == '1-minute'>selected</#if>></option>
                        <option value="2-minute" class="i18n" i18n_code="ldp_i18n_group_create_1011" <#if entity.timeParam == '2-minute'>selected</#if>></option>
                        <option value="3-minute" class="i18n" i18n_code="ldp_i18n_group_create_1012" <#if entity.timeParam == '3-minute'>selected</#if>></option>
                        <option value="5-minute" class="i18n" i18n_code="ldp_i18n_group_create_1013" <#if entity.timeParam == '5-minute'>selected</#if>></option>
                        <option value="10-minute" class="i18n" i18n_code="ldp_i18n_group_create_1014" <#if entity.timeParam == '10-minute'>selected</#if>></option>
                        <option value="20-minute" class="i18n" i18n_code="ldp_i18n_group_create_1015" <#if entity.timeParam == '20-minute'>selected</#if>></option>
                        <option value="30-minute" class="i18n" i18n_code="ldp_i18n_group_create_1016" <#if entity.timeParam == '30-minute'>selected</#if>></option>
                        <option value="1-hour" class="i18n" i18n_code="ldp_i18n_group_create_1017" <#if entity.timeParam == '1-hour'>selected</#if>></option>
                        <option value="2-hour" class="i18n" i18n_code="ldp_i18n_group_create_1018" <#if entity.timeParam == '2-hour'>selected</#if>></option>
                        <option value="3-hour" class="i18n" i18n_code="ldp_i18n_group_create_1019" <#if entity.timeParam == '3-hour'>selected</#if>></option>
                        <option value="1-day" class="i18n" i18n_code="ldp_i18n_group_create_1020" <#if entity.timeParam == '1-day'>selected</#if>></option>
                      </select>
                    </label>
                  </td>
                  <td style=" vertical-align: middle;">
                    <label>
                      <select autocomplete="off" class="stat_data_expire" disabled style="width: 100px;color: #b8b0b0;" tabindex="-1" aria-hidden="true">
                        <option value="259200" class="i18n" i18n_code="ldp_i18n_group_create_1021" <#if entity.dataExpire == 259200>selected</#if>></option>
                        <option value="604800" class="i18n" i18n_code="ldp_i18n_group_create_1022" <#if entity.dataExpire == 604800>selected</#if>></option>
                        <option value="1209600" class="i18n" i18n_code="ldp_i18n_group_create_1023" <#if entity.dataExpire == 1209600>selected</#if>></option>
                        <option value="2592000" class="i18n" i18n_code="ldp_i18n_group_create_1024" <#if entity.dataExpire == 2592000>selected</#if>></option>
                        <option value="7776000" class="i18n" i18n_code="ldp_i18n_group_create_1025" <#if entity.dataExpire == 7776000>selected</#if>></option>
                        <option value="15552000" class="i18n" i18n_code="ldp_i18n_group_create_1026" <#if entity.dataExpire == 15552000>selected</#if>></option>
                        <option value="31104000" class="i18n" i18n_code="ldp_i18n_group_create_1027" <#if entity.dataExpire == 31104000>selected</#if>></option>
                        <option value="62208000" class="i18n" i18n_code="ldp_i18n_group_create_1028" <#if entity.dataExpire == 62208000>selected</#if>></option>
                      </select>
                    </label>
                  </td>
                  <td style="vertical-align: middle;" onclick="void(0);">
                    <a onclick="STAT_OPERATOR.showUpdateModal('${entity.id}')"><span class="col-md-1 col-sm-2" title=""><i class="fa fa-fw fa fa-pencil"></i></span></a>
                  </td>
                </tr>
              </#list>
            </#if>
            </tbody></table>
        </div>
      </div>
        <div class="row">
          <div class="col-xs-12" style="text-align: center;vertical-align: middle;">
            <a href="javascript:GROUP_OPERATOR.updateGroup();">
              <button  class="btn btn-primary" style="float: right;line-height: 1.42857;vertical-align: middle;"><samp class="i18n" i18n_code="ldp_i18n_group_update_1009"></samp></button>
            </a>
          </div>
        </div>
    </div>
    </div>
    <@popFrame.statUpdateModal />
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
    GROUP_OPERATOR.initEditor(".stat_template_origin",true);
  }
</script>
