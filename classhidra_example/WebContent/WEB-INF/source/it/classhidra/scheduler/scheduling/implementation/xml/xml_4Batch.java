package it.classhidra.scheduler.scheduling.implementation.xml;



import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_beanMessageFactory;
import it.classhidra.core.tool.util.util_classes;
import it.classhidra.core.tool.util.util_file;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.v2.Util_sort;
import it.classhidra.scheduler.common.i_4Batch;
import it.classhidra.scheduler.common.i_batch;
import it.classhidra.scheduler.scheduling.DriverScheduling;
import it.classhidra.scheduler.scheduling.db.db_batch;
import it.classhidra.scheduler.scheduling.db.db_batch_log2;
import it.classhidra.scheduler.scheduling.db.db_batch_property;
import it.classhidra.scheduler.scheduling.db.i_batch_log;
import it.classhidra.scheduler.scheduling.init.batch_init;
import it.classhidra.scheduler.util.util_batch;




public class xml_4Batch implements i_4Batch  {
	private static final long serialVersionUID = 1L;
	protected static List<db_batch> xml_batch = new ArrayList<db_batch>();
	protected static List<i_batch_log> xml_batch_log = new ArrayList<i_batch_log>();
	protected static List<db_batch_property> xml_batch_property = new ArrayList<db_batch_property>();
	protected static Map<String,i_batch_log> last_batch_log = new HashMap<String, i_batch_log>();
	protected static final int max_log_size = 20;
	protected static final String CONST_XML_FOLDER = "xml.data.folder";
	
