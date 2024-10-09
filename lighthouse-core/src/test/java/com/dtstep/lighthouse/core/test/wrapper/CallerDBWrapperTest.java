package com.dtstep.lighthouse.core.test.wrapper;

import com.dtstep.lighthouse.common.modal.Caller;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.core.test.CoreBaseTest;
import com.dtstep.lighthouse.core.wrapper.CallerDBWrapper;
import org.junit.Test;

public class CallerDBWrapperTest extends CoreBaseTest {

    @Test
    public void testQueryByName() throws Exception {
        String callerName = "caller:wuxian_test_demo2";
        for(int i=0;i<100;i++){
            Caller caller = CallerDBWrapper.queryByName(callerName);
            System.out.println(caller);
            System.out.println("caller:" + JsonUtil.toJSONString(caller));
        }
    }
}
