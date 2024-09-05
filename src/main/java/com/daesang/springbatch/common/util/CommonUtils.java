package com.daesang.springbatch.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * fileName			: CommonUtils
 * author			: 최종민차장
 * date				: 2022-11-17
 * descrition       : 공통 Util 정보
 * =======================================================
 * DATE					AUTHOR					NOTE
 * -------------------------------------------------------
 * 2022-11-17			최종민차장             최초생성
 */

public class CommonUtils {

    /**
     * 숫자 확인 Util
     * @param s
     * @return
     */
    public static boolean isNumeric(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 특정 Key 중복제거
     * @param keyExtractor
     * @return
     * @param <T>
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * LONG 문자열 시간으로 변환(HH시간MM분SS초)
     * @param time
     * @return
     */
    public static String longToStrTime(Long time){
        Long hour = TimeUnit.MILLISECONDS.toHours(time);
        String s_hour = hour > 0 ? hour + "시간" : "";
        Long min =  TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(hour);
        String s_min = min > 0 ? min + "분" : "";
        Long sec = TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(min);
        String s_sec = sec > 0 ? sec + "초" : "";
        return s_hour + s_min + s_sec;
    }

    /**
     * LONG 문자열 DATETIME으로 변환(yyyy-MM-dd HH:mm:ss)
     * @param dateTime
     * @return
     */
    public static String longToStrDateTime(Long dateTime){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date(dateTime));
    }

    /**
     * LONG 문자열 DATETIME으로 변환(datePattern)
     * @param dateTime
     * @param datePattern
     * @return
     */
    public static String longToStrDateTime(Long dateTime, String datePattern){
        SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
        return dateFormat.format(new Date(dateTime));
    }

    /**
     * 당일 날짜 확인
     * @param
     * @return
     */
    public static String getToday() {
        LocalDateTime date = LocalDateTime.now();
        String today = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return today;
    }

    /**
     * 당월 조회
     * @param
     * @return
     */
    public static String getCurrentMonth() {
        LocalDateTime date = LocalDateTime.now();
        String today = date.format(DateTimeFormatter.ofPattern("yyyyMM"));
        return today;
    }


    /**
     * filelist 파일 읽기
     * @param file
     * @return
     */
    public static String readFile(File file) {
        StringBuilder sb = null;
        FileInputStream fis = null;
        BufferedReader br = null;

        try {
            sb = new StringBuilder();
            fis = new FileInputStream(file);
            br = new BufferedReader(new InputStreamReader(fis));

            String line;
            while((line = br.readLine()) != null) {
                sb.append(line + System.lineSeparator());
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                br.close();
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    /**
     * 현재 일 DateString 생성
     * @param datePattern: return Date 패턴
     * @return
     */
    public static String getToday(String datePattern) {
        LocalDateTime date = LocalDateTime.now();
        return date.format(DateTimeFormatter.ofPattern(datePattern));
    }

    /**
     * 지난 일 DateString 생성
     * @param minusDay: 이전 날짜 수
     * @param datePattern: return Date 패턴
     * @return
     */
    public static String getLastDay(int minusDay, String datePattern) {
        LocalDateTime date = LocalDateTime.now();
        date = date.minusDays(minusDay);
        return date.format(DateTimeFormatter.ofPattern(datePattern));
    }

    /**
     * 현재 월 첫째날 DateString 생성
     * @param datePattern: return Date 패턴
     * @return
     */
    public static String getCurrentMonthFirstDay(String datePattern) {
        LocalDateTime date = LocalDateTime.now();
        date = date.with(TemporalAdjusters.firstDayOfMonth());
        return date.format(DateTimeFormatter.ofPattern(datePattern));
    }

    /**
     * 현재 월 DateString 생성
     * @param datePattern: return Date 패턴
     * @return
     */
    public static String getCurrentMonth(String datePattern) {
        LocalDateTime date = LocalDateTime.now();
        return date.format(DateTimeFormatter.ofPattern(datePattern));
    }

    /**
     * 지난 월 DateString 생성
     * @param minusMonth: 이전 개월 수
     * @param datePattern: return Date 패턴 
     * @return
     */
    public static String getLastMonth(int minusMonth, String datePattern) {
        LocalDateTime date = LocalDateTime.now();
        date = date.minusMonths(minusMonth);
        return date.format(DateTimeFormatter.ofPattern(datePattern));
    }

    /**
     * 지난 월 첫째날 DateString 생성
     * @param minusMonth: 이전 개월 수
     * @param datePattern: return Date 패턴
     * @return
     */
    public static String getLastMonthFirstDay(int minusMonth, String datePattern) {
        LocalDateTime date = LocalDateTime.now();
        date = date.minusMonths(minusMonth);
        date = date.with(TemporalAdjusters.firstDayOfMonth());
        return date.format(DateTimeFormatter.ofPattern(datePattern));
    }

}