	@SuppressWarnings("unchecked")
	public xml_4Batch(){
		super();
		batch_init bInit = DriverScheduling.getConfiguration();
		if(bInit==null)
			bInit = new batch_init();
		boolean loadedFromPath=false;
		try {
			if(bInit.getCurrentProperties().get(CONST_XML_FOLDER)!=null) {
				try{
					Object obj = null;
					File xml_file = new File(bInit.getCurrentProperties().get(CONST_XML_FOLDER)+bInit.get_db_prefix()+"batch.xml");
					if(xml_file.exists())
						obj = util_beanMessageFactory.message2bean(util_file.getBytesFromFile(xml_file));
					if(obj==null) {
						xml_file = new File(bInit.getCurrentProperties().get(CONST_XML_FOLDER)+"batch.xml");
						if(xml_file.exists())
							obj = util_beanMessageFactory.message2bean(util_file.getBytesFromFile(xml_file));
					}
					if(obj!=null && obj instanceof List<?>) {
						xml_batch = (List<db_batch>)obj;
						loadedFromPath=true;
					}
				}catch(Exception e){
				}
				try{
					Object obj = null;
					File xml_file = new File(bInit.getCurrentProperties().get(CONST_XML_FOLDER)+bInit.get_db_prefix()+"batch_property.xml");
					if(xml_file.exists())
						obj = util_beanMessageFactory.message2bean(util_file.getBytesFromFile(xml_file));
					if(obj==null) {
						xml_file = new File(bInit.getCurrentProperties().get(CONST_XML_FOLDER)+"batch_property.xml");
						if(xml_file.exists())
							obj = util_beanMessageFactory.message2bean(util_file.getBytesFromFile(xml_file));
					}
					if(obj!=null && obj instanceof List<?>)
						xml_batch_property = (List<db_batch_property>)obj;
				}catch(Exception e){
				}	
				try{
					Object obj = null;
					File xml_file = new File(bInit.getCurrentProperties().get(CONST_XML_FOLDER)+bInit.get_db_prefix()+"batch_log.xml");
					if(xml_file.exists())
						obj = util_beanMessageFactory.message2bean(util_file.getBytesFromFile(xml_file));
					if(obj==null) {
						xml_file = new File(bInit.getCurrentProperties().get(CONST_XML_FOLDER)+"batch_log.xml");
						if(xml_file.exists())
							obj = util_beanMessageFactory.message2bean(util_file.getBytesFromFile(xml_file));
					}
					if(obj!=null && obj instanceof List<?>)
						xml_batch_log = (List<i_batch_log>)obj;
					for(i_batch_log log:xml_batch_log){
						i_batch_log h_log = last_batch_log.get(log.getCd_btch());
						if(h_log==null || (h_log!=null && h_log.getTm_fin().getTime()<log.getTm_fin().getTime()))
							last_batch_log.put(log.getCd_btch(),log);	
					}
				}catch(Exception e){
				}		
				
			}
		}catch(Exception e) {
			new bsException(e, iStub.log_ERROR);
		}

		if(loadedFromPath)
			return;
		
		try{
			Object obj = util_beanMessageFactory.message2bean(util_classes.getResourceAsByte(bInit.get_db_prefix()+"batch.xml"));
			if(obj==null)
				obj = util_beanMessageFactory.message2bean(util_classes.getResourceAsByte("config/"+bInit.get_db_prefix()+"batch.xml"));
			if(obj==null)
				obj = util_beanMessageFactory.message2bean(util_classes.getResourceAsByte("batch.xml"));
			if(obj==null)
				obj = util_beanMessageFactory.message2bean(util_classes.getResourceAsByte("config/batch.xml"));			
			if(obj!=null && obj instanceof List<?>)
				xml_batch = (List<db_batch>)obj;
		}catch(Exception e){
		}
		try{
			Object obj = util_beanMessageFactory.message2bean(util_classes.getResourceAsByte(bInit.get_db_prefix()+"batch_property.xml"));
			if(obj==null)
				obj = util_beanMessageFactory.message2bean(util_classes.getResourceAsByte("config/"+bInit.get_db_prefix()+"batch_property.xml"));
			if(obj==null)
				obj = util_beanMessageFactory.message2bean(util_classes.getResourceAsByte("batch_property.xml"));
			if(obj==null)
				obj = util_beanMessageFactory.message2bean(util_classes.getResourceAsByte("config/batch_property.xml"));			
			if(obj!=null && obj instanceof List<?>)
				xml_batch_property = (List<db_batch_property>)obj;
		}catch(Exception e){
		}	
		try{
			Object obj = util_beanMessageFactory.message2bean(util_classes.getResourceAsByte(bInit.get_db_prefix()+"batch_log.xml"));
			if(obj==null)
				obj = util_beanMessageFactory.message2bean(util_classes.getResourceAsByte("config/"+bInit.get_db_prefix()+"batch_log.xml"));
			if(obj==null)
				obj = util_beanMessageFactory.message2bean(util_classes.getResourceAsByte("batch_log.xml"));
			if(obj==null)
				obj = util_beanMessageFactory.message2bean(util_classes.getResourceAsByte("config/batch_log.xml"));			
			if(obj!=null && obj instanceof List<?>)
				xml_batch_log = (List<i_batch_log>)obj;
			for(i_batch_log log:xml_batch_log){
				i_batch_log h_log = last_batch_log.get(log.getCd_btch());
				if(h_log==null || (h_log!=null && h_log.getTm_fin().getTime()<log.getTm_fin().getTime()))
					last_batch_log.put(log.getCd_btch(),log);	
			}
		}catch(Exception e){
			new bsException(e, iStub.log_ERROR);
		}		
	}


