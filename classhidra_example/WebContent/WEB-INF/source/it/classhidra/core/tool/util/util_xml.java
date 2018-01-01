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

package it.classhidra.core.tool.util;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;

import java.io.File;
import java.io.StringReader;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilderFactory;

public class util_xml {
	
	public final static Map charsetEscapeXml10 = new HashMap() {
		private static final long serialVersionUID = 1L;
		{			
			put( "&", "&amp;");
			put( "'", "&apos;");
			put( ">", "&gt;");
			put( "<", "&lt;");
			put( "\"", "&quot;");
			
			put( "\u0000", "");
			put( "\u0001", "");
			put( "\u0002", "");
			put( "\u0003", "");
			put( "\u0004", "");
			put( "\u0005", "");
			put( "\u0006", "");
			put( "\u0007", "");
			put( "\u0008", "");
			put( "\u000b", "");
			put( "\u000c", "");
			put( "\u000e", "");
			put( "\u000f", "");
			put( "\u0010", "");
			put( "\u0011", "");
			put( "\u0012", "");
			put( "\u0013", "");
			put( "\u0014", "");
			put( "\u0015", "");
			put( "\u0016", "");
			put( "\u0017", "");
			put( "\u0018", "");
			put( "\u0019", "");
			put( "\u001a", "");
			put( "\u001b", "");
			put( "\u001c", "");
			put( "\u001d", "");
			put( "\u001e", "");
			put( "\u001f", "");
			put( "\ufffe", "");
			put( "\uffff", "");
		}};

