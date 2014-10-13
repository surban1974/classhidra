package it.classhidra.core.tool.log.statistic;

import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.util_xml;

import java.io.Serializable;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

public class StatisticEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	private String session;
	private String ip;
	private String matriculation;
	private String lang;
	private String action;
	private Throwable exception;
	private Date st;
	private Date ft;
	private String bi;
	HttpServletRequest request;
	
	public StatisticEntity(){
		super();
	}
	
	public StatisticEntity(String _session, String _ip, String _matriculation, String _lang, String _action, Throwable _exception, Date _st,Date _ft, HttpServletRequest _request){
		super();
		session=_session;
		ip=_ip;
		matriculation=_matriculation;
		lang=_lang;
		action=_action;
		exception=_exception;
		st=_st;
		ft=_ft;
		request=_request;
		if(request!=null){
			bi = getBrowserInfo(request.getHeader("User-Agent"));
		}
		
	}
	
	public String getSession() {
		return session;
	}
	public void setSession(String session) {
		this.session = session;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getMatriculation() {
		return matriculation;
	}
	public void setMatriculation(String matriculation) {
		this.matriculation = matriculation;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Throwable getException() {
		return exception;
	}
	public void setException(Throwable exception) {
		this.exception = exception;
	}
	public Date getSt() {
		return st;
	}
	public void setSt(Date st) {
		this.st = st;
	}
	public Date getFt() {
		return ft;
	}
	public void setFt(Date ft) {
		this.ft = ft;
	}
	public long getDelta(){
		if(st==null || ft==null) return 0;
		return ft.getTime()-st.getTime();
	}
	
	private String  getBrowserInfo( String information ){
		if(information == null) return null;
		try{
		    String browsername = "";
		    String browserversion = "";
		    String browser = information;
		    if (browser.contains("MSIE")){
		      String subsString = browser.substring(browser.indexOf("MSIE"));
		      String Info[] = (subsString.split(";")[0]).split(" ");
		      browsername = Info[0];
		      browserversion = Info[1];
		    } else if (browser.contains("Firefox")){
	
		      String subsString = browser.substring(browser.indexOf("Firefox"));
		      String Info[] = (subsString.split(" ")[0]).split("/");
		      browsername = Info[0];
		      browserversion = Info[1];
		    } else if (browser.contains("Chrome")){
	
		      String subsString = browser.substring(browser.indexOf("Chrome"));
		      String Info[] = (subsString.split(" ")[0]).split("/");
		      browsername = Info[0];
		      browserversion = Info[1];
		    } else if (browser.contains("Opera")){
	
		      String subsString = browser.substring(browser.indexOf("Opera"));
		      String Info[] = (subsString.split(" ")[0]).split("/");
		      browsername = Info[0];
		      browserversion = Info[1];
		    } else if (browser.contains("Safari")){
	
		      String subsString = browser.substring(browser.indexOf("Safari"));
		      String Info[] = (subsString.split(" ")[0]).split("/");
		      browsername = Info[0];
		      browserversion = Info[1];
		    }
		    return browsername + "-" + browserversion + ((information.indexOf("Mobile") != -1)?"-mobile":"");
		}catch(Exception e){
			return null;
		}
	  }
	
	
	public String toString(){
		return toXml();
	}
	
	public String toXml(){
		String result="<event ";
		if(st!=null) result+="ld=\""+util_format.dataToString(st, "yyyy-MM-dd HH:mm")+"\" ";
		if(session!=null) result+="s=\""+util_xml.normalXML(session, null)+"\" ";
		if(ip!=null) result+="ip=\""+util_xml.normalXML(ip, null)+"\" ";
		if(matriculation!=null) result+="m=\""+util_xml.normalXML(matriculation, null)+"\" ";
		if(lang!=null) result+="l=\""+util_xml.normalXML(lang, null)+"\" ";
		if(action!=null) result+="a=\""+util_xml.normalXML(action, null)+"\" ";
		if(exception!=null) result+="e=\""+util_xml.normalXML(exception.toString(), null)+"\" ";
		if(st!=null) result+="st=\""+util_format.dataToString(st, "yyyyMMddHHmmssSSS")+"\" ";
//		if(ft!=null) result+="ft=\""+util_format.dataToString(ft, "yyyyMMddHHmmssSSS")+"\" ";
		if(getDelta()>0) result+="d=\""+getDelta()+"\" ";
		if(bi!=null) result+="bi=\""+util_xml.normalXML(bi, null)+"\" ";
		result+="/>"; 
		return result;		
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getBi() {
		return bi;
	}

	public void setBi(String bi) {
		this.bi = bi;
	}
}