	public Object operation(String oper, HashMap<String,?> form) throws Exception {
		if(oper==null) return null;
		oper=oper.toUpperCase();
		
		if(oper.equals(o_INSTANCE_LOG_OBJECT))
			return new db_batch_log2();

		if(oper.equals(o_FINDFORMLIST))
					return operation_FINDFORMLIST(form);
				
		if(oper.equals(o_LOAD_BATCH_PROPERTIES))
					return operation_LOAD_BATCH_PROPERTIES(form);	
		
		if(oper.equals(o_DELETE_BATCH_PROPERTIES))
					return operation_DELETE_BATCH_PROPERTIES(form);	
		
		if(oper.equals(o_INSERT_BATCH_PROPERTY))
					return operation_INSERT_BATCH_PROPERTY(form);	
		
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

		

		for(db_batch batch:xml_batch){
			boolean insert = true;
			if(batch.getCd_ist()==null)
				insert = false;
			if(form.get("cd_ist")!=null && batch.getCd_ist()!=Integer.valueOf(form.get("cd_ist").toString()))
				insert = false;
			
			
			
			if(form.get("cd_btch")==null || form.get("cd_btch").toString().trim().equals("")){
				if(batch.getCd_btch()==null || batch.getCd_btch().equals("")){
				}else insert = false;
			}else{
				if(batch.getCd_btch()!=null && !batch.getCd_btch().equals("") && batch.getCd_btch().equals(form.get("cd_btch").toString())){
				}else insert = false;
			}
			if(form.get("state")!=null){
				if(batch.getState().intValue()==Integer.valueOf(form.get("state").toString()).intValue()){						
				}else insert = false;
			}
			if(insert){
				i_batch_log h_log = last_batch_log.get(batch.getCd_btch());
				if(h_log!=null)
					batch.setTm_last(h_log.getTm_fin());
				return batch;
			}
		}
				
				
		return null;
	}
	
	private Object operation_FIND_SIMPLE(HashMap<String,?> form) throws Exception{
		db_batch element = (db_batch)form.get("selected");
		if(element==null)
			return null;
		
		for(db_batch batch:xml_batch){
			if(batch.getCd_ist().intValue()==element.getCd_ist().intValue() && batch.getCd_btch().equals(element.getCd_btch())){
				i_batch_log h_log = last_batch_log.get(batch.getCd_btch());
				if(h_log!=null)
					batch.setTm_last(h_log.getTm_fin());
				return batch;
			}
		}
		return null;
	}	


	private Object operation_FINDFORMLIST(HashMap<String,?> form) throws Exception{

		Vector<Object> elements = new Vector<Object>();
		if(form!=null && form.get("operation")!=null && form.get("operation").equals("log")){
			for(int i=xml_batch_log.size()-1;i>=0;i--){
				if(elements.size()<(max_log_size+1))
					elements.add(xml_batch_log.get(i));
				else
					break;
			}
		}
		else{
			
			for(db_batch batch:xml_batch){
				boolean insert = true;
				if(batch.getCd_ist()==null)
					insert = false;
				if(form.get("cd_ist")!=null && batch.getCd_ist()!=Integer.valueOf(form.get("cd_ist").toString()))
					insert = false;
				if(form.get("cd_btch")==null || form.get("cd_btch").toString().trim().equals("")){
					if(batch.getCd_p_btch()==null || batch.getCd_p_btch().equals("")){
					}else insert = false;
				}else{
					if(batch.getCd_p_btch()!=null && !batch.getCd_p_btch().equals("") && batch.getCd_p_btch().equals(form.get("cd_btch").toString())){
					}else insert = false;
				}
				if(form.get("state")!=null){
					if(batch.getState().intValue()==Integer.valueOf(form.get("state").toString()).intValue()){						
					}else insert = false;
				}
				if(insert){
					i_batch_log h_log = last_batch_log.get(batch.getCd_btch());
					if(h_log!=null)
						batch.setTm_last(h_log.getTm_fin());
					elements.add(batch);
				}

			}
			
			elements = Util_sort.sort(elements, new String[]{"ord","cd_btch "});
		}
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
		if(selected==null) 
			return new Boolean(false);
		db_batch original = null;

		for(db_batch batch:xml_batch){
			if(batch.getCd_ist().intValue()==selected.getCd_ist().intValue() && batch.getCd_btch().equals(selected.getCd_btch())){
				original = batch;
				break;
			}
		}
		
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

		

		return new Boolean(true);


	}
	
