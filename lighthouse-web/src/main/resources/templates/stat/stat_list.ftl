<html lang="UTF-8">
<head>
  <#import "../common/frame.ftl" as baseFrame>
  <title class="i18n" i18n_code="ldp_i18n_stat_list_1000"></title>
  <@baseFrame.top />
</head>
<body class="skin-blue sidebar-mini sidebar-collapse">
<div class="wrapper" style="overflow: hidden;min-height: 750px;">
    <@baseFrame.header />
    <@baseFrame.leftMenu />
    <@baseFrame.rightMenu />
  <div class="content-wrapper">
    <section class="content-header">
        <h1>
          <samp class="i18n" i18n_code="ldp_i18n_stat_list_1001"></samp>
        </h1>
        <ol class="breadcrumb">
          <li><a href="/index.shtml"><i class="fa fa-dashboard"></i> Home</a></li>
          <li class="active"><samp class="i18n" i18n_code="ldp_i18n_stat_list_1001"></samp></li>
        </ol>
      </section>
    <section class="content">
      <div class="row">
        <div class="col-md-12">
          <div class="box">
            <div class="mailbox-controls">
              <div style="float: right;width: 750px;margin-bottom:5px;">
                <label for="department_id"></label>
                <select class="form-control select2 select2-accessible ssd" id="department_id" style="width: 280px;float:left;height: 29px;border:1px solid #e1e3e9;padding: 4px 12px;" tabindex="-1" aria-hidden="true">
                  <option selected="selected" value="-1" class="i18n" i18n_code="ldp_i18n_stat_list_1002"></option>
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
                <label for="project_id"></label>
                <select class="form-control select2 select2-accessible ssd" id="project_id" style="width: 250px;float:left;height: 29px;border:1px solid #e1e3e9;padding: 4px 12px;" tabindex="-1" aria-hidden="true">
                  <option selected="selected" value="-1" class="i18n" i18n_code="ldp_i18n_stat_list_1003"></option>
                  <#if projectList??>
                    <#list projectList as project>
                      <#if project.id == projectId>
                        <option selected value="${project.id}">${project.name}</option>
                      <#else>
                        <option value="${project.id}">${project.name}</option>
                      </#if>
                    </#list>
                  </#if>
                </select>
                <div class="input-group input-group-sm" style="width: 200px;float:right;">
                  <label for="search_param"></label>
                  <input type="text" id="search_param" class="form-control pull-right" value="${search!}" placeholder="Search">
                  <div class="input-group-btn">
                    <button type="submit" class="btn btn-default" onclick="STAT_OPERATOR.search();"><i class="fa fa-search"></i></button>
                  </div>
                </div>
              </div>
            </div>
            <div class="box-body">
              <table class="table table-bordered table-hover">
                <tr>
                  <th></th>
                  <th>ID</th>
                  <th>Title</th>
                  <th>Department</th>
                  <th>Project</th>
                  <th>Token</th>
                  <th>TimeParam</th>
                  <th>Admins</th>
                  <th>CreateTime</th>
                  <th>Status</th>
                  <th>Operation</th>
                </tr>
                <#if listObject.dataList??>
                  <#list listObject.dataList as entity>
                  <tr>
                      <#if entity.favorite?string("true","false") == "true">
                        <td class="mailbox-star" style="padding-top:5px;font-size:14px;text-align:center;width:50px;">
                          <a href="javascript:FAVORITES_OPERATOR.removeFavoritesConfirm('${entity.id}',1,'${entity.title}',document.URL);"><i class="fa fa-star text-yellow"></i></a>
                        </td>
                      <#else>
                        <td class="mailbox-star" style="padding-top:5px;font-size:14px;text-align:center;width:50px;">
                          <a href="javascript:FAVORITES_OPERATOR.favoritesConfirm('${entity.id}',1,'${entity.title}',document.URL);"><i class="fa fa-star-o text-yellow"></i></a>
                        </td>
                      </#if>
                    <td>
                        ${entity.id}
                    </td>
                    <td>
                        ${entity.title}
                    </td>
                    <td title="${entity.fullDepartmentName}">
                        ${entity.departmentName}
                    </td>
                    <td>
                      [${entity.projectId}]${entity.projectName}
                      <#if entity.privateFlag == '1'>
                        <i class="fa fa-fw fa-lock i18n" title="i18n(ldp_i18n_project_list_1007)"></i>
                      </#if>
                    </td>
                    <td>
                      [${entity.groupId}]${entity.token}
                    </td>
                    <td>
                      ${entity.timeParam}
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
                        <#if entity.state == 1>
                          <span style="color:#08c410;font-size: 15px;font-weight: bold;">Running</span>
                        <#elseif entity.state == 3>
                          <span style="color:#538be2;font-size: 15px;font-weight: bold;">Limiting</span>
                        <#elseif entity.state == 5>
                          <span style="color:#538be2;font-size: 15px;font-weight: bold;">Rejected</span>
                        <#else>
                          <span style="color:#843709;font-size: 15px;font-weight: bold;">Stopped</span>
                        </#if>
                    </td>
                    <td>
                        <#if entity.privilegeIds?seq_contains(7) >
                          <span class="col-md-1 col-sm-2 i18n" title="i18n(ldp_i18n_stat_list_1006)"><a href="/display/stat.shtml?statId=${entity.id}" style="color:#000000;" target="_blank"><i class="fa fa-fw fa-bar-chart"></i></a></span>
                        <#else>
                            <span class="col-md-1 col-sm-2 i18n" title="i18n(ldp_i18n_stat_list_1007)"><a href="javascript:APPLY_OPERATOR.privilegeApplyConfirm('${entity.id}','2','${entity.title}',document.URL)" style="color:#000000;"><i class="fa fa-fw fa-user-plus"></i></a></span>
                            <#if entity.apply?string("true","false") == "true">
                              <span style="font-size: 10px;color: #40c62a;" class="i18n">（i18n(ldp_i18n_stat_list_1008)）</span>
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
<script type="text/javascript" src="/static/js/stat/stat_operator.js"></script>
<script src="/static/js/apply/apply_operator.js" charset="utf-8"></script>
<script type="text/javascript" src="/static/js/favorites/favorites_operator.js"></script>
</body>
</html>
