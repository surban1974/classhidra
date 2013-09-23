package it.classhidra.scheduler.job_examples;

import it.classhidra.core.tool.util.util_format;
import it.classhidra.scheduler.common.generic_batch;
import it.classhidra.scheduler.common.i_batch;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;


 


public class job_date extends generic_batch implements i_batch,Serializable{
	private static final long serialVersionUID = 1L;
	boolean writeLog=true;
	
	public String getId(){
		return "date";
	}

	public String execute() throws Exception {
		updateDATA();
		return null;
	}

	public void readInput(String xml) {
		super.readInput(xml);
	}

	public String writeOutput() {
		return super.writeOutput();
	}

	public String getDescription() {
		return "Anyday: UPDATE DATE";
	}

	public int getOrder() {
		return 1;
	}

	private void updateDATA(){

		if(output.get("EXIT")==null) output.put("EXIT", "");
		Properties prop_feste = getProperty(this.getClass().getPackage().getName()+".properties."+getId());

		Date current_date = new Date();
		String prop = prop_feste.getProperty(util_format.dataToString(current_date, "yyyyMMdd"));
		if(prop!=null){
			output.put("EXIT", "Update KO: la data corrente corrisponde al giorno festivo: "+prop);
			exit="OK";
			return;
		}


		Calendar current_cl = Calendar.getInstance();
		current_cl.setTimeInMillis(current_date.getTime());
		if(current_cl.get(Calendar.DAY_OF_WEEK)==7 || current_cl.get(Calendar.DAY_OF_WEEK)==0){
			output.put("EXIT", "Update KO: la data corrente corrisponde al : "+util_format.dataToString(current_date, "EEEEEEE"));
			exit="OK";

		}

/* TO DO
 * 
 * ...
 * 
 */
			exit="OK";
		if(!exit.equals("KO")){
			exit="OK";
			output.put("EXIT", output.get("EXIT")+" Batch OK");
		}		

	}

	public String getVersion(){
		return "b20100720";
	}
	
	public boolean isWriteLog(){
		return writeLog;
	}
}
