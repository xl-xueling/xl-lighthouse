<#macro publishToSiteMap>
<div class="modal fade pop-dialog" id="publishToSiteMap">
    <div class="modal-dialog" style="overflow-y:auto; overflow-x:auto;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <input type="hidden" id="quote_statId">
                <input type="hidden" id="quote_groupId">
                <input type="hidden" id="quote_projectId">
                <h4 class="modal-title">【<samp class="i18n" i18n_code="ldp_i18n_project_manage_1008"></samp>】</h4>
            </div>
            <div class="modal-body" style="padding: 30px;">
                <div class="box-body no-padding">
                    <div class="form-group">
                        <label for="quote_siteId"></label><span style="color: #9d9b32;font-size:13px;float:right;"></span>
                        <select id="quote_siteId" class="form-control select2 select2-accessible" style="width: 100%;" >
                            <option value="-1">---Select SiteMap---</option>
                            <#if siteMapList??>
                                <#list siteMapList as siteMap>
                                    <#if siteMap??>
                                        <option value="${siteMap.id!''}">/${siteMap.id!''}/${siteMap.name!''}【${siteMap.star!''}-STAR】</option>
                                    </#if>
                                </#list>
                            </#if>
                        </select>
                        <div style="margin-top:20px;font-size: 12px;">
                            <samp class="i18n" i18n_code="ldp_i18n_project_manage_1009"></samp>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <a href="javascript:SITEMAP_OPERATOR.publishToSiteMapConfirm();" class="btn bg-navy btn-xs">
                    <samp class="i18n" i18n_code="ldp_i18n_project_manage_1010"></samp>
                </a>
                <a href="javascript:void(0);" class="btn bg-red btn-xs" data-dismiss="modal">
                    <samp class="i18n" i18n_code="ldp_i18n_project_manage_1011"></samp>
                </a>
            </div>
        </div>
    </div>
</div>
</#macro>


<#macro updateGroupThreshold>
    <script type="text/javascript">
        var groupMessageSizeLimit = '${groupEntity.limitedThresholdMap['GROUP_MESSAGE_SIZE_LIMIT']}';
        var statResultSizeLimit = '${groupEntity.limitedThresholdMap['STAT_RESULT_SIZE_LIMIT']}';
    </script>
    <div class="modal fade pop-sub-page pop-dialog" id="updateGroupThreshold">
        <div class="modal-dialog" style="overflow-y:auto; overflow-x:auto;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title">【<samp class="i18n" i18n_code="ldp_i18n_group_manage_1035"></samp> 】</h4>
                </div>
                <div class="modal-body" style="padding-top:20px;padding-bottom:20px;">
                    <div class="box-body no-padding">
                        <div class="form-group">
                            <label for="originValue"><samp class="i18n" i18n_code="ldp_i18n_group_manage_1036"></samp>：</label><span style="color: #9d9b32;font-size:13px;float:right;"></span>
                            <label for="strategyType"></label>
                            <select id="strategyType" class="form-control select2 select2-hidden-accessible" style="width: 100%;" tabindex="-1" aria-hidden="true" onchange="DISPLAY_OPERATOR.changeStrategy();">
                                <option value="1" selected>GROUP_MESSAGE_SIZE_LIMIT</option>
                                <option value="2">STAT_RESULT_SIZE_LIMIT</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="originValue"><samp class="i18n" i18n_code="ldp_i18n_group_manage_1039"></samp>：</label><span style="color: #9d9b32;font-size:13px;float:right;">Unit：Second</span>
                            <input type="text" class="form-control" id="originValue" disabled autocomplete="off" value="${groupEntity.limitedThresholdMap['GROUP_MESSAGE_SIZE_LIMIT']}">
                        </div>
                        <div class="form-group">
                            <label for="updateValue"><samp class="i18n" i18n_code="ldp_i18n_group_manage_1040"></samp>：</label><span style="color: #9d9b32;font-size:13px;float:right;">Unit：Second</span>
                            <input type="text" class="form-control" id="updateValue" autocomplete="off">
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <a href="javascript:DISPLAY_OPERATOR.updateThreshold('${groupEntity.id}');" class="btn bg-navy btn-xs"><samp class="i18n" i18n_code="ldp_i18n_group_manage_1041"></samp></a>
                    <a href="javascript:void(0);" class="btn bg-red btn-xs" data-dismiss="modal"><samp class="i18n" i18n_code="ldp_i18n_group_manage_1042"></samp></a>
                </div>
            </div>
        </div>
    </div>
