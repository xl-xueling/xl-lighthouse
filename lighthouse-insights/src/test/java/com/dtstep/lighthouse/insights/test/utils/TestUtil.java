package com.dtstep.lighthouse.insights.test.utils;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.modal.PermissionEnum;
import com.dtstep.lighthouse.common.modal.Order;
import com.dtstep.lighthouse.common.modal.Project;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class TestUtil {

    @Test
    public void testJson() throws Exception {
//        Column column = new Column();
//        column.setType(ColumnTypeEnum.STRING);
//        column.setComment("sss");
//        column.setName("AAAS");
//        System.out.println("ss:" + JsonUtil.toJSONString(column));
//
//        String str = JsonUtil.toJSONString(column);
//        System.out.println(str);
//        Column column1 = JsonUtil.toJavaObject("{\"name\":\"AAAS\",\"type\":\"1\",\"comment\":\"sss\"}",Column.class);
//        System.out.println("column1 is:" + JsonUtil.toJSONString(column1));

        String s = "{\"orderType\":1,\"state\":100,\"desc\":\"ss\"}";
        Order order = JsonUtil.toJavaObject(s, Order.class);
        System.out.println("order state:" + order.getState());
        System.out.println("order:" + JsonUtil.toJSONString(order));

    }

    @Test
    public void testArray() throws Exception{
        Set<PermissionEnum> sets = new HashSet<>();
        sets.add(PermissionEnum.AccessAble);
        sets.add(PermissionEnum.ManageAble);
        sets.add(PermissionEnum.ManageAble);
        sets.add(null);
        System.out.println("sets:" + JsonUtil.toJSONString(sets));
    }

    @Test
    public void testRandom() throws Exception{
        Object obj = null;
        Project project = (Project) obj;
        System.out.println("project:" + project);
    }

}
