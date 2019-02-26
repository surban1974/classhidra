package it.classhidra.serialize;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import it.classhidra.core.tool.util.util_reflect;
import it.classhidra.core.tool.util.util_xml;



public class MapWriter {

	
	public static Map<String,Object> object2map(Object obj){
		return object2map(obj,null);
	}
	
	public static Map<String,Object> object2map(Object obj, String name){
		return object2map(obj,name,null);
	}	

	public static Map<String,Object> object2map(Object obj, String name, List filters){
		Map<String,Object> map= new HashMap<String,Object>();
		if(obj!=null){
			Serialized annotation = obj.getClass().getAnnotation(Serialized.class);		
			String map_name = name;
			if(name==null && annotation!=null && annotation.output()!=null && !annotation.output().name().equals(""))
				map_name = annotation.output().name();
			else if(name==null && obj!=null && (obj instanceof List || obj.getClass().isArray()))
				map_name = "items";
			else if(name==null)
				map_name = "item";

			generateMap(map,obj,map_name,0,false,new HashMap(),null, (annotation!=null)?annotation.children():false, (annotation!=null)?annotation.depth():0, util_xml.convertFilters(filters));

		}
//		if(name==null){
//			Map<String,Object> rmap = (Map<String,Object>)map.get(null);
//			if(rmap!=null)
//				return rmap;
//		}
		return map;
	}	

	
	public static Map<String,Object> object2map(Object obj, String name, List filters, boolean children, int depth){
		Map<String,Object> map = new HashMap<String,Object>();
		if(obj!=null){
			Serialized annotation = obj.getClass().getAnnotation(Serialized.class);		
			String map_name = name;
			if(name==null && annotation!=null && annotation.output()!=null && !annotation.output().name().equals(""))
				map_name = annotation.output().name();
			else if(name==null && obj!=null && (obj instanceof List || obj.getClass().isArray()))
				map_name = "items";
			else if(name==null)
				map_name = "item";

			generateMap(
							map,
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
							util_xml.convertFilters(filters)
							);
		}
		return map;
	}	
	

	private static Object generateMap(Map<String,Object> map, Object sub_obj, String name, int level, boolean notFirst, Map avoidCyclicPointers, Serialized annotation, boolean serializeChildren, int serializeDepth, Map treeFilters){

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
		if(goAhead)			
			return generateMapContent(map, sub_obj, name, level,avoidCyclicPointers, annotation, serializeChildren, serializeDepth, subTreeFilters);

		return null;
	}

	
	
