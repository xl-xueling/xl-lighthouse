package com.dtstep.lighthouse.ice.test.servant.rpc.http;

import com.dtstep.lighthouse.core.http.LightHouseHttpService;
import org.junit.Test;

public class TestStartHttpService extends CoreBaseTest {

    @Test
    public void testDataQuery() throws Exception {
        new LightHouseHttpService().start();
    }
}
