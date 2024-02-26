package com.dtstep.lighthouse.insights.test.utils;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.common.entity.ResultCode;
import com.dtstep.lighthouse.common.modal.TreeNode;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestTreeUtil {

    @Test
    public void test() throws Exception {
        String s = "[\n" +
                "  {\n" +
                "    \"label\": \"Node 1\",\n" +
                "    \"value\": \"1\",\n" +
                "    \"children\": [\n" +
                "      {\n" +
                "        \"label\": \"Node 1-1\",\n" +
                "        \"value\": \"1-1\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"label\": \"Node 1-2\",\n" +
                "        \"value\": \"1-2\",\n" +
                "        \"children\": [\n" +
                "          {\n" +
                "            \"label\": \"Node 1-2-1\",\n" +
                "            \"value\": \"1-2-1\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"label\": \"Node 1-2-2\",\n" +
                "            \"value\": \"1-2-2\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"label\": \"Node 2\",\n" +
                "    \"value\": \"2\",\n" +
                "    \"children\": [\n" +
                "      {\n" +
                "        \"label\": \"Node 2-1\",\n" +
                "        \"value\": \"2-1\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"label\": \"Node 2-2\",\n" +
                "        \"value\": \"2-2\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]";

        List<TreeNode> nodeList = JsonUtil.toJavaObjectList(s, TreeNode.class);
        ResultCode resultCode = validate(nodeList,1,new ArrayList<String>());
        System.out.println("resultCode:" + JsonUtil.toJSONString(resultCode) + ",param:" + JsonUtil.toJSONString(resultCode.getParams()));
    }

    public static ResultCode validate(List<TreeNode> nodeList, int level,List<String> valueList){
        ResultCode resultCode = ResultCode.success;
        if(level > 3){
            return ResultCode.componentVerifyLevelLimit;
        }
        for(int i=0;i<nodeList.size();i++){
            TreeNode treeNode = nodeList.get(i);
            System.out.println("treeNode is:" + JsonUtil.toJSONString(treeNode));
            String label = treeNode.getLabel();
            Object value = treeNode.getValue();
            if(StringUtil.isEmpty(label) ){
                return ResultCode.getExtendResultCode(ResultCode.componentVerifyNotEmpty,new String[]{"label"});
            }
            if(value == null || StringUtil.isEmpty(value.toString())){
                return ResultCode.getExtendResultCode(ResultCode.componentVerifyNotEmpty,new String[]{"value"});
            }
            if(valueList.contains(value)){
                return ResultCode.componentVerifyDuplicateValue;
            }else{
                valueList.add(value.toString());
            }
            List<TreeNode> children = treeNode.getChildren();
            if(children != null && children.size() == 0){
                return ResultCode.getExtendResultCode(ResultCode.componentVerifyEmptyChildren,new String[]{label});
            }
            if(CollectionUtils.isNotEmpty(children)){
                resultCode = validate(children,level + 1,valueList);
                if(resultCode != ResultCode.success){
                    return resultCode;
                }
            }
        }
        return resultCode;
    }
}
