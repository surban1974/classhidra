package it.classhidra.qreports;

import java.math.BigDecimal;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import it.classhidra.core.tool.elements.elementBase;
import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.serialize.Serialized;





@Serialized
public class parameter extends elementBase{
	private static final long serialVersionUID = 1L;
	
	public final static String VALUE_NUMBER=		"NUMBER";
	public final static String VALUE_CHARACTER=		"CHARACTER";
	public final static String VALUE_DATE=			"DATE";
	
	public final static String VIEW_INPUT=			"INPUT";
	public final static String VIEW_SELECT=			"SELECT";
	public final static String VIEW_HIDDEN=			"HIDDEN";
	
	public final static String EXEC_NORMALLY=		"";
	public final static String EXEC_BEFORE_SQL=		"BEFORE_SQL";
	public final static String EXEC_IMMEDIATELY=	"IMMEDIATELY";
	
	@Serialized
	private String name;
	
	@Serialized(false)
	private String description;
	
	@Serialized(false)
	private String format_input;
	
	@Serialized(false)
	private String format_language;

	@Serialized(false)
	private String format_country;

	@Serialized(false)
	private String format_currency;
	
	@Serialized(false)
	private String format_timezone_shift;	
	
	@Serialized(false)
	private String format_output;
	
	@Serialized
	private String value_type; 	// NUMBER, CHARACTER, DATE 
	
	@Serialized
	private String view_type; 	// INPUT, SELECT
	
	@Serialized(false)
	private String exec_type; 	// NORMALLY, BEFORE_SQL, IMMEDIATELY	
	
	@Serialized(false)
	private String default_value;
	
	@Serialized(false)
	private String initial_default_value;
	
	@Serialized(false)
	private String length;
	
	@Serialized(false)
	private String mandatory;
	
	@Serialized(false)
	private String adaptsql;
	
	@Serialized(false)
	private languagetranslationtable languagetranslationtable;
	
	@Serialized
	private source source;
	
	@Serialized(false)
	private HashMap<String, parameter> dependencies;
	
	@Serialized(false)
	private String description_view;
	
	public parameter(){
		super();
		init();
	}
	
	public void init(){
		name="";
		description="";
		languagetranslationtable=new languagetranslationtable();
		format_input="";
		format_country="";
		format_language="";
		format_timezone_shift="";
		format_output="";
		value_type=""; 
		view_type="";
		default_value="";
		mandatory="YES";
		adaptsql="";
		exec_type=EXEC_NORMALLY;
		length="";
		source=new source();
		dependencies=new HashMap<String, parameter>();
		
		description_view="";
	}	
	
	public void load4view(Statement st, HashMap<String, parameter> h_parameters){
		load4view(st, h_parameters, null);
	}
	
	public void load4view(Statement st, HashMap<String, parameter> h_parameters, String lang){
		try{
			default_value = sqlTransformer.getTransformed( default_value, this, h_parameters, lang);
		}catch(Exception e){		
			new bsException(e, iStub.log_ERROR);
		}
		try{
			description_view = sqlTransformer.getTransformed( description_view, this, h_parameters, lang);
		}catch(Exception e){	
			new bsException(e, iStub.log_ERROR);
		}	
		
		try{
			format_input = sqlTransformer.getTransformed( format_input, this, h_parameters, lang);
		}catch(Exception e){		
			new bsException(e, iStub.log_ERROR);
		}
		try{
			format_output = sqlTransformer.getTransformed( format_output, this, h_parameters, lang);
		}catch(Exception e){		
			new bsException(e, iStub.log_ERROR);
		}
		try{
			format_language = sqlTransformer.getTransformed( format_language, this, h_parameters, lang);
		}catch(Exception e){		
			new bsException(e, iStub.log_ERROR);
		}
		try{
			format_country = sqlTransformer.getTransformed( format_country, this, h_parameters, lang);
		}catch(Exception e){		
			new bsException(e, iStub.log_ERROR);
		}
		try{
			format_currency = sqlTransformer.getTransformed( format_currency, this, h_parameters, lang);
		}catch(Exception e){		
			new bsException(e, iStub.log_ERROR);
		}	
		try{
			format_timezone_shift = sqlTransformer.getTransformed( format_timezone_shift, this, h_parameters, lang);
		}catch(Exception e){		
			new bsException(e, iStub.log_ERROR);
		}		
//		description_view=description;
		if(	languagetranslationtable!=null){
			languagetranslationtable.load4view(st,h_parameters, lang);
			if(languagetranslationtable.getDescription_view()!=null && !languagetranslationtable.getDescription_view().equals(""))
				description_view=languagetranslationtable.getDescription_view();
		}
		if(source!=null)
			source.load4view(st,this,h_parameters, lang);
	}

