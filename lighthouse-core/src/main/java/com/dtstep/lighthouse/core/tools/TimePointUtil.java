package com.dtstep.lighthouse.core.tools;

import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.stat.StatExtEntity;
import com.dtstep.lighthouse.common.util.DateUtil;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimePointUtil {

    private static final Pattern BATCH_NUMBER = Pattern.compile("t-(\\d+)");
    private static final Pattern BATCH_MINUTE = Pattern.compile("t-(\\d+)m");
    private static final Pattern BATCH_HOUR = Pattern.compile("t-(\\d+)h");
    private static final Pattern BATCH_DAY = Pattern.compile("t-(\\d+)d");
    private static final Pattern BATCH_MONTH = Pattern.compile("t-(\\d+)M");
    private static final Pattern BATCH_YEAR = Pattern.compile("t-(\\d+)y");

    public static Long getBatchTime(StatExtEntity statExtEntity, long currentTime, String batch) throws Exception {
        if (statExtEntity == null) {
            return StatConst.ILLEGAL_VAL;
        }
        if (batch == null || batch.isEmpty()) {
            return StatConst.ILLEGAL_VAL;
        }
        ZoneId zone = ZoneId.systemDefault();
        ZonedDateTime now = Instant.ofEpochMilli(currentTime).atZone(zone);
        Matcher matcher;
        if ((matcher = BATCH_MINUTE.matcher(batch)).matches()) {
            int diff = Integer.parseInt(matcher.group(1));
            ZonedDateTime target = now.minusMinutes(diff);
            return DateUtil.batchTime(
                    statExtEntity.getTimeParamInterval(),
                    statExtEntity.getTimeUnit(),
                    target.toInstant().toEpochMilli()
            );
        }
        if ((matcher = BATCH_HOUR.matcher(batch)).matches()) {
            int diff = Integer.parseInt(matcher.group(1));
            ZonedDateTime target = now.minusHours(diff);
            return DateUtil.batchTime(
                    statExtEntity.getTimeParamInterval(),
                    statExtEntity.getTimeUnit(),
                    target.toInstant().toEpochMilli()
            );
        }

        if ((matcher = BATCH_DAY.matcher(batch)).matches()) {
            int diff = Integer.parseInt(matcher.group(1));
            ZonedDateTime target = now.minusDays(diff);
            return DateUtil.batchTime(
                    statExtEntity.getTimeParamInterval(),
                    statExtEntity.getTimeUnit(),
                    target.toInstant().toEpochMilli()
            );
        }

        if ((matcher = BATCH_MONTH.matcher(batch)).matches()) {
            int diff = Integer.parseInt(matcher.group(1));
            ZonedDateTime target = now.minusMonths(diff);
            return DateUtil.batchTime(
                    statExtEntity.getTimeParamInterval(),
                    statExtEntity.getTimeUnit(),
                    target.toInstant().toEpochMilli()
            );
        }

        if ((matcher = BATCH_YEAR.matcher(batch)).matches()) {
            int diff = Integer.parseInt(matcher.group(1));
            ZonedDateTime target = now.minusYears(diff);
            return DateUtil.batchTime(
                    statExtEntity.getTimeParamInterval(),
                    statExtEntity.getTimeUnit(),
                    target.toInstant().toEpochMilli()
            );
        }

        if ((matcher = BATCH_NUMBER.matcher(batch)).matches()) {
            int diff = Integer.parseInt(matcher.group(1));
            long interval = statExtEntity.getTimeUnit().toMillis(statExtEntity.getTimeParamInterval());
            long timeStamp = currentTime - (long) diff * interval;
            return DateUtil.batchTime(
                    statExtEntity.getTimeParamInterval(),
                    statExtEntity.getTimeUnit(),
                    timeStamp);
        }
        return StatConst.ILLEGAL_VAL;
    }
}