</#macro>

<#macro limitedRecordModal ptype>
<script>
    var limitedPage = 1;
</script>
<div class="modal fade pop-sub-page pop-dialog" id="limitedRecordModal">
    <div class="modal-dialog" style="overflow-y:auto;min-height:200px; max-height:600px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">【<samp class="i18n" i18n_code="ldp_i18n_popup_limited_1000"></samp>】</h4>
            </div>
            <div class="modal-body" style="padding-top:10px;padding-bottom:10px;">
                <div class="box-header" style="float: right;">
                        <#if ptype == '1'>
                            <a href="javascript:DISPLAY_OPERATOR.loadGroupLimitedRecord('${groupEntity.id}',true);" class="btn bg-red btn-xs">
                                <samp class="i18n" i18n_code="ldp_i18n_popup_limited_1001"></samp>..
                            </a>
                        <#else >
                            <a href="javascript:DISPLAY_OPERATOR.loadStatLimitedRecord('${statEntity.id}',true);" class="btn bg-red btn-xs">
                                <samp class="i18n" i18n_code="ldp_i18n_popup_limited_1001"></samp>
                                ..</a>
                        </#if>
                </div>
                <div class="box-body no-padding">
                    <table class="table">
                        <tbody id="limited_tbody">
                        <tr>
                            <th style="width: 60px">ID</th>
                            <th>Strategy</th>
                            <th>StartTime</th>
                            <th>EndTime</th>
                            <th>Duration</th>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="modal-footer">
            </div>
        </div>
    </div>
</div>
</#macro>

<#macro siteMapPreviewModal>
<div class="modal fade pop-dialog" id="siteMapPreviewModal">
    <div class="modal-dialog" style="overflow-y:auto; overflow-x:auto; min-height:400px; max-height:700px;width: 1200px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">【<samp class="i18n" i18n_code="ldp_i18n_sitemap_preview_1001"></samp> 】</h4>
            </div>
            <div class="modal-body" style="padding-top:10px;padding-bottom:10px;">
                <div class="box-body no-padding">
                    <div id="mainChart" style="width: 1000px; height: 600px;"></div>
                </div>
            </div>
            <div class="modal-footer">
            </div>
        </div>
    </div>
</div>
</#macro>

<#macro siteBindUpdate>
    <div class="modal fade pop-sub-page pop-dialog" id="siteBindUpdateModal">
    <div class="modal-dialog" style="overflow-y:auto; overflow-x:auto;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <input type="hidden" id="bind_siteId">
                <input type="hidden" id="bind_id">
                <h4 class="modal-title">【<samp class="i18n" i18n_code="ldp_i18n_sitebind_update_1001"></samp> 】</h4>
            </div>
            <div class="modal-body" style="padding: 30px;">
                <div class="box-body no-padding">
                    <div class="form-group">
                        <label for="bind_name"><samp class="i18n" i18n_code="ldp_i18n_sitebind_update_1002"></samp>：</label>
                        <input type="text" class="form-control" id="bind_name" autocomplete="off">
                    </div>
                    <div class="form-group">
                        <label for="element_name"><samp class="i18n" i18n_code="ldp_i18n_sitebind_update_1003"></samp>：</label>
                        <a id="element_link" target="_blank"><i class="fa fa-fw fa-link" style="font-size: 8px;"></i></a>
                        <input type="text" class="form-control" id="element_name" disabled autocomplete="off">
                    </div>
                    <div class="form-group">
                        <label for="sitebind_path"><samp class="i18n" i18n_code="ldp_i18n_sitebind_update_1004"></samp>：</label><span style="color: #9d9b32;font-size:13px;float:right;"></span>
                        <select id="sitebind_path" class="form-control select2" style="width: 100%;" >
                        </select>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <a href="javascript:SITEBIND_OPERATOR.updateSubmitConfirm();" class="btn bg-navy btn-xs"><samp class="i18n" i18n_code="ldp_i18n_sitebind_update_1005"></samp></a>
                <a href="javascript:void(0);" class="btn bg-red btn-xs" data-dismiss="modal"><samp class="i18n" i18n_code="ldp_i18n_sitebind_update_1006"></samp></a>
            </div>
        </div>
    </div>
