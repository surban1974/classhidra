package it.classhidra.core.tool.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.HashMap;

public class util_makeValue {

	public static Object makeValue1(Object req, String value, String key) throws Exception{
		Object resultObject = null;
		Field fld = null;
		Method ret_fld = null;
		Class ret_class = null;
		try{
			fld=req.getClass().getField(key);
		}catch(Exception ex){
		}
		if(fld==null){
			try{
				ret_fld = req.getClass().getMethod("get"+util_reflect.adaptMethodName(key),new Class[0]);
			}catch(Exception e){
				if(	req instanceof HashMap &&
					((HashMap)req).get(key)!=null) {
					ret_class = ((HashMap)req).get(key).getClass();
				}
				if(	req instanceof HashMap &&
						((HashMap)req).get(key)==null) {
						ret_class = new String().getClass();
					}
			}
		}

		if(fld!=null) ret_class = fld.getType();
		if(fld==null && ret_fld!=null) ret_class = ret_fld.getReturnType();

		if(ret_class==null) return null;

		if(ret_class.isPrimitive()){
			try{
				if(ret_class.getName().equals("int")) resultObject = Integer.valueOf(value);
				if(ret_class.getName().equals("short")) resultObject = Short.valueOf(value);
				if(ret_class.getName().equals("long")) resultObject = Long.valueOf(value);
				if(ret_class.getName().equals("float")) resultObject = Float.valueOf(value);
				if(ret_class.getName().equals("double")) resultObject = Double.valueOf(value);
				if(ret_class.getName().equals("byte")) resultObject = Byte.valueOf(value);
				if(ret_class.getName().equals("boolean")) resultObject = Boolean.valueOf(value);
				if(ret_class.getName().equals("char")) resultObject = new Character(value.charAt(0));
				return resultObject;
			}catch(Exception e){
				if(value!=null && value.equals("")){
					if(ret_class.getName().equals("int")) resultObject = Integer.valueOf(0);
					if(ret_class.getName().equals("short")) resultObject = Short.valueOf((short)0);
					if(ret_class.getName().equals("long")) resultObject = Long.valueOf(0);
					if(ret_class.getName().equals("float")) resultObject = Float.valueOf(0);
					if(ret_class.getName().equals("double")) resultObject = Double.valueOf(0);
					if(ret_class.getName().equals("byte")) resultObject = Byte.valueOf((byte)0);
					if(ret_class.getName().equals("boolean")) resultObject = Boolean.valueOf(false);
					if(ret_class.getName().equals("char")) resultObject = new Character(' ');
					return resultObject;
				}
			}
		}else{
			try{
				Object[] prs = new Object[1];
				Class[] cls = new Class[1];
					cls[0]=value.getClass();
					prs[0]=value;
				Class cl = ret_class;
				if(value!=null && value.equals("")){
					boolean superIsNumber=false;
					Class cl_super = cl.getSuperclass();
					while(cl_super!=null){
						if(cl_super.getName().indexOf(".Number")>0) superIsNumber=true;
						cl_super = cl_super.getSuperclass();
					}
					if(superIsNumber) prs[0]="0";
				}
				resultObject = cl.getConstructor(cls).newInstance(prs);

				return resultObject;
			}catch(Exception e){
			}
			String[] formatesD = new String[8];
				formatesD[0]="yyyy-MM-dd";
				formatesD[1]="yyyy/MM/dd";
				formatesD[2]="yyyyMMdd";
				formatesD[3]="yyyy.MM.dd";
				formatesD[4]="dd-MM-yyyy";
				formatesD[5]="dd/MM/yyyy";
				formatesD[6]="dd-MM-yyyy";
				formatesD[7]="dd.MM.yyyy";
			if(ret_class.getName().equals("java.sql.Date")){
				for(int i=0;i< formatesD.length;i++){
					try{
						resultObject = util_format.stringToData(value, formatesD[i]);
						if(resultObject!=null){
							resultObject = new java.sql.Date(((java.util.Date)resultObject).getTime());
							return resultObject;
						}
					}catch(Exception e){
					}
				}
			}
			if(ret_class.getName().equals("java.sql.Timestamp")){
				for(int i=0;i< formatesD.length;i++){
					try{
						resultObject = util_format.stringToData(value, formatesD[i]+"-hh.mm.ssssss");
						if(resultObject!=null){
							resultObject = new Timestamp(((java.util.Date)resultObject).getTime());
							 return resultObject;
						}
					}catch(Exception e){
					}
				}
			}
			if(ret_class.getName().equals("java.util.Date")){
				for(int i=0;i< formatesD.length;i++){
					try{
						resultObject = util_format.stringToData(value, formatesD[i]);
						if(resultObject!=null) return resultObject;
					}catch(Exception e){
					}
				}
			}
		}
		return resultObject;
	}

