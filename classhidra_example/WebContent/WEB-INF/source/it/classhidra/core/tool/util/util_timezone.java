package it.classhidra.core.tool.util;

import it.classhidra.core.controller.bsController;

public class util_timezone {
	
	public  static  void updateTimeZone(java.text.SimpleDateFormat sdf, Object resultObject, String timeZoneToShift) {
		bsController.getTimeZoneMapper().updateTimeZone(sdf, resultObject, timeZoneToShift);
	}
	
	public static long calcTimezoneDistance(Object resultObject, String timeZoneShift) {
		return bsController.getTimeZoneMapper().calcTimezoneDistance(resultObject, timeZoneShift);
	}


	public static java.util.Date updateTimezoneServerShift(Object date, String format) {
		return bsController.getTimeZoneMapper().updateTimezoneServerShift(date, format);
	}


}
