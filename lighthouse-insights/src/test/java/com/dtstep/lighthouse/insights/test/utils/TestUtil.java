package com.dtstep.lighthouse.insights.test.utils;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.modal.PermissionEnum;
import com.dtstep.lighthouse.common.modal.Order;
import com.dtstep.lighthouse.common.modal.Project;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

    @Test
    public void test2() throws Exception {
        ArrayNode arrayNode = JsonUtil.createArrayNode();
        ObjectNode objectNode1 = JsonUtil.createObjectNode();
        objectNode1.put("label","北京");
        objectNode1.put("value","1");
        arrayNode.add(objectNode1);
        ArrayNode arrayNode1 = JsonUtil.createArrayNode();
        ObjectNode objectNode11 = JsonUtil.createObjectNode();
        objectNode11.put("label","朝阳");
        objectNode11.put("value","11");
        arrayNode1.add(objectNode11);
        ObjectNode objectNode12 = JsonUtil.createObjectNode();
        objectNode12.put("label","海淀");
        objectNode12.put("value","12");
        arrayNode1.add(objectNode12);
        objectNode1.set("children",arrayNode1);
        ObjectNode objectNode2 = JsonUtil.createObjectNode();
        objectNode2.put("label","上海");
        objectNode2.put("value","2");
        arrayNode.add(objectNode2);
        ArrayNode arrayNode2 = JsonUtil.createArrayNode();
        ObjectNode objectNode21 = JsonUtil.createObjectNode();
        objectNode21.put("label","浦东");
        objectNode21.put("value","21");
        arrayNode2.add(objectNode21);
        ObjectNode objectNode22 = JsonUtil.createObjectNode();
        objectNode22.put("label","静安");
        objectNode22.put("value","22");
        arrayNode2.add(objectNode22);
        objectNode2.set("children",arrayNode2);
        System.out.println(arrayNode.toString());
    }

}