	private Object operation_UPDATE_STATE(HashMap<String,?> form) throws Exception{

		if(form==null) return new Boolean(false);

		db_batch batch = (db_batch)form.get("selected");
		Integer state = (Integer)form.get("state");
		if(batch==null || state==null)
			return new Boolean(false);
		
		db_batch original = null;

		for(db_batch batch_:xml_batch){
			if(batch_.getCd_ist().intValue()==batch.getCd_ist().intValue() && batch_.getCd_btch().equals(batch.getCd_btch())){
				original = batch;
				break;
			}
		}
		
		if(original==null)
			return false;



		batch.setState(state);

		if(
				(batch.getPeriod()!=null && !batch.getPeriod().equals("")) ||
				(batch.getCd_p_btch()!=null && !batch.getCd_p_btch().equals("")) ||
				(batch.getCls_btch()!=null && !batch.getCls_btch().equals(""))
			)
				original.reInit(batch);

		else{
				original.setState(batch.getState());
				original.setTm_next(null);
				original.setSt_exec(0);

		}
		return new Boolean(true);


	}	
	
	
	private Object operation_UPDATE_STATES_AND_NEXTEXEC(HashMap<String,?> form) throws Exception{

		if(form==null) return new Boolean(false);

		List<?> batch_updated = (List<?>)form.get("list");
		
		if(batch_updated==null || batch_updated.size()==0)
			return new Boolean(false);
		

		for(int i=0;i<batch_updated.size();i++){
			db_batch el = (db_batch)batch_updated.get(i);
			
			for(db_batch batch:xml_batch){
				if(batch.getCd_ist().intValue()==el.getCd_ist().intValue() && batch.getCd_btch().equals(el.getCd_btch())){
					batch.reInit(el);
					break;
				}
			}
		}
		
		
		
		return new Boolean(true);


	}		
	
	private Object operation_INSERT(HashMap<String,?> form) throws Exception{

		if(form==null) return new Boolean(false);

		db_batch inserted = (db_batch)form.get("inserted");
		if(inserted==null) return new Boolean(false);
		
		xml_batch.add(inserted);
		return new Boolean(true);
	}
	


	private Object operation_CLEAR_STATE(HashMap<String,?> form) throws Exception{

		if(form==null) return new Boolean(false);

		for(db_batch batch:xml_batch){
			boolean update = true;
			if(form.get("cd_ist")!=null && batch.getCd_ist()!=Integer.valueOf(form.get("cd_ist").toString()))
				update = false;
			
			if(batch.getCd_btch().equals(form.get("cd_btch").toString()) && batch.getState() != i_batch.STATE_SUSPEND){				
			}else update = false;
			
			if(update)
				batch.setState(Integer.valueOf(i_batch.STATE_NORMAL));
			

		}

		return new Boolean(true);
	}


	private Object operation_DELETELOG(HashMap<String,?> form) throws Exception{

		if(form.get("cd_btch")==null)
			return new Boolean(false);
		
		for (Iterator<i_batch_log> iter = xml_batch_log.listIterator(); iter.hasNext(); ) {
			i_batch_log log = iter.next();
		    if (log.getCd_btch().equals(form.get("cd_btch").toString())) {
		        iter.remove();
		    }
		}
		return new Boolean(true);
	}

	private Object operation_DELETEBATCH(HashMap<String,?> form) throws Exception{
		
		if(form.get("cd_btch")==null)
			return new Boolean(false);
		
		for (Iterator<db_batch> iter = xml_batch.listIterator(); iter.hasNext(); ) {
			db_batch batch = iter.next();
			boolean delete = true;
			
			if(form.get("cd_ist")!=null && batch.getCd_ist()!=Integer.valueOf(form.get("cd_ist").toString()))
				delete=false;
			
			if((batch.getCd_btch().equals(form.get("cd_btch").toString()) || batch.getCd_p_btch().equals(form.get("cd_btch").toString())) && batch.getState()!=i_batch.STATE_SUSPEND){
			}else delete = false;
			
		    if (delete) {
		        iter.remove();
		        return new Boolean(true);
		    }else
		    	return new Boolean(false);
		}
		return new Boolean(true);

	}
	
