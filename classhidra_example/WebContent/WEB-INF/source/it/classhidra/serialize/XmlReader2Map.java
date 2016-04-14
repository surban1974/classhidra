package it.classhidra.serialize;

import java.util.ArrayList;
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


	public Map mapping(i_bean bean, String xml, Map table) {
		if(xml==null)
			return table;
		if(table==null)
			table = new HashMap();
		try{			
			Object obj = createMap(util_xml.readXMLData(xml).getDocumentElement(),true);
			if(obj instanceof Map){
				
				HashMap mapped = (HashMap)obj;
				if(mapped!=null && mapped.size()>0){
					Map<String, Object> root = null;
					
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
					
					Map<String, Object> cl_parameters = new HashMap<String, Object>();
					
					for (Object elem : root.keySet()) {
						String key = (String)elem;
						cl_parameters = recursive(key, root, cl_parameters, "");
					}
					
					table.put(JsonMapper.CONST_ID_CHECKFORDESERIALIZE, "true");
					table.putAll(cl_parameters);
				}

			}
		}catch(Exception e){
			new bsControllerException(e, iStub.log_ERROR);
			return null;
		}
		return table;
	}

	private Map<String, Object> recursive(String key, Map<String, Object> original, Map<String, Object> result, String prefix){
		if(original.get(key) instanceof Map){
			Map<String, Object> sub = (Map<String, Object>)original.get(key);
			for(Object elem : sub.keySet())
				result = recursive((String)elem, sub, result, ((prefix.equals(""))?"":prefix+".")+key);
		}else
			result.put(((prefix.equals(""))?"":prefix+".")+key, original.get(key).toString());
			
		return result;
	}
	
	public static Object createMap(Node node, boolean first) {
	    Map map = new HashMap();
	    if(first){
	    	Node currentNode = node;
	        String name = currentNode.getNodeName();
	        Object value = null;
	        if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
	            value = createMap(currentNode,false);
	            map.put(name,value);
	            return map;
	        }
	        else if (currentNode.getNodeType() == Node.TEXT_NODE) {
	            return currentNode.getTextContent();
	        }
	        if (map.containsKey(name)) {
	            Object os = map.get(name);
	            if (os instanceof List) {
	                ((List)os).add(value);
	            }
	            else {
	                List<Object> objs = new ArrayList();
	                objs.add(os);
	                objs.add(value);
	                map.put(name, objs);
	            }
	        }
	        else {
	            map.put(name, value);
	        }
	    }else{
		    NodeList nodeList = node.getChildNodes();
		    for (int i = 0; i < nodeList.getLength(); i++) {
		        Node currentNode = nodeList.item(i);
		        String name = currentNode.getNodeName();
		        Object value = null;
		        if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
		            value = createMap(currentNode,false);
		        }
		        else if (currentNode.getNodeType() == Node.TEXT_NODE) {
		            return currentNode.getTextContent();
		        }
		        if (map.containsKey(name)) {
		            Object os = map.get(name);
		            if (os instanceof List) {
		                ((List)os).add(value);
		            }
		            else {
		                List<Object> objs = new ArrayList();
		                objs.add(os);
		                objs.add(value);
		                map.put(name, objs);
		            }
		        }
		        else {
		            map.put(name, value);
		        }
		    }
	    }
	    return map;
	}	
	
}
