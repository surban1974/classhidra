package it.classhidra.qreports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.classhidra.core.tool.util.util_format;


public class sqlTransformer {
	private static final char START_ARGUMENTS = '{';
	private static final char FINISH_ARGUMENTS = '}';

	public class MaskFunction{
		public static final int CONST_TEXT=0;
		public static final int CONST_XPATH=1;
		public static final int CONST_DLID=2;
		
		int type;
		String argument;
		
		public MaskFunction(){
			super();
			type = 0;
			argument="";
		}
		
		public void addArgument(char ch) {
			this.argument+= ch;
		}
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		public String getArgument() {
			return argument;
		}
		public void setArgument(String argument) {
			this.argument = argument;
		}
		public String toString(){
			return new Integer(type).toString()+"|"+argument;
		}
	}	


	
	public static String getTransformed(String input, parameter current, HashMap<String, parameter> h_parameters) throws Exception{
		String result="";
		
		List<MaskFunction> functions = parsing(input);

		for(int i=0;i<functions.size();i++){
			MaskFunction func = (MaskFunction)functions.get(i);
			switch (func.getType()){
				case 0: result+=func.getArgument();break;
				case 1: {
					parameter par = (parameter)h_parameters.get(func.getArgument());
					if(par!=null){
						if(par.getAdaptsql().equalsIgnoreCase("YES"))
							result+=util_format.convertAp(par.getDefault_value());
						else
							result+=par.getDefault_value();
						if(current!=null)
							par.getDependencies().put(current.getName(), current);
					}
					break;
				}

			}
			
		}
		
		return result;
	}

	
	private static int isSTART_AREA_ARGUMENTS(char arg){
		if(arg==START_ARGUMENTS) return 1;
		return 0;
	}
	
	private static int isFINISH_AREA_ARGUMENTS(char arg){
		if(arg==FINISH_ARGUMENTS) return 1;
		return -1;
	}	
	
	private static List<MaskFunction> parsing(String input){

		
		List<MaskFunction> result = new ArrayList<MaskFunction>();
		
		MaskFunction func = new sqlTransformer(). new MaskFunction();
		

		for(int i=0;i<input.length();i++){
			char current = input.charAt(i);
			int funcTypeStart=isSTART_AREA_ARGUMENTS(current);
			int funcTypeFinish=isFINISH_AREA_ARGUMENTS(current);
			if(funcTypeStart>0){
				if(funcTypeStart!=func.getType()){
					if(!func.getArgument().equals("")) result.add(func);
					func = new sqlTransformer().new MaskFunction();
					func.setType(funcTypeStart);
				}	
			}else if(funcTypeFinish==func.getType()){ 
				if(!func.getArgument().equals("")) result.add(func);
				func = new sqlTransformer().new MaskFunction();
			}else{
				func.addArgument(current);
			}

		}
		if(!func.getArgument().equals("")) 
			result.add(func);

		
		return result;
	}
	

}
