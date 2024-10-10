package com.dtstep.lighthouse.core.test.wrapper;

import com.dtstep.lighthouse.common.enums.OwnerTypeEnum;
import com.dtstep.lighthouse.common.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.common.modal.Role;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.test.CoreBaseTest;
import com.dtstep.lighthouse.core.wrapper.PermissionDBWrapper;
import com.dtstep.lighthouse.core.wrapper.RoleDBWrapper;
import org.junit.Test;

public class PermissionDBWrapperTest extends CoreBaseTest {

    @Test
    public void testHasPermission() throws Exception {
        for(int i=0;i<100;i++){
            boolean is = PermissionDBWrapper.hasPermission(11033, OwnerTypeEnum.CALLER,1224);
            System.out.println("is:" + is);
        }
    }

}
