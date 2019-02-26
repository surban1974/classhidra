package it.classhidra.scheduler.def;

import it.classhidra.core.tool.db.db_connection;
import it.classhidra.scheduler.common.generic_batch;
import it.classhidra.scheduler.common.i_batch;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;




public class ExecStoredProcedure extends generic_batch implements i_batch,Serializable{
	private static final long serialVersionUID = 1L;
	protected static String STOREDPROCEDURE_NAME = "$sp.name";
	protected static String STOREDPROCEDURE_PARAMETER = "$sp.parameter.";
	protected static String STOREDPROCEDURE_ASUPDATE = "$sp.asupdate";
	
	boolean writeLog=true;
	
	public String getId(){
		return "ExecStoredProcedure";
	}

	public String execute() throws Exception {
		
		callStoredProcedure(loadProperty(db));
		return null;
	}


	public String writeOutput() {
		return super.writeOutput();
	}

	public String getDescription() {
		return "ExecStoredProcedure";
	}

	public int getOrder() {
		return 1;
	}

	private void callStoredProcedure(Properties spproperty){

		String sp_asupdate = spproperty.getProperty(STOREDPROCEDURE_ASUPDATE);
		if(sp_asupdate!=null && sp_asupdate.toLowerCase().equals("true")){
			callStoredProcedureAsUpdate(spproperty);
			return;
		}		
		
		if(output.get("EXIT")==null) output.put("EXIT", "");

		String sp_name=spproperty.getProperty(STOREDPROCEDURE_NAME);
		if(sp_name==null){
			exit="KO";
			output.put("EXIT", output.get("EXIT")+" Exec StoredProcedure KO: $sp.name=null");
			return;
		}

		Connection conn=null;
		CallableStatement cst=null;



		try{
			conn = new db_connection().getContent();

			cst = conn.prepareCall("{call "+sp_name+"}");
			
			int parCounter=1;
			while(spproperty.getProperty(STOREDPROCEDURE_PARAMETER+parCounter)!=null){
				cst.setString(parCounter, spproperty.getProperty(STOREDPROCEDURE_PARAMETER+parCounter));
				parCounter++;
			}

			cst.execute();
			
			boolean isOutParameter=true;
			parCounter=1;
			while(isOutParameter){
				try{
					Object outParam = cst.getObject(parCounter);
					output.put(STOREDPROCEDURE_PARAMETER+"out."+parCounter, outParam.toString());
					parCounter++;
				}catch(Exception exp){
					isOutParameter=false;
				}
			}
			
			
			output.put("EXIT", output.get("EXIT")+" Exec StoredProcedure OK");

			exit="OK";
		}catch(Exception ex){
			exit="KO";
			output.put("EXIT", output.get("EXIT")+" Exec StoredProcedure KO: "+ex.toString());

		}finally{

			db_connection.release(null, cst, conn);
		}

		if(!exit.equals("KO")){
			exit="OK";
			output.put("EXIT", output.get("EXIT")+" Batch OK");
		}		

	}
	
	private void callStoredProcedureAsUpdate(Properties spproperty){


		
		if(output.get("EXIT")==null) output.put("EXIT", "");

		String sp_name=spproperty.getProperty(STOREDPROCEDURE_NAME);
		if(sp_name==null){
			exit="KO";
			output.put("EXIT", output.get("EXIT")+" Exec StoredProcedureAsUpdate KO: $sp.name=null");
			return;
		}

		Connection conn=null;
		Statement st=null;



		try{
			conn = new db_connection().getContent();

			st = conn.createStatement();
			

			st.executeUpdate("call "+sp_name);
			
			
			
			output.put("EXIT", output.get("EXIT")+" Exec StoredProcedureAsUpdate OK");

			exit="OK";
		}catch(Exception ex){
			exit="KO";
			output.put("EXIT", output.get("EXIT")+" Exec StoredProcedureAsUpdate KO: "+ex.toString());

		}finally{

			db_connection.release(null, st, conn);
		}

		if(!exit.equals("KO")){
			exit="OK";
			output.put("EXIT", output.get("EXIT")+" Batch OK");
		}		

	}
	

	public String getVersion(){
		return "b20120629";
	}
	
	public boolean isWriteLog(){
		return writeLog;
	}
}
