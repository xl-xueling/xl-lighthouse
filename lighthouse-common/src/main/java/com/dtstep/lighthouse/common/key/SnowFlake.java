package com.dtstep.lighthouse.common.key;


public class SnowFlake {

    private final static long START_STMP = 1480166465631L;

    private final static long SEQUENCE_BIT = 12;

    private final static long MACHINE_BIT = 5;

    private final static long DATACENTER_BIT = 5;

    private final static long MAX_DATACENTER_NUM = -1L ^ (-1L << DATACENTER_BIT);

    private final static long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);

    private final static long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);

    private final static long MACHINE_LEFT = SEQUENCE_BIT;

    private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;

    private final static long TIMESTMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

    private long datacenterId;

    private long machineId;

    private long sequence = 0L;

    private long lastStmp = -1L;

    private static SnowFlake snowFlake = new SnowFlake(3, 6);

    public static SnowFlake getInstance(){
        return snowFlake;
    }

    private SnowFlake(){}

    private SnowFlake(long datacenterId, long machineId) {
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
            throw new IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }

    public synchronized long nextId() {
        long currStmp = getNewstmp();
        if (currStmp < lastStmp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }

        if (currStmp == lastStmp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0L) {
                currStmp = getNextMill();
            }
        } else {
            sequence = 0L;
        }

        lastStmp = currStmp;

        return (currStmp - START_STMP) << TIMESTMP_LEFT
                | datacenterId << DATACENTER_LEFT
                | machineId << MACHINE_LEFT
                | sequence;
    }

    private long getNextMill() {
        long mill = getNewstmp();
        while (mill <= lastStmp) {
            mill = getNewstmp();
        }
        return mill;
    }

    private long getNewstmp() {
        return System.currentTimeMillis();
    }

}