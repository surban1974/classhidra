package application.batch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import it.classhidra.core.controller.bsController;
import it.classhidra.core.tool.log.statistic.StatisticEntity;
import it.classhidra.scheduler.common.generic_batch;
import it.classhidra.scheduler.common.i_batch;



public class Check extends generic_batch implements i_batch,Serializable{
	private static final long serialVersionUID = 1L;
	boolean writeLog=true;

	public String getId(){
		return "Check";
	}

	public String execute() throws Exception {

		StatisticEntity stat = null;
		try{
			stat = new StatisticEntity(
					"Check URL",
					"",
					"",
					"",
					"execute",
					null,
					new Date(),
					null,
					null);
		}catch(Exception e){
		}

		if(output.get("EXIT")==null) output.put("EXIT", "");
		exit="OK";

/*
		checkUrl("http://sijinn-surban1974.rhcloud.com");
		checkUrl("http://classhidra-surban1974.rhcloud.com");
		checkUrl("http://neohort-surban1974.rhcloud.com");
*/

		if(!exit.equals("KO")){
			exit="OK";
			output.put("EXIT", output.get("EXIT")+" Batch Check... OK");
		}


		if(stat!=null){
			stat.setFt(new Date());
			bsController.putToStatisticProvider(stat);
		}
		return null;
	}

	public void readInput(String xml) {
		super.readInput(xml);
	}

	public String writeOutput() {
		return super.writeOutput();
	}

	public String getDescription() {
		return "Giornaliero: Check";
	}

	public int getOrder() {
		return 1;
	}

	private void checkUrl(String path){

		if(output.get("EXIT")==null) output.put("EXIT", "");


		try{

			URL url = new URL(path);
	        URLConnection yc = url.openConnection();
	        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
	        String inputLine;
	        long length=0;
	        while ((inputLine = in.readLine()) != null)
	        	length+=inputLine.length();
	        in.close();
		}catch(Exception e){
			output.put("EXIT", output.get("EXIT")+"ERROR URL "+path+" "+e.toString());
		}


		exit="OK";




		if(!exit.equals("KO")){
			exit="OK";
			output.put("EXIT", output.get("EXIT")+" Batch URL "+path+" OK");
		}

	}

	public String getVersion(){
		return "b20161006";
	}

	public boolean isWriteLog(){
		return writeLog;
	}
}
