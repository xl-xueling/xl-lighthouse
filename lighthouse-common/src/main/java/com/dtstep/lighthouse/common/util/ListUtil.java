package com.dtstep.lighthouse.common.util;
/*
 * Copyright (C) 2022-2024 XueLing.雪灵
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
