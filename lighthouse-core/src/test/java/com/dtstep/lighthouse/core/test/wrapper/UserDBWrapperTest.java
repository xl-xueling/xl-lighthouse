package com.dtstep.lighthouse.core.test.wrapper;

import com.dtstep.lighthouse.common.modal.User;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.test.CoreBaseTest;
import com.dtstep.lighthouse.core.wrapper.UserDBWrapper;
import org.junit.Test;

import java.util.List;

public class UserDBWrapperTest extends CoreBaseTest {

    @Test
    public void testQuery() throws Exception {
        int id = 110240;
        User user = UserDBWrapper.queryById(id);
        System.out.println("user is:" + JsonUtil.toJSONString(user));
    }

    @Test
    public void testQueryByDepartment() throws Exception {
        int departmentId = 10252;
        List<Integer> userIdList = UserDBWrapper.queryUseIdListByDepartment(departmentId);
        System.out.println("userIdList is:" + JsonUtil.toJSONString(userIdList));
    }
}
