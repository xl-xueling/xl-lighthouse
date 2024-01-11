package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.common.util.BeanCopyUtil;
import com.dtstep.lighthouse.insights.modal.Component;
import com.dtstep.lighthouse.insights.modal.Group;
import com.dtstep.lighthouse.insights.modal.User;

public class ComponentDto extends Component {

    private User user;

    public ComponentDto(Component component){
        assert component != null;
        BeanCopyUtil.copy(component,this);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
