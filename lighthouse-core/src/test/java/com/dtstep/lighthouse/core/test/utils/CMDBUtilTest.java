package com.dtstep.lighthouse.core.test.utils;

import com.dtstep.lighthouse.core.test.CoreBaseTest;
import com.dtstep.lighthouse.core.tools.CMDBUtil;
import org.junit.Test;

public class CMDBUtilTest extends CoreBaseTest {

    @Test
    public void testAddColumn() throws Exception {
        System.out.println("add column start!");
        CMDBUtil.addColumnIfNotExist("ldp_relations","extend2","MEDIUMTEXT");
        System.out.println("add column end!");
    }
}
