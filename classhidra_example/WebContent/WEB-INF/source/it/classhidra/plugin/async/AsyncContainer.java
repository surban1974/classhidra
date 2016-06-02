package it.classhidra.plugin.async;


import javax.servlet.AsyncContext;

import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.info_async;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.stubs.iStub;



public class AsyncContainer implements Runnable{ 
	
	private AsyncContext asyncContext;
	protected info_async iAsync;
	protected boolean beCompleted=false;
	protected boolean asyncDispatched=false;
	protected boolean firstTime=true;

	
	public AsyncContainer(){
		super();
	}
	
	public void run() {
		long loopEvery = 0;
		try{
			loopEvery = Long.valueOf(iAsync.getLoopEvery());
		}catch(Exception e){
		}
		if(loopEvery==0)
			runSingle();
		else if(loopEvery>0){
			boolean reInitBeenEveryLoop = false;
			try{
				reInitBeenEveryLoop = Boolean.valueOf(iAsync.getReInitBeenEveryLoop());
			}catch(Exception e){
			}
			runLoop(loopEvery, reInitBeenEveryLoop);
		}
	}
	
	private void runSingle(){

		if(asyncContext!=null && !checkRequestCompletedProcessing()){
			
			try{
				AsyncController.service(iAsync.getBsIdAction(), iAsync.getBsIdCall(), iAsync.getBsIdComplete(), this, firstTime);
				firstTime = false;
				if(iAsync.getFlushBuffer()!=null && iAsync.getFlushBuffer().equalsIgnoreCase("true"))
					asyncContext.getResponse().flushBuffer();
				beCompleted=true;
			}catch(Exception e){
				new bsControllerException(e, iStub.log_ERROR);
			}catch(Throwable t){
				new bsControllerException(t, iStub.log_ERROR);
			}
		}
		if(beCompleted && !checkRequestCompletedProcessing()){
			if(asyncContext!=null && !asyncDispatched){
				try{
					asyncContext.complete();
					asyncDispatched = true;
				}catch(Exception e){
					new bsControllerException(e, iStub.log_ERROR);
				}
			}
		}		
		
	}
	
	private void runLoop(long loopEvery, boolean reInitBeenEveryLoop){

		while(!beCompleted){
			if(asyncContext!=null && !checkRequestCompletedProcessing()){
				
				try{
					i_action action_instance = AsyncController.service(iAsync.getBsIdAction(), iAsync.getBsIdCall(), iAsync.getBsIdComplete(), this, (reInitBeenEveryLoop || firstTime));
					firstTime = false;
					if(iAsync.getFlushBuffer()!=null && iAsync.getFlushBuffer().equalsIgnoreCase("true"))
						asyncContext.getResponse().flushBuffer();
					if(action_instance!=null){
						beCompleted = beCompleted || action_instance.get_bean().asBean().isAsyncInterrupt() || asyncDispatched;
					}
					

					if(!beCompleted){
						try {
							Thread.sleep(loopEvery);
						} catch (InterruptedException e) {
							beCompleted=true;
						}
					}else{
						if(asyncContext!=null && !asyncDispatched){
							try{
								asyncContext.complete();
								asyncDispatched=true;
							}catch(Exception e){
								new bsControllerException(e, iStub.log_ERROR);
							}
						}
					}
				}catch(Exception e){
					beCompleted=true;
					new bsControllerException(e, iStub.log_ERROR);
				}catch(Throwable t){
					beCompleted=true;
					new bsControllerException(t, iStub.log_ERROR);
				}

			}
		}
		if(beCompleted && !checkRequestCompletedProcessing()){
			if(asyncContext!=null && !asyncDispatched){
				try{
					asyncContext.complete();
				}catch(Exception e){
					new bsControllerException(e, iStub.log_ERROR);
				}
			}
		}		
	}	
	
	private boolean checkRequestCompletedProcessing(){
		boolean request_completed_processing = false;
		try{
			if(asyncContext.getRequest()==null)
				request_completed_processing=true;
		}catch(Exception e){
			request_completed_processing=true;
		}catch(Throwable t){
			request_completed_processing=true;
		}
		return request_completed_processing;
	}


	public AsyncContext getAsyncContext() {
		return asyncContext;
	}

	public AsyncContainer setAsyncContext(AsyncContext asyncContext) {
		beCompleted = false;
		this.asyncContext = asyncContext;
		return this;
		
	}

	public info_async getInfoAsync() {
		return iAsync;
	}

	public AsyncContainer setInfoAsync(info_async iAsync) {
		this.iAsync = iAsync;
		return this;
	}

	public boolean isAsyncDispatched() {
		return asyncDispatched;
	}

	public void setAsyncDispatched(boolean asyncDispatched) {
		this.asyncDispatched = asyncDispatched;
	}


}
