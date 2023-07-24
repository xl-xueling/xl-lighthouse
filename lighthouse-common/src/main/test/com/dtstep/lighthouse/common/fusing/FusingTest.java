package com.dtstep.lighthouse.common.fusing;

import com.dtstep.lighthouse.common.enums.fusing.FusingRules;
import org.junit.Test;

public class FusingTest {

    @Test
    public void testFusing() throws Exception {
        for(int i=0;i<100;i++){
            FusingToken fusingToken = FusingSwitch.entry(FusingRules.CLIENT_EXCEPTION_RULE);

            FusingSwitch.track(fusingToken);
        }
    }
}