	public static Object makeValue(String value, Object ref) throws Exception{
		Object resultObject=null;
		if(ref.getClass().isPrimitive()) return resultObject;
		try{
			Object[] prs = new Object[1];
			Class[] cls = new Class[1];
				cls[0]=value.getClass();
				prs[0]=value;
			Class cl = ref.getClass();
			resultObject = cl.getConstructor(cls).newInstance(prs);
			return resultObject;
		}catch(Exception e){
		}
		String[] formatesD = new String[8];
			formatesD[0]="yyyy-MM-dd";
			formatesD[1]="yyyy/MM/dd";
			formatesD[2]="yyyyMMdd";
			formatesD[3]="yyyy.MM.dd";
			formatesD[4]="dd-MM-yyyy";
			formatesD[5]="dd/MM/yyyy";
			formatesD[6]="dd-MM-yyyy";
			formatesD[7]="dd.MM.yyyy";
		if(ref instanceof java.sql.Date){
			for(int i=0;i< formatesD.length;i++){
				try{
					resultObject = util_format.stringToData(value, formatesD[i]);
					if(resultObject!=null) return new java.sql.Date(((java.util.Date)resultObject).getTime());
				}catch(Exception e){
				}
			}
		}
		if(ref instanceof Timestamp){
			for(int i=0;i< formatesD.length;i++){
				try{
					resultObject = util_format.stringToData(value, formatesD[i]+"-hh.mm.ssssss");
					if(resultObject!=null) return new Timestamp(((java.util.Date)resultObject).getTime());
				}catch(Exception e){
				}
			}
		}
		if(ref instanceof java.util.Date){
			for(int i=0;i< formatesD.length;i++){
				try{
					resultObject = util_format.stringToData(value, formatesD[i]);
					if(resultObject!=null) return resultObject;
				}catch(Exception e){
				}
			}
		}
		return resultObject;
	}

