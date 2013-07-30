package it.classhidra.scheduler.scheduling.process;





import it.classhidra.core.tool.db.db_connection;
import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_blob;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.framework.web.integration.i_module_integration;
import it.classhidra.scheduler.common.generic_batch;
import it.classhidra.scheduler.common.i_4Batch;
import it.classhidra.scheduler.common.i_batch;

import it.classhidra.scheduler.scheduling.db.db_batch;
import it.classhidra.scheduler.scheduling.db.db_batch_log;
import it.classhidra.scheduler.scheduling.init.batch_init;
import it.classhidra.scheduler.scheduling.thread.schedulingThreadEvent;
import it.classhidra.scheduler.servlets.servletBatchScheduling;
import it.classhidra.scheduler.util.util_batch;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;






public class ProcessBatchEvent  {


	public ProcessBatchEvent(){
		super();

	}

	public void launch(Integer cd_ist, String cd_btch, boolean recalc, boolean sequence){
		try {
			executeBatch(cd_ist,cd_btch, "",recalc,sequence);
		}catch(Exception ex){
			new bsException("Scheduler: "+ex.toString(),iStub.log_ERROR);
		}catch(Throwable th){
			new bsException("Scheduler: "+th.toString(),iStub.log_ERROR);
		}finally{
		}
	}
	
	public void launch(Integer cd_ist, String cd_btch){
		try {
			executeBatch(cd_ist,cd_btch, "",true,false);
		}catch(Exception ex){
			new bsException("Scheduler: "+ex.toString(),iStub.log_ERROR);
		}catch(Throwable th){
			new bsException("Scheduler: "+th.toString(),iStub.log_ERROR);
		}finally{
		}
	}


