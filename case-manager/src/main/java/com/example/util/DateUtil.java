package com.example.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日期时间工具类
 */
public class DateUtil {

	private static final String DATE_PATTERN = "yyyy-MM-dd";
	private static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN);
	private static final DateFormat TIME_FORMAT = new SimpleDateFormat(TIME_PATTERN);
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN);

	/**
	 * 转换Date为字符串
	 * @param date Date对象
	 * @return yyyy-MM-dd格式字符串
	 */
	public static String toDateStr(Date date) {
		return DATE_FORMAT.format(date);
	}

	/**
	 * 转换LocalDate为字符串
	 * @param date LocalDate对象
	 * @return yyyy-MM-dd格式字符串
	 */
	public static String toDateStr(LocalDate date) {
		return date.format(DATE_FORMATTER);
	}

	/**
	 * 转换Date为字符串
	 * @param date Date对象
	 * @return yyyy-MM-dd hh:mm:ss格式字符串
	 */
	public static String toTimeStr(Date date) {
		return TIME_FORMAT.format(date);
	}

	/**
	 * 转换LocalDateTime为字符串
	 * @param date LocalDateTime对象
	 * @return yyyy-MM-dd hh:mm:ss格式字符串
	 */
	public static String toTimeStr(LocalDateTime date) {
		return date.format(DATE_TIME_FORMATTER);
	}
}