package com.dtstep.lighthouse.core.test.template;

import com.dtstep.lighthouse.core.test.CoreBaseTest;
import org.junit.Test;

public class TestTemplate extends CoreBaseTest {

    @Test
    public void testTemplate() throws Exception {
        String template = "<stat-item  title=\"每5分钟_各分类_点击率top10\" stat=\"count(clickedrecharged == '确认支付')\" dimens=\"top_level\" limit=\"top30\" />";

    }
}
