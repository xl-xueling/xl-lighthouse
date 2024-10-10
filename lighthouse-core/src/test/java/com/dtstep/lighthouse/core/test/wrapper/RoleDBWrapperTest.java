package com.dtstep.lighthouse.core.test.wrapper;

import com.dtstep.lighthouse.common.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.common.modal.Role;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.test.CoreBaseTest;
import com.dtstep.lighthouse.core.wrapper.RoleDBWrapper;
import org.junit.Test;

public class RoleDBWrapperTest extends CoreBaseTest {

    @Test
    public void testQueryByName() throws Exception {
        Role role = RoleDBWrapper.queryAccessRoleByResource(1100607, ResourceTypeEnum.Stat);
        System.out.println("role is:" + JsonUtil.toJSONString(role));
    }
}
