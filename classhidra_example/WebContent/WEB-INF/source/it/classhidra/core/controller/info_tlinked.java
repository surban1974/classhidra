/**
* Creation date: (31/05/2016)
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
package it.classhidra.core.controller;

import it.classhidra.annotation.elements.TemporaryLinked;
import it.classhidra.core.tool.elements.i_elementBase;

public class info_tlinked extends info_entity implements i_elementBase{
	private static final long serialVersionUID = -1L;
	private String value;
	private Class reference;
	private boolean unlinkAndSetNull;
	
	public info_tlinked(){
		super();
		reimposta();
	}
	
	public info_tlinked(TemporaryLinked annotation){
		super();
		if(annotation!=null) {
			this.value=annotation.value();
			this.reference=annotation.reference();
			this.unlinkAndSetNull=annotation.unlinkAndSetNull();
		}else
			reimposta();
	}

	public void reimposta(){
		value="";
		unlinkAndSetNull=true;	
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isUnlinkAndSetNull() {
		return unlinkAndSetNull;
	}

	public void setUnlinkAndSetNull(boolean unlinkAndSetNull) {
		this.unlinkAndSetNull = unlinkAndSetNull;
	}

	public Class getReference() {
		return reference;
	}

	public void setReference(Class reference) {
		this.reference = reference;
	}


}
