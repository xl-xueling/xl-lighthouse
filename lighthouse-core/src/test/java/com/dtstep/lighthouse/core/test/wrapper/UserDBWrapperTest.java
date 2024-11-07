package com.dtstep.lighthouse.core.test.wrapper;

import com.dtstep.lighthouse.core.test.CoreBaseTest;
import com.dtstep.lighthouse.core.wrapper.UserDBWrapper;
import org.junit.Test;

public class UserDBWrapperTest extends CoreBaseTest {

    @Test
    public void testQuery() throws Exception {
        UserDBWrapper.queryUserList(null,null);
    }
}
