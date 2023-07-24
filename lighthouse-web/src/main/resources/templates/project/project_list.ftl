<html lang="UTF-8">
<head>
  <#import "../common/frame.ftl" as baseFrame>
  <title class="i18n">i18n(ldp_i18n_project_list_1000)</title>
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
        <samp class="i18n" i18n_code="ldp_i18n_project_list_1001"></samp>
      </h1>
      <ol class="breadcrumb">
        <li><a href="/index.shtml"><i class="fa fa-dashboard"></i> Home</a></li>
        <li class="active"><samp class="i18n" i18n_code="ldp_i18n_project_list_1002"></samp></li>
      </ol>
    </section>
    <section class="content">
      <div class="row">
        <div class="col-md-12">
          <div class="box" style="min-height: 120px;">
            <div class="mailbox-controls">
                <div class="btn-group">
                    <a href="/project/create/index.shtml" class="i18n" title="i18n(ldp_i18n_project_list_1003)">
                        <button type="button" class="btn btn-default btn-sm"><i class="fa fa-plus i18n" >&nbsp;&nbsp;i18n(ldp_i18n_project_list_1003)</i></button>
                    </a>
                </div>
              <div style="float: right;width: 590px;">
                <div class="checkbox" style="float: left;margin-right:10px;">
                  <label>
                    <input type="checkbox" <#if owner == 1>checked</#if> id="owner">
                    <font size="2px;" class="i18n">i18n(ldp_i18n_project_list_1004)</font>
                  </label>
                </div>
                <label for="department_id"></label>
                <select class="form-control select2 select2-accessible" id="department_id" style="width: 275px;float:left;border:1px solid #e1e3e9;padding: 2px 12px;" tabindex="-1" aria-hidden="true">
                  <option selected="selected" value="-1" class="i18n">i18n(ldp_i18n_project_list_1005)</option>
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
                <div class="input-group input-group-sm" style="float:right;width: 220px;">
                  <label for="search_param"></label>
                  <input type="text" value="${search!}" id="search_param" class="form-control pull-right" placeholder="Search">
                  <div class="input-group-btn">
                    <button onclick="PROJECT_OPERATOR.search();" type="submit" class="btn btn-default"><i class="fa fa-search"></i></button>
                  </div>
                </div>
              </div>
            </div>
            <div class="box-body">
                <table class="table table-bordered table-hover">
                  <tr>
                    <th></th>
                    <th>ID</th>
                    <th>ProjectName</th>
                    <th>Description</th>
                    <th>Admins</th>
                    <th>Department</th>
                    <th>CreateTime</th>
                    <th style="width: 200px;">Operation</th>
                  </tr>
                  <#if listObject.dataList ??>
                    <#list listObject.dataList as entity>
                      <tr>
                        <#if entity.favorite?string("true","false") == "true">
                          <td class="mailbox-star" style="font-size:14px;text-align:center;width:50px;">
                            <a href="javascript:FAVORITES_OPERATOR.removeFavoritesConfirm('${entity.id}','2','${entity.name}',document.URL);"><i class="fa fa-star text-yellow"></i></a>
                          </td>
                        <#else>
                          <td class="mailbox-star" style="font-size:14px;text-align:center;width:50px;">
                            <a href="javascript:FAVORITES_OPERATOR.favoritesConfirm('${entity.id}','2','${entity.name}',document.URL);"><i class="fa fa-star-o text-yellow"></i></a>
                          </td>
                        </#if>
                        <td>${entity.id}</td>
                        <td>
                          ${entity.name}
                          <#if entity.privateFlag == '1'>
                              <i class="fa fa-fw fa-lock i18n" title="i18n(ldp_i18n_project_list_1007)"></i>
                          </#if>
                        </td>
                        <td>
                          ${entity.desc}
                        </td>
                        <td>
                          <#if entity.admins??>
                              <#list entity.admins as userEntity>
                                <a href="javascript:MsgBox.Alert('UserName：${userEntity.userName};<br>Phone：${userEntity.phone};<br>Email：${userEntity.email};');">${userEntity.userName};</a>
                              </#list>
                          </#if>
                        </td>
                          <td title="${entity.fullDepartmentName}">
                          ${entity.departmentName}
                        </td>
                        <td>
                          ${entity.createTime?string('yyyy-MM-dd HH:mm:ss')}
                        </td>
                        <td>
                          <#if entity.privilegeIds?seq_contains(5) >
                            <span class="col-md-1 col-sm-2" title=""><a class="i18n" href="javascript:PROJECT_OPERATOR.projectView('${entity.id}');" title="i18n(ldp_i18n_project_list_1008)" style="color:#000000;"><i class="fa fa-fw fa-bar-chart"></i></a></span>
                            <span class="col-md-1 col-sm-2" title=""><a class="i18n" href="/project/manage/index.shtml?projectId=${entity.id}" title="i18n(ldp_i18n_project_list_1009)" style="color:#000000;"><i class="fa fa-fw fa-gears"></i></a></span>
                            <span class="col-md-1 col-sm-2" title=""><a class="i18n" href="/project/update/index.shtml?id=${entity.id}" title="i18n(ldp_i18n_project_list_1010)" style="color:#000000;"><i class="fa fa-fw fa fa-pencil"></i></a></span>
                            <span class="col-md-1 col-sm-2" title=""><a class="i18n" href="javascript:PROJECT_OPERATOR.deleteConfirm('${entity.id}','${entity.name}');" title="i18n(ldp_i18n_project_list_1011)" style="color:#000000;"><i class="fa fa-fw fa-minus-circle"></i></a></span>
                          <#elseif entity.privilegeIds?seq_contains(6) >
                            <span class="col-md-1 col-sm-2" title=""><a class="i18n" href="javascript:PROJECT_OPERATOR.projectView('${entity.id}');" title="i18n(ldp_i18n_project_list_1008)" style="color:#000000;"><i class="fa fa-fw fa-bar-chart"></i></a></span>
                          <#else>
                            <span class="col-md-1 col-sm-2" title=""><a class="i18n" href="javascript:APPLY_OPERATOR.privilegeApplyConfirm('${entity.id}','1','${entity.name}',document.URL)" title="i18n(ldp_i18n_project_list_1012)" style="color:#000000;"><i class="fa fa-fw fa-user-plus"></i></a></span>
                            <#if entity.apply?string("true","false") == "true">
                              <span class="i18n" style="font-size: 10px;color: #40c62a;">（i18n(ldp_i18n_project_list_1013)）</span>
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
<script src="/static/js/apply/apply_operator.js" charset="utf-8"></script>
<script src="/static/js/favorites/favorites_operator.js"></script>
<script src="/static/js/project/project_operator.js" charset="utf-8"></script>
</body>
</html>
