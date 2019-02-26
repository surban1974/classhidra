package it.classhidra.serialize;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.Stack;
import java.util.TreeMap;

import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.util_reflect;
import it.classhidra.core.tool.util.util_xml;




public class XmlWriter {




	
	public static String object2xml(Object obj){
		return object2xml(obj, null);
	}
	
	public static String object2xml(Object obj, String name){
		return object2xml(obj, name, null);
	}	
	
	public static String object2xml(Object obj, String name, List filters){
		return object2xml(obj, name, filters, null, null);
	}
	public static String object2xml(Object obj, String name, List filters, Map namespaces){
		return object2xml(obj, name, filters, namespaces, null);
	}
	public static String object2xml(Object obj, String name, List filters, Map namespaces, WriteValidator validator){
		Map avoidCyclicPointers = new HashMap();

		String result="";
		if(obj==null)
			result+="<error>Object "+((name!=null)?"["+name+"]":"")+" is undefined or NULL</error>";
		else{
			Serialized annotation = obj.getClass().getAnnotation(Serialized.class);	
			String map_name = name;
			if(name==null && annotation!=null && annotation.output()!=null && !annotation.output().name().equals(""))
				map_name = annotation.output().name();
			else if(name==null && obj!=null && (obj instanceof List || obj.getClass().isArray()))
				map_name = "items";
			else if(name==null)
				map_name = "item";
			Stack objList = new Stack();
			
			result+=generateXmlItem(
					obj,
					map_name,
					0,
					avoidCyclicPointers,
					null,
					(annotation!=null)?annotation.children():false,
					(annotation!=null)?annotation.depth():0,
					util_xml.convertFilters(filters),
					namespaces,
					true,
					validator,
					objList);
			
			objList.clear();
			objList=null;
		}
		return result;
	}
	
	public static String object2xml(Object obj, String name, List filters, boolean children, int depth){
		return object2xml(obj, name, filters, children, depth, null, null);
	}
	public static String object2xml(Object obj, String name, List filters, boolean children, int depth, Map namespaces){
		return object2xml(obj, name, filters, children, depth, namespaces, null);
	}	
	public static String object2xml(Object obj, String name, List filters, boolean children, int depth, Map namespaces, WriteValidator validator){
		Map avoidCyclicPointers = new HashMap();

		String result="";
		if(obj==null)
			result+="<error>Object "+((name!=null)?"["+name+"]":"")+" is undefined or NULL</error>";
		else{
			Serialized annotation = obj.getClass().getAnnotation(Serialized.class);	
			String map_name = name;
			if(name==null && annotation!=null && annotation.output()!=null && !annotation.output().name().equals(""))
				map_name = annotation.output().name();
			else if(name==null && obj!=null && (obj instanceof List || obj.getClass().isArray()))
				map_name = "items";
			else if(name==null)
				map_name = "item";

			Stack objList = new Stack();
			result+=generateXmlItem(
					obj,
					map_name,
					0,
					avoidCyclicPointers,
					null,
					(annotation!=null)?(annotation.children() || children):children,
					(annotation!=null)
					?
					(depth>annotation.depth())?depth:annotation.depth()
					:
					depth,
					util_xml.convertFilters(filters),
					namespaces,
					true,
					validator,
					objList);
			
			objList.clear();
			objList=null;
		}
		return result;
	}


	
	private static String generateXmlItem(Object sub_obj, String name, int level, Map avoidCyclicPointer, Serialized annotation, boolean serializeChildren, int serializeDepth, Map treeFilters, Map namespaces, boolean fixedName, WriteValidator validator, Stack objList){
		String result="";
		boolean goAhead = true;
		Map subTreeFilters = null;
		if(treeFilters!=null){
			if(name!=null && treeFilters.get(name)==null && treeFilters.get(util_reflect.revAdaptMethodName(name))==null)
				goAhead=false;
			else if(name!=null){
				subTreeFilters = (Map)treeFilters.get(name);
				if(subTreeFilters==null)
					subTreeFilters = (Map)treeFilters.get(util_reflect.revAdaptMethodName(name));
			}
		}
		if(goAhead && validator!=null)
			goAhead = validator.isWritable(sub_obj, objList, getOutputName(name, annotation, fixedName));
		if(goAhead){
			if(objList !=null && sub_obj!=null)
				objList.push(sub_obj);
			result+=generateXmlItemTag_Start(sub_obj, name, level, annotation, namespaces, fixedName);
			result+=generateXmlItemTag_Content(sub_obj, name, level,avoidCyclicPointer,annotation,serializeChildren,serializeDepth,subTreeFilters, validator, objList);
			result+=generateXmlItemTag_Finish(sub_obj, name, level, annotation, fixedName);
			if(objList !=null && sub_obj!=null)
				objList.remove(sub_obj);
		}
		return result;
	}

