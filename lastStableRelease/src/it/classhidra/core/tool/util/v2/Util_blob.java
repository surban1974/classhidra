package it.classhidra.core.tool.util.v2;

import it.classhidra.core.init.db_init;
import it.classhidra.core.tool.db.db_connection;
import it.classhidra.core.tool.elements.i_elementDBBase;

import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

public class Util_blob {
	
	   public static byte[] readBlob(ResultSet rs, String field){
			byte[] content = null;
			try{
				InputStream in = rs.getBinaryStream(field);
				
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				final int BUF_SIZE = 1 << 8; //1KiB buffer
				byte[] buffer = new byte[BUF_SIZE];
				int bytesRead = -1;
				while((bytesRead = in.read(buffer)) > -1) {
				      out.write(buffer, 0, bytesRead);
				}
				in.close();
				content = out.toByteArray();
			}catch(Exception ex){
//				new bsControllerException(ex,"ERROR");
			}finally{
			try{
				rs.getBinaryStream(field).close();
			}catch(Exception ex){}
			}	

			return content;
		}
	   
	   public static boolean transaction_update_blob(Connection ext_conn, byte[] blob_content, String prepareStatement_sql){
			boolean result=true;
			Connection conn=null;
			PreparedStatement pstmt=null;
			try{
				
				
				ByteArrayInputStream inStream = new ByteArrayInputStream(blob_content);				
				
				if(ext_conn==null){
					db_init init = new db_init();
					init.init();				
					conn = new db_connection().getContent(init);
					conn.setAutoCommit(false);
				}else{
					conn=ext_conn;
				}

//				pstmt = conn.prepareStatement("update to_content_ex_feed set blob_content_document = ? where id_document="+currentForm.getId_document());
				pstmt = conn.prepareStatement(prepareStatement_sql);
				pstmt.setBinaryStream(1,inStream,inStream.available());
				pstmt.executeUpdate();
				
				if(ext_conn==null)
					conn.commit();
			}catch(Exception ex){
				result=false;
			}finally{
				try{
					pstmt.close();
				}catch(Exception ex){
				}
				if(ext_conn==null)
					db_connection.release( null, conn);
			}				
			return result;
		}	
	   
	   public static i_elementDBBase load_db_element(i_elementDBBase element,Connection ext_conn,Statement ext_st) throws Exception{
			if(element==null) return element;
			i_elementDBBase clone_element = null;
			try{
				clone_element = Util_cloner.clone(element,i_elementDBBase.class);
			}catch(Exception ex){				
			}
			Connection conn=null;
			Statement st=null;
			ResultSet rs=null;
			try{
				if(ext_conn!=null && ext_st!=null){
					conn=ext_conn;
					st=ext_st;
				}
				if(ext_st!=null){
					st=ext_st;
				}
				if(ext_conn!=null && ext_st==null){
					st = conn.createStatement();
				}
				if(ext_conn==null && ext_st==null){
					conn = new db_connection().getContent();
					conn.setAutoCommit(false);
					st = conn.createStatement();
				}
				rs = st.executeQuery(element.sql_Select());
				if(rs.next()){
					clone_element.reInit(rs);
				}else{
					clone_element=null;
				}
				rs.close();

			}catch(Exception e){
				new bsException(e,iStub.log_ERROR);
				throw e;	
			}finally{
				if(ext_conn!=null && ext_st!=null){
					db_connection.release(rs, null, null);
				}
				if(ext_st!=null){	
					db_connection.release(rs, null, null);
				}
				
				if(ext_conn!=null && ext_st==null){
					db_connection.release(rs, st, null);
				}
				if(ext_conn==null && ext_st==null){
					db_connection.release(rs, st, conn);
				}

			}   
			return clone_element;

	   }
	   
