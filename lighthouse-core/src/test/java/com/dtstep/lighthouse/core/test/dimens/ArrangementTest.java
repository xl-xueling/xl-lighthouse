package com.dtstep.lighthouse.core.test.dimens;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.google.common.collect.Lists;
import jodd.util.ArraysUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class ArrangementTest {

    @Test
    public void testArrangement() throws Exception {
        LinkedHashMap<String,String[]> dataMap = new LinkedHashMap<>();
        String[] list1 = new String[]{"a","b","c"};
        String[] list2 = new String[]{"1","2","3"};
        String[] list3 = new String[]{"e","f","g"};
        dataMap.put("k1",list1);
        dataMap.put("k2",list2);
        dataMap.put("k3",list3);


        String[] dimensArray = new String[]{"k3","k1","k2"};
        List<String> dimensSortList = Arrays.asList(dimensArray);
        List<Map.Entry<String, String[]>> list = new ArrayList<>(dataMap.entrySet());
        list.sort(new CustomComparator(dimensSortList));
        List<String> keyList = list.stream().map(Map.Entry::getKey).collect(Collectors.toList());
        System.out.println("sorted keyList is:" + JsonUtil.toJSONString(keyList));
        LinkedHashMap<String, String[]> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, String[]> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        String[][] array = sortedMap.values().toArray(new String[0][0]);
        List<String> result = arrangement(array);
        for(int i=0;i<result.size();i++){
            System.out.println("result is:" + result.get(i));
        }
    }
    private static class CustomComparator implements Comparator<Map.Entry<String, String[]>> {

        private final List<String> dimensSortList;

        public CustomComparator(List<String> dimensSortList){
            this.dimensSortList = dimensSortList;
        }

        @Override
        public int compare(Map.Entry<String, String[]> o1, Map.Entry<String, String[]> o2) {
            return dimensSortList.indexOf(o1.getKey()) - dimensSortList.indexOf(o2.getKey());
        }
    }

    public List<String> arrangement(String[]... datas) {
        List<String> result = new ArrayList<>();
        beArrangement(result, "", datas, 0);
        return result;
    }

    private void beArrangement(List<String> result, String current, String[][] lists, int index) {
        if (index == lists.length) {
            result.add(current);
            return;
        }
        for (String item : lists[index]) {
            beArrangement(result, current + (current.isEmpty() ? "" : ";") + item, lists, index + 1);
        }
    }
}
