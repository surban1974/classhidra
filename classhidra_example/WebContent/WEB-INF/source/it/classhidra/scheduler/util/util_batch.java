package it.classhidra.scheduler.util;

import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.scheduler.common.i_4Batch;
import it.classhidra.scheduler.common.i_batch;
import it.classhidra.scheduler.scheduling.DriverScheduling;
import it.classhidra.scheduler.scheduling.db.db_batch;
import it.classhidra.scheduler.scheduling.db.db_batch_log;
import it.classhidra.scheduler.scheduling.init.batch_init;
import it.classhidra.scheduler.scheduling.process.ProcessBatchEngine;
import it.classhidra.scheduler.scheduling.process.ProcessBatchEvent;
import it.classhidra.scheduler.scheduling.thread.schedulingThreadEvent;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;


public class util_batch {

	private final static String CONST_EVERY = "every";
	
	public static HashMap readInput(String xml){
		HashMap result = new HashMap();

		return result;
	}

	public static String writeOutput(HashMap out){
		return "";
	}
	
	public static synchronized db_batch checkBatchAndState(Integer cd_ist, String cd_btch, db_batch_log log) throws Exception{


		db_batch batch = new db_batch();
		batch = new db_batch();
		batch.setCd_ist(cd_ist);
		batch.setCd_btch(cd_btch);
		
		batch_init binit = DriverScheduling.getConfiguration();		
		HashMap form = new HashMap();
		form.put("selected",batch);

		try{
			batch = (db_batch)binit.get4BatchManager().operation(i_4Batch.o_FIND_SIMPLE, form);
		}catch(Exception e){
			new bsException(e,iStub.log_ERROR);
		}
		
//		batch = (db_batch)util_blob.load_db_element(batch, null);
		
		if(	batch==null ||
			batch.getState().shortValue()== i_batch.STATE_INEXEC ||
			batch.getState().shortValue()== i_batch.STATE_SUSPEND){
			if(log!=null){				
				log.setCd_btch(cd_btch);
				log.setSt_exec(new Integer(i_batch.STATE_KO));
				log.setDsc_exec(((log.getDsc_exec()==null)?"":log.getDsc_exec())+"Batch ["+cd_btch+"] is null or in execution.");
				log.setTm_fin(new Timestamp(new Date().getTime()));
			}
			return null;
		}

		log.setCd_btch(batch.getCd_btch());
		batch.setInitialState(batch.getState());

		ProcessBatchEvent.changeState(batch, new Integer(i_batch.STATE_INEXEC));	
		
		return batch;

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

				int int_key_periodTime = Integer.parseInt(key_periodTime);
				int int_key_currentTime = Integer.parseInt(key_currentTime);
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
		return calcolatePeriod(currentTime, periodTime, 0, null);
	}
	
	public static String calcolatePeriod(String currentTime, String periodTime, long increased, db_batch batch){

//currentTime = "2011-12-11-09-51";
		if(periodTime.toLowerCase().indexOf(CONST_EVERY)==0){
			long addmillis = -1;
			StringTokenizer st=new StringTokenizer(periodTime);
			if(st.hasMoreTokens()){
				if(st.nextToken().equalsIgnoreCase(CONST_EVERY)){
					if(st.hasMoreTokens()){
						try{
							addmillis = new Long(st.nextToken()).longValue()*1000;
						}catch(Exception e){							
						}
						if(addmillis>-1){
							if(st.hasMoreTokens()){
								switch (st.nextToken().trim().charAt(0)) {
								case 's':									
									break;
								case 'm':	
									addmillis=addmillis*60;
									break;
								case 'h':	
									addmillis=addmillis*60*60;
									break;	
								case 'd':	
									addmillis=addmillis*60*60*24;
									break;	
								case 'w':	
									addmillis=addmillis*60*60*24*7;
									break;							
								default:
									break;
								}
							}
						}
					}
				}
			}
			String newTime=currentTime;
			if(addmillis>-1){
				try{
					long deltaWithlastExec = 0;
					addmillis = addmillis-increased;
					if(batch!=null && batch.getTm_last()!=null){
						deltaWithlastExec = util_format.stringToData(newTime, "yyyy-MM-dd-HH-mm").getTime()-batch.getTm_last().getTime();
						if(deltaWithlastExec>0 && deltaWithlastExec<addmillis)
							addmillis = addmillis - deltaWithlastExec;
					}
					newTime = util_format.dataToString(new Date(util_format.stringToData(newTime, "yyyy-MM-dd-HH-mm").getTime()+addmillis), "yyyy-MM-dd-HH-mm");
				}catch(Exception e){					
				}
			}
			return newTime;
		}else{

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
						int int_key_periodTime = Integer.parseInt(key_periodTime);
						int int_key_currentTime = Integer.parseInt(key_currentTime);
	
						pr.fixedTime[current]=int_key_periodTime;
	
						if(int_key_currentTime>int_key_periodTime){
							pr.setIncrement(current, int_key_periodTime,currentTime);
							increment=true;
						}
						else pr.nextTime[current]=int_key_periodTime;
					}catch(Exception e){
						pr.setDefault(current,Integer.parseInt(key_currentTime),newTime);
					}
					current++;
				}
				newTime = pr.toNewTime();
// New 20151124				
				try{
					Date prev = util_format.stringToData(newTime, "yyyy-MM-dd-HH-mm");
					Date curr = util_format.stringToData(currentTime, "yyyy-MM-dd-HH-mm");
					if(prev.getTime()>curr.getTime())
						return newTime;
				}catch(Exception e){
				}
//				
				maxcycle++;
			}
	
			return newTime;
		}
	}

	public static boolean reCalcNextTime_(db_batch el, String currentTime) throws Exception{
		return reCalcNextTime(el, currentTime, 0);
	}

	public static boolean reCalcNextTime(db_batch el, String currentTime, long increased) throws Exception{

		String newNextTime=calcolatePeriod(currentTime, el.getPeriod().trim(),increased, el);
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
	
	public static schedulingThreadEvent findFromPbe(ProcessBatchEngine pbe, String cd_code){
		if(pbe!=null){
			for(int i=0;i<pbe.getContainer_threadevents().size();i++){
				schedulingThreadEvent current = (schedulingThreadEvent)pbe.getContainer_threadevents().get(i);
				if(current!=null && current.getBatch()!=null && current.getBatch().getCd_btch().equalsIgnoreCase(cd_code))
					return current;
			}
		}
		return null;
	}
	
	public static batch_init getBatch_init(){
		batch_init b_init=null;
	    try{
	    	b_init = DriverScheduling.getConfiguration();
	    	if(b_init==null)
	    		b_init = new batch_init();
	    }catch(Exception e){
	    	b_init = new batch_init();
	    }
	    return b_init;
	}
	


}
