package com.example.solution.common.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.util.Date;

/**
 * 时间工具类
 *
 * @date 2021/12/15
 */
@Slf4j
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    /**
     * 日期格式 时 分 如12:00
     */
    public static final String DATE_HOUSE_MINUTE = "HH:mm";

    /**
     * 日期格式 年 如2009
     */
    public static final String DATE_FORMAT_YEAR = "yyyy";

    /**
     * 日期格式 年 月  如 2009-02
     */
    public static final String DATE_FORMAT_MONTH = "yyyy-MM";

    /**
     * 日期格式 年 月 日 如2009-02-26
     */
    public static final String DATE_FORMAT_DAY = "yyyy-MM-dd";

    /**
     * 日期格式 年 月 日 如02/26/2009
     */
    public static final String DATE_FORMAT_DAY_SLASH = "MM/dd/yyyy";

    /**
     * 日期格式 年 月 日 如2009_02_26
     */
    public static final String DATE_FORMAT_DAY_UNDERSCORE = "yyyy_MM_dd";

    /**
     * 日期格式 年 月 日 时 如2009-02-26 15
     */
    public static final String DATE_FORMAT_HOUR = "yyyy-MM-dd HH";

    /**
     * 日期格式 年 月 日 时 分 如2009-02-26 15:40
     */
    public static final String DATE_FORMAT_MINUTE = "yyyy-MM-dd HH:mm";

    /**
     * 日期格式年 月 日 时 分 秒 如 2009-02-26 15:40:00
     */
    public static final String DATE_FORMAT_SECOND = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式年 月 日  如 090226
     */
    public static final String DATE_FORMAT_YEAR_DAY_COMPRESS = "yyMMdd";

    /**
     * 日期格式年 月 日 时 分 秒 如 20090226154000
     */
    public static final String DATE_FORMAT_SECOND_COMPRESS = "yyyyMMddHHmmss";

    /**
     * 日期格式年 月 日 时 分 秒 如 201229110910122
     */
    public static final String DATE_FORMAT_MILLI_SECOND = "yyMMddHHmmssSSS";

    /**
     * 日期格式年 月 日 时 分 秒 微秒 如 20090226154000
     */
    public static final String DATE_FORMAT_MILLISECOND_COMPRESS = "yyyyMMddHHmmssSSS";

    /**
     * 日期格式年 月 日 如 20200915
     */
    public static final String DATE_FORMAT_DAY_COMPRESS = "yyyyMMdd";

    /**
     * 日期格式年 月 如 202009
     */
    public static final String DATE_FORMAT_MONTH_DAY = "yyyyMM";

    /**
     * 日期格式年 月 日 时 分 秒 毫秒 如2009-02-26 15:40:00 110
     */
    public static final String DATE_FORMAT_MILLISECOND = "yyyy-MM-dd HH:mm:ss SSS";

    /**
     * 日期格式 2020/01/20
     */
    public static final String DATE_FORMAT_YEAR_MONTH_DAY = "yyyy/MM/dd";

    /**
     * 日期格式年 月 日 时  如 2021020120
     */
    public static final String DATE_FORMAT_HOUR_COMPRESS = "yyyyMMddHH";

    /**
     * 日期格式 月日 如0207
     */
    public static final String DATE_FORMAT_MONTH_DAY_COMPRESS = "MMdd";


    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(String str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str, parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 增加 LocalDateTime ==> Date
     */
    public static Date toDate(LocalDateTime temporalAccessor) {
        ZonedDateTime zdt = temporalAccessor.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 增加 LocalDate ==> Date
     */
    public static Date toDate(LocalDate temporalAccessor) {
        LocalDateTime localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0));
        return toDate(localDateTime);
    }


    /**
     * 不同时区日期转化
     */
    public static Date convertUsTimeToChinaTime(String date, String source, String target) {
        try {
            LocalDateTime sourceLocalDateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern(DATE_FORMAT_SECOND));
            ZoneId sourceZoneId = ZoneId.of(source);
            ZonedDateTime sourceZonedDateTime = ZonedDateTime.of(sourceLocalDateTime, sourceZoneId);

            ZoneId targetZoneId = ZoneId.of(target);
            ZonedDateTime chinaDateTime = sourceZonedDateTime.withZoneSameInstant(targetZoneId);
            return Date.from(chinaDateTime.toInstant());
        } catch (Exception e) {
            log.error("时间转化失败", e);
            return null;
        }
    }

    public static String datePath() {

        return DateFormatUtils.format(new Date(), "yyyy/MM/dd");
    }
}
