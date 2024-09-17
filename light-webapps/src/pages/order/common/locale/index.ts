const i18n = {
  'en-US': {
    'detailModal.columns.id':'ID',
    'detailModal.columns.user':'User',
    'detailModal.columns.type':'OrderType',
    'detailModal.columns.type.project.access':'Request Project Access',
    'detailModal.columns.type.stat.access':'Request Statistic Access',
    'detailModal.columns.type.metrics.access':'Request Metrics Access',
    'detailModal.columns.type.update.limiting.threshold':'Update Limiting Threshold',
    'detailModal.columns.type.stat.pend.approve':'Stat Pend Approve',
    'detailModal.columns.type.user.pend.approve':'User Pend Approve',
    'detailModal.columns.desc':'Description',
    'detailModal.columns.admins':'Approve Users',
    'detailModal.columns.state':'OrderState',
    'detailModal.columns.state.process':'Processing',
    'detailModal.columns.state.approved':'Approved',
    'detailModal.columns.state.rejected':'Rejected',
    'detailModal.columns.state.retracted':'Retracted',
    'detailModal.columns.createTime':'CreateTime',
    'detailModal.columns.updateTime':'UpdateTime',
    'detailModal.columns.operations':'Operations',
    'detailModal.columns.operations.process':'Process',
    'detailModal.warning.relateElementDeleted':'The elements related to this order have been deleted!',
    'searchForm.username.label':'UserName',
    'searchForm.orderType.label':'OrderType',
    'searchForm.state.label':'State',
    'searchForm.createTime.label':'CreateTime',
    'detailModal.user.approve.columns.id':'ID',
    'detailModal.user.approve.columns.username':'username',
    'detailModal.user.approve.columns.email':'Email',
    'detailModal.user.approve.columns.department':'Department',
    'detailModal.user.approve.columns.createTime':'CreateTime',
    'detailModal.user.approve.columns.state':'UserState',
    'detailModal.user.approve.columns.state.pending': 'Pending',
    'detailModal.user.approve.columns.state.normal': 'Normal',
    'detailModal.user.approve.columns.state.frozen': 'Frozen',
    'detailModal.user.approve.columns.state.deleted': 'Deleted',
    'detailModal.detail.columns.id':'ID',
    'detailModal.detail.columns.roleType':'RoleType',
    'detailModal.detail.columns.roleType.systemManager':'System Manager',
    'detailModal.detail.columns.roleType.departmentManager':'Department Manager',
    'detailModal.detail.columns.roleType.projectManager':'Project Manager',
    'detailModal.detail.columns.roleType.viewManager':'DataView Manager',
    'detailModal.detail.columns.roleType.operateManager':'Operation Manager',
    'detailModal.detail.columns.approver':'Approver',
    'detailModal.detail.columns.approve.state':'State',
    'detailModal.detail.columns.approve.state.wait':'Wait',
    'detailModal.detail.columns.approve.state.pending':'Pending',
    'detailModal.detail.columns.approve.state.approved':'Approved',
    'detailModal.detail.columns.approve.state.rejected':'Rejected',
    'detailModal.detail.columns.approve.state.retracted':'Retracted',
    'detailModal.detail.columns.approve.state.suspend':'Suspend',
    'detailModal.detail.columns.approve.approveTime':'ApproveTime',
    'detailModal.detail.columns.approve.reply':'Reply',
    'detailModal.label.order.info':'Order Info',
    'detailModal.label.user.info':'User Info',
    'detailModal.label.reason':'Reason',
    'detailModal.label.process.info':'Process',
    'detailModal.title':'Order Detail',
    'detailModal.form.button.close':'Close',
    'detailModal.projectAccess.columns.id':'ID',
    'detailModal.projectAccess.columns.title':'Title',
    'detailModal.projectAccess.columns.department':'Department',
    'detailModal.projectAccess.columns.admins':"Admins",
    'detailModal.projectAccess.columns.desc':'Description',
    'detailModal.statAccess.columns.id':'ID',
    'detailModal.statAccess.columns.relationship':'Relationship',
    'detailModal.statAccess.columns.title':'Title',
    'detailModal.statAccess.columns.department':'Department',
    'detailModal.statAccess.columns.admins':"Admins",
    'detailModal.statAccess.columns.desc':'Description',
    'detailModal.viewAccess.columns.id':'ID',
    'detailModal.viewAccess.columns.title':'Title',
    'detailModal.viewAccess.columns.admins':"Admins",
    'detailModal.viewAccess.columns.desc':'Description',
    'detailModal.limitingSettings.columns.id':'ID',
    'detailModal.limitingSettings.columns.token':'Token',
    'detailModal.limitingSettings.columns.project':'Project',
    'detailModal.limitingSettings.columns.strategy':'Limiting Strategy',
    'detailModal.limitingSettings.columns.currentValue':'Current Value[PerSecond]',
    'detailModal.limitingSettings.columns.updateValue':'Update Value[PerSecond]',
    'detailModal.label.related.information':'Related Information',
  },
  'zh-CN': {
    'detailModal.columns.id':'ID',
    'detailModal.columns.user':'申请人',
    'detailModal.columns.type':'工单类型',
    'detailModal.columns.type.project.access':'工程访问权限申请',
    'detailModal.columns.type.stat.access':'统计项访问权限申请',
    'detailModal.columns.type.metrics.access':'指标集访问权限申请',
    'detailModal.columns.type.update.limiting.threshold':'修改限流阈值',
    'detailModal.columns.type.stat.pend.approve':'审核统计项',
    'detailModal.columns.type.user.pend.approve':'审核新用户',
    'detailModal.columns.desc':'描述',
    'detailModal.columns.admins':'审核人',
    'detailModal.columns.state':'工单状态',
    'detailModal.columns.state.process':'处理中',
    'detailModal.columns.state.approved':'已通过',
    'detailModal.columns.state.rejected':'已驳回',
    'detailModal.columns.state.retracted':'已撤回',
    'detailModal.columns.createTime':'创建时间',
    'detailModal.columns.updateTime':'更新时间',
    'detailModal.columns.operations':'操作',
    'detailModal.columns.operations.process':'处理',
    'detailModal.warning.relateElementDeleted':'工单相关元素已被删除！',
    'searchForm.username.label':'申请人',
    'searchForm.orderType.label':'工单类型',
    'searchForm.state.label':'状态',
    'searchForm.createTime.label':'创建时间',
    'detailModal.user.approve.columns.id':'ID',
    'detailModal.user.approve.columns.username':'用户名',
    'detailModal.user.approve.columns.email':'邮箱',
    'detailModal.user.approve.columns.department':'部门',
    'detailModal.user.approve.columns.createTime':'创建时间',
    'detailModal.user.approve.columns.state':'用户状态',
    'detailModal.user.approve.columns.state.pending': '待审核',
    'detailModal.user.approve.columns.state.normal': '正常',
    'detailModal.user.approve.columns.state.frozen': '已冻结',
    'detailModal.user.approve.columns.state.deleted': '已删除',
    'detailModal.detail.columns.id':'ID',
    'detailModal.detail.columns.roleType':'系统角色',
    'detailModal.detail.columns.roleType.systemManager':'系统管理员',
    'detailModal.detail.columns.roleType.departmentManager':'部门管理员',
    'detailModal.detail.columns.roleType.projectManager':'工程管理员',
    'detailModal.detail.columns.roleType.viewManager':'数据视图管理员',
    'detailModal.detail.columns.roleType.operateManager':'运维管理员',
    'detailModal.detail.columns.approver':'审批人',
    'detailModal.detail.columns.approve.state':'审核状态',
    'detailModal.detail.columns.approve.state.wait':'待处理',
    'detailModal.detail.columns.approve.state.pending':'待审核',
    'detailModal.detail.columns.approve.state.approved':'已审核',
    'detailModal.detail.columns.approve.state.rejected':'已驳回',
    'detailModal.detail.columns.approve.state.retracted':'已撤回',
    'detailModal.detail.columns.approve.state.suspend':'已中断',
    'detailModal.detail.columns.approve.approveTime':'审核时间',
    'detailModal.detail.columns.approve.reply':'审核批复',
    'detailModal.label.order.info':'工单信息',
    'detailModal.label.user.info':'用户信息',
    'detailModal.label.reason':'申请原因',
    'detailModal.label.process.info':'流程信息',
    'detailModal.title':'工单详情',
    'detailModal.form.button.close':'关闭',
    'detailModal.projectAccess.columns.id':'ID',
    'detailModal.projectAccess.columns.title':'工程名称',
    'detailModal.projectAccess.columns.department':'所属部门',
    'detailModal.projectAccess.columns.admins':"工程管理员",
    'detailModal.projectAccess.columns.desc':'工程描述',
    'detailModal.statAccess.columns.id':'ID',
    'detailModal.statAccess.columns.relationship':'归属',
    'detailModal.statAccess.columns.title':'统计项名称',
    'detailModal.statAccess.columns.department':'部门',
    'detailModal.statAccess.columns.admins':"管理员",
    'detailModal.statAccess.columns.desc':'描述信息',
    'detailModal.viewAccess.columns.id':'ID',
    'detailModal.viewAccess.columns.title':'视图名称',
    'detailModal.viewAccess.columns.admins':"管理员",
    'detailModal.viewAccess.columns.desc':'描述信息',
    'detailModal.limitingSettings.columns.id':'ID',
    'detailModal.limitingSettings.columns.token':'Token',
    'detailModal.limitingSettings.columns.project':'所属工程',
    'detailModal.limitingSettings.columns.strategy':'限流策略',
    'detailModal.limitingSettings.columns.currentValue':'当前值[每秒]',
    'detailModal.limitingSettings.columns.updateValue':'更新值[每秒]',
    'detailModal.label.related.information':'关联信息',
  },
};

export default i18n;
