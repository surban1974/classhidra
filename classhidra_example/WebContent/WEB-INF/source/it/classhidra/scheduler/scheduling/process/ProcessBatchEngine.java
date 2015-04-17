package it.classhidra.scheduler.scheduling.process;




import it.classhidra.core.tool.db.db_connection;
import it.classhidra.core.tool.exception.bsControllerMessageException;
import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.framework.web.integration.i_module_integration;
import it.classhidra.scheduler.common.i_4Batch;
import it.classhidra.scheduler.scheduling.db.db_batch;
import it.classhidra.scheduler.scheduling.init.batch_init;
import it.classhidra.scheduler.scheduling.thread.schedulingThreadEvent;
import it.classhidra.scheduler.servlets.servletBatchScheduling;
import it.classhidra.scheduler.util.util_batch;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;





public class ProcessBatchEngine  {
	private boolean scan=false;
	private Timestamp nextScanTime;
	private ArrayList container_threadevents;


	public 	ProcessBatchEngine(){
		super();
		container_threadevents=new ArrayList();
	}


	public void launch(){
		scan=true;

		batch_init binit = servletBatchScheduling.getConfiguration();

		new bsException("Scheduler: BatchEngine:launch:scan", iStub.log_DEBUG);

		try{
			String currentTime = util_format.dataToString(new java.util.Date(), "yyyy-MM-dd-HH-mm");
			String periodTime = binit.get_scan();
			nextScanTime = null;
			long delta = 0;

				if(	periodTime.equals("****-**-**-**-**") || periodTime.equals("")	) {
					delta = binit.getLsleep();
					nextScanTime = new java.sql.Timestamp(util_format.stringToData(currentTime, "yyyy-MM-dd-HH-mm").getTime()+delta);
				}else{
					String newTime=util_batch.calcolatePeriod(currentTime, periodTime);
					nextScanTime = new java.sql.Timestamp(
						util_format.stringToData(newTime, "yyyy-MM-dd-HH-mm").getTime()
					);
					delta = nextScanTime.getTime() - new java.util.Date().getTime();
				}

			if(delta>0 && delta <= binit.getLsleep() && nextScanTime!=null){
				scanAndLaunch(nextScanTime.getTime(),currentTime, binit);
			}else{
				scanAndLaunch(0,currentTime,binit);
			}


		}catch(Exception e){
			new bsException("Scheduler: BatchEngine:launch:error: " + e.toString(), iStub.log_ERROR);
		}
		scan=false;
	}

	private void scanAndLaunch(long nextScanTimeL, String currentTime, batch_init binit){
		long currentTimeL = new java.util.Date().getTime();
		try{
			currentTimeL = util_format.stringToData(currentTime, "yyyy-MM-dd-HH-mm").getTime();
		}catch (Exception e) {
		}
		long delta = nextScanTimeL - currentTimeL;


		HashMap h_batchs=new HashMap();

		List elementsAll = new ArrayList();

		i_4Batch m4b = binit.get4BatchManager();
		HashMap form = new HashMap();

		try{
			elementsAll = (List)m4b.operation(i_module_integration.o_FINDFORMLIST, form);
		}catch(Exception e){
			e.toString();
		}


		List elements = new ArrayList();
		for(int i=0;i<elementsAll.size();i++){
			db_batch el = (db_batch)elementsAll.get(i);
			if(el.getState().shortValue()==db_batch.STATE_SCHEDULED){
				if(servletBatchScheduling.getThProcess()!=null){
					schedulingThreadEvent ste = getFromThreadEvents(el);
					if(ste==null){
						el.setState(new Integer(db_batch.STATE_NORMAL));
						elements.add(el);
					}
					else if(ste.getStateThread()==2){
						ste.interrupt();
						el.setState(new Integer(db_batch.STATE_NORMAL));
						elements.add(el);
					}
				}
			}
			if(el.getState().shortValue()==db_batch.STATE_NORMAL){
				elements.add(el);
			}
		}

		List sql_updates=new ArrayList();

		for(int i=0;i<elements.size();i++){
			db_batch el = (db_batch)elements.get(i);
			boolean updated=false;
			try{
				if(util_batch.reCalcNextTime(el,currentTime,0)){
					updated=true;
				}
			}catch(Exception e){
			}
			long deltaTmp = el.getTm_next().getTime() - currentTimeL;
			if(delta>0 && 0 <= deltaTmp && deltaTmp <= delta){
				el.setState(new Integer(db_batch.STATE_SCHEDULED));
				updated=true;
				h_batchs.put(el.getCd_btch(), el);
			}
			if(updated) sql_updates.add(el.sql_Update(el));
		}


		Connection conn=null;
		Statement st=null;

		try{
			conn = new db_connection().getContent();
			st = conn.createStatement();

			for(int i=0;i<sql_updates.size();i++){
				try{
					st.executeUpdate((String)sql_updates.get(i));
				}catch (Exception e) {
					e.toString();
				}
			}


		}catch(Exception ex){
			new bsControllerMessageException(ex);
		}finally{
			db_connection.release(null, st, conn);
		}

		if(h_batchs!=null && h_batchs.size()>0){
			Iterator it = h_batchs.entrySet().iterator();
		    while (it.hasNext()) {
				db_batch el = (db_batch)((Map.Entry)it.next()).getValue();
				long deltaBatch = el.getTm_next().getTime() - new Date().getTime();
				schedulingThreadEvent t_ev_old = util_batch.findFromPbe(this, el.getCd_btch());
				if(t_ev_old!=null){
					t_ev_old.setStateThread(2);
					t_ev_old.setThreadDone(true);
					t_ev_old.interrupt();
					this.getContainer_threadevents().remove(t_ev_old);
				}
				
				schedulingThreadEvent t_ev = new schedulingThreadEvent(
						deltaBatch,
						el,
						this);
				t_ev.start();
			}
		}

	}


	public Timestamp getNextScanTime() {
		return nextScanTime;
	}


	public ArrayList getContainer_threadevents() {
		return container_threadevents;
	}
	
	private schedulingThreadEvent getFromThreadEvents(db_batch el){
		if(el!=null && servletBatchScheduling.getThProcess()!=null){
			for(int i=0;i<servletBatchScheduling.getThProcess().getPbe().getContainer_threadevents().size();i++){
				schedulingThreadEvent ste = (schedulingThreadEvent)servletBatchScheduling.getThProcess().getPbe().getContainer_threadevents().get(i);
				if(ste.getBatch()!=null && ste.getBatch().getCd_btch().equals(el.getCd_btch())) return ste;
			}
		}
		return null;
	}


}
