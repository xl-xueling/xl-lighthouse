package com.dtstep.lighthouse.core.tools;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.Objects;


/**
 *
 * last5m	最近5分钟(最近5分钟的开始时间到当前时间)
 * last1h	最近1小时(最近1小时的开始时间到当前时间)
 * last7d	最近7天（最近7天，从开始时间到当前时间）
 *
 * last0d	今天（从今天的开始时间到今天的结束时间23：59：59）
 * prev0d  今天
 * last1d  最近两天
 * prev1d  昨天
 * prev1w	上周（上周的开始时间到上周的结束时间）
 * prev7d	七天前那一天
 * prev1M	上月
 * last7d-last1d	从7天前到1天前（不含今天）
 * last14d-last3d	从7天前到3天前（不含3天气的那天）
 *
 * @return
 * @throws Exception
 */
public class TimePeriodUtil {

    private static final ZoneId ZONE = ZoneId.systemDefault();
    private static final long SECOND_MS = 1000L;
    private static final long MINUTE_MS = 60 * SECOND_MS;
    private static final long HOUR_MS = 60 * MINUTE_MS;
    private static final long DAY_MS = 24 * HOUR_MS;

    public static long[] resolvePeriod(String period) {
        Objects.requireNonNull(period, "period不能为空");
        period = period.trim();
        long now = System.currentTimeMillis();
        ZonedDateTime nowZdt = Instant.ofEpochMilli(now).atZone(ZONE);
        LocalDate today = nowZdt.toLocalDate();
        if (period.contains("-")) {
            String[] parts = period.split("-", 2);
            long start = getStart(parts[0], now, today);
            long end = getStart(parts[1], now, today);
            return new long[] { start, end };
        }
        return new long[] { getStart(period, now, today), getEnd(period, now, today) };
    }

    private static long getStart(String expr, long now, LocalDate today) {
        Parsed p = parse(expr);
        return p.type == Type.LAST ? calcLastStart(p, now, today) : calcPrevStart(p, now, today);
    }

    private static long getEnd(String expr, long now, LocalDate today) {
        Parsed p = parse(expr);
        return p.type == Type.LAST ? calcLastEnd(p, now, today) : calcPrevEnd(p, now, today);
    }

    private static Parsed parse(String expr) {
        if (expr == null) throw new IllegalArgumentException("expr null");
        Parsed p = new Parsed();
        if (expr.startsWith("last")) p.type = Type.LAST;
        else if (expr.startsWith("prev")) p.type = Type.PREV;
        else throw new IllegalArgumentException("expr must start with last or prev: " + expr);
        int i = 4;
        int num = 0;
        boolean hasNum = false;
        while (i < expr.length() && Character.isDigit(expr.charAt(i))) {
            num = num * 10 + (expr.charAt(i++) - '0');
            hasNum = true;
        }
        if (!hasNum || i >= expr.length()) throw new IllegalArgumentException("invalid expr: " + expr);
        p.num = num;
        p.unit = expr.charAt(i);
        return p;
    }

    private static long calcLastStart(Parsed p, long nowMillis, LocalDate today) {
        switch (p.unit) {
            case 's': return nowMillis - (long) p.num * SECOND_MS;
            case 'm': return nowMillis - (long) p.num * MINUTE_MS;
            case 'h': return nowMillis - (long) p.num * HOUR_MS;
            case 'd':
                return (p.num == 0)
                        ? today.atStartOfDay(ZONE).toInstant().toEpochMilli()
                        : today.minusDays(p.num).atStartOfDay(ZONE).toInstant().toEpochMilli();
            case 'w':
                LocalDate weekStart = today.minusWeeks(p.num).with(DayOfWeek.MONDAY);
                return weekStart.atStartOfDay(ZONE).toInstant().toEpochMilli();
            case 'M':
                LocalDate monthStart = today.minusMonths(p.num).withDayOfMonth(1);
                return monthStart.atStartOfDay(ZONE).toInstant().toEpochMilli();
            default:
                throw new IllegalArgumentException("unsupported unit: " + p.unit);
        }
    }

    private static long calcLastEnd(Parsed p, long nowMillis, LocalDate today) {
        if (p.unit == 'd') {
            return today.atTime(23, 59, 59, 999_000_000).atZone(ZONE).toInstant().toEpochMilli();
        }
        if (p.unit == 'w' && p.num >= 1) {
            LocalDate thisWeekEnd = today.with(DayOfWeek.SUNDAY); // 本周日
            return thisWeekEnd.atTime(23, 59, 59, 999_000_000).atZone(ZONE).toInstant().toEpochMilli();
        }
        if (p.unit == 'M' && p.num >= 1) {
            LocalDate thisMonthLast = today.with(TemporalAdjusters.lastDayOfMonth());
            return thisMonthLast.atTime(23, 59, 59, 999_000_000).atZone(ZONE).toInstant().toEpochMilli();
        }
        return nowMillis;
    }

    private static long calcPrevStart(Parsed p, long now, LocalDate today) {
        switch (p.unit) {
            case 's': return (now / SECOND_MS) * SECOND_MS - p.num * SECOND_MS;
            case 'm': return (now / MINUTE_MS) * MINUTE_MS - p.num * MINUTE_MS;
            case 'h': return (now / HOUR_MS) * HOUR_MS - p.num * HOUR_MS;
            case 'd': return today.minusDays(p.num).atStartOfDay(ZONE).toInstant().toEpochMilli();
            case 'w': return today.minusWeeks(p.num).with(DayOfWeek.MONDAY).atStartOfDay(ZONE).toInstant().toEpochMilli();
            case 'M': return today.minusMonths(p.num).withDayOfMonth(1).atStartOfDay(ZONE).toInstant().toEpochMilli();
            default: throw new IllegalArgumentException("unsupported unit: " + p.unit);
        }
    }

    private static long calcPrevEnd(Parsed p, long now, LocalDate today) {
        switch (p.unit) {
            case 's': return (now / SECOND_MS) * SECOND_MS - (p.num - 1) * SECOND_MS - 1;
            case 'm': return (now / MINUTE_MS) * MINUTE_MS - (p.num - 1) * MINUTE_MS - 1;
            case 'h': return (now / HOUR_MS) * HOUR_MS - (p.num - 1) * HOUR_MS - 1;
            case 'd': return today.minusDays(p.num).atTime(23, 59, 59,999_000_000).atZone(ZONE).toInstant().toEpochMilli();
            case 'w': {
                LocalDate ws = today.minusWeeks(p.num).with(DayOfWeek.MONDAY);
                LocalDate we = ws.with(DayOfWeek.SUNDAY);
                return we.atTime(23, 59, 59).atZone(ZONE).toInstant().toEpochMilli();
            }
            case 'M': {
                LocalDate m1 = today.minusMonths(p.num).withDayOfMonth(1);
                LocalDate mEnd = m1.with(TemporalAdjusters.lastDayOfMonth());
                return mEnd.atTime(23, 59, 59,999_000_000).atZone(ZONE).toInstant().toEpochMilli();
            }
            default: throw new IllegalArgumentException("unsupported unit: " + p.unit);
        }
    }

    private enum Type { LAST, PREV }
    private static class Parsed { Type type; int num; char unit; }
}
