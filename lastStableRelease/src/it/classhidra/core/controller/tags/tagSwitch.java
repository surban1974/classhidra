/**
* Creation date: (10/04/2015)
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


import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;



public class tagSwitch extends TagSupport{
	private static final long serialVersionUID = -1L;
	protected boolean conditionBreak = false;
	
	public int doStartTag() throws JspException {
		return EVAL_BODY_INCLUDE;
	}		

	public int doEndTag() throws JspException {
		
		this.release();
		return super.doStartTag();		
	}

	public void release() {
		super.release();
		conditionBreak=false;
	}

	public boolean isConditionBreak() {
		return conditionBreak;
	}

	public void setConditionBreak(boolean conditionBreak) {
		this.conditionBreak = conditionBreak;
	}
  

}

