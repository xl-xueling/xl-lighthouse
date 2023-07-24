<html lang="utf-8">
<head>
  <#import "../common/frame.ftl" as baseFrame>
  <title class="i18n">i18n(ldp_i18n_order_apply_1000)</title>
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
        <samp class="i18n" i18n_code="ldp_i18n_order_apply_1001"></samp>
      </h1>
      <ol class="breadcrumb">
        <li><a href=/index.shtml"><i class="fa fa-dashboard"></i> Home</a></li>
        <li class="active"><samp class="i18n" i18n_code="ldp_i18n_order_apply_1001"></samp></li>
      </ol>
    </section>
    <section class="content">
      <div class="row">
        <div class="col-md-12">
          <div class="box">
            <div class="nav-tabs-custom">
          <div class="tab-content" style="padding-top: 0 !important;">
            <div class="tab-pane active" id="fa-icons">
              <div class="box" style="border-top:none !important;">
                <div class="mailbox-controls" style="float: right;margin-bottom: 5px;">
                  <div class="input-group input-group-sm" style="width: 230px;float:right;margin-left:3px;">
                    <div>
                      <label for="apply_state"></label>
                      <select id="apply_state" class="form-control2 select2" style="width: 99%;float:left;height: 30px;border:1px solid #e1e3e9;">
                          <#if state == -1>
                            <option value="-1" selected="selected" class="i18n" i18n_code="ldp_i18n_order_common_1007"></option>
                          <#else >
                            <option value="-1" class="i18n" i18n_code="ldp_i18n_order_common_1007"></option>
                          </#if>
                          <#if state == 1>
                            <option selected value="1" class="i18n" i18n_code="ldp_i18n_order_common_1008"></option>
                          <#else >
                            <option value="1" class="i18n" i18n_code="ldp_i18n_order_common_1008"></option>
                          </#if>
                          <#if state == 2>
                            <option selected value="2" class="i18n" i18n_code="ldp_i18n_order_common_1009"></option>
                          <#else >
                            <option value="2" class="i18n" i18n_code="ldp_i18n_order_common_1009"></option>
                          </#if>
                          <#if state == 3>
                            <option selected value="3" class="i18n" i18n_code="ldp_i18n_order_common_1010"></option>
                          <#else >
                            <option value="3" class="i18n" i18n_code="ldp_i18n_order_common_1010"></option>
                          </#if>
                      </select>
                    </div>
                    <div class="input-group-btn">
                      <button type="submit" class="btn btn-default" style="margin-top:4px;" onclick="APPLY_OPERATOR.searchApply();"><i class="fa fa-search"></i></button>
                    </div>
                  </div>
                </div>
                <div class="box-body">
                  <table class="table table-bordered table-hover">
                    <tr>
                      <th>ID</th>
                      <th>OrderType</th>
                      <th>Desc</th>
                      <th>Admins</th>
                      <th>CreateTime</th>
                      <th>State</th>
                      <th>ApproveUser</th>
                      <th>ProcessTime</th>
                      <th style="width: 100px">Operation</th>
                    </tr>
                    <#if listObject.dataList ??>
                      <#list listObject.dataList as entity>
                      <tr>
                        <td>
                            ${entity.id}
                        </td>
                        <td class="i18n">
                            ${entity.orderTypeDesc}
                        </td>
                        <td class="i18n">
                            ${entity.desc}
                        </td>
                        <td>
                          <#if entity.admins??>
                            <#list entity.admins as userEntity>
                              <a href="javascript:MsgBox.Alert('UserName：${userEntity.userName};<br>Phone：${userEntity.phone};<br>Email：${userEntity.email};');">${userEntity.userName};</a>
                            </#list>
                          </#if>
                        </td>
                        <td>
                          ${entity.createTime?string('yyyy-MM-dd HH:mm:ss')}
                        </td>
                        <td class="i18n">
                            ${entity.orderStateDesc}
                        </td>
                        <td>
                          <#if entity.approveUser??>
                            <a href="javascript:MsgBox.Alert('UserName：${entity.approveUserEntity.userName};<br>Phone：${entity.approveUserEntity.phone};<br>Email：${entity.approveUserEntity.email};');">${entity.approveUserEntity.userName};</a>
                          <#else>
                            --
                          </#if>
                        </td>
                        <td>
                          <#if entity.processTime??>
                            ${entity.processTime?string('yyyy-MM-dd HH:mm:ss')}
                          <#else>
                            --
                          </#if>
                        </td>
                        <td>
                            <#if entity.state == 1>
                              <span class="i18n" title="i18n(ldp_i18n_order_apply_1008)">
                                <a href="javascript:APPLY_OPERATOR.retractApplyConfirm('${entity.id}','${entity.id}',document.URL)" style="color:#000000;">
                                  <i class="fa fa-fw fa-rotate-left"></i>
                                </a>
                              </span>
                            <#else>
                              --
                            </#if>
                        </td>
                      </tr>
                    </#list>
                    </#if>
                  </table>
                </div>
                <#if listObject??>
                  <@baseFrame.paging listObject.pageEntity/>
                </#if>
              </div>
            </div>
          </div>
        </div>
          </div>
      </div>
      </div>
    </section>
  </div>
  <@baseFrame.footer />
</div>
<@baseFrame.tail />
<script src="/static/js/apply/apply_operator.js"></script>
</body>
</html>