	   public static i_elementDBBase load_db_element(i_elementDBBase element,Connection ext_conn) throws Exception{
			if(element==null) return element;
			i_elementDBBase clone_element = null;
			try{
				clone_element = Util_cloner.clone(element,i_elementDBBase.class);
			}catch(Exception ex){				
			}
			Connection conn=null;
			Statement st=null;
			ResultSet rs=null;
			try{
				if(ext_conn==null){
					conn = new db_connection().getContent();
					conn.setAutoCommit(false);
				}else conn=ext_conn;	
				st = conn.createStatement();
				rs = st.executeQuery(element.sql_Select());
				if(rs.next()){
					clone_element.reInit(rs);
				}else{
					clone_element=null;
				}
				rs.close();

			}catch(Exception e){
				new bsException(e,iStub.log_ERROR);
				throw e;	
			}finally{
				
				db_connection.release(rs, st, null);
				if(ext_conn==null) db_connection.release( null, conn);
			}   
			return clone_element;
	     }

	   public static i_elementDBBase load_db_element(i_elementDBBase element,Connection ext_conn,Statement ext_st, String sql) throws Exception{
			if(element==null) return element;
			i_elementDBBase clone_element = null;
			try{
				clone_element = Util_cloner.clone(element,i_elementDBBase.class);
			}catch(Exception ex){				
			}
			Connection conn=null;
			Statement st=null;
			ResultSet rs=null;
			try{
				if(ext_conn!=null && ext_st!=null){
					conn=ext_conn;
					st=ext_st;
				}
				if(ext_st!=null){
					st=ext_st;
				}
				if(ext_conn!=null && ext_st==null){
					st = conn.createStatement();
				}
				if(ext_conn==null && ext_st==null){
					conn = new db_connection().getContent();
					conn.setAutoCommit(false);
					st = conn.createStatement();
				}
				rs = st.executeQuery(sql);
				if(rs.next()){
					clone_element.reInit(rs);
				}else{
					clone_element=null;
				}
				rs.close();

			}catch(Exception e){
				new bsException(e,iStub.log_ERROR);
				throw e;	
			}finally{
				if(ext_conn!=null && ext_st!=null){
					db_connection.release(rs, null, null);
				}
				if(ext_st!=null){	
					db_connection.release(rs, null, null);
				}
				
				if(ext_conn!=null && ext_st==null){
					db_connection.release(rs, st, null);
				}
				if(ext_conn==null && ext_st==null){
					db_connection.release(rs, st, conn);
				}
			}   
			return clone_element;
	     }	   
	   
	   public static i_elementDBBase load_db_element(i_elementDBBase element,Connection ext_conn, String sql) throws Exception{
			if(element==null) return element;
			i_elementDBBase clone_element = null;
			try{
				clone_element = Util_cloner.clone(element,i_elementDBBase.class);
			}catch(Exception ex){				
			}
			Connection conn=null;
			Statement st=null;
			ResultSet rs=null;
			try{
				if(ext_conn==null){
					db_init init = new db_init();
					init.init();
					conn = new db_connection().getContent(init);
					conn.setAutoCommit(false);
				}else conn=ext_conn;	
				st = conn.createStatement();
				rs = st.executeQuery(sql);
				if(rs.next()){
					clone_element.reInit(rs);
				}else{
					clone_element=null;
				}
				rs.close();

			}catch(Exception e){
				new bsException(e,iStub.log_ERROR);
				throw e;	
			}finally{
				
				db_connection.release(rs, st, null);
				if(ext_conn==null) db_connection.release( null, conn);
			}   
			return clone_element;
	     }	   

