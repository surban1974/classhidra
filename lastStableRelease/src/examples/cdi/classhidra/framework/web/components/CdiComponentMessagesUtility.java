package examples.cdi.classhidra.framework.web.components;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import au.id.jericho.lib.html.Attributes;
import au.id.jericho.lib.html.Element;
import au.id.jericho.lib.html.MasonTagTypes;
import au.id.jericho.lib.html.PHPTagTypes;
import au.id.jericho.lib.html.Source;
import it.classhidra.annotation.elements.Action;
import it.classhidra.annotation.elements.NavigatedDirective;
import it.classhidra.core.controller.action;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.exception.message;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_format;


@Action (
	path="messagesUtility",
	name="formMessagesUtility",
	redirect="/jsp/framework/messagesUtility.jsp",
    reloadAfterAction="true"
)


@NavigatedDirective(memoryContent="true")

public class CdiComponentMessagesUtility extends action implements i_action, Serializable{
	private static final long serialVersionUID = -3476492198668495035L;
	private String integrity_log;
	private HashMap presented ;
	private String input_dir_path;

	private String pathInput;
	private String pathOutput;
	private String langs;


public CdiComponentMessagesUtility(){
	super();

}

public redirects actionservice(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException {

	integrity_log="";integrity_log+="";
	presented=new HashMap();

	if(middleAction==null) middleAction="";
	if(middleAction.equals("generate")){
		Vector langs=new Vector();
		StringTokenizer st = new StringTokenizer((String)get_bean().getCampoValue("langs"),";");
		while(st.hasMoreTokens()){
			String key = st.nextToken();
			if(!key.equals("IT")) langs.add(key);
		}


//		String rootPath = bsController.getAppInit().get_path_config().replace('\\','/').replace("/config/","");
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		String tmpPath = rootPath+"/tmp/";
		String tmpPathDate = rootPath+"/tmp/"+util_format.dataToString(new Date(),"yyyyMMddhhmm");

		try{
			File tmp = new File(tmpPath);
			if(!tmp.exists()) tmp.mkdir();
			new File(tmpPathDate).mkdir();
		}catch(Exception e){}


		String inp = pathInput;
		inp = util_format.replace(inp,"\\","/");
		String pathInput1= "";
		if( !inp.equals("/") && new File(inp).exists()){
			pathInput1=util_format.replace( pathInput,"//","/");
		}else{
			pathInput1=util_format.replace( (rootPath+pathInput),"//","/");
		}
		String mask_name=util_format.replace( pathInput,"//","/").replace('/','.').replace(':', '.');
		if(mask_name.length()>20) mask_name=mask_name.substring(0,20);
		mask_name=mask_name.replace('.', '_');

		File input = new File(pathInput1);
		if(input.exists()){

			input_dir_path = input.getAbsolutePath();

			if(input.isFile() ){
				if(checkExt(input))
					elaborateFile(input,langs,tmpPathDate,mask_name);
			}
			else elaborateDir(input,langs,tmpPathDate,mask_name);
		}
		write2file(integrity_log,tmpPathDate+"/integrity.log");
		try{
			service_toZip(tmpPathDate,"messages.zip",new File(tmpPathDate).list());
			File[] forDelete =new File(tmpPathDate).listFiles();
			for(int i=0;i<forDelete.length;i++){
				if(!forDelete[i].getName().equals("messages.zip")) forDelete[i].delete();
			}
			get_bean().set("pathOutput",util_format.replace(tmpPathDate,rootPath,"")+"/messages.zip");

		}catch(Exception e){
		}

	}else{
		get_bean().set("pathOutput","");
	}
	return new redirects(get_infoaction().getRedirect());
}

private void elaborateFile(File input,Vector langs, String tmpPathDate,String mask_name){
	String name = input.getName().toLowerCase();
	if(	(name+"|").indexOf(".jsp|")>-1 ||
		(name+"|").indexOf(".xml|")>-1 ||
		(name+"|").indexOf(".config|")>-1 ){
		Vector mess = new Vector();
		try{
			mess=getMessages("file:"+input.getAbsolutePath());
			prepareContent(mess,langs,tmpPathDate,input.getAbsolutePath(),mask_name);
		}catch(Exception e){
			new bsControllerException(e,iStub.log_ERROR);
		}
	}
}

private void elaborateDir(File input,Vector langs, String tmpPathDate,String mask_name){
	File[] list = input.listFiles();
	if(list.length==0) return;
	for(int i=0;i<list.length;i++){
		File current = list[i];
		if(current.isFile()){
			if(checkExt(current))
				elaborateFile(current,langs,tmpPathDate,mask_name);
		}
		else elaborateDir(current,langs,tmpPathDate,mask_name+"."+current.getName());
	}
}

private boolean checkExt(File current){
	String exts = ";XML;JSP;";
	boolean result = false;
	if(current==null) return result;
	String ext = "";
	try{
		ext = current.getName();
		ext = ext.substring(ext.lastIndexOf(".")+1).toUpperCase();

	}catch(Exception e){

	}
	if(	exts.indexOf(";"+ext+";")>-1) result=true;
	return result;
}

private String getURLContent(String url_request) throws Exception {
	String result = "";
	try{
		URL url = new URL(url_request);
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		String inputLine;
		while ((inputLine = in.readLine()) != null)
			result+=inputLine+"\n";
		in.close();
	}catch(Exception e){

	}
	return result;
}



private  Vector getMessages(String file) throws Exception {
	file = util_format.replace(file,"//","/");
	Vector result = new Vector();
	String sourceUrlString=file;

	PHPTagTypes.register();
	PHPTagTypes.PHP_SHORT.deregister();
	MasonTagTypes.register();
	String test = "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\"/>\n";
		test+=getURLContent(sourceUrlString);


//	Source source=new Source(new URL(sourceUrlString));
	Source source=new Source(test.subSequence(0,test.length()));
	source.setLogWriter(new OutputStreamWriter(System.err)); // send log messages to stderr
	source.fullSequentialParse();
	List elementList=source.findAllElements();
	HashMap jspParamDesc = new HashMap();
	for (Iterator i=elementList.iterator(); i.hasNext();) {
		Element element=(Element)i.next();
		if(element.getName().indexOf("bs:message")==0){
			Attributes atts = element.getAttributes();
			if (atts!=null && atts.get("code")!=null){
				message mes = new message();
				mes.setCD_MESS(atts.get("code").getValue());
				mes.setCD_LANG("IT");
				try{
					mes.setDESC_MESS(atts.get("defaultValue").getValue());
				}catch(Exception e){
				}
				result.add(mes);
			}
		}
		if(element.getName().indexOf("jsp:param")==0){
			Attributes atts = element.getAttributes();
			if (atts!=null && atts.get("name")!=null && atts.get("name").getValue().indexOf("code_tab_desc")==0){
				jspParamDesc.put(util_format.replace(atts.get("name").getValue(),"code_tab_desc","def_tab_desc"), atts.get("value").getValue());
			}
			if (atts!=null && atts.get("name")!=null && atts.get("name").getValue().indexOf("def_tab_desc")==0){
				String code = (String)jspParamDesc.get(atts.get("name").getValue());
				if(code!=null){
					message mes = new message();
					mes.setCD_MESS(code);
					mes.setCD_LANG("IT");
					try{
						mes.setDESC_MESS(atts.get("value").getValue());
					}catch(Exception e){
					}
					result.add(mes);
					jspParamDesc.remove(atts.get("name").getValue());
				}
			}
		}
		if(element.getName().indexOf("bs:show_lm_actionlabel")==0){
			Attributes atts = element.getAttributes();
			if (atts!=null && atts.get("message_code")!=null){
				message mes = new message();
				mes.setCD_MESS(atts.get("message_code").getValue());
				mes.setCD_LANG("IT");
				try{
					mes.setDESC_MESS(atts.get("message_defaultValue").getValue());
				}catch(Exception e){
				}
				result.add(mes);
			}
		}
		if(	element.getName().indexOf("element")==0 ||
			element.getName().indexOf("form-redirect")==0){
			Attributes atts = element.getAttributes();
			if (atts!=null && atts.get("mess_id")!=null){
				message mes = new message();
				mes.setCD_MESS(atts.get("mess_id").getValue());
				mes.setCD_LANG("IT");
				try{
					mes.setDESC_MESS(atts.get("descr").getValue());
				}catch(Exception e){
				}
				result.add(mes);
			}
		}
	}

	return result;
}

private void prepareContent(Vector mess,Vector langs,  String tmpPathDate, String fInput, String mask_name){
	String sep = System.getProperty("line.separator");
	String jsp_name = util_format.replace(fInput.replace('\\','/'),"//","/");

	String tmp =  util_format.replace(input_dir_path.replace('\\','/'),"//","/");
	String view_jsp_name = util_format.replace( jsp_name,tmp, "");

	jsp_name=jsp_name.substring(jsp_name.lastIndexOf("/")+1,jsp_name.length());



	 String tmp_integrity_log = "";


	String 	result="";
	for(int i=0;i<mess.size();i++){
		message mes = (message)mess.get(i);
		if(mes.getCD_MESS()!=null && !mes.getCD_MESS().trim().equals("")  && presented.get(mes.getCD_MESS())!=null){
			tmp_integrity_log = tmp_integrity_log +"          KO-->" + "[" +mes.getCD_MESS()+"] ["+mes.DESC_MESS+"] si presenta: "+presented.get(mes.getCD_MESS())+sep;
		}
		else presented.put(mes.getCD_MESS(),jsp_name);
		if(!isPresentMessage(mes)){
			if(result.indexOf("cd_mess=\""+mes.getCD_MESS()+"\"")==-1){
				result+="	"+mes.toString()+sep;
				for(int j=0;j<langs.size();j++){
					mes.setCD_LANG((String)langs.get(j));
					mes.setDESC_MESS("???");
					result+="	"+mes.toString()+sep;
				}
				result+=sep;
			}
		}
	}
	if(!result.equals("")){
		result="<messages view=\""+jsp_name+"\"> "+sep+result;
		result+="</messages>";
		String fileName = mask_name+"."+jsp_name+".xml";
		while (fileName.indexOf("..")>-1){
			fileName=util_format.replace(fileName,"..",".");
		}

		fileName = tmpPathDate+"/"+fileName;

		result = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"+sep+result;
		write2file(result,fileName);
	}
	if(!tmp_integrity_log.equals(""))
		integrity_log+=sep + view_jsp_name +sep+tmp_integrity_log;

}
private boolean isPresentMessage(message mes){
	if(bsController.getMess_config().get_messages().get(mes.getCD_LANG()+"."+mes.getCD_MESS())!=null) return true;
	else return false;
}

private void write2file(String content,String path) {
	try {

		FileOutputStream fileOut = new FileOutputStream(path);
		PrintStream Output = new PrintStream(fileOut);
		Output.print(content);
		Output.close();
		fileOut.close();
	} catch (Exception e) {

	}
}
private void service_toZip(String outFolder, String outFilename, String[] filenames) throws Exception{
	String[] fileLabels=null;
	if(fileLabels==null || fileLabels.length<filenames.length){
		fileLabels = new String[filenames.length];
		for(int i=0;i<filenames.length;i++)
			fileLabels[i]=filenames[i];
	}
	byte[] buf = new byte[1024];
	ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFolder+"/"+outFilename));
	for (int i=0; i<filenames.length; i++) {
		if(new File(outFolder+"/"+filenames[i]).exists()){
			FileInputStream in = new FileInputStream(outFolder+"/"+filenames[i]);
			out.putNextEntry(new ZipEntry(fileLabels[i]));
			int len;
			while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
			out.closeEntry();
			in.close();
		}
	}
	out.flush();
	out.finish();
	out.close();
}

public void reimposta(){
	pathInput="/jsp/";
	pathOutput="";
	langs="IT;EN;DC;FR;";
}

public redirects validate(HttpServletRequest request){
	return null;
}

public String getLangs() {
	return langs;
}

public void setLangs(String langs) {
	this.langs = langs;
}

public String getPathInput() {
	return pathInput;
}

public void setPathInput(String pathInput) {
	this.pathInput = pathInput;
}

public String getPathOutput() {
	return pathOutput;
}

public void setPathOutput(String pathOutput) {
	this.pathOutput = pathOutput;
}

}