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
import java.io.DataInput;
import java.io.DataOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Iterator;
import java.util.Objects;
import org.roaringbitmap.ArrayContainer;
import org.roaringbitmap.CharIterator;
import org.roaringbitmap.Container;
import org.roaringbitmap.PeekableCharIterator;
import org.roaringbitmap.RunContainer;
import org.roaringbitmap.Util;
import org.roaringbitmap.art.ContainerIterator;
import org.roaringbitmap.art.KeyIterator;
import org.roaringbitmap.art.LeafNode;
import org.roaringbitmap.art.LeafNodeIterator;
import org.roaringbitmap.longlong.*;

public class RoaringBitMapEmbed implements Externalizable, LongBitmapDataProvider {

    private static final long serialVersionUID = -8201254780288457436L;

    private HighLowContainer highLowContainer = new HighLowContainer();

    public RoaringBitMapEmbed() {
    }

    public void addInt(int x) {
        this.addLong((long)x);
    }

    public void addLong(long x) {
        byte[] high = LongUtils.highPart(x);
        char low = LongUtils.lowPart(x);
        ContainerWithIndex containerWithIndex = this.highLowContainer.searchContainer(high);
        if (containerWithIndex != null) {
            Container container = containerWithIndex.getContainer();
            Container freshOne = container.add(low);
            this.highLowContainer.replaceContainer(containerWithIndex.getContainerIdx(), freshOne);
        } else {
            ArrayContainer arrayContainer = new ArrayContainer();
            arrayContainer.add(low);
            this.highLowContainer.put(high, arrayContainer);
        }

    }

    public long getLongCardinality() {
        if (this.highLowContainer.isEmpty()) {
            return 0L;
        } else {
            Iterator<Container> containerIterator = this.highLowContainer.containerIterator();

            long cardinality;
            Container container;
            for(cardinality = 0L; containerIterator.hasNext(); cardinality += (long)container.getCardinality()) {
                container = (Container)containerIterator.next();
            }

            return cardinality;
        }
    }

    public int getIntCardinality() throws UnsupportedOperationException {
        long cardinality = this.getLongCardinality();
        if (cardinality > 2147483647L) {
            throw new UnsupportedOperationException("Can not call .getIntCardinality as the cardinality is bigger than Integer.MAX_VALUE");
        } else {
            return (int)cardinality;
        }
    }

    public long select(long j) throws IllegalArgumentException {
        long left = j;

        int card;
        for(LeafNodeIterator leafNodeIterator = this.highLowContainer.highKeyLeafNodeIterator(false); leafNodeIterator.hasNext(); left -= (long)card) {
            LeafNode leafNode = leafNodeIterator.next();
            long containerIdx = leafNode.getContainerIdx();
            Container container = this.highLowContainer.getContainer(containerIdx);
            card = container.getCardinality();
            if (left < (long)card) {
                byte[] high = leafNode.getKeyBytes();
                int leftAsUnsignedInt = (int)left;
                char low = container.select(leftAsUnsignedInt);
                return LongUtils.toLong(high, low);
            }
        }

        return this.throwSelectInvalidIndex(j);
    }

    private long throwSelectInvalidIndex(long j) {
        throw new IllegalArgumentException("select " + j + " when the cardinality is " + this.getLongCardinality());
    }

