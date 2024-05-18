package com.dtstep.lighthouse.common.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ListUtil {

    public static <T> List<List<T>> listPartition(List<T> list, int targetGroupSize) {
        return IntStream.range(0, (list.size() + targetGroupSize - 1) / targetGroupSize)
                .mapToObj(i -> list.subList(i * targetGroupSize, Math.min((i + 1) * targetGroupSize, list.size())))
                .collect(Collectors.toList());
    }
}