	private static String generateXmlItemTag_Start(Object sub_obj, String name, int level, Serialized annotation, Map namespaces, boolean fixedName){
		if(sub_obj==null || (name!=null && name.equals("Class"))) return "";
		String map_name = name;
		if(name!=null && !fixedName)
			map_name = util_reflect.revAdaptMethodName(name);
		if(annotation!=null && annotation.output()!=null && !annotation.output().name().equals(""))
				map_name = annotation.output().name();
		
		
		String result=spaceLevel(level);
		
		if(map_name!=null) result+="<"+(map_name);
		else{
			if(sub_obj instanceof List || sub_obj.getClass().isArray() || sub_obj instanceof Set)
				result+="<items";
			else result+="<item";
		}
		
		if(namespaces!=null && namespaces.size()>0) {

			Iterator it = namespaces.entrySet().iterator();
			while (it.hasNext()) {
			    Map.Entry pair = (Map.Entry)it.next();
			    result+=" "+pair.getKey()+"=\""+pair.getValue()+"\"";
			}
		}
		
		result+=">";
		
		if(sub_obj instanceof List || sub_obj.getClass().isArray() || sub_obj instanceof Map || sub_obj instanceof Set){
			result+="\n";
			return result;
		}
		
		boolean simple = false;
		if(	sub_obj.getClass().isPrimitive() ||
				sub_obj instanceof String ||
				sub_obj instanceof Number ||
				sub_obj instanceof Date ||
				sub_obj instanceof Boolean)
			simple = true;		
		
		if(!simple){
			result+="\n";
			return result;
		}

		return result;
	}

