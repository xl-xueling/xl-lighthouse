const vi18n = {
    'en-US': {
        'system.error':'System Error!',
        'basic.form.button.search':'Search',
        'basic.form.button.reset':'Reset',
        'basic.form.button.submit':'Submit',
        'basic.form.button.cancel':'Cancel',
        'basic.form.button.yes':'Yes',
        'basic.form.button.no':'No',
        'basic.form.button.close':'Close',
        'basic.form.button.preview':'Preview',
        'basic.form.button.manage':'Manage',
        'basic.route.back':'GoBack',
        'basic.form.label.date':'Date',
        'basic.form.label.input':'Input',
        'basic.form.label.search':'Search',
        'basic.form.label.refresh':'Refresh',
        'basic.form.verification.empty.warning':'Parameter cannot be empty!',
        'basic.form.verification.failed.warning':'Parameter verification failed!',
        'basic.form.verification.length.less.warning':'Parameter length is less than required!',
        'basic.form.verification.length.larger.warning':'Parameter length is larger than required!',
        'basic.modal.confirm.delete.title':'Delete Confirmation',
        'basic.columns.state.pending':'Pending',
        'basic.columns.state.running':'Running',
        'basic.columns.state.limiting':'Limiting',
        'basic.columns.state.frozen':'Frozen',
        'basic.columns.state.deleted':'Deleted',
        'basic.columns.state.rejected':'Rejected',
        'basic.columns.state.stopped':'Stopped',
        'basic.columns.state.invalid':'Invalid',
        'basic.warning.relateElementDeleted':'Related element have been deleted!',
        'orderApprove.roleType.description.systemManager':'System Manager',
        'orderApprove.roleType.description.operationManager':'Operation Manager',
        'orderApprove.roleType.description.departmentManager':'Department Manager',
        'orderApprove.roleType.description.projectManager':'Project Manager',
        'orderApprove.roleType.description.metricManager':'MetricSet Manager',
        'orderApprove.roleType.description.viewManager':'DataView Manager',
        'basic.orderState.description.processing':'Processing',
        'basic.orderState.description.approved':'Approved',
        'basic.orderState.description.rejected':'Rejected',
        'basic.orderState.description.retracted':'Retracted',
        'basic.orderState.description.deleted':'Deleted',
        'basic.orderDetail.approveState.description.wait':'wait',
        'basic.orderDetail.approveState.description.pending':'pending',
        'basic.orderDetail.approveState.description.approved':'approved',
        'basic.orderDetail.approveState.description.rejected':'rejected',
        'basic.orderDetail.approveState.description.retracted':'retracted',
        'basic.orderDetail.approveState.description.suspend':'suspend',
        'basic.userState.description.pending':'Pending',
        'basic.userState.description.normal':'Normal',
        'basic.userState.description.frozen':'Frozen',
        'basic.userState.description.rejected':'Rejected',
        'basic.userState.description.deleted':'Deleted',
        'basic.orderType.description.projectAccess':'Request Project Access',
        'basic.orderType.description.statAccess':'Request Statistic Access',
        'basic.orderType.description.viewAccess':'Request View Access',
        'basic.orderType.description.metricAccess':'Request MetricSet Access',
        'basic.orderType.description.updateLimitingThreshold':'Update Limiting Threshold',
        'basic.orderType.description.statPendApprove':'Stat Pend Approve',
        'basic.orderType.description.userPendApprove':'User Pend Approve',
        'basic.orderType.description.callerProjectAccess':'Caller Request Project Access',
        'basic.orderType.description.callerStatAccess':'Caller Request Stat Access',
        'basic.orderType.description.callerViewAccess':'Caller Request View Access',
        'basic.orderType.description.callerProjectAccessExtension':'Caller Extend Project Access',
        'basic.orderType.description.callerStatAccessExtension':'Caller Extend Stat Access',
        'basic.orderType.description.callerViewAccessExtension':'Caller Extend View Access',
        'basic.order.description.projectAccess':'Request Project[%s] Access',
        'basic.order.description.statAccess':'Request Stat[%s] Access',
        'basic.order.description.metricAccess':'Request Metrics[%s] Access',
        'basic.order.description.viewAccess':'Request DataView[%s] Access',
        'basic.order.description.updateLimitingThreshold':'Update Group[%s] Limiting Threshold.',
        'basic.order.description.statPendApprove':'Stat[%s] Pend Approve',
        'basic.order.description.userPendApprove':'User[%s] Pend Approve',
        'basic.order.description.callerProjectAccess':'Caller[%s] request project[%s] access',
        'basic.order.description.callerStatAccess':'Caller[%s] request stat[%s] access',
        'basic.order.description.callerViewAccess':'Caller[%s] request view[%s] access',
        'basic.order.description.callerProjectAccessExtension':'Caller[%s] extend project[%s] access',
        'basic.order.description.callerStatAccessExtension':'Caller[%s] extend stat[%s] access',
        'basic.order.description.callerViewAccessExtension':'Caller[%s] extend view[%s] access',
        'basic.orderExpired.description.expired1':'1 Month',
        'basic.orderExpired.description.expired2':'3 Month',
        'basic.orderExpired.description.expired3':'6 Month',
        'basic.orderExpired.description.expired4':'12 Month',
        'basic.roleType.description.projectManage':'Project Manage Permission',
        'basic.roleType.description.projectAccess':'Project Access Permission',
        'basic.roleType.description.metricManage':'Metric Manage Permission',
        'basic.roleType.description.metricAccess':'Metric Access Permission',
        'basic.roleType.description.statManage':'Statistic Manage Permission',
        'basic.roleType.description.statAccess':'Statistic Access Permission',
        'basic.roleType.description.viewManage':'View Manage Permission',
        'basic.roleType.description.viewAccess':'View Access Permission',
        'basic.roleType.description.callerManage':'Caller Manage Permission',
        'basic.roleType.description.callerAccess':'Caller Access Permission',
        'basic.roleType.description.domainManage':'Domain Manage Permission',
        'basic.roleType.description.domainAccess':'Domain Access Permission',
        'basic.roleType.description.groupManage':'Group Manage Permission',
        'basic.roleType.description.groupAccess':'Group Access Permission',
        'basic.roleType.description.departmentManage':'Department Manage Permission',
        'basic.roleType.description.departmentAccess':'Department Access Permission',
        'basic.roleType.description.operationManage':'Operation Manage Permission',
        'basic.roleType.description.operationAccess':'Operation Access Permission',
        'basic.componentsType.dateSelect':'Date Select',
        'basic.componentsType.dateRangeSelect':'Date Range Select',
        'basic.componentsType.datetimeRangeSelect':'Time Range Select',
        'basic.componentsType.weekSelect':'Date(Week) Select',
        'basic.componentsType.monthSelect':'Date(Month) Select',
        'basic.componentsType.filterInput':'Filter Input',
        'basic.componentsType.filterSelect':'Select Box',
        'basic.componentsType.system.filterSelect':'Default Selection Box',
        'basic.componentsType.system.filterInput':'Default Input Box',
        'basic.resourceType.project':'Project',
        'basic.resourceType.group':'Group',
        'basic.resourceType.statistic':'Statistic',
        'basic.resourceType.metricSet':'MetricSet',
        'basic.resourceType.view':'Data View',
        'basic.resourceType.department':'Department',
        'basic.resourceType.domain':'Domain',
        'basic.limitingStrategy.messageSizeLimiting':'Group Message Size Limiting',
        'basic.limitingStrategy.resultSizeLimiting':'Statistic Result Size Limiting',
        'basic.viewState.published':'Published',
        'basic.viewState.unPublished':'Unpublished',
        'basic.widget.widgetType.chart':'Chart',
        'basic.widget.widgetType.filter':'Filter',
        'basic.widget.widgetType.date':'DATE',
        'basic.componentType.datePicker.dateSelect':'Date Select',
        'basic.componentType.datePicker.dateRangeSelect':'Date Range Select',
        'basic.componentType.datePicker.dateTimeRangeSelect':'Time Range Select',
        'basic.componentType.filter.input':'Input',
        'basic.componentType.filter.select':'Select',
        'basic.componentType.filter.remoteSearchSelect':'Remote Select',
        'basic.componentType.datePicker.weekSelect':'Week Select',
        'basic.componentType.datePicker.monthSelect':'Month Select',
        'basic.chartType.lineChart':'LineChart',
        'basic.chartType.pieChart':'PieChart',
        'basic.chartType.lineAreaChart':'LineAreaChart',
        'basic.chartType.barChart':'BarChart',
        'basic.chartType.sortedBarChart':'BarChart(Sorted)',
        'basic.chartType.funnelChart':'FunnelChart',
        'basic.chartType.calendarChart':'CalendarChart',
        'basic.chartType.table':'Table',
        'basic.chartType.material.m1':'Numerical Card',
        'basic.chart.empty.warning': 'No Data!',
        'basic.shortcuts.today':'Today',
        'basic.shortcuts.yesterday':'Yesterday',
        'basic.shortcuts.7dago':'7d ago',
        'basic.shortcuts.14dago':'14d ago',
        'basic.shortcuts.30dago':'30d ago',
        'basic.shortcuts.last3d':'last3d',
        'basic.shortcuts.last7d':'last7d',
        'basic.shortcuts.last14d':'last14d',
        'basic.shortcuts.last30d':'last30d',
        'basic.shortcuts.last0w':'ThisWeek',
        'basic.shortcuts.last1w':'1 Week ago',
        'basic.shortcuts.last2w':'2 Week ago',
        'basic.shortcuts.last3w':'3 Week ago',
        'basic.shortcuts.last4w':'4 Week ago',
        'basic.shortcuts.last0m':'This Month',
        'basic.shortcuts.last1m':'1 Month ago',
        'basic.shortcuts.last2m':'2 Month ago',
        'basic.shortcuts.last3m':'3 Month ago',
        'basic.view.label.lastWeek':'Week',
        'basic.view.label.lastMonth':'Month',
    },
    'zh-CN': {
        'system.error':'系统异常!',
        'basic.form.button.search':'搜索',
        'basic.form.button.reset':'重置',
        'basic.form.button.submit':'确定',
        'basic.form.button.cancel':'取消',
        'basic.form.button.yes':'确定',
        'basic.form.button.no':'取消',
        'basic.form.button.close':'关闭',
        'basic.form.button.preview':'查看',
        'basic.form.button.manage':'管理',
        'basic.route.back':'返回上一页',
        'basic.form.label.date':'日期',
        'basic.form.label.input':'输入',
        'basic.form.label.search':'查询',
        'basic.form.label.refresh':'刷新',
        'basic.form.verification.empty.warning':'参数不能为空！',
        'basic.form.verification.failed.warning':'参数校验失败！',
        'basic.form.verification.length.less.warning':'参数长度低于要求！',
        'basic.form.verification.length.larger.warning':'参数长度超出限制！',
        'basic.modal.confirm.delete.title':'删除确认',
        'basic.columns.state.pending':'待审核',
        'basic.columns.state.running':'运行中',
        'basic.columns.state.limiting':'已限流',
        'basic.columns.state.frozen':'已冻结',
        'basic.columns.state.deleted':'已删除',
        'basic.columns.state.rejected':'已拒绝',
        'basic.columns.state.stopped':'已停止',
        'basic.columns.state.invalid':'无效配置',
        'basic.warning.relateElementDeleted':'相关元素已被删除！',
        'components.type.dateSelect':'日期选择',
        'components.type.dateRangeSelect':'日期范围选择',
        'components.type.datetimeRangeSelect':'日期时间范围选择',
        'components.type.filterInput':'输入框',
        'components.type.filterSelect':'下拉选择组件',
        'components.type.system.filterSelect':'默认下拉选择框',
        'components.type.system.filterInput':'默认输入框',
        'orderApprove.roleType.description.systemManager':'系统管理员',
        'orderApprove.roleType.description.operationManager':'运维管理员',
        'orderApprove.roleType.description.departmentManager':'部门管理员',
        'orderApprove.roleType.description.projectManager':'工程管理员',
        'orderApprove.roleType.description.metricManager':'指标集管理员',
        'orderApprove.roleType.description.viewManager':'数据视图管理员',
        'basic.orderState.description.processing':'处理中',
        'basic.orderState.description.approved':'已通过',
        'basic.orderState.description.rejected':'已驳回',
        'basic.orderState.description.retracted':'已撤回',
        'basic.orderState.description.deleted':'已删除',
        'basic.orderDetail.approveState.description.wait':'流程等待',
        'basic.orderDetail.approveState.description.pending':'待审核',
        'basic.orderDetail.approveState.description.approved':'已通过',
        'basic.orderDetail.approveState.description.rejected':'已驳回',
        'basic.orderDetail.approveState.description.retracted':'已撤回',
        'basic.orderDetail.approveState.description.suspend':'已中断',
        'basic.userState.description.pending':'待审核',
        'basic.userState.description.normal':'正常',
        'basic.userState.description.frozen':'冻结',
        'basic.userState.description.rejected':'审核未通过',
        'basic.userState.description.deleted':'已删除',
        'basic.orderType.description.projectAccess':'申请工程访问权限',
        'basic.orderType.description.statAccess':'申请统计项访问权限',
        'basic.orderType.description.viewAccess':'申请数据视图访问权限',
        'basic.orderType.description.metricAccess':'申请指标集访问权限',
        'basic.orderType.description.updateLimitingThreshold':'修改限流阈值',
        'basic.orderType.description.statPendApprove':'审核统计项',
        'basic.orderType.description.userPendApprove':'审核新用户',
        'basic.orderType.description.callerProjectAccess':'调用方申请工程访问权限',
        'basic.orderType.description.callerStatAccess':'调用方申请统计项访问权限',
        'basic.orderType.description.callerViewAccess':'调用方申请视图访问权限',
        'basic.orderType.description.callerProjectAccessExtension':'调用方续签工程访问权限',
        'basic.orderType.description.callerStatAccessExtension':'调用方续签统计项访问权限',
        'basic.orderType.description.callerViewAccessExtension':'调用方续签视图访问权限',
        'basic.order.description.projectAccess':'申请工程[%s]访问权限',
        'basic.order.description.statAccess':'申请统计项[%s]访问权限',
        'basic.order.description.metricAccess':'申请指标集[%s]访问权限',
        'basic.order.description.viewAccess':'申请数据视图[%s]访问权限',
        'basic.order.description.updateLimitingThreshold':'调整统计组[%s]限流阈值',
        'basic.order.description.statPendApprove':'新增统计项[%s]待审核',
        'basic.order.description.userPendApprove':'新增用户[%s]待审核',
        'basic.order.description.callerProjectAccess':'调用方[%s]申请工程[%s]访问权限',
        'basic.order.description.callerStatAccess':'调用方[%s]申请统计项[%s]访问权限',
        'basic.order.description.callerViewAccess':'调用方[%s]申请视图[%s]访问权限',
        'basic.order.description.callerProjectAccessExtension':'调用方[%s]续签工程[%s]访问权限',
        'basic.order.description.callerStatAccessExtension':'调用方[%s]续签统计项[%s]访问权限',
        'basic.order.description.callerViewAccessExtension':'调用方[%s]续签视图[%s]访问权限',
        'basic.orderExpired.description.expired1':'一个月',
        'basic.orderExpired.description.expired2':'三个月',
        'basic.orderExpired.description.expired3':'六个月',
        'basic.orderExpired.description.expired4':'一年',
        'basic.roleType.description.projectManage':'工程管理权限',
        'basic.roleType.description.projectAccess':'工程访问权限',
        'basic.roleType.description.metricManage':'指标集管理权限',
        'basic.roleType.description.metricAccess':'指标集访问权限',
        'basic.roleType.description.statManage':'统计项管理权限',
        'basic.roleType.description.statAccess':'统计项访问权限',
        'basic.roleType.description.viewManage':'数据视图管理权限',
        'basic.roleType.description.viewAccess':'数据视图访问权限',
        'basic.roleType.description.callerManage':'调用方管理权限',
        'basic.roleType.description.callerAccess':'调用方访问权限',
        'basic.roleType.description.domainManage':'域管理权限',
        'basic.roleType.description.domainAccess':'域访问权限',
        'basic.roleType.description.groupManage':'统计组管理权限',
        'basic.roleType.description.groupAccess':'统计组访问权限',
        'basic.roleType.description.departmentManage':'部门管理权限',
        'basic.roleType.description.departmentAccess':'部门访问权限',
        'basic.roleType.description.operationManage':'运维管理权限',
        'basic.roleType.description.operationAccess':'运维访问权限',
        'basic.componentsType.dateSelect':'日期选择框',
        'basic.componentsType.dateRangeSelect':'日期范围选择框',
        'basic.componentsType.datetimeRangeSelect':'日期时间范围选择框',
        'basic.componentsType.weekSelect':'日期（周）选择框',
        'basic.componentsType.monthSelect':'日期（月）选择框',
        'basic.componentsType.filterInput':'输入框',
        'basic.componentsType.filterSelect':'下拉选择框',
        'basic.componentsType.system.filterSelect':'默认下拉选择框',
        'basic.componentsType.system.filterInput':'默认输入框',
        'basic.resourceType.project':'统计工程',
        'basic.resourceType.group':'统计组',
        'basic.resourceType.statistic':'统计项',
        'basic.resourceType.metricSet':'指标集',
        'basic.resourceType.view':'数据视图',
        'basic.resourceType.department':'部门',
        'basic.resourceType.domain':'域',
        'basic.limitingStrategy.messageSizeLimiting':'统计组消息量限流',
        'basic.limitingStrategy.resultSizeLimiting':'统计项结果量限流',
        'basic.viewState.published':'已发布',
        'basic.viewState.unPublished':'未发布',
        'basic.widget.widgetType.chart':'图表',
        'basic.widget.widgetType.filter':'筛选组件',
        'basic.widget.widgetType.date':'日期组件',
        'basic.componentType.datePicker.dateSelect':'日期筛选框',
        'basic.componentType.datePicker.dateRangeSelect':'日期范围筛选框',
        'basic.componentType.datePicker.dateTimeRangeSelect':'日期时间筛选框',
        'basic.componentType.filter.input':'输入框',
        'basic.componentType.filter.select':'选择框',
        'basic.componentType.filter.remoteSearchSelect':'远程搜索选择框',
        'basic.componentType.datePicker.weekSelect':'周选择框',
        'basic.componentType.datePicker.monthSelect':'月选择框',
        'basic.chartType.lineChart':'折线图',
        'basic.chartType.pieChart':'饼状图',
        'basic.chartType.lineAreaChart':'面积图',
        'basic.chartType.barChart':'柱状图',
        'basic.chartType.sortedBarChart':'柱状图（排序）',
        'basic.chartType.funnelChart':'漏斗图',
        'basic.chartType.calendarChart':'日历图',
        'basic.chartType.table':'表格',
        'basic.chartType.material.m1':'数值卡片',
        'basic.chart.empty.warning': '暂无数据!',
        'basic.shortcuts.today':'今天',
        'basic.shortcuts.yesterday':'昨天',
        'basic.shortcuts.7dago':'7天前',
        'basic.shortcuts.14dago':'14天前',
        'basic.shortcuts.30dago':'30天前',
        'basic.shortcuts.last3d':'最近3天',
        'basic.shortcuts.last7d':'最近7天',
        'basic.shortcuts.last14d':'最近14天',
        'basic.shortcuts.last30d':'最近30天',
        'basic.shortcuts.last0w':'本周',
        'basic.shortcuts.last1w':'前一周',
        'basic.shortcuts.last2w':'前两周',
        'basic.shortcuts.last3w':'前三周',
        'basic.shortcuts.last4w':'前四周',
        'basic.shortcuts.last0m':'本月',
        'basic.shortcuts.last1m':'前一月',
        'basic.shortcuts.last2m':'前两月',
        'basic.shortcuts.last3m':'前三月',
        'basic.view.label.lastWeek':'周同比',
        'basic.view.label.lastMonth':'月同比',
    }
}
export default vi18n;