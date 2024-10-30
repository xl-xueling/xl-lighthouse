package com.dtstep.lighthouse.insights.vo;

import com.dtstep.lighthouse.common.entity.AlarmTemplateExtEntity;
import com.dtstep.lighthouse.common.modal.AlarmTemplate;
import com.dtstep.lighthouse.common.modal.Department;
import com.dtstep.lighthouse.common.modal.User;
import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import java.util.List;

public class AlarmTemplateVO extends AlarmTemplateExtEntity {

    private List<User> userList;

    private List<Department> departmentList;

    public AlarmTemplateVO(){}

    public AlarmTemplateVO(AlarmTemplate alarmTemplate){
        assert alarmTemplate != null;
        BeanCopyUtil.copy(alarmTemplate,this);
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public List<Department> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<Department> departmentList) {
        this.departmentList = departmentList;
    }
}
