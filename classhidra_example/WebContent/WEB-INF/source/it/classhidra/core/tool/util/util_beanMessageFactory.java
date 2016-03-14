package it.classhidra.core.tool.util;


import it.classhidra.core.controller.i_bean;
import it.classhidra.core.tool.elements.i_elementBase;
import it.classhidra.core.tool.elements.i_elementDBBase;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.Locale;



import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;



public class util_beanMessageFactory {




	public static Object message2bean(String xml) throws Exception{
		if (xml==null) return null;
		else return message2bean(xml.getBytes());
	}

	public static Object message2bean(String xml, String charset) throws Exception{
		if (xml==null) return null;
		else return message2bean(xml.getBytes(charset));
	}

	public static Object message2bean(byte[] xml_byte) throws Exception{
		return message2bean(xml_byte,null);
	}

	public static Object message2bean(byte[] xml_byte, Class itemClass) throws Exception{
		Object result=null;


		Document documentXML = null;
		if (xml_byte==null) return result;
			ByteArrayInputStream xmlSrcStream = new	ByteArrayInputStream(xml_byte);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				dbf.setValidating(false);
				documentXML = dbf.newDocumentBuilder().parse(xmlSrcStream);
			Node node = null;
			try {
				int first=0;
				while (node==null && first < documentXML.getChildNodes().getLength()) {
					if (documentXML.getChildNodes().item(first).getNodeType() == Node.ELEMENT_NODE)
						node = documentXML.getChildNodes().item(first);
					first++;
				}
			}
			catch (Exception e) {}
			if (node==null) return result;




		result=generateObject(node,itemClass);
		return result;
	}


	public static String bean2xml(Object obj){
		return bean2xml(obj,null,false);
	}
	
	public static String bean2xml(Object obj, String name, boolean lowerCase1char){
		return bean2xml(obj,null,false,false,null);
	}
	
	public static String bean2xml(Object obj, String name, boolean lowerCase1char, boolean avoidCheckPermission){
		return bean2xml(obj, name, lowerCase1char, avoidCheckPermission, null);
	}
	
	public static String bean2xml(Object obj, String name, boolean lowerCase1char, boolean avoidCheckPermission, String textencoding){
		Map avoidCyclicPointers = new HashMap();
		
		boolean showInXml = true;
		if(!avoidCheckPermission){
			try{
				showInXml = ((Boolean)util_reflect.getValue(obj, "convert2xml", null)).booleanValue();
			}catch(Exception e){
			}		
		}
		
		String result="";
		if(obj==null)
			result+="<error>Object is NULL</error>";
		else{
			if(showInXml)
				result+=generateXmlItem(obj,name,0,false,lowerCase1char,avoidCyclicPointers,textencoding);
			else
				result+="<error>Object ["+obj.getClass().getName()+"] does not contain the method [convert2xml] or this method return [false]</error>";
		}
		return result;
	}
	
	public static String bean2json(Object obj){
		return bean2json(obj,null,false,null);
	}
	
	public static String bean2json(Object obj, String name){
		return bean2json(obj,name,false,null);
	}
	
	public static String bean2json(Object obj, String name, boolean avoidCheckPermission){
		return bean2json(obj,name,avoidCheckPermission,null);
	}	

	public static String bean2json(Object obj, String name, boolean avoidCheckPermission, String textencoding){
		Map avoidCyclicPointers = new HashMap();		
		
		boolean showInJson = true;
		if(!avoidCheckPermission){
			try{
				showInJson = ((Boolean)util_reflect.getValue(obj, "convert2json", null)).booleanValue();
			}catch(Exception e){
			}	
		}
		
		String result="{\n";
		if(obj==null)
			result+="\"error\":  \"Object is NULL\"";
		else{
			if(showInJson)
				result+=generateJsonItem(obj,name,0,false,avoidCyclicPointers,textencoding);
			else
				result+="\"error\":  \"Object ["+obj.getClass().getName()+"] does not contain the method [convert2json] or this method return [false]\"";
		}
		
		return result+"\n}";
	}	
	

	public static String bean2message(Object obj, String name, boolean lowerCase1char){
		return bean2message(obj, name, lowerCase1char, null);
	}