</div>
</#macro>

<#macro statUpdateModal>
    <div class="modal fade pop-dialog" id="statUpdateModal">
        <div class="modal-dialog" style="overflow-y:auto; overflow-x:auto; min-height:400px; max-height:700px;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <input type="hidden" class="form-control" id="stat_id">
                    <input type="hidden" class="form-control" id="group_id">
                    <input type="hidden" class="form-control" id="project_id">
                    <h4 class="modal-title">【<samp class="i18n" i18n_code="ldp_i18n_stat_update_1001"></samp> 】</h4>
                </div>
                <div class="modal-body" style="padding: 30px;">
                    <div class="box-body no-padding">
                        <div class="form-group">
                            <label for="stat_title"><samp class="i18n" i18n_code="ldp_i18n_stat_update_1002"></samp>：</label>
                            <input type="text" class="form-control" id="stat_title" autocomplete="off">
                        </div>
                        <div class="form-group">
                            <label for="stat_template"><samp class="i18n" i18n_code="ldp_i18n_stat_update_1003"></samp>：</label>
                            <input type="text" class="form-control" id="stat_template" disabled autocomplete="off">
                        </div>
                        <div class="form-group">
                            <label for="stat_timeparam"><samp class="i18n" i18n_code="ldp_i18n_stat_update_1004"></samp>：</label><span style="color: #9d9b32;font-size:13px;float:right;"></span>
                            <select id="stat_timeparam" disabled class="form-control select2 select2-hidden-accessible" style="width: 100%;" tabindex="-1" aria-hidden="true">
                                <option value="1-minute" class="i18n" i18n_code="ldp_i18n_group_create_1010"></option>
                                <option value="2-minute" class="i18n" i18n_code="ldp_i18n_group_create_1011"></option>
                                <option value="3-minute" class="i18n" i18n_code="ldp_i18n_group_create_1012"></option>
                                <option value="5-minute" class="i18n" i18n_code="ldp_i18n_group_create_1013"></option>
                                <option value="10-minute" class="i18n" i18n_code="ldp_i18n_group_create_1014"></option>
                                <option value="20-minute" class="i18n" i18n_code="ldp_i18n_group_create_1015"></option>
                                <option value="30-minute" class="i18n" i18n_code="ldp_i18n_group_create_1016"></option>
                                <option value="1-hour" class="i18n" i18n_code="ldp_i18n_group_create_1017"></option>
                                <option value="2-hour" class="i18n" i18n_code="ldp_i18n_group_create_1018"></option>
                                <option value="3-hour" class="i18n" i18n_code="ldp_i18n_group_create_1019"></option>
                                <option value="1-day" class="i18n" i18n_code="ldp_i18n_group_create_1020"></option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="stat_expire"><samp class="i18n" i18n_code="ldp_i18n_stat_update_1005"></samp>：</label><span style="color: #9d9b32;font-size:13px;float:right;"></span>
                            <select id="stat_expire" autocomplete="off" class="form-control select2 select2-hidden-accessible stat_data_expire" style="width: 100%;" tabindex="-1" aria-hidden="true">
                                <option value="259200" class="i18n" i18n_code="ldp_i18n_group_create_1021"></option>
                                <option value="604800" class="i18n" i18n_code="ldp_i18n_group_create_1022"></option>
                                <option value="1209600" class="i18n" i18n_code="ldp_i18n_group_create_1023"></option>
                                <option value="2592000" class="i18n" i18n_code="ldp_i18n_group_create_1024"></option>
                                <option value="7776000" class="i18n" i18n_code="ldp_i18n_group_create_1025"></option>
                                <option value="15552000" class="i18n" i18n_code="ldp_i18n_group_create_1026"></option>
                                <option value="31104000" class="i18n" i18n_code="ldp_i18n_group_create_1027"></option>
                                <option value="62208000" class="i18n" i18n_code="ldp_i18n_group_create_1028"></option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <a href="javascript:STAT_OPERATOR.updateConfirm();" class="btn bg-navy btn-xs"><samp class="i18n" i18n_code="ldp_i18n_stat_update_1006"></samp></a>
                    <a href="javascript:void(0);" class="btn bg-red btn-xs" data-dismiss="modal"><samp class="i18n" i18n_code="ldp_i18n_stat_update_1007"></samp></a>
                </div>
            </div>
        </div>
    </div>