	private Object operation_CLEAR_BATCH_STATES(HashMap<String,?> form) throws Exception{

		if(form==null) return new Boolean(false);

		for(db_batch batch:xml_batch){
			batch.setState(0);
			batch.setSt_exec(0);
			batch.setTm_next(null);
		}
		return new Boolean(true);
	}
	
	private Object operation_KILL4TIMEOUT(HashMap<String,?> form) throws Exception{

		if(form==null) return new Boolean(false);

		for(db_batch batch:xml_batch){
			if(batch.getState().shortValue()==i_batch.STATE_INEXEC && batch.getTm_next()!=null){
				db_batch_property found = null;
				for(db_batch_property property:xml_batch_property){
					if(	property.getCd_btch().equals(batch.getCd_btch()) &&
						property.getCd_ist().shortValue()==batch.getCd_ist().shortValue() &&
						property.getCd_property().equalsIgnoreCase(i_4Batch.o_KILL4TIMEOUT)){
						found = property;
						break;
					}
				}
				if(found!=null){
					try{
						long tmout = Long.valueOf(found.getValue());
						if(tmout>0 && (new Date().getTime() - batch.getTm_next().getTime())/60000>tmout)
							batch.setState(Integer.valueOf(i_batch.STATE_NORMAL));
					}catch(Exception e){						
					}
				}
			}
		}

		return new Boolean(true);

	}	
	
	private Object operation_WRITE_LOG(HashMap<String,?> form) throws Exception{

		if(form==null) return new Boolean(false);

		i_batch_log log = (i_batch_log)form.get("selected");
		if(log==null)
			return new Boolean(false);
		
		xml_batch_log.add(log);
		if(xml_batch_log.size()>max_log_size)
			xml_batch_log.remove(0);
		i_batch_log h_log = last_batch_log.get(log.getCd_btch());
		if(h_log==null || (h_log!=null && h_log.getTm_fin().getTime()<log.getTm_fin().getTime()))
			last_batch_log.put(log.getCd_btch(),log);	

		return new Boolean(true);
	}		
	
	
	public Object operation_LOAD_BATCH_PROPERTIES(HashMap<String,?> form){

	    db_batch batch = (db_batch)form.get("selected");
	    if(batch==null)
			return new Boolean(false);
	    
		Properties property = new Properties();
		
		for(db_batch_property prop:xml_batch_property){
			if(prop.getCd_ist().shortValue()==batch.getCd_ist().shortValue() && prop.getCd_btch().equals(batch.getCd_btch())){
				String key = prop.getCd_property();
				String value = prop.getValue();
				if(key!=null && value!=null){
					property.put(key, value);
				}				
			}
		}
		return property;
	}
	
	public Object operation_DELETE_BATCH_PROPERTIES(HashMap<String,?> form){

	    db_batch batch = (db_batch)form.get("selected");
	    if(batch==null)
			return new Boolean(false);
	    
		Properties property = new Properties();
		
		Iterator<db_batch_property> it = xml_batch_property.iterator();
		while (it.hasNext()) {
			db_batch_property prop = it.next();
			if(prop.getCd_ist().shortValue()==batch.getCd_ist().shortValue() && prop.getCd_btch().equals(batch.getCd_btch())){
				it.remove();				
			}
		}
		return property;
	}	
	
	public Object operation_INSERT_BATCH_PROPERTY(HashMap<String,?> form){

	    db_batch_property property = (db_batch_property)form.get("selected_property");
	    if(property==null)
			return new Boolean(false);
	    
	    if(xml_batch_property==null)
	    	xml_batch_property = new ArrayList<db_batch_property>();
	    
	    xml_batch_property.add(property);	
	    
	    return new Boolean(true);
	}	
}