	public static String bean2message(Object obj, String name, boolean lowerCase1char,String textencoding){
		Map avoidCyclicPointers = new HashMap();
		String result="";
		result+=generateXmlItem(obj,name,0,false,lowerCase1char,avoidCyclicPointers,textencoding);
		return result;
	}
	
	public static String bean2message(Object obj){
		return bean2message(obj,null,false,null);
	}


	public static String bean2messageNormalized(Object obj, String name, boolean lowerCase1char){
		return bean2messageNormalized(obj, name, lowerCase1char,null);
	}
	
	public static String bean2messageNormalized(Object obj, String name, boolean lowerCase1char,String textencoding){
		Map avoidCyclicPointers = new HashMap();
		String result="";
		result+=generateXmlItem(obj,name,0, true,lowerCase1char,avoidCyclicPointers,textencoding);
		return result;
	}
	public static String bean2messageNormalized(Object obj){
		return bean2messageNormalized(obj,null,false);
	}



	private static Object generateObject(Node node, Class itemClass){
		Object result=null;

		if(node==null) return null;

		Object itemObj = null;


		if(itemClass==null){
			String type=getNodeAttr(node, "type");

			if(type.equals("int")) itemClass = Integer.class;
			if(type.equals("short")) itemClass =  Short.class;
			if(type.equals("long")) itemClass =  Long.class;
			if(type.equals("float")) itemClass =  Float.class;
			if(type.equals("double")) itemClass =  Double.class;
			if(type.equals("byte")) itemClass =  Byte.class;
			if(type.equals("boolean")) itemClass =  Boolean.class;
			if(type.equals("char")) itemClass =  Character.class;



			if(type.equals("") && node.getNodeName().equals("items")) type="java.util.Vector";
			if(type.equals("") && node.getNodeName().equals("item")) type="java.lang.String";


			if(itemObj==null && itemClass==null){
				try{
					itemClass = Class.forName(type);
					itemObj = itemClass.newInstance();
				}catch(Exception e){
				}
			}

			if(itemObj==null ){
				try{
					itemClass = Object.class;
					itemObj = itemClass.newInstance();
				}catch(Exception e){
				}
			}


			if(itemClass==null) return null;
	//		if(itemObj==null && !itemClass.isPrimitive()) return null;

		}else{
			try{
				itemObj = itemClass.newInstance();
			}catch(Exception e){
			}
		}


		if(node.getNodeName().equals("item")){
			if(itemObj==null || (itemObj!=null && itemObj instanceof String)){
				if(	itemClass.isPrimitive() ||
					checkSuperClass(itemClass,String.class)	||
					checkSuperClass(itemClass,Number.class)	||
					checkSuperClass(itemClass,Boolean.class)){


					try{
						String valueTxt = "";
						valueTxt = ((Text)node.getFirstChild()).getData();

						if(checkSuperClass(itemClass,Number.class))
							valueTxt=adaptValueForNumber(valueTxt);

						Object prm[] = new Object[]{valueTxt};
						Class[] cls = new Class[]{String.class};
						result =  itemClass.getConstructor(cls).newInstance(prm);
						return result;
					}catch (Exception e) {
					}

				}

				if(	checkSuperClass(itemClass,Date.class)){

						try{
							String valueTxt = "";
							valueTxt = ((Text)node.getFirstChild()).getData();
							long time = util_format.stringToTimestamp(valueTxt).getTime();

							Object prm[] = new Object[]{Long.valueOf(time)};
							Class[] cls = new Class[]{Long.class};
							result =  itemClass.getConstructor(cls).newInstance(prm);
							return result;
						}catch (Exception e) {
						}

				}
			}


			if(itemObj!=null){
				for (int i=0;i<node.getChildNodes().getLength();i++) {
					if (node.getChildNodes().item(i).getNodeType() == Node.ELEMENT_NODE){
						String subitemName = getNodeAttr(node.getChildNodes().item(i),"name");
						Class subitemClass = null;

						try{
							subitemClass = util_reflect.getValue(itemObj, "get" + util_reflect.adaptMethodName(subitemName),null).getClass();
						}catch(Exception e){
							try{
								String subitemType = getNodeAttr(node.getChildNodes().item(i),"type");
								subitemClass = Class.forName(subitemType);
							}catch(Exception ex){
							}
						}

						Object result_sub_node = generateObject(node.getChildNodes().item(i),subitemClass);
						try{
							if(!util_reflect.setValue(itemObj, "set" + util_reflect.adaptMethodName(subitemName), new Object[]{result_sub_node},false)){
								if(itemObj instanceof i_bean){
									((i_bean)itemObj).put(subitemName, result_sub_node);
								}
								if(itemObj instanceof Map){
									((Map)itemObj).put(subitemName, result_sub_node);
								}

							}
						}catch(Exception e){

						}
					}
					if (node.getChildNodes().item(i).getNodeType() == Node.CDATA_SECTION_NODE){
						String valueB64 = ((Text)node.getFirstChild()).getData();
						try{
							byte[] byteAsObj = new BASE64Decoder().decodeBuffer(valueB64);
							itemObj = bytes2object(byteAsObj);
						}catch(Exception e){
						}
					}
				}
			}
			result = itemObj;
			return result;
		}


		if(node.getNodeName().equals("items")){
			if(itemObj!=null){
				for (int i=0;i<node.getChildNodes().getLength();i++) {
					if (node.getChildNodes().item(i).getNodeType() == Node.ELEMENT_NODE){
						Object result_sub_node = generateObject(node.getChildNodes().item(i),null);
						((List)itemObj).add(result_sub_node);
					}
				}
				result = itemObj;
				return result;
			}
		}

		return result;
	}




