package it.classhidra.scheduler.scheduling.implementation.mysql2;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import it.classhidra.core.tool.db.db_connection;
import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_blob;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.scheduler.common.i_4Batch;
import it.classhidra.scheduler.common.i_batch;
import it.classhidra.scheduler.scheduling.db.db_batch;
import it.classhidra.scheduler.scheduling.db.db_batch_log2;
import it.classhidra.scheduler.scheduling.db.i_batch_log;
import it.classhidra.scheduler.util.util_batch;



public class db_4Batch implements i_4Batch  {
	private static final long serialVersionUID = 1L;


	public Object operation(String oper, HashMap<String,?> form) throws Exception {
		if(oper==null) return null;
		oper=oper.toUpperCase();
		
		if(oper.equals(o_INSTANCE_LOG_OBJECT))
			return new db_batch_log2();

		if(oper.equals(o_FINDFORMLIST))
			return operation_FINDFORMLIST(form);
		
		if(oper.equals(o_LOAD_BATCH_PROPERTIES))
			return operation_LOAD_BATCH_PROPERTIES(form);		

		if(oper.equals(o_DELETE))
			return operation_DELETE(form);

		if(oper.equals(o_UPDATE))
			return operation_UPDATE(form);
		
		if(oper.equals(o_UPDATE_STATE))
			return operation_UPDATE_STATE(form);	

		if(oper.equals(o_UPDATE_STATES_AND_NEXTEXEC))
			return operation_UPDATE_STATES_AND_NEXTEXEC(form);			
		
		if(oper.equals(o_INSERT))
			return operation_INSERT(form);

		if(oper.equals(o_CLEAR_STATE))
			return operation_CLEAR_STATE(form);

		if(oper.equals(o_FIND))
			return operation_FIND(form);
		
		if(oper.equals(o_FIND_SIMPLE))
			return operation_FIND_SIMPLE(form);		
		
		if(oper.equals(o_CLEAR_BATCH_STATES))
			return operation_CLEAR_BATCH_STATES(form);	
		
		if(oper.equals(o_KILL4TIMEOUT))
			return operation_KILL4TIMEOUT(form);			

		if(oper.equals(o_WRITE_LOG))
			return operation_WRITE_LOG(form);			

		return null;
	}

	private Object operation_FIND(HashMap<String,?> form) throws Exception{
		db_batch element = null;
		element = (db_batch)util_blob.load_db_element(new db_batch(), null, sql_batch.sql_LoadBatchSingle(form));
		return element;
	}
	
	private Object operation_FIND_SIMPLE(HashMap<String,?> form) throws Exception{
		db_batch element = (db_batch)form.get("selected");
		if(element==null)
			return null;
		element = (db_batch)util_blob.load_db_element(element, null);
		return element;
	}	


	private Object operation_FINDFORMLIST(HashMap<String,?> form) throws Exception{

		List<?> elements = null;
		if(form!=null && form.get("operation")!=null && form.get("operation").equals("log")){
			elements = util_blob.load_db_elements(new db_batch_log2(), null, sql_batch.sql_LoadBatchLog(form));

		}
		else elements = util_blob.load_db_elements(new db_batch(), null, sql_batch.sql_LoadBatch(form));
		return elements;
	}

	private Object operation_DELETE(HashMap<String,?> form) throws Exception{


		if(form!=null && form.get("operation")!=null && form.get("operation").equals("log")){
			return operation_DELETELOG(form);
		}
		else return operation_DELETEBATCH(form);

	}

	private Object operation_UPDATE(HashMap<String,?> form) throws Exception{

		if(form==null) return new Boolean(false);

		db_batch selected = (db_batch)form.get("selected");
		if(selected==null) return new Boolean(false);
		db_batch original = null;

		Connection conn=null;
		Statement st=null;

		try{

			conn = new db_connection().getContent();
			conn.setAutoCommit(false);
			st = conn.createStatement();

			original = (db_batch)util_blob.load_db_element(selected, conn, st);
			if(original==null) return new Boolean(false);

			if(original.getState().shortValue()==i_batch.STATE_INEXEC)
				return new Boolean(false);

			if(original.getState().shortValue()==i_batch.STATE_SCHEDULED)
				original.setState(new Integer(i_batch.STATE_NORMAL));

			original.setOrd(selected.getOrd());
			original.setDsc_btch(selected.getDsc_btch());
			original.setPeriod(selected.getPeriod());
			original.setTm_next(null);

			try{
				util_batch.reCalcNextTime(original, util_format.dataToString(new java.util.Date(), "yyyy-MM-dd-HH-mm"),0);
			}catch(Exception e){
			}
			st.execute(original.sql_Update(original));


			conn.commit();
			return new Boolean(true);
		}catch(Exception ex){
			try{
				conn.rollback();
			}catch(Exception e){
			}
			new bsException("Scheduler: "+ex.toString(),iStub.log_ERROR);
			throw ex;
		}finally{
			db_connection.release(null, st, conn);
		}

	}
	
