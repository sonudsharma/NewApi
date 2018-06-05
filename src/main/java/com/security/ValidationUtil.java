package com.security;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class ValidationUtil {
	public static boolean EmailValid(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";

		Pattern pat = Pattern.compile(emailRegex);
		if (email == null)
			return false;
		return pat.matcher(email).matches();
	}

	public static boolean DateValid(String Date) {
		/* Check if date is 'null' */
		if (Date.trim().equals("")) {
			return true;
		}
		/* Date is not 'null' */
		else {
			/*
			 * Set preferred date format, For example MM-dd-yyyy,
			 * MM.dd.yyyy,dd.MM.yyyy etc.
			 */
			SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyy/MM/dd");
			sdfrmt.setLenient(false);
			/*
			 * Create Date object parse the string into date
			 */
			try {
				Date javaDate = sdfrmt.parse(Date);
				System.out.println(Date + " is valid date format");
			}
			/* Date format is invalid */
			catch (ParseException e) {
				System.out.println(Date + " is Invalid Date format");
				return false;
			}
			/* Return true if date format is valid */
			return true;
		}
	}
}
