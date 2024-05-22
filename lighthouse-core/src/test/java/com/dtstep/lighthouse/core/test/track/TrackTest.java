package com.dtstep.lighthouse.core.test.track;

import com.dtstep.lighthouse.core.config.LDPConfig;
import org.junit.Test;

public class TrackTest {

    static {
        try{
            LDPConfig.initWithHomePath("/Users/xueling/lighthouse");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Test
    public void testScanMessage() throws Exception {
        int groupId = 100288;
        int statId = 1100520;

    }
}
