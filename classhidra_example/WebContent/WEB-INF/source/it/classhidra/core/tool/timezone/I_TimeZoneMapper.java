package it.classhidra.core.tool.timezone;

public interface I_TimeZoneMapper {

	void updateTimeZone(java.text.SimpleDateFormat sdf, Object resultObject, String timeZoneToShift);

	long calcTimezoneDistance(Object resultObject, String timeZoneShift);

	java.util.Date updateTimezoneServerShift(Object date, String format);

}