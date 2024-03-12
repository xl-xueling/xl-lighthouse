package com.dtstep.lighthouse.core.test.dimens;

import com.dtstep.lighthouse.common.util.JsonUtil;
import org.junit.Test;

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

    @Test
    public void testArrangement2() throws Exception {
        LinkedHashMap<String,String[]> dataMap  = new LinkedHashMap<>();
        String[] arr1 = new String[]{"a;1","b;2"};
        String[] arr2 = new String[]{"1","2"};
        String[] arr3 = new String[]{"A","B"};
        dataMap.put("recallno;behaviorType",arr1);
        dataMap.put("town",arr3);
        String [] dimensArray = new String[]{"behaviorType","town","recallno"};
        List<String> dimensSortList = Arrays.asList(dimensArray);
        List<String> dimensOriginList = new ArrayList<>();
        for(String dimens : dataMap.keySet()){
            if(!dimens.contains(";")){
                dimensOriginList.add(dimens);
            }else{
                String [] arr = dimens.split(";");
                dimensOriginList.addAll(Arrays.asList(arr));
            }
        }

        String[][] originArray = dataMap.values().toArray(new String[0][0]);
        List<String> list = arrangement(originArray);
        List<String> tList = list.stream().map(z -> {
            String[] arr = z.split(";");
            DimensEntity[] pairs = new DimensEntity[arr.length];
            for(int i=0;i<arr.length;i++){
                DimensEntity pair = new DimensEntity(dimensOriginList.get(i), arr[i]);
                pairs[i] = pair;
            }
            List<DimensEntity> pairs1 = Arrays.stream(pairs).sorted(new CustomComparator2(dimensSortList)).collect(Collectors.toList());
            return pairs1.stream()
                    .map(DimensEntity::getValue)
                    .collect(Collectors.joining(";"));
        }).collect(Collectors.toList());
        for(int i=0;i<tList.size();i++){
            System.out.println("result2:" + tList.get(i));
        }

    }

    private static class DimensEntity {

        private final String dimens;

        private final String value;

        public DimensEntity(String dimens, String value){
            this.dimens = dimens;
            this.value = value;
        }
        public String getDimens() {
            return dimens;
        }

        public String getValue() {
            return value;
        }
    }


    private static class CustomComparator2 implements Comparator<DimensEntity> {

        private final List<String> dimensSortList;

        public CustomComparator2(List<String> dimensSortList){
            this.dimensSortList = dimensSortList;
        }

        @Override
        public int compare(DimensEntity o1, DimensEntity o2) {
            return dimensSortList.indexOf(o1.getDimens()) - dimensSortList.indexOf(o2.getDimens());
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