</#macro>

<#macro filterConfig>
    <div class="modal fade pop-dialog" id="popup_filter_config">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title">【<samp class="i18n" i18n_code="ldp_i18n_display_1040"></samp> 】 --  &nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:FILTER_OPERATOR.addSystemComponents();" class="i18n">+&nbsp;i18n(ldp_i18n_display_1041)</a> &nbsp;&nbsp;&nbsp;&nbsp; <a href="javascript:FILTER_OPERATOR.addCustomComponents();" class="i18n">+&nbsp;i18n(ldp_i18n_display_1042)</a></h4>
                </div>
                <section class = "content">
                    <div class="row">
                        <div class="col-md-12">
                            <form class="layui-form" method="post">
                                <table class="table" style="border: none;">
                                    <thead>
                                    <tr>
                                        <th style="height: 35px;border: none;vertical-align:top;text-align:center;">Label</th>
                                        <th style="height: 35px;border: none;vertical-align:top;text-align:center;">FilterParam</th>
                                        <th style="height: 35px;border: none;vertical-align:top;width: 354px;!important;text-align:center;">Display</th>
                                        <th style="height: 35px;border: none;vertical-align:top;width: 80px;text-align:center;">Operate</th>
                                    </tr>
                                    </thead>
                                    <tbody class="layui-form-item filter_change_components">
                                    </tbody>
                                </table>
                            </form>
                            <div class="modal-footer" style="text-align: center;">
                                <a href="javascript:FILTER_OPERATOR.change('${statEntity.id}');" class="btn bg-purple btn-xs"><samp class="i18n" i18n_code="ldp_i18n_display_1043"></samp></a>
                                <a href="javascript:FILTER_OPERATOR.reset('${statEntity.id}');" class="btn bg-red btn-xs"><samp class="i18n" i18n_code="ldp_i18n_display_1044"></samp></a>
                            </div>
                        </div>
                    </div>
                </section>
            </div>
        </div>
    </div>

    <div class="modal fade change_components pop-dialog" id="custom_components">
        <div class="modal-dialog1">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title">【<samp class="i18n" i18n_code="ldp_i18n_display_1042"></samp>】</h4>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <form class="layui-form" method="post">
                            <table class="table" style="border: none;">
                                <thead>
                                <tr>
                                    <th style="border: none;"></th>
                                    <th style="border: none;width: 354px;!important;"></th>
                                    <th style="border: none;width: 80px;"></th>
                                </tr>
                                </thead>
                                <tbody class="layui-form-item filter_custom_components">
                                </tbody>
                            </table>
                        </form>
                        <div style="width: 100%;text-align: center;margin:20px auto;">
                            <div id="custom_filter_page"></div>
                            <input type="hidden" id="custom_page_index" value="1"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>


    <div class="modal fade change_components pop-dialog" id="system_components">
        <div class="modal-dialog1">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title">【<samp class="i18n" i18n_code="ldp_i18n_display_1055"></samp>】</h4>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <form class="layui-form" method="post">
                            <table class="table" style="border: none;">
                                <thead>
                                <tr>
                                    <th style="border: none;"></th>
                                    <th style="border: none;width: 354px;!important;"></th>
                                    <th style="border: none;width: 80px;"></th>
                                </tr>
                                </thead>
                                <tbody class="layui-form-item filter_system_components">
                                </tbody>
                            </table>
                        </form>
                        <div style="width: 100%;text-align: center;margin:20px auto;">
                            <div id="system_filter_page"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        /**
        var filterComponentsSize = '3';
        if(filterComponentsSize > 0){
            layui.use(['laypage', 'layer'], function(){
                var laypage = layui.laypage
                    ,layer = layui.layer;
                laypage.render({
                    elem: 'system_filter_page'
                    ,count: filterComponentsSize
                    ,jump: function(obj,first){
                        if(!first){
                            let curPage = obj.curr;
                            $("#custom_page_index").val(curPage);
                            FILTER_OPERATOR.addCustomComponents(curPage);
                        }
                    }
                });
            });
        }
         **/
    </script>
</#macro>

