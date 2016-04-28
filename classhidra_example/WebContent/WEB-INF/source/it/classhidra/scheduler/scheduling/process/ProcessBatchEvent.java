package it.classhidra.scheduler.scheduling.process;





import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.integration.i_integration;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.scheduler.common.generic_batch;
import it.classhidra.scheduler.common.i_4Batch;
import it.classhidra.scheduler.common.i_batch;
import it.classhidra.scheduler.scheduling.DriverScheduling;
import it.classhidra.scheduler.scheduling.db.db_batch;
import it.classhidra.scheduler.scheduling.db.db_batch_log;
import it.classhidra.scheduler.scheduling.init.batch_init;
import it.classhidra.scheduler.scheduling.thread.schedulingThreadEvent;
import it.classhidra.scheduler.util.util_batch;

public class ProcessBatchEvent  {

	public ProcessBatchEvent(){
		super();
	}

	public void launch(Integer cd_ist, String cd_btch, boolean recalc, boolean sequence){
		try {
			executeBatch(cd_ist,cd_btch, "",recalc,sequence,recalc);
		}catch(Exception ex){
			new bsException("Scheduler: "+ex.toString(),iStub.log_ERROR);
		}catch(Throwable th){
			new bsException("Scheduler: "+th.toString(),iStub.log_ERROR);
		}
	}
	
	public void launch(Integer cd_ist, String cd_btch){
		try {
			executeBatch(cd_ist,cd_btch, "",true,false,true);
		}catch(Exception ex){
			new bsException("Scheduler: "+ex.toString(),iStub.log_ERROR);
		}catch(Throwable th){
			new bsException("Scheduler: "+th.toString(),iStub.log_ERROR);
		}
	}
	
	public String[] launchR(Integer cd_ist, String cd_btch, boolean recalc, boolean sequence){
		String[] result_eb = null;
		try {
			result_eb = executeBatch(cd_ist,cd_btch, "",recalc,sequence,recalc);
		}catch(Exception ex){
			new bsException("Scheduler: "+ex.toString(),iStub.log_ERROR);
		}catch(Throwable th){
			new bsException("Scheduler: "+th.toString(),iStub.log_ERROR);
		}
		return result_eb;
	}
	
	public String[] launchR(Integer cd_ist, String cd_btch){
		String[] result_eb = null;
		try {
			result_eb = executeBatch(cd_ist,cd_btch, "",true,false,true);
		}catch(Exception ex){
			new bsException("Scheduler: "+ex.toString(),iStub.log_ERROR);
		}catch(Throwable th){
			new bsException("Scheduler: "+th.toString(),iStub.log_ERROR);
		}
		return result_eb;
	}	
	
	public String[] single(db_batch batch, db_batch_log log, i_batch worker){
		String[] result_eb = null;
		try {
			result_eb = executeSingle(batch,log, worker, "");
		}catch(Exception ex){
			new bsException("Scheduler: "+ex.toString(),iStub.log_ERROR);
		}catch(Throwable th){
			new bsException("Scheduler: "+th.toString(),iStub.log_ERROR);
		}
		return result_eb;
	}		


