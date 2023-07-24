<html lang="UTF-8">
<head>
  <#import "../common/frame.ftl" as baseFrame>
  <title class="i18n">i18n(ldp_i18n_snapshot_list_1000)</title>
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
        <samp class="i18n" i18n_code="ldp_i18n_snapshot_list_1001"></samp>
      </h1>
      <ol class="breadcrumb">
        <li><a href="/index.shtml"><i class="fa fa-dashboard"></i> Home</a></li>
        <li class="active"><samp class="i18n" i18n_code="ldp_i18n_snapshot_list_1001"></samp></li>
      </ol>
    </section>
    <section class="content">
      <div class="row">
        <div class="col-md-6">
          <div class="box">
            <div class="box-body">
              <table class="table table-bordered table-hover">
                  <tr>
                    <th>ID</th>
                    <th>batch</th>
                    <th>filesize</th>
                    <th>state</th>
                    <th>cost</th>
                  </tr>
                  <#if listObject.dataList??>
                    <#list listObject.dataList as entity>
                      <tr>
                        <td>
                          ${entity.id}
                        </td>
                        <td>
                          ${entity.batchTime?string('yyyy-MM-dd HH:mm:ss')}
                        </td>
                        <td>
                            <#if entity.fileSize??>
                              ${entity.fileSize}
                            <#else >
                              --
                            </#if>
                        </td>
                        <td>
                            <#if entity.state == 0>
                              <samp class="i18n" i18n_code="ldp_i18n_snapshot_list_1002"></samp>
                            <#elseif entity.state == 1>
                              <samp class="i18n" i18n_code="ldp_i18n_snapshot_list_1003"></samp>
                            <#elseif entity.state == 2>
                              <samp class="i18n" i18n_code="ldp_i18n_snapshot_list_1004"></samp>
                            </#if>
                        </td>
                        <td>
                          ${entity.cost} <samp class="i18n" i18n_code="ldp_i18n_snapshot_list_1005"></samp>
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
<script type="text/javascript" src="/static/js/department/department_operator.js"></script>
</body>
</html>
