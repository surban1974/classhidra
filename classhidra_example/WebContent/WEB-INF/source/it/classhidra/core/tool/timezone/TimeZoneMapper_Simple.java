package it.classhidra.core.tool.timezone;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TimeZone;

import it.classhidra.core.controller.bsController;

public class TimeZoneMapper_Simple implements I_TimeZoneMapper {
	public  void updateTimeZone(java.text.SimpleDateFormat sdf, Object resultObject, String timeZoneToShift) {
		if(resultObject==null || timeZoneToShift==null || timeZoneToShift.isEmpty() || !(resultObject instanceof java.util.Date))
			return ;
		
		if(resultObject instanceof java.util.Date) {
			java.util.Date utc = (java.util.Date)resultObject;
			Calendar calendar = Calendar.getInstance();
		    calendar.setTime(utc);
			if(calendar.get(Calendar.HOUR_OF_DAY)==0 && calendar.get(Calendar.MINUTE)==0 && calendar.get(Calendar.SECOND)==0 && calendar.get(Calendar.MILLISECOND)==0)
				return ;
		}

		if(timeZoneToShift!=null && !timeZoneToShift.isEmpty()) {
			TimeZone timeZone = null;
			try {
				timeZone = TimeZone.getTimeZone(timeZoneToShift);
			}catch (Exception e) {
			}
			if(timeZone!=null)
				sdf.setTimeZone(timeZone);
		}
	}
	
	public long calcTimezoneDistance(Object resultObject, String timeZoneShift) {
		long minusInMillis = 0;
		if(resultObject==null || timeZoneShift==null || timeZoneShift.isEmpty() || !(resultObject instanceof java.util.Date))
			return minusInMillis;
		
		if(resultObject instanceof java.util.Date) {
			java.util.Date utc = (java.util.Date)resultObject;
			Calendar calendar = Calendar.getInstance();
		    calendar.setTime(utc);
			if(calendar.get(Calendar.HOUR_OF_DAY)==0 && calendar.get(Calendar.MINUTE)==0 && calendar.get(Calendar.SECOND)==0 && calendar.get(Calendar.MILLISECOND)==0)
				return minusInMillis;
		}
		
		if(timeZoneShift!=null) {
			long shiftInMillis = 0;
			if(	(bsController.getAppInit().get_timezone_db()!=null && !bsController.getAppInit().get_timezone_db().isEmpty()) ||
					(bsController.getAppInit().get_timezone_vm()!=null && !bsController.getAppInit().get_timezone_vm().isEmpty())
				) {	
			
				TimeZone tz_db = TimeZone.getTimeZone("GMT");
				TimeZone tz_vm = TimeZone.getTimeZone("GMT");
				try {
					tz_db = TimeZone.getTimeZone(bsController.getAppInit().get_timezone_db());
				}catch (Exception e) {
				}
				try {
					tz_vm = TimeZone.getTimeZone(bsController.getAppInit().get_timezone_vm());
				}catch (Exception e) {
				}			
				shiftInMillis = tz_db.getRawOffset() - tz_vm.getRawOffset() + tz_db.getDSTSavings() - tz_vm.getDSTSavings();
			}
			try {
				TimeZone tz1 = TimeZone.getTimeZone(Calendar.getInstance().getTimeZone().getID());			
			    TimeZone tz2 = TimeZone.getTimeZone(timeZoneShift);
			    minusInMillis = tz1.getRawOffset() - tz2.getRawOffset() + tz1.getDSTSavings() - tz2.getDSTSavings();
			    if(shiftInMillis!=0)
			    	minusInMillis+=shiftInMillis*1000*60*60;
			}catch (Exception e) {
			}
		}
		return minusInMillis;
	}


	public java.util.Date updateTimezoneServerShift(Object date, String format) {
		if((format.contains("z") || format.contains("Z") || format.contains("X"))) {
			int shiftInMillis = 0;
			if(	(bsController.getAppInit().get_timezone_db()!=null && !bsController.getAppInit().get_timezone_db().isEmpty()) ||
				(bsController.getAppInit().get_timezone_vm()!=null && !bsController.getAppInit().get_timezone_vm().isEmpty())
			) {	
				TimeZone tz_db = TimeZone.getTimeZone("GMT");
				TimeZone tz_vm = TimeZone.getTimeZone("GMT");
				try {
					tz_db = TimeZone.getTimeZone(bsController.getAppInit().get_timezone_db());
				}catch (Exception e) {
				}
				try {
					tz_vm = TimeZone.getTimeZone(bsController.getAppInit().get_timezone_vm());
				}catch (Exception e) {
				}			
				shiftInMillis = tz_db.getRawOffset() - tz_vm.getRawOffset() + tz_db.getDSTSavings() - tz_vm.getDSTSavings();
			}	
			if(shiftInMillis!=0)
				date=addMillisToDate(date, shiftInMillis);
		}

	    if(date instanceof java.util.Date)
	    	return (java.util.Date)date;
	    if(date instanceof java.sql.Date)
	    	return (java.sql.Date)date;  
	    if(date instanceof Timestamp)
	    	return (Timestamp)date;    
		
		return null;
	}

	private java.util.Date addMillisToDate(Object date, int millisecond) {
		
	    if(!
	    		(date instanceof java.util.Date) || (date instanceof java.sql.Date) || (date instanceof Timestamp)
	    )
	    	return null;
		
	    Calendar calendar = Calendar.getInstance();
	    if(date instanceof java.util.Date)
	    	calendar.setTime((java.util.Date)date);
	    if(date instanceof java.sql.Date)
	    	calendar.setTime((java.sql.Date)date);  
	    if(date instanceof Timestamp)
	    	calendar.setTime((Timestamp)date);    
	    calendar.add(Calendar.MILLISECOND, millisecond);
	    
	    return calendar.getTime();
	}

}