	private String[] executeBatch(Integer cd_ist, String cd_btch, String common_area, boolean recalc, boolean sequence, boolean parent_recalc){
		
		batch_init binit = DriverScheduling.getConfiguration();
		String[] result_eb = new String[]{common_area,"1"};
		if(cd_ist==null || cd_btch==null)
			return result_eb;

		db_batch batch = null;

		db_batch_log log = new db_batch_log();
		log.setTm_start(new Timestamp(new Date().getTime()));

		i_batch currentBatchClass = null;
		try {
			Integer initialBatchState = Integer.valueOf(0); 
			batch = util_batch.checkBatchAndState(cd_ist, cd_btch, log);
			if(batch==null)
				return result_eb;
			else
				initialBatchState = batch.getInitialState();
			
			if(!recalc && batch.getTm_next()!=null){
				Calendar nextTime = Calendar.getInstance();
				nextTime.set(Calendar.SECOND,0);
				nextTime.set(Calendar.MILLISECOND,0);
				long deltaPrecision = nextTime.getTimeInMillis() - new Date().getTime();
				if(deltaPrecision>0 && deltaPrecision<60*1000){
					java.util.concurrent.TimeUnit.MILLISECONDS.sleep(deltaPrecision);
				}
			}
			log.setTm_start(new Timestamp(new Date().getTime()));
			

			String output = "";
			SortedMap h_common_area = new TreeMap();
			try{
				
				if(batch.getCls_btch()!=null && !batch.getCls_btch().equals("")){
					currentBatchClass = DriverScheduling.getBatchInstance(batch.getCd_btch(), batch.getCls_btch());
					if(currentBatchClass!=null)
						currentBatchClass.setDb(batch);
				}
				
				if(recalc || parent_recalc)
					checkExcluded(batch);


				
				if(currentBatchClass!=null){
					currentBatchClass.onBeforeReadInput(common_area);
					currentBatchClass.readInput(common_area);
					currentBatchClass.onAfterReadInput(common_area);
					try{
						currentBatchClass.onBeforeExecute();
						currentBatchClass.execute();
						currentBatchClass.onAfterExecute();

						log.setWriteLog(currentBatchClass.isWriteLog());
						h_common_area.putAll(currentBatchClass.getInput());
						h_common_area.putAll(currentBatchClass.getOutput());
						common_area = generic_batch.map2xml(h_common_area);
						
						currentBatchClass.onBeforeWriteOutput();
						output = currentBatchClass.writeOutput();
						currentBatchClass.onAfterWriteOutput();
						
						
						log.setDsc_exec(((log.getDsc_exec()==null)?"":log.getDsc_exec())+output);
						if(currentBatchClass.getExit().equals("OK")){
							log.setSt_exec(Integer.valueOf(i_batch.STATE_OK));
						} else if(currentBatchClass.getExit().equals("KO")){
							log.setSt_exec(Integer.valueOf(i_batch.STATE_KO));
						} else log.setSt_exec(Integer.valueOf(i_batch.STATE_WARNING));

					}catch (Exception e) {
						log.setDsc_exec(((log.getDsc_exec()==null)?"":log.getDsc_exec())+e.toString());
						log.setSt_exec(Integer.valueOf(i_batch.STATE_KO));
						currentBatchClass.onErrorExecute();
					}

					log.setTm_fin(new Timestamp(new Date().getTime()));
					batch.setSt_exec(log.getSt_exec());

				}else{
					List child_batch = new ArrayList();
					
					i_4Batch m4b = binit.get4BatchManager();
					HashMap form = new HashMap();
					form.put("cd_ist", batch.getCd_ist());
					form.put("cd_btch", batch.getCd_btch());
					form.put("state", new Short(i_batch.STATE_NORMAL));
					try{
						child_batch = (List)m4b.operation(i_integration.o_FINDFORMLIST, form);
					}catch(Exception e){
						e.toString();
					}
						
					for(int i=0;i<child_batch.size();i++){
							db_batch current = (db_batch)child_batch.get(i);
							String[] current_eb = executeBatch(current.getCd_ist(), current.getCd_btch(), common_area,false,true,parent_recalc);
							common_area = current_eb[0];
							short current_state = Short.parseShort(current_eb[1]);
							h_common_area.put(current.getOrd()+"_"+current.getCd_btch().trim(), current.getCd_btch().trim()+" -> "+current.getDesSt_exec(new Integer(current_state)));
							
							if( current_state>batch.getSt_exec().shortValue()) 
								batch.setSt_exec(new Integer(current_state));
					}
					log.setDsc_exec(((log.getDsc_exec()==null)?"":log.getDsc_exec()) + generic_batch.map2xml(h_common_area));	

				}
			}catch(Exception e){
				log.setDsc_exec(((log.getDsc_exec()==null)?"":log.getDsc_exec())+e.toString());
				log.setSt_exec(Integer.valueOf(i_batch.STATE_KO));
				batch.setSt_exec(log.getSt_exec());
			}

			
			log.setTm_fin(new Timestamp(new Date().getTime()));
			log.setSt_exec(batch.getSt_exec());

			if(!recalc && !sequence) 
				batch.setState(initialBatchState);
			else 
				batch.setState(Integer.valueOf(i_batch.STATE_NORMAL));
			if(batch.getCd_p_btch()==null || batch.getCd_p_btch().equals("")){

				if(batch.getState().shortValue()==i_batch.STATE_NORMAL && batch.getPeriod()!=null && !batch.getPeriod().equals("")){
					try{
						if(recalc){
							Date rec = new java.util.Date();
							long currentTimeL=rec.getTime();
							long recT = rec.getTime()+60*1000;
							util_batch.reCalcNextTime(batch, util_format.dataToString(new java.util.Date(recT), "yyyy-MM-dd-HH-mm"),60*1000);
						
							if(log!=null && log.getTm_fin()!=null){
								long delta2 = batch.getTm_next().getTime() - log.getTm_fin().getTime();
								if(delta2>0 && delta2<60*1000){
									rec = new java.util.Date();
									currentTimeL=rec.getTime();
									recT = rec.getTime()+60*1000+delta2+10;
									util_batch.reCalcNextTime(batch, util_format.dataToString(new java.util.Date(recT), "yyyy-MM-dd-HH-mm"),60*1000+delta2+10);
								}
							}else if(batch!=null && batch.getTm_last()!=null){
								long delta2 = batch.getTm_next().getTime() - log.getTm_fin().getTime();
								if(delta2>0 && delta2<60*1000){
									rec = new java.util.Date();
									currentTimeL=rec.getTime();
									recT = rec.getTime()+60*1000+delta2+10;
									util_batch.reCalcNextTime(batch, util_format.dataToString(new java.util.Date(recT), "yyyy-MM-dd-HH-mm"),60*1000+delta2+10);
								}
							}
							
							long nextScanTimeL=DriverScheduling.getThProcess().getPbe().getNextScanTime().getTime();
							long delta = nextScanTimeL - currentTimeL;
							long deltaTmp = batch.getTm_next().getTime() - new Date().getTime();
							if(delta>0 && 0 <= deltaTmp && deltaTmp <= delta){
								batch.setState(Integer.valueOf(i_batch.STATE_SCHEDULED));
	
								try{
									schedulingThreadEvent t_ev_old = util_batch.findFromPbe(DriverScheduling.getThProcess().getPbe(), batch.getCd_btch());
									if(t_ev_old!=null){
										t_ev_old.setStateThread(2);
										t_ev_old.setThreadDone(true);
										t_ev_old.interrupt();
										DriverScheduling.getThProcess().getPbe().getContainer_threadevents().remove(t_ev_old);
									}
								}catch(Exception e){
								}
								schedulingThreadEvent t_ev = new schedulingThreadEvent(batch,DriverScheduling.getThProcess().getPbe());								
								t_ev.start();
							}else{
								new bsException("BatchEngine:reScheduling after exec->false: batch->"+batch.getCd_btch()+": delta->"+delta+": deltaTmp->"+deltaTmp, iStub.log_WARN);
							}
						}

					}catch (Exception e) {
					}			
				}else{
					if(batch.getState().shortValue()==i_batch.STATE_NORMAL)
						new bsException("BatchEngine:reScheduling after exec->false: batch->"+batch.getCd_btch()+": state->"+batch.getSt_exec().shortValue()+": period->"+batch.getPeriod(), iStub.log_WARN);
				}
			}
			
			
		}catch(Exception ex){
			log.setDsc_exec(((log.getDsc_exec()==null)?"":log.getDsc_exec())+ex.toString());
			log.setSt_exec(Integer.valueOf(i_batch.STATE_KO));
			batch.setSt_exec(log.getSt_exec());

		}catch(Throwable th){
			log.setDsc_exec(((log.getDsc_exec()==null)?"":log.getDsc_exec())+th.toString());
			log.setSt_exec(Integer.valueOf(i_batch.STATE_KO));
			batch.setSt_exec(log.getSt_exec());
		}finally{

			if(log.isWriteLog()) writeLog(log);
			changeState(batch, batch.getState());
		}
		new bsException("Scheduler: BatchEvent:exec for:["+batch.getCd_btch().trim()+ ((currentBatchClass==null)?" ":" "+currentBatchClass.getVersion()) +"]->"+batch.getDesSt_exec(), iStub.log_INFO);
		result_eb[0]=common_area;
		result_eb[1]=batch.getSt_exec().toString();
		return result_eb;
	}
	
	
	private String[] executeSingle(db_batch batch, db_batch_log log, i_batch worker, String common_area){
		
		batch_init binit = DriverScheduling.getConfiguration();
		String[] result_eb = new String[]{common_area,"1"};
		if(batch==null || log==null) 
			return result_eb;


		log.setTm_start(new Timestamp(new Date().getTime()));


		try {
			
			
			log.setCd_btch(batch.getCd_btch());
			batch.setInitialState(batch.getState());
			batch.setState(Integer.valueOf(i_batch.STATE_INEXEC));	
			Integer initialBatchState = batch.getInitialState();


			log.setTm_start(new Timestamp(new Date().getTime()));
			

			String output = "";
			SortedMap h_common_area = new TreeMap();
			try{
				
				if(worker==null){
					if(batch.getCls_btch()!=null && !batch.getCls_btch().equals("")){
						worker = DriverScheduling.getBatchInstance(batch.getCd_btch(), batch.getCls_btch());
						if(worker!=null)
							worker.setDb(batch);
					}
				}else
					worker.setDb(batch);
				
				
				if(worker!=null){
					worker.onBeforeReadInput(common_area);
					worker.readInput(common_area);
					worker.onAfterReadInput(common_area);
					try{
						worker.onBeforeExecute();
						worker.execute();
						worker.onAfterExecute();

						log.setWriteLog(worker.isWriteLog());
						h_common_area.putAll(worker.getInput());
						h_common_area.putAll(worker.getOutput());
						common_area = generic_batch.map2xml(h_common_area);
						
						worker.onBeforeWriteOutput();
						output = worker.writeOutput();
						worker.onAfterWriteOutput();
						
						
						log.setDsc_exec(((log.getDsc_exec()==null)?"":log.getDsc_exec())+output);
						if(worker.getExit().equals("OK")){
							log.setSt_exec(Integer.valueOf(i_batch.STATE_OK));
						} else if(worker.getExit().equals("KO")){
							log.setSt_exec(Integer.valueOf(i_batch.STATE_KO));
						} else log.setSt_exec(Integer.valueOf(i_batch.STATE_WARNING));

					}catch (Exception e) {
						log.setDsc_exec(((log.getDsc_exec()==null)?"":log.getDsc_exec())+e.toString());
						log.setSt_exec(Integer.valueOf(i_batch.STATE_KO));
						worker.onErrorExecute();
					}

					log.setTm_fin(new Timestamp(new Date().getTime()));
					batch.setSt_exec(log.getSt_exec());

				}
			}catch(Exception e){
				log.setDsc_exec(((log.getDsc_exec()==null)?"":log.getDsc_exec())+e.toString());
				log.setSt_exec(Integer.valueOf(i_batch.STATE_KO));
				batch.setSt_exec(log.getSt_exec());
			}

			
			log.setTm_fin(new Timestamp(new Date().getTime()));
			log.setSt_exec(batch.getSt_exec());

			batch.setState(initialBatchState);
			
			
		}catch(Exception ex){
			log.setDsc_exec(((log.getDsc_exec()==null)?"":log.getDsc_exec())+ex.toString());
			log.setSt_exec(Integer.valueOf(i_batch.STATE_KO));
			batch.setSt_exec(log.getSt_exec());

		}catch(Throwable th){
			log.setDsc_exec(((log.getDsc_exec()==null)?"":log.getDsc_exec())+th.toString());
			log.setSt_exec(Integer.valueOf(i_batch.STATE_KO));
			batch.setSt_exec(log.getSt_exec());
		}finally{

			if(log.isWriteLog()) writeLog(log);
			changeState(batch, batch.getState());
		}
		new bsException("Scheduler: BatchEvent:exec for:["+batch.getCd_btch().trim()+ ((worker==null)?" ":" "+worker.getVersion()) +"]->"+batch.getDesSt_exec(), iStub.log_INFO);
		result_eb[0]=common_area;
		result_eb[1]=batch.getSt_exec().toString();
		return result_eb;
	}
	