	public void loadParameter(Statement st, HashMap<String, parameter> h_parameters){
		loadParameter(st, h_parameters, null);
	}
	
	public void loadParameter(Statement st, HashMap<String, parameter> h_parameters, String lang){
		if(source!=null)
			default_value = source.loadParameter(st, this, h_parameters, lang);
	}

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
		this.description_view = description;
	}
	public String getValue_type() {
		return value_type;
	}
	public void setValue_type(String valueType) {
		value_type = valueType;
	}
	public String getView_type() {
		return view_type;
	}
	public void setView_type(String viewType) {
		view_type = viewType;
	}
	public String getDefault_value() {
		return default_value;
	}
	public void setDefault_value(String defaultValue) {
		default_value = defaultValue;
		if(initial_default_value==null)
			initial_default_value = defaultValue;
	}
	public void setValue(String value) throws Exception {
		if(value==null)
			return;
		if(!this.getView_type().equals(parameter.VIEW_HIDDEN)){
			value=value.trim();

			if(this.getValue_type().equals(parameter.VALUE_DATE)){
				Date dat = null;
				try{
					if(!this.getFormat_input().equals("")){
						dat = util_format.stringToData(value, this.getFormat_input());
					}else{
						dat = util_format.stringToData(value, "yyyy-MM-dd");
					}
				}catch(Exception ex){
					dat=null;
				}
				if(dat==null)
					throw new bsException("ERROR get value = "+this.getName(), iStub.log_ERROR);
				

				if(!this.getFormat_output().equals("")){
					value=util_format.dataToString(dat, this.getFormat_output());
				}else{
					value=util_format.dataToString(dat, "yyyy-MM-dd");
				}
				if(value==null)
					throw new bsException("ERROR get value = "+this.getName(), iStub.log_ERROR);

				this.setDefault_value(value);
			}
			if(this.getValue_type().equals(parameter.VALUE_CHARACTER)){
				try{
					if(value!=null) {
						if(!this.getFormat_output().equals("") && this.getFormat_output().toLowerCase().indexOf("sqlescape")>-1)
							value = util_format.convertAp(value);
					}
				}catch(Exception e){
					if(value==null)
						throw new bsException("ERROR get value = "+this.getName(), iStub.log_ERROR);
				}
				this.setDefault_value(value);
			}
			if(this.getValue_type().equals(parameter.VALUE_NUMBER)){
				Number num = null;
				try{
					if(!this.getFormat_input().equals("")){
						num = new DecimalFormat(this.getFormat_input()).parse(value);
					}else{
						num = new BigDecimal(value);
					}
					if(num==null)
						throw new bsException("ERROR get value = "+this.getName(), iStub.log_ERROR);

					if(!this.getFormat_output().equals("")){
						value = new DecimalFormat(this.getFormat_output()).format(num.longValue());
					}else{
						value = num.toString();
					}
				}catch(Exception e){
					if(num==null)
						throw new bsException("ERROR get value = "+this.getName(), iStub.log_ERROR);
				}
				this.setDefault_value(value);
			}
		}

	}	
	
	public source getSource() {
		return source;
	}
	public void setSource(source source) {
		this.source = source;
	}

	public String getFormat_input() {
		return format_input;
	}

	public void setFormat_input(String formatInput) {
		format_input = formatInput;
	}

	public String getFormat_output() {
		return format_output;
	}

	public void setFormat_output(String formatOutput) {
		format_output = formatOutput;
	}


	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getMandatory() {
		return mandatory;
	}

	public void setMandatory(String mandatory) {
		this.mandatory = mandatory;
	}
	
	public String getTagname(){
		return "parameter";
	}

	public languagetranslationtable getLanguagetranslationtable() {
		return languagetranslationtable;
	}

	public void setLanguagetranslationtable(
			languagetranslationtable languagetranslationtable) {
		this.languagetranslationtable = languagetranslationtable;
	}

	public String getDescription_view() {
		return description_view;
	}

	public void setDescription_view(String descriptionView) {
		description_view = descriptionView;
	}
	
	public String toHtml(){
		String html="";
		
		if(view_type.equals(VIEW_INPUT)){
			html+="<input ";
			html+="name=\""+util_format.convertAp(name)+"\" id=\""+util_format.convertAp(name)+"\" ";
//			html+="class=\"repInput\" ";
			if(!length.equals(""))
				html+="maxlength="+length+" ";
			if(mandatory.toUpperCase().equals("YES"))
				html+="style=\"background-color: #FFAAAA;width: 350px;\" ";
			else html+="width: 350px;\" ";
			String value = getFormat(default_value);
			html+="value=\""+value+"\" ";
			html+="/>";		
		}
		if(view_type.equals(VIEW_SELECT)){
			html+="<select ";
			html+="name=\""+util_format.convertAp(name)+"\" id=\""+util_format.convertAp(name)+"\" ";
//			html+="class=\"repSelect\" ";
			if(mandatory.toUpperCase().equals("YES"))
				html+="style=\"background-color: #FFAAAA;width: 350px;\" ";
			else html+="style=\"width: 350px;\" ";
			String value = getFormat(default_value);
			html+="value=\""+value+"\" ";
			html+=">\n";
			if(source!=null && source.getList()!=null){
				for(source_item sitem:source.getList()){
					html+="     <option  value=\""+sitem.getValue()+"\" ";
					if(sitem.getValue().equals(default_value)) html+="selected ";
					html+=">"+sitem.getDescription_view()+"\n";
				}
			}
			html+="</select>";
		}
		
		
		return html;
	}
	
	private String getFormat(String value){
		if(!format_input.equals("")){
			Locale locale = null;
			try{
				if(format_language!=null && !format_language.equals("")){
					if(format_country!=null && !format_country.equals(""))
						locale = new Locale(format_language,format_country);
					else
						locale = new Locale(format_language);
				}
			}catch(Exception e){		
			}
			
			if(!format_output.equals("")){
				if(value_type.equals(VALUE_DATE)){
					try{
						Date resultObject = util_format.stringToData(value, format_output, locale);
						if(resultObject!=null){
							value = util_format.dataToString(resultObject, format_input, locale);
						}
					}catch(Exception e){
					}
				}
				if(value_type.equals(VALUE_NUMBER)){
					try{
						Long ref = new Long(new DecimalFormat(format_output).parse(value.trim()).longValue());
//						value = util_format.makeFormatedString(format_input, null, ref);
						value = util_format.makeFormatedString(format_input, format_language, format_country, format_timezone_shift, format_currency, ref);						
					}catch(Exception e){
						e.toString();
					}	
				}
			}else{
				try{
//					value = util_format.makeFormatedString(format_input, null, value);
					value = util_format.makeFormatedString(format_input, format_language, format_country, format_timezone_shift, format_currency, value);
				}catch(Exception e){
				}
			}
			
		}
		return value;
	}

	public String getExec_type() {
		return exec_type;
	}

	public void setExec_type(String execType) {
		exec_type = execType;
	}
	
	public String toString(){
		return name+":"+default_value;
	}
	
	@Serialized
	public String getFormattedValue(){
		return getFormat(default_value);
	}

	public String getAdaptsql() {
		return adaptsql;
	}

	public void setAdaptsql(String adaptsql) {
		this.adaptsql = adaptsql;
	}

	public HashMap<String, parameter> getDependencies() {
		return dependencies;
	}

	public void setDependencies(HashMap<String, parameter> dependencies) {
		this.dependencies = dependencies;
	}

	public String getInitial_default_value() {
		return initial_default_value;
	}

	public String getFormat_language() {
		return format_language;
	}

	public void setFormat_language(String format_language) {
		this.format_language = format_language;
	}

	public String getFormat_country() {
		return format_country;
	}

	public void setFormat_country(String format_country) {
		this.format_country = format_country;
	}

	public String getFormat_currency() {
		return format_currency;
	}

	public void setFormat_currency(String format_currency) {
		this.format_currency = format_currency;
	}

	public String getFormat_timezone_shift() {
		return format_timezone_shift;
	}

	public void setFormat_timezone_shift(String format_timezone_shift) {
		this.format_timezone_shift = format_timezone_shift;
	}

}