	private static String adaptValueForNumber(String value){

		if(value==null) return "0";
		if(value.indexOf('.')==-1) return value;
		boolean found=false;
		int i=value.length()-1;
		while(!found && i>0){
			if(value.charAt(i)=='0' && value.charAt(i-1)!='0') found=true;
			i--;
		}
		if(value.charAt(i)=='.') i--;
		if(i>=0) value = value.substring(0,i+1);
		return value;
	}

	private static boolean checkSuperClass(Class current, Class forCheck){
		boolean result=false;
		Class parent = current;
		while(parent!=null ){
			if(parent.equals(forCheck)) return true;
			else parent = parent.getSuperclass();
		}

		return result;
	}

	private static String getNodeAttr(Node node, String attr){
		String result="";
		NamedNodeMap nnm = node.getAttributes();
		if (nnm!=null){
			for (int i=0;i<node.getAttributes().getLength();i++){
				String paramName = node.getAttributes().item(i).getNodeName();
				Node node_nnm =	nnm.getNamedItem(paramName);
				if (node_nnm!=null){
					if(paramName.equals(attr)) result = node_nnm.getNodeValue();
				}
			}
		}
		return result;
	}

	private static String generateJsonItem(Object sub_obj, String name, int level, boolean notFirst, Map avoidCyclicPointers, String textencoding ){
		String result="";
		result+=generateJsonItemTag_Start(sub_obj, name,level, notFirst);
		result+=generateJsonItemTag_Content(sub_obj, name,level,avoidCyclicPointers, textencoding);
		result+=generateJsonItemTag_Finish(sub_obj, name, level, notFirst);		
		return result;
	}

