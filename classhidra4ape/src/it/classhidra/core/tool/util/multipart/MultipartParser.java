
package it.classhidra.core.tool.util.multipart;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletInputStream;


public class MultipartParser {

	/** input stream to read parts from */
	private ServletInputStream in;
  
	/** MIME boundary that delimits parts */
	private String sBoundary;
  
	/** reference to the last file part we returned */
	private MultipartDataFile lastFilePart;

	/** buffer for readLine method */
	private byte[] buf = new byte[8 * 1024];
  
	/** default encoding */
	private static String DEFAULT_ENCODING = "ISO-8859-1";

	/** preferred encoding */
	private String sEncoding = DEFAULT_ENCODING;

	/**
	 * Creates a <code>MultipartParser</code> from the specified request,
	 * which limits the upload size to the specified length, buffers for 
	 * performance and prevent attempts to read past the amount specified 
	 * by the Content-Length.
	 * 
	 * @param request	the servlet request.
	 * @param iMaxSize	the maximum size of the POST content.
	 */
	public MultipartParser(HttpServletRequest request, int iMaxSize) throws IOException {
		this(request, iMaxSize, true, true);
	}
  
	/**
	 * Creates a <code>MultipartParser</code> from the specified request,
	 * which limits the upload size to the specified length, and optionally 
	 * buffers for performance and prevents attempts to read past the amount 
	 * specified by the Content-Length. 
	 * 
	 * @param req   the servlet request.
	 * @param maxSize the maximum size of the POST content.
	 * @param buffer whether to do internal buffering or let the server buffer,
	 *               useful for servers that don't buffer
	 * @param limitLength boolean flag to indicate if we need to filter 
	 *                    the request's input stream to prevent trying to 
	 *                    read past the end of the stream.
	 */
	public MultipartParser(HttpServletRequest request, int iMaxSize, boolean buffer, boolean limitLength) throws IOException {
		this(request, iMaxSize, buffer, limitLength, null);
	}

	/**
	 * Creates a <code>MultipartParser</code> from the specified request,
	 * which limits the upload size to the specified length, and optionally 
	 * buffers for performance and prevents attempts to read past the amount 
	 * specified by the Content-Length, and with a specified encoding. 
	 * 
	 * @param req   the servlet request.
	 * @param maxSize the maximum size of the POST content.
	 * @param buffer whether to do internal buffering or let the server buffer,
	 *               useful for servers that don't buffer
	 * @param limitLength boolean flag to indicate if we need to filter 
	 *                    the request's input stream to prevent trying to 
	 *                    read past the end of the stream.
	 * @param encoding the encoding to use for parsing, default is ISO-8859-1.
	 */
	public MultipartParser(HttpServletRequest request, int iMaxSize, boolean buffer, boolean limitLength, String sEncoding) throws IOException {

		// First make sure we know the encoding to handle chars correctly.
		// Thanks to Andreas Granzer, andreas.granzer@wave-solutions.com,
		// for pointing out the need to have this in the constructor.
		if (sEncoding != null) {
			setEncoding(sEncoding);
		}

		// Check the content type to make sure it's "multipart/form-data"
		// Access header two ways to work around WebSphere oddities
		String type = null;
		String type1 = request.getHeader("Content-Type");
		String type2 = request.getContentType();
		
		// If one value is null, choose the other value
		if (type1 == null && type2 != null) {
			type = type2;
		}
		else if (type2 == null && type1 != null) {
			type = type1;
		}
		// If neither value is null, choose the longer value
		else if (type1 != null && type2 != null) {
			type = (type1.length() > type2.length() ? type1 : type2);
		}

		if (type == null || !type.toLowerCase().startsWith("multipart/form-data")) {
			throw new IOException("Posted content type isn't multipart/form-data");
		}

		// Check the content length to prevent denial of service attacks
		int length = request.getContentLength();
		if (iMaxSize!=-1 && length > iMaxSize) {
//			throw new IOException("Posted content length of " + length + " exceeds limit of " + iMaxSize);
		}

		// Get the boundary string; it's included in the content type.
		// Should look something like "------------------------12012133613061"
		String sBoundary = extractBoundary(type);
		if (sBoundary == null) {
			throw new IOException("Separation boundary was not specified");
	  	}

		ServletInputStream in = request.getInputStream();
    
		// If required, wrap the real input stream with classes that 
		// "enhance" its behaviour for performance and stability
//		if (buffer) {
//			in = new BufferedServletInputStream(in);
//		}
//		if (limitLength) {
//			in = new LimitedServletInputStream(in, length);
//	  	}

		// Save our values for later
		this.in = in;
		this.sBoundary = sBoundary;
    
		// Read until we hit the boundary
		// Some clients send a preamble (per RFC 2046), so ignore that
		// Thanks to Ben Johnson, ben.johnson@merrillcorp.com, for pointing out
		// the need for preamble support.
		do {
			String line = readLine();
			if (line == null) {
		  		throw new IOException("Corrupt form data: premature ending");
			}
			// See if this line is the boundary, and if so break
			if (line.startsWith(sBoundary)) {
		  		break;  // success
			}
		} while (true);
	}

