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
import it.classhidra.serialize.JsonWriter.OrderContainer;



public class JsonWriter2 {

	
	public static String object2json(Object obj){
		return object2json(obj,null);
	}
	
	public static String object2json(Object obj, String name){
		return object2json(obj,name,null);
	}
	
	public static String object2json(Object obj, String name, List filters){
		return object2json(obj,name,filters,null);
	}

	public static String object2json(Object obj, String name, List filters, WriteValidator validator){
		String result="{\n";
		if(obj==null)
			result+="\"error\":  \"Object "+((name!=null)?"["+name+"]":"")+" is undefined or NULL\"";
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
			if(map_name!=null && map_name.equals("")) {
				result+=generateJsonItemOnlyContent(
						obj,
						map_name,
						0,
						false,
						new HashMap(),
						null,
						(annotation!=null)?annotation.children():false,
						(annotation!=null)?annotation.depth():0,
						util_xml.convertFilters(filters),
						null,
						validator,
						objList);
			}else {
				result+=generateJsonItem(
						obj,
						map_name,
						0,
						false,
						new HashMap(),
						null,
						(annotation!=null)?annotation.children():false,
						(annotation!=null)?annotation.depth():0,
						util_xml.convertFilters(filters),
						null,
						validator,
						objList);
			}
			objList.clear();
			objList=null;
		}
		return result+"\n}";
	}	
	
	public static String object2json(Object obj, String name, List filters, boolean children, int depth){
		return object2json(obj, name, filters, children, depth, null);
	}
	
	public static String object2json(Object obj, String name, List filters, boolean children, int depth, WriteValidator validator){
		String result="{\n";
		if(obj==null)
			result+="\"error\":  \"Object "+((name!=null)?"["+name+"]":"")+" is undefined or NULL\"";
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
			if(map_name!=null && map_name.equals("")) {
				result+=generateJsonItemOnlyContent(
						obj,
						map_name,
						0,
						false,
						new HashMap(),
						null,
						(annotation!=null)?(annotation.children() || children):children,
						(annotation!=null)
						?
						(depth>annotation.depth())?depth:annotation.depth()
						:
						depth,
						util_xml.convertFilters(filters),
						null,
						validator,
						objList);
				
			}else {
				result+=generateJsonItem(
						obj,
						map_name,
						0,
						false,
						new HashMap(),
						null,
						(annotation!=null)?(annotation.children() || children):children,
						(annotation!=null)
						?
						(depth>annotation.depth())?depth:annotation.depth()
						:
						depth,
						util_xml.convertFilters(filters),
						null,
						validator,
						objList);
			}
			objList.clear();
			objList=null;
		}
		return result+"\n}";
	}	
	

	private static String generateJsonItem(Object sub_obj, String name, int level, boolean notFirst, Map avoidCyclicPointers, Serialized annotation, boolean serializeChildren, int serializeDepth, Map treeFilters, String subFilterName, WriteValidator validator, Stack objList){
		String result="";
		boolean goAhead = true;
		Map subTreeFilters = null;
		if(treeFilters!=null){
			if(name!=null && treeFilters.get(name)==null && treeFilters.get(util_reflect.revAdaptMethodName(name))==null)
				goAhead=false;
			else if(name==null && subFilterName!=null && treeFilters.get(subFilterName)==null && treeFilters.get(util_reflect.revAdaptMethodName(subFilterName))==null)
				goAhead=false; 
			else if(name!=null){
				subTreeFilters = (Map)treeFilters.get(name);
				if(subTreeFilters==null)
					subTreeFilters = (Map)treeFilters.get(util_reflect.revAdaptMethodName(name));
			} else if(name==null && subFilterName!=null){
				subTreeFilters = (Map)treeFilters.get(subFilterName);
				if(subTreeFilters==null)
					subTreeFilters = (Map)treeFilters.get(util_reflect.revAdaptMethodName(subFilterName));
			}
		}
		if(goAhead && validator!=null)
			goAhead = validator.isWritable(sub_obj, objList, getOutputName(name, annotation));
		if(goAhead){
			result+=generateJsonItemTag_Start(sub_obj, name, level, annotation, notFirst);
			result+=generateJsonItemTag_Content(sub_obj, name, level, avoidCyclicPointers, annotation, serializeChildren, serializeDepth, subTreeFilters, validator, objList);
			result+=generateJsonItemTag_Finish(sub_obj, name, level, notFirst);		
		}
		return result;
	}
	
	private static String generateJsonItemOnlyContent(Object sub_obj, String name, int level, boolean notFirst, Map avoidCyclicPointers, Serialized annotation, boolean serializeChildren, int serializeDepth, Map treeFilters, String subFilterName, WriteValidator validator, Stack objList){
		String result="";
		boolean goAhead = true;
		Map subTreeFilters = null;
		if(treeFilters!=null){
			if(name!=null && treeFilters.get(name)==null && treeFilters.get(util_reflect.revAdaptMethodName(name))==null)
				goAhead=false;
			else if(name==null && subFilterName!=null && treeFilters.get(subFilterName)==null && treeFilters.get(util_reflect.revAdaptMethodName(subFilterName))==null)
				goAhead=false; 
			else if(name!=null){
				subTreeFilters = (Map)treeFilters.get(name);
				if(subTreeFilters==null)
					subTreeFilters = (Map)treeFilters.get(util_reflect.revAdaptMethodName(name));
			} else if(name==null && subFilterName!=null){
				subTreeFilters = (Map)treeFilters.get(subFilterName);
				if(subTreeFilters==null)
					subTreeFilters = (Map)treeFilters.get(util_reflect.revAdaptMethodName(subFilterName));
			}
		}
		if(goAhead && validator!=null)
			goAhead = validator.isWritable(sub_obj, objList, getOutputName(name, annotation));
		if(goAhead){
			result+=generateJsonItemTag_Content(sub_obj, name, level, avoidCyclicPointers, annotation, serializeChildren, serializeDepth, subTreeFilters, validator, objList);
		}
		return result;
	}
	

	private static String generateJsonItemTag_Start(Object sub_obj, String name, int level, Serialized annotation, boolean notFirst){
		if(sub_obj==null || (name!=null && name.equals("Class"))) return "";
		String map_name = name;
		if(name!=null){
			map_name = util_reflect.revAdaptMethodName(name);
			if(annotation!=null && annotation.output()!=null && !annotation.output().name().equals(""))
				map_name = annotation.output().name();
		}
		
		String space=spaceLevel(level);
		String result="";
		
		if(sub_obj.getClass().isArray()){
			if(notFirst) result+=",\n";
			result+=space;
			if(map_name!=null && !map_name.equals(""))
				result+="\""+(map_name)+"\":";
			if(notFirst){
				result+="\n";
				result+=space+"[\n";
			}else result+="[\n";
			
			return result;
		}
		
		if(sub_obj instanceof List ){
			if(notFirst) result+=",\n";
			result+=space;
			if(map_name!=null && !map_name.equals(""))
				result+="\""+(map_name)+"\":";
			if(notFirst){
				result+="\n";
				result+=space+"[\n";
			}else result+="[\n";
			
			return result;
		}
		
		
		if(sub_obj instanceof Map){
			if(notFirst) result+=",\n";
			result+=space;
			if(map_name!=null && !map_name.equals(""))
				result+="\""+(map_name)+"\":";
			if(notFirst){
				result+="\n";
				result+=space+"{\n";
			}else result+="{\n";
			
			return result;
		}

		if(sub_obj instanceof Set ){
			if(notFirst) result+=",\n";
			result+=space;
			if(map_name!=null && !map_name.equals(""))
				result+="\""+(map_name)+"\":";
			if(notFirst){
				result+="\n";
				result+=space+"[\n";
			}else result+="[\n";
			
			return result;
		}		

		boolean simple = false;
		if(	sub_obj.getClass().isPrimitive() ||
				sub_obj instanceof String ||
				sub_obj instanceof Number ||
				sub_obj instanceof Date ||
				sub_obj instanceof Boolean)
			simple = true;
		
		if(	!simple){
			if(map_name==null){
				if(notFirst) result+=",\n";
				result+=space+"{\n";
			}else{
				if(notFirst) result+=",\n";
			}
		}else{
			if(map_name==null){
				if(notFirst) result+=",\n";
				result+=space+"";
			}else{
				if(notFirst) result+=",\n";
				result+="";
			}
			
		}

		if(map_name!=null){
			result+=space+"\""+(map_name)+"\":";
			if(	!simple)
				result+="{\n";				
			
		}
		

		return result;
	}
	
	
	private static String generateJsonItemTag_Content(Object sub_obj, String name, int level, Map avoidCyclicPointers, Serialized annotation, boolean serializeChildren, int serializeDepth, Map treeFilters, WriteValidator validator, Stack objList){
		if(sub_obj==null || (name!=null && name.equals("Class"))) return "";
		String space=spaceLevel(level);
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

				String result_tmp="";
				for(int i=0;i<list.size();i++) {
						
					boolean nFirst = true;
					if(result_tmp.length()==0) nFirst=false;
					
					Object sub_obj2=list.get(i);
					if(sub_obj2!=null){			
						
						Serialized sub_annotation = sub_obj2.getClass().getAnnotation(Serialized.class);
						if(annotation!=null || sub_annotation!=null || serializeChildren || serializeDepth>0){

							if(avoidCyclicPointers.get(Integer.valueOf(System.identityHashCode(sub_obj2)))!=null){
								result_tmp+=generateJsonItemTag_Start(new Object(), null, level+1, sub_annotation, nFirst);
								result_tmp+="\"WARNING\":\"cyclic pointer\"";
								result_tmp+=generateJsonItemTag_Finish(new Object(), null, level+1, nFirst);
							}else{
								avoidCyclicPointers.put(Integer.valueOf(System.identityHashCode(sub_obj2)), sub_obj2.getClass().getName());
								result_tmp+=generateJsonItem(sub_obj2, null,level+1,nFirst,avoidCyclicPointers,sub_annotation,
										(sub_annotation!=null)?sub_annotation.children():false,
												(sub_annotation!=null && sub_annotation.depth()>0)
												?
													sub_annotation.depth()
												:
													(serializeDepth-1>=0)?serializeDepth-1:0
												,
												treeFilters,
												String.valueOf(i),
												validator,
												objList
											);
								avoidCyclicPointers.remove(Integer.valueOf(System.identityHashCode(sub_obj2)));									
							}
						}
					}else
						result_tmp+=generateJsonItem(sub_obj2, null,level+1,nFirst,avoidCyclicPointers,annotation,serializeChildren,(serializeDepth-1>=0)?serializeDepth:0,treeFilters,String.valueOf(i), validator, objList);
									        
			    }
			    return result+result_tmp;
			}else{
				Iterator it = Arrays.asList((Object[])sub_obj).iterator(); 
				String result_tmp="";
	
				int i=0;
			    while (it.hasNext()) {
	
					boolean nFirst = true;
					if(result_tmp.length()==0) nFirst=false;
					
					Object sub_obj2=it.next();
					if(sub_obj2!=null){			
						
						Serialized sub_annotation = sub_obj2.getClass().getAnnotation(Serialized.class);
						if(annotation!=null || sub_annotation!=null || serializeChildren || serializeDepth>0){							
						
							if(avoidCyclicPointers.get(Integer.valueOf(System.identityHashCode(sub_obj2)))!=null){
								result_tmp+=generateJsonItemTag_Start(new Object(), null, level+1, sub_annotation, nFirst);
								result_tmp+="\"WARNING\":\"cyclic pointer\"";
								result_tmp+=generateJsonItemTag_Finish(new Object(), null,level+1, nFirst);
							}else{
								avoidCyclicPointers.put(Integer.valueOf(System.identityHashCode(sub_obj2)), sub_obj2.getClass().getName());
								result_tmp+=generateJsonItem(sub_obj2, null,level+1,nFirst,avoidCyclicPointers,sub_annotation,
										(sub_annotation!=null)?sub_annotation.children():false,
												(sub_annotation!=null && sub_annotation.depth()>0)
												?
													sub_annotation.depth()
												:
													(serializeDepth-1>=0)?serializeDepth-1:0
												,
												treeFilters,
												String.valueOf(i),
												validator,
												objList);
								avoidCyclicPointers.remove(Integer.valueOf(System.identityHashCode(sub_obj2)));									
							}
						}
					}else
						result_tmp+=generateJsonItem(sub_obj2, null,level+1,nFirst,avoidCyclicPointers,annotation,serializeChildren,(serializeDepth-1>=0)?serializeDepth:0,treeFilters,String.valueOf(i), validator, objList);
					i++;
			    }
			    return result+result_tmp;
			}
			
		}		
		
		if(sub_obj instanceof List){
			String result_tmp="";
			
			
			List list_sub_obj = (List)sub_obj;
			for(int i=0;i<list_sub_obj.size();i++){
				boolean nFirst = true;
				if(result_tmp.length()==0) nFirst=false;
				
				Object sub_obj2=list_sub_obj.get(i);
				
				if(sub_obj2!=null){		
					
					Serialized sub_annotation = sub_obj2.getClass().getAnnotation(Serialized.class);
					if(annotation!=null || sub_annotation!=null || serializeChildren || serializeDepth>0){
					
						if(avoidCyclicPointers.get(Integer.valueOf(System.identityHashCode(sub_obj2)))!=null){
							result_tmp+=generateJsonItemTag_Start(new Object(), null, level+1, sub_annotation, nFirst);
							result_tmp+="\"WARNING\":\"cyclic pointer\"";
							result_tmp+=generateJsonItemTag_Finish(new Object(), null, level+1, nFirst);
						}else{
							avoidCyclicPointers.put(Integer.valueOf(System.identityHashCode(sub_obj2)), sub_obj2.getClass().getName());
							result_tmp+=generateJsonItem(
									sub_obj2,null,level+1,nFirst,avoidCyclicPointers,sub_annotation,
									(sub_annotation!=null)?sub_annotation.children():false,
									(sub_annotation!=null && sub_annotation.depth()>0)
									?
										sub_annotation.depth()
									:
										(serializeDepth-1>=0)?serializeDepth-1:0
									,
									treeFilters,
									String.valueOf(i),
									validator,
									objList);
							avoidCyclicPointers.remove(Integer.valueOf(System.identityHashCode(sub_obj2)));									
						}
					}
				}else
					result_tmp+=generateJsonItem(sub_obj2, null,level+1,nFirst,avoidCyclicPointers,annotation,serializeChildren,(serializeDepth-1>=0)?serializeDepth:0,treeFilters,String.valueOf(i), validator, objList);
				
			}
			return result+result_tmp;
		}
		
		if(sub_obj instanceof Set){

			Iterator it = Arrays.asList(((Set)sub_obj).toArray()).iterator(); 
			String result_tmp="";

			int i = 0;
		    while (it.hasNext()) {

				boolean nFirst = true;
				if(result_tmp.length()==0) nFirst=false;
				
				Object sub_obj2=it.next();
				if(sub_obj2!=null){			
					
					Serialized sub_annotation = sub_obj2.getClass().getAnnotation(Serialized.class);
					if(annotation!=null || sub_annotation!=null || serializeChildren || serializeDepth>0){							
					
						if(avoidCyclicPointers.get(Integer.valueOf(System.identityHashCode(sub_obj2)))!=null){
							result_tmp+=generateJsonItemTag_Start(new Object(), null, level+1, sub_annotation, nFirst);
							result_tmp+="\"WARNING\":\"cyclic pointer\"";
							result_tmp+=generateJsonItemTag_Finish(new Object(), null,level+1, nFirst);
						}else{
							avoidCyclicPointers.put(Integer.valueOf(System.identityHashCode(sub_obj2)), sub_obj2.getClass().getName());
							result_tmp+=generateJsonItem(sub_obj2, null,level+1,nFirst,avoidCyclicPointers,sub_annotation,
									(sub_annotation!=null)?sub_annotation.children():false,
											(sub_annotation!=null && sub_annotation.depth()>0)
											?
												sub_annotation.depth()
											:
												(serializeDepth-1>=0)?serializeDepth-1:0
											,
											treeFilters,
											String.valueOf(i),
											validator,
											objList);
							avoidCyclicPointers.remove(Integer.valueOf(System.identityHashCode(sub_obj2)));									
						}
					}
				}else
					result_tmp+=generateJsonItem(sub_obj2, null,level+1,nFirst,avoidCyclicPointers,annotation,serializeChildren,(serializeDepth-1>=0)?serializeDepth:0,treeFilters,String.valueOf(i), validator, objList);
				i++;			        
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
					
					Serialized sub_annotation = sub_obj2.getClass().getAnnotation(Serialized.class);
					if(annotation!=null || sub_annotation!=null || serializeChildren || serializeDepth>0){
					
						if(avoidCyclicPointers.get(Integer.valueOf(System.identityHashCode(sub_obj2)))!=null){
							result_tmp+=generateJsonItemTag_Start(new Object(), pair.getKey().toString(), level+1, sub_annotation, nFirst);
							result_tmp+="\"WARNING\":\"cyclic pointer\"";
							result_tmp+=generateJsonItemTag_Finish(new Object(), pair.getKey().toString(), level+1, nFirst);
						}else{
							avoidCyclicPointers.put(Integer.valueOf(System.identityHashCode(sub_obj2)), sub_obj2.getClass().getName());
							result_tmp+=generateJsonItem(sub_obj2, pair.getKey().toString(),level+1,nFirst,avoidCyclicPointers,sub_annotation,
									(sub_annotation!=null)?sub_annotation.children():false,
									(sub_annotation!=null && sub_annotation.depth()>0)
									?
										sub_annotation.depth()
									:
										(serializeDepth-1>=0)?serializeDepth-1:0
									,
									treeFilters,
									pair.getKey().toString(),
									validator,
									objList);
							avoidCyclicPointers.remove(Integer.valueOf(System.identityHashCode(sub_obj2)));									
						}
						
					}
				}else
					result_tmp+=generateJsonItem(sub_obj2, pair.getKey().toString(),level+1,nFirst,avoidCyclicPointers,annotation,serializeChildren,(serializeDepth-1>=0)?serializeDepth:0,treeFilters,pair.getKey().toString(), validator, objList);
							        
		    }
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
				if(annotation!=null && annotation.output()!=null){
					try{					
						value=util_format.makeFormatedString(annotation.output().format(), annotation.output().language(),annotation.output().country(), sub_obj);
						result+="\""+normalJSON(value,((annotation.output().characterset().equals(""))?((annotation.output().ascii())?"ascii":null):null))+"\"";
					}catch(Exception e){	
						result+="\""+normalJSON(value,null)+"\"";
					}	
				}else				
					result+="\""+normalJSON(value,null)+"\"";
				return result;
			}
			if(sub_obj instanceof Boolean){
				check=true;
				result+=normalJSON(value,null);
				return result;
			}
			
			if(sub_obj instanceof Number){
				check=true;
				if(sub_obj!=null && (sub_obj.toString().equalsIgnoreCase("NaN") || sub_obj.toString().equalsIgnoreCase("Infinity") || sub_obj.toString().equalsIgnoreCase("-Infinity"))){
					result = "\""+sub_obj.toString()+"\"";
				}
				else if(annotation!=null &&
						annotation.output()!=null &&
						annotation.output().format()!=null &&
						!annotation.output().format().equals("")
						){
					try{					
						value=util_format.makeFormatedString(annotation.output().format(), annotation.output().language(),annotation.output().country(), sub_obj);
						result+="\""+normalJSON(value,((annotation.output().characterset().equals(""))?((annotation.output().ascii())?"ascii":null):null))+"\"";
					}catch(Exception e){	
						result+=normalJSON(value,null);
					}	
				}else				
					result+=normalJSON(value,null);

				return result;
			}
			if(sub_obj instanceof Date){
				check=true;
				if(annotation!=null && annotation.output()!=null){
					try{					
						value=util_format.makeFormatedString(annotation.output().format(), annotation.output().language(),annotation.output().country(), sub_obj);
						result+="\""+normalJSON(value,((annotation.output().characterset().equals(""))?((annotation.output().ascii())?"ascii":null):null))+"\"";
					}catch(Exception e){	
						result+="\""+normalJSON(value,null)+"\"";
					}	
				}else				
					result+="\""+normalJSON(value,null)+"\"";

				return result;
			}
			if(!check){
				try{
					java.text.DecimalFormat df = new java.text.DecimalFormat("##0.000000", new DecimalFormatSymbols(new Locale("en")));
					result+= df.format(new java.math.BigDecimal(value.trim()).doubleValue());
					return result;
				}catch(Exception e){
					result+=normalJSON(value,null);
					return result;
				}
			}

		}


			String result_tmp="";
			Map name_tmp = new HashMap();
			try{
				String[] prefixes = new String[]{"get","is"};
				
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
								
								if(!sub_obj2.equals(sub_obj)){
										boolean nFirst = true;
										if(result_tmp.length()==0) nFirst=false;
										
									
										if(avoidCyclicPointers.get(Integer.valueOf(System.identityHashCode(sub_obj2)))!=null){
												result_tmp+=generateJsonItemTag_Start(new Object(), methodName, level+1, sub_annotation, nFirst);
												result_tmp+="\"WARNING\":\"cyclic pointer\"";
												result_tmp+=generateJsonItemTag_Finish(new Object(), methodName, level+1, nFirst);
										}else{
											String map_name = util_reflect.revAdaptMethodName(methodName);
											if(sub_annotation!=null && sub_annotation.output()!=null && sub_annotation.output().name()!=null && !sub_annotation.output().name().equals(""))
												map_name = sub_annotation.output().name();
											name_tmp.put(map_name, map_name);
											
											if(sub_annotation!=null && sub_annotation.order()>-1){
												OrderContainer oc = new JsonWriter2().new OrderContainer();
												oc.sub_obj = sub_obj2;
												oc.name = methodName;
												oc.annotation = sub_annotation;
												oc.serializeChildren = (sub_annotation!=null)?sub_annotation.children():false;
												oc.serializeDepth = (sub_annotation!=null && sub_annotation.depth()>0)
															?
																sub_annotation.depth()
															:
																(serializeDepth-1>=0)?serializeDepth-1:0;
												ordered.put(sub_annotation.order(), oc);				
											}else{
												avoidCyclicPointers.put(Integer.valueOf(System.identityHashCode(sub_obj2)), sub_obj2.getClass().getName());
												result_tmp+=generateJsonItem(
														sub_obj2, methodName,level+1,nFirst,avoidCyclicPointers,sub_annotation,
														(sub_annotation!=null)?sub_annotation.children():false,
														(sub_annotation!=null && sub_annotation.depth()>0)
														?
															sub_annotation.depth()
														:
															(serializeDepth-1>=0)?serializeDepth-1:0
														,
														treeFilters,
														null,
														validator,
														objList);
												avoidCyclicPointers.remove(Integer.valueOf(System.identityHashCode(sub_obj2)));		
											}
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
									boolean nFirst = true;
									if(result_tmp.length()==0) nFirst=false;
									
									if(avoidCyclicPointers.get(Integer.valueOf(System.identityHashCode(sub_obj2)))!=null){
										result_tmp+=generateJsonItemTag_Start(new Object(), fieldName, level+1, sub_annotation, nFirst);
										result_tmp+="\"WARNING\":\"cyclic pointer\"";
										result_tmp+=generateJsonItemTag_Finish(new Object(), fieldName, level+1, nFirst);
									}else{
										if(sub_annotation!=null && sub_annotation.order()>-1){
											OrderContainer oc = new JsonWriter2().new OrderContainer();
											oc.sub_obj = sub_obj2;
											oc.name = fieldName;
											oc.annotation = sub_annotation;
											oc.serializeChildren = (sub_annotation!=null)?sub_annotation.children():false;
											oc.serializeDepth = (sub_annotation!=null && sub_annotation.depth()>0)
														?
															sub_annotation.depth()
														:
															(serializeDepth-1>=0)?serializeDepth-1:0;
											ordered.put(sub_annotation.order(), oc);				
										}else{	
											avoidCyclicPointers.put(Integer.valueOf(System.identityHashCode(sub_obj2)), sub_obj2.getClass().getName());											 
											result_tmp+=generateJsonItem(
													sub_obj2, fieldName,level+1,nFirst,avoidCyclicPointers,sub_annotation,
													(sub_annotation!=null)?sub_annotation.children():false,
													(sub_annotation!=null && sub_annotation.depth()>0)
													?
														sub_annotation.depth()
													:
														(serializeDepth-1>=0)?serializeDepth-1:0
													,
													treeFilters,
													null,
													validator,
													objList);												
											avoidCyclicPointers.remove(Integer.valueOf(System.identityHashCode(sub_obj2)));	
										}
									}							
								}
							}							
						}						
					}	
				}					
				
				if(ordered.size()>0) {
					Iterator it = ordered.values().iterator();
					 
					while (it.hasNext()) {
						OrderContainer oc = (OrderContainer)it.next();
						if(oc!=null && oc.sub_obj!=null) {
							boolean nFirst = true;
							if(result_tmp.length()==0) 
								nFirst=false;
							avoidCyclicPointers.put(Integer.valueOf(System.identityHashCode(oc.sub_obj)), oc.sub_obj.getClass().getName());										 
							result_tmp+=generateJsonItem(
									oc.sub_obj,
									oc.name,
									level+1,
									nFirst,
									avoidCyclicPointers,
									oc.annotation,
									oc.serializeChildren,
									oc.serializeDepth,
									treeFilters,
									null,
									validator,
									objList);												
							avoidCyclicPointers.remove(Integer.valueOf(System.identityHashCode(oc.sub_obj)));
						}
					}
				}
				ordered.clear();
				ordered=null;				
				
				return result+result_tmp;
			}catch(Exception e){				
			}
			

			
		result="\"WARNING: unknown\""; 


		return result;
	}

	private static String generateJsonItemTag_Finish(Object sub_obj, String name, int level, boolean notFirst){
		if(sub_obj==null || (name!=null && name.equals("Class"))) return "";
		String space=spaceLevel(level);

		if(sub_obj.getClass().isArray()){
			return "\n"+space+"]";
		}
		if(sub_obj instanceof List ){
			return "\n"+space+"]";
		}
		if(sub_obj instanceof Map){
			return  "\n"+space+"}";
		}
		if(sub_obj instanceof Set ){
			return "\n"+space+"]";
		}
		
		
		
		if(	sub_obj.getClass().isPrimitive() ||
				sub_obj instanceof String ||
				sub_obj instanceof Number ||
				sub_obj instanceof Date ||
				sub_obj instanceof Boolean)
			
			return "";
		else
			return "\n"+space+"}";
	}	
	
	
	


	private static String spaceLevel(int level){
		String result="";
		for(int i=0;i<level;i++) result+="     ";
		return result;
	}


	
	private static String normalJSON(String input, String encoding) {

		if (input==null) return input;


		String result="";
		
//		if (
//			input.indexOf('\\')>-1 ||
//			input.indexOf('\"')>-1 ||
//			input.indexOf('\b')>-1 ||
//			input.indexOf('\f')>-1 ||
//			input.indexOf('\n')>-1 ||
//			input.indexOf('\r')>-1 ||
//			input.indexOf('\t')>-1
//			
//		) {

			for (int i=0;i<input.length();i++) {
				char c = input.charAt(i);
				if (c=='\\') 
					result+="\\\\";
				else if (c=='"') 
					result+="\\\"";
				else if (c=='\b') 
					result+="\\b";
				else if (c=='\f') 
					result+="\\f";
				else if (c=='\n') 
					result+="\\n";
				else if (c=='\r') 
					result+="\\r";
				else if (c=='\t') 
					result+="\\t";
				else if (c<' '){
					String t = "000" + Integer.toHexString(c);
					result+=("\\u" + t.substring(t.length() - 4));
				}else 
					result+=c;
			}
			
//		}
//		else
//			result = input;
		
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
	
	private static String getOutputName(String name, Serialized annotation) {
		String map_name = name;
		if(name!=null){
			map_name = util_reflect.revAdaptMethodName(name);
			if(annotation!=null && annotation.output()!=null && !annotation.output().name().equals(""))
				map_name = annotation.output().name();
		}
		return map_name;
	}
	
	class OrderContainer{
		Object sub_obj;
		String name;
		int level;
		Serialized annotation;
		boolean serializeChildren;
		int serializeDepth;
	}
}