		public final static Map charsetEscapeXml11 = new HashMap() {
			private static final long serialVersionUID = 1L;
			{				
				put( "&", "&amp;");
				put( "'", "&apos;");
				put( ">", "&gt;");
				put( "<", "&lt;");
				put( "\"", "&quot;");				
			
				put( "\u0000", "");
				put( "\u000b", "&#11;");
				put( "\u000c", "&#12;");
				put( "\ufffe", "");
				put( "\uffff", "");
			}};
		
public util_xml() {
	super();
}
public static Document readXML(String uriXML, boolean valid) throws Exception{
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	dbf.setValidating(valid);
	if(uriXML.toUpperCase().trim().indexOf("HTTP:")==-1){
		return  dbf.newDocumentBuilder().parse(new File(uriXML));
	}else return  DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(uriXML);
}
public static Document readXMLData(String dataXML, boolean valid) throws Exception{
	if(dataXML==null) return null;
//	ByteArrayInputStream xmlSrcStream = new	ByteArrayInputStream(dataXML.getBytes());
    InputSource xmlSrcStream = new InputSource();
    xmlSrcStream.setCharacterStream(new StringReader(dataXML));
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(valid);
	return dbf.newDocumentBuilder().parse(xmlSrcStream);
}

public static Document readXML(String uriXML) throws Exception{
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	dbf.setValidating(false);
	if(uriXML.toUpperCase().trim().indexOf("HTTP:")==-1){
		return  dbf.newDocumentBuilder().parse(new File(uriXML));
	}else return  DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(uriXML);
}
public static Document readXMLData(String dataXML) throws Exception{
	if(dataXML==null) return null;
//	ByteArrayInputStream xmlSrcStream = new	ByteArrayInputStream(dataXML.getBytes());
    InputSource xmlSrcStream = new InputSource();
    xmlSrcStream.setCharacterStream(new StringReader(dataXML));
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
	return  dbf.newDocumentBuilder().parse(xmlSrcStream);
}
	
public static String removeNonUtf8CompliantCharacters( final String inString ) {
	if (null == inString ) return null;
	byte[] byteArr = inString.getBytes();
	for ( int i=0; i < byteArr.length; i++ ) {
	byte ch= byteArr[i];
	// remove any characters outside the valid UTF-8 range as well as all control characters
	// except tabs and new lines
	if ( !( (ch > 31 && ch < 253 ) || ch == '\t' || ch == '\n' || ch == '\r') ) {
	byteArr[i]=' ';
	}
	}
	return new String( byteArr );
	}


public static String escapeXML10 (String input, String charSet) {
	if (input==null) return input;	
	if(input.indexOf("<![CDATA[")==0)
		return input;
	try{
		if(charSet!=null) input = new String(input.getBytes(),charSet);
	}catch(Exception e){
		new bsException(e, iStub.log_ERROR);
	}

	StringBuffer result=new StringBuffer();
	for (int i=0;i<input.length();i++) {
		char current = input.charAt(i);
		int codepoint = (int) current;
		boolean beTranslated = false;
		if (codepoint >= 0x7f && codepoint <= 0x84) 
			beTranslated = true;
		else if (codepoint >= 0x86 && codepoint <= 0x9f) 	
			beTranslated = true;		
		
		if(beTranslated) {
			result.append("&#");
			result.append(Integer.toString(codepoint, 10));
			result.append(';');
		}else {
			String beChanged = (String)charsetEscapeXml10.get(String.valueOf(current));
			if(beChanged!=null)
				result.append(beChanged);
			else
				result.append(current);				
		}
	}
	return result.toString();	
}

public static String escapeXML11 (String input, String charSet) {
	if (input==null) return input;	
	if(input.indexOf("<![CDATA[")==0)
		return input;
	try{
		if(charSet!=null) input = new String(input.getBytes(),charSet);
	}catch(Exception e){
		new bsException(e, iStub.log_ERROR);
	}

	StringBuffer result=new StringBuffer();
	for (int i=0;i<input.length();i++) {
		char current = input.charAt(i);
		int codepoint = (int) current;
		boolean beTranslated = false;
		if (codepoint >= 0x1 && codepoint <= 0x8) 
			beTranslated = true;
		else if (codepoint >= 0xe && codepoint <= 0x1f) 
			beTranslated = true;
		else if (codepoint >= 0x7f && codepoint <= 0x84) 
			beTranslated = true;
		else if (codepoint >= 0x86 && codepoint <= 0x9f) 
			beTranslated = true;
		
		if(beTranslated) {
			result.append("&#");
			result.append(Integer.toString(codepoint, 10));
			result.append(';');
		}else {
			String beChanged = (String)charsetEscapeXml10.get(String.valueOf(current));
			if(beChanged!=null)
				result.append(beChanged);
			else
				result.append(current);				
		}
	}
	return result.toString();
}

public static String normalXML (String input, String charSet) {	
	if (input==null) return input;	
	if(input.indexOf("<![CDATA[")==0)
		return input;
	try{
		if(charSet!=null) input = new String(input.getBytes(),charSet);
	}catch(Exception e){
		new bsException(e, iStub.log_ERROR);
	}

	String result="";
	if (input.indexOf("&")>-1 ||
//		input.indexOf("\'")>-1 ||
		input.indexOf(">")>-1 ||
		input.indexOf("<")>-1 ||
		input.indexOf("\"")>-1) { 

		for (int i=0;i<input.length();i++) {
			if (input.charAt(i)=='&') result+="&amp;";
//			else if (input.charAt(i)=='\'') result+="&apos;";
			else if (input.charAt(i)=='>') result+="&gt;";
			else if (input.charAt(i)=='<') result+="&lt;";
			else if (input.charAt(i)=='"') result+="&quot;";
			else result+=input.charAt(i);
		}
		return result;
	}
	else 
		return input;
}

public static String normalASCII(String input){
	if(input==null || input.length()==0) return "";
	String result="";
	for(int i=0;i<input.length();i++){
		char c = input.charAt(i); 
		int ascii = (int)c;
//		if(ascii!=19 && ascii!=7 && ascii!=25)
//			result+="&#"+ascii+";";
		
		if ((ascii == 0x9) ||
            (ascii == 0xA) ||
            (ascii == 0xD) ||
            ((ascii >= 0x20) && (ascii <= 0xD7FF)) ||
            ((ascii >= 0xE000) && (ascii <= 0xFFFD)) ||
            ((ascii >= 0x10000) && (ascii <= 0x10FFFF))){
			result+="&#"+ascii+";";
        }		
		
	}
	
	return result;
	
}

public static String normalHTML(String input, String charSet) {	
	if (input==null) return "";
	
	try{
		if(charSet!=null) input = new String(input.getBytes(),charSet);
	}catch(Exception e){

	}

	String result="";
	if (input.indexOf("&")>-1 ||
		input.indexOf("\\")>-1 ||
		input.indexOf(">")>-1 ||
		input.indexOf("<")>-1 ||
		input.indexOf("\"")>-1) { 

		for (int i=0;i<input.length();i++) {
			if (input.charAt(i)=='&') result+="&amp;";
			else if (input.charAt(i)=='\'') result+="&apos;";
			else if (input.charAt(i)=='>') result+="&gt;";
			else if (input.charAt(i)=='<') result+="&lt;";
			else if (input.charAt(i)=='"') result+="&quot;";
			else result+=input.charAt(i);
		}
		return result;
	}
	else 
		return input;
}

public static Map convertFilters(List filters){
	Map treeFilters = null;
	if(filters!=null){
		treeFilters = new HashMap();
		Iterator it = filters.iterator();
		while(it.hasNext()){
			String filter = it.next().toString();
			if(filter.indexOf(".")>-1){
				Map current = null; 
				StringTokenizer st = new StringTokenizer(filter, ".");
				while(st.hasMoreTokens()){
					String part = st.nextToken();
					if(current==null){
						current=(Map)treeFilters.get(part);
						if(current==null){
							current = new HashMap();
							treeFilters.put(part, current);
						}
					}else{
						Map subcurrent = (Map)current.get(part);
						if(subcurrent==null){
							subcurrent = new HashMap();
							current.put(part, subcurrent);									
						}	
						current = subcurrent;
					}
				}
			}else{
				if(treeFilters.get(filter)==null)
					treeFilters.put(filter, new HashMap());
			}
		}
	}
	return treeFilters;
}

}

