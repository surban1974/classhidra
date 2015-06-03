package test.component;

import java.util.Vector;

import it.classhidra.annotation.annotation_scanner;
import it.classhidra.core.controller.info_action;
import it.classhidra.core.controller.info_bean;
import it.classhidra.core.init.auth_init;
import it.classhidra.core.tool.util.util_beanMessageFactory;
import it.classhidra.core.tool.util.util_file;
import it.classhidra.framework.web.components.componentAmanager;

public class ct__ComponentAmanager {


	public static void main(String[] args) {
		
		try{
			
			
			componentAmanager comp = null;
			try{
				comp = (componentAmanager)util_beanMessageFactory.message2bean(util_file.getBytesFromFile("c:/tmp/inputComponentAmanager.xml"));
			}catch(Exception e){
			}
			
			if(comp==null){
				comp = new componentAmanager();
				comp.reimposta();
			}
			
			annotation_scanner scaner = new annotation_scanner();
			scaner.checkClassAnnotation(comp.getClass().getName());
			if(scaner.get_actions()!=null && scaner.get_actions().size()>0)
				comp.set_infoaction((info_action)new Vector(scaner.get_actions().values()).get(0));
			if(scaner.get_beans()!=null && scaner.get_beans().size()>0)
				comp.set_infobean((info_bean)new Vector(scaner.get_beans().values()).get(0));

			
			auth_init current_auth = new auth_init();
			comp.setCurrent_auth(current_auth);
			
			

// Test start			
			comp.actionservice(null, null);
// Test finish
			
			util_file.writeByteToFile(
					util_beanMessageFactory.bean2message(comp,"componentAmanager",true).getBytes(),
					"c:/tmp/outputComponentAmanager.xml"
			);		
			
		}catch(Exception e){
			e.toString();
		}

	}

}