	/**
	 * Sets the encoding used to parse from here onward.  The default is
	 * ISO-8859-1.  Encodings are actually best passed into the contructor,
	 * so even the initial line reads are correct.
	 *
	 * @param encoding The encoding to use for parsing
	 */
	 public void setEncoding(String sEncoding) {
		this.sEncoding = sEncoding;
	 }

	/**
	 * Read the next part arriving in the stream. Will be either a 
	 * <code>FilePart</code> or a <code>ParamPart</code>, or <code>null</code>
	 * to indicate there are no more parts to read. The order of arrival 
	 * corresponds to the order of the form elements in the submitted form.
	 * 
	 * @return either a <code>FilePart</code>, a <code>ParamPart</code> or
	 *        <code>null</code> if there are no more parts to read.
	 * @exception IOException	if an input or output exception has occurred.
	 * 
	 * @see FilePart
	 * @see ParamPart
	 */
	public MultipartData readNextPart() throws IOException {
		
		// Make sure the last file was entirely read from the input
		if (lastFilePart != null) {
			lastFilePart.getInputStream().close();
			lastFilePart = null;
	  	}
    
		// Read the headers; they look like this (not all may be present):
		// Content-Disposition: form-data; name="field1"; filename="file1.txt"
		// Content-Type: type/subtype
		// Content-Transfer-Encoding: binary
		Vector headers = new Vector();

		String line = readLine();
		if (line == null) {
			// No parts left, we're done
			return null;
		}
		else if (line.length() == 0) {
			// IE4 on Mac sends an empty line at the end; treat that as the end.
			// Thanks to Daniel Lemire and Henri Tourigny for this fix.
			return null;
		}

		// Read the following header lines we hit an empty line
		// A line starting with whitespace is considered a continuation;
		// that requires a little special logic.  Thanks to Nic Ferrier for
		// identifying a good fix.
		while (line != null && line.length() > 0) {
			String nextLine = null;
			boolean getNextLine = true;
			while (getNextLine) {
				nextLine = readLine();
				if (nextLine != null && (nextLine.startsWith(" ") || nextLine.startsWith("\t"))) {
					line = line + nextLine;
				}
				else {
					getNextLine = false;
				}
			}
			// Add the line to the header list
			headers.addElement(line);
			line = nextLine;
		}

		// If we got a null above, it's the end
		if (line == null) {
			return null;
		}

		String name = null;
		String filename = null;
		String origname = null;
		String contentType = "text/plain";  // rfc1867 says this is the default

		Enumeration en = headers.elements();
		while (en.hasMoreElements()) {
			String headerline = (String) en.nextElement();
			if (headerline.toLowerCase().startsWith("content-disposition:")) {
				// Parse the content-disposition line
				String[] dispInfo = extractDispositionInfo(headerline);
				// String disposition = dispInfo[0];  // not currently used
				name = dispInfo[1];
				filename = dispInfo[2];
				origname = dispInfo[3];
			}
			else if (headerline.toLowerCase().startsWith("content-type:")) {
				// Get the content type, or null if none specified
				String type = extractContentType(headerline);
				if (type != null) {
					contentType = type;
				}
			}
		}

		// Now, finally, we read the content (end after reading the boundary)
		if (filename == null) {
			// This is a parameter, add it to the vector of values
			// The encoding is needed to help parse the value
			return new MultipartDataParameter(name, in, sBoundary, sEncoding);
		}
		else {
			// This is a file
			if (filename.equals("")) {
				filename = null; // empty filename, probably an "empty" file param
			}
			lastFilePart = new MultipartDataFile(name, in, sBoundary, contentType, filename, origname);
			return lastFilePart;
		}
	}
  
	/**
	 * Extracts and returns the boundary token from a line.
	 * 
	 * @return the boundary token.
	 */
	private String extractBoundary(String sLine) {
		
		// Use lastIndexOf() because IE 4.01 on Win98 has been known to send the
		// "boundary=" string multiple times.  Thanks to David Wall for this fix.
		int iIndex = sLine.lastIndexOf("boundary=");
		if (iIndex == -1) {
			return null;
		}
		String sBoundary = sLine.substring(iIndex + 9);  // 9 for "boundary="
		if (sBoundary.charAt(0) == '"') {
			// The boundary is enclosed in quotes, strip them
			iIndex = sBoundary.lastIndexOf('"');
			sBoundary = sBoundary.substring(1, iIndex);
		}
		
		// The real boundary is always preceeded by an extra "--"
		sBoundary = "--" + sBoundary;
	
		return sBoundary;
	}

