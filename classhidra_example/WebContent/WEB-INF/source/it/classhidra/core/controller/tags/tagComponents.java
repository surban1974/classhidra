/**
* Creation date: (07/04/2006)
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

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*; 
import java.io.IOException;

public class tagComponents extends TagSupport{
	private static final long serialVersionUID = 1L;
	protected String skip=null;



	public int doStartTag() throws JspException {
		final StringBuffer results = new StringBuffer();
		results.append("<!-- components begin -->");
		JspWriter writer = pageContext.getOut();
		try {
			writer.print(results.toString());
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
		if (condition())
			return (EVAL_BODY_INCLUDE);
		else
			return (SKIP_BODY);
	}

	public void release() {
		super.release();
		skip=null;
	}

	@Override
	public int doEndTag() throws JspException {
		final StringBuffer results = new StringBuffer();
		results.append("<!-- components end -->");
		JspWriter writer = pageContext.getOut();
		try {
			writer.print(results.toString());
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
		return (EVAL_PAGE);
	}

	private boolean condition() throws JspException{
		if(skip==null || !skip.equalsIgnoreCase("true"))
			return true;
		return false;
	}

}

