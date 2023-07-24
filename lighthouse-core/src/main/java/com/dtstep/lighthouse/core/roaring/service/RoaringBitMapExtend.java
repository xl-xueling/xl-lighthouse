package com.dtstep.lighthouse.core.roaring.service;
/*
 * Copyright (C) 2022-2023 XueLing.雪灵
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

import org.javatuples.Pair;
import java.io.Serializable;

public final class RoaringBitMapExtend implements Serializable {

    private static final long serialVersionUID = 3095465635356407182L;

    private final Pair<RoaringBitMapEmbed, RoaringBitMapEmbed> container = Pair.with(new RoaringBitMapEmbed(),new RoaringBitMapEmbed());

    public boolean checkAndPut(long bitIndex){
        int low = (int) (bitIndex);
        int high = (int) (bitIndex >>> 32);
        if(low < 0){low = ~low;}
        if(high < 0){high = ~high;}
        RoaringBitMapEmbed highContainer = container.getValue0();
        RoaringBitMapEmbed lowContainer = container.getValue1();
        if(highContainer.contains(high) && lowContainer.contains(low)){
            return true;
        }else{
            highContainer.add(high);
            lowContainer.add(low);
            return false;
        }
    }

    public void put(long bitIndex){
        int low = (int) (bitIndex);
        int high = (int) (bitIndex >>> 32);
        if(low < 0){low = ~low;}
        if(high < 0){high = ~high;}
        RoaringBitMapEmbed highContainer = container.getValue0();
        RoaringBitMapEmbed lowContainer = container.getValue1();
        highContainer.add(high);
        lowContainer.add(low);
    }

    public void clear(){
        container.getValue0().clear();
        container.getValue1().clear();
    }

    public Pair<RoaringBitMapEmbed, RoaringBitMapEmbed> getContainer() {
        return container;
    }
}