	/**
	 * Extracts and returns disposition info from a line, as a <code>String<code>
	 * array with elements: disposition, name, filename.
	 * 
	 * @return String[] of elements: disposition, name, filename.
	 * @exception  IOException if the line is malformatted.
	 */
	private String[] extractDispositionInfo(String line) throws IOException {
		
		// Return the line's data as an array: disposition, name, filename
		String[] retval = new String[4];

		// Convert the line to a lowercase string without the ending \r\n
	  // Keep the original line for error messages and for variable names.
	  String origline = line;
	  line = origline.toLowerCase();

	  // Get the content disposition, should be "form-data"
	  int start = line.indexOf("content-disposition: ");
	  int end = line.indexOf(";");
	  if (start == -1 || end == -1) {
		throw new IOException("Content disposition corrupt: " + origline);
	  }
	  String disposition = line.substring(start + 21, end);
	  if (!disposition.equals("form-data")) {
		throw new IOException("Invalid content disposition: " + disposition);
	  }

	  // Get the field name
	  start = line.indexOf("name=\"", end);  // start at last semicolon
	  end = line.indexOf("\"", start + 7);   // skip name=\"
	  int startOffset = 6;
	  if (start == -1 || end == -1) {
		// Some browsers like lynx don't surround with ""
		// Thanks to Deon van der Merwe, dvdm@truteq.co.za, for noticing
		start = line.indexOf("name=", end);
		end = line.indexOf(";", start + 6);
		if (start == -1) {
		  throw new IOException("Content disposition corrupt: " + origline);
		}
		else if (end == -1) {
		  end = line.length();
		}
		startOffset = 5;  // without quotes we have one fewer char to skip
	  }
	  String name = origline.substring(start + startOffset, end);

	  // Get the filename, if given
	  String filename = null;
	  String origname = null;
	  start = line.indexOf("filename=\"", end + 2);  // start after name
	  end = line.indexOf("\"", start + 10);          // skip filename=\"
	  if (start != -1 && end != -1) {                // note the !=
		filename = origline.substring(start + 10, end);
		origname = filename;
		// The filename may contain a full path.  Cut to just the filename.
		int slash =
		  Math.max(filename.lastIndexOf('/'), filename.lastIndexOf('\\'));
		if (slash > -1) {
		  filename = filename.substring(slash + 1);  // past last slash
		}
	  }

	  // Return a String array: disposition, name, filename
	  // empty filename denotes no file posted!
	  retval[0] = disposition;
	  retval[1] = name;
	  retval[2] = filename;
	  retval[3] = origname;
	  return retval;
	}

	/**
	 * Extracts and returns the content type from a line, or null if the
	 * line was empty.
	 * 
	 * @return content type, or null if line was empty.
	 * @exception  IOException if the line is malformatted.
	 */
	private static String extractContentType(String sLine) throws IOException {
		
		// Convert the line to a lowercase string
		sLine = sLine.toLowerCase();
		
		// Get the content type, if any
		// Note that Opera at least puts extra info after the type, so handle
		// that.  For example:  Content-Type: text/plain; name="foo"
		// Thanks to Leon Poyyayil, leon.poyyayil@trivadis.com, for noticing this.
		int iEnd = sLine.indexOf(";");
		if (iEnd == -1) {
			iEnd = sLine.length();
		}
		
		return sLine.substring(13, iEnd).trim();  // "content-type:" is 13
	}
  
	/**
	 * Read the next line of input.
	 * 
	 * @return					a String containing the next line of input from the stream,
	 * 							or null to indicate the end of the stream.
	 * @exception IOException	if an input or output exception has occurred.
	 */
	private String readLine() throws IOException {
		
		StringBuffer sbLine = new StringBuffer();
		int iLen;

		do {
			iLen = in.readLine(buf, 0, buf.length);  // does +=
			if (iLen != -1) {
				sbLine.append(new String(buf, 0, iLen, sEncoding));
			}
		} while (iLen == buf.length);	// loop only if the buffer was filled

		if (sbLine.length() == 0) {
			return null;  					// nothing read, must be at the end of stream
		}
		
		// Cut off the trailing \n or \r\n
		// It should always be \r\n but IE5 sometimes does just \n
		// Thanks to Luke Blaikie for helping make this work with \n
		iLen = sbLine.length();
		if (iLen >= 2 && sbLine.charAt(iLen - 2) == '\r') {
			sbLine.setLength(iLen - 2);  // cut \r\n
		}
		else if (iLen >= 1 && sbLine.charAt(iLen - 1) == '\n') {
			sbLine.setLength(iLen - 1);  // cut \n
		}

		return sbLine.toString();
	}

}
