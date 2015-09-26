package ch.ivyteam.ivy.addons.filemanager.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;


public final class DateUtil {
	
	public static final String DATE_FORMAT = "dd.MM.yyyy";
	public static final String TIME_FORMAT = "HH:mm:ss";
	
	private DateUtil() {}
	
	
	public static String getIvyDateAsString(ch.ivyteam.ivy.scripting.objects.Date date) {
		return date.format(DATE_FORMAT);
	}
	
	public static String getIvyTimeAsString(ch.ivyteam.ivy.scripting.objects.Time time) {
		return time.format(TIME_FORMAT);
	}
	
	public static String getNewDateAsString() {
		return getIvyDateAsString(new ch.ivyteam.ivy.scripting.objects.Date());
	}
	
	public static String getNewTimeAsString() {
		return getIvyTimeAsString(new ch.ivyteam.ivy.scripting.objects.Time());
	}
	
	public static java.sql.Date getSqlDateFromDateAsString(String date) {
		return new java.sql.Date(new ch.ivyteam.ivy.scripting.objects.Date(date).toJavaDate().getTime());
	}
	
	public static java.sql.Time getSqlTimeFromTimeAsString(String time) throws ParseException {
		DateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
		return new java.sql.Time(sdf.parse(time).getTime());
	}
	
	public static ch.ivyteam.ivy.scripting.objects.Time getIvyTimeFromSqlTime(java.sql.Time time) {
		try {
			String timeAsString = time.toString();
			if (timeAsString.length() > 8) {
				timeAsString = timeAsString.substring(0, 8);
			}
			return new ch.ivyteam.ivy.scripting.objects.Time(timeAsString);
		} catch (Exception ex) {
			return new ch.ivyteam.ivy.scripting.objects.Time("00:00:00");
		}
	}
	
	/**
	 * returned the given time String formatted as HH:mm:ss.<br />
	 * Following rules apply:<br>
	 * - the given time is null empty ":". The new current time is returned.<br />
	 * - All the missing or wrong formated parts for the format HH:mm:ss will be replaced by 00.<br />
	 * Example:<br />
	 * <table>
	 * 	<tr><td>INPUT</td><td>OUTPUT</td><tr>
	 * 	<tr><td>null or " "</td><td>Current time HH:mm:ss</td><tr>
	 * 	<tr><td>"18"</td><td>"18:00:00"</td><tr>
	 *  <tr><td>"55:76:33"</td><td>"00:00:33"</td><tr>
	 *  <tr><td>"23:Hello:33"</td><td>"23:00:33"</td><tr>
	 *  <tr><td>"9:6:7"</td><td>"09:06:07"</td><tr>
	 * </table>
	 * @param time the String representation of the time
	 * @return
	 */
	public static String formatTime(String time){
		if(StringUtils.isBlank(time)){
			return new ch.ivyteam.ivy.scripting.objects.Time().format("HH:mm:ss");
		}
		String [] ts = time.split(":");
		for(int i= 0;i<ts.length;i++){
			try{
				int number = Integer.parseInt(ts[i]);
				if(number < 0 || (i == 0 && number> 25) || (number > 60)) {
					ts[i] = "00";
				}
			} catch (NumberFormatException ex) {
				ts[i] = "00";
			}
			
			ts[i]=ts[i].trim().length()==0?"00":(ts[i].trim().length()==1?"0"+ts[i]:ts[i].trim().substring(0, 2));
		}
		if(ts.length==1) {
			return ts[0]+":00:00";
		}
		if(ts.length==2) {
			return ts[0]+":"+ts[1]+":00";
		}
		return ts[0]+":"+ts[1]+":"+ts[2];
	}
}
