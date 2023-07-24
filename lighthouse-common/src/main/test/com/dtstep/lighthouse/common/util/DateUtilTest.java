package com.dtstep.lighthouse.common.util;

import org.junit.Test;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class DateUtilTest {

    @Test
    public void getCurrentHourTimeTest(){
        long t1 = DateUtil.getCurrentHourTime();
        System.out.println("t1:" + t1);
    }

    @Test
    public void getDayStartTimeTest() throws Exception {
        Thread.sleep(500);
        long t3 = System.currentTimeMillis();
        for(int i=0;i<10000;i++){
            long t = DateUtil.getDayStartTime(System.currentTimeMillis());
        }
        long t4 = System.currentTimeMillis();
        System.out.println("cost2:" + (t4 - t3));
    }

    @Test
    public void getDayEndTimeTest() throws Exception{
        long t = DateUtil.getDayEndTime(System.currentTimeMillis());
        System.out.println(t);
    }


    @Test
    public void getMonthIndexOfYearTest() throws Exception{
        int n = DateUtil.getMonthIndexOfYear(System.currentTimeMillis());
        System.out.println("n:" + n);
    }

    @Test
    public void getYearTest() throws Exception{
        int year = DateUtil.getYear(System.currentTimeMillis());
        System.out.println("year:" + year);
    }

    @Test
    public void getDayTimeTest() throws Exception{
        long t = DateUtil.getDayTime(2019,3);
        System.out.println("t1:" + t);
    }

    @Test
    public void getYearStartTimeTest() throws Exception{
        long t = DateUtil.getYearStartTime(System.currentTimeMillis());
        System.out.println("t:" + t);
    }

    @Test
    public void getDayBeforeTest() throws Exception {
        long t = DateUtil.getDayBefore(System.currentTimeMillis(),5);
        System.out.println("t:" + t);
    }

    @Test
    public void daysBetweenTest() throws Exception{
        long days = DateUtil.daysBetween(System.currentTimeMillis(),DateUtil.getDayBefore(System.currentTimeMillis(),10));
        System.out.println("days:" + days);
    }

    @Test
    public void getWeekStartTimeTest() throws Exception {
        long t = DateUtil.getWeekStartTime(System.currentTimeMillis());
        System.out.println("t:" + t);
    }

    @Test
    public void getWeekEndTimeTest() throws Exception{
        long t = DateUtil.getWeekEndTime(System.currentTimeMillis());
        System.out.println("t:" + t);
    }

    @Test
    public void getMonthStartTimeTest() throws Exception {
        long t = DateUtil.getMonthStartTime(System.currentTimeMillis());
        System.out.println("t:" + t);

        long t2 = DateUtil.getMonthStartTime(2019,11);
        System.out.println("t2:" + t2);
    }

    @Test
    public void getMonthEndTimeTest() throws Exception {
        long t = DateUtil.getMonthEndTime(System.currentTimeMillis());
        System.out.println("t:" + t);

        long t2 = DateUtil.getMonthEndTime(2019,12);
        System.out.println("t2:" + t2);
    }

    @Test
    public void getHourStartTimeTest() throws Exception{
        long t = DateUtil.getHourStartTime(System.currentTimeMillis());
        System.out.println("t:" + t);
    }

    @Test
    public void getHourEndTimeTest() throws Exception{
        long t = DateUtil.getHourEndTime(System.currentTimeMillis());
        System.out.println("t:" + t);
    }

    @Test
    public void getBetweenDaysTest() throws Exception{
        long t1 = DateUtil.getDayBefore(System.currentTimeMillis(),13);
        List<String> list = DateUtil.getBetweenDays(t1,System.currentTimeMillis());
        for(int i=0;i<list.size();i++){
            System.out.println(list.get(i));
        }
    }

    @Test
    public void getBetweenDaysTest2() throws Exception{
        String date1 = "2019-01-03";
        String date2 = "2019-01-15";
        long t1 = DateUtil.parseDate(date1,"yyyy-MM-dd");
        List<String> list = DateUtil.getBetweenDays(date1,date2);
        for(int i=0;i<list.size();i++){
            String temp = list.get(i);
            System.out.println("temp:" + temp);
        }
    }

    @Test
    public void getCurMinute() throws Exception{
        int size = 1;
        for(int n=0;n<300;n++){
            long time = System.currentTimeMillis() - new Random().nextInt(1000000000);
            long t3 = System.currentTimeMillis();
            for(int i=0;i<size;i++){
                long t = DateUtil.getCurrentMinuteTime(time);
                System.out.println("t:" + t + ",time:" + DateUtil.formatTimeStamp(time,"yyyy-MM-dd HH:mm:ss") + ",date:" + DateUtil.formatTimeStamp(t,"yyyy-MM-dd HH:mm:ss"));
            }
            long t4 = System.currentTimeMillis();
            Thread.sleep(1000);
        }
    }

    @Test
    public void getCurrentTime() throws Exception{
        int size = 1000000000;
        for(int n =0;n<100;n++){
            long t1 = System.currentTimeMillis();
            for(int i=0;i<size;i++){
                long t = t1;
            }
            long t2 = System.currentTimeMillis();

            long t3 = System.currentTimeMillis();
            for(int i=0;i<size;i++){
                long t = System.nanoTime();
            }
            long t4 = System.currentTimeMillis();
            System.out.println("cost1:" + (t2 - t1) + ",cost2:" + (t4 - t3));
            Thread.sleep(1000);
        }
    }

    @Test
    public void test000() throws Exception{
        for(int i=0;i<1000;i++){
            int n = new Random().nextInt(100000000);
            long t = System.currentTimeMillis()  -  n;
            System.out.println("date:" + DateUtil.formatTimeStamp(t,"yyyy-MM-dd HH:mm:ss") + ",hour time:" + DateUtil.formatTimeStamp(DateUtil.getCurrentMinuteTime(t),"yyyy-MM-dd HH:mm:ss"));
        }
    }

    @Test
    public void test001() throws Exception{
        int size = 10000000;
        for(int n =0;n<100;n++){
            long t1 = System.currentTimeMillis();
            for(int i=0;i<size;i++){
                long t = DateUtil.parseDate("2005122003","yyMMddHHmm");
            }
            long t2 = System.currentTimeMillis();

            long t3 = System.currentTimeMillis();
            for(int i=0;i<size;i++){
                long t = DateUtil.getCurrentMinuteTime();
            }
            long t4 = System.currentTimeMillis();
            System.out.println("cost1:" + (t2 - t1) + ",cost2:" + (t4 - t3));
            Thread.sleep(1000);
        }
    }

    @Test
    public void test002() throws Exception{
        int size = 10000000;
        Object object = new Object();
        for(int n =0;n<100;n++){
            long t1 = System.currentTimeMillis();
            synchronized (object){
                object.wait(3);
            }
            synchronized (object){
                object.notify();
            }
            long t2 = System.currentTimeMillis();

            long t3 = System.currentTimeMillis();
            for(int i=0;i<size;i++){
                long t = DateUtil.getCurrentMinuteTime();
            }
            long t4 = System.currentTimeMillis();
            System.out.println("cost1:" + (t2 - t1) + ",cost2:" + (t4 - t3));
            Thread.sleep(1000);
        }
    }

    @Test
    public void test003() throws Exception{
        System.out.println("-Test");
        long batchTime = DateUtil.batchTime(10, TimeUnit.SECONDS,System.currentTimeMillis());
        System.out.println("time:" + DateUtil.formatTimeStamp(batchTime,"yyyy-MM-dd HH:mm:ss"));
    }

    @Test
    public void test004() throws Exception{
        long t1 = System.currentTimeMillis();
        long t2 = DateUtil.getMonthStartTime(t1);
        long delta = (t1 - t2) / (60 * 60 * 1000);
        System.out.println("delta:" + delta);
        for(int i=0;i<=delta;i++){
            System.out.println("temp date:" + DateUtil.formatTimeStamp(t2 + i * (60 * 60 * 1000),"yyyy-MM-dd HH:mm:ss"));
        }
    }


    @Test
    public void test0021() throws Exception{
        int size = 1000000;
        Object object = new Object();
        long temp = System.currentTimeMillis();
        for(int n =0;n<100;n++){
            long t1 = System.currentTimeMillis();
            for(int i=0;i<size;i++) {
            }
            long t2 = System.currentTimeMillis();

            long t3 = System.currentTimeMillis();
            for(int i=0;i<size;i++){
                long b = DateUtil.getDayStartTime(temp);
            }
            long t4 = System.currentTimeMillis();
            System.out.println("cost1:" + (t2 - t1) + ",cost2:" + (t4 - t3));
            Thread.sleep(1000);
        }
    }

    @Test
    public void test0023() throws Exception {
        for(int i=0;i<10000;i++){
            long a = System.currentTimeMillis() - ThreadLocalRandom.current().nextLong(2000000000);
            long b = DateUtil.getDayStartTime(a);
            System.out.println("current date:" + DateUtil.formatTimeStamp(a,"yyyy-MM-dd HH:mm:ss") + ",day start date:" + DateUtil.formatTimeStamp(b,"yyyy-MM-dd HH:mm:ss"));
        }
    }

    @Test
    public void testBatchTime() throws Exception {
        long t1 = System.currentTimeMillis();
        long t = DateUtil.batchTime(1,TimeUnit.MINUTES,System.currentTimeMillis());
        System.out.println("t:" + t + ",date:" + DateUtil.formatTimeStamp(t,"yyyy-MM-dd HH:mm:ss"));
    }
}
