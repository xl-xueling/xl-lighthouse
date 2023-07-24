<html lang="UTF-8">
<#import "../common/frame.ftl" as baseFrame>
<@baseFrame.sub_head />
<div class="nav-tabs-custom nested-sub-page">
  <ul class="nav nav-tabs">
    <li class="active"><a href="#activity1" data-toggle="tab"><b><samp class="i18n" i18n_code="ldp_i18n_department_update_1001"></samp></b></a></li>
  </ul>
  <div class="tab-content">
    <div class="active tab-pane" id="activity1">
      <div class="row">
        <div class="col-md-12">
          <div class="register-box-body">
            <div class="form-group">
              <label for="name"><samp class="i18n" i18n_code="ldp_i18n_department_update_1001"></samp>：</label>
              <input type="hidden" id="id" value="${department.id}">
              <input type="text" class="form-control" id="name" placeholder="Name" value="${department.name}" autocomplete = "off">
            </div>
            <div class="form-group">
              <label for="pid"><samp class="i18n" i18n_code="ldp_i18n_department_update_1002"></samp>：</label>
              <select id="pid" disabled class="form-control select2 select2-hidden-accessible" style="width: 100%;" tabindex="-1" aria-hidden="true">
                  <#if pid != 0>
                    <option value="${pid}">${parentDepart.name}</option>
                  <#else >
                    <option value="0">Default</option>
                  </#if>
              </select>
            </div>
            <div class="row">
              <div class="col-xs-12" style="text-align: center;vertical-align: middle;">
                <a href="javascript:DEPARTMENT_OPERATOR.updateDepartment();">
                  <button  class="btn btn-primary" style="float: right;line-height: 1.42857;vertical-align: middle;"><samp class="i18n" i18n_code="ldp_i18n_department_update_1004"></samp></button>
                </a>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<@baseFrame.sub_tail />