    public Iterator<Long> iterator() {
        final LongIterator it = this.getLongIterator();
        return new Iterator<Long>() {
            public boolean hasNext() {
                return it.hasNext();
            }

            public Long next() {
                return it.next();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public void forEach(LongConsumer lc) {
        KeyIterator keyIterator = this.highLowContainer.highKeyIterator();

        while(keyIterator.hasNext()) {
            byte[] high = keyIterator.next();
            long containerIdx = keyIterator.currentContainerIdx();
            Container container = this.highLowContainer.getContainer(containerIdx);
            PeekableCharIterator charIterator = container.getCharIterator();

            while(charIterator.hasNext()) {
                char low = charIterator.next();
                long v = LongUtils.toLong(high, low);
                lc.accept(v);
            }
        }

    }

    public long rankLong(long id) {
        long result = 0L;
        byte[] high = LongUtils.highPart(id);
        char low = LongUtils.lowPart(id);
        ContainerWithIndex containerWithIndex = this.highLowContainer.searchContainer(high);
        KeyIterator keyIterator = this.highLowContainer.highKeyIterator();
        byte[] key;
        if (containerWithIndex == null) {
            while(keyIterator.hasNext()) {
                key = keyIterator.next();
                int res = LongUtils.compareHigh(key, high);
                if (res > 0) {
                    break;
                }

                long containerIdx = keyIterator.currentContainerIdx();
                Container container = this.highLowContainer.getContainer(containerIdx);
                result += (long)container.getCardinality();
            }
        } else {
            while(keyIterator.hasNext()) {
                key = keyIterator.next();
                long containerIdx = keyIterator.currentContainerIdx();
                Container container = this.highLowContainer.getContainer(containerIdx);
                if (LongUtils.compareHigh(key, high) == 0) {
                    result += (long)container.rank(low);
                    break;
                }

                result += (long)container.getCardinality();
            }
        }

        return result;
    }

    public void or(RoaringBitMapEmbed x2) {
        KeyIterator highIte2 = x2.highLowContainer.highKeyIterator();

        while(highIte2.hasNext()) {
            byte[] high = highIte2.next();
            long containerIdx = highIte2.currentContainerIdx();
            Container container2 = x2.highLowContainer.getContainer(containerIdx);
            ContainerWithIndex containerWithIdx = this.highLowContainer.searchContainer(high);
            Container container2clone;
            if (containerWithIdx == null) {
                container2clone = container2.clone();
                this.highLowContainer.put(high, container2clone);
            } else {
                container2clone = containerWithIdx.getContainer().ior(container2);
                this.highLowContainer.replaceContainer(containerWithIdx.getContainerIdx(), container2clone);
            }
        }

    }

    public void xor(RoaringBitMapEmbed x2) {
        KeyIterator keyIterator = x2.highLowContainer.highKeyIterator();

        while(keyIterator.hasNext()) {
            byte[] high = keyIterator.next();
            long containerIdx = keyIterator.currentContainerIdx();
            Container container = x2.highLowContainer.getContainer(containerIdx);
            ContainerWithIndex containerWithIndex = this.highLowContainer.searchContainer(high);
            Container containerClone2;
            if (containerWithIndex == null) {
                containerClone2 = container.clone();
                this.highLowContainer.put(high, containerClone2);
            } else {
                containerClone2 = containerWithIndex.getContainer().ixor(container);
                this.highLowContainer.replaceContainer(containerWithIndex.getContainerIdx(), containerClone2);
            }
        }

    }

    public void and(RoaringBitMapEmbed x2) {
        KeyIterator thisIterator = this.highLowContainer.highKeyIterator();

        while(thisIterator.hasNext()) {
            byte[] highKey = thisIterator.next();
            long containerIdx = thisIterator.currentContainerIdx();
            ContainerWithIndex containerWithIdx = x2.highLowContainer.searchContainer(highKey);
            if (containerWithIdx == null) {
                thisIterator.remove();
            } else {
                Container container1 = this.highLowContainer.getContainer(containerIdx);
                Container freshContainer = container1.iand(containerWithIdx.getContainer());
                this.highLowContainer.replaceContainer(containerIdx, freshContainer);
            }
        }

    }

    public void andNot(RoaringBitMapEmbed x2) {
        KeyIterator thisKeyIterator = this.highLowContainer.highKeyIterator();

        while(thisKeyIterator.hasNext()) {
            byte[] high = thisKeyIterator.next();
            long containerIdx = thisKeyIterator.currentContainerIdx();
            ContainerWithIndex containerWithIdx2 = x2.highLowContainer.searchContainer(high);
            if (containerWithIdx2 != null) {
                Container thisContainer = this.highLowContainer.getContainer(containerIdx);
                Container freshContainer = thisContainer.iandNot(containerWithIdx2.getContainer());
                this.highLowContainer.replaceContainer(containerIdx, freshContainer);
                if (!freshContainer.isEmpty()) {
                    this.highLowContainer.replaceContainer(containerIdx, freshContainer);
                } else {
                    thisKeyIterator.remove();
                }
            }
        }

    }

    public void writeExternal(ObjectOutput out) throws IOException {
        this.serialize((DataOutput)out);
    }

    public void readExternal(ObjectInput in) throws IOException {
        this.deserialize((DataInput)in);
    }

    public String toString() {
        StringBuilder answer = new StringBuilder();
        LongIterator i = this.getLongIterator();
        answer.append("{");
        if (i.hasNext()) {
            answer.append(i.next());
        }

        while(i.hasNext()) {
            answer.append(",");
            if (answer.length() > 524288) {
                answer.append("...");
                break;
            }

            answer.append(i.next());
        }

        answer.append("}");
        return answer.toString();
    }

    public LongIterator getLongIterator() {
        LeafNodeIterator leafNodeIterator = this.highLowContainer.highKeyLeafNodeIterator(false);
        return this.toIterator(leafNodeIterator, false);
    }

    protected LongIterator toIterator(final LeafNodeIterator keyIte, final boolean reverse) {
        return new LongIterator() {
            private byte[] high;
            private CharIterator charIterator;
            private boolean hasNextCalled = false;

            public boolean hasNext() {
                this.hasNextCalled = true;
                LeafNode leafNode;
                long containerIdx;
                Container container;
                if (this.charIterator != null && !this.charIterator.hasNext()) {
                    do {
                        if (!keyIte.hasNext()) {
                            return false;
                        }

                        leafNode = keyIte.next();
                        this.high = leafNode.getKeyBytes();
                        containerIdx = leafNode.getContainerIdx();
                        container = RoaringBitMapEmbed.this.highLowContainer.getContainer(containerIdx);
                        if (!reverse) {
                            this.charIterator = container.getCharIterator();
                        } else {
                            this.charIterator = container.getReverseCharIterator();
                        }
                    } while(!this.charIterator.hasNext());

                    return true;
                } else if (this.charIterator != null && this.charIterator.hasNext()) {
                    return true;
                } else if (this.charIterator == null) {
                    do {
                        if (!keyIte.hasNext()) {
                            return false;
                        }

                        leafNode = keyIte.next();
                        this.high = leafNode.getKeyBytes();
                        containerIdx = leafNode.getContainerIdx();
                        container = RoaringBitMapEmbed.this.highLowContainer.getContainer(containerIdx);
                        if (!reverse) {
                            this.charIterator = container.getCharIterator();
                        } else {
                            this.charIterator = container.getReverseCharIterator();
                        }
                    } while(!this.charIterator.hasNext());

                    return true;
                } else {
                    return false;
                }
            }

            public long next() {
                boolean hasNext = true;
                if (!this.hasNextCalled) {
                    hasNext = this.hasNext();
                    this.hasNextCalled = false;
                }

                if (hasNext) {
                    char low = this.charIterator.next();
                    return LongUtils.toLong(this.high, low);
                } else {
                    throw new IllegalStateException("empty");
                }
            }

            public LongIterator clone() {
                throw new UnsupportedOperationException("TODO");
            }
        };
    }

    public boolean contains(long x) {
        byte[] high = LongUtils.highPart(x);
        ContainerWithIndex containerWithIdx = this.highLowContainer.searchContainer(high);
        if (containerWithIdx == null) {
            return false;
        } else {
            char low = LongUtils.lowPart(x);
            return containerWithIdx.getContainer().contains(low);
        }
    }

    public int getSizeInBytes() {
        throw new UnsupportedOperationException();
    }

    public long getLongSizeInBytes() {
        throw new UnsupportedOperationException();
    }

    public boolean isEmpty() {
        return this.getLongCardinality() == 0L;
    }

    public ImmutableLongBitmapDataProvider limit(long x) {
        throw new UnsupportedOperationException("TODO");
    }

    public boolean runOptimize() {
        boolean hasChanged = false;
        ContainerIterator containerIterator = this.highLowContainer.containerIterator();

        while(containerIterator.hasNext()) {
            Container container = containerIterator.next();
            Container freshContainer = container.runOptimize();
            if (freshContainer instanceof RunContainer) {
                hasChanged = true;
                containerIterator.replace(freshContainer);
            }
        }

        return hasChanged;
    }

    public void serialize(DataOutput out) throws IOException {
        this.highLowContainer.serialize(out);
    }

    public void serialize(ByteBuffer byteBuffer) throws IOException {
        this.highLowContainer.serialize(byteBuffer);
    }

    public void deserialize(DataInput in) throws IOException {
        this.clear();
        this.highLowContainer.deserialize(in);
    }

    public void deserialize(ByteBuffer in) throws IOException {
        this.clear();
        this.highLowContainer.deserialize(in);
    }

    public long serializedSizeInBytes() {
        long nbBytes = this.highLowContainer.serializedSizeInBytes();
        return nbBytes;
    }

    public void clear() {
        if(this.highLowContainer != null){
            ContainerIterator it = this.highLowContainer.containerIterator();
            while (it.hasNext()){
                Container container = it.next();
                container.clear();
            }
            this.highLowContainer.clear();
            this.highLowContainer = null;
        }
    }

    public long[] toArray() {
        long cardinality = this.getLongCardinality();
        if (cardinality > 2147483647L) {
            throw new IllegalStateException("The cardinality does not fit in an array");
        } else {
            long[] array = new long[(int)cardinality];
            int pos = 0;

            for(LongIterator it = this.getLongIterator(); it.hasNext(); array[pos++] = it.next()) {
            }

            return array;
        }
    }

    public static Roaring64Bitmap bitmapOf(long... dat) {
        Roaring64Bitmap ans = new Roaring64Bitmap();
        ans.add(dat);
        return ans;
    }

    public void add(long... dat) {
        long[] var2 = dat;
        int var3 = dat.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            long oneLong = var2[var4];
            this.addLong(oneLong);
        }

    }

    public void add(long rangeStart, long rangeEnd) {
        byte[] startHigh = LongUtils.highPart(rangeStart);
        int startLow = LongUtils.lowPart(rangeStart);
        byte[] endHigh = LongUtils.highPart(rangeEnd - 1L);
        int endLow = LongUtils.lowPart(rangeEnd - 1L);
        long rangeStartVal = rangeStart;

        for(byte[] startHighKey = startHigh; LongUtils.compareHigh(startHighKey, endHigh) <= 0; startHighKey = LongUtils.highPart(rangeStartVal)) {
            int containerStart = LongUtils.compareHigh(startHighKey, startHigh) == 0 ? startLow : 0;
            int containerLast = LongUtils.compareHigh(startHighKey, endHigh) == 0 ? endLow : Util.maxLowBitAsInteger();
            ContainerWithIndex containerWithIndex = this.highLowContainer.searchContainer(startHighKey);
            if (containerWithIndex != null) {
                long containerIdx = containerWithIndex.getContainerIdx();
                Container freshContainer = this.highLowContainer.getContainer(containerIdx).iadd(containerStart, containerLast + 1);
                this.highLowContainer.replaceContainer(containerIdx, freshContainer);
            } else {
                Container freshContainer = Container.rangeOfOnes(containerStart, containerLast + 1);
                this.highLowContainer.put(startHighKey, freshContainer);
            }

            rangeStartVal = rangeStartVal + (long)(containerLast - containerStart) + 1L;
        }

    }

    public LongIterator getReverseLongIterator() {
        LeafNodeIterator leafNodeIterator = this.highLowContainer.highKeyLeafNodeIterator(true);
        return this.toIterator(leafNodeIterator, true);
    }

    public void removeLong(long x) {
        byte[] high = LongUtils.highPart(x);
        ContainerWithIndex containerWithIdx = this.highLowContainer.searchContainer(high);
        if (containerWithIdx != null) {
            char low = LongUtils.lowPart(x);
            Container container = containerWithIdx.getContainer();
            Container freshContainer = container.remove(low);
            this.highLowContainer.replaceContainer(containerWithIdx.getContainerIdx(), freshContainer);
        }

    }

    public void trim() {
        if (!this.highLowContainer.isEmpty()) {
            KeyIterator keyIterator = this.highLowContainer.highKeyIterator();

            while(keyIterator.hasNext()) {
                long containerIdx = keyIterator.currentContainerIdx();
                Container container = this.highLowContainer.getContainer(containerIdx);
                if (container.isEmpty()) {
                    keyIterator.remove();
                } else {
                    container.trim();
                }
            }

        }
    }

    public int hashCode() {
        return this.highLowContainer.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            RoaringBitMapEmbed other = (RoaringBitMapEmbed)obj;
            return Objects.equals(this.highLowContainer, other.highLowContainer);
        }
    }

    public void flip(long x) {
        byte[] high = LongUtils.highPart(x);
        ContainerWithIndex containerWithIndex = this.highLowContainer.searchContainer(high);
        if (containerWithIndex == null) {
            this.addLong(x);
        } else {
            char low = LongUtils.lowPart(x);
            Container freshOne = containerWithIndex.getContainer().flip(low);
            this.highLowContainer.replaceContainer(containerWithIndex.getContainerIdx(), freshOne);
        }

    }

    public RoaringBitMapEmbed clone() {
        long sizeInBytesL = this.serializedSizeInBytes();
        if (sizeInBytesL >= 2147483647L) {
            throw new UnsupportedOperationException();
        } else {
            int sizeInBytesInt = (int)sizeInBytesL;
            ByteBuffer byteBuffer = ByteBuffer.allocate(sizeInBytesInt).order(ByteOrder.LITTLE_ENDIAN);

            try {
                this.serialize(byteBuffer);
                byteBuffer.flip();
                RoaringBitMapEmbed freshOne = new RoaringBitMapEmbed();
                freshOne.deserialize(byteBuffer);
                return freshOne;
            } catch (Exception var6) {
                throw new RuntimeException("fail to clone thorough the ser/deser", var6);
            }
        }
    }
}

