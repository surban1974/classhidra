package it.classhidra.scheduler.util;

import it.classhidra.core.tool.util.util_format;
import it.classhidra.scheduler.scheduling.db.db_batch;

import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;


public class util_batch {


	public static HashMap readInput(String xml){
		HashMap result = new HashMap();

		return result;
	}

	public static String writeOutput(HashMap out){
		return "";
	}

	public static String calcolatePeriod_(String currentTime, String periodTime){

		int current=0;
		StringTokenizer st_currentTime = new StringTokenizer(currentTime,"-");
		StringTokenizer st_periodTime = new StringTokenizer(periodTime,"-");

		Vector res=new Vector();
		boolean prevIsMore=false;
		boolean findFirst=false;
		while(st_periodTime.hasMoreTokens()){
			int add_const=0;
			if(current==1) add_const=12;
			if(current==3) add_const=24;
			if(current==4) add_const=60;


			String key_periodTime = st_periodTime.nextToken();
			String key_currentTime = st_currentTime.nextToken();

			try{

				if(current==4){
					String _key_periodTime="";
					for(int i=0;i<key_periodTime.length();i++){
						if(key_periodTime.charAt(i)=='*') _key_periodTime+=key_currentTime.charAt(i);
						else _key_periodTime+=key_periodTime.charAt(i);
					}
					if(key_periodTime.charAt(0)=='*' && key_periodTime.charAt(1)!='*')  add_const=10;

					key_periodTime=_key_periodTime;
				}

				int int_key_periodTime = new Integer(key_periodTime).intValue();
				int int_key_currentTime = new Integer(key_currentTime).intValue();
				findFirst=true;
				if(int_key_currentTime>int_key_periodTime){

					if(current==2){
						if(res.size()>0)
							res.set(res.size()-1,new Integer(((Integer)res.get(res.size()-1)).intValue()+1));
					}else{
						if(!prevIsMore) int_key_periodTime=int_key_periodTime+add_const;
					}
					prevIsMore=false;

				}else{
					if(current==2) prevIsMore=false;
					prevIsMore=true;
				}
				res.add(new Integer(int_key_periodTime));
			}catch(Exception e){

				prevIsMore=false;
				if(findFirst) res.add(new Integer(0));
				else res.add(new Integer(key_currentTime));
			}
			current++;
		}
		String newTime="";
		for(int i=0;i<res.size();i++){
			String prefix="-";
			if(i==0) prefix="";
			newTime+=prefix+res.get(i).toString();
		}
		try{
			newTime = util_format.dataToString(util_format.stringToData(newTime, "yyyy-MM-dd-HH-mm"), "yyyy-MM-dd-HH-mm");
		}catch(Exception e){
		}

		return newTime;
	}



	public static String calcolatePeriod(String currentTime, String periodTime){

//currentTime = "2011-12-11-09-51";

		boolean increment=true;
		int maxcycle=0;

		String newTime=currentTime;
		period pr = new period();
		while(increment && maxcycle<100){
			increment=false;
			int current=0;
			StringTokenizer st_currentTime = new StringTokenizer(newTime,"-");
			StringTokenizer st_periodTime = new StringTokenizer(periodTime,"-");




			while(st_periodTime.hasMoreTokens()){


				String key_periodTime = st_periodTime.nextToken();
				String key_currentTime = st_currentTime.nextToken();



				try{



					int int_key_periodTime = new Integer(key_periodTime).intValue();
					int int_key_currentTime = new Integer(key_currentTime).intValue();

					pr.fixedTime[current]=int_key_periodTime;

					if(int_key_currentTime>int_key_periodTime){
						pr.setIncrement(current, int_key_periodTime,currentTime);
						increment=true;
					}
					else pr.nextTime[current]=int_key_periodTime;
				}catch(Exception e){
					pr.setDefault(current,new Integer(key_currentTime).intValue(),newTime);
				}
				current++;
			}
			newTime = pr.toNewTime();
			maxcycle++;
		}

		return newTime;
	}


	public static boolean reCalcNextTime(db_batch el, String currentTime) throws Exception{

		String newNextTime=calcolatePeriod(currentTime, el.getPeriod().trim());
		newNextTime = util_format.dataToString(util_format.stringToData(newNextTime, "yyyy-MM-dd-HH-mm"), "yyyy-MM-dd-HH-mm");
		String oldNextTime=null;
		if(el.getTm_next()!=null)
			oldNextTime = util_format.dataToString(new Date(el.getTm_next().getTime()), "yyyy-MM-dd-HH-mm");

		if(	el.getTm_next()==null ||
			!newNextTime.equals(oldNextTime)){
			el.setTm_next(
					new java.sql.Timestamp(
							util_format.stringToData(newNextTime, "yyyy-MM-dd-HH-mm").getTime()
					)
				);
			return true;
		}else{
			return false;
		}

	}


}
