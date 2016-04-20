package it.classhidra.serialize;

import java.util.HashMap;
import java.util.Map;

import it.classhidra.core.controller.i_bean;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_json_parser;

public class JsonReader2Map implements JsonMapper{
	public Map mapping(i_bean bean, String json, Map table) {
		try{

			Map mapped = null;
			Object obj = util_json_parser.decode(json);
			
			if(obj!=null && obj instanceof Map)
				mapped = (Map)obj;
			
			if(mapped!=null && mapped.size()>0){
				Map root = null;
				
				String modelname = (String)mapped.get("modelname");
				if(modelname!=null)
					root = (mapped.get(modelname)!=null && mapped.get(modelname) instanceof Map)?(Map)mapped.get(modelname):null;
				else if(bean!=null && bean.get_infobean()!=null && bean.get_infobean().getName()!=null && !bean.get_infobean().getName().equals(""))
					root = (mapped.get(bean.get_infobean().getName())!=null && mapped.get(bean.get_infobean().getName()) instanceof Map)
							?
							(Map)mapped.get(bean.get_infobean().getName())
							:
							null;
				else if(bean!=null && bean.get_infoaction()!=null && bean.get_infoaction().getName()!=null && !bean.get_infoaction().getName().equals(""))
					root = (mapped.get(bean.get_infoaction().getName())!=null && mapped.get(bean.get_infoaction().getName()) instanceof Map)
							?
							(Map)mapped.get(bean.get_infoaction().getName())
							:
							null;
				
				if(root==null)
					root = mapped;
				
				Map cl_parameters = new HashMap();
				
				for (Object elem : root.keySet()) {
					String key = (String)elem;
					cl_parameters = recursive(key, root, cl_parameters, "");
				}
				
				table.put(JsonMapper.CONST_ID_CHECKFORDESERIALIZE, "true");
				table.putAll(cl_parameters);
			}
		}catch(Exception e){
			new bsControllerException(e, iStub.log_ERROR);
		}
		return table;
	}
	
	private Map recursive(String key, Map original, Map result, String prefix){
		if(original.get(key) instanceof Map){
			Map<String, Object> sub = (Map)original.get(key);
			for(Object elem : sub.keySet())
				result = recursive((String)elem, sub, result, ((prefix.equals(""))?"":prefix+".")+key);
		}else
			result.put(((prefix.equals(""))?"":prefix+".")+key, original.get(key).toString());
			
		return result;
	}
	
}
