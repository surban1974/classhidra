/**
* Creation date: (07/04/2006)
* @author: Svyatoslav Urbanovych svyatoslav.urbanovych@gmail.com
*/

/********************************************************************************
*
*	Copyright (C) 2006  Svyatoslav Urbanovych
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




import it.classhidra.core.controller.bsController;
import it.classhidra.core.tool.util.util_format;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import java.io.IOException;
import java.util.HashMap;

public class showLMActionLabel extends TagSupport{
	private static final long serialVersionUID = 8732529734440992215L;
	protected String style = null;
	protected String styleClass = null;
	protected String message_code = null;
	protected String message_defaultValue = null;
	protected String _parent = null;
	protected String type = null;
	protected String index = null;
	protected String js_function = null;
	protected String img = null;
	protected String path_for_backimg = null;

	protected String img_before;
	protected String viewborder;
	protected String img_height;
	protected String img_width;
	protected String img_disable;

	protected HashMap parameters=null;



	public int doStartTag() throws JspException {
		parameters=new HashMap();
		return EVAL_BODY_INCLUDE;
	}

	public int doEndTag() throws JspException {
		final StringBuffer results = new StringBuffer();
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
		style=null;
		styleClass=null;
		message_code=null;
		message_defaultValue=null;

		_parent = null;
		type = null;
		index = null;
		js_function = null;
		img = null;
		path_for_backimg = null;
		parameters=null;

		img_before = null;
		viewborder = null;
		img_height = null;
		img_width = null;
		img_disable = null;

	}

	protected String createTagBody() {
		HttpServletRequest request  = (HttpServletRequest) this.pageContext.getRequest();
		final StringBuffer results = new StringBuffer("");

		try{

			String mess = null;
			try{
				mess=bsController.writeLabel(request,message_code,message_defaultValue,parameters);
			}catch(Exception e){
			}
//			mess = mess.replace("\"","\\\"");
			mess = util_format.replace(mess,"\"","\\\"");

			results.append("<script>ObjectDraw(");
			if (_parent != null) {
				results.append('"');
				results.append(_parent);
				results.append("\",");
			}else results.append("\"page1\",");

			if (type != null) {
				results.append('"');
				results.append(type);
				results.append("\",");
			}else results.append("\"button\",");

			if (index != null) {
				results.append('"');
				results.append(index);
				results.append("\",");
			}else results.append("\"0\",");
			if (mess != null) {
				results.append('"');
				results.append(mess);
				results.append("\",");
			}else results.append("\"\",");
			if (js_function != null) {
				results.append('"');
				results.append(js_function);
				results.append("\",");
			}else results.append("\"\",");
			if (styleClass != null) {
				results.append('"');
				results.append(styleClass);
				results.append("\",");
			}else results.append("\"page_section\",");
			if (img != null) {
				results.append('"');
				results.append(img);
				results.append("\",");
			}else results.append("\"\",");
			if (path_for_backimg != null) {
				results.append('"');
				results.append(path_for_backimg);
				results.append("\",");
			}else results.append("\"\",");
			if (img_before != null) {
				results.append('"');
				results.append(img_before);
				results.append("\",");
			}else results.append("\"\",");
			if (viewborder != null) {
				results.append('"');
				results.append(viewborder);
				results.append("\",");
			}else results.append("\"\",");
			if (img_height != null) {
				results.append('"');
				results.append(img_height);
				results.append("\",");
			}else results.append("\"\",");
			if (img_width != null) {
				results.append('"');
				results.append(img_width);
				results.append("\",");
			}else results.append("\"\",");
			if (img_disable != null) {
				results.append('"');
				results.append(img_disable);
				results.append('"');
			}else results.append("\"\"");



			results.append(");</script>");
		}catch(Exception e){

		}
		return results.toString();
	}


	public String getStyleClass() {
		return styleClass;
	}
	public void setStyleClass(String string) {
		styleClass = string;
	}
	public String getStyle() {
		return style;
	}

	public void setStyle(String string) {
		style = string;
	}


	public String getMessage_code() {
		return message_code;
	}

	public String getMessage_defaultValue() {
		return message_defaultValue;
	}

	public void setMessage_code(String string) {
		message_code = string;
	}

	public void setMessage_defaultValue(String string) {
		message_defaultValue = string;
	}


	public String getImg() {
		return img;
	}

	public String getIndex() {
		return index;
	}

	public String getJs_function() {
		return js_function;
	}


	public String getPath_for_backimg() {
		return path_for_backimg;
	}

	public String getType() {
		return type;
	}

	public void setImg(String string) {
		img = string;
	}

	public void setIndex(String string) {
		index = string;
	}

	public void setJs_function(String string) {
		js_function = string;
	}

	public void set_parent(String string) {
		_parent = string;
	}

	public void setPath_for_backimg(String string) {
		path_for_backimg = string;
	}

	public void setType(String string) {
		type = string;
	}

	public String get_parent() {
		return _parent;
	}

	public HashMap getParameters() {
		return parameters;
	}

	public String getImg_before() {
		return img_before;
	}

	public void setImg_before(String imgBefore) {
		img_before = imgBefore;
	}

	public String getViewborder() {
		return viewborder;
	}

	public void setViewborder(String viewborder) {
		this.viewborder = viewborder;
	}

	public String getImg_height() {
		return img_height;
	}

	public void setImg_height(String imgHeight) {
		img_height = imgHeight;
	}

	public String getImg_width() {
		return img_width;
	}

	public void setImg_width(String imgWidth) {
		img_width = imgWidth;
	}

	public String getImg_disable() {
		return img_disable;
	}

	public void setImg_disable(String imgDisable) {
		img_disable = imgDisable;
	}

}