	private void checkExcluded(db_batch batch) throws Exception{
		bsException exReturn = null;
		Properties properties = generic_batch.loadProperty(batch);
		if(properties!=null && properties.size()>0){
			String exclude_dayofweek = properties.getProperty(generic_batch.PROPERTY_EXCLUDE_DAYOFWEEK);
			String exclude_date = properties.getProperty(generic_batch.PROPERTY_EXCLUDE_DATE); 
			if(exclude_dayofweek==null && exclude_date==null)
				return;
			try{

				if(exclude_dayofweek!=null){
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(new Date());
					StringTokenizer sto = new StringTokenizer(exclude_dayofweek, ";");
					while(exReturn==null && sto.hasMoreTokens()){						
						try{
							int exclude_day = Integer.valueOf(sto.nextToken()).intValue();
							if(calendar.get(Calendar.DAY_OF_WEEK) == exclude_day)
								exReturn = new bsException("BLOCK. Processing blocked by exclusion of the week's day = ["+ exclude_day+"] from the days allowed.");							
						}catch(Exception e){							
						}
					}

				}
				if(exclude_date!=null){
					String date = util_format.dataToString(new Date(), "yyyy-MM-dd");
					StringTokenizer sto = new StringTokenizer(exclude_date, ";");
					while(sto.hasMoreTokens()){						
						try{
							String exclude = sto.nextToken();
							if(exReturn==null && exclude.equals(date))
								exReturn = new bsException("BLOCK. Processing blocked for exclusion of data = ["+ exclude+"] from the dates allowed.");	
						}catch(Exception e){							
						}
					}

				}

			}catch(Exception e){
				
			}
			if(exReturn!=null)
				throw exReturn;
		}
	}

	public static void changeState(db_batch batch, Integer state){
		
		batch_init binit = DriverScheduling.getConfiguration();		
		HashMap form = new HashMap();
		form.put("selected",batch);
		form.put("state",state);

		try{
			binit.get4BatchManager().operation(i_4Batch.o_UPDATE_STATE, form);
		}catch(Exception e){
			new bsException(e,iStub.log_ERROR);
		}		
		

	}

	private void writeLog(db_batch_log log){
		if(log==null) return;
		
		batch_init binit = DriverScheduling.getConfiguration();		
		HashMap form = new HashMap();
		form.put("selected",log);

		try{
			binit.get4BatchManager().operation(i_4Batch.o_WRITE_LOG, form);
		}catch(Exception e){
			new bsException(e,iStub.log_ERROR);
		}		

	}
	

}
