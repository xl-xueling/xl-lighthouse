<html lang="UTF-8">
<head>
  <#import "../common/frame.ftl" as baseFrame>
  <title class="i18n" i18n_code="ldp_i18n_user_list_1000"></title>
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
        <samp class="i18n" i18n_code="ldp_i18n_user_list_1001"></samp>
      </h1>
      <ol class="breadcrumb">
        <li><a href="/index.shtml"><i class="fa fa-dashboard"></i>Home</a></li>
        <li class="active"><samp class="i18n" i18n_code="ldp_i18n_user_list_1001"></samp></li>
      </ol>
    </section>
    <section class="content">
      <div class="row">
        <div class="col-md-12">
          <div class="box" style="min-height: 100px;">
            <div class="mailbox-controls">
              <div style="float: right;width: 640px;margin-bottom:5px;">
                <label for="department_id"></label>
                <select class="form-control select2 select2-accessible" id="department_id" style="width: 300px;float:left;height: 29px;border:1px solid #e1e3e9;padding: 2px 12px;" tabindex="-1" aria-hidden="true">
                  <option selected="selected" value="-1">-- Select Department --</option>
                  <#if departmentList??>
                      <#list departmentList as department>
                          <#if department.id == departmentId>
                            <option selected value="${department.id}">${department.fullPathName}</option>
                          <#else>
                            <option value="${department.id}">${department.fullPathName}</option>
                          </#if>
                      </#list>
                  </#if>
                </select>
                <label for="state"></label>
                <select id="state" class="form-control2 select2" style="width: 120px;float:left;height: 29px;border:1px solid #e1e3e9;">
                    <#if state == -1>
                      <option value="-1" selected="selected" class="i18n" i18n_code="ldp_i18n_user_list_1002"></option>
                    <#else >
                      <option value="-1" class="i18n" i18n_code="ldp_i18n_user_list_1002"></option>
                    </#if>
                    <#if state == 0>
                      <option selected value="0" class="i18n" i18n_code="ldp_i18n_user_list_1003"></option>
                    <#else >
                      <option value="0" class="i18n" i18n_code="ldp_i18n_user_list_1003"></option>
                    </#if>
                    <#if state == 1>
                      <option selected value="1" class="i18n" i18n_code="ldp_i18n_user_list_1004"></option>
                    <#else >
                      <option value="1" class="i18n" i18n_code="ldp_i18n_user_list_1004"></option>
                    </#if>
                    <#if state == 3>
                      <option selected value="3" class="i18n" i18n_code="ldp_i18n_user_list_1005"></option>
                    <#else >
                      <option value="3" class="i18n" i18n_code="ldp_i18n_user_list_1005"></option>
                    </#if>
                </select>
                  <div class="input-group input-group-sm" style="width: 200px;float:right;">
                    <label for="user_search"></label>
                    <input type="text" id="user_search" class="form-control pull-right" value="${search!}" placeholder="Search">
                    <div class="input-group-btn">
                      <button type="submit" class="btn btn-default" onclick="USER_OPERATOR.searchUser();"><i class="fa fa-search"></i></button>
                    </div>
                  </div>
                </div>
            </div>
            <div class="box-body">
              <table class="table table-bordered table-hover">
                  <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Department</th>
                    <th>Phone</th>
                    <th>Email</th>
                    <th>Status</th>
                    <th>CreateTime</th>
                    <th>LastTime</th>
                    <th style="width: 200px">Operation</th>
                  </tr>
                  <#if listObject.dataList??>
                    <#list listObject.dataList as entity>
                      <tr>
                        <td>
                          ${entity.id}
                        </td>
                        <td>
                          ${entity.userName}
                        </td>
                        <td>
                          <#if entity.departmentFullName??>
                            ${entity.departmentFullName}
                          <#else >
                            --
                          </#if>
                        </td>
                        <td>
                          ${entity.phone}
                        </td>
                        <td>
                          ${entity.email}
                        </td>
                        <td>
                          <#if entity.state == 1>
                            <samp class="i18n" i18n_code="ldp_i18n_user_list_1006"></samp>
                          <#elseif entity.state == 0>
                            <span style="color: red"><b><samp class="i18n" i18n_code="ldp_i18n_user_list_1007"></samp></b></span>
                          <#elseif entity.state == 3>
                            <samp class="i18n" i18n_code="ldp_i18n_user_list_1008"></samp>
                          <#elseif entity.state == 4>
                            <samp class="i18n" i18n_code="ldp_i18n_user_list_1009"></samp>
                          </#if>
                        </td>
                        <td>
                          ${entity.createTime?string('yyyy-MM-dd HH:mm:ss')}
                        </td>
                        <td>
                          ${entity.lastTime?string('yyyy-MM-dd HH:mm:ss')}
                        </td>
                        <td class="i18n">
                          <#if entity.userName == 'admin'>
                            <i title="i18n(ldp_i18n_user_list_1014)" onclick="USER_OPERATOR.resetPasswordConfirm('${entity.id}','${entity.userName}');" class="fa fa-fw fa-history"></i>
                          <#else>
                            <#if entity.state == 0>
                              <i title="i18n(ldp_i18n_user_list_1010)" onclick="USER_OPERATOR.activateUserConfirm('${entity.id}','${entity.userName}');" class="fa fa-fw fa-check"></i>
                              <i title="i18n(ldp_i18n_user_list_1011)" onclick="USER_OPERATOR.refuseUserConfirm('${entity.id}','${entity.userName}');" class="fa fa-fw fa-times"></i>
                            <#elseif entity.state == 3>
                              <i title="i18n(ldp_i18n_user_list_1013)" onclick="USER_OPERATOR.activateUserConfirm('${entity.id}','${entity.userName}');" class="fa fa-fw fa-refresh"></i>
                              <i title="i18n(ldp_i18n_user_list_1012)" onclick="USER_OPERATOR.deleteConfirm('${entity.id}','${entity.userName}');" class="fa fa-fw fa-minus-circle"></i>
                            </#if>
                            <#if entity.state == 1>
                              <i title="i18n(ldp_i18n_user_list_1014)" onclick="USER_OPERATOR.resetPasswordConfirm('${entity.id}','${entity.userName}');" class="fa fa-fw fa-history"></i>
                              <i title="i18n(ldp_i18n_user_list_1015)" onclick="USER_OPERATOR.freezeUserConfirm('${entity.id}','${entity.userName}');" class="fa fa-fw fa-empire"></i>
                              <i title="i18n(ldp_i18n_user_list_1012)" onclick="USER_OPERATOR.deleteConfirm('${entity.id}','${entity.userName}');" class="fa fa-fw fa-minus-circle"></i>
                            </#if>
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
    </section>
  </div>
  <@baseFrame.footer />
</div>
<@baseFrame.tail />
<script src="/static/js/user/user_operator.js" charset="utf-8"></script>
</body>
</html>
