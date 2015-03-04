package it.classhidra.core.tool.util;

import it.classhidra.core.controller.i_bean;
import it.classhidra.core.tool.elements.i_elementBase;
import it.classhidra.core.tool.elements.i_elementDBBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class util_find {

	public static Object findElementFromListASText(Vector elements, Object obj_value, String key){
		if(obj_value==null || key==null) return null;
		for(int i=0;i<elements.size();i++){
			if(elements.get(i) instanceof i_bean){
				i_bean el = (i_bean)elements.get(i);
				if(el!=null && obj_value.toString().equals(el.get(key).toString())) return el;
			}
			if(elements.get(i) instanceof i_elementDBBase){
				i_elementDBBase el = (i_elementDBBase)elements.get(i);
				if(el!=null && obj_value.toString().equals(el.getCampoValue(key).toString())) return el;
			}
			if(elements.get(i) instanceof i_elementBase){
				i_elementBase el = (i_elementBase)elements.get(i);
				if(el!=null && obj_value.toString().equals(el.getCampoValue(key).toString())) return el;
			}
			
		}
		return null;
	}



	public static Object findElementFromList(Vector elements, Object obj_value, String key){
		if(obj_value==null || key==null) return null;
		for(int i=0;i<elements.size();i++){
			if(elements.get(i) instanceof i_bean){
				i_bean el = (i_bean)elements.get(i);
				if(el!=null && obj_value.equals(el.get(key))) return el;
			}
			if(elements.get(i) instanceof i_elementDBBase){
				i_elementDBBase el = (i_elementDBBase)elements.get(i);
				if(el!=null && obj_value.equals(el.getCampoValue(key))) return el;
			}
			if(elements.get(i) instanceof i_elementBase){
				i_elementBase el = (i_elementBase)elements.get(i);
				if(el!=null && obj_value.toString().equals(el.getCampoValue(key))) return el;
			}
			
		}
		return null;
	}

	public static Vector findElementsFromListAsText(Vector elements, Object obj_value,  String key){
		Vector result = new Vector();
		if(obj_value==null || key==null) return null;
		for(int i=0;i<elements.size();i++){
			if(elements.get(i) instanceof i_bean){
				i_bean el = (i_bean)elements.get(i);
				if(el!=null && obj_value.toString().equals(el.get(key).toString())) result.add(el);
			}else if(elements.get(i) instanceof i_elementDBBase){
				i_elementDBBase el = (i_elementDBBase)elements.get(i);
				if(el!=null && obj_value.toString().equals(el.getCampoValue(key).toString())) result.add(el);
			}else if(elements.get(i) instanceof i_elementBase){
				i_elementBase el = (i_elementBase)elements.get(i);
				if(el!=null && obj_value.toString().equals(el.getCampoValue(key).toString())) result.add(el);
			}
			
		}
		return result;
	}	
	public static Vector findElementsFromList(Vector elements, Object obj_value,  String key){
		Vector result = new Vector();
		if(obj_value==null || key==null) return null;
		for(int i=0;i<elements.size();i++){
			if(elements.get(i) instanceof i_bean){
				i_bean el = (i_bean)elements.get(i);
				if(el!=null && obj_value.equals(el.get(key))) result.add(el);
			}else if(elements.get(i) instanceof i_elementDBBase){
				i_elementDBBase el = (i_elementDBBase)elements.get(i);
				if(el!=null && obj_value.equals(el.getCampoValue(key))) result.add(el);
			}else if(elements.get(i) instanceof i_elementBase){
				i_elementBase el = (i_elementBase)elements.get(i);
				if(el!=null && obj_value.equals(el.getCampoValue(key))) result.add(el);
			}			
		}
		return result;
	}
	public static Object findElementFromListASText(Vector elements, Object[] obj_value, String[] key){
		if(obj_value==null || key==null) return null;
		for(int i=0;i<elements.size();i++){
			if(elements.get(i) instanceof i_bean){
				i_bean el = (i_bean)elements.get(i);
				if(el!=null){
					boolean isEqual=true;
					for(int j=0;j<obj_value.length;j++){
						isEqual&=obj_value[j].toString().equals(el.get(key[j]).toString());
					}
					if(isEqual) return el;
				}

			}
			if(elements.get(i) instanceof i_elementDBBase){
				i_elementDBBase el = (i_elementDBBase)elements.get(i);
				if(el!=null){
					boolean isEqual=true;
					for(int j=0;j<obj_value.length;j++){
						isEqual&=obj_value[j].toString().equals(el.getCampoValue(key[j]).toString());
					}
					if(isEqual) return el;
				}

			}
			if(elements.get(i) instanceof i_elementBase){
				i_elementBase el = (i_elementBase)elements.get(i);
				if(el!=null){
					boolean isEqual=true;
					for(int j=0;j<obj_value.length;j++){
						isEqual&=obj_value[j].toString().equals(el.getCampoValue(key[j]).toString());
					}
					if(isEqual) return el;
				}
			}
			
		}
		return null;
	}
	
	public static Object findElementFromList(Vector elements, Object[] obj_value, String[] key){
		if(obj_value==null || key==null) return null;
		for(int i=0;i<elements.size();i++){
			if(elements.get(i) instanceof i_bean){
				i_bean el = (i_bean)elements.get(i);
				if(el!=null){
					boolean isEqual=true;
					for(int j=0;j<obj_value.length;j++){
						isEqual&=obj_value[j].equals(el.get(key[j]));
					}
					if(isEqual) return el;
				}
			}
			if(elements.get(i) instanceof i_elementDBBase){
				i_elementDBBase el = (i_elementDBBase)elements.get(i);
				if(el!=null){
					boolean isEqual=true;
					for(int j=0;j<obj_value.length;j++){
						isEqual&=obj_value[j].equals(el.getCampoValue(key[j]));
					}
					if(isEqual) return el;
				}
			}
			if(elements.get(i) instanceof i_elementBase){
				i_elementBase el = (i_elementBase)elements.get(i);
				if(el!=null){
					boolean isEqual=true;
					for(int j=0;j<obj_value.length;j++){
						isEqual&=obj_value[j].equals(el.getCampoValue(key[j]));
					}
					if(isEqual) return el;
				}
			}
			
		}
		return null;
	}
	

	public static Vector findElementsFromListAsText(Vector elements, Object obj_value[],  String key[]){
		Vector result = new Vector();
		if(obj_value==null || key==null) return null;
		for(int i=0;i<elements.size();i++){
			if(elements.get(i) instanceof i_bean){
				i_bean el = (i_bean)elements.get(i);
				if(el!=null){
					boolean isEqual=true;
					for(int j=0;j<obj_value.length;j++){
						isEqual&=obj_value[j].toString().equals(el.get(key[j]).toString());
					}
					if(isEqual) result.add(el);
				}
			}else if(elements.get(i) instanceof i_elementDBBase){
				i_elementDBBase el = (i_elementDBBase)elements.get(i);
				if(el!=null){
					boolean isEqual=true;
					for(int j=0;j<obj_value.length;j++){
						isEqual&=obj_value[j].toString().equals(el.getCampoValue(key[j]).toString());
					}
					if(isEqual) result.add(el);
				}
			}else if(elements.get(i) instanceof i_elementBase){
				i_elementBase el = (i_elementBase)elements.get(i);
				if(el!=null){
					boolean isEqual=true;
					for(int j=0;j<obj_value.length;j++){
						isEqual&=obj_value[j].toString().equals(el.getCampoValue(key[j]).toString());
					}
					if(isEqual) result.add(el);
				}
			}
			
		}
		return result;
	}	
	public static Vector findElementsFromList(Vector elements, Object obj_value[],  String[] key){
		Vector result = new Vector();
		if(obj_value==null || key==null) return null;
		for(int i=0;i<elements.size();i++){
			if(elements.get(i) instanceof i_bean){
				i_bean el = (i_bean)elements.get(i);
				if(el!=null){
					boolean isEqual=true;
					for(int j=0;j<obj_value.length;j++){
						isEqual&=obj_value[j].equals(el.get(key[j]));
					}
					if(isEqual) result.add(el);
				}
			}else if(elements.get(i) instanceof i_elementDBBase){
				i_elementDBBase el = (i_elementDBBase)elements.get(i);
				if(el!=null){
					boolean isEqual=true;
					for(int j=0;j<obj_value.length;j++){
						isEqual&=obj_value[j].equals(el.getCampoValue(key[j]));
					}
					if(isEqual) result.add(el);
				}
			}else if(elements.get(i) instanceof i_elementBase){
				i_elementBase el = (i_elementBase)elements.get(i);
				if(el!=null){
					boolean isEqual=true;
					for(int j=0;j<obj_value.length;j++){
						isEqual&=obj_value[j].equals(el.getCampoValue(key[j]));
					}
					if(isEqual) result.add(el);
				}
			}			
		}
		return result;
	}	

	
	
	
	public static Object findElementFromListASText(List elements, Object obj_value, String key){
		if(obj_value==null || key==null) return null;
		for(int i=0;i<elements.size();i++){
			if(elements.get(i) instanceof i_bean){
				i_bean el = (i_bean)elements.get(i);
				if(el!=null && obj_value.toString().equals(el.get(key).toString())) return el;
			}
			if(elements.get(i) instanceof i_elementDBBase){
				i_elementDBBase el = (i_elementDBBase)elements.get(i);
				if(el!=null && obj_value.toString().equals(el.getCampoValue(key).toString())) return el;
			}
			if(elements.get(i) instanceof i_elementBase){
				i_elementBase el = (i_elementBase)elements.get(i);
				if(el!=null && obj_value.toString().equals(el.getCampoValue(key).toString())) return el;
			}
			
		}
		return null;
	}



	public static Object findElementFromList(List elements, Object obj_value, String key){
		if(obj_value==null || key==null) return null;
		for(int i=0;i<elements.size();i++){
			if(elements.get(i) instanceof i_bean){
				i_bean el = (i_bean)elements.get(i);
				if(el!=null && obj_value.equals(el.get(key))) return el;
			}
			if(elements.get(i) instanceof i_elementDBBase){
				i_elementDBBase el = (i_elementDBBase)elements.get(i);
				if(el!=null && obj_value.equals(el.getCampoValue(key))) return el;
			}
			if(elements.get(i) instanceof i_elementBase){
				i_elementBase el = (i_elementBase)elements.get(i);
				if(el!=null && obj_value.toString().equals(el.getCampoValue(key))) return el;
			}
			
		}
		return null;
	}

	public static List findElementsFromListAsText(List elements, Object obj_value,  String key){
		List result = new ArrayList();
		if(obj_value==null || key==null) return null;
		for(int i=0;i<elements.size();i++){
			if(elements.get(i) instanceof i_bean){
				i_bean el = (i_bean)elements.get(i);
				if(el!=null && obj_value.toString().equals(el.get(key).toString())) result.add(el);
			}else if(elements.get(i) instanceof i_elementDBBase){
				i_elementDBBase el = (i_elementDBBase)elements.get(i);
				if(el!=null && obj_value.toString().equals(el.getCampoValue(key).toString())) result.add(el);
			}else if(elements.get(i) instanceof i_elementBase){
				i_elementBase el = (i_elementBase)elements.get(i);
				if(el!=null && obj_value.toString().equals(el.getCampoValue(key).toString())) result.add(el);
			}
			
		}
		return result;
	}	
	public static List findElementsFromList(List elements, Object obj_value,  String key){
		List result = new ArrayList();
		if(obj_value==null || key==null) return null;
		for(int i=0;i<elements.size();i++){
			if(elements.get(i) instanceof i_bean){
				i_bean el = (i_bean)elements.get(i);
				if(el!=null && obj_value.equals(el.get(key))) result.add(el);
			}else if(elements.get(i) instanceof i_elementDBBase){
				i_elementDBBase el = (i_elementDBBase)elements.get(i);
				if(el!=null && obj_value.equals(el.getCampoValue(key))) result.add(el);
			}else if(elements.get(i) instanceof i_elementBase){
				i_elementBase el = (i_elementBase)elements.get(i);
				if(el!=null && obj_value.equals(el.getCampoValue(key))) result.add(el);
			}			
		}
		return result;
	}
	public static Object findElementFromListASText(List elements, Object[] obj_value, String[] key){
		if(obj_value==null || key==null) return null;
		for(int i=0;i<elements.size();i++){
			if(elements.get(i) instanceof i_bean){
				i_bean el = (i_bean)elements.get(i);
				if(el!=null){
					boolean isEqual=true;
					for(int j=0;j<obj_value.length;j++){
						isEqual&=obj_value[j].toString().equals(el.get(key[j]).toString());
					}
					if(isEqual) return el;
				}

			}
			if(elements.get(i) instanceof i_elementDBBase){
				i_elementDBBase el = (i_elementDBBase)elements.get(i);
				if(el!=null){
					boolean isEqual=true;
					for(int j=0;j<obj_value.length;j++){
						isEqual&=obj_value[j].toString().equals(el.getCampoValue(key[j]).toString());
					}
					if(isEqual) return el;
				}

			}
			if(elements.get(i) instanceof i_elementBase){
				i_elementBase el = (i_elementBase)elements.get(i);
				if(el!=null){
					boolean isEqual=true;
					for(int j=0;j<obj_value.length;j++){
						isEqual&=obj_value[j].toString().equals(el.getCampoValue(key[j]).toString());
					}
					if(isEqual) return el;
				}
			}
			
		}
		return null;
	}
	
	public static Object findElementFromList(List elements, Object[] obj_value, String[] key){
		if(obj_value==null || key==null) return null;
		for(int i=0;i<elements.size();i++){
			if(elements.get(i) instanceof i_bean){
				i_bean el = (i_bean)elements.get(i);
				if(el!=null){
					boolean isEqual=true;
					for(int j=0;j<obj_value.length;j++){
						isEqual&=obj_value[j].equals(el.get(key[j]));
					}
					if(isEqual) return el;
				}
			}
			if(elements.get(i) instanceof i_elementDBBase){
				i_elementDBBase el = (i_elementDBBase)elements.get(i);
				if(el!=null){
					boolean isEqual=true;
					for(int j=0;j<obj_value.length;j++){
						isEqual&=obj_value[j].equals(el.getCampoValue(key[j]));
					}
					if(isEqual) return el;
				}
			}
			if(elements.get(i) instanceof i_elementBase){
				i_elementBase el = (i_elementBase)elements.get(i);
				if(el!=null){
					boolean isEqual=true;
					for(int j=0;j<obj_value.length;j++){
						isEqual&=obj_value[j].equals(el.getCampoValue(key[j]));
					}
					if(isEqual) return el;
				}
			}
			
		}
		return null;
	}
	

	public static List findElementsFromListAsText(List elements, Object obj_value[],  String key[]){
		List result = new ArrayList();
		if(obj_value==null || key==null) return null;
		for(int i=0;i<elements.size();i++){
			if(elements.get(i) instanceof i_bean){
				i_bean el = (i_bean)elements.get(i);
				if(el!=null){
					boolean isEqual=true;
					for(int j=0;j<obj_value.length;j++){
						isEqual&=obj_value[j].toString().equals(el.get(key[j]).toString());
					}
					if(isEqual) result.add(el);
				}
			}else if(elements.get(i) instanceof i_elementDBBase){
				i_elementDBBase el = (i_elementDBBase)elements.get(i);
				if(el!=null){
					boolean isEqual=true;
					for(int j=0;j<obj_value.length;j++){
						isEqual&=obj_value[j].toString().equals(el.getCampoValue(key[j]).toString());
					}
					if(isEqual) result.add(el);
				}
			}else if(elements.get(i) instanceof i_elementBase){
				i_elementBase el = (i_elementBase)elements.get(i);
				if(el!=null){
					boolean isEqual=true;
					for(int j=0;j<obj_value.length;j++){
						isEqual&=obj_value[j].toString().equals(el.getCampoValue(key[j]).toString());
					}
					if(isEqual) result.add(el);
				}
			}
			
		}
		return result;
	}	
	public static List findElementsFromList(List elements, Object obj_value[],  String[] key){
		List result = new ArrayList();
		if(obj_value==null || key==null) return null;
		for(int i=0;i<elements.size();i++){
			if(elements.get(i) instanceof i_bean){
				i_bean el = (i_bean)elements.get(i);
				if(el!=null){
					boolean isEqual=true;
					for(int j=0;j<obj_value.length;j++){
						isEqual&=obj_value[j].equals(el.get(key[j]));
					}
					if(isEqual) result.add(el);
				}
			}else if(elements.get(i) instanceof i_elementDBBase){
				i_elementDBBase el = (i_elementDBBase)elements.get(i);
				if(el!=null){
					boolean isEqual=true;
					for(int j=0;j<obj_value.length;j++){
						isEqual&=obj_value[j].equals(el.getCampoValue(key[j]));
					}
					if(isEqual) result.add(el);
				}
			}else if(elements.get(i) instanceof i_elementBase){
				i_elementBase el = (i_elementBase)elements.get(i);
				if(el!=null){
					boolean isEqual=true;
					for(int j=0;j<obj_value.length;j++){
						isEqual&=obj_value[j].equals(el.getCampoValue(key[j]));
					}
					if(isEqual) result.add(el);
				}
			}			
		}
		return result;
	}		

}
