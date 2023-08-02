<#import "../common/frame.ftl" as baseFrame>
<#import "../common/pop.ftl" as popFrame>
<@baseFrame.sub_head />
<div class="row nested-sub-page">
    <div class="col-md-12">
        <div class="box">
            <section class="content-header">
                <h1 style="color:#57524d;font-size: 18px;">
                    <samp class="i18n" i18n_code="ldp_i18n_group_manage_1001"></samp>
                </h1>
            </section>
            <div class="mailbox-controls" style="float: right;">
                <input type="hidden" id="groupId" value="${groupEntity.id}">
            </div>
            <div class="box-body">
                <table class="table table-bordered table-hover">
                    <tr>
                        <th>ID</th>
                        <th>Title</th>
                        <th>TimeParam</th>
                        <th>Expire</th>
                        <th>CreateTime</th>
                        <th>State</th>
                        <th style="width: 15%">Operation</th>
                    </tr>
                    <#if itemList??>
                        <#list itemList as statItem>
                            <tr>
                                <td title="${statItem.templateOfHtml}">
                                    ${statItem.id}
                                </td>
                                <td title="${statItem.templateOfHtml}">
                                    ${statItem.title}
                                </td>
                                <td>
                                    ${statItem.timeParam}
                                </td>
                                <td>
                                    <#if statItem.dataExpire == 259200>
                                        <samp class="i18n" i18n_code="ldp_i18n_group_create_1021"></samp>
                                    <#elseif statItem.dataExpire == 604800>
                                        <samp class="i18n" i18n_code="ldp_i18n_group_create_1022"></samp>
                                    <#elseif statItem.dataExpire == 1209600>
                                        <samp class="i18n" i18n_code="ldp_i18n_group_create_1023"></samp>
                                    <#elseif statItem.dataExpire == 2592000>
                                        <samp class="i18n" i18n_code="ldp_i18n_group_create_1024"></samp>
                                    <#elseif statItem.dataExpire == 7776000>
                                        <samp class="i18n" i18n_code="ldp_i18n_group_create_1025"></samp>
                                    <#elseif statItem.dataExpire == 15552000>
                                        <samp class="i18n" i18n_code="ldp_i18n_group_create_1026"></samp>
                                    <#elseif statItem.dataExpire == 31104000>
                                        <samp class="i18n" i18n_code="ldp_i18n_group_create_1027"></samp>
                                    <#elseif statItem.dataExpire == 62208000>
                                        <samp class="i18n" i18n_code="ldp_i18n_group_create_1028"></samp>
                                    <#else >
                                        <samp class="i18n" i18n_code="ldp_i18n_group_create_1022"></samp>
                                    </#if>
                                </td>
                                <td>
                                    ${statItem.createTime?string('yyyy-MM-dd HH:mm:ss')}
                                </td>
                                <td class="i18n">
                                    <#if statItem.state == 1>
                                        <span title="${statItem.statStateEnum.desc}" style="color:#08c410;font-size: 14px;font-weight: bold;">Running</span>
                                    <#elseif statItem.state == 3>
                                        <span title="${statItem.statStateEnum.desc}" style="color:#538be2;font-size: 14px;font-weight: bold;">Limiting</span>
                                    <#elseif statItem.state == 0>
                                        <span title="${statItem.statStateEnum.desc}" style="color:#538be2;font-size: 14px;font-weight: bold;">Pending</span>
                                    <#elseif statItem.state == 5>
                                        <span title="${statItem.statStateEnum.desc}" style="color:#843709;font-size: 14px;font-weight: bold;">Rejected</span>
                                    <#elseif statItem.state == 6>
                                        <span title="${statItem.statStateEnum.desc}" style="color:#843709;font-size: 14px;font-weight: bold;">Frozen</span>
                                    <#else>
                                        <span title="${statItem.statStateEnum.desc}" style="color:#843709;font-size: 14px;font-weight: bold;">Stopped</span>
                                    </#if>
                                </td>
                                <td style="padding-top:5px;">
                                    <#if (statItem.state == 0 || statItem.state == 5)>
                                        <span class="col-md-1 col-sm-2 i18n" title="i18n(ldp_i18n_group_manage_1016)"><a href="javascript:STAT_OPERATOR.deleteStatConfirm('${statItem.id}','${statItem.title}','${statItem.groupId}');" style="color:#000000;"><i class="fa fa-fw fa-minus-circle"></i></a></span>
                                    <#else>
                                        <span class="col-md-1 col-sm-2 i18n" title="i18n(ldp_i18n_group_manage_1011)"><a onclick="window.open(Encrypt.encryptUrl('/display/stat.shtml?statId=${statItem.id}'),'_blank');" style="color:#000000;"><i class="fa fa-fw fa-bar-chart"></i></a></span>
                                        <#if Session["user"].userName == 'admin'>
                                            <#if statItem.state == 6>
                                                <span class="col-md-1 col-sm-2 i18n" title="i18n(ldp_i18n_group_manage_1012)"><a href="javascript:STAT_OPERATOR.enableStatConfirm('${statItem.id}','${statItem.title}','${statItem.groupId}');" style="color:#000000;"><i class="fa fa-fw fa-play"></i></a></span>
                                                <span class="col-md-1 col-sm-2 i18n" title="i18n(ldp_i18n_group_manage_1016)"><a href="javascript:STAT_OPERATOR.deleteStatConfirm('${statItem.id}','${statItem.title}','${statItem.groupId}');" style="color:#000000;"><i class="fa fa-fw fa-minus-circle"></i></a></span>
                                            <#elseif statItem.state == 3>
                                                <span class="col-md-1 col-sm-2 i18n" title="i18n(ldp_i18n_group_manage_1012)"><a href="javascript:STAT_OPERATOR.enableStatConfirm('${statItem.id}','${statItem.title}','${statItem.groupId}');" style="color:#000000;"><i class="fa fa-fw fa-play"></i></a></span>
                                                <span class="col-md-1 col-sm-2 i18n" title="i18n(ldp_i18n_group_manage_1013)"><a href="javascript:STAT_OPERATOR.frozenStatConfirm('${statItem.id}','${statItem.title}','${statItem.groupId}');" style="color:#000000;"><i class="fa fa-fw fa-empire"></i></a></span>
                                            <#else>
                                                <span class="col-md-1 col-sm-2 i18n" title="i18n(ldp_i18n_group_manage_1013)"><a href="javascript:STAT_OPERATOR.frozenStatConfirm('${statItem.id}','${statItem.title}','${statItem.groupId}');" style="color:#000000;"><i class="fa fa-fw fa-empire"></i></a></span>
                                            </#if>
                                        <#else >
                                            <#if (statItem.state == 1)>
                                                <span class="col-md-1 col-sm-2 i18n" title="i18n(ldp_i18n_group_manage_1015)"><a href="javascript:STAT_OPERATOR.disableStatConfirm('${statItem.id}','${statItem.title}','${statItem.groupId}');" style="color:#000000;"><i class="fa fa-fw fa-pause"></i></a></span>
                                                <span class="col-md-1 col-sm-2 i18n" title="i18n(ldp_i18n_group_manage_1016)"><a href="javascript:MsgBox.Alert($.i18n.prop('ldp_i18n_group_manage_1021'))" style="color:#cabebe;"><i class="fa fa-fw fa-minus-circle"></i></a></span>
                                            <#elseif statItem.state == 3>
                                                <span class="col-md-1 col-sm-2 i18n" title="i18n(ldp_i18n_group_manage_1012)"><a href="javascript:STAT_OPERATOR.enableStatConfirm('${statItem.id}','${statItem.title}','${statItem.groupId}');" style="color:#000000;"><i class="fa fa-fw fa-play"></i></a></span>
                                                <span class="col-md-1 col-sm-2 i18n" title="i18n(ldp_i18n_group_manage_1015)"><a href="javascript:STAT_OPERATOR.disableStatConfirm('${statItem.id}','${statItem.title}','${statItem.groupId}');" style="color:#000000;"><i class="fa fa-fw fa-pause"></i></a></span>
                                                <span class="col-md-1 col-sm-2 i18n" title="i18n(ldp_i18n_group_manage_1016)"><a href="javascript:MsgBox.Alert($.i18n.prop('ldp_i18n_group_manage_1021'))" style="color:#cabebe;"><i class="fa fa-fw fa-minus-circle"></i></a></span>
                                            <#elseif statItem.state == 2>
                                                <span class="col-md-1 col-sm-2 i18n" title="i18n(ldp_i18n_group_manage_1012)"><a href="javascript:STAT_OPERATOR.enableStatConfirm('${statItem.id}','${statItem.title}','${statItem.groupId}');" style="color:#000000;"><i class="fa fa-fw fa-play"></i></a></span>
                                                <span class="col-md-1 col-sm-2 i18n" title="i18n(ldp_i18n_group_manage_1016)"><a href="javascript:STAT_OPERATOR.deleteStatConfirm('${statItem.id}','${statItem.title}','${statItem.groupId}');" style="color:#000000;"><i class="fa fa-fw fa-minus-circle"></i></a></span>
                                            </#if>
                                            <#if (statItem.state == 0 || statItem.state == 5 || statItem.state == 6)>
                                                <span class="col-md-1 col-sm-2 i18n" title="i18n(ldp_i18n_group_manage_1016)"><a href="javascript:STAT_OPERATOR.deleteStatConfirm('${statItem.id}','${statItem.title}','${statItem.groupId}');" style="color:#000000;"><i class="fa fa-fw fa-minus-circle"></i></a></span>
                                            </#if>
                                        </#if>
                                    </#if>
                                </td>
                            </tr>
                        </#list>
                    </#if>
                </table>
                <div class="modal-footer" style="margin-right:50px;">
                    <a href="javascript:MsgBox.Alert('${groupEntity.secretKey}');" id="confirmDelete2" class="btn bg-purple btn-xs">
                        <samp class="i18n" i18n_code="ldp_i18n_group_manage_1018"></samp>
                    </a>
                    <a href="javascript:DISPLAY_OPERATOR.showGroupLimitedRecord('${groupEntity.id}');" id="confirmDelete" class="btn bg-navy btn-xs">
                        <samp class="i18n" i18n_code="ldp_i18n_group_manage_1019"></samp>
                    </a>
                    <a href="javascript:DISPLAY_OPERATOR.showUpdateThresholdModal();" class="btn bg-red btn-xs" data-dismiss="modal">
                        <samp class="i18n" i18n_code="ldp_i18n_group_manage_1020"></samp>
                    </a>
                </div>
            </div>
        </div>
    </div>
    <@popFrame.publishToSiteMap />
    <@popFrame.updateGroupThreshold />
    <@popFrame.limitedRecordModal "1"/>
</div>
<@baseFrame.sub_tail />
<script src="/static/js/apply/apply_operator.js" charset="utf-8"></script>