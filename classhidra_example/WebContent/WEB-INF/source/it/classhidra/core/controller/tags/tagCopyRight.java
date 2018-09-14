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

public class tagCopyRight extends TagSupport{
	private static final long serialVersionUID = -896536174738762236L;
	protected String img_path = null;


	public int doStartTag() throws JspException {
		final StringBuffer results = new StringBuffer();
		if(img_path==null) img_path="../images";
		results.append(this.createTagBody());
		JspWriter writer = pageContext.getOut();
		try {
			writer.print(results.toString());
		} catch (IOException e) {
			throw new JspException(e.toString());
		}
		return EVAL_BODY_INCLUDE; 
	}

	public void release() {
		super.release();
		img_path = null;
	}
  
	protected String createTagBody() {
	
		final StringBuffer results = new StringBuffer("");
			results.append("<img src=\"../images/copyright.gif\" border=\"0\"/>");
		return results.toString();
	}

	public String getImg_path() {
		return img_path;
	}

	public void setImg_path(String imgPath) {
		img_path = imgPath;
	}


}

