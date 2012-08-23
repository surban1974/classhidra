
package it.classhidra.core.tool.util.multipart;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.servlet.ServletInputStream;


public class MultipartDataParameter extends MultipartData {

	private byte[] bValue;		// Contents of the parameter
	private String sEncoding;

	/**
	 * @param string
	 */
	public MultipartDataParameter(String sName, ServletInputStream sisInput, String sBoundary, String sEncoding) throws IOException {

		super(sName);
		
		this.sEncoding = sEncoding;

		// Copy the part's contents into a byte array
		PartInputStream pis = new PartInputStream(sisInput, sBoundary);
		ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
		byte[] buf = new byte[128];
		int read;
		while ((read = pis.read(buf)) != -1) {
			baos.write(buf, 0, read);
		}
		pis.close();
		baos.close();
    
		this.bValue = baos.toByteArray();
	}

	public byte[] getValue() {
		return bValue;
	}

	public String getStringValue() throws UnsupportedEncodingException {
		return getStringValue(sEncoding);
	}

	public String getStringValue(String sEncoding) throws UnsupportedEncodingException {
		return new String(bValue, sEncoding);
	}

	/**
	 * Returns <code>true</code> to indicate this part is a parameter.
	 * 
	 * @return true.
	 */
	public boolean isParameter() {
		return true;
	}

}
