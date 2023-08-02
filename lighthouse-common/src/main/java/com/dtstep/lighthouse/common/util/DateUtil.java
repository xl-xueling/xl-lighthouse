package com.dtstep.lighthouse.common.util;
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
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public final class DateUtil {

    private DateUtil(){}

    private static final String FORMAT_STR_1 = "yyyy-MM-dd HH:mm:ss";

    private static final String FORMAT_STR_2 = "yyyyMMddHHmmss";

    private static final String FORMAT_STR_3 = "yyyyMMddHHmm";

    private static final String FORMAT_STR_4 = "yyyyMMddHH";

    private static final String FORMAT_STR_5 = "yyyy-MM-dd";

    private static final String FORMAT_STR_6 = "yyyyMMdd";

    private static final String FORMAT_STR_7 = "HH:mm:ss";

    private static final String FORMAT_STR_8 = "HH:mm";

    private static final DateTimeFormatter TIME_FORMATTER1 = DateTimeFormatter.ofPattern(FORMAT_STR_1);

    private static final DateTimeFormatter TIME_FORMATTER2 = DateTimeFormatter.ofPattern(FORMAT_STR_2);

    private static final DateTimeFormatter TIME_FORMATTER3 = DateTimeFormatter.ofPattern(FORMAT_STR_3);

    private static final DateTimeFormatter TIME_FORMATTER4 = DateTimeFormatter.ofPattern(FORMAT_STR_4);

    private static final DateTimeFormatter TIME_FORMATTER5 = DateTimeFormatter.ofPattern(FORMAT_STR_5);

    private static final DateTimeFormatter TIME_FORMATTER6 = DateTimeFormatter.ofPattern(FORMAT_STR_6);

    private static final DateTimeFormatter TIME_FORMATTER7 = DateTimeFormatter.ofPattern(FORMAT_STR_7);

    private static final DateTimeFormatter TIME_FORMATTER8 = DateTimeFormatter.ofPattern(FORMAT_STR_8);

    public static long getCurrentHourTime(){
        return getCurrentHourTime(System.currentTimeMillis());
    }

    public static long getCurrentHourTime(long t){
        return t / (60 * 60 * 1000) * (60 * 60 * 1000);
    }

    public static long getCurrentMinuteTime(){
        return getCurrentMinuteTime(System.currentTimeMillis());
    }

    public static long getCurrentMinuteTime(long t){
        return t / (60 * 1000) * (60 * 1000);
    }

    public static LocalDate date2LocalDate(Date date){
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime date2ToLocalDateTime(Date date){
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static Date localDate2Date(LocalDate localDate){
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDate.atStartOfDay().atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    public static Date localDateTime2Date(LocalDateTime localDateTime){
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    public static long getDayStartTime(long t){
        return Instant.ofEpochMilli(t).atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static long getDayEndTime(long t){
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(t), ZoneId.systemDefault());
        LocalDateTime endTime = LocalDateTime.of(localDateTime.toLocalDate(),LocalTime.MAX);
        return endTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static int getDayIndexOfYear(long t){
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(t), ZoneId.systemDefault());
        return localDateTime.getDayOfYear();
    }

    public static int getMonthIndexOfYear(long t){
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(t), ZoneId.systemDefault());
        return localDateTime.getMonthValue();
    }

    public static int getYear(long t) {
        if(t < year_2024_timestamp && t >= year_2023_timestamp){
            return year_2023;
        }else if(t < year_2025_timestamp && t >= year_2024_timestamp){
            return year_2024;
        }else{
            LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(t), ZoneId.systemDefault());
            return localDateTime.getYear();
        }
    }

    public static long getDayTime(int year,int dayOfYear){
        LocalDate localDate = LocalDate.ofYearDay(year,dayOfYear);
        return localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    private static final int year_2023 = 2023;
    private static final long year_2023_timestamp = 1672502400000L;
    private static final int year_2024 = 2024;
    private static final long year_2024_timestamp = 1704038400000L;
    private static final int year_2025 = 2025;
    private static final long year_2025_timestamp = 1735660800000L;

    public static long getYearStartTime(int year){
        if(year == year_2023){
            return year_2023_timestamp;
        }else if(year == year_2024){
            return year_2024_timestamp;
        }else if(year == year_2025){
            return year_2025_timestamp;
        }else{
            LocalDate date = LocalDate.of(year, 1, 1);
            return date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }
    }

    public static long getYearStartTime(long t){
        int year = getYear(t);
        return LocalDate.of(year, 1, 1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static long getDayBefore(long t,int beforeDay){
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(t), ZoneId.systemDefault());
        return localDateTime.minusDays(beforeDay).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static long getDayAfter(long t,int afterDay){
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(t), ZoneId.systemDefault());
        return localDateTime.plusDays(afterDay).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static long daysBetween(long t1,long t2){
        LocalDate localDate1 = LocalDateTime.ofInstant(Instant.ofEpochMilli(t1), ZoneId.systemDefault()).toLocalDate();
        LocalDate localDate2 = LocalDateTime.ofInstant(Instant.ofEpochMilli(t2), ZoneId.systemDefault()).toLocalDate();
        return localDate1.until(localDate2, ChronoUnit.DAYS);
    }

    public static long getWeekStartTime(long t){
        TemporalField fieldISO = WeekFields.of(Locale.getDefault()).dayOfWeek();
        LocalDate localDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(t), ZoneId.systemDefault()).toLocalDate();
        return localDate.with(fieldISO, 1).atStartOfDay(ZoneId.systemDefault()).plusDays(1).toInstant().toEpochMilli();
    }

    public static long getWeekEndTime(long t){
        TemporalField fieldISO = WeekFields.of(Locale.getDefault()).dayOfWeek();
        LocalDate localDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(t), ZoneId.systemDefault()).toLocalDate().with(fieldISO, 7).plusDays(1);
        LocalDateTime endTime = LocalDateTime.of(localDate,LocalTime.MAX);
        return endTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static long getMonthStartTime(long t){
        LocalDate localDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(t), ZoneId.systemDefault()).toLocalDate();
        return localDate.with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static long getMonthEndTime(long t){
        LocalDate localDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(t), ZoneId.systemDefault()).toLocalDate().with(TemporalAdjusters.lastDayOfMonth());
        LocalDateTime endTime = LocalDateTime.of(localDate,LocalTime.MAX);
        return endTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static long getMonthStartTime(int year,int month){
        LocalDate localDate = LocalDate.of(year,month,1);
        return localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static long getMonthEndTime(int year,int month){
        LocalDate localDate = LocalDate.of(year,month,1).with(TemporalAdjusters.lastDayOfMonth());
        LocalDateTime endTime = LocalDateTime.of(localDate,LocalTime.MAX);
        return endTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static long getHourStartTime(long t){
//        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(t), ZoneId.systemDefault()).withMinute(0).withSecond(0).withNano(0);
//        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return getCurrentHourTime(t);
    }

    public static long getHourEndTime(long t){
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(t), ZoneId.systemDefault()).withMinute(59).withSecond(59).withNano(999);
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static long getHourBefore(long t,int n){
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(t), ZoneId.systemDefault());
        return localDateTime.minusHours(n).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static long getHourAfter(long t,int n){
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(t), ZoneId.systemDefault());
        return localDateTime.plusHours(n).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static long getMinuteBefore(long t,int n){
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(t), ZoneId.systemDefault());
        return localDateTime.minusMinutes(n).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static long getSecondBefore(long t,int n){
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(t), ZoneId.systemDefault());
        return localDateTime.minusSeconds(n).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static long getSecondAfter(long t,int n){
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(t), ZoneId.systemDefault());
        return localDateTime.plusSeconds(n).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static long getMinuteAfter(long t,int n){
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(t), ZoneId.systemDefault());
        return localDateTime.plusMinutes(n).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static List<String> getBetweenDays(String startDate,String endDate) throws Exception{
        long t1 = parseDate(startDate,"yyyy-MM-dd");
        long t2 = parseDate(endDate,"yyyy-MM-dd");
        return getBetweenDays(t1,t2);
    }

    public static List<String> getBetweenDays(long startTime,long endTime){
        List<String> list = new ArrayList<>();
        LocalDate startDate = Instant.ofEpochMilli(startTime).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = Instant.ofEpochMilli(endTime).atZone(ZoneId.systemDefault()).toLocalDate();
        if(startTime == endTime){
            list.add(startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            return list;
        }
        long distance = ChronoUnit.DAYS.between(startDate, endDate);
        if (distance < 1) {
            return list;
        }
        list = Stream.iterate(startDate, d -> {return d.plusDays(1); }).limit(distance + 1).map(LocalDate::toString).collect(Collectors.toList());
        return list;
    }

    public static String formatDate(Date date,String fmt) {
        return formatTimeStamp(date.getTime(),fmt);
    }

    public static String formatTimeStamp(long time,String fmt) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
        if(FORMAT_STR_8.equals(fmt)){
            return localDateTime.format(TIME_FORMATTER8);
        }else if(FORMAT_STR_7.equals(fmt)){
            return localDateTime.format(TIME_FORMATTER7);
        }else if(FORMAT_STR_6.equals(fmt)){
            return localDateTime.format(TIME_FORMATTER6);
        }else if(FORMAT_STR_5.equals(fmt)){
            return localDateTime.format(TIME_FORMATTER5);
        }else if(FORMAT_STR_4.equals(fmt)){
            return localDateTime.format(TIME_FORMATTER4);
        }else if(FORMAT_STR_3.equals(fmt)){
            return localDateTime.format(TIME_FORMATTER3);
        }else if(FORMAT_STR_2.equals(fmt)){
            return localDateTime.format(TIME_FORMATTER2);
        }else if(FORMAT_STR_1.equals(fmt)){
            return localDateTime.format(TIME_FORMATTER1);
        }else{
            return localDateTime.format(DateTimeFormatter.ofPattern(fmt));
        }
    }

    public static long parseDate(String dateStr,String fmt){
        if(FORMAT_STR_6.equals(fmt)){
            LocalDate ldt = LocalDate.parse(dateStr, TIME_FORMATTER6);
            return ldt.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }else if(FORMAT_STR_5.equals(fmt)){
            LocalDate ldt = LocalDate.parse(dateStr,TIME_FORMATTER5);
            return ldt.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }else if(FORMAT_STR_4.equals(fmt)){
            LocalDateTime ldt = LocalDateTime.parse(dateStr,TIME_FORMATTER4);
            return ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }else if(FORMAT_STR_3.equals(fmt)){
            LocalDateTime ldt = LocalDateTime.parse(dateStr,TIME_FORMATTER3);
            return ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }else if(FORMAT_STR_2.equals(fmt)){
            LocalDateTime ldt = LocalDateTime.parse(dateStr,TIME_FORMATTER2);
            return ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }else if(FORMAT_STR_1.equals(fmt)){
            LocalDateTime ldt = LocalDateTime.parse(dateStr, TIME_FORMATTER1);
            return ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }else{
            LocalDateTime ldt = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(fmt));
            return ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }
    }

    public static long batchTime(int interval, TimeUnit timeUnit, long current){
        long duration = timeUnit.toMillis(interval);
        int year = DateUtil.getYear(current);
        long res = 0L;
        if(timeUnit == TimeUnit.MINUTES){
            long yearStart = DateUtil.getYearStartTime(year);
            int index = (int)((current - yearStart) / duration);
            res = yearStart + index * duration;
        }else if(timeUnit == TimeUnit.HOURS){
            long yearStart = DateUtil.getYearStartTime(year);
            int index = (int)((current - yearStart) / duration);
            res = yearStart + index * duration;
        }else if(timeUnit == TimeUnit.DAYS){
            int dayIndex = DateUtil.getDayIndexOfYear(current);
            int tempIndex = (dayIndex - 1) / interval  * interval  + 1;
            res = DateUtil.getDayTime(year, tempIndex);
        }else if(timeUnit == TimeUnit.SECONDS){
            long yearStart = DateUtil.getYearStartTime(year);
            int index = (int)((current - yearStart) / duration);
            res = yearStart + index * duration;
        }
        return res;
    }

    public static long previousBatchTime(int interval, TimeUnit timeUnit, long current){
        return batchTime(interval,timeUnit,current - timeUnit.toMillis(interval));
    }

    public static int getCurrentYear(){
        LocalDateTime currentDate = LocalDateTime.now();
        return currentDate.getYear();
    }
}