	private static Object generateMapContent(Map<String,Object> map, Object sub_obj, String name, int level, Map avoidCyclicPointers, Serialized annotation, boolean serializeChildren, int serializeDepth, Map treeFilters){

		String map_name = name;
		if(name!=null){
			map_name = util_reflect.revAdaptMethodName(name);
			if(annotation!=null && annotation.output()!=null && !annotation.output().name().equals(""))
				map_name = annotation.output().name();
		}
		
		if(sub_obj==null || (name!=null && name.equals("Class")))
			return null;
		
		if(map==null)
			map = new HashMap<String,Object>();

		if(sub_obj==null) return null;

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

				List toMap = new ArrayList();

				for(int i=0;i<list.size();i++) {
						
					boolean nFirst = true;
					
					Object sub_obj2=list.get(i);
					if(sub_obj2!=null){			
						
						Serialized sub_annotation = sub_obj2.getClass().getAnnotation(Serialized.class);
						if(annotation!=null || sub_annotation!=null || serializeChildren || serializeDepth>0){

							if(avoidCyclicPointers.get(Integer.valueOf(System.identityHashCode(sub_obj2)))!=null){

								toMap.add("\"WARNING\":\"cyclic pointer\"");
							}else{
								avoidCyclicPointers.put(Integer.valueOf(System.identityHashCode(sub_obj2)), sub_obj2.getClass().getName());
								toMap.add(
									generateMap(null, sub_obj2, null,level+1,nFirst,avoidCyclicPointers,sub_annotation,
										(sub_annotation!=null)?sub_annotation.children():false,
												(sub_annotation!=null && sub_annotation.depth()>0)
												?
													sub_annotation.depth()
												:
													(serializeDepth-1>=0)?serializeDepth-1:0
												,
												treeFilters)
								);
								avoidCyclicPointers.remove(Integer.valueOf(System.identityHashCode(sub_obj2)));									
							}
						}
					}else
						toMap.add(generateMap(null, sub_obj2, null,level+1,nFirst,avoidCyclicPointers,annotation,serializeChildren,(serializeDepth-1>=0)?serializeDepth:0,treeFilters));
									        
			    }
				
				map.put(map_name, toMap);
			    return toMap;
			}else{
				Iterator it = Arrays.asList((Object[])sub_obj).iterator(); 
	
				List list = new ArrayList();
			    while (it.hasNext()) {
	
					boolean nFirst = true;

					
					Object sub_obj2=it.next();
					if(sub_obj2!=null){			
						
						Serialized sub_annotation = sub_obj2.getClass().getAnnotation(Serialized.class);
						if(annotation!=null || sub_annotation!=null || serializeChildren || serializeDepth>0){							
						
							if(avoidCyclicPointers.get(Integer.valueOf(System.identityHashCode(sub_obj2)))!=null){

								list.add("\"WARNING\":\"cyclic pointer\"");

							}else{
								avoidCyclicPointers.put(Integer.valueOf(System.identityHashCode(sub_obj2)), sub_obj2.getClass().getName());
								list.add(generateMap(null, sub_obj2, null,level+1,nFirst,avoidCyclicPointers,sub_annotation,
										(sub_annotation!=null)?sub_annotation.children():false,
												(sub_annotation!=null && sub_annotation.depth()>0)
												?
													sub_annotation.depth()
												:
													(serializeDepth-1>=0)?serializeDepth-1:0
												,
												treeFilters));
								avoidCyclicPointers.remove(Integer.valueOf(System.identityHashCode(sub_obj2)));									
							}
						}
					}else
						list.add(generateMap(null, sub_obj2, null,level+1,nFirst,avoidCyclicPointers,annotation,serializeChildren,(serializeDepth-1>=0)?serializeDepth:0,treeFilters));
								        
			    }
			    map.put(map_name, list);
			    return list;
			}
			
		}		
		
		if(sub_obj instanceof List){

			
			
			List list_sub_obj = (List)sub_obj;
			List toMap = new ArrayList();
			for(int i=0;i<list_sub_obj.size();i++){
				boolean nFirst = true;

				
				Object sub_obj2=list_sub_obj.get(i);
				
				if(sub_obj2!=null){		
					
					Serialized sub_annotation = sub_obj2.getClass().getAnnotation(Serialized.class);
					if(annotation!=null || sub_annotation!=null || serializeChildren || serializeDepth>0){
					
						if(avoidCyclicPointers.get(Integer.valueOf(System.identityHashCode(sub_obj2)))!=null){

							toMap.add("\"WARNING\":\"cyclic pointer\"");

						}else{
							avoidCyclicPointers.put(Integer.valueOf(System.identityHashCode(sub_obj2)), sub_obj2.getClass().getName());
							toMap.add(
								generateMap(
									null,
									sub_obj2,null,level+1,nFirst,avoidCyclicPointers,sub_annotation,
									(sub_annotation!=null)?sub_annotation.children():false,
									(sub_annotation!=null && sub_annotation.depth()>0)
									?
										sub_annotation.depth()
									:
										(serializeDepth-1>=0)?serializeDepth-1:0
									,
									treeFilters)
							);
							avoidCyclicPointers.remove(Integer.valueOf(System.identityHashCode(sub_obj2)));									
						}
					}
				}else
					list_sub_obj.set(i,generateMap(null,sub_obj2, null,level+1,nFirst,avoidCyclicPointers,annotation,serializeChildren,(serializeDepth-1>=0)?serializeDepth:0,treeFilters));
				
			}
			map.put(map_name, toMap);
		    return toMap;
		}

		if(sub_obj instanceof Map){

			
			Iterator it = ((Map)sub_obj).entrySet().iterator(); 
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
				boolean nFirst = true;

				
				Object sub_obj2=pair.getValue();
				if(sub_obj2!=null){		
					
					Serialized sub_annotation = sub_obj2.getClass().getAnnotation(Serialized.class);
					if(annotation!=null || sub_annotation!=null || serializeChildren || serializeDepth>0){					
						if(avoidCyclicPointers.get(Integer.valueOf(System.identityHashCode(sub_obj2)))!=null)
							pair.setValue("\"WARNING\":\"cyclic pointer\"");
						else{
							avoidCyclicPointers.put(Integer.valueOf(System.identityHashCode(sub_obj2)), sub_obj2.getClass().getName());
							pair.setValue(generateMap(null,sub_obj2, pair.getKey().toString(),level+1,nFirst,avoidCyclicPointers,sub_annotation,
									(sub_annotation!=null)?sub_annotation.children():false,
									(sub_annotation!=null && sub_annotation.depth()>0)
									?
										sub_annotation.depth()
									:
										(serializeDepth-1>=0)?serializeDepth-1:0
									,
									treeFilters));
							avoidCyclicPointers.remove(Integer.valueOf(System.identityHashCode(sub_obj2)));									
						}
						
					}
				}else
					pair.setValue(generateMap(null,sub_obj2, pair.getKey().toString(),level+1,nFirst,avoidCyclicPointers,annotation,serializeChildren,(serializeDepth-1>=0)?serializeDepth:0,treeFilters));
							        
		    }
		    map.put(map_name, sub_obj);
		    return sub_obj;
		}
		

		


		if(	sub_obj.getClass().isPrimitive() ||
			sub_obj instanceof String ||
			sub_obj instanceof Number ||
			sub_obj instanceof Date ||
			sub_obj instanceof Boolean){
			
		    map.put(map_name, sub_obj);
		    return sub_obj;
		}


		Map<String,Object> result_tmp = new HashMap<String,Object>();
			try{
				String[] prefixes = new String[]{"get","is"};
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
										if(result_tmp.size()==0) nFirst=false;
										
									
											if(avoidCyclicPointers.get(Integer.valueOf(System.identityHashCode(sub_obj2)))!=null){

//												result_tmp+="\"WARNING\":\"cyclic pointer\"";
											}else{
												avoidCyclicPointers.put(Integer.valueOf(System.identityHashCode(sub_obj2)), sub_obj2.getClass().getName());
												 
													generateMap(
														result_tmp,
														sub_obj2, methodName,level+1,nFirst,avoidCyclicPointers,sub_annotation,
														(sub_annotation!=null)?sub_annotation.children():false,
														(sub_annotation!=null && sub_annotation.depth()>0)
														?
															sub_annotation.depth()
														:
															(serializeDepth-1>=0)?serializeDepth-1:0
														,
														treeFilters);
													
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
							if(sub_map_name2!=null && result_tmp.get(sub_map_name2)==null){
								Object sub_obj2 = null;
								if(!fields[j].isAccessible()){
									fields[j].setAccessible(true);
									sub_obj2 = fields[j].get(sub_obj);
									fields[j].setAccessible(false);
								}else
									sub_obj2 = fields[j].get(sub_obj);
									
								if(sub_obj2!=null && !sub_obj2.equals(sub_obj)){
									boolean nFirst = true;
									if(result_tmp.size()==0) nFirst=false;
									
									if(avoidCyclicPointers.get(Integer.valueOf(System.identityHashCode(sub_obj2)))!=null){

//										result_tmp+="\"WARNING\":\"cyclic pointer\"";
									}else{
										avoidCyclicPointers.put(Integer.valueOf(System.identityHashCode(sub_obj2)), sub_obj2.getClass().getName());
										 
											generateMap(
												result_tmp,
												sub_obj2, fieldName,level+1,nFirst,avoidCyclicPointers,sub_annotation,
												(sub_annotation!=null)?sub_annotation.children():false,
												(sub_annotation!=null && sub_annotation.depth()>0)
												?
													sub_annotation.depth()
												:
													(serializeDepth-1>=0)?serializeDepth-1:0
												,
												treeFilters);
											
										avoidCyclicPointers.remove(Integer.valueOf(System.identityHashCode(sub_obj2)));									
									}
							
								}
							}
							
						}
						
					}	
				}					
				
			    map.put(map_name, result_tmp);
			    return result_tmp;
			}catch(Exception e){	
				e.toString();
			}

		return null;
	}


	




}