	private Object operation_UPDATE_STATE(HashMap<String,?> form) throws Exception{

		if(form==null) return new Boolean(false);

		db_batch batch = (db_batch)form.get("selected");
		Integer state = (Integer)form.get("state");
		if(batch==null || state==null)
			return new Boolean(false);
		
		


		Connection conn=null;
		Statement st=null;

		try{

			conn = new db_connection().getContent();
			conn.setAutoCommit(false);
			st = conn.createStatement();

			batch.setState(state);

			if(
				(batch.getPeriod()!=null && !batch.getPeriod().equals("")) ||
				(batch.getCd_p_btch()!=null && !batch.getCd_p_btch().equals("")) ||
				(batch.getCls_btch()!=null && !batch.getCls_btch().equals(""))
			)
				st.execute(batch.sql_Update(batch));
			else
				st.execute(batch.sql_UpdateOnlyState(batch));
			
			conn.commit();

			return new Boolean(true);
		}catch(Exception ex){
			try{
				conn.rollback();
			}catch(Exception e){
			}
			new bsException("Scheduler: "+ex.toString(),iStub.log_ERROR);
			throw ex;
		}finally{
			db_connection.release(null, st, conn);
		}

	}	
	
	
	private Object operation_UPDATE_STATES_AND_NEXTEXEC(HashMap<String,?> form) throws Exception{

		if(form==null) return new Boolean(false);

		List<?> batch_updated = (List<?>)form.get("list");
		
		if(batch_updated==null || batch_updated.size()==0)
			return new Boolean(false);
		
		List<String> sql_updates  = new ArrayList<String>();
		for(int i=0;i<batch_updated.size();i++){
			db_batch el = (db_batch)batch_updated.get(i);
			sql_updates.add(el.sql_Update(el));
		}
		
		
		



		Connection conn=null;
		Statement st=null;

		try{

			conn = new db_connection().getContent();
			conn.setAutoCommit(false);
			st = conn.createStatement();


			for(int i=0;i<sql_updates.size();i++){
				try{
					st.executeUpdate((String)sql_updates.get(i));
				}catch (Exception e) {
					e.toString();
				}
			}

			
			conn.commit();
			return new Boolean(true);
		}catch(Exception ex){
			try{
				conn.rollback();
			}catch(Exception e){
			}
			new bsException("Scheduler: "+ex.toString(),iStub.log_ERROR);
			throw ex;
		}finally{
			db_connection.release(null, st, conn);
		}

	}		
	
	private Object operation_INSERT(HashMap<String,?> form) throws Exception{

		if(form==null) return new Boolean(false);

		db_batch inserted = (db_batch)form.get("inserted");
		if(inserted==null) return new Boolean(false);

		Connection conn=null;
		Statement st=null;

		try{

			conn = new db_connection().getContent();
			conn.setAutoCommit(false);
			st = conn.createStatement();

			try{
				util_batch.reCalcNextTime(inserted, util_format.dataToString(new java.util.Date(), "yyyy-MM-dd-HH-mm"),0);
			}catch(Exception e){
			}

			st.execute(inserted.sql_Insert());


			conn.commit();
			return new Boolean(true);
		}catch(Exception ex){
			try{
				conn.rollback();
			}catch(Exception e){
			}
			new bsException("Scheduler: "+ex.toString(),iStub.log_ERROR);
			throw ex;
		}finally{
			db_connection.release(null, st, conn);
		}

	}
	


	private Object operation_CLEAR_STATE(HashMap<String,?> form) throws Exception{

		if(form==null) return new Boolean(false);


		Connection conn=null;
		Statement st=null;

		try{

			conn = new db_connection().getContent();
			conn.setAutoCommit(false);
			st = conn.createStatement();

			st.executeUpdate(sql_batch.sql_ClearStateBatch(form));

			conn.commit();
			return new Boolean(true);
		}catch(Exception ex){
			try{
				conn.rollback();
			}catch(Exception e){
			}
			new bsException("Scheduler: "+ex.toString(),iStub.log_ERROR);
			throw ex;
		}finally{
			db_connection.release(null, st, conn);
		}

	}


