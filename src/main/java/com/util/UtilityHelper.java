package com.util;

import java.sql.Timestamp;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilityHelper {

	final static String regExp = "[0-9]+([,.][0-9]{1,2})?";

	public static boolean isDecimal(String input) {
		boolean isMatched = false;
		final Pattern pattern = Pattern.compile(regExp);
		// This can be repeated in a loop with different inputs:
		Matcher matcher = pattern.matcher(input);
		if (matcher.matches()) {
			isMatched = true;
		}
		return isMatched;
	}

	public static Date convertTimestampToDate(String input) {
		input = input.replace(".", "");
		Timestamp ts = new Timestamp(new Long(input));// System.currentTimeMillis());
		Date date = new Date(ts.getTime());
		return date;
	}

	public static Timestamp convertDateToTimestamp(String input) {
		Date date = new Date(input);
		Timestamp ts = new Timestamp(date.getTime());// System.currentTimeMillis());
		return ts;
	}

	public static void main(String args[]) {
		System.out.println(convertDateToTimestamp("1980/12/11"));
		// System.out.println(convertTimestampToDate("1394283600"));

	}
}
