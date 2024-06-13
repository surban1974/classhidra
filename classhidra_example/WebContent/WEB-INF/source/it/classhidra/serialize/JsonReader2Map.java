package it.classhidra.serialize;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import it.classhidra.core.controller.i_bean;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.stubs.iStub;

public class JsonReader2Map implements JsonMapper{
	@SuppressWarnings("unchecked")
	public Map<String,Object> mapping(i_bean bean, String json, Map<String,Object> table) {
		if(table==null)
			table = new HashMap<String,Object>();			
		if(json==null)
			return table;
		try{
			Map<String,Object> mapped = null;
			
//			Object obj = util_json_parser.decode(json);
			JsonValue value = Json.parse(json);
			Object obj = createMap(value);
			
			if(obj!=null && obj instanceof Map)
				mapped = (Map<String,Object>)obj;
			else if(obj!=null && obj instanceof List){
				mapped = new HashMap<String, Object>();
				for(int i=0;i<((List<?>)obj).size();i++){
					if(((List<?>)obj).get(i) instanceof Map)
					mapped.putAll((Map<String,Object>)((List<?>)obj).get(i));
				}
			}
			
			if(mapped!=null && mapped.size()>0){
				Map<String, Object> root = null;
				
				String modelname = (String)mapped.get("modelname");
				if(modelname==null && table!=null)
					modelname = (String)table.get("modelname");
				String rootName = null;
				if(modelname!=null){
					root = (mapped.get(modelname)!=null && mapped.get(modelname) instanceof Map<?,?>)?(Map<String,Object>)mapped.get(modelname):null;
					rootName = modelname;
				}
				else if(bean!=null && bean.get_infobean()!=null && bean.get_infobean().getName()!=null && !bean.get_infobean().getName().equals("")){
					root = (mapped.get(bean.get_infobean().getName())!=null && mapped.get(bean.get_infobean().getName()) instanceof Map<?,?>)
							?
							(Map<String, Object>)mapped.get(bean.get_infobean().getName())
							:
							null;
					rootName = bean.get_infobean().getName();
				}else if(bean!=null && bean.get_infoaction()!=null && bean.get_infoaction().getName()!=null && !bean.get_infoaction().getName().equals("")){
					root = (mapped.get(bean.get_infoaction().getName())!=null && mapped.get(bean.get_infoaction().getName()) instanceof Map<?,?>)
							?
							(Map<String, Object>)mapped.get(bean.get_infoaction().getName())
							:
							null;
					rootName = bean.get_infoaction().getName();
				}
				
				if(root==null)
					root = mapped;

				
				Map<String, Object> cl_parameters = new HashMap<String, Object>();
				
				for (Object elem : root.keySet()) {
					String key = (String)elem;
					cl_parameters = recursive(key, root, cl_parameters, "");
				}
				
				if(rootName!=null && cl_parameters!=null){
					Map<String, Object> norm_parameters = new HashMap<String, Object>();
					Iterator<String> it = cl_parameters.keySet().iterator();
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
	
	public static Object createMap(JsonValue jsonValue) {
	    Map<String,Object> map = new HashMap<String, Object>();
	    if(jsonValue.isObject()) {
		    JsonObject jsonObj = jsonValue.asObject();
		    
	
		    List<String> names = jsonObj.names();
		    for (String name:names) {
		    	Object value = createMap(jsonObj.get(name));
	
		        if (map.containsKey(name)) {
		            Object os = map.get(name);
		            if (os instanceof List<?>) {
		            	@SuppressWarnings("unchecked")
						List<Object> list = (List<Object>)os;
		            	list.add(value);
		            }
		            else {
		                List<Object> objs = new ArrayList<Object>();
		                objs.add(os);
		                objs.add(value);
		                map.put(name, objs);
		            }
		        }
		        else 
	        		map.put(name, value);
		        
		    }
	    }else {
	    	if(jsonValue.isArray()) {
	    		JsonArray array = jsonValue.asArray();
	    		List<Object> list = new ArrayList<Object>();
	    		for(int i=0;i<array.size();i++) {
	    			list.add(createMap(array.get(i)));
	    		}
	    		return list;
	    	}else 
//	    		return jsonValue.toString();
    		if(jsonValue.isString())
	    		return jsonValue.asString();
	    	else if(jsonValue.isBoolean())
	    		return Boolean.valueOf(jsonValue.asBoolean());
	    	else if(jsonValue.isNull())
	    		return null;
	    	else if(jsonValue.isNumber()) {
	    		try {
	    			int number = Integer.parseInt(jsonValue.toString());
	    			return Integer.valueOf(number);
	    		}catch (Exception e) {
				}
	    		try {
	    			float number = Float.parseFloat(jsonValue.toString());
	    			return Float.valueOf(number);
	    		}catch (Exception e) {
				}	
	    		try {
	    			double number = Double.parseDouble(jsonValue.toString());
	    			return Double.valueOf(number);
	    		}catch (Exception e) {
				}	    		
	    		return null;
	    	}

	    	
	    }

	    return map;
	}		

	@SuppressWarnings("unchecked")
	private static Map<String, Object> recursive(String key, Map<String, Object> original, Map<String, Object> result, String prefix){
		if(original.get(key) instanceof Map){
			Map<String, Object> sub = (Map<String, Object>)original.get(key);
			for(Object elem : sub.keySet())
				result = recursive((String)elem, sub, result, ((prefix.equals(""))?"":prefix+".")+key);
		}else if(original.get(key) instanceof Collection<?>){
			Collection<Object> list = (Collection<Object>)original.get(key);
			int i=0;
			for(Object sub: list){
				if(sub instanceof Map<?,?>){
					for(Object elem : ((Map<String, Object>)sub).keySet())
						result = recursive((String)elem, (Map<String, Object>)sub, result, ((prefix.equals(""))?"":prefix+".")+key+"."+i);
				}else{
					Map<String,Object> lMap = new HashMap<String, Object>();
					String lKey=String.valueOf(i);
					if(!key.equals("item"))
						lKey=key+"."+lKey;
					lMap.put(lKey, sub);
					result = recursive(lKey, lMap, result, ((prefix.equals(""))?"":prefix));					
				}
				i++;
			}
		}else
			result.put(((prefix.equals(""))?"":prefix+".")+key, original.get(key).toString());
			
		return result;
	}	
}
