package examples.spring.classhidra.framework.web.components.beans;



import it.classhidra.annotation.elements.Bean;
import it.classhidra.core.controller.bean;
import it.classhidra.core.controller.i_bean;

import java.io.Serializable;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;





@Bean(name="beanContent")
@Component("beanContent")
@Scope("session")
public class SpringBeanContent extends bean implements i_bean, Serializable{

	private static final long serialVersionUID = -1830493478965489235L;
	
	private String menuSource;
	private boolean firstEnter=false;
	
	

public SpringBeanContent(){
	super();
}



public void reimposta(){
	menuSource="";
}

public String getMenuSource() {
	return menuSource;
}

public void setMenuSource(String menuSource) {
	this.menuSource = menuSource;
}


public boolean getFirstEnter() {
	return firstEnter;
}

public void setFirstEnter(boolean firstEnter) {
	this.firstEnter = firstEnter;
}


}
