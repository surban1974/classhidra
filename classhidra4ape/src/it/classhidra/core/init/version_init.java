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

package it.classhidra.core.init;

import it.classhidra.core.tool.util.util_file;

import java.io.Serializable;
import java.util.Properties;

public class version_init implements Serializable{
	private static final long serialVersionUID = -1L;
	static public String id_author =					"application.author";
	static public String id_vendor =					"application.vendor";

	static public String id_name =						"application.name";
	static public String id_version= 					"application.version";
	static public String id_lastmod = 					"application.lastmod";

	private String _author;
	private String _vendor;
	private String _name;
	private String _version;
	private String _lastmod;
	

public version_init(String property_name) {
	super();
	init(property_name);
}

public void init(String property_name) {
	Properties property = null;
	if(property_name==null || property_name.equals("")) property_name = "version";
	try{
		property = util_file.loadProperty(property_name);					
	}catch (Exception e) {
	}

	if(property!=null){
		_author = (_author==null)?property.getProperty(id_author):_author;
		_vendor = (_vendor==null)?property.getProperty(id_vendor):_vendor;
		_name = (_name==null)?property.getProperty(id_name):_name;
		_version = (_version==null)?property.getProperty(id_version):_version;
		_lastmod = (_lastmod==null)?property.getProperty(id_lastmod):_lastmod;
	}
}

public String get_name() {
	return _name;
}

public void set_name(String _name) {
	this._name = _name;
}

public String get_version() {
	return _version;
}

public void set_version(String _version) {
	this._version = _version;
}

public String get_lastmod() {
	return _lastmod;
}

public void set_lastmod(String _lastmod) {
	this._lastmod = _lastmod;
}

public String getInfo() {
	String info="";
	if(_name!=null) info+=" app: "+ _name+";";
	if(_author!=null) info+=" author: "+ _author+";";
	if(_vendor!=null) info+=" vendor: "+ _vendor+";";
	if(_version!=null) info+=" version: "+ _version+";";
	if(_lastmod!=null) info+=" lastmod: "+ _lastmod+";";
	return  info;
}

public String get_author() {
	return _author;
}

public void set_author(String author) {
	_author = author;
}

public String get_vendor() {
	return _vendor;
}

public void set_vendor(String vendor) {
	_vendor = vendor;
}

}
