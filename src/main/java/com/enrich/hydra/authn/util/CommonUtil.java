package com.enrich.hydra.authn.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtil {

	  public static String formatDateTime(String inputDateStr, String inputPattern, String outputPattern) throws ParseException {
	        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
	        Date parsedDate = inputFormat.parse(inputDateStr);
	        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
	        return outputFormat.format(parsedDate);
	    }
}