	private static String generateJsonItemTag_Start(Object sub_obj, String name, int level, boolean notFirst){
		if(sub_obj==null || (name!=null && name.equals("Class"))) return "";
		String space=spaceLevel(level);
		String result="";

		
		if(	sub_obj instanceof i_bean ||
			sub_obj instanceof i_elementDBBase ||
			sub_obj instanceof i_elementBase){
			if(name==null){
				if(notFirst) result+=",\n";
				result+=space+"{\n";
			}else{
				if(notFirst) result+=",\n";
			}
		}else{
			if(notFirst) result+=",\n";
			result+="";
		}

		if(name!=null){
			result+=space+"\""+util_reflect.revAdaptMethodName(name)+"\":";
			if(	sub_obj instanceof i_bean ||
					sub_obj instanceof i_elementDBBase ||
					sub_obj instanceof i_elementBase){
				result+="{\n";				
			}
		}
		
		if(sub_obj instanceof List ){
			if(notFirst){
				result+="\n";
				result+=space+"[\n";
			}else result+="[\n";
			
			return result;
		}
		
		if(sub_obj instanceof Map){
			if(notFirst){
				result+="\n";
				result+=space+"{\n";
			}else result+="{\n";
			
			return result;
		}

		
		
		


		return result;
	}
	
	
	private static String generateJsonItemTag_Content(Object sub_obj, String name, int level, Map avoidCyclicPointers, String textencoding){
		if(sub_obj==null || (name!=null && name.equals("Class"))) return "";
		
		String result="";
		if(sub_obj==null) return result;

		if(sub_obj instanceof List){
			String result_tmp="";
			List list_sub_obj = (List)sub_obj;
			for(int i=0;i<list_sub_obj.size();i++){
				boolean nFirst = true;
				if(result_tmp.length()==0) nFirst=false;
				
				Object sub_obj2=list_sub_obj.get(i);
				if(sub_obj2!=null){								
					if(avoidCyclicPointers.get(Integer.valueOf(System.identityHashCode(sub_obj2)))!=null){
						result_tmp+=generateJsonItemTag_Start(new Object(), null,level+1, nFirst);
						result_tmp+="\"WARNING: cyclic pointer\"";
						result_tmp+=generateJsonItemTag_Finish(new Object(), null,level+1, nFirst);
					}else{
						avoidCyclicPointers.put(Integer.valueOf(System.identityHashCode(sub_obj2)), sub_obj2.getClass().getName());
						result_tmp+=generateJsonItem(sub_obj2, null,level+1,nFirst,avoidCyclicPointers,textencoding);
						avoidCyclicPointers.remove(Integer.valueOf(System.identityHashCode(sub_obj2)));									
					}
				}else
					result_tmp+=generateJsonItem(sub_obj2, null,level+1,nFirst,avoidCyclicPointers,textencoding);

				
//				result_tmp+=generateJsonItem(list_sub_obj.get(i),null,level+1,nFirst,avoidCyclicPointers);
				
			}
			return result+result_tmp;
		}

		if(sub_obj instanceof Map){
			String result_tmp="";
			
			Iterator it = ((Map)sub_obj).entrySet().iterator(); 
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
				boolean nFirst = true;
				if(result_tmp.length()==0) nFirst=false;
				
				Object sub_obj2=pair.getValue();
				if(sub_obj2!=null){								
					if(avoidCyclicPointers.get(Integer.valueOf(System.identityHashCode(sub_obj2)))!=null){
						result_tmp+=generateJsonItemTag_Start(new Object(), pair.getKey().toString(),level+1, nFirst);
						result_tmp+="\"WARNING: cyclic pointer\"";
						result_tmp+=generateJsonItemTag_Finish(new Object(), pair.getKey().toString(),level+1, nFirst);
					}else{
						avoidCyclicPointers.put(Integer.valueOf(System.identityHashCode(sub_obj2)), sub_obj2.getClass().getName());
						result_tmp+=generateJsonItem(sub_obj2, pair.getKey().toString(),level+1,nFirst,avoidCyclicPointers,textencoding);
						avoidCyclicPointers.remove(Integer.valueOf(System.identityHashCode(sub_obj2)));									
					}
				}else
					result_tmp+=generateJsonItem(sub_obj2, pair.getKey().toString(),level+1,nFirst,avoidCyclicPointers,textencoding);
				
				
//				result_tmp+=generateJsonItem(pair.getValue(),pair.getKey().toString(),level+1,nFirst,avoidCyclicPointers);
							        
		    }
/*			
			List list_sub_obj = new Vector(((Map)sub_obj).values());
			List names_sub_obj = new Vector(((Map)sub_obj).keySet());
			for(int i=0;i<list_sub_obj.size();i++){
				boolean nFirst = true;
				if(result_tmp.length()==0) nFirst=false;
				result_tmp+=generateJsonItem(list_sub_obj.get(i),names_sub_obj.get(i).toString(),level+1,nFirst);
			}
*/			
			return result+result_tmp;
		}


