package it.classhidra.scheduler.scheduling.thread;

import java.util.Date;


import it.classhidra.scheduler.common.i_batch;
import it.classhidra.scheduler.scheduling.db.db_batch;
import it.classhidra.scheduler.scheduling.process.ProcessBatchEvent;
import it.classhidra.scheduler.scheduling.db.db_batch_log;








public class singleThreadEvent extends Thread implements Runnable{


	private boolean threadDone = false;
	private long delta_time;
	private db_batch batch;
	private db_batch_log log;
	private i_batch worker;
	private int state;
	private Date exec_time;
	private Date realExec_time;





	
	public singleThreadEvent(db_batch _batch, db_batch_log _log, i_batch _worker) {
		super();
		delta_time = -1;
		batch = _batch;
		log = _log;
		worker = _worker;
		threadDone=false;
		state=0;

	}	

	public void run() {
		
		
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
				p_ev.single(batch,log,worker);
				realExec_time=new Date();
				state=2;
			}

		} catch (InterruptedException e) {
			state=2;			
			Thread.currentThread().interrupt();
		} finally {
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
	}

	public void setThreadDone(boolean threadDone) {
		this.threadDone = threadDone;
	}

	public Date getRealExec_time() {
		return realExec_time;
	}

	public db_batch_log getLog() {
		return log;
	}

	public i_batch getWorker() {
		return worker;
	}

}