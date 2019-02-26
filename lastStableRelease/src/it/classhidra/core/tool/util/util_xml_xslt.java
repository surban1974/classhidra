package it.classhidra.core.tool.util; 

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;

public class util_xml_xslt {

	public static String xml_xslt_transform2String(byte[] xml, byte[] xslt)   throws Exception {
		
		javax.xml.transform.Source xmlSource = new javax.xml.transform.stream.StreamSource(new ByteArrayInputStream(xml));
		javax.xml.transform.Source xsltSource = new javax.xml.transform.stream.StreamSource(new ByteArrayInputStream(xslt));

		ByteArrayOutputStream baos= xml_xslt_transform(xmlSource, xsltSource);
	    String xml_result=new String(baos.toByteArray());
	    baos.close();
		return xml_result;
	}
	
	public static byte[] xml_xslt_transform2Byte(byte[] xml, byte[] xslt)   throws Exception {
		
		javax.xml.transform.Source xmlSource = new javax.xml.transform.stream.StreamSource(new ByteArrayInputStream(xml));
		javax.xml.transform.Source xsltSource = new javax.xml.transform.stream.StreamSource(new ByteArrayInputStream(xslt));

		ByteArrayOutputStream baos= xml_xslt_transform(xmlSource, xsltSource);
		byte[] res = baos.toByteArray();
		baos.close();
		return res;	
	}
	
	public static String xml_xslt_transform2String(File xmlFile, File xsltFile)   throws Exception {
		
		javax.xml.transform.Source xmlSource = new javax.xml.transform.stream.StreamSource(xmlFile);
		javax.xml.transform.Source xsltSource = new javax.xml.transform.stream.StreamSource(xsltFile);

		ByteArrayOutputStream baos= xml_xslt_transform(xmlSource, xsltSource);
	    String xml_result=new String(baos.toByteArray());
	    baos.close();
		return xml_result;
}

	public static String xml_xslt_transform2String(String xml, String xslt)   throws Exception {
		
		javax.xml.transform.Source xmlSource = new javax.xml.transform.stream.StreamSource(new ByteArrayInputStream(xml.getBytes("UTF-8")));
		javax.xml.transform.Source xsltSource = new javax.xml.transform.stream.StreamSource(new ByteArrayInputStream(xslt.getBytes("UTF-8")));
		
		ByteArrayOutputStream baos= xml_xslt_transform(xmlSource, xsltSource);
	    String xml_result=new String(baos.toByteArray());	
	    baos.close();
		return xml_result;

}	
	
	public static String xml_xslt_transform2String(String xml, File xsltFile)   throws Exception {
		
		javax.xml.transform.Source xmlSource = new javax.xml.transform.stream.StreamSource(new ByteArrayInputStream(xml.getBytes("UTF-8")));
		javax.xml.transform.Source xsltSource = new javax.xml.transform.stream.StreamSource(xsltFile);
		
		ByteArrayOutputStream baos= xml_xslt_transform(xmlSource, xsltSource);
	    String xml_result=new String(baos.toByteArray());
	    baos.close();
		return xml_result;

	}		

	public static byte[] xml_xslt_transform2Byte(File xmlFile, File xsltFile)   throws Exception {
		
		javax.xml.transform.Source xmlSource = new javax.xml.transform.stream.StreamSource(xmlFile);
		javax.xml.transform.Source xsltSource = new javax.xml.transform.stream.StreamSource(xsltFile);

		ByteArrayOutputStream baos= xml_xslt_transform(xmlSource, xsltSource);
		byte[] res = baos.toByteArray();
		baos.close();
		return res;
}

	public static byte[] xml_xslt_transform2Byte(String xml, String xslt)   throws Exception {
		
		javax.xml.transform.Source xmlSource = new javax.xml.transform.stream.StreamSource(new ByteArrayInputStream(xml.getBytes("UTF-8")));
		javax.xml.transform.Source xsltSource = new javax.xml.transform.stream.StreamSource(new ByteArrayInputStream(xslt.getBytes("UTF-8")));
		
		ByteArrayOutputStream baos= xml_xslt_transform(xmlSource, xsltSource);
		byte[] res = baos.toByteArray();
		baos.close();
		return res;

}	
	
	public static byte[] xml_xslt_transform2Byte(String xml, File xsltFile)   throws Exception {
		
		javax.xml.transform.Source xmlSource = new javax.xml.transform.stream.StreamSource(new ByteArrayInputStream(xml.getBytes("UTF-8")));
		javax.xml.transform.Source xsltSource = new javax.xml.transform.stream.StreamSource(xsltFile);
		
		ByteArrayOutputStream baos= xml_xslt_transform(xmlSource, xsltSource);
		byte[] res = baos.toByteArray();
		baos.close();
		return res;

	}			
	
	public static ByteArrayOutputStream  xml_xslt_transform(Source xmlSource, Source xsltSource)   throws Exception {
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		Result result = new javax.xml.transform.stream.StreamResult(baos);
		TransformerFactory transFact = javax.xml.transform.TransformerFactory.newInstance(  );
		Transformer trans = transFact.newTransformer(xsltSource);
		trans.transform(xmlSource, result);
		return baos;
}		
	
}