	private Object operation_DELETELOG(HashMap<String,?> form) throws Exception{


		Connection conn=null;
		Statement st=null;

		try{

			conn = new db_connection().getContent();
			conn.setAutoCommit(false);
			st = conn.createStatement();

			st.execute(sql_batch.sql_DeleteBatchLog(form));


			conn.commit();
			return new Boolean(true);
		}catch(Exception ex){
			try{
				conn.rollback();
			}catch(Exception e){
			}
			new bsException("Scheduler: "+ex.toString(),iStub.log_ERROR);
			throw ex;
		}finally{
			db_connection.release(null, st, conn);
		}


	}

	private Object operation_DELETEBATCH(HashMap<String,?> form) throws Exception{

		Connection conn=null;
		Statement st=null;

		try{

			conn = new db_connection().getContent();
			conn.setAutoCommit(false);
			st = conn.createStatement();

			st.execute(sql_batch.sql_DeleteBatch(form));


			conn.commit();
			return new Boolean(true);
		}catch(Exception ex){
			try{
				conn.rollback();
			}catch(Exception e){
			}
			new bsException("Scheduler: "+ex.toString(),iStub.log_ERROR);
			throw ex;
		}finally{
			db_connection.release(null, st, conn);
		}

	}
	
	private Object operation_CLEAR_BATCH_STATES(HashMap<String,?> form) throws Exception{

		if(form==null) return new Boolean(false);


		Connection conn=null;
		Statement st=null;

		try{

			conn = new db_connection().getContent();
			conn.setAutoCommit(false);
			st = conn.createStatement();
//			st.executeUpdate(sql_batch.sql_ClearStateBatch(form));
			st.executeUpdate(sql_batch.sql_ClearBatchState());
			conn.commit();
			return new Boolean(true);
		}catch(Exception ex){
			try{
				conn.rollback();
			}catch(Exception e){
			}
			new bsException("Scheduler: "+ex.toString(),iStub.log_ERROR);
			throw ex;
		}finally{
			db_connection.release(null, st, conn);
		}

	}
	
	private Object operation_KILL4TIMEOUT(HashMap<String,?> form) throws Exception{

		if(form==null) return new Boolean(false);


		Connection conn=null;
		Statement st=null;

		try{

			conn = new db_connection().getContent();
			conn.setAutoCommit(false);
			st = conn.createStatement();

			st.executeUpdate(sql_batch.sql_Kill4Timeout(form));
			conn.commit();

			return new Boolean(true);
		}catch(Exception ex){
			try{
				conn.rollback();
			}catch(Exception e){
			}
			new bsException("Scheduler: "+ex.toString(),iStub.log_ERROR);
		}finally{
			db_connection.release(null, st, conn);
		}
		return new Boolean(false);
	}	
	
	private Object operation_WRITE_LOG(HashMap<String,?> form) throws Exception{

		if(form==null) return new Boolean(false);

		i_batch_log log = (i_batch_log)form.get("selected");
		if(log==null)
			return new Boolean(false);
		
		


		Connection conn=null;
		Statement st=null;

		try{

			conn = new db_connection().getContent();
			conn.setAutoCommit(false);
			st = conn.createStatement();

			st.executeUpdate(log.sql_Insert());
			conn.commit();

			return new Boolean(true);
		}catch(Exception ex){
			try{
				conn.rollback();
			}catch(Exception e){
			}
			new bsException("Scheduler: "+ex.toString(),iStub.log_ERROR);
			throw ex;
		}finally{
			db_connection.release(null, st, conn);
		}

	}		
	
	
	public Object operation_LOAD_BATCH_PROPERTIES(HashMap<String,?> form){


	    db_batch batch = (db_batch)form.get("selected");
	    if(batch==null)
			return new Boolean(false);
	    
		Properties property = new Properties();
		Connection conn=null;
		Statement st=null;
		ResultSet rs=null;
		
		try{
			conn = new db_connection().getContent();
			st = conn.createStatement();

			rs = st.executeQuery(sql_batch.sql_LoadBatchProperties(form));
			
			while(rs.next()){
				String key = rs.getString("cd_property");
				String value = rs.getString("value");
				if(key!=null && value!=null){
					property.put(key, value);
				}
			}
		}catch(Exception ex){
			new bsException("Scheduler: "+ex.toString(), iStub.log_ERROR);
		}finally{
			db_connection.release(rs, st, conn);
		}
		return property;
	}	
}
