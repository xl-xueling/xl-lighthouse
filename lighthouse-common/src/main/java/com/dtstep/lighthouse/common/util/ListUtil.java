package com.dtstep.lighthouse.common.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ListUtil {

    public static <T> List<List<T>> listPartition(List<T> list, int targetGroupSize) {
        Validate.isTrue(CollectionUtils.isNotEmpty(list) && targetGroupSize != 0 && list.size() >= targetGroupSize);
        int totalSize = list.size();
        int groupSize = (totalSize + targetGroupSize - 1) / targetGroupSize;
        return IntStream.range(0, targetGroupSize)
                .mapToObj(i -> list.subList(i * groupSize, Math.min((i + 1) * groupSize, totalSize)))
                .collect(Collectors.toList());
    }
}