	private static String generateXmlItemTag_Content(Object sub_obj, String name, int level, Map avoidCyclicPointers, Serialized annotation, boolean serializeChildren, int serializeDepth, Map treeFilters, WriteValidator validator, Stack objList){
		if(sub_obj==null || (name!=null && name.equals("Class"))) return "";
		String result="";
		if(sub_obj==null) return result;

		if(sub_obj.getClass().isArray()){
			Class componentType = sub_obj.getClass().getComponentType();
			if(componentType.isPrimitive()){
				List list = new ArrayList();
				if (boolean.class.isAssignableFrom(componentType)){ 
					for(int i=0;i<((boolean[])sub_obj).length;i++)
						list.add(((boolean[])sub_obj)[i]); 
				}else if (byte.class.isAssignableFrom(componentType)){
					for(int i=0;i<((byte[])sub_obj).length;i++)
						list.add(((byte[])sub_obj)[i]); 
				}else if (char.class.isAssignableFrom(componentType)){
					for(int i=0;i<((char[])sub_obj).length;i++)
						list.add(((char[])sub_obj)[i]);
				}else if (double.class.isAssignableFrom(componentType)){
					for(int i=0;i<((double[])sub_obj).length;i++)
						list.add(((double[])sub_obj)[i]);
				}else if (float.class.isAssignableFrom(componentType)){
					for(int i=0;i<((float[])sub_obj).length;i++)
						list.add(((float[])sub_obj)[i]);
				}else if (int.class.isAssignableFrom(componentType)){
					for(int i=0;i<((int[])sub_obj).length;i++)
						list.add(((int[])sub_obj)[i]);
				}else if (long.class.isAssignableFrom(componentType)){
					for(int i=0;i<((long[])sub_obj).length;i++)
						list.add(((long[])sub_obj)[i]);
				}else if (short.class.isAssignableFrom(componentType)){
					for(int i=0;i<((short[])sub_obj).length;i++)
						list.add(((short[])sub_obj)[i]);
				}

				for(int i=0;i<list.size();i++) {						
					Object sub_obj2=list.get(i);
					if(sub_obj2!=null){		
						
						Serialized sub_annotation = sub_obj2.getClass().getAnnotation(Serialized.class);
						if(annotation!=null || sub_annotation!=null || serializeChildren || serializeDepth>0){

							if(avoidCyclicPointers.get(Integer.valueOf(System.identityHashCode(sub_obj2)))!=null){
								result+=generateXmlItemTag_Start(new Object(), null, level+1, sub_annotation, null, false);
								result+="\"WARNING: cyclic pointer\"";
								result+=generateXmlItemTag_Finish(new Object(), null, level+1, sub_annotation, false);
							}else{
								avoidCyclicPointers.put(Integer.valueOf(System.identityHashCode(sub_obj2)), sub_obj2.getClass().getName());
								result+=generateXmlItem(sub_obj2, null,level+1,avoidCyclicPointers,sub_annotation,
												(sub_annotation!=null)?sub_annotation.children():false,
												(sub_annotation!=null && sub_annotation.depth()>0)
												?
													sub_annotation.depth()
												:
													(serializeDepth-1>=0)?serializeDepth-1:0
												,
												treeFilters,
												null,
												false,
												validator,
												objList);
								avoidCyclicPointers.remove(Integer.valueOf(System.identityHashCode(sub_obj2)));									
							}
						}
					}else
						result+=generateXmlItem(sub_obj2, null,level+1,avoidCyclicPointers,annotation,serializeChildren,(serializeDepth-1>=0)?serializeDepth:0,treeFilters, null, false, validator, objList);				
			    }
			    return result;
			}else{
				Iterator it = Arrays.asList((Object[])sub_obj).iterator(); 
				String result_tmp="";
			    while (it.hasNext()) {
					Object sub_obj2=it.next();
					if(sub_obj2!=null){				
						
						Serialized sub_annotation = sub_obj2.getClass().getAnnotation(Serialized.class);
						if(annotation!=null || sub_annotation!=null || serializeChildren || serializeDepth>0){							

							if(avoidCyclicPointers.get(Integer.valueOf(System.identityHashCode(sub_obj2)))!=null){
								result+=generateXmlItemTag_Start(new Object(), null, level+1, sub_annotation, null, false);
								result+="\"WARNING: cyclic pointer\"";
								result+=generateXmlItemTag_Finish(new Object(), null, level+1, sub_annotation, false);
							}else{
								avoidCyclicPointers.put(Integer.valueOf(System.identityHashCode(sub_obj2)), sub_obj2.getClass().getName());
								result+=generateXmlItem(sub_obj2, null,level+1,avoidCyclicPointers,sub_annotation,
												(sub_annotation!=null)?sub_annotation.children():false,
												(sub_annotation!=null && sub_annotation.depth()>0)
												?
													sub_annotation.depth()
												:
													(serializeDepth-1>=0)?serializeDepth-1:0
												,
												treeFilters,
												null,
												false,
												validator,
												objList);
								avoidCyclicPointers.remove(Integer.valueOf(System.identityHashCode(sub_obj2)));									
							}
						}
					}else
						result+=generateXmlItem(sub_obj2, null,level+1,avoidCyclicPointers,annotation,serializeChildren,(serializeDepth-1>=0)?serializeDepth:0,treeFilters, null, false, validator, objList);				
								        
			    }
			    return result+result_tmp;
			}
			
		}				
		
		if(sub_obj instanceof List){
			List list_sub_obj = (List)sub_obj;
			for(int i=0;i<list_sub_obj.size();i++){				
				Object sub_obj2=list_sub_obj.get(i);
				if(sub_obj2!=null){				
					
					Serialized sub_annotation = sub_obj2.getClass().getAnnotation(Serialized.class);
					if(annotation!=null || sub_annotation!=null || serializeChildren || serializeDepth>0){

						if(avoidCyclicPointers.get(Integer.valueOf(System.identityHashCode(sub_obj2)))!=null){
							result+=generateXmlItemTag_Start(new Object(), null, level+1, sub_annotation, null, false);
							result+="\"WARNING: cyclic pointer\"";
							result+=generateXmlItemTag_Finish(new Object(), null, level+1, sub_annotation, false);
						}else{
							avoidCyclicPointers.put(Integer.valueOf(System.identityHashCode(sub_obj2)), sub_obj2.getClass().getName());
							result+=generateXmlItem(sub_obj2, null,level+1,avoidCyclicPointers,sub_annotation,
									(sub_annotation!=null)?sub_annotation.children():false,
									(sub_annotation!=null && sub_annotation.depth()>0)
									?
										sub_annotation.depth()
									:
										(serializeDepth-1>=0)?serializeDepth-1:0
									,
									treeFilters,
									null,
									false,
									validator,
									objList);
							avoidCyclicPointers.remove(Integer.valueOf(System.identityHashCode(sub_obj2)));									
						}
					}
				}else
					result+=generateXmlItem(sub_obj2, null,level+1,avoidCyclicPointers,annotation,serializeChildren,(serializeDepth-1>=0)?serializeDepth:0,treeFilters, null, false, validator, objList);				
				
			}
			return result;
		}

		if(sub_obj instanceof Set){
			Iterator it = Arrays.asList(((Set)sub_obj).toArray()).iterator(); 

			String result_tmp="";
		    while (it.hasNext()) {
				Object sub_obj2=it.next();
				if(sub_obj2!=null){				
					
					Serialized sub_annotation = sub_obj2.getClass().getAnnotation(Serialized.class);
					if(annotation!=null || sub_annotation!=null || serializeChildren || serializeDepth>0){							

						if(avoidCyclicPointers.get(Integer.valueOf(System.identityHashCode(sub_obj2)))!=null){
							result+=generateXmlItemTag_Start(new Object(), null, level+1, sub_annotation, null, false);
							result+="\"WARNING: cyclic pointer\"";
							result+=generateXmlItemTag_Finish(new Object(), null, level+1, sub_annotation, false);
						}else{
							avoidCyclicPointers.put(Integer.valueOf(System.identityHashCode(sub_obj2)), sub_obj2.getClass().getName());
							result+=generateXmlItem(sub_obj2, null,level+1,avoidCyclicPointers,sub_annotation,
											(sub_annotation!=null)?sub_annotation.children():false,
											(sub_annotation!=null && sub_annotation.depth()>0)
											?
												sub_annotation.depth()
											:
												(serializeDepth-1>=0)?serializeDepth-1:0
											,
											treeFilters,
											null,
											false,
											validator,
											objList);
							avoidCyclicPointers.remove(Integer.valueOf(System.identityHashCode(sub_obj2)));									
						}
					}
				}else
					result+=generateXmlItem(sub_obj2, null,level+1,avoidCyclicPointers,annotation,serializeChildren,(serializeDepth-1>=0)?serializeDepth:0,treeFilters, null, false, validator, objList);				
							        
		    }
		    return result+result_tmp;
		}		
		
		if(sub_obj instanceof Map){
			
			Iterator it = ((Map)sub_obj).entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        
				Object sub_obj2=pair.getValue();
				if(sub_obj2!=null){	
					
					Serialized sub_annotation = sub_obj2.getClass().getAnnotation(Serialized.class);
					if(annotation!=null || sub_annotation!=null || serializeChildren || serializeDepth>0){

						if(avoidCyclicPointers.get(Integer.valueOf(System.identityHashCode(sub_obj2)))!=null){
							result+=generateXmlItemTag_Start(new Object(), pair.getKey().toString(), level+1, sub_annotation, null, false);
							result+="\"WARNING: cyclic pointer\"";
							result+=generateXmlItemTag_Finish(new Object(), pair.getKey().toString(), level+1, sub_annotation, false);
						}else{
							avoidCyclicPointers.put(Integer.valueOf(System.identityHashCode(sub_obj2)), sub_obj2.getClass().getName());
							result+=generateXmlItem(sub_obj2, pair.getKey().toString(),level+1,avoidCyclicPointers,sub_annotation,
									(sub_annotation!=null)?sub_annotation.children():false,
									(sub_annotation!=null && sub_annotation.depth()>0)
									?
										sub_annotation.depth()
									:
										(serializeDepth-1>=0)?serializeDepth-1:0
									,
									treeFilters,
									null,
									false,
									validator,
									objList);
							avoidCyclicPointers.remove(Integer.valueOf(System.identityHashCode(sub_obj2)));									
						}
					}
				}else
					result+=generateXmlItem(sub_obj2, pair.getKey().toString(),level+1,avoidCyclicPointers,annotation,serializeChildren,(serializeDepth-1>=0)?serializeDepth:0,treeFilters, null, false, validator, objList);
		        
        
		    }
			
			return result;
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
				if(annotation!=null && annotation.output()!=null){
					try{					
						value=util_format.makeFormatedString(annotation.output().format(), annotation.output().language(),annotation.output().country(), sub_obj);
						if(annotation.output().xml_cdata() && value!=null) {
							String charset = ((annotation.output().characterset().equals(""))?((annotation.output().ascii())?"ascii":null):null);
							if(charset==null)
								result+="<![CDATA["+value.replace("]]>", "]]&gt;")+"]]>";
							else
								result+="<![CDATA["+new String(value.replace("]]>", "]]&gt;").getBytes(),charset)+"]]>";
						}else if(annotation.output().xml_escape10()) 
							result+=util_xml.escapeXML10(value,((annotation.output().characterset().equals(""))?((annotation.output().ascii())?"ascii":null):null));
						else if(annotation.output().xml_escape11()) 
							result+=util_xml.escapeXML11(value,((annotation.output().characterset().equals(""))?((annotation.output().ascii())?"ascii":null):null));
						else
							result+=util_xml.normalXML(value,((annotation.output().characterset().equals(""))?((annotation.output().ascii())?"ascii":null):null));
					}catch(Exception e){	
						result+=util_xml.normalXML(value,null);
					}	
				}else				
					result+=util_xml.normalXML(value,null);
				

				return result;
			}
			if(sub_obj instanceof Boolean){
				check=true;
				if(annotation!=null && annotation.output()!=null){
					try{					
						value=String.valueOf(((Boolean)sub_obj));
						if(annotation.output().xml_cdata() && value!=null) {
							String charset = ((annotation.output().characterset().equals(""))?((annotation.output().ascii())?"ascii":null):null);
							if(charset==null)
								result+="<![CDATA["+value.replace("]]>", "]]&gt;")+"]]>";
							else
								result+="<![CDATA["+new String(value.replace("]]>", "]]&gt;").getBytes(),charset)+"]]>";
						}else if(annotation.output().xml_escape10()) 
							result+=util_xml.escapeXML10(value,((annotation.output().characterset().equals(""))?((annotation.output().ascii())?"ascii":null):null));
						else if(annotation.output().xml_escape11()) 
							result+=util_xml.escapeXML11(value,((annotation.output().characterset().equals(""))?((annotation.output().ascii())?"ascii":null):null));
						else
							result+=util_xml.normalXML(value,((annotation.output().characterset().equals(""))?((annotation.output().ascii())?"ascii":null):null));

					}catch(Exception e){	
						result+=util_xml.normalXML(value,null);
					}	
				}else				
					result+=util_xml.normalXML(value,null);
				
				return result;
			}			
			if(sub_obj instanceof Number){
				check=true;
				if(annotation!=null && annotation.output()!=null){
					try{					
						value=util_format.makeFormatedString(annotation.output().format(), annotation.output().language(),annotation.output().country(), sub_obj);
						if(annotation.output().xml_cdata() && value!=null) {
							String charset = ((annotation.output().characterset().equals(""))?((annotation.output().ascii())?"ascii":null):null);
							if(charset==null)
								result+="<![CDATA["+value.replace("]]>", "]]&gt;")+"]]>";
							else
								result+="<![CDATA["+new String(value.replace("]]>", "]]&gt;").getBytes(),charset)+"]]>";
						}else if(annotation.output().xml_escape10()) 
							result+=util_xml.escapeXML10(value,((annotation.output().characterset().equals(""))?((annotation.output().ascii())?"ascii":null):null));
						else if(annotation.output().xml_escape11()) 
							result+=util_xml.escapeXML11(value,((annotation.output().characterset().equals(""))?((annotation.output().ascii())?"ascii":null):null));
						else
							result+=util_xml.normalXML(value,((annotation.output().characterset().equals(""))?((annotation.output().ascii())?"ascii":null):null));

					}catch(Exception e){	
						result+=util_xml.normalXML(value,null);
					}	
				}else				
					result+=util_xml.normalXML(value,null);
				

				return result;
			}
			if(sub_obj instanceof Date){
				check=true;
				if(annotation!=null && annotation.output()!=null){
					try{					
						value=util_format.makeFormatedString(annotation.output().format(), annotation.output().language(),annotation.output().country(), sub_obj);
						if(annotation.output().xml_cdata() && value!=null) {
							String charset = ((annotation.output().characterset().equals(""))?((annotation.output().ascii())?"ascii":null):null);
							if(charset==null)
								result+="<![CDATA["+value.replace("]]>", "]]&gt;")+"]]>";
							else
								result+="<![CDATA["+new String(value.replace("]]>", "]]&gt;").getBytes(),charset)+"]]>";
						}else if(annotation.output().xml_escape10()) 
							result+=util_xml.escapeXML10(value,((annotation.output().characterset().equals(""))?((annotation.output().ascii())?"ascii":null):null));
						else if(annotation.output().xml_escape11()) 
							result+=util_xml.escapeXML11(value,((annotation.output().characterset().equals(""))?((annotation.output().ascii())?"ascii":null):null));
						else
							result+=util_xml.normalXML(value,((annotation.output().characterset().equals(""))?((annotation.output().ascii())?"ascii":null):null));

					}catch(Exception e){	
						result+=util_xml.normalXML(value,null);
					}	
				}else				
					result+=util_xml.normalXML(value,null);

				return result;
			}
			if(!check){
				if(annotation!=null && annotation.output()!=null){
					try{
						java.text.DecimalFormat df = new java.text.DecimalFormat("##0.000000", new DecimalFormatSymbols(new Locale("en")));
						value = df.format(new java.math.BigDecimal(value.trim()).doubleValue());
						if(annotation.output().xml_cdata() && value!=null) {
							String charset = ((annotation.output().characterset().equals(""))?((annotation.output().ascii())?"ascii":null):null);
							if(charset==null)
								result+="<![CDATA["+value.replace("]]>", "]]&gt;")+"]]>";
							else
								result+="<![CDATA["+new String(value.replace("]]>", "]]&gt;").getBytes(),charset)+"]]>";
						}else if(annotation.output().xml_escape10()) 
							result+=util_xml.escapeXML10(value,((annotation.output().characterset().equals(""))?((annotation.output().ascii())?"ascii":null):null));
						else if(annotation.output().xml_escape11()) 
							result+=util_xml.escapeXML11(value,((annotation.output().characterset().equals(""))?((annotation.output().ascii())?"ascii":null):null));
						else
							result+=util_xml.normalXML(value,((annotation.output().characterset().equals(""))?((annotation.output().ascii())?"ascii":null):null));
	
					}catch(Exception e){
						result+=util_xml.normalXML(value,null);
						return result;
					}
				}else				
					result+=util_xml.normalXML(value,null);

				return result;
			}

		}

		Map name_tmp = new HashMap();
			try{
				String[] prefixes = new String[]{"get","is"};
				StringBuffer stringBuffer = new StringBuffer();
				SortedMap ordered = new TreeMap();
				
				for(int p=0;p<prefixes.length;p++){
					Method[] methods = util_reflect.getMethods(sub_obj,prefixes[p]);
					for(int i=0;i<methods.length;i++){
						if(!Modifier.isStatic(methods[i].getModifiers())){
							String methodName = methods[i].getName().substring(prefixes[p].length());
							Serialized sub_annotation = methods[i].getAnnotation(Serialized.class);
							Object sub_obj2 = null;
							if(sub_annotation==null){
								Field sub_field = util_reflect.getFieldRecursive(sub_obj.getClass(), util_reflect.revAdaptMethodName(methodName));
								if(sub_field!=null){
									sub_annotation = sub_field.getAnnotation(Serialized.class);
									if(!sub_field.isAccessible()){
										sub_field.setAccessible(true);
										sub_obj2 = sub_field.get(sub_obj);
										sub_field.setAccessible(false);
									}else
										sub_obj2 = sub_field.get(sub_obj);
								}
							}
							if(sub_obj2==null)
								sub_obj2 = util_reflect.getValue(sub_obj, prefixes[p]+util_reflect.adaptMethodName(methodName), null);
							if(sub_obj2!=null){
								if(sub_annotation==null)
									sub_annotation = sub_obj2.getClass().getAnnotation(Serialized.class);
									
								if(	(sub_annotation==null && (serializeChildren || serializeDepth>0))
										||
										(sub_annotation!=null && sub_annotation.value())
								){								
//								if(sub_annotation!=null || serializeChildren || serializeDepth>0){
									if(!sub_obj2.equals(sub_obj)){
							
										if(avoidCyclicPointers.get(Integer.valueOf(System.identityHashCode(sub_obj2)))!=null){
											String tag="";
											tag+=generateXmlItemTag_Start(new Object(), methodName, level+1, sub_annotation, null, false);
											tag+="\"WARNING: cyclic pointer\"";
											tag+=generateXmlItemTag_Finish(new Object(), methodName, level+1, sub_annotation, false);
											
											if(sub_annotation!=null && sub_annotation.order()>-1)
												ordered.put(sub_annotation.order(), tag);
											else
												stringBuffer.append(tag);
										}else{
											avoidCyclicPointers.put(Integer.valueOf(System.identityHashCode(sub_obj2)), sub_obj2.getClass().getName());
											
											String map_name = util_reflect.revAdaptMethodName(methodName);
											if(sub_annotation!=null && sub_annotation.output()!=null && sub_annotation.output().name()!=null && !sub_annotation.output().name().equals(""))
												map_name = sub_annotation.output().name();
											
											name_tmp.put(map_name, map_name);
											
											String tag = generateXmlItem(
													sub_obj2,
													methodName,
													level+1,
													avoidCyclicPointers,
													sub_annotation,
													(sub_annotation!=null)?sub_annotation.children():false,
													(sub_annotation!=null && sub_annotation.depth()>0)
													?
														sub_annotation.depth()
													:
														(serializeDepth-1>=0)?serializeDepth-1:0
													,
													treeFilters,
													null,
													false,
													validator,
													objList
											);
											
											if(sub_annotation!=null && sub_annotation.order()>-1)
												ordered.put(sub_annotation.order(), tag);
											else
												stringBuffer.append(tag);
											
											avoidCyclicPointers.remove(Integer.valueOf(System.identityHashCode(sub_obj2)));									
										}
									}
								}
							}
						}
					}
				}
				

				Field[] fields = sub_obj.getClass().getDeclaredFields();

				for(int j=0;j<fields.length;j++){
					if(!Modifier.isStatic(fields[j].getModifiers())){
						String fieldName = fields[j].getName();
						Serialized sub_annotation = fields[j].getAnnotation(Serialized.class);
						
						if(sub_annotation!=null && sub_annotation.value()){
							String sub_map_name2 = fields[j].getName();
							if(sub_annotation.output()!=null && sub_annotation.output().name()!=null && !sub_annotation.output().name().equals(""))
								sub_map_name2 = sub_annotation.output().name();
							if(sub_map_name2!=null && name_tmp.get(sub_map_name2)==null){
								Object sub_obj2 = null;
								if(!fields[j].isAccessible()){
									fields[j].setAccessible(true);
									sub_obj2 = fields[j].get(sub_obj);
									fields[j].setAccessible(false);
								}else
									sub_obj2 = fields[j].get(sub_obj);
									
								if(sub_obj2!=null && !sub_obj2.equals(sub_obj)){

									
									if(avoidCyclicPointers.get(Integer.valueOf(System.identityHashCode(sub_obj2)))!=null){
										String tag = "";
										tag+=generateXmlItemTag_Start(new Object(), fieldName, level+1, sub_annotation, null, false);
										tag+="\"WARNING: cyclic pointer\"";
										tag+=generateXmlItemTag_Finish(new Object(), fieldName, level+1, sub_annotation, false);
										
										if(sub_annotation!=null && sub_annotation.order()>-1)
											ordered.put(sub_annotation.order(), tag);
										else
											stringBuffer.append(tag);
									}else{
										avoidCyclicPointers.put(Integer.valueOf(System.identityHashCode(sub_obj2)), sub_obj2.getClass().getName());
										 
										
										String tag = generateXmlItem(
												sub_obj2, fieldName,level+1,avoidCyclicPointers,sub_annotation,
												(sub_annotation!=null)?sub_annotation.children():false,
												(sub_annotation!=null && sub_annotation.depth()>0)
												?
													sub_annotation.depth()
												:
													(serializeDepth-1>=0)?serializeDepth-1:0
												,
												treeFilters,
												null,
												false,
												validator,
												objList);
										
										if(sub_annotation!=null && sub_annotation.order()>-1)
											ordered.put(sub_annotation.order(), tag);
										else
											stringBuffer.append(tag);
											
										avoidCyclicPointers.remove(Integer.valueOf(System.identityHashCode(sub_obj2)));									
									}
							
								}
							}
							
						}
						
					}	
				}	
				
				if(ordered.size()>0) {
					Iterator it = ordered.values().iterator();
					while (it.hasNext()) 
						result+=it.next();
				}
				ordered.clear();
				ordered=null;
				result+=stringBuffer.toString();
				
				
			}catch(Exception e){
			}




		return result;
	}

	private static String generateXmlItemTag_Finish(Object sub_obj, String name, int level, Serialized annotation, boolean fixedName){
		if(sub_obj==null || (name!=null && name.equals("Class"))) return "";
		String map_name = name;
		if(name!=null && !fixedName)
			map_name = util_reflect.revAdaptMethodName(name);
		if(annotation!=null && annotation.output()!=null && !annotation.output().name().equals(""))
			map_name = annotation.output().name();
		

		String result="";

		if(map_name!=null){
			if(sub_obj instanceof List || sub_obj.getClass().isArray() || sub_obj instanceof Map || sub_obj instanceof Set){
				result+="\n"+spaceLevel(level)+"</"+(map_name)+">\n";
				return result;
			}
			boolean simple = false;
			if(	sub_obj.getClass().isPrimitive() ||
					sub_obj instanceof String ||
					sub_obj instanceof Number ||
					sub_obj instanceof Date ||
					sub_obj instanceof Boolean)
				simple = true;
			if(!simple){
				result+=spaceLevel(level)+"</"+(map_name)+">\n";
				return result;
			}
			result+="</"+(map_name)+">\n";
//			result+="</"+(map_name)+">\n";
			
		}else{
			if(sub_obj instanceof List || sub_obj.getClass().isArray() || sub_obj instanceof Set){
				result+="\n"+spaceLevel(level)+"</items>\n";
				return result;
			}
			boolean simple = false;
			if(	sub_obj.getClass().isPrimitive() ||
					sub_obj instanceof String ||
					sub_obj instanceof Number ||
					sub_obj instanceof Date ||
					sub_obj instanceof Boolean)
				simple = true;
			if(!simple){
				result+=spaceLevel(level)+"</item>\n";
				return result;
			}
			result+="</item>\n";
//			result+=spaceLevel(level)+"</item>\n";
		}
		

		return result;
	}

	private static String spaceLevel(int level){
		String result="";
		for(int i=0;i<level;i++) result+="     ";
		return result;
	}

	private static String getOutputName(String name, Serialized annotation, boolean fixedName) {
		String map_name = name;
		if(name!=null && !fixedName)
			map_name = util_reflect.revAdaptMethodName(name);
		if(annotation!=null && annotation.output()!=null && !annotation.output().name().equals(""))
				map_name = annotation.output().name();
		return map_name;
	}


}
