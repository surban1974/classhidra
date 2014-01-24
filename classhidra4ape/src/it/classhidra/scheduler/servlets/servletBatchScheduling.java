package it.classhidra.scheduler.servlets; 


import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.scheduler.scheduling.init.batch_init;
import it.classhidra.scheduler.scheduling.thread.schedulingThreadEvent;
import it.classhidra.scheduler.scheduling.thread.schedulingThreadProcess;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;



public class servletBatchScheduling extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static schedulingThreadProcess thProcess=null;   
	private static batch_init configuration;




	public void init(ServletConfig config) throws ServletException {

		configuration=new batch_init();
		
		try{
			if(configuration.get_active().toLowerCase().trim().equals("true")){
				thProcess = new schedulingThreadProcess();
				thProcess.start();
				new bsException("Scheduler:Start schedulingThreadProcess");
			}
		}catch(Exception e){			
		}
	}

	public static void reScan(){
		try{
			configuration=new batch_init();
			clearContainer();
			
			thProcess = new schedulingThreadProcess();
			thProcess.start();
			new bsException("Scheduler: Start schedulingThreadProcess");
		}catch(Exception e){	
			e.toString();
		}
	}
	
	public static void reStart(){
		try{
			configuration=new batch_init();
			clearContainer();
			
			if(thProcess!=null){
				thProcess.interrupt();
				thProcess=null;
			}

			thProcess = new schedulingThreadProcess();
			thProcess.start();
			new bsException("Scheduler: Start schedulingThreadProcess", iStub.log_INFO);
		}catch(Exception e){	
			e.toString();
		}
	}
	
	public static void stop(){
		try{
			configuration=new batch_init();
			clearContainer();
			if(thProcess!=null){
				thProcess.interrupt();
				thProcess=null;
			}
			new bsException("Scheduler: Stop schedulingThreadProcess", iStub.log_INFO);
		}catch(Exception e){	
			e.toString();
		}
	}

	public static schedulingThreadProcess getThProcess() {
		return thProcess;
	}


	private static void clearContainer(){
		try{


			for(int i=0; i<thProcess.getPbe().getContainer_threadevents().size();i++){
				schedulingThreadEvent ste =  (schedulingThreadEvent)thProcess.getPbe().getContainer_threadevents().get(i);
				if(ste.getStateThread()==0){
					ste.setStateThread(2);				
				}
				ste.interrupt();
//				container.remove(keys.get(i));
			}

		}catch (Exception e) {

		}	
		
	}
	


	public static batch_init getConfiguration() {
		if(configuration==null) configuration=new batch_init(); 
		if(configuration.getLoadedFrom().trim().equals("")){
			configuration=new batch_init();
		}
		return configuration;
	}
	

}
