package test.unit.annotation;

import it.classhidra.annotation.annotation_scanner;
import it.classhidra.framework.web.components.componentAmanager;
import junit.framework.TestCase;

public class ut__annotation_scanner extends TestCase {

	   public void testScanClass4Annotation() {
			componentAmanager comp = new componentAmanager();
			
			annotation_scanner scaner = new annotation_scanner();
			scaner.checkClassAnnotation(comp.getClass().getName());
		     
			assertNotNull(scaner.get_actions().get("amanager"));
			assertNotNull(scaner.get_beans().get("formAmanager"));
			
	   }	
	
}
