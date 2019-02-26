package it.classhidra.scheduler.scheduling.process;




import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.integration.i_integration;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.scheduler.common.i_4Batch;
import it.classhidra.scheduler.common.i_batch;
import it.classhidra.scheduler.scheduling.DriverScheduling;
import it.classhidra.scheduler.scheduling.db.db_batch;
import it.classhidra.scheduler.scheduling.init.batch_init;
import it.classhidra.scheduler.scheduling.thread.schedulingThreadEvent;
import it.classhidra.scheduler.scheduling.thread.singleThreadEvent;
import it.classhidra.scheduler.util.util_batch;





public class ProcessBatchEngine implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean scan=false;
	private Timestamp nextScanTime;
	private ArrayList<schedulingThreadEvent> container_threadevents;


	public 	ProcessBatchEngine(){
		super();
		container_threadevents=new ArrayList<schedulingThreadEvent>();
	}


	public void launch(){
		scan=true;

		batch_init binit = DriverScheduling.getConfiguration();

		new bsException("Scheduler: BatchEngine:launch:scan", iStub.log_INFO);

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
	
	public boolean addAndLaunch(db_batch el){
		if(el==null)
			return false;

		batch_init binit = DriverScheduling.getConfiguration();

		new bsException("Scheduler: BatchEngine:launch:scan", iStub.log_INFO);

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
				return addAndLaunch(el,nextScanTime.getTime(),currentTime, binit);
			}else{
				return addAndLaunch(el,0,currentTime,binit);
			}


		}catch(Exception e){
			new bsException("Scheduler: BatchEngine:launch:error: " + e.toString(), iStub.log_ERROR);
		}

		return true;
	}
	
	public boolean updateAndLaunch(db_batch el){
		if(el==null)
			return false;

		batch_init binit = DriverScheduling.getConfiguration();

		new bsException("Scheduler: BatchEngine:launch:scan", iStub.log_INFO);

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
				return updateAndLaunch(el,nextScanTime.getTime(),currentTime, binit);
			}else{
				return updateAndLaunch(el,0,currentTime,binit);
			}


		}catch(Exception e){
			new bsException("Scheduler: BatchEngine:launch:error: " + e.toString(), iStub.log_ERROR);
		}

		return true;
	}	
	
	public boolean stopAndRemove(db_batch el, boolean remove){
		if(el==null)
			return false;
		
		batch_init binit = DriverScheduling.getConfiguration();
		
		
		try{
			new bsException("Scheduler: BatchEngine:stoping "+el.getCd_btch(), iStub.log_INFO);
			if(el.getState().shortValue()==i_batch.STATE_SCHEDULED){
				if(DriverScheduling.getThProcess()!=null){
					schedulingThreadEvent ste = getFromThreadEvents(el);
					if(ste!=null){
						ste.interrupt();
						el.setState(new Integer(i_batch.STATE_NORMAL));
					}
				}
			}

			schedulingThreadEvent t_ev_old = util_batch.findFromPbe(this, el.getCd_btch());
			if(t_ev_old!=null){
				t_ev_old.setStateThread(2);
				t_ev_old.setThreadDone(true);
				t_ev_old.interrupt();
				this.getContainer_threadevents().remove(t_ev_old);
			}
			
			if(remove){
				new bsException("Scheduler: BatchEngine:removing "+el.getCd_btch(), iStub.log_INFO);
				i_4Batch m4b = binit.get4BatchManager();
				HashMap<String,Object> form = new HashMap<String, Object>();
		
	
				form.put("cd_ist",el.getCd_ist());
				form.put("cd_btch",el.getCd_btch());
				try{
					Boolean res = (Boolean)m4b.operation(i_integration.o_DELETE, form);
					if(res==false)
						return false;
				}catch(Exception ex){
					return false;	
				}
			}

	
			

			
		}catch(Exception e){
			new bsException("Scheduler: BatchEngine:remove "+((el!=null)?el.getCd_btch():"null")+":error: " + e.toString(), iStub.log_ERROR);
			return false;
		}
		return true;
		
	}
	
	public boolean launchAsSingleIfIdle(db_batch el, String common_area){
		if(el==null)
			return false;
		
		batch_init binit = DriverScheduling.getConfiguration();
		
		i_4Batch m4b = binit.get4BatchManager();
		HashMap<String,Object> form = new HashMap<String, Object>();		
		try{
			new bsException("Scheduler: BatchEngine:launchSingle "+el.getCd_btch(), iStub.log_INFO);
			form.put("cd_ist",el.getCd_ist());
			form.put("cd_btch",el.getCd_btch());
			db_batch founded = (db_batch)m4b.operation(i_integration.o_FIND, form);
			if(founded==null || founded.getState().shortValue()==i_batch.STATE_INEXEC)
				return false;

			singleThreadEvent t_ev = new singleThreadEvent()
					.setDelta_time(0)
					.setBatch(founded)
					.setCommon_area(common_area);
			t_ev.start();
			
		}catch(Exception e){
			new bsException("Scheduler: BatchEngine:launchSingle "+((el!=null)?el.getCd_btch():"null")+":error: " + e.toString(), iStub.log_ERROR);
			return false;
		}
		return true;
		
	}	

	private void scanAndLaunch(long nextScanTimeL, String currentTime, batch_init binit){
		long currentTimeL = new java.util.Date().getTime();
		try{
			currentTimeL = util_format.stringToData(currentTime, "yyyy-MM-dd-HH-mm").getTime();
		}catch (Exception e) {
		}
		long delta = nextScanTimeL - currentTimeL;


		HashMap<String,db_batch> h_batchs=new HashMap<String, db_batch>();

		List<db_batch> elementsAll = new ArrayList<db_batch>();

		i_4Batch m4b = binit.get4BatchManager();
		HashMap<String,Object> form = new HashMap<String, Object>();

		try{
			@SuppressWarnings("unchecked")
			final List<db_batch> operation = 
					 (List<db_batch>)m4b.operation(i_integration.o_FINDFORMLIST,form);
			elementsAll = operation;
		}catch(Exception e){
			e.toString();
		}


		List<db_batch> elements = new ArrayList<db_batch>();
		for(int i=0;i<elementsAll.size();i++){
			db_batch el = (db_batch)elementsAll.get(i);
			if(el.getState().shortValue()==i_batch.STATE_SCHEDULED){
				if(DriverScheduling.getThProcess()!=null){
					schedulingThreadEvent ste = getFromThreadEvents(el);
					if(ste==null){
						el.setState(new Integer(i_batch.STATE_NORMAL));
						elements.add(el);
					}
					else if(ste.getStateThread()==2){
						ste.interrupt();
						el.setState(new Integer(i_batch.STATE_NORMAL));
						elements.add(el);
					}
				}
			}
			if(el.getState().shortValue()==i_batch.STATE_NORMAL){
				elements.add(el);
			}
		}


		List<db_batch> batch_updated=new ArrayList<db_batch>();

		for(int i=0;i<elements.size();i++){
			db_batch el = (db_batch)elements.get(i);
			boolean updated=false;
			try{
				Date rec = new java.util.Date();
				long recT = rec.getTime()+60*1000;
				if(util_batch.reCalcNextTime(el, util_format.dataToString(new java.util.Date(recT), "yyyy-MM-dd-HH-mm"),60*1000, util_batch.CONST_APPLICANT_SCANNER)){
					updated=true;
				}
					
//				if(util_batch.reCalcNextTime(el,currentTime,0)){
//					updated=true;
//				}
			}catch(Exception e){
			}
			long deltaTmp = el.getTm_next().getTime() - currentTimeL;
			if(delta>0 && 0 <= deltaTmp && deltaTmp <= delta){
				el.setState(new Integer(i_batch.STATE_SCHEDULED));
				updated=true;
				h_batchs.put(el.getCd_btch(), el);
			}
			if(updated){
				if(
						(el.getPeriod()!=null && !el.getPeriod().equals("")) ||
						(el.getCd_p_btch()!=null && !el.getCd_p_btch().equals("")) ||
						(el.getCls_btch()!=null && !el.getCls_btch().equals(""))
					)
					batch_updated.add(el);
//				sql_updates.add(el.sql_Update(el));
			}
		}
		
	
		form = new HashMap<String,Object>();
		form.put("list",batch_updated);

		try{
			binit.get4BatchManager().operation(i_4Batch.o_UPDATE_STATES_AND_NEXTEXEC, form);
		}catch(Exception e){
			new bsException(e,iStub.log_ERROR);
		}		

		
		if(h_batchs!=null && h_batchs.size()>0){
			Iterator<Map.Entry<String, db_batch>> it = h_batchs.entrySet().iterator();
		    while (it.hasNext()) {
				db_batch el = (db_batch)((Map.Entry<String, db_batch>)it.next()).getValue();
				schedulingThreadEvent t_ev_old = util_batch.findFromPbe(this, el.getCd_btch());
				if(t_ev_old!=null){
					t_ev_old.setStateThread(2);
					t_ev_old.setThreadDone(true);
					t_ev_old.interrupt();
					this.getContainer_threadevents().remove(t_ev_old);
				}
				schedulingThreadEvent t_ev = new schedulingThreadEvent(el,this);
				t_ev.start();
			}
		}

	}
	
	private boolean addAndLaunch(db_batch el, long nextScanTimeL, String currentTime, batch_init binit){
		long currentTimeL = new java.util.Date().getTime();
		try{
			currentTimeL = util_format.stringToData(currentTime, "yyyy-MM-dd-HH-mm").getTime();
		}catch (Exception e) {
		}
		long delta = nextScanTimeL - currentTimeL;

		HashMap<String, db_batch> h_batchs=new HashMap<String, db_batch>();
		
		i_4Batch m4b = binit.get4BatchManager();
		HashMap<String,Object> form = new HashMap<String, Object>();

		try{
			form.put("cd_ist",el.getCd_ist());
			form.put("cd_btch",el.getCd_btch());
			if(m4b.operation(i_integration.o_FIND, form)==null){
				form.clear();
				form.put("inserted",el);
				try{
					Boolean res = (Boolean)m4b.operation(i_integration.o_INSERT, form);
					if(res==false)
						return false;
				}catch(Exception ex){
					
				}
			}
				
		}catch(Exception e){
			e.toString();
		}


		db_batch el_go = null;
		if(el.getState().shortValue()==i_batch.STATE_SCHEDULED){
			if(DriverScheduling.getThProcess()!=null){
				schedulingThreadEvent ste = getFromThreadEvents(el);
				if(ste==null){
					el.setState(new Integer(i_batch.STATE_NORMAL));
					el_go = el;
				}else{
					ste.interrupt();
					el.setState(new Integer(i_batch.STATE_NORMAL));
					el_go = el;
				}
			}
		}
		if(el.getState().shortValue()==i_batch.STATE_NORMAL){
			el_go = el;
		}
		


		List<db_batch> batch_updated=new ArrayList<db_batch>();

		if(el_go!=null){
			boolean updated=false;
			try{
				Date rec = new java.util.Date();
				long recT = rec.getTime()+60*1000;
				if(util_batch.reCalcNextTime(el_go, util_format.dataToString(new java.util.Date(recT), "yyyy-MM-dd-HH-mm"),60*1000, util_batch.CONST_APPLICANT_SCANNER)){
					updated=true;
				}

			}catch(Exception e){
			}
			long deltaTmp = el_go.getTm_next().getTime() - currentTimeL;
			if(delta>0 && 0 <= deltaTmp && deltaTmp <= delta){
				el_go.setState(new Integer(i_batch.STATE_SCHEDULED));
				updated=true;
				h_batchs.put(el_go.getCd_btch(), el_go);
			}
			if(updated){
				if(
						(el_go.getPeriod()!=null && !el_go.getPeriod().equals("")) ||
						(el_go.getCd_p_btch()!=null && !el_go.getCd_p_btch().equals("")) ||
						(el_go.getCls_btch()!=null && !el_go.getCls_btch().equals(""))
					)
					batch_updated.add(el_go);
			}
		}
		
	
		form.clear();
		form.put("list",batch_updated);

		try{
			binit.get4BatchManager().operation(i_4Batch.o_UPDATE_STATES_AND_NEXTEXEC, form);
		}catch(Exception e){
			new bsException(e,iStub.log_ERROR);
		}		

		
		if(h_batchs!=null && h_batchs.size()>0){
			Iterator<Map.Entry<String,db_batch>> it = h_batchs.entrySet().iterator();
		    while (it.hasNext()) {
				db_batch el_upd = (db_batch)((Map.Entry<String,db_batch>)it.next()).getValue();
				schedulingThreadEvent t_ev_old = util_batch.findFromPbe(this, el_upd.getCd_btch());
				if(t_ev_old!=null){
					t_ev_old.setStateThread(2);
					t_ev_old.setThreadDone(true);
					t_ev_old.interrupt();
					this.getContainer_threadevents().remove(t_ev_old);
				}
				schedulingThreadEvent t_ev = new schedulingThreadEvent(el_upd,this);
				t_ev.start();
				return true;
			}
		}
		return true;
	}	
	
	private boolean updateAndLaunch(db_batch el, long nextScanTimeL, String currentTime, batch_init binit){
		long currentTimeL = new java.util.Date().getTime();
		try{
			currentTimeL = util_format.stringToData(currentTime, "yyyy-MM-dd-HH-mm").getTime();
		}catch (Exception e) {
		}
		long delta = nextScanTimeL - currentTimeL;

		HashMap<String,db_batch> h_batchs=new HashMap<String, db_batch>();
		
		i_4Batch m4b = binit.get4BatchManager();
		HashMap<String,Object> form = new HashMap<String, Object>();

		try{
			form.put("cd_ist",el.getCd_ist());
			form.put("cd_btch",el.getCd_btch());
			if(m4b.operation(i_integration.o_FIND, form)!=null){
				form.clear();
				form.put("selected",el);
				try{
					Boolean res = (Boolean)m4b.operation(i_integration.o_UPDATE, form);
					if(res==false)
						return false;
				}catch(Exception ex){
					
				}
			}
				
		}catch(Exception e){
			e.toString();
		}


		db_batch el_go = null;
		if(el.getState().shortValue()==i_batch.STATE_SCHEDULED){
			if(DriverScheduling.getThProcess()!=null){
				schedulingThreadEvent ste = getFromThreadEvents(el);
				if(ste==null){
					el.setState(new Integer(i_batch.STATE_NORMAL));
					el_go = el;
				}else {
					ste.interrupt();
					el.setState(new Integer(i_batch.STATE_NORMAL));
					el_go = el;
				}
			}
		}
		if(el.getState().shortValue()==i_batch.STATE_NORMAL){
			el_go = el;
		}
		


		List<db_batch> batch_updated=new ArrayList<db_batch>();

		if(el_go!=null){
			boolean updated=false;
			try{
				Date rec = new java.util.Date();
				long recT = rec.getTime()+60*1000;
				if(util_batch.reCalcNextTime(el_go, util_format.dataToString(new java.util.Date(recT), "yyyy-MM-dd-HH-mm"),60*1000, util_batch.CONST_APPLICANT_SCANNER)){
					updated=true;
				}

			}catch(Exception e){
			}
			long deltaTmp = el_go.getTm_next().getTime() - currentTimeL;
			if(delta>0 && 0 <= deltaTmp && deltaTmp <= delta){
				el_go.setState(new Integer(i_batch.STATE_SCHEDULED));
				updated=true;
				h_batchs.put(el_go.getCd_btch(), el_go);
			}
			if(updated){
				if(
						(el_go.getPeriod()!=null && !el_go.getPeriod().equals("")) ||
						(el_go.getCd_p_btch()!=null && !el_go.getCd_p_btch().equals("")) ||
						(el_go.getCls_btch()!=null && !el_go.getCls_btch().equals(""))
					)
					batch_updated.add(el_go);
			}
		}
		
	
		form.clear();
		form.put("list",batch_updated);

		try{
			binit.get4BatchManager().operation(i_4Batch.o_UPDATE_STATES_AND_NEXTEXEC, form);
		}catch(Exception e){
			new bsException(e,iStub.log_ERROR);
		}		

		
		if(h_batchs!=null && h_batchs.size()>0){
			Iterator<Map.Entry<String,db_batch>> it = h_batchs.entrySet().iterator();
		    while (it.hasNext()) {
				db_batch el_upd = (db_batch)((Map.Entry<String,db_batch>)it.next()).getValue();
				schedulingThreadEvent t_ev_old = util_batch.findFromPbe(this, el_upd.getCd_btch());
				if(t_ev_old!=null){
					t_ev_old.setStateThread(2);
					t_ev_old.setThreadDone(true);
					t_ev_old.interrupt();
					this.getContainer_threadevents().remove(t_ev_old);
				}
				schedulingThreadEvent t_ev = new schedulingThreadEvent(el_upd,this);
				t_ev.start();
				return true;
			}
		}
		return true;
	}
	
	public Timestamp getNextScanTime() {
		return nextScanTime;
	}


	public ArrayList<schedulingThreadEvent> getContainer_threadevents() {
		return container_threadevents;
	}
	
	private schedulingThreadEvent getFromThreadEvents(db_batch el){
		if(el!=null && DriverScheduling.getThProcess()!=null){
			for(int i=0;i<DriverScheduling.getThProcess().getPbe().getContainer_threadevents().size();i++){
				schedulingThreadEvent ste = (schedulingThreadEvent)DriverScheduling.getThProcess().getPbe().getContainer_threadevents().get(i);
				if(ste.getBatch()!=null && ste.getBatch().getCd_btch().equals(el.getCd_btch())) return ste;
			}
		}
		return null;
	}
	
	public void interruptThreadEvents(){
		try{

			int initialSize = getContainer_threadevents().size();
			for(int i=0; i<getContainer_threadevents().size();i++){
				schedulingThreadEvent ste =  (schedulingThreadEvent)getContainer_threadevents().get(i);
				if(ste!=null){
					if(ste.getStateThread()==0){
						ste.setThreadDone(true);
						ste.setStateThread(2);				
					}
					try{
						ste.interrupt();
					}catch(Exception ex){					
					}
				}
				if(getContainer_threadevents().size()<initialSize){
					initialSize = getContainer_threadevents().size();
					i--;
				}
			}
			scan = false;
			nextScanTime = null; 


		}catch (Exception e) {

		}	

	}


	public boolean isScan() {
		return scan;
	}


}
