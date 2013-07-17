package it.classhidra.scheduler.common;


import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.util_reflect;

public class SenderLog {
	public static void write(String mess, String level){
		String prefix = "["+util_format.dataToString(new java.util.Date(), "yyyy-MM-dd HH:mm:ss")+"] ";
		writeLog(prefix+mess,level);
		
	}

	public static void write(String mess){
		
		write(mess,"DEBUG");

	}	
	
	public static void write(Exception ex){
		
		write(ex,"DEBUG");

	}
	
	public static void write(Throwable ex){
		
		write(ex,"DEBUG");

	}	

	public static void write(Exception ex, String level){
		String prefix = "["+util_format.dataToString(new java.util.Date(), "yyyy-MM-dd HH:mm:ss")+"] ";
		String mess="";
		if(ex!=null){
			if(ex.getMessage()!=null) mess=ex.getMessage();
		}else{
			 mess=ex.toString();
		}
		writeLog(prefix+mess,level);

	}


	public static void write(Throwable th, String level){
		String prefix = "["+util_format.dataToString(new java.util.Date(), "yyyy-MM-dd HH:mm:ss")+"] ";
		String mess="";
		if(th!=null){
			if(th.getMessage()!=null) mess=th.getMessage();
		}else{
			 mess=th.toString();
		}
		writeLog(prefix+mess,level);

	}
	
	public static void writeLog(String mess, String level){
		String classInfo=util_reflect.prepareClassInfo(new String[]{"SenderLog.java"},new String[]{"writeLog"});
/*
		Logger log = Logger.getLogger(classInfo);
		
		if(level.toUpperCase().equals("INFO")) log.info(mess);
		if(level.toUpperCase().equals("WARN")) log.warn(mess);
		if(level.toUpperCase().equals("DEBUG")) log.debug(mess);
		if(level.toUpperCase().equals("ERROR")) log.error(mess);
		if(level.toUpperCase().equals("FATAL")) log.fatal(mess);
*/
		
		util_format.writeToConsole(null,"["+level+"]" + mess);
	}	
}
