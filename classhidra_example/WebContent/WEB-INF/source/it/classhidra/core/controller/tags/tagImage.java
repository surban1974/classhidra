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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.*;
import javax.servlet.jsp.*;
import java.io.IOException;

import it.classhidra.core.controller.bsController;
import it.classhidra.core.init.auth_init;

import java.math.BigDecimal;
import java.io.File;
/**
 * @author esparm
 *
 */
public class tagImage extends TagSupport{
	private static final long serialVersionUID = 5955985523908683660L;
	protected String width = null;
	protected String vspace = null;
	protected String usemap = null;
	protected String title = null;
	protected String style = null;
	protected String onmouseup = null;
	protected String onmouseover = null;
	protected String onmouseout = null;
	protected String onmousemove = null;
	protected String  onmousedown = null;
	protected String onkeyup = null;
	protected String onkeypress = null;
	protected String onkeydown = null;
	protected String onhelp = null;
	protected String ondblclick = null;
	protected String onclick = null;
	protected String name = null;
	protected String mapfile = null;
	protected String longdesc = null;
	protected String lang = null;
	protected String ismap = null;
	protected String objId = null;
	protected String hspace = null;
	protected String height = null;
	protected String dir = null;
	protected String styleClass = null;
	protected String border = null;
	protected String alt = null;
	protected String align = null;
	protected String src = null;

	public tagImage()
	{
		super();
	}

	public int doStartTag() throws JspException
	{
		StringBuffer results = new StringBuffer();
		results.append(this.createTagBody());
		JspWriter writer = pageContext.getOut();
		try
		{
			writer.print(results.toString());
		}
		catch (IOException e)
		{
			throw new JspException(e.toString());
		}
		return EVAL_BODY_INCLUDE;
	}

	public void release()
	{
		super.release();

		width = null;
		vspace = null;
		usemap = null;
		title = null;
		onmouseup = null;
		onmouseover = null;
		onmouseout = null;
		onmousemove = null;
		onmousedown = null;
		onkeyup = null;
		onkeypress = null;
		onkeydown = null;
		onhelp = null;
		ondblclick = null;
		onclick = null;
		name = null;
		mapfile = null;
		longdesc = null;
		lang = null;
		ismap = null;
		objId = null;
		hspace = null;
		height = null;
		dir = null;
		styleClass = null;
		border = null;
		alt = null;
		align = null;
		src = null;
	}

