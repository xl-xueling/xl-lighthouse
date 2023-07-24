<html lang="UTF-8">
<head>
  <#import "../common/frame.ftl" as baseFrame>
  <title class="i18n" i18n_code="ldp_i18n_department_list_1000"></title>
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
        <samp class="i18n" i18n_code="ldp_i18n_department_list_1001"></samp>
      </h1>
      <ol class="breadcrumb">
        <li><a href="/index.shtml"><i class="fa fa-dashboard"></i> Home</a></li>
        <li class="active"><samp class="i18n" i18n_code="ldp_i18n_department_list_1002"></samp></li>
      </ol>
    </section>
    <section class="content">
      <div class="row">
        <div class="col-md-12">
          <div class="box">
            <div class="mailbox-controls">
              <div class="btn-group">
                <a href="/department/manage/index.shtml" class="i18n" title="i18n(ldp_i18n_department_list_1003)">
                  <button type="button" class="btn btn-default btn-sm"><i class="fa fa-pencil">&nbsp;&nbsp;<samp class="i18n" i18n_code="ldp_i18n_department_list_1003"></samp></i></button>
                </a>
              </div>
              <div style="float: right;width: 430px;">
                <label for="level"></label>
                <select id="level" class="form-control2 select2" style="width: 220px;float:left;height: 26px;border:1px solid #e1e3e9;">
                    <#if level == -1>
                      <option value="-1" selected="selected" class="i18n" i18n_code="ldp_i18n_department_list_1004"></option>
                    <#else >
                      <option value="-1" class="i18n" i18n_code="ldp_i18n_department_list_1004"></option>
                    </#if>
                    <#if level == 1>
                      <option selected value="1" class="i18n" i18n_code="ldp_i18n_department_list_1005"></option>
                    <#else >
                      <option value="1" class="i18n" i18n_code="ldp_i18n_department_list_1005"></option>
                    </#if>
                  <#if level == 2>
                      <option selected value="2" class="i18n" i18n_code="ldp_i18n_department_list_1006"></option>
                    <#else >
                      <option value="2" class="i18n" i18n_code="ldp_i18n_department_list_1006"></option>
                    </#if>
                  <#if level == 3>
                      <option selected value="3" class="i18n" i18n_code="ldp_i18n_department_list_1007"></option>
                    <#else>
                      <option value="3" class="i18n" i18n_code="ldp_i18n_department_list_1007"></option>
                  </#if>
                  <#if level == 4>
                      <option selected value="4" class="i18n" i18n_code="ldp_i18n_department_list_1008"></option>
                    <#else >
                      <option value="4" class="i18n" i18n_code="ldp_i18n_department_list_1008"></option>
                  </#if>
                </select>
                <div class="input-group input-group-sm" style="width: 200px;float:right;">
                  <label for="search_param"></label>
                  <input type="text" value="${search!}" id="search_param" class="form-control pull-right" placeholder="Search"/>
                  <div class="input-group-btn">
                    <button onclick="DEPARTMENT_OPERATOR.search();" type="submit" class="btn btn-default"><i class="fa fa-search"></i></button>
                  </div>
                </div>
              </div>
            </div>
            <div class="box-body">
              <table class="table table-bordered table-hover">
                  <tr>
                    <th>ID</th>
                    <th>Department</th>
                    <th>Level</th>
                    <th>FullPath</th>
                    <th>CreateTime</th>
                    <th>UpdateTime</th>
                  </tr>
                  <#if listObject.dataList??>
                      <#list listObject.dataList as department>
                          <tr>
                              <td>${department.id}</td>
                              <td>${department.name}</td>
                              <td>
                                  <#if department.level == 1 >
                                      <samp class="i18n" i18n_code="ldp_i18n_department_list_1005"></samp>
                                  </#if>
                                  <#if department.level == 2 >
                                      <samp class="i18n" i18n_code="ldp_i18n_department_list_1006"></samp>
                                  </#if>
                                  <#if department.level == 3 >
                                      <samp class="i18n" i18n_code="ldp_i18n_department_list_1007"></samp>
                                  </#if>
                                  <#if department.level == 4 >
                                      <samp class="i18n" i18n_code="ldp_i18n_department_list_1008"></samp>
                                  </#if>
                              </td>
                              <td>
                                  <#if department.fullPathName??>
                                      ${department.fullPathName}
                                  <#else>
                                      --
                                  </#if>
                              </td>
                              <td>
                                  ${department.createTime?string('yyyy-MM-dd HH:mm:ss')}
                              </td>
                              <td>
                                  ${department.updateTime?string('yyyy-MM-dd HH:mm:ss')}
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
<script src="/static/js/department/department_operator.js"></script>
</body>
</html>
