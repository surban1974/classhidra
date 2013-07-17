package it.classhidra.scheduler.scheduling.db;


import it.classhidra.core.tool.db.db_connection;
import it.classhidra.core.tool.util.util_blob;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.framework.web.integration.i_module_integration;
import it.classhidra.scheduler.util.util_batch;

import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Vector;


public class db_4Batch  {

	public Object operation(String oper, HashMap form) throws Exception {
		if(oper==null) return null;
		oper=oper.toUpperCase();

		if(oper.equals(i_module_integration.o_FINDFORMLIST))
			return operation_FINDFORMLIST(form);

		if(oper.equals(i_module_integration.o_DELETE))
			return operation_DELETE(form);

		if(oper.equals(i_module_integration.o_UPDATE))
			return operation_UPDATE(form);

		if(oper.equals("CLEAR_STATE"))
			return operation_CLEAR_STATE(form);


		if(oper.equals(i_module_integration.o_FIND))
			return operation_FIND(form);

		return null;
	}

	private Object operation_FIND(HashMap form) throws Exception{
		db_batch element = null;
		element = (db_batch)util_blob.load_db_element(new db_batch(), null, sql_batch.sql_LoadBatchSingle(form));
		return element;
	}


	private Object operation_FINDFORMLIST(HashMap form) throws Exception{

		Vector elements = null;
		if(form!=null && form.get("operation")!=null && form.get("operation").equals("log")){
			elements = util_blob.load_db_elements(new db_batch_log(), null, sql_batch.sql_LoadBatchLog(form));

		}
		else elements = util_blob.load_db_elements(new db_batch(), null, sql_batch.sql_LoadBatch(form));
		return elements;
	}

	private Object operation_DELETE(HashMap form) throws Exception{


		if(form!=null && form.get("operation")!=null && form.get("operation").equals("log")){
			return operation_DELETELOG(form);
		}
		else return operation_DELETEBATCH(form);

	}

	private Object operation_UPDATE(HashMap form) throws Exception{

		if(form==null) return new Boolean(false);

		db_batch selected = (db_batch)form.get("selected");
		if(selected==null) return new Boolean(false);
		db_batch original = null;

		Connection conn=null;
		Statement st=null;

		try{

			conn = new db_connection().getContent();
			st = conn.createStatement();

			original = (db_batch)util_blob.load_db_element(selected, conn, st);
			if(original==null) return new Boolean(false);

			if(original.getState().shortValue()==db_batch.STATE_INEXEC)
				return new Boolean(false);

			if(original.getState().shortValue()==db_batch.STATE_SCHEDULED)
				original.setState(new Integer(db_batch.STATE_NORMAL));

			original.setDsc_btch(selected.getDsc_btch());
			original.setPeriod(selected.getPeriod());
			original.setTm_next(null);

			try{
				util_batch.reCalcNextTime(original, util_format.dataToString(new java.util.Date(), "yyyy-MM-dd-HH-mm"));
			}catch(Exception e){
			}
			st.execute(original.sql_Update(original));


			conn.close();
			return new Boolean(true);
		}catch(Exception ex){
			throw ex;
		}finally{
			db_connection.release(null, st, conn);
		}

	}


	private Object operation_CLEAR_STATE(HashMap form) throws Exception{

		if(form==null) return new Boolean(false);


		Connection conn=null;
		Statement st=null;

		try{

			conn = new db_connection().getContent();
			st = conn.createStatement();

			st.executeUpdate(sql_batch.sql_ClearStateBatch(form));

			conn.close();
			return new Boolean(true);
		}catch(Exception ex){
			throw ex;
		}finally{
			db_connection.release(null, st, conn);
		}

	}


	private Object operation_DELETELOG(HashMap form) throws Exception{


		Connection conn=null;
		Statement st=null;

		try{

			conn = new db_connection().getContent();

			st = conn.createStatement();

			st.execute(sql_batch.sql_DeleteBatchLog(form));


			conn.close();
			return new Boolean(true);
		}catch(Exception ex){
			throw ex;
		}finally{
			db_connection.release(null, st, conn);
		}


	}

	private Object operation_DELETEBATCH(HashMap form) throws Exception{

		Connection conn=null;
		Statement st=null;

		try{

			conn = new db_connection().getContent();

			st = conn.createStatement();

			st.execute(sql_batch.sql_DeleteBatch(form));


			conn.close();
			return new Boolean(true);
		}catch(Exception ex){
			throw ex;
		}finally{
			db_connection.release(null, st, conn);
		}

	}
}
