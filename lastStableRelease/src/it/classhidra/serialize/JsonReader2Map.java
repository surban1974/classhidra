package it.classhidra.serialize;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import it.classhidra.core.controller.i_bean;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_json_parser;

public class JsonReader2Map implements JsonMapper{
	public Map mapping(i_bean bean, String json, Map table) {
		if(table==null)
			table = new HashMap();			
		if(json==null)
			return table;
		try{
			Map mapped = null;
			Object obj = util_json_parser.decode(json);
			
			if(obj!=null && obj instanceof Map)
				mapped = (Map)obj;
			else if(obj!=null && obj instanceof List){
				mapped = new HashMap();
				for(int i=0;i<((List)obj).size();i++){
					if(((List)obj).get(i) instanceof Map)
					mapped.putAll((Map)((List)obj).get(i));
				}
			}
			
			if(mapped!=null && mapped.size()>0){
				Map root = null;
				
				String modelname = (String)mapped.get("modelname");
				String rootName = null;
				if(modelname!=null){
					root = (mapped.get(modelname)!=null && mapped.get(modelname) instanceof Map)?(Map)mapped.get(modelname):null;
					rootName = modelname;
				}
				else if(bean!=null && bean.get_infobean()!=null && bean.get_infobean().getName()!=null && !bean.get_infobean().getName().equals("")){
					root = (mapped.get(bean.get_infobean().getName())!=null && mapped.get(bean.get_infobean().getName()) instanceof Map)
							?
							(Map)mapped.get(bean.get_infobean().getName())
							:
							null;
					rootName = bean.get_infobean().getName();
				}else if(bean!=null && bean.get_infoaction()!=null && bean.get_infoaction().getName()!=null && !bean.get_infoaction().getName().equals("")){
					root = (mapped.get(bean.get_infoaction().getName())!=null && mapped.get(bean.get_infoaction().getName()) instanceof Map)
							?
							(Map)mapped.get(bean.get_infoaction().getName())
							:
							null;
					rootName = bean.get_infoaction().getName();
				}
				
				if(root==null)
					root = mapped;

				
				Map cl_parameters = new HashMap();
				
				for (Object elem : root.keySet()) {
					String key = (String)elem;
					cl_parameters = recursive(key, root, cl_parameters, "");
				}
				
				if(rootName!=null && cl_parameters!=null){
					Map norm_parameters = new HashMap();
					Iterator it = cl_parameters.keySet().iterator();
					while(it.hasNext()){
						String key = it.next().toString();
						if(key.startsWith(rootName+"."))
							norm_parameters.put(key.substring((rootName+".").length(), key.length()), cl_parameters.get(key));
						else
							norm_parameters.put(key, cl_parameters.get(key));
						table.put(JsonMapper.CONST_ID_CHECKFORDESERIALIZE, "true");
						table.putAll(norm_parameters);						
					}
				}else{				
					table.put(JsonMapper.CONST_ID_CHECKFORDESERIALIZE, "true");
					table.putAll(cl_parameters);
				}
			}
		}catch(Exception e){
			new bsControllerException(e, iStub.log_ERROR);
		}
		return table;
	}
/*	
	private static Map recursive(String key, Map original, Map result, String prefix){
		if(original.get(key) instanceof Map){
			Map<String, Object> sub = (Map)original.get(key);
			for(Object elem : sub.keySet())
				result = recursive((String)elem, sub, result, ((prefix.equals(""))?"":prefix+".")+key);
		}else
			result.put(((prefix.equals(""))?"":prefix+".")+key, original.get(key).toString());
			
		return result;
	}
*/	
	private Map recursive(String key, Map original, Map result, String prefix){
		if(original.get(key) instanceof Map){
			Map<String, Object> sub = (Map)original.get(key);
			for(Object elem : sub.keySet())
				result = recursive((String)elem, sub, result, ((prefix.equals(""))?"":prefix+".")+key);
		}else if(original.get(key) instanceof Collection){
			Collection list = (Collection)original.get(key);
			int i=0;
			for(Object sub: list){
				if(sub instanceof Map){
					for(Object elem : ((Map)sub).keySet())
						result = recursive((String)elem, (Map)sub, result, ((prefix.equals(""))?"":prefix+".")+key+"."+i);
				}
				i++;
			}
		}else
			result.put(((prefix.equals(""))?"":prefix+".")+key, original.get(key).toString());
			
		return result;
	}	
}