		if(	sub_obj.getClass().isPrimitive() ||
			sub_obj instanceof String ||
			sub_obj instanceof Number ||
			sub_obj instanceof Date ||
			sub_obj instanceof Boolean){
			String value=sub_obj.toString();
			boolean check=false;
			if(sub_obj instanceof String){
				check=true;
				result+="\""+normalJSON(value,textencoding)+"\"";
				return result;
			}
			if(sub_obj instanceof Boolean){
				check=true;
				result+=normalJSON(value,textencoding);
				return result;
			}
			
			if(sub_obj instanceof Number){
				check=true;
				try{
					java.text.DecimalFormat df = new java.text.DecimalFormat("##0.000000", new DecimalFormatSymbols(new Locale("en")));
					result+= df.format(new java.math.BigDecimal(value.trim()).doubleValue());
				}catch(Exception e){}
				return result;
			}
			if(sub_obj instanceof Date){
				check=true;
				java.sql.Timestamp  tstmp = new java.sql.Timestamp(((Date)sub_obj).getTime());
				result+="\""+ util_format.timestampToString(tstmp)+"\"";
				return result;
			}
			if(!check){
				try{
					java.text.DecimalFormat df = new java.text.DecimalFormat("##0.000000", new DecimalFormatSymbols(new Locale("en")));
					result+= df.format(new java.math.BigDecimal(value.trim()).doubleValue());
					return result;
				}catch(Exception e){
					result+=normalJSON(value,textencoding);
					return result;
				}
			}

		}

		
		if(	sub_obj instanceof i_bean ||
			sub_obj instanceof i_elementDBBase || 
			sub_obj instanceof i_elementBase){
			String result_tmp="";
			try{
				Method[] methods = util_reflect.getMethods(sub_obj,"get");
				for(int i=0;i<methods.length;i++){
					String methodName = methods[i].getName().substring(3);
					Object sub_obj2 = util_reflect.getValue(sub_obj, "get"+util_reflect.adaptMethodName(methodName), null);
					if(sub_obj2!=null){
						if(sub_obj2.equals(sub_obj)){
						}else{
							boolean nFirst = true;
							if(result_tmp.length()==0) nFirst=false;
							
							if(sub_obj2!=null){								
								if(avoidCyclicPointers.get(Integer.valueOf(System.identityHashCode(sub_obj2)))!=null){
									result_tmp+=generateJsonItemTag_Start(new Object(), methodName,level+1, nFirst);
									result_tmp+="\"WARNING: cyclic pointer\"";
									result_tmp+=generateJsonItemTag_Finish(new Object(), methodName,level+1, nFirst);
								}else{
									avoidCyclicPointers.put(Integer.valueOf(System.identityHashCode(sub_obj2)), sub_obj2.getClass().getName());
									result_tmp+=generateJsonItem(sub_obj2, methodName,level+1,nFirst,avoidCyclicPointers,textencoding);
									avoidCyclicPointers.remove(Integer.valueOf(System.identityHashCode(sub_obj2)));									
								}
							}else
								result_tmp+=generateJsonItem(sub_obj2, methodName,level+1,nFirst,avoidCyclicPointers,textencoding);
								
						}
					}
				}
			}catch(Exception e){
			}
			return result+result_tmp;
		}
		result="\"WARNING: unknown\""; 


