package it.classhidra.scheduler.scheduling.thread;

import it.classhidra.scheduler.scheduling.db.db_batch;
import it.classhidra.scheduler.scheduling.process.ProcessBatchEngine;
import it.classhidra.scheduler.scheduling.process.ProcessBatchEvent;

import java.util.Date;







public class schedulingThreadEvent extends Thread implements Runnable{


	private boolean threadDone = false;
	private long delta_time;
	private db_batch batch;
	private int state;
	private Date exec_time;
	private ProcessBatchEngine pbe;






	public schedulingThreadEvent(long _delta_time, db_batch _batch, ProcessBatchEngine _pbe ) {
		super();
		delta_time = _delta_time;
		batch = _batch;
		threadDone=false;
		state=0;
		pbe=_pbe;


	}

	public void run() {
		
			if(pbe!=null) pbe.getContainer_threadevents().add(this);
		try{
			if(delta_time>0){
				threadDone=true;
				exec_time = new Date(new Date().getTime()+delta_time);
				Thread.sleep(delta_time);
			}
			if(state==0){
				state=1;
				ProcessBatchEvent p_ev = new ProcessBatchEvent();
				p_ev.launch(batch.getCd_ist(),batch.getCd_btch());
				state=2;
			}
			try{
				pbe.getContainer_threadevents().remove(this);
			}catch(Exception e){
			}

		} catch (InterruptedException e) {
			state=2;			
			Thread.currentThread().interrupt();
			pbe.getContainer_threadevents().remove(this);
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
}