	protected String createTagBody(){
		HttpServletRequest request  = (HttpServletRequest) this.pageContext.getRequest();

		BigDecimal cdIst = ((auth_init)request.getSession().getAttribute(bsController.CONST_BEAN_$AUTHENTIFICATION)).get_cd_ist();

		StringBuffer results = new StringBuffer("");
		results.append("<img ");
		if ( src != null)
		{
			String source = src.substring(0,src.indexOf("."))+String.valueOf(cdIst.intValue())+src.substring(src.indexOf("."));
			String path = this.pageContext.getServletContext().getRealPath("/"+source);
			File f = new File(path);
			if ( !f.exists())
			{
				if ( new File(this.pageContext.getServletContext().getRealPath("/"+src)).exists())
				{
					source = src;
				}
				else
				{
					source="images/not_found.gif";
				}
			}
			results.append(" src=\"");
			results.append(source);
			results.append('"');
		}
		if(align!=null)
		{
			results.append(" align=\"");
			results.append(align);
			results.append('"');
		}
		if(alt!=null)
		{
			results.append(" alt=\"");
			results.append(alt);
			results.append('"');
		}
		if(border!=null)
		{
			results.append(" border=\"");
			results.append(border);
			results.append('"');
		}
		if(styleClass!=null)
		{
			results.append(" class=\"");
			results.append(styleClass);
			results.append('"');
		}
		if(dir!=null)
		{
			results.append(" dir=\"");
			results.append(dir);
			results.append('"');
		}
		if(height!=null)
		{
			results.append(" height=\"");
			results.append(height);
			results.append('"');
		}
		if(hspace!=null)
		{
			results.append(" hspace=\"");
			results.append(hspace);
			results.append('"');
		}
		if(objId!=null)
		{
			results.append(" id=\"");
			results.append(objId);
			results.append('"');
		}
		if(ismap!=null)
		{
			results.append(" ismap=\"");
			results.append(ismap);
			results.append('"');
		}
		if(lang!=null)
		{
			results.append(" lang=\"");
			results.append(lang);
			results.append('"');
		}
		if(longdesc!=null)
		{
			results.append(" longdesc=\"");
			results.append(longdesc);
			results.append('"');
		}
		if(mapfile!=null)
		{
			results.append(" mapfile=\"");
			results.append(objId);
			results.append('"');
		}
		if(name!=null)
		{
			results.append(" name=\"");
			results.append(name);
			results.append('"');
		}
		if(onclick!=null)
		{
			results.append(" onclick=\"");
			results.append(onclick);
			results.append('"');
		}
		if(ondblclick!=null)
		{
			results.append(" ondblclick=\"");
			results.append(ondblclick);
			results.append('"');
		}
		if(onhelp!=null)
		{
			results.append(" onhelp=\"");
			results.append(onhelp);
			results.append('"');
		}
		if(onkeydown!=null)
		{
			results.append(" onkeydown=\"");
			results.append(onkeydown);
			results.append('"');
		}
		if(onkeypress!=null)
		{
			results.append(" onkeypress=\"");
			results.append(onkeypress);
			results.append('"');
		}
		if(onkeyup!=null)
		{
			results.append(" onkeyup=\"");
			results.append(onkeyup);
			results.append('"');
		}
		if(onmousedown!=null)
		{
			results.append(" onmousedown=\"");
			results.append(onmousedown);
			results.append('"');
		}
		if(onmouseup!=null)
		{
			results.append(" onmouseup=\"");
			results.append(onmouseup);
			results.append('"');
		}
		if(onmousemove!=null)
		{
			results.append(" onmousemove=\"");
			results.append(onmousemove);
			results.append('"');
		}
		if(onmouseout!=null)
		{
			results.append(" onmouseout=\"");
			results.append(onmouseout);
			results.append('"');
		}
		if(onmouseover!=null)
		{
			results.append(" onmouseover=\"");
			results.append(onmouseover);
			results.append('"');
		}
		if(title!=null)
		{
			results.append(" title=\"");
			results.append(title);
			results.append('"');
		}
		if(usemap!=null)
		{
			results.append(" usemap=\"");
			results.append(usemap);
			results.append('"');
		}
		if(width!=null)
		{
			results.append(" width=\"");
			results.append(width);
			results.append('"');
		}
		if(vspace!=null)
		{
			results.append(" vspace=\"");
			results.append(vspace);
			results.append('"');
		}
		results.append("></img>");
		return results.toString();
	}


	/**
	 * @return
	 */
	public String getAlign() {
		return align;
	}

	/**
	 * @return
	 */
	public String getAlt() {
		return alt;
	}

	/**
	 * @return
	 */
	public String getBorder() {
		return border;
	}

	/**
	 * @return
	 */
	public String getDir() {
		return dir;
	}

	/**
	 * @return
	 */
	public String getHeight() {
		return height;
	}

	/**
	 * @return
	 */
	public String getHspace() {
		return hspace;
	}

	/**
	 * @return
	 */
	public String getObjId() {
		return objId;
	}

	/**
	 * @return
	 */
	public String getIsmap() {
		return ismap;
	}

	/**
	 * @return
	 */
	public String getLang() {
		return lang;
	}

	/**
	 * @return
	 */
	public String getLongdesc() {
		return longdesc;
	}

