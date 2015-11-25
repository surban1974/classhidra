package it.classhidra.scheduler.servlets; 


import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.scheduler.scheduling.init.batch_init;
import it.classhidra.scheduler.scheduling.process.ProcessBatchEngine;
import it.classhidra.scheduler.scheduling.thread.schedulingThreadProcess;



public class servletBatchScheduling extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static schedulingThreadProcess thProcess=null;   
	private static batch_init configuration;
	private static final ProcessBatchEngine pbe = new ProcessBatchEngine();
	private static boolean active = false;




	public void init(ServletConfig config) throws ServletException {
		start();
	}
	
	public static void start(){

		configuration=new batch_init();
		
		try{
			if(configuration.get_active().toLowerCase().trim().equals("true")){
				if(thProcess!=null){
					clearContainer();
					thProcess.setThreadDone(true);
					thProcess.interrupt();
					thProcess=null;
					
				}
				thProcess = new schedulingThreadProcess();
				thProcess.start();
				new bsException("Scheduler:Start schedulingThreadProcess");
				active = true;
			}
		}catch(Exception e){	
			new bsException("ERROR Scheduler:Start schedulingThreadProcess "+e.toString(),iStub.log_ERROR);
		}
	}	

	public static void reScan(){
		reStart();
/*		
		try{
			configuration=new batch_init();
			clearContainer();
			
			thProcess = new schedulingThreadProcess();
			thProcess.start();
			new bsException("Scheduler: Start schedulingThreadProcess");
		}catch(Exception e){	
			e.toString();
		}
*/		
	}
	
	public static void reStart(){
		active = false;
		try{
			configuration=new batch_init();
			clearContainer();
			
			if(thProcess!=null){
				thProcess.setThreadDone(true);
				thProcess.interrupt();
				thProcess=null;
			}

			thProcess = new schedulingThreadProcess();
			thProcess.start();
			new bsException("Scheduler: ReStart schedulingThreadProcess", iStub.log_INFO);
			active = true;
		}catch(Exception e){	
			new bsException("ERROR Scheduler:ReStart schedulingThreadProcess "+e.toString(),iStub.log_ERROR);
		}
	}
	
	public static void stop(){
		try{
			configuration=new batch_init();
			clearContainer();
			if(thProcess!=null){
				thProcess.setThreadDone(true);
				thProcess.interrupt();
				thProcess=null;
			}
			new bsException("Scheduler: Stop schedulingThreadProcess", iStub.log_INFO);
			active = false;
		}catch(Exception e){	
			new bsException("ERROR Scheduler:Stop schedulingThreadProcess "+e.toString(),iStub.log_ERROR);
		}
	}

	public static schedulingThreadProcess getThProcess() {
		return thProcess;
	}


	public static void clearContainer(){
		try{
			if(pbe!=null)
				pbe.interruptThreadEvents();
		}catch(Exception e){
			new bsException("ERROR Scheduler:Clean schedulingThreadProcess "+e.toString(),iStub.log_ERROR);
		}
/*		
		try{

			int initialSize = thProcess.getPbe().getContainer_threadevents().size();
			for(int i=0; i<thProcess.getPbe().getContainer_threadevents().size();i++){
				schedulingThreadEvent ste =  (schedulingThreadEvent)thProcess.getPbe().getContainer_threadevents().get(i);
				if(ste.getStateThread()==0){
					ste.setThreadDone(true);
					ste.setStateThread(2);				
				}
				ste.interrupt();
				if(thProcess.getPbe().getContainer_threadevents().size()<initialSize){
					initialSize = thProcess.getPbe().getContainer_threadevents().size();
					i--;
				}
			}

		}catch (Exception e) {

		}
*/			
		
	}
	


	public static batch_init getConfiguration() {
		if(configuration==null) configuration=new batch_init(); 
		if(configuration.getLoadedFrom().trim().equals("")){
			configuration=new batch_init();
		}
		return configuration;
	}

	public static ProcessBatchEngine getPbe() {
		return pbe;
	}

	public static boolean isActive() {
		return active;
	}

//	public static void setPbe(ProcessBatchEngine pbe) {
//		servletBatchScheduling.pbe = pbe;
//	}
	

}