		return result;
	}

	private static String generateJsonItemTag_Finish(Object sub_obj, String name, int level, boolean notFirst){
		if(sub_obj==null || (name!=null && name.equals("Class"))) return "";
		String space=spaceLevel(level);
		String result="";


		if(sub_obj instanceof List ){
			result+="\n"+space+"]";
		}
		if(sub_obj instanceof Map){
			result+="\n"+space+"}";
		}
		
		
		if(	sub_obj instanceof i_bean ||
				sub_obj instanceof i_elementDBBase ||
				sub_obj instanceof i_elementBase){
				result+="\n"+space+"}";
			}

		return result;
	}	
	
	
	
	
	
	
	private static String generateXmlItem(Object sub_obj, String name, int level, boolean normalized, boolean lowerCase1char,Map avoidCyclicPointer,String textencoding){
		String result="";
		result+=generateXmlItemTag_Start(sub_obj, name,level,normalized,lowerCase1char);
		result+=generateXmlItemTag_Content(sub_obj, name,level,normalized,lowerCase1char,avoidCyclicPointer,textencoding);
		result+=generateXmlItemTag_Finish(sub_obj, name, level,normalized,lowerCase1char);
		return result;
	}

	private static String generateXmlItemTag_Start(Object sub_obj, String name, int level, boolean normalized, boolean lowerCase1char){
		if(sub_obj==null || (name!=null && name.equals("Class"))) return "";
		String result=spaceLevel(level);
		if(normalized){
			if(name!=null) result+="<"+name;
			else{
				if(	sub_obj instanceof List) result+="<items";
				else result+="<item";
			}
			result+=">";
		}else{
			if(	sub_obj instanceof List) result+="<items";
			else result+="<item";
			if(name!=null){
				result+=" name=\"";
				if(lowerCase1char) result+=util_reflect.revAdaptMethodName(name);
				else result+=name;
				result+="\"";
			}
			result+=" type=\"";
			result+=sub_obj.getClass().getName();
			result+="\"";
			if(sub_obj.getClass().isPrimitive() ) result+=" primitive=\"true\"";


			result+=">";
		}
		if(sub_obj instanceof List || sub_obj instanceof Map){
			result+="\n";
			return result;
		}
		if(	sub_obj instanceof i_bean ||
			sub_obj instanceof i_elementDBBase ||
			sub_obj instanceof i_elementBase){
			result+="\n";
			return result;
		}

		return result;
	}

	private static String generateXmlItemTag_Content(Object sub_obj, String name, int level, boolean normalized, boolean lowerCase1char, Map avoidCyclicPointers, String textencoding){
		if(sub_obj==null || (name!=null && name.equals("Class"))) return "";
		String result="";
		if(sub_obj==null) return result;

		if(sub_obj instanceof List){
			List list_sub_obj = (List)sub_obj;
			for(int i=0;i<list_sub_obj.size();i++){
				
				Object sub_obj2=list_sub_obj.get(i);
				if(sub_obj2!=null){								
					if(avoidCyclicPointers.get(Integer.valueOf(System.identityHashCode(sub_obj2)))!=null){
						result+=generateXmlItemTag_Start(new Object(), null,level+1, normalized,lowerCase1char);
						result+="\"WARNING: cyclic pointer\"";
						result+=generateXmlItemTag_Finish(new Object(), null,level+1, normalized,lowerCase1char);
					}else{
						avoidCyclicPointers.put(Integer.valueOf(System.identityHashCode(sub_obj2)), sub_obj2.getClass().getName());
						result+=generateXmlItem(sub_obj2, null,level+1,normalized,lowerCase1char,avoidCyclicPointers,textencoding);
						avoidCyclicPointers.remove(Integer.valueOf(System.identityHashCode(sub_obj2)));									
					}
				}else
					result+=generateXmlItem(sub_obj2, null,level+1,normalized,lowerCase1char,avoidCyclicPointers,textencoding);				
				
//				result+=generateXmlItem(list_sub_obj.get(i),null,level+1,normalized,lowerCase1char);
			}
			return result;
		}

		if(sub_obj instanceof Map){
			
			Iterator it = ((Map)sub_obj).entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        
				Object sub_obj2=pair.getValue();
				if(sub_obj2!=null){								
					if(avoidCyclicPointers.get(Integer.valueOf(System.identityHashCode(sub_obj2)))!=null){
						result+=generateXmlItemTag_Start(new Object(), pair.getKey().toString(),level+1, normalized,lowerCase1char);
						result+="\"WARNING: cyclic pointer\"";
						result+=generateXmlItemTag_Finish(new Object(), pair.getKey().toString(),level+1, normalized,lowerCase1char);
					}else{
						avoidCyclicPointers.put(Integer.valueOf(System.identityHashCode(sub_obj2)), sub_obj2.getClass().getName());
						result+=generateXmlItem(sub_obj2, pair.getKey().toString(),level+1,normalized,lowerCase1char,avoidCyclicPointers,textencoding);
						avoidCyclicPointers.remove(Integer.valueOf(System.identityHashCode(sub_obj2)));									
					}
				}else
					result+=generateXmlItem(sub_obj2, pair.getKey().toString(),level+1,normalized,lowerCase1char,avoidCyclicPointers,textencoding);
		        
//		        result+=generateXmlItem(sub_obj2,pair.getKey().toString(),level+1,normalized,lowerCase1char);	        
		    }
/*			
			List list_sub_obj = new Vector(((Map)sub_obj).values());
			List names_sub_obj = new Vector(((Map)sub_obj).keySet());
			for(int i=0;i<list_sub_obj.size();i++){
				result+=generateXmlItem(list_sub_obj.get(i),names_sub_obj.get(i).toString(),level+1,normalized,lowerCase1char);
			}
*/			
			return result;
		}


		if(	sub_obj.getClass().isPrimitive() ||
			sub_obj instanceof String ||
			sub_obj instanceof Number ||
			sub_obj instanceof Date ||
			sub_obj instanceof Boolean){
			String value=sub_obj.toString();
			boolean check=false;
			if(sub_obj instanceof String || sub_obj instanceof Boolean){
				check=true;
				result+=normalXML(value,textencoding);
				return result;
			}
			if(sub_obj instanceof Number){
				check=true;
				try{
					java.text.DecimalFormat df = new java.text.DecimalFormat("##0.000000", new DecimalFormatSymbols(new Locale("en")));
					result+= df.format(new java.math.BigDecimal(value.trim()).doubleValue());
				}catch(Exception e){}
				return result;
			}
			if(sub_obj instanceof Date){
				check=true;
				java.sql.Timestamp  tstmp = new java.sql.Timestamp(((Date)sub_obj).getTime());
				result+= util_format.timestampToString(tstmp);
				return result;
			}
			if(!check){
				try{
					java.text.DecimalFormat df = new java.text.DecimalFormat("##0.000000", new DecimalFormatSymbols(new Locale("en")));
					result+= df.format(new java.math.BigDecimal(value.trim()).doubleValue());
					return result;
				}catch(Exception e){
					result+=normalXML(value,textencoding);
					return result;
				}
			}

		}

		if(	sub_obj instanceof i_bean ||
			sub_obj instanceof i_elementDBBase ||
			sub_obj instanceof i_elementBase){
			try{
				Method[] methods = util_reflect.getMethods(sub_obj,"get");
				for(int i=0;i<methods.length;i++){
					String methodName = methods[i].getName().substring(3);
					Object sub_obj2 = util_reflect.getValue(sub_obj, "get"+util_reflect.adaptMethodName(methodName), null);
					if(sub_obj2!=null){
						if(!sub_obj2.equals(sub_obj)){
							if(sub_obj2!=null){								
								if(avoidCyclicPointers.get(Integer.valueOf(System.identityHashCode(sub_obj2)))!=null){
									result+=generateXmlItemTag_Start(new Object(), methodName,level+1, normalized,lowerCase1char);
									result+="\"WARNING: cyclic pointer\"";
									result+=generateXmlItemTag_Finish(new Object(), methodName,level+1, normalized,lowerCase1char);
								}else{
									avoidCyclicPointers.put(Integer.valueOf(System.identityHashCode(sub_obj2)), sub_obj2.getClass().getName());
									result+=generateXmlItem(sub_obj2, methodName,level+1,normalized,lowerCase1char,avoidCyclicPointers,textencoding);
									avoidCyclicPointers.remove(Integer.valueOf(System.identityHashCode(sub_obj2)));									
								}
							}else
								result+=generateXmlItem(sub_obj2, methodName,level+1,normalized,lowerCase1char,avoidCyclicPointers,textencoding);
							
//							result+=generateXmlItem(sub_obj2, methodName,level+1,normalized,checkConvert2xml,lowerCase1char);
						}
					}
				}
			}catch(Exception e){
			}
			return result;
		}

		if(normalized){
			result="";
		}else{
			try{
				result+="<![CDATA[";
				try{
					byte[] objAsByte = object2bytes(sub_obj);
					result+=new BASE64Encoder().encode(objAsByte);
				}catch(Exception ex){

				}
				result+="]]>";

			}catch(Exception e){
			}
		}

		return result;
	}

	private static String generateXmlItemTag_Finish(Object sub_obj, String name, int level, boolean normalized, boolean lowerCase1char){
		if(sub_obj==null || (name!=null && name.equals("Class"))) return "";

		String result="";

		if(normalized){
			if(name!=null){
				if(sub_obj instanceof List || sub_obj instanceof Map){
					result+="\n"+spaceLevel(level)+"</"+name+">\n";
				}else{
					result+="</"+name+">\n";
				}
			}
			else{
				if(sub_obj instanceof List){
					result+="\n"+spaceLevel(level)+"</items>\n";
					return result;
				}
				if(	sub_obj instanceof i_bean ||
					sub_obj instanceof i_elementDBBase ||
					sub_obj instanceof i_elementBase){
					result+=spaceLevel(level)+"</item>\n";
					return result;
				}
				result+=spaceLevel(level)+"</item>\n";
			}
		}else{
			if(sub_obj instanceof List){
				result+="\n"+spaceLevel(level)+"</items>\n";
				return result;
			}
			if(	sub_obj instanceof i_bean ||
				sub_obj instanceof i_elementDBBase ||
				sub_obj instanceof i_elementBase){
				result+="\n"+spaceLevel(level)+"</item>\n";
				return result;
			}
			result+="</item>\n";
		}

		return result;
	}

	private static String spaceLevel(int level){
		String result="";
		for(int i=0;i<level;i++) result+="     ";
		return result;
	}

	private static String normalXML (String input, String encoding) {

		if (input==null) return input;

//		try{
//			input = new String(input.getBytes(),"utf8");
//		}catch(Exception e){
//			input="";
//		}

		String result="";
		if (input.indexOf("&")>-1 ||
			input.indexOf("\\")>-1 ||
			input.indexOf(">")>-1 ||
			input.indexOf("<")>-1 ||
			input.indexOf("\"")>-1) {

			for (int i=0;i<input.length();i++) {
				if (input.charAt(i)=='&') result+="&amp;";
				else if (input.charAt(i)=='\'') result+="&apos;";
				else if (input.charAt(i)=='>') result+="&gt;";
				else if (input.charAt(i)=='<') result+="&lt;";
				else if (input.charAt(i)=='"') result+="&quot;";
				else result+=input.charAt(i);
			}
		}
		else
			result = input;
		
		if(encoding==null || encoding.equals(""))
			return result;
		else if(encoding.equalsIgnoreCase("asci"))
			return util_xml.normalASCII((result==null)?"":result.toString());
		else{
			try{
				return new String(result.getBytes(),encoding);
			}catch(Exception e){
			}
		}
		
		return result;		
		
	}
	
	private static String normalJSON (String input, String encoding) {

		if (input==null) return input;


		String result="";
		if (
			input.indexOf("\\")>-1 ||
			input.indexOf("\"")>-1) {

			for (int i=0;i<input.length();i++) {
				if (input.charAt(i)=='\\') result+="\\\\";
				else if (input.charAt(i)=='"') result+="\"";
				else result+=input.charAt(i);
			}
			
		}
		else
			result = input;
		
		if(encoding==null || encoding.equals(""))
			return result;
		else if(encoding.equalsIgnoreCase("asci"))
			return util_xml.normalASCII((result==null)?"":result.toString());
		else{
			try{
				return new String(result.getBytes(),encoding);
			}catch(Exception e){
			}
		}
		
		return result;
	}


	static public byte[] object2bytes(Object oldObj) throws Exception {
		byte[] retVal = null;
		ObjectOutputStream oos = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(oldObj);
			oos.flush();
			retVal = bos.toByteArray();

		} catch (Exception e) {
			throw (e);
		} finally {
			try {
				oos.close();
			} catch (java.io.IOException e) {
				throw (e);
			}
		}
		return retVal;
	}
	static public Object bytes2object(byte[] bos) throws Exception {
		Object retVal = null;
		ObjectInputStream ois = null;
		try {

			ByteArrayInputStream bin = new ByteArrayInputStream(bos);
			ois = new ObjectInputStream(bin);
			retVal = ois.readObject();

		} catch (Exception e) {
			throw (e);
		} finally {
			try {
				ois.close();
			} catch (java.io.IOException e) {
				throw (e);
			}
		}
		return retVal;
	}

	static public int sizeByte(Object oldObj) throws Exception {
		ObjectOutputStream oos = null;
		try {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(oldObj);
			oos.flush();
			return bos.toByteArray().length;
		} catch (Exception e) {
			throw (e);
		} finally {
			try {
				oos.close();
			} catch (java.io.IOException e) {
				throw (e);
			}
		}
	}

}
