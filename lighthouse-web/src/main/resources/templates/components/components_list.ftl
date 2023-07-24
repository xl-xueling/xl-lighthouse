<html lang="UTF-8">
<head>
  <#import "../common/frame.ftl" as baseFrame>
  <title class="i18n">i18n(ldp_i18n_components_list_1000)</title>
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
        <samp class="i18n" i18n_code="ldp_i18n_components_list_1001"></samp>
      </h1>
      <ol class="breadcrumb">
        <li><a href="/index.shtml"><i class="fa fa-dashboard"></i>Home</a></li>
        <li class="active"><samp class="i18n" i18n_code="ldp_i18n_components_list_1001"></samp></li>
      </ol>
    </section>
    <section class="content">
      <div class="row">
        <div class="col-md-12">
          <div class="box" style="min-height: 100px;">
            <div class="mailbox-controls">
              <div class="btn-group">
                <a href="/components/register/index.shtml" class="i18n" title="i18n(ldp_i18n_components_list_1002)">
                  <button type="button" class="btn btn-default btn-sm"><i class="fa fa-plus">&nbsp;&nbsp;
                    <samp class="i18n" i18n_code="ldp_i18n_components_list_1002"></samp>
                  </i></button>
                </a>
              </div>
              <div style="float: right;width: 610px;margin-bottom:5px;">
                <div class="input-group input-group-sm" style="width: 200px;float:right;">
                  <label for="components_search"></label>
                  <input type="text" id="components_search" class="form-control pull-right" value="${search!}" placeholder="Search">
                  <div class="input-group-btn">
                    <button type="submit" class="btn btn-default" onclick="COMPONENTS_OPERATOR.search();"><i class="fa fa-search"></i></button>
                  </div>
                </div>
              </div>
            </div>
            <div class="box-body">
              <form class="layui-form" method="post">
                <table class="table table-bordered">
                    <tr>
                      <th style="width: 10%">ID</th>
                      <th style="width: 20%">Title</th>
                      <th style="width: 356px;!important;">Display</th>
                      <th>CreateUser</th>
                      <th>CreateTime</th>
                      <th>Operation</th>
                    </tr>
                    <#if listObject.dataList??>
                      <#list listObject.dataList as entity>
                        <tr>
                          <td>
                            ${entity.id}
                          </td>
                          <td>
                            ${entity.title}
                            <#if entity.privateFlag == 1>
                              <i class="fa fa-fw fa-lock i18n" title="Private Component"></i>
                            </#if>
                          </td>
                          <td>
                            <div class="filter_components"  id="filter_${entity.id}"></div>
                          </td>
                          <td>
                            <#if entity.userEntity??>
                              <a href="javascript:MsgBox.Alert('UserName：${entity.userEntity.userName};<br>Phone：${entity.userEntity.phone};<br>Email：${entity.userEntity.email};');">${entity.userEntity.userName};</a>
                            <#else >
                              --
                            </#if>
                          </td>
                          <td>
                            ${entity.createTime?string('yyyy-MM-dd HH:mm:ss')}
                          </td>
                          <td>
                            <#if Session["user"].id == entity.userId>
                              <span class="i18n" title="i18n(ldp_i18n_components_list_1005)"><a href="/components/update/index.shtml?id=${entity.id}"><i class="fa fa-fw fa fa-pencil"></i></a></span>
                              <span class="i18n" title="i18n(ldp_i18n_components_list_1006)"><a href="javascript:COMPONENTS_OPERATOR.privilegeApplyConfirm('${entity.title}','${entity.id}')"><i class="fa fa-fw fa-minus-circle"></i></a></span>
                            </#if>
                          </td>
                        </tr>
                      </#list>
                    </#if>
                  </table>
              </form>
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
<script type="text/javascript" src="/static/js/components/components_operator.js" charset="utf-8"></script>
<script type="text/javascript" src="/static/js/filter/filter_operator.js" charset="utf-8"></script>
<script>
  COMPONENTS_OPERATOR.initList();
</script>
</body>
</html>