	public static Object makeFormatedValue1(Object req, String format, String value, String key, String replaceOnBlank, String replaceOnErrorFormat) throws Exception{
		Object resultObject = null;
		Field fld = null;
		Method ret_fld = null;
		Class ret_class = null;
		try{
			fld=req.getClass().getField(key);
		}catch(Exception ex){
		}
		if(fld==null){
			try{
				ret_fld = req.getClass().getMethod("get"+util_reflect.adaptMethodName(key),new Class[0]);
			}catch(Exception e){
			}
		}

		if(fld!=null) ret_class = fld.getType();
		if(fld==null && ret_fld!=null) ret_class = ret_fld.getReturnType();

		if(ret_class==null && req instanceof HashMap){
			ret_class = ((HashMap)req).get(key).getClass();
		}

		if(ret_class.isPrimitive()){
			try{
				if(format!=null && !format.equals("")){
					try{
						if((value==null || value.equals("")) && replaceOnBlank!=null && replaceOnBlank!=null && !replaceOnBlank.equals("")) value=replaceOnBlank;
						value = new DecimalFormat(format).parse(value.trim()).toString();
					}catch(Exception e){
					}
				}

				if(ret_class.getName().equals("int")) resultObject = Integer.valueOf(value);
				if(ret_class.getName().equals("short")) resultObject = Short.valueOf(value);
				if(ret_class.getName().equals("long")) resultObject = Long.valueOf(value);
				if(ret_class.getName().equals("float")) resultObject = Float.valueOf(value);
				if(ret_class.getName().equals("double")) resultObject = Double.valueOf(value);
				if(ret_class.getName().equals("byte")) resultObject = Byte.valueOf(value);
				if(ret_class.getName().equals("boolean")) resultObject = Boolean.valueOf(value);
				if(ret_class.getName().equals("char")) resultObject = new Character(value.charAt(0));
				return resultObject;
			}catch(Exception e){
				if(value!=null && value.equals("")){
					if(ret_class.getName().equals("int")) resultObject = Integer.valueOf(0);
					if(ret_class.getName().equals("short")) resultObject = Short.valueOf((short)0);
					if(ret_class.getName().equals("long")) resultObject = Long.valueOf(0);
					if(ret_class.getName().equals("float")) resultObject = Float.valueOf(0);
					if(ret_class.getName().equals("double")) resultObject = Double.valueOf(0);
					if(ret_class.getName().equals("byte")) resultObject = Byte.valueOf((byte)0);
					if(ret_class.getName().equals("boolean")) resultObject = Boolean.valueOf(false);
					if(ret_class.getName().equals("char")) resultObject = new Character(' ');
					return resultObject;
				}
				if(replaceOnErrorFormat!=null){
					resultObject=makeFormatedValue1(req, format, replaceOnErrorFormat, key, replaceOnBlank, null);
					return resultObject;
				}
			}
		}else{

			if(	!(ret_class.getName().equals("java.sql.Date")) &&
				!(ret_class.getName().equals("java.util.Date")) &&
				!(ret_class.getName().equals("java.sql.Timestamp")) &&
				!(ret_class.getName().equals("java.lang.String"))
			){
				try{
					try{
						if((value==null || value.equals("")) && replaceOnBlank!=null && !replaceOnBlank.equals("")) value=replaceOnBlank;
//						value = new DecimalFormat(format).format(new java.math.BigDecimal(value.trim()).doubleValue());
						value = new DecimalFormat(format).parse(value.trim()).toString();
					}catch(Exception e){}

					Object[] prs = new Object[1];
					Class[] cls = new Class[1];
					cls[0]=value.getClass();
					prs[0]=value;
//					Class cl = resultObject.getClass();
					resultObject = ret_class.getConstructor(cls).newInstance(prs);
					return resultObject;
				}catch(Exception e){
					if(replaceOnErrorFormat!=null){
						resultObject=makeFormatedValue1(req, format, replaceOnErrorFormat, key, replaceOnBlank, null);
						return resultObject;
					}
				}
			}
			if(ret_class.getName().equals("java.sql.Date")){
				try{
					if((value==null || value.equals("")) && replaceOnBlank!=null && !replaceOnBlank.equals("")) value=replaceOnBlank;
					resultObject = util_format.stringToData(value, format);
					if(resultObject!=null) return new java.sql.Date(((java.util.Date)resultObject).getTime());
				}catch(Exception e){
					if(replaceOnErrorFormat!=null){
						resultObject=makeFormatedValue1(req, format, replaceOnErrorFormat, key, replaceOnBlank, null);
						return resultObject;
					}
				}
			}
			if(ret_class.getName().equals("java.sql.Timestamp")){
				try{
					if((value==null || value.equals("")) && replaceOnBlank!=null && !replaceOnBlank.equals("")) value=replaceOnBlank;
					resultObject = util_format.stringToData(value, format);
					if(resultObject!=null) return new Timestamp(((java.util.Date)resultObject).getTime());
				}catch(Exception e){
					if(replaceOnErrorFormat!=null){
						resultObject=makeFormatedValue1(req, format, replaceOnErrorFormat, key, replaceOnBlank, null);
						return resultObject;
					}
				}
			}
			if(ret_class.getName().equals("java.util.Date")){
				try{
					if((value==null || value.equals("")) && replaceOnBlank!=null && !replaceOnBlank.equals("")) value=replaceOnBlank;
					resultObject = util_format.stringToData(value, format);
					if(resultObject!=null) return resultObject;
				}catch(Exception e){
					if(value!=null && value.equals("")){
//						resultObject = get(key);
//						if(resultObject==null) 	resultObject = new java.util.Date();
						return resultObject;
					}
					if(replaceOnErrorFormat!=null){
						resultObject=makeFormatedValue1(req, format, replaceOnErrorFormat, key, replaceOnBlank, null);
						return resultObject;
					}
				}
			}
			if(ret_class.getName().equals("java.lang.String")){
				try{
					if((value==null || value.equals("")) && replaceOnBlank!=null && !replaceOnBlank.equals("")) value=replaceOnBlank;
					resultObject = new DecimalFormat(format).parse(value.trim()).toString();
					if(resultObject!=null) return resultObject;
				}catch(Exception e){
					if(replaceOnErrorFormat!=null){
						resultObject=makeFormatedValue1(req, format, replaceOnErrorFormat, key, replaceOnBlank, null);
						return resultObject;
					}
					resultObject = value;
				}
			}
			return resultObject;
		}
		return resultObject;
	}


