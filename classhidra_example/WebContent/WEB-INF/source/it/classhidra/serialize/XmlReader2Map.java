package it.classhidra.serialize;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import it.classhidra.core.controller.i_bean;
import it.classhidra.core.tool.util.util_xml;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.stubs.iStub;

public class XmlReader2Map implements XmlMapper {


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String,Object> mapping(i_bean bean, String xml, Map<String,Object> table) {
		if(table==null)
			table = new HashMap<String,Object>();
		if(xml==null)
			return table;

		try{			
			Object obj = createMap(util_xml.readXMLData(xml).getDocumentElement(),true);
			if(obj instanceof Map){
				

				Map<String,Object> mapped = (Map<String, Object>)obj;
				if(mapped!=null && mapped.size()>0){
					Map<String, Object> root = null;
					
					String modelname = (String)mapped.get("modelname");
					if(modelname==null && table!=null)
						modelname = (String)table.get("modelname");
					if(modelname!=null)
						root = (mapped.get(modelname)!=null && mapped.get(modelname) instanceof Map<?,?>)?(Map)mapped.get(modelname):null;
					else if(bean!=null && bean.get_infobean()!=null && bean.get_infobean().getName()!=null && !bean.get_infobean().getName().equals(""))
						root = (mapped.get(bean.get_infobean().getName())!=null && mapped.get(bean.get_infobean().getName()) instanceof Map<?,?>)
								?
								(Map<String, Object>)mapped.get(bean.get_infobean().getName())
								:
								null;
					else if(bean!=null && bean.get_infoaction()!=null && bean.get_infoaction().getName()!=null && !bean.get_infoaction().getName().equals(""))
						root = (mapped.get(bean.get_infoaction().getName())!=null && mapped.get(bean.get_infoaction().getName()) instanceof Map<?,?>)
								?
								(Map<String, Object>)mapped.get(bean.get_infoaction().getName())
								:
								null;
					
					if(root==null)
						root = mapped;
					
					Map<String, Object> cl_parameters = new HashMap<String, Object>();
					
					for (Object elem : root.keySet()) {
						String key = (String)elem;
						cl_parameters = recursive(key, root, cl_parameters, "");
					}
					
					table.put(XmlMapper.CONST_ID_CHECKFORDESERIALIZE, "true");
					table.putAll(cl_parameters);
				}

			}
		}catch(Exception e){
			new bsControllerException(e, iStub.log_ERROR);
			return null;
		}
		return table;
	}

//	private static Map<String, Object> recursive_(String key, Map<String, Object> original, Map<String, Object> result, String prefix){
//		if(original.get(key) instanceof Map){
//			@SuppressWarnings("unchecked")
//			Map<String, Object> sub = (Map<String, Object>)original.get(key);
//			for(Object elem : sub.keySet())
//				result = recursive((String)elem, sub, result, ((prefix.equals(""))?"":prefix+".")+key);
//		}else if(original.get(key) instanceof Collection) {
//			Collection<?> sub = (Collection<?>)original.get(key);
//			int i=0;
//			for(Object item:sub) {
//				Map<String,Object> lMap = new HashMap<String, Object>();
//				String lKey=String.valueOf(i);
//				if(!key.equals("item"))
//					lKey=key+"."+lKey;
//				lMap.put(lKey, item);
//				result = recursive(lKey, lMap, result, ((prefix.equals(""))?"":prefix));
//				i++;
//			}
//		}else
//			result.put(((prefix.equals(""))?"":prefix+".")+key, original.get(key).toString());
//			
//		return result;
//	}
	
	@SuppressWarnings("unchecked")
	private static Map<String, Object> recursive(String key, Map<String, Object> original, Map<String, Object> result, String prefix){
		if(original.get(key) instanceof Map){
			Map<String, Object> sub = (Map<String, Object>)original.get(key);
			for(Object elem : sub.keySet())
				result = recursive((String)elem, sub, result, ((prefix.equals(""))?"":prefix+".")+key);
		}else if(original.get(key) instanceof Collection<?>){
			Collection<?> list = (Collection<?>)original.get(key);
			int i=0;
			for(Object sub: list){
				if(sub instanceof Map<?,?>){
					for(Object elem : ((Map<String, Object>)sub).keySet()) {
						String lKey=String.valueOf(i);
						if(!key.equals("item"))
							lKey=key+"."+lKey;
						result = recursive((String)elem, (Map<String, Object>)sub, result, ((prefix.equals(""))?"":prefix+".")+lKey);
					}
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
	
	public static Object createMap(Node node, boolean first) {
	    Map<String,Object> map = new HashMap<String, Object>();
	    
	    if(first){
	    	boolean textNode=false;
	    	Node currentNode = node;
	        String name = currentNode.getNodeName();
	        Object value = null;
	        if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
	            value = createMap(currentNode,false);
	            map.put(name,value);
	            return map;
	        }
	        else if (currentNode.getNodeType() == Node.TEXT_NODE) {
//	        	if(nodeList.getLength()>1)
//	        		textNode = true;
//	        	else
	        	return currentNode.getTextContent();
	        }
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
	        else {
	        	if(!textNode)
	        		map.put(name, value);
	        }
	    }else{
		    NodeList nodeList = node.getChildNodes();
		    for (int i = 0; i < nodeList.getLength(); i++) {
		    	boolean textNode=false;
		        Node currentNode = nodeList.item(i);
		        String name = currentNode.getNodeName();
		        Object value = null;
		        if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
		            value = createMap(currentNode,false);
		        }
		        else if (currentNode.getNodeType() == Node.TEXT_NODE) {
		        	if(nodeList.getLength()>1)
		        		textNode = true;
		        	else
		        		return currentNode.getTextContent();
		        }
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
		        else {
		        	if(!textNode)
		        		map.put(name, value);
		        }
		    }
	    }
	    return map;
	}	
	
}
