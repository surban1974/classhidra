/**
* Creation date: (27/07/2012)
* @author: Svyatoslav Urbanovych svyatoslav.urbanovych@gmail.com 
*/

/********************************************************************************
*
*	Copyright (C) 2005  Svyatoslav Urbanovych
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.

* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.

* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*********************************************************************************/

package it.classhidra.core.controller.tags;


import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;


public class tagExpression extends TagSupport{
	private static final long serialVersionUID = -1L;
	protected String name = null;
	protected List elements = null;
	
	
	public int doStartTag() throws JspException {
		elements = new Vector();
		return EVAL_BODY_INCLUDE; 
	}
	public int doEndTag() throws JspException {
		try{
			HttpServletRequest request  = (HttpServletRequest) this.pageContext.getRequest();
			

			Object obj = null;
			
			obj = calculation();
			
			request.setAttribute(name,obj);
		}catch(Exception e){
		}
		return EVAL_BODY_INCLUDE;

	}

	private Object calculation(){
		try{
			boolean isFinish=false;
			int i=0;
			while(i<elements.size()){
				if(elements.get(i) instanceof tagOperation){
					int arguments_count = countArguments((tagOperation)elements.get(i));
					if(i>arguments_count-1){
						Vector param = new Vector();
						for(int j=i-1;j>i-1-arguments_count;j--) param.add(elements.get(j));					
						tagOperand result = calcOperation((tagOperation)elements.get(i),param);
						elements.remove(i);
						for(int j=i-1;j>i-1-arguments_count+1;j--) elements.remove(j);
						elements.set(i-1-arguments_count+1, result);
						i=-1;
					}
				}
				i++;
			}
		}catch(Exception e){
			return new String("ExpressionError:"+e.toString());
		}
		return ((tagOperand)elements.get(0)).getValue();
	}
	
	private int countArguments(tagOperation operation){
		int result=0;
		if(operation==null || operation.getName()==null) return result;
		if("^+-*/%".indexOf(operation.getName())>-1) return 2;
		else return 1;
	}
	
	
	private tagOperand calcOperation(tagOperation operation, Vector param){
		
		
		double par1 = 0;
		double par2 = 0;
		double result = 0;
		if(param.size()==1) par2=new Double(((tagOperand)param.get(0)).getValue()).doubleValue();
		if(param.size()==2){
			par2=new Double(((tagOperand)param.get(0)).getValue()).doubleValue();
			par1=new Double(((tagOperand)param.get(1)).getValue()).doubleValue();
		}
		if(operation.getName().trim().equals("+"))
			result=(par2+par1)/1;
		if(operation.getName().trim().equals("-"))
			result=(par2-par1)/1;
		if(operation.getName().trim().equals("*"))
			result=par2*par1/1;
		if(operation.getName().trim().equals("/"))
			result=par2/par1/1;
		if(operation.getName().trim().equals("%"))
			result=par2%par1/1;
		if(operation.getName().trim().equals("^"))
			result=Math.pow(par2,par1);
		
		tagOperand operand = new tagOperand();
		operand.setValue(new Double(result).toString());
		return operand;
	}
	
	public void release() {
		super.release();

			super.release();				
			name=null;
			elements = new Vector();

	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public List getElements() {
		return elements;
	}


	public void setElements(List elements) {
		this.elements = elements;
	}

}