	   public static Vector<i_elementDBBase> load_db_elements(i_elementDBBase element,Connection ext_conn, String sql) throws Exception{
		   Vector<i_elementDBBase> result = new Vector<i_elementDBBase>(); 
		   if(element==null) return result;
			Connection conn=null;
			Statement st=null;
			ResultSet rs=null;
			try{
				if(ext_conn==null){
					db_init init = new db_init();
					init.init();
					conn = new db_connection().getContent(init);
					conn.setAutoCommit(false);
				}else conn=ext_conn;	
				st = conn.createStatement();
				rs = st.executeQuery(sql);
				while(rs.next()){
					i_elementDBBase clone_element = null;
					try{
						clone_element = Util_cloner.clone(element,i_elementDBBase.class);
					}catch(Exception ex){				
					}
					clone_element.reInit(rs);
					result.add(clone_element);
				}
				rs.close();

			}catch(Exception e){
				new bsException(e,iStub.log_ERROR);
				throw e;	
			}finally{
				
				db_connection.release(rs, st, null);
				if(ext_conn==null) db_connection.release( null, conn);
			}   
			return result;
	     }	   
	   
	   public static String load_from_config(String field,String db_name) throws Exception{
		    String result = null;
			String sql = "select content from "+ db_name+ " where id='"+field+"' and backup_tm is null ";
			Connection conn=null;
			Statement st=null;
			ResultSet rs=null;
			try{
				
					conn = new db_connection().getContent();
//					conn.setAutoCommit(false);
					st = conn.createStatement();
				rs = st.executeQuery(sql);
				if(rs.next()){
					result=rs.getString(1);
				}
				rs.close();

			}catch(Exception e){
				new bsException(e,iStub.log_ERROR);
				throw e;	
			}finally{
				db_connection.release(rs, st, conn);
			}   
			return result;

	   }
	   
	   public static String load_from_config_clear(String field,String db_name) throws Exception{
		    String result = null;
			String sql = "select content from "+ db_name+ " where id='"+field+"' and backup_tm is null ";
			Connection conn=null;
			Statement st=null;
			ResultSet rs=null;
			try{
				
					conn = new db_connection().getContent(false);
//					conn.setAutoCommit(false);
					st = conn.createStatement();
				rs = st.executeQuery(sql);
				if(rs.next()){
					result=rs.getString(1);
				}
				rs.close();

			}catch(Exception e){
				new bsException("DBConfig Read Error name="+db_name+" :"+e.toString(),iStub.log_ERROR);
				throw e;	
			}finally{
				db_connection.release(rs, st, conn);
			}   
			return result;

	   }	
	   
	   public static int update_db_element(Connection ext_conn, String sql) throws Exception{
		   int result = 0;
			Connection conn=null;
			Statement st=null;

			try{
				if(ext_conn==null){
					db_init init = new db_init();
					init.init();
					conn = new db_connection().getContent(init);
					conn.setAutoCommit(false);
				}else conn=ext_conn;	
				st = conn.createStatement();
				result = st.executeUpdate(sql);
				conn.commit();

			}catch(Exception e){
				new bsException(e,iStub.log_ERROR);
				throw e;	
			}finally{
				
				db_connection.release(st, null);
				if(ext_conn==null) db_connection.release( null, conn);
			}   
			return result;
	     }	  
	   
	   public static i_elementDBBase update_db_element(i_elementDBBase element,Connection ext_conn, String sql_upd) throws Exception{
		   int cnt = 0;
			if(element==null) return null;
			Connection conn=null;
			Statement st=null;

			try{
				if(ext_conn==null){
					db_init init = new db_init();
					init.init();
					conn = new db_connection().getContent(init);
					conn.setAutoCommit(false);
				}else conn=ext_conn;	
				st = conn.createStatement();
				cnt = st.executeUpdate(sql_upd);
				conn.commit();

			}catch(Exception e){
				new bsException(e,iStub.log_ERROR);
				throw e;	
			}finally{
				
				db_connection.release(st, null);
				if(ext_conn==null) db_connection.release( null, conn);
			} 
			if(cnt>0){
				i_elementDBBase clone_element = null;
				try{
					clone_element = Util_cloner.clone(element,i_elementDBBase.class);
				}catch(Exception ex){				
				}

				return load_db_element(clone_element, ext_conn);
			}else return null;

	     }		   

}
