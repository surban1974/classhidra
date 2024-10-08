package it.classhidra.scheduler.scheduling.implementation.mysql;



import it.classhidra.core.tool.util.util_format;
import it.classhidra.scheduler.common.i_4Batch;
import it.classhidra.scheduler.common.i_batch;
import it.classhidra.scheduler.scheduling.db.db_batch;
import it.classhidra.scheduler.scheduling.db.db_batch_property;
import it.classhidra.scheduler.scheduling.init.batch_init;
import it.classhidra.scheduler.servlets.servletBatchScheduling;

import java.util.HashMap;





public class sql_batch {

	public static String sql_LoadBatch(HashMap<String,?> form){
		batch_init b_init = null;
	    try{
	    	b_init = servletBatchScheduling.getConfiguration();
	    }catch(Exception e){
	    	b_init = new batch_init();
	    }
		String result="";
		result+="select B.*, L.tm_fin as tm_last from "+b_init.get_db_prefix()+"batch B \n";
		result+="LEFT JOIN  \n";
		result+="( select max(tm_fin) as tm_fin ,cd_btch   from "+b_init.get_db_prefix()+"batch_log \n";
		result+="group by cd_btch ) L \n";
		result+="ON  B.cd_btch = L.cd_btch \n";

		
		result+="WHERE B.cd_ist  is not null \n";
		if(form.get("cd_ist")!=null )
			result+="and B.cd_ist = "+form.get("cd_ist")+" \n";
		if(form.get("cd_btch")==null || form.get("cd_btch").toString().trim().equals(""))
			result+="and (B.cd_p_btch is null or B.cd_p_btch='')  \n";
		else result+="and B.cd_p_btch = '"+form.get("cd_btch")+"' AND B.cd_p_btch <> '' \n";
		
		if(form.get("state")!=null)
			result+="and B.state= "+form.get("state")+" \n";

		result+="ORDER BY B.ord, B.cd_btch  \n";

		return result;
	}
	
	public static String sql_LoadBatchSingle(HashMap<String,?> form){
		batch_init b_init = null;
	    try{
	    	b_init = servletBatchScheduling.getConfiguration();
	    }catch(Exception e){
	    	b_init = new batch_init();
	    }
		String result="";
		result+="select B.*, L.tm_fin as tm_last from "+b_init.get_db_prefix()+"batch B \n";
		result+="LEFT JOIN  \n";
		result+="( select max(tm_fin) as tm_fin ,cd_btch   from "+b_init.get_db_prefix()+"batch_log \n";
		result+="group by cd_btch ) L \n";
		result+="ON  B.cd_btch = L.cd_btch \n";

		
		result+="WHERE B.cd_ist  is not null \n";
		if(form.get("cd_ist")!=null )
			result+="and B.cd_ist = "+form.get("cd_ist")+" \n";
		
		if(form.get("cd_btch")==null || form.get("cd_btch").toString().trim().equals(""))
			result+="and (B.cd_btch is null or B.cd_btch='')  \n";
		else result+="and B.cd_btch = '"+form.get("cd_btch")+"' AND B.cd_btch <> '' \n";
		
		if(form.get("state")!=null)
			result+="and B.state= "+form.get("state")+" \n";


		return result;
	}

	public static String sql_LoadBatchLog(HashMap<String,?> form){
		batch_init b_init = null;
	    try{
	    	b_init = servletBatchScheduling.getConfiguration();
	    }catch(Exception e){
	    	b_init = new batch_init();
	    }
		String result="";
		result+="select cd_btch,tm_start,tm_fin,st_exec, dsc_exec from "+b_init.get_db_prefix()+"batch_log \n";
		result+="WHERE \n";

		result+="cd_btch = '"+form.get("cd_btch")+"'  \n";
		result+="ORDER BY tm_fin desc  \n";
		result+=" LIMIT 0,20  \n";
		return result;
	}

	public static String sql_DeleteBatchLog(HashMap<String,?> form){
		batch_init b_init = null;
	    try{
	    	b_init = servletBatchScheduling.getConfiguration();
	    }catch(Exception e){
	    	b_init = new batch_init();
	    }
		String result="";
		result+="DELETE  from "+b_init.get_db_prefix()+"batch_log \n";
		result+="WHERE \n";

		result+="cd_btch = '"+form.get("cd_btch")+"'  \n";
		return result;
	}

	public static String sql_DeleteBatch(HashMap<String,?> form){
		batch_init b_init = null;
	    try{
	    	b_init = servletBatchScheduling.getConfiguration();
	    }catch(Exception e){
	    	b_init = new batch_init();
	    }
		String result="";
		result+="DELETE from "+b_init.get_db_prefix()+"batch \n";
		result+="WHERE \n";
		if(form.get("cd_ist")!=null )
			result+=" cd_ist = "+form.get("cd_ist")+" AND \n";

		result+="( cd_btch = '"+form.get("cd_btch")+"'  \n";
		result+="OR cd_p_btch = '"+form.get("cd_btch")+"')  \n";
		result+="AND state <> "+i_batch.STATE_SUSPEND+" \n";
		return result;
	}