	public static Object makeFormatedValue(Object req, String format, String value, Object ref, String replaceOnBlank, String replaceOnErrorFormat) throws Exception{
		Object resultObject=null;
		if(ref.getClass().isPrimitive()) return resultObject;
		if(	!(ref instanceof java.sql.Date) &&
			!(ref instanceof java.util.Date) &&
			!(ref instanceof Timestamp) &&
			!(ref instanceof String)
		){
			try{
				try{
					if((value==null || value.equals("")) && replaceOnBlank!=null && !replaceOnBlank.equals("")) value=replaceOnBlank;
					value = new DecimalFormat(format).format(new java.math.BigDecimal(value.trim()).doubleValue());
				}catch(Exception e){}

				Object[] prs = new Object[1];
				Class[] cls = new Class[1];
				cls[0]=value.getClass();
				prs[0]=value;
				Class cl = ref.getClass();
				resultObject = cl.getConstructor(cls).newInstance(prs);
				return resultObject;
			}catch(Exception e){
				if(replaceOnErrorFormat!=null){
					resultObject=makeFormatedValue(req, format, replaceOnErrorFormat, ref, replaceOnBlank, null);
					return resultObject;
				}
			}
		}
		if(ref instanceof java.sql.Date){
			try{
				if((value==null || value.equals("")) && replaceOnBlank!=null && !replaceOnBlank.equals("")) value=replaceOnBlank;
				resultObject = util_format.stringToData(value, format);
				if(resultObject!=null) return new java.sql.Date(((java.util.Date)resultObject).getTime());
			}catch(Exception e){
				if(replaceOnErrorFormat!=null){
					resultObject=makeFormatedValue(req, format, replaceOnErrorFormat, ref, replaceOnBlank, null);
					return resultObject;
				}
			}
		}
		if(ref instanceof Timestamp){
			try{
				if((value==null || value.equals("")) && replaceOnBlank!=null && !replaceOnBlank.equals("")) value=replaceOnBlank;
				resultObject = util_format.stringToData(value, format);
				if(resultObject!=null) return new Timestamp(((java.util.Date)resultObject).getTime());
			}catch(Exception e){
				if(replaceOnErrorFormat!=null){
					resultObject=makeFormatedValue(req, format, replaceOnErrorFormat, ref, replaceOnBlank, null);
					return resultObject;
				}
			}
		}
		if(ref instanceof java.util.Date){
			try{
				if((value==null || value.equals("")) && replaceOnBlank!=null && !replaceOnBlank.equals("")) value=replaceOnBlank;
				resultObject = util_format.stringToData(value, format);
				if(resultObject!=null) return resultObject;
			}catch(Exception e){
				if(replaceOnErrorFormat!=null){
					resultObject=makeFormatedValue(req, format, replaceOnErrorFormat, ref, replaceOnBlank, null);
					return resultObject;
				}
			}
		}
		return resultObject;
	}

}
