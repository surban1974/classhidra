package it.classhidra.plugin.async;


import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.classhidra.core.controller.bsContext;
import it.classhidra.core.controller.iContext;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.info_async;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.stubs.iStub;



public class AsyncContainer extends bsContext implements Runnable, iContext{ 

	private static final long serialVersionUID = 1L;
	private AsyncContext asyncContext;
	protected info_async iAsync;
	protected boolean beCompleted=false;
	protected boolean asyncDispatched=false;
	protected boolean firstTime=true;

	
	public AsyncContainer(){
		super();
	}
	
	public void run() {
		try {
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
		} catch (Exception e) {
			new bsControllerException(e, iStub.log_ERROR);
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
			}finally{

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

			}else
				beCompleted=true;
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


	public HttpServletRequest getRequest() {
		try{
			if(this.asyncContext!=null)
				return (HttpServletRequest)this.asyncContext.getRequest();
			else return null;
		}catch(Exception e){
			return null;
		}
	}


	public HttpServletResponse getResponse() {
		try{
			if(this.asyncContext!=null)
				return (HttpServletResponse)this.asyncContext.getResponse();
			else 
				return null;
		}catch(Exception e){
			return null;
		}
	}

	public void setResponse(HttpServletResponse response) {
	}


	public void flushBuffer() throws Exception {
		if(this.asyncContext!=null)
			this.asyncContext.getResponse().flushBuffer();		
	}


	public void flush() throws Exception {
		if(this.asyncContext!=null)
			this.asyncContext.getResponse().flushBuffer();	}

	@Override
	public void write(byte[] bytes) throws Exception {
		if(this.asyncContext!=null)
			this.asyncContext.getResponse().getOutputStream().write(bytes);
		
	}


}