	public static String sql_ClearStateBatch(HashMap<String,?> form){
		batch_init b_init = null;
	    try{
	    	b_init = servletBatchScheduling.getConfiguration();
	    }catch(Exception e){
	    	b_init = new batch_init();
	    }
		String result="";
		result+="UPDATE "+b_init.get_db_prefix()+"batch \n";
		result+="SET state = "+i_batch.STATE_NORMAL+" \n";
		result+="WHERE \n";
		if(form.get("cd_ist")!=null )
			result+=" cd_ist = "+form.get("cd_ist")+" AND \n";

		result+="cd_btch = '"+form.get("cd_btch")+"'  \n";
//		result+="OR cd_p_btch = '"+form.get("cd_btch")+"')  \n";
		result+="AND state <> "+i_batch.STATE_SUSPEND+" \n";
		return result;
	}
	
	public static String sql_Kill4Timeout(HashMap<String,?> form){
		batch_init b_init = null;
	    try{
	    	b_init = servletBatchScheduling.getConfiguration();
	    }catch(Exception e){
	    	b_init = new batch_init();
	    }
		String result="";	
		result+="update "+b_init.get_db_prefix()+"batch \n";
		result+="inner join ( \n";
		result+="SELECT cd_btch, cd_ist, CONVERT(value, SIGNED INTEGER) as timeout FROM "+b_init.get_db_prefix()+"batch_property \n";
		result+="where cd_property = '"+i_4Batch.o_KILL4TIMEOUT+"' \n";
		result+=") prop on prop.cd_btch= "+b_init.get_db_prefix()+"batch.cd_btch and prop.cd_ist= "+b_init.get_db_prefix()+"batch.cd_ist \n";
		result+="set state = "+i_batch.STATE_NORMAL+" \n"; 
		result+="WHERE \n"; 
		result+=""+b_init.get_db_prefix()+"batch.state = "+i_batch.STATE_INEXEC+" \n";
		result+="and ROUND(time_to_sec((TIMEDIFF(NOW(), tm_next))) / 60) > prop.timeout \n"; 
		return result;
	}	
	
	public static String sql_ClearBatchState(){
		batch_init b_init = null;
	    try{
	    	b_init = servletBatchScheduling.getConfiguration();
	    }catch(Exception e){
	    	b_init = new batch_init();
	    }
		return "update "+b_init.get_db_prefix()+"batch set state=0, st_exec=0, tm_next=null";
	}
	
	public static String sql_LoadBatchProperties(HashMap<String,?> form){
		batch_init b_init = null;
	    try{
	    	b_init = servletBatchScheduling.getConfiguration();
	    }catch(Exception e){
	    	b_init = new batch_init();
	    }
	    db_batch batch = (db_batch)form.get("selected");
	    String result="";
	    result="SELECT * FROM "+b_init.get_db_prefix()+"batch_property \n";
	    result+="where cd_btch='"+util_format.convertAp(batch.getCd_btch())+"' and cd_ist="+batch.getCd_ist()+" \n";
		return result;
	}
	
	public static String sql_DeleteBatchProperties(HashMap<String,?> form){
		batch_init b_init = null;
	    try{
	    	b_init = servletBatchScheduling.getConfiguration();
	    }catch(Exception e){
	    	b_init = new batch_init();
	    }
	    db_batch batch = (db_batch)form.get("selected");
	    String result="";
	    result="DELETE FROM "+b_init.get_db_prefix()+"batch_property \n";
	    result+="where cd_btch='"+util_format.convertAp(batch.getCd_btch())+"' and cd_ist="+batch.getCd_ist()+" \n";
		return result;
	}	
	
	public static String sql_UpdateBatchProperties(HashMap<String,?> form){
		batch_init b_init = null;
	    try{
	    	b_init = servletBatchScheduling.getConfiguration();
	    }catch(Exception e){
	    	b_init = new batch_init();
	    }
	    db_batch batch = (db_batch)form.get("selected");
	    db_batch_property property = (db_batch_property)form.get("selected_property");
	    String result="";
	    result="UPDATE "+b_init.get_db_prefix()+"batch_property SET value='"+util_format.convertAp(property.getValue())+"'  \n";
	    result+="where cd_btch='"+util_format.convertAp(batch.getCd_btch())+"' and cd_ist="+batch.getCd_ist()+" and cd_property='"+util_format.convertAp(property.getCd_property())+"' \n";
		return result;
	}		
	
	public static String sql_InsertBatchProperty(HashMap<String,?> form){
		batch_init b_init = null;
	    try{
	    	b_init = servletBatchScheduling.getConfiguration();
	    }catch(Exception e){
	    	b_init = new batch_init();
	    }
	    db_batch_property property = (db_batch_property)form.get("selected_property");
	    String result="";
	    result="INSERT INTO "+b_init.get_db_prefix()+"batch_property (cd_ist, cd_btch, cd_property, value) VALUES (\n";
	    result+=property.getCd_ist()+",";
	    result+="'"+util_format.convertAp(property.getCd_btch())+"',";
	    result+="'"+util_format.convertAp(property.getCd_property())+"',";
	    result+="'"+util_format.convertAp(property.getValue())+"'";
	    result+=") \n";
		return result;
	}	
}
