<html lang="UTF-8">
<head>
  <#import "../common/frame.ftl" as baseFrame>
  <title class="i18n">i18n(ldp_i18n_order_approve_1000)</title>
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
        <samp class="i18n" i18n_code="ldp_i18n_order_approve_1001"></samp>
      </h1>
      <ol class="breadcrumb">
        <li><a href="/index.shtml"><i class="fa fa-dashboard"></i>Home</a></li>
        <li class="active"><samp class="i18n" i18n_code="ldp_i18n_order_approve_1001"></samp></li>
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
                    <div>
                      <label for="approve_state"></label>
                      <select id="approve_state" class="form-control2 select2" style="width: 200px;float:left;height: 30px;border:1px solid #e1e3e9;">
                          <#if state == -1>
                            <option value="-1" selected="selected" class="i18n" i18n_code="ldp_i18n_order_approve_1004"></option>
                          <#else >
                            <option value="-1" class="i18n" i18n_code="ldp_i18n_order_approve_1004"></option>
                          </#if>
                        <#if state == 1>
                            <option selected value="1" class="i18n" i18n_code="ldp_i18n_order_approve_1005"></option>
                        <#else >
                            <option value="1" class="i18n" i18n_code="ldp_i18n_order_approve_1005"></option>
                        </#if>
                        <#if state == 2>
                            <option selected value="2" class="i18n" i18n_code="ldp_i18n_order_approve_1006"></option>
                        <#else >
                            <option value="2" class="i18n" i18n_code="ldp_i18n_order_approve_1006"></option>
                        </#if>
                        <#if state == 3>
                            <option selected value="3" class="i18n" i18n_code="ldp_i18n_order_approve_1007"></option>
                          <#else >
                            <option value="3" class="i18n" i18n_code="ldp_i18n_order_approve_1007"></option>
                          </#if>
                      </select>
                      <div class="input-group input-group-sm" style="width: 200px;float:right;margin-left:3px;">
                        <label for="approve_search"></label>
                        <input type="text" id="approve_search" class="form-control pull-right" value="${search!}" placeholder="Search">
                        <div class="input-group-btn">
                          <button type="submit" class="btn btn-default" onclick="APPROVE_OPERATOR.searchApprove();"><i class="fa fa-search"></i></button>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="box-body">
                    <table class="table table-bordered table-hover">
                      <tr>
                        <th>ID</th>
                        <th>User</th>
                        <th>OrderType</th>
                        <th>Desc</th>
                        <th>Admins</th>
                        <th>CreateTime</th>
                        <th>ApproveUser</th>
                        <th>ProcessTime</th>
                        <th>State</th>
                        <th style="width: 100px">Operation</th>
                      </tr>
                      <#if listObject.dataList??>
                        <#list listObject.dataList as entity>
                          <tr>
                            <td>
                              ${entity.id}
                            </td>
                            <td>
                              ${entity.userName!}
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
                            <td>
                              <#if entity.approveUserEntity??>
                                <a href="javascript:MsgBox.Alert('UserName：${entity.approveUserEntity.userName};<br>Phone：${entity.approveUserEntity.phone};<br>Email：${entity.approveUserEntity.email};');">${entity.approveUserEntity.userName};</a>
                              <#else >
                                --
                              </#if>
                            </td>
                            <td>
                              <#if entity.processTime??>
                                ${entity.processTime?string('yyyy-MM-dd HH:mm:ss')}
                              <#else >
                                --
                              </#if>
                            </td>
                            <td class="i18n">
                              ${entity.orderStateDesc}
                            </td>
                            <td>
                              <#if entity.state == 1>
                                <i onclick="APPROVE_OPERATOR.agreeConfirm('${entity.id}','${entity.userName}','${entity.id}');" class="fa fa-fw fa-check"></i>
                                <i onclick="APPROVE_OPERATOR.refuseConfirm('${entity.id}','${entity.userName}','${entity.id}');" class="fa fa-fw fa-times"></i>
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
<script type="text/javascript" src="/static/js/approve/approve_operator.js" charset="utf-8"></script>
</body>
</html>
