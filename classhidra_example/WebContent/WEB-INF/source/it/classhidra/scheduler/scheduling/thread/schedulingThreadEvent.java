package it.classhidra.scheduler.scheduling.thread;

import it.classhidra.core.tool.util.util_cloner;
import it.classhidra.scheduler.common.i_batch;
import it.classhidra.scheduler.scheduling.db.db_batch;
import it.classhidra.scheduler.scheduling.process.ProcessBatchEngine;
import it.classhidra.scheduler.scheduling.process.ProcessBatchEvent;
import it.classhidra.scheduler.util.util_batch;

import java.util.Date;








public class schedulingThreadEvent extends Thread implements Runnable{


	private boolean threadDone = false;
	private long delta_time;
	private db_batch batch;
	private int state;
	private Date exec_time;
	private Date realExec_time;
	private ProcessBatchEngine pbe;






	public schedulingThreadEvent(long _delta_time, db_batch _batch, ProcessBatchEngine _pbe ) {
		super();
		delta_time = _delta_time;
		try{
			batch = (db_batch)util_cloner.clone(_batch);
		}catch(Exception e){
		}
		threadDone=false;
		state=0;
		pbe=_pbe;
	}
	
	public schedulingThreadEvent(db_batch _batch, ProcessBatchEngine _pbe ) {
		super();
		delta_time = -1;
		try{
			batch = (db_batch)util_cloner.clone(_batch);
		}catch(Exception e){
		}
		threadDone=false;
		state=0;
		pbe=_pbe;
	}	

	public void run() {
		
		if(pbe!=null){
			
			schedulingThreadEvent ste_mem =  util_batch.findFromPbe(pbe, batch.getCd_btch());
			if(ste_mem!=null ){
				if(ste_mem.getBatch()!=null && ste_mem.getBatch().getState()==i_batch.STATE_INEXEC){
					state=2;
					threadDone=true;
					Thread.currentThread().interrupt();
					return;
				}
				ste_mem.setStateThread(2);	
				ste_mem.setThreadDone(true);
				ste_mem.interrupt();
			}
			
			pbe.getContainer_threadevents().add(this);
		}
		
		try{
			if(delta_time==-1 && batch!=null && batch.getTm_next()!=null){
				delta_time = batch.getTm_next().getTime()-new Date().getTime();
			}
			if(delta_time>0){
				threadDone=true;
				exec_time = new Date(new Date().getTime()+delta_time);
				Thread.sleep(delta_time);
			}
			if(state==0){
				state=1;
				ProcessBatchEvent p_ev = new ProcessBatchEvent();
				p_ev.launch(batch.getCd_ist(),batch.getCd_btch());
				realExec_time=new Date();
				state=2;
			}

		} catch (InterruptedException e) {
			state=2;			
			Thread.currentThread().interrupt();
		} finally {
			if(pbe!=null){
				schedulingThreadEvent ste_mem =  util_batch.findFromPbe(pbe, batch.getCd_btch());
				if(ste_mem!=null && ste_mem.hashCode()==this.hashCode())
					pbe.getContainer_threadevents().remove(ste_mem);
			}
		}
	}


	
	


	public boolean isThreadDone() {
		return threadDone;
	}



	public int getStateThread() {
		return state;
	}
	public void setStateThread(int _state) {
		state = _state;
	}

	public Date getExec_time() {
		return exec_time;
	}

	public db_batch getBatch() {
		return batch;
	}


	public void interrupt() {
		super.interrupt();
		pbe.getContainer_threadevents().remove(this);
	}

	public void setThreadDone(boolean threadDone) {
		this.threadDone = threadDone;
	}

	public Date getRealExec_time() {
		return realExec_time;
	}
}