	/**
	 * @return
	 */
	public String getMapfile() {
		return mapfile;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public String getOnclick() {
		return onclick;
	}

	/**
	 * @return
	 */
	public String getOndblclick() {
		return ondblclick;
	}

	/**
	 * @return
	 */
	public String getOnhelp() {
		return onhelp;
	}

	/**
	 * @return
	 */
	public String getOnkeydown() {
		return onkeydown;
	}

	/**
	 * @return
	 */
	public String getOnkeypress() {
		return onkeypress;
	}

	/**
	 * @return
	 */
	public String getOnkeyup() {
		return onkeyup;
	}

	/**
	 * @return
	 */
	public String getOnmousedown() {
		return onmousedown;
	}

	/**
	 * @return
	 */
	public String getOnmousemove() {
		return onmousemove;
	}

	/**
	 * @return
	 */
	public String getOnmouseout() {
		return onmouseout;
	}

	/**
	 * @return
	 */
	public String getOnmouseover() {
		return onmouseover;
	}

	/**
	 * @return
	 */
	public String getOnmouseup() {
		return onmouseup;
	}

	/**
	 * @return
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * @return
	 */
	public String getStyleClass() {
		return styleClass;
	}

	/**
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return
	 */
	public String getUsemap() {
		return usemap;
	}

	/**
	 * @return
	 */
	public String getVspace() {
		return vspace;
	}

	/**
	 * @return
	 */
	public String getWidth() {
		return width;
	}

	/**
	 * @param string
	 */
	public void setAlign(String string) {
		align = string;
	}

	/**
	 * @param string
	 */
	public void setAlt(String string) {
		alt = string;
	}

	/**
	 * @param string
	 */
	public void setBorder(String string) {
		border = string;
	}

	/**
	 * @param string
	 */
	public void setDir(String string) {
		dir = string;
	}

	/**
	 * @param string
	 */
	public void setHeight(String string) {
		height = string;
	}

	/**
	 * @param string
	 */
	public void setHspace(String string) {
		hspace = string;
	}

	/**
	 * @param string
	 */
	public void setObjId(String string) {
		objId = string;
	}

	/**
	 * @param string
	 */
	public void setIsmap(String string) {
		ismap = string;
	}

	/**
	 * @param string
	 */
	public void setLang(String string) {
		lang = string;
	}

	/**
	 * @param string
	 */
	public void setLongdesc(String string) {
		longdesc = string;
	}

	/**
	 * @param string
	 */
	public void setMapfile(String string) {
		mapfile = string;
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * @param string
	 */
	public void setOnclick(String string) {
		onclick = string;
	}

	/**
	 * @param string
	 */
	public void setOndblclick(String string) {
		ondblclick = string;
	}

	/**
	 * @param string
	 */
	public void setOnhelp(String string) {
		onhelp = string;
	}

	/**
	 * @param string
	 */
	public void setOnkeydown(String string) {
		onkeydown = string;
	}

	/**
	 * @param string
	 */
	public void setOnkeypress(String string) {
		onkeypress = string;
	}

	/**
	 * @param string
	 */
	public void setOnkeyup(String string) {
		onkeyup = string;
	}

	/**
	 * @param string
	 */
	public void setOnmousedown(String string) {
		onmousedown = string;
	}

	/**
	 * @param string
	 */
	public void setOnmousemove(String string) {
		onmousemove = string;
	}

	/**
	 * @param string
	 */
	public void setOnmouseout(String string) {
		onmouseout = string;
	}

	/**
	 * @param string
	 */
	public void setOnmouseover(String string) {
		onmouseover = string;
	}

	/**
	 * @param string
	 */
	public void setOnmouseup(String string) {
		onmouseup = string;
	}

	/**
	 * @param string
	 */
	public void setStyle(String string) {
		style = string;
	}

	/**
	 * @param string
	 */
	public void setStyleClass(String string) {
		styleClass = string;
	}

	/**
	 * @param string
	 */
	public void setTitle(String string) {
		title = string;
	}

	/**
	 * @param string
	 */
	public void setUsemap(String string) {
		usemap = string;
	}

	/**
	 * @param string
	 */
	public void setVspace(String string) {
		vspace = string;
	}

	/**
	 * @param string
	 */
	public void setWidth(String string) {
		width = string;
	}

	/**
	 * @return
	 */
	public String getSrc() {
		return src;
	}

	/**
	 * @param string
	 */
	public void setSrc(String string) {
		src = string;
	}

}