	private String[] executeBatch(Integer cd_ist, String cd_btch, String common_area, boolean recalc, boolean sequence){
		
		batch_init binit = servletBatchScheduling.getConfiguration();
		String[] result_eb = new String[2];
		result_eb[0]=common_area;
		result_eb[1]="1";
		if(cd_ist==null || cd_btch==null) return result_eb;

		db_batch batch = null;

		db_batch_log log = new db_batch_log();
		log.setTm_start(new Timestamp(new Date().getTime()));

		i_batch currentBatchClass = null;
		boolean isChild=false;
		try {
			batch = new db_batch();

			batch.setCd_ist(cd_ist);
			batch.setCd_btch(cd_btch);
			batch = (db_batch)util_blob.load_db_element(batch, null);
			if(	batch==null ||
				batch.getState().shortValue()== db_batch.STATE_INEXEC ||
				batch.getState().shortValue()== db_batch.STATE_SUSPEND) return result_eb;

			log.setCd_btch(batch.getCd_btch());

			Integer initialBatchState = batch.getState();
			changeState(batch, new Integer(db_batch.STATE_INEXEC));

			String output = "";
			HashMap h_common_area = new HashMap();
			try{
				
				if(batch.getCls_btch()!=null && !batch.getCls_btch().equals(""))
				currentBatchClass = loadBatchsClass(batch);


				
				if(currentBatchClass!=null){
					currentBatchClass.readInput(common_area);

					try{
						currentBatchClass.execute();

						log.setWriteLog(currentBatchClass.isWriteLog());
						h_common_area.putAll(currentBatchClass.getInput());
						h_common_area.putAll(currentBatchClass.getOutput());
						common_area = generic_batch.map2xml(h_common_area);
						output = currentBatchClass.writeOutput();
						log.setDsc_exec(((log.getDsc_exec()==null)?"":log.getDsc_exec())+output);
						if(currentBatchClass.getExit().equals("OK")){
							log.setSt_exec(new Integer(db_batch_log.STATE_OK));
						} else if(currentBatchClass.getExit().equals("KO")){
							log.setSt_exec(new Integer(db_batch_log.STATE_KO));
						} else log.setSt_exec(new Integer(db_batch_log.STATE_WARNING));

					}catch (Exception e) {
						log.setDsc_exec(((log.getDsc_exec()==null)?"":log.getDsc_exec())+e.toString());
						log.setSt_exec(new Integer(db_batch_log.STATE_KO));
					}

					log.setTm_fin(new Timestamp(new Date().getTime()));
					batch.setSt_exec(log.getSt_exec());

				}else{
					Vector child_batch = new Vector();
					
					i_4Batch m4b = binit.get4BatchManager();
					HashMap form = new HashMap();
					form.put("cd_ist", batch.getCd_ist());
					form.put("cd_btch", batch.getCd_btch());
					form.put("state", new Short(db_batch.STATE_NORMAL));
					try{
						child_batch = (Vector)m4b.operation(i_module_integration.o_FINDFORMLIST, form);
					}catch(Exception e){					
					}
						
					for(int i=0;i<child_batch.size();i++){
							isChild=true;
							db_batch current = (db_batch)child_batch.get(i);
							String[] current_eb = executeBatch(current.getCd_ist(), current.getCd_btch(), common_area,false,true);
							common_area = current_eb[0];
							short current_state = new Short(current_eb[1]).shortValue();
							h_common_area.put(current.getCd_btch().trim(), current.getCd_btch().trim()+" -> "+current.getDesSt_exec(new Integer(current_state)));
							
							if( current_state>batch.getSt_exec().shortValue()) batch.setSt_exec(new Integer(current_state));
					}
					log.setDsc_exec(((log.getDsc_exec()==null)?"":log.getDsc_exec()) + generic_batch.map2xml(h_common_area));	

				}
			}catch(Exception e){
				log.setDsc_exec(((log.getDsc_exec()==null)?"":log.getDsc_exec())+e.toString());
				log.setSt_exec(new Integer(db_batch_log.STATE_KO));
				batch.setSt_exec(log.getSt_exec());
			}

			
			log.setTm_fin(new Timestamp(new Date().getTime()));
			log.setSt_exec(batch.getSt_exec());
//			log.setDsc_exec(((log.getDsc_exec()==null)?"":log.getDsc_exec()) + generic_batch.map2xml(h_common_area));

			if(!recalc && !sequence) batch.setState(initialBatchState);
			else batch.setState(new Integer(db_batch.STATE_NORMAL));
			if(batch.getCd_p_btch()==null || batch.getCd_p_btch().equals("")){

				if(batch.getSt_exec().shortValue()==0 && batch.getPeriod()!=null && !batch.getPeriod().equals("")){
					try{
						Date rec = new java.util.Date();
						long currentTimeL=rec.getTime();
						long recT = rec.getTime()+60000;
						util_batch.reCalcNextTime(batch, util_format.dataToString(new java.util.Date(recT), "yyyy-MM-dd-HH-mm"));

						
						if(recalc){
							long nextScanTimeL=servletBatchScheduling.getThProcess().getPbe().getNextScanTime().getTime();
							long delta = nextScanTimeL - currentTimeL;
							long deltaTmp = batch.getTm_next().getTime() - recT;
							if(delta>0 && 0 <= deltaTmp && deltaTmp <= delta){
								batch.setState(new Integer(db_batch.STATE_SCHEDULED));
	
								long deltaBatch = batch.getTm_next().getTime() - new Date().getTime();
								schedulingThreadEvent t_ev = new schedulingThreadEvent(
										deltaBatch,
										batch,
										servletBatchScheduling.getThProcess().getPbe());
								t_ev.start();
							}
						}
					}catch (Exception e) {
					}			
				}
			}
			
			
		}catch(Exception ex){
			log.setDsc_exec(((log.getDsc_exec()==null)?"":log.getDsc_exec())+ex.toString());
			log.setSt_exec(new Integer(db_batch_log.STATE_KO));
			batch.setSt_exec(log.getSt_exec());

		}catch(Throwable th){
			log.setDsc_exec(((log.getDsc_exec()==null)?"":log.getDsc_exec())+th.toString());
			log.setSt_exec(new Integer(db_batch_log.STATE_KO));
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

	private i_batch loadBatchsClass(db_batch batch) throws Exception{
		i_batch objBatch = null;
		objBatch = (i_batch)Class.forName(batch.getCls_btch()).newInstance();
		if(objBatch!=null) objBatch.setDb(batch);
		return objBatch;
	}


	private void changeState(db_batch batch, Integer state){
		Connection conn=null;
		Statement st=null;
		try{

			conn = new db_connection().getContent();
			st = conn.createStatement();

			batch.setState(state);

			st.execute(batch.sql_Update(batch));


		}catch(Exception ex){
			new bsException("Scheduler: "+ex.toString(),iStub.log_ERROR);
		}finally{
			db_connection.release(null, st, conn);
		}

	}

	private void writeLog(db_batch_log log){
		if(log==null) return;

		Connection conn=null;
		Statement st=null;


		try{

			conn = new db_connection().getContent();
			conn.setAutoCommit(false);
			st = conn.createStatement();

			st.executeUpdate(log.sql_Insert());
			conn.commit();
		}catch(Exception ex){
			try{
				conn.rollback();
			}catch(Exception e){
			}
			new bsException("Scheduler: "+ex.toString(),iStub.log_ERROR);
		}finally{
			db_connection.release(null, st, conn);
		}

	}